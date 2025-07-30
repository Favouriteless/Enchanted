package net.favouriteless.enchanted.common.blocks.entity;

import net.favouriteless.enchanted.api.power.IPowerConsumer;
import net.favouriteless.enchanted.api.power.IPowerProvider;
import net.favouriteless.enchanted.api.power.PowerHelper;
import net.favouriteless.enchanted.client.client_handlers.block_entities.CauldronClientHandler;
import net.favouriteless.enchanted.client.particles.types.ColourOptions;
import net.favouriteless.enchanted.common.ServerConfig;
import net.favouriteless.enchanted.common.altar.SimplePowerPosHolder;
import net.favouriteless.enchanted.common.init.EParticleTypes;
import net.favouriteless.enchanted.common.init.ETags.Blocks;
import net.favouriteless.enchanted.common.recipes.CauldronTypeRecipe;
import net.favouriteless.enchanted.common.recipes.recipe_inputs.ListInput;
import net.favouriteless.enchanted.common.util.ItemUtils;
import net.favouriteless.enchanted.common.util.RandomUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public abstract class CauldronBlockEntity<T extends CauldronTypeRecipe> extends ContainerBlockEntityBase implements IPowerConsumer, WorldlyContainer {

	private final SimplePowerPosHolder posHolder;

	private final int fluidCapacity;
	private int fluidAmount = 0;

	private static final int WARMING_MAX = 80;
	private static final int BLENDING_MILLISECONDS = 500;
	private final int cookDuration;

	private final List<T> potentialRecipes = new ArrayList<>();

	protected ItemStack itemOut = ItemStack.EMPTY;
	protected int cookProgress = 0;
	protected int warmingUp = 0;
	public boolean isFailed = false;
	public boolean isComplete = false;

	private boolean firstTick = true;

	// Only needed client side (rendering)
	private boolean hasItems = false;

	private int targetRed = 63;
	private int targetGreen = 118;
	private int targetBlue = 228;
	private int startRed = targetRed;
	private int startGreen = targetGreen;
	private int startBlue = targetBlue;
	public long startTime = System.currentTimeMillis();

	public CauldronBlockEntity(BlockEntityType<? extends CauldronBlockEntity<?>> type, BlockPos pos, BlockState state, int bucketCapacity, int cookDuration) {
		super(type, pos, state, NonNullList.create());
		this.posHolder = new SimplePowerPosHolder(pos);
		this.fluidCapacity = bucketCapacity * 1000;
		this.cookDuration = cookDuration;
	}

	public static <T extends BlockEntity> void serverTick(Level level, BlockPos pos, BlockState state, T t) {
		CauldronBlockEntity<?> be = (CauldronBlockEntity<?>)t;
		if(be.firstTick)
			be.firstTick();
		if(be.isFailed || be.isComplete)
			return;

		BlockState stateBelow = level.getBlockState(be.worldPosition.below());
		if(!providesHeat(stateBelow) || be.fluidAmount != be.fluidCapacity) { // If cauldron not heated or full, fail/return.
			boolean update = be.cookProgress > 0 || be.warmingUp > 0;
			be.cookProgress = 0;
			be.warmingUp = 0;
			if(!be.inventory.isEmpty()) {
				be.setFailed();
				be.recalculateTargetColour();
				update = true;
			}
			if(update)
				be.updateBlock();
			return;
		}

		if(be.warmingUp < WARMING_MAX) { // Handle heating up
			if(++be.warmingUp == WARMING_MAX)
				be.updateBlock();
			return;
		}

		if(be.potentialRecipes.size() != 1 || !be.potentialRecipes.get(0).fullMatch(ListInput.of(be.inventory))) // Has final recipe
			return;
		
		if(be.cookProgress < be.cookDuration) {
			be.cookProgress++;
			be.recalculateTargetColour();
			if(be.cookProgress == 1 || be.cookProgress == be.cookDuration)
				be.updateBlock();
		} else {
			CauldronTypeRecipe recipe = be.potentialRecipes.get(0);
			IPowerProvider powerProvider = PowerHelper.tryGetPowerProvider(level, be.getPosHolder());
			if(recipe.getPower() <= 0)
				be.setComplete();
			else if(powerProvider != null && powerProvider.tryConsume(recipe.getPower()))
				be.setComplete();
			else
				be.setFailed(); // Fail if not enough power
			be.updateBlock();
		}
	}

	public static <T extends BlockEntity> void clientTick(Level level, BlockPos pos, BlockState state, T t) {
		CauldronBlockEntity<?> be = (CauldronBlockEntity<?>)t;
		if(be.firstTick)
			be.firstTick();

		long time = System.currentTimeMillis() - be.startTime;
		double waterY = be.getWaterY(state);

		if(be.isHot() && RandomUtils.nextInt(10) > 2) {
			double dx = pos.getX() + 0.5D + (Math.random() - 0.5D) * be.getWaterWidth();
			double dy = pos.getY() + waterY + 0.02D;
			double dz = pos.getZ() + 0.5D + (Math.random() - 0.5D) * be.getWaterWidth();

			level.addParticle(new ColourOptions(EParticleTypes.BOILING.get(), FastColor.ARGB32.color(be.getRed(time), be.getGreen(time), be.getBlue(time))), dx, dy, dz, 0, 0, 0);
		}

		if(be.isFailed)
			return;

		if(!be.isComplete && be.cookProgress > 0 && be.cookProgress < be.cookDuration)
			be.handleCookParticles(time);
		else if(be.warmingUp == WARMING_MAX && be.hasItems && RandomUtils.nextInt(10) > 6) {
			double xOffset = 0.5D + (Math.random() - 0.5D) * be.getWaterWidth();
			double zOffset = 0.5D + (Math.random() - 0.5D) * be.getWaterWidth();
			double dx = be.worldPosition.getX() + xOffset;
			double dy = be.worldPosition.getY() + waterY;
			double dz = be.worldPosition.getZ() + zOffset;
			Vec3 velocity = new Vec3(xOffset, 0, zOffset).subtract(0.5D, 0.0D, 0.5D).normalize().scale((1D + Math.random()) * 0.06D);

			level.addParticle(new ColourOptions(EParticleTypes.CAULDRON_BREW.get(), FastColor.ARGB32.color(be.getRed(time), be.getGreen(time), be.getBlue(time))), dx, dy, dz, velocity.x, (1.0D + Math.random()) * 0.06D, velocity.z);
		}
	}

	public double getWaterY(BlockState state) {
		return getWaterStartY(state) + (getWaterMaxHeight() * fluidAmount / fluidCapacity);
	}

	public abstract double getWaterStartY(BlockState state);

	public abstract double getWaterMaxHeight();

	public abstract double getWaterWidth();

	public abstract void handleCookParticles(long time);

	public int getRed(long time) {
		return (int)Math.round(Mth.lerp(Math.min((double)time / BLENDING_MILLISECONDS, 1.0D), startRed, targetRed));
	}

	public int getGreen(long time) {
		return (int)Math.round(Mth.lerp(Math.min((double)time / BLENDING_MILLISECONDS, 1.0D), startGreen, targetGreen));
	}

	public int getBlue(long time) {
		return (int)Math.round(Mth.lerp(Math.min((double)time / BLENDING_MILLISECONDS, 1.0D), startBlue, targetBlue));
	}

	private void setFailed() {
		itemOut = ItemStack.EMPTY;
		resetValues();
		isFailed = true;
		recalculateTargetColour();
	}

	private void setComplete() {
		itemOut = potentialRecipes.get(0).getResultItem(level.registryAccess()).copy();
		resetValues();
		isComplete = true;
		recalculateTargetColour();
	}

	public void takeContents(Player player) {
		if(level != null && !level.isClientSide) {
			if(isFailed) {
				setWater(0);
				level.playSound(null, worldPosition, SoundEvents.BUCKET_EMPTY, SoundSource.PLAYERS, 1.0F, 1.0F);
			}
			else {
				fluidAmount -= fluidCapacity / potentialRecipes.getFirst().getResultItem(level.registryAccess()).getCount() + 1;
				if(fluidAmount < 0)
					fluidAmount = 0;
			}

			if(!itemOut.isEmpty()) {
				if(player != null)
					ItemUtils.giveOrDrop(player, isFailed ? new ItemStack(Items.WATER_BUCKET) : new ItemStack(itemOut.getItem()));
				else
					level.addFreshEntity(new ItemEntity(level, worldPosition.getX(), worldPosition.getY() + 1, worldPosition.getZ(), new ItemStack(itemOut.getItem())));

				level.playSound(null, worldPosition, SoundEvents.BUCKET_EMPTY, SoundSource.PLAYERS, 1.0F, 1.0F);
			}
			itemOut.shrink(1);
			if(itemOut.isEmpty()) {
				inventory.clear();
				potentialRecipes.clear();

				resetValues();
			}
			recalculateTargetColour();
			updateBlock();
		}
	}

	private void resetValues() {
		isFailed = false;
		isComplete = false;
		cookProgress = 0;
	}

	public void addItem(ItemEntity itemEntity) {
		if(itemEntity.isAlive()) {
			inventory.add(itemEntity.getItem());
			matchRecipes();

			if(potentialRecipes.isEmpty()) {
				if(ServerConfig.INSTANCE.cauldronItemSpoil.get())
					setFailed();
				else {
					inventory.remove(itemEntity.getItem());
					matchRecipes();
					return;
				}
			}

			if(level != null) level.playSound(null, worldPosition, SoundEvents.BUCKET_EMPTY, SoundSource.PLAYERS, 1.0F, 1.0F);
			itemEntity.kill();
			recalculateTargetColour();
			updateBlock();
		}
	}

	public boolean addWater(int amount) {
		if(!isComplete && !isFailed) {
			if (fluidAmount < fluidCapacity) {
				fluidAmount = Mth.clamp(fluidAmount + amount, 0, fluidCapacity);
				setChanged();
				recalculateTargetColour();
				updateBlock();
				return true;
			}
		}
		return false;
	}

	public boolean removeWater(int amount) {
		if(!isComplete && !isFailed) {
			if (fluidAmount >= amount) {
				fluidAmount -= amount;
				setChanged();
				updateBlock();
				return true;
			}
		}
		return false;
	}

	public void setWater(int amount) {
		fluidAmount = Math.min(amount, fluidCapacity);
		setChanged();
		updateBlock();
	}

	public int getWater() {
		return fluidAmount;
	}

	public boolean isFull() {
		return fluidAmount >= fluidCapacity;
	}

	protected abstract void matchRecipes();

	public static boolean providesHeat(BlockState state) {
		return state.is(Blocks.HEAT_SOURCES) && (!state.getValues().containsKey(BlockStateProperties.LIT) || state.getValue(BlockStateProperties.LIT));
	}

	private void recalculateTargetColour() {
		if(inventory.isEmpty()) {
			targetRed = 63;
			targetGreen = 118;
			targetBlue = 228;
		}
		else if(isComplete) {
			int colour = potentialRecipes.get(0).getFinalColour();
			targetRed = FastColor.ARGB32.red(colour);
			targetGreen = FastColor.ARGB32.green(colour);
			targetBlue = FastColor.ARGB32.blue(colour);
		}
		else if(isFailed) {
			targetRed = 150;
			targetGreen = 100;
			targetBlue = 47;
		}
		else if(!potentialRecipes.isEmpty() && cookProgress > 0) {
			int colour = potentialRecipes.get(0).getCookColour();
			targetRed = FastColor.ARGB32.red(colour);
			targetGreen = FastColor.ARGB32.green(colour);
			targetBlue = FastColor.ARGB32.blue(colour);
		}
		else {
			targetRed = RandomUtils.nextInt(80);
			targetGreen = RandomUtils.nextInt(80);
			targetBlue = RandomUtils.nextInt(80);
		}
	}

	public boolean isHot() {
		return warmingUp == WARMING_MAX;
	}

	protected void setPotentialRecipes(List<RecipeHolder<T>> potentialRecipes) {
		this.potentialRecipes.clear();
		for(RecipeHolder<T> holder : potentialRecipes) {
			this.potentialRecipes.add(holder.value());
		}
	}

	public void firstTick() {
		firstTick = false;
		if(!level.isClientSide)
			matchRecipes();
		else {
			startRed = targetRed;
			startGreen = targetGreen;
			startBlue = targetBlue;
			CauldronClientHandler.startSound(this);
		}
	}

	@Nullable
	@Override
	public ClientboundBlockEntityDataPacket getUpdatePacket() {
		return ClientboundBlockEntityDataPacket.create(this);
	}

	/**
	 * Save data to a {@link CompoundTag} which is present in both update packets and the default level save.
	 * @param nbt The {@link CompoundTag} to save to.
	 */
	public void saveBase(CompoundTag nbt) {
		nbt.putInt("waterAmount", fluidAmount);
		nbt.putInt("targetRed", targetRed);
		nbt.putInt("targetGreen", targetGreen);
		nbt.putInt("targetBlue", targetBlue);
		nbt.putBoolean("isFailed", isFailed);
		nbt.putBoolean("isComplete", isComplete);
		nbt.putInt("warmingUp", warmingUp);
		nbt.putInt("cookProgress", cookProgress);
	}

	/**
	 * Load data from a {@link CompoundTag} which is present in both update packets and the default level save.
	 * @param nbt The {@link CompoundTag} to load from.
	 */
	public void loadBase(CompoundTag nbt) {
		setWater(nbt.getInt("waterAmount"));
		isFailed = nbt.getBoolean("isFailed");
		isComplete = nbt.getBoolean("isComplete");
		warmingUp = nbt.getInt("warmingUp");
		cookProgress = nbt.getInt("cookProgress");

		int tr = nbt.getInt("targetRed");
		int tg = nbt.getInt("targetGreen");
		int tb = nbt.getInt("targetBlue");

		if(tr != targetRed || tg != targetGreen || tb != targetBlue) { // If colour changed
			updateTargetColour(tr, tg, tb);
		}
	}

	@Override
	public void saveAdditional(@NotNull CompoundTag tag, @NotNull Provider registries) {
		super.saveAdditional(tag, registries);
		saveBase(tag);
		ContainerHelper.saveAllItems(tag, inventory, registries);
		if(itemOut != ItemStack.EMPTY) {
			CompoundTag itemNbt = new CompoundTag();
			itemNbt.putString("item", BuiltInRegistries.ITEM.getKey(itemOut.getItem()).toString());
			itemNbt.putInt("count", itemOut.getCount());
			tag.put("itemOut", itemNbt);
		}
		tag.put("posHolder", posHolder.serialize());
	}

	@Override
	public void loadAdditional(@NotNull CompoundTag tag, @NotNull Provider registries) {
		super.loadAdditional(tag, registries);
		loadBase(tag);

		if(tag.contains("posHolder"))
			posHolder.deserialize(tag.getCompound("posHolder"));

		if(tag.contains("Items")) {
			// Have to load nbt weirdly because inventory is not a fixed size
			ListTag list = tag.getList("Items", 10);
			for(int i = 0; i < list.size(); ++i) {
				CompoundTag compoundnbt = list.getCompound(i);
				inventory.add(ItemStack.parse(registries, compoundnbt).orElse(ItemStack.EMPTY));
			}

			if(tag.contains("itemOut")) {
				CompoundTag itemNbt = (CompoundTag)tag.get("itemOut");
				itemOut = new ItemStack(BuiltInRegistries.ITEM.get(ResourceLocation.parse(itemNbt.getString("item"))), itemNbt.getInt("count"));
			}
		}
		else if(tag.contains("hasItems"))
			hasItems = tag.getBoolean("hasItems");
	}

	@Override
	@NotNull
	public CompoundTag getUpdateTag(Provider registries) {
		CompoundTag nbt = new CompoundTag();
		saveBase(nbt);
		nbt.putBoolean("hasItems", !inventory.isEmpty());
		return nbt;
	}

	public void updateTargetColour(int red, int green, int blue) {
		long time = System.currentTimeMillis();
		long timeSince = time - startTime;
		startRed = getRed(timeSince);
		startGreen = getGreen(timeSince);
		startBlue = getBlue(timeSince);
		targetRed = red;
		targetGreen = green;
		targetBlue = blue;
		startTime = time;
	}

	@Override
	public NonNullList<ItemStack> getDroppableInventory() {
		return NonNullList.create();
	}

	@Override
	@NotNull
	public IPowerConsumer.IPowerPosHolder getPosHolder() {
		return posHolder;
	}

	@Override
	public int[] getSlotsForFace(Direction direction) {
		return new int[0];
	}

	@Override
	public boolean canPlaceItemThroughFace(int i, ItemStack itemStack, @Nullable Direction direction) {
		return false;
	}

	@Override
	public boolean canTakeItemThroughFace(int i, ItemStack itemStack, Direction direction) {
		return false;
	}

}
