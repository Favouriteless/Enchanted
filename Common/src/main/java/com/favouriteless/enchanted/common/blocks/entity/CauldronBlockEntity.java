package com.favouriteless.enchanted.common.blocks.entity;

import com.favouriteless.enchanted.Enchanted;
import com.favouriteless.enchanted.api.power.IPowerConsumer;
import com.favouriteless.enchanted.api.power.IPowerProvider;
import com.favouriteless.enchanted.api.power.PowerHelper;
import com.favouriteless.enchanted.client.client_handlers.blockentities.CauldronBlockEntityClientHandler;
import com.favouriteless.enchanted.client.particles.types.SimpleColouredParticleType.SimpleColouredData;
import com.favouriteless.enchanted.common.CommonConfig;
import com.favouriteless.enchanted.common.altar.SimplePowerPosHolder;
import com.favouriteless.enchanted.common.blocks.cauldrons.CauldronBlockBase;
import com.favouriteless.enchanted.common.init.registry.EnchantedParticleTypes;
import com.favouriteless.enchanted.common.recipes.CauldronTypeRecipe;
import com.favouriteless.enchanted.common.util.PlayerInventoryHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public abstract class CauldronBlockEntity<T extends CauldronTypeRecipe> extends ContainerBlockEntityBase implements IPowerConsumer {

	private final SimplePowerPosHolder posHolder;

	private final int fluidCapacity;
	private int fluidAmount = 0;

	private static final int WARMING_MAX = 80;
	private static final int BLENDING_MILLISECONDS = 500;
	private final int cookDuration;

	private List<T> potentialRecipes = new ArrayList<>();

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


	public static <T extends BlockEntity> void tick(Level level, BlockPos blockPos, BlockState blockState, T t) {
		if(t instanceof CauldronBlockEntity<?> be) {
			if(be.firstTick)
				be.firstTick();
			if(!level.isClientSide) {
				boolean shouldUpdate = false;
				if(!be.isFailed && !be.isComplete) {
					BlockState stateBelow = level.getBlockState(be.worldPosition.below());

					if(providesHeat(stateBelow) && be.fluidAmount == be.fluidCapacity) { // On top of heat block, cauldron is full
						if(be.warmingUp < WARMING_MAX) {
							be.warmingUp++;
							if(be.warmingUp == WARMING_MAX)
								shouldUpdate = true;
						} else if(be.potentialRecipes.size() == 1 && be.potentialRecipes.get(0).fullMatch(be, level)) { // Has final recipe
							if(be.cookProgress < be.cookDuration) {
								be.cookProgress++;
								be.recalculateTargetColour();
								if(be.cookProgress == 1 || be.cookProgress == be.cookDuration)
									shouldUpdate = true;
							} else {
								CauldronTypeRecipe recipe = be.potentialRecipes.get(0);
								IPowerProvider powerProvider = PowerHelper.tryGetPowerProvider(level, be.getPosHolder());
								if(recipe.getPower() <= 0)
									be.setComplete();
								else if(powerProvider != null && powerProvider.tryConsumePower(recipe.getPower()))
									be.setComplete();
								else
									be.setFailed(); // Fail if not enough power

								shouldUpdate = true;
							}
						}
					} else {
						if(be.hasItems)
							be.setFailed(); // Fail if heat or water is lost while cooking
						if(be.warmingUp > 0)
							shouldUpdate = true;
						be.warmingUp = 0;
					}
				}
				if(shouldUpdate)
					be.updateBlock();
			}
			// ------------------------ CLIENT STUFF ------------------------
			else {
				if(blockState.getBlock() instanceof CauldronBlockBase) {
					long time = System.currentTimeMillis() - be.startTime;
					double waterY = be.getWaterY(blockState);

					if(be.isHot() && Enchanted.RANDOM.nextInt(10) > 2) {
						double dx = be.worldPosition.getX() + 0.5D + (Math.random() - 0.5D) * be.getWaterWidth();
						double dy = be.worldPosition.getY() + waterY + 0.02D;
						double dz = be.worldPosition.getZ() + 0.5D + (Math.random() - 0.5D) * be.getWaterWidth();

						level.addParticle(new SimpleColouredData(EnchantedParticleTypes.BOILING.get(), be.getRed(time), be.getGreen(time), be.getBlue(time)), dx, dy, dz, 0D, 0D, 0D);
					}
					if(!be.isFailed) {
						if(!be.isComplete && be.cookProgress > 0 && be.cookProgress < be.cookDuration) {
							be.handleCookParticles(time);
						} else if(be.warmingUp == WARMING_MAX && be.hasItems && Enchanted.RANDOM.nextInt(10) > 6) {
							double xOffset = 0.5D + (Math.random() - 0.5D) * be.getWaterWidth();
							double zOffset = 0.5D + (Math.random() - 0.5D) * be.getWaterWidth();
							double dx = be.worldPosition.getX() + xOffset;
							double dy = be.worldPosition.getY() + waterY;
							double dz = be.worldPosition.getZ() + zOffset;
							Vec3 velocity = new Vec3(xOffset, 0, zOffset).subtract(0.5D, 0.0D, 0.5D).normalize().scale((1D + Math.random()) * 0.06D);

							level.addParticle(new SimpleColouredData(EnchantedParticleTypes.CAULDRON_BREW.get(), be.getRed(time), be.getGreen(time), be.getBlue(time)), dx, dy, dz, velocity.x, (1.0D + Math.random()) * 0.06D, velocity.z);
						}
					}
				}
			}
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
		itemOut = potentialRecipes.get(0).getResultItem().copy();
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
			else
				fluidAmount -= fluidCapacity / potentialRecipes.get(0).getResultItem().getCount() + 1;

			if(!itemOut.isEmpty()) {
				if(player != null)
					PlayerInventoryHelper.tryGiveItem(player, isFailed ? new ItemStack(Items.WATER_BUCKET) : new ItemStack(itemOut.getItem()));
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

			if (potentialRecipes.isEmpty()) {
				if(CommonConfig.CAULDRON_ITEM_SPOIL.get())
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
		fluidAmount = amount;
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

	public static boolean providesHeat(BlockState state){
		return  state.getBlock() == Blocks.FIRE ||
				state.getBlock() == Blocks.SOUL_FIRE ||
				state.getBlock() == Blocks.LAVA ||
				state.getBlock() == Blocks.CAMPFIRE && state.getValue(CampfireBlock.LIT) ||
				state.getBlock() == Blocks.SOUL_CAMPFIRE && state.getValue(CampfireBlock.LIT) ||
				state.getBlock() == Blocks.MAGMA_BLOCK;
	}

	private void recalculateTargetColour() {
		if(inventory.isEmpty()) {
			targetRed = 63;
			targetGreen = 118;
			targetBlue = 228;
		}
		else if(isComplete) {
			targetRed = potentialRecipes.get(0).getFinalRed();
			targetGreen = potentialRecipes.get(0).getFinalGreen();
			targetBlue = potentialRecipes.get(0).getFinalBlue();
		}
		else if(isFailed) {
			targetRed = 150;
			targetGreen = 100;
			targetBlue = 47;
		}
		else if(!potentialRecipes.isEmpty() && cookProgress > 0) {
			targetRed = potentialRecipes.get(0).getCookingRed();
			targetGreen = potentialRecipes.get(0).getCookingGreen();
			targetBlue = potentialRecipes.get(0).getCookingBlue();
		}
		else {
			targetRed = Enchanted.RANDOM.nextInt(80);
			targetGreen = Enchanted.RANDOM.nextInt(80);
			targetBlue = Enchanted.RANDOM.nextInt(80);
		}
	}

	public boolean isHot() {
		return warmingUp == WARMING_MAX;
	}

	protected void setPotentialRecipes(List<T> potentialRecipes) {
		this.potentialRecipes = potentialRecipes;
	}

	public void firstTick() {
		firstTick = false;
		if(!level.isClientSide)
			matchRecipes();
		else {
			startRed = targetRed;
			startGreen = targetGreen;
			startBlue = targetBlue;
			CauldronBlockEntityClientHandler.startCauldronBubbling(this);
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
	public void saveAdditional(@NotNull CompoundTag nbt) {
		super.saveAdditional(nbt);
		saveBase(nbt);
		ContainerHelper.saveAllItems(nbt, inventory);
		if(itemOut != ItemStack.EMPTY) {
			CompoundTag itemNbt = new CompoundTag();
			itemNbt.putString("item", Registry.ITEM.getKey(itemOut.getItem()).toString());
			itemNbt.putInt("count", itemOut.getCount());
			nbt.put("itemOut", itemNbt);
		}
		nbt.put("posHolder", posHolder.serialize());
	}

	@Override
	public void load(@NotNull CompoundTag nbt) {
		super.load(nbt);
		loadBase(nbt);

		if(nbt.contains("posHolder"))
			posHolder.deserialize(nbt.getList("posHolder", 10));

		if(nbt.contains("Items")) {
			// Have to load nbt weirdly because inventory is not a fixed size
			ListTag list = nbt.getList("Items", 10);
			for(int i = 0; i < list.size(); ++i) {
				CompoundTag compoundnbt = list.getCompound(i);
				inventory.add(ItemStack.of(compoundnbt));
			}

			if(nbt.contains("itemOut")) {
				CompoundTag itemNbt = (CompoundTag)nbt.get("itemOut");
				itemOut = new ItemStack(Registry.ITEM.get(new ResourceLocation(itemNbt.getString("item"))), itemNbt.getInt("count"));
			}
		}
		else if(nbt.contains("hasItems"))
			hasItems = nbt.getBoolean("hasItems");
	}

	@Override
	@NotNull
	public CompoundTag getUpdateTag() {
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
	@NotNull
	public IPowerConsumer.IPowerPosHolder getPosHolder() {
		return posHolder;
	}

}
