/*
 *
 *   Copyright (c) 2022. Favouriteless
 *   Enchanted, a minecraft mod.
 *   GNU GPLv3 License
 *
 *       This file is part of Enchanted.
 *
 *       Enchanted is free software: you can redistribute it and/or modify
 *       it under the terms of the GNU General Public License as published by
 *       the Free Software Foundation, either version 3 of the License, or
 *       (at your option) any later version.
 *
 *       Enchanted is distributed in the hope that it will be useful,
 *       but WITHOUT ANY WARRANTY; without even the implied warranty of
 *       MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *       GNU General Public License for more details.
 *
 *       You should have received a copy of the GNU General Public License
 *       along with Enchanted.  If not, see <https://www.gnu.org/licenses/>.
 *
 *
 */

package com.favouriteless.enchanted.common.blockentities;

import com.favouriteless.enchanted.Enchanted;
import com.favouriteless.enchanted.EnchantedConfig;
import com.favouriteless.enchanted.api.altar.AltarPowerHelper;
import com.favouriteless.enchanted.api.altar.IAltarPowerConsumer;
import com.favouriteless.enchanted.client.particles.SimpleColouredParticleType.SimpleColouredData;
import com.favouriteless.enchanted.common.blocks.cauldrons.CauldronBlockBase;
import com.favouriteless.enchanted.common.init.EnchantedParticles;
import com.favouriteless.enchanted.common.recipes.CauldronTypeRecipe;
import com.favouriteless.enchanted.common.sounds.CauldronBubblingSoundInstance;
import com.favouriteless.enchanted.core.util.PlayerInventoryHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.wrapper.InvWrapper;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public abstract class CauldronBlockEntity<T extends CauldronTypeRecipe> extends RandomizableContainerBlockEntity implements IAltarPowerConsumer {

	private final FluidTank tank;
	private final LazyOptional<IFluidHandler> fluidHandler;

	private NonNullList<ItemStack> inventoryContents = NonNullList.create();
	private final IItemHandlerModifiable items = new InvWrapper(this);
	private final LazyOptional<IItemHandlerModifiable> itemHandler = LazyOptional.of(() -> items);

	private static final int WARMING_MAX = 80;
	private static final int BLENDING_MILLISECONDS = 500;
	private final int cookTime;

	private final List<BlockPos> potentialAltars = new ArrayList<>();
	private List<T> potentialRecipes = new ArrayList<>();

	protected ItemStack itemOut = ItemStack.EMPTY;
	protected int cookProgress = 0;
	protected int warmingUp = 0;
	public boolean isFailed = false;
	public boolean isComplete = false;

	// Only needed client side (rendering)
	private boolean hasItems = false;

	private int targetRed = 63;
	private int targetGreen = 118;
	private int targetBlue = 228;
	private int startRed = targetRed;
	private int startGreen = targetGreen;
	private int startBlue = targetBlue;
	public long startTime = System.currentTimeMillis();

	public CauldronBlockEntity(BlockEntityType<? extends CauldronBlockEntity<?>> type, BlockPos pos, BlockState state, int bucketCapacity, int cookTime) {
		super(type, pos, state);
		tank = new FluidTank(FluidAttributes.BUCKET_VOLUME*bucketCapacity, (fluid) -> fluid.getFluid() == Fluids.WATER);
		fluidHandler = LazyOptional.of(() -> tank);
		this.cookTime = cookTime;
	}


	public static <T extends BlockEntity> void tick(Level level, BlockPos blockPos, BlockState blockState, T t) {
		if(t instanceof CauldronBlockEntity<?> blockEntity) {
			if(level != null) {
				if(!level.isClientSide) {
					boolean shouldUpdate = false;
					if(!blockEntity.isFailed && !blockEntity.isComplete) {

						BlockState stateBelow = level.getBlockState(blockEntity.worldPosition.below());

						if(providesHeat(stateBelow) && blockEntity.tank.getFluidAmount() == blockEntity.tank.getCapacity()) { // On top of heat block, cauldron is full
							if(blockEntity.warmingUp < WARMING_MAX) {
								blockEntity.warmingUp++;
								if(blockEntity.warmingUp == WARMING_MAX)
									shouldUpdate = true;
							}
							else if(blockEntity.potentialRecipes.size() == 1 && blockEntity.potentialRecipes.get(0).fullMatch(blockEntity, level)) { // Has final recipe
								if(blockEntity.cookProgress < blockEntity.cookTime) {
									blockEntity.cookProgress++;
									blockEntity.recalculateTargetColour();
									if(blockEntity.cookProgress == 1 || blockEntity.cookProgress == blockEntity.cookTime)
										shouldUpdate = true;
								}
								else {
									CauldronTypeRecipe recipe = blockEntity.potentialRecipes.get(0);
									AltarBlockEntity altar = AltarPowerHelper.tryGetAltar(level, blockEntity.potentialAltars);
									if(recipe.getPower() <= 0) {
										blockEntity.setComplete();
									}
									else if(altar != null && altar.currentPower > recipe.getPower()) {
										altar.currentPower -= recipe.getPower();
										blockEntity.setComplete();
									}
									else {
										blockEntity.setFailed(); // Fail if not enough power
									}
									shouldUpdate = true;
								}
							}
						}
						else {
							if(blockEntity.hasItems)
								blockEntity.setFailed(); // Fail if heat or water is lost while cooking
							if(blockEntity.warmingUp > 0)
								shouldUpdate = true;
							blockEntity.warmingUp = 0;
						}
					}
					if(shouldUpdate)
						blockEntity.updateBlock();
				}
				// ------------------------ CLIENT STUFF ------------------------
				else {
					if(blockState.getBlock() instanceof CauldronBlockBase) {
						long time = System.currentTimeMillis() - blockEntity.startTime;
						double waterY = blockEntity.getWaterY(blockState);

						if(blockEntity.isHot() && Enchanted.RANDOM.nextInt(10) > 2) {
							double dx = blockEntity.worldPosition.getX() + 0.5D + (Math.random() - 0.5D) * blockEntity.getWaterWidth();
							double dy = blockEntity.worldPosition.getY() + waterY + 0.02D;
							double dz = blockEntity.worldPosition.getZ() + 0.5D + (Math.random() - 0.5D) * blockEntity.getWaterWidth();

							level.addParticle(new SimpleColouredData(EnchantedParticles.BOILING.get(), blockEntity.getRed(time), blockEntity.getGreen(time), blockEntity.getBlue(time)), dx, dy, dz, 0D, 0D, 0D);
						}
						if(!blockEntity.isFailed) {
							if(!blockEntity.isComplete && blockEntity.cookProgress > 0 && blockEntity.cookProgress < blockEntity.cookTime) {
								blockEntity.handleCookParticles(time);
							}
							else if(blockEntity.warmingUp == WARMING_MAX && blockEntity.hasItems && Enchanted.RANDOM.nextInt(10) > 6) {
								double xOffset = 0.5D + (Math.random() - 0.5D) * blockEntity.getWaterWidth();
								double zOffset = 0.5D + (Math.random() - 0.5D) * blockEntity.getWaterWidth();
								double dx = blockEntity.worldPosition.getX() + xOffset;
								double dy = blockEntity.worldPosition.getY() + waterY;
								double dz = blockEntity.worldPosition.getZ() + zOffset;
								Vec3 velocity = new Vec3(xOffset, 0, zOffset).subtract(0.5D, 0.0D, 0.5D).normalize().scale((1D + Math.random()) * 0.06D);

								level.addParticle(new SimpleColouredData(EnchantedParticles.CAULDRON_BREW.get(), blockEntity.getRed(time), blockEntity.getGreen(time), blockEntity.getBlue(time)), dx, dy, dz, velocity.x, (1.0D + Math.random()) * 0.06D, velocity.z);
							}
						}
					}
				}
			}
		}
	}

	public double getWaterY(BlockState state) {
		return getWaterStartY(state) + (getWaterMaxHeight() * tank.getFluidAmount() / tank.getCapacity());
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
			else {
				tank.drain(new FluidStack(Fluids.WATER, tank.getCapacity() / potentialRecipes.get(0).getResultItem().getCount() + 1), IFluidHandler.FluidAction.EXECUTE);
			}
			if(!itemOut.isEmpty()) {
				if(player != null) {
					PlayerInventoryHelper.tryGiveItem(player, isFailed ? new ItemStack(Items.WATER_BUCKET) : new ItemStack(itemOut.getItem()));
				}
				else {
					level.addFreshEntity(new ItemEntity(level, worldPosition.getX(), worldPosition.getY() + 1, worldPosition.getZ(), new ItemStack(itemOut.getItem())));
				}
				level.playSound(null, worldPosition, SoundEvents.BUCKET_EMPTY, SoundSource.PLAYERS, 1.0F, 1.0F);
			}
			itemOut.shrink(1);
			if(itemOut.isEmpty()) {
				inventoryContents.clear();
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
			inventoryContents.add(itemEntity.getItem());
			matchRecipes();

			if (potentialRecipes.isEmpty()) {
				if(EnchantedConfig.CAULDRON_ITEM_SPOIL.get()) {
					setFailed();
				}
				else {
					inventoryContents.remove(itemEntity.getItem());
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
			if (tank.getFluidAmount() < tank.getCapacity()) {
				tank.fill(new FluidStack(Fluids.WATER, amount), IFluidHandler.FluidAction.EXECUTE);
				recalculateTargetColour();
				updateBlock();
				return true;
			}
		}
		return false;
	}

	public boolean removeWater(int amount) {
		if(!isComplete && !isFailed) {
			if (tank.getFluidAmount() >= amount) {
				tank.drain(new FluidStack(Fluids.WATER, amount), IFluidHandler.FluidAction.EXECUTE);
				updateBlock();
				return true;
			}
		}
		return false;
	}

	public void setWater(int amount) {
		tank.setFluid(new FluidStack(Fluids.WATER, amount));
		updateBlock();
	}

	public int getWater() {
		return tank.getFluidAmount();
	}

	public boolean isFull() {
		return tank.getFluidAmount() == tank.getCapacity();
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
		if(inventoryContents.isEmpty()) {
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

	private void updateBlock() {
		if(level != null && !level.isClientSide) {
			BlockState state = level.getBlockState(worldPosition);
			level.sendBlockUpdated(worldPosition, state, state, 2);
		}
	}

	public boolean isHot() {
		return warmingUp == WARMING_MAX;
	}

	protected void setPotentialRecipes(List<T> potentialRecipes) {
		this.potentialRecipes = potentialRecipes;
	}

	@Override
	public void saveAdditional(CompoundTag nbt) {
		super.saveAdditional(nbt);
		nbt.putInt("waterAmount", tank.getFluidAmount());
		nbt.putInt("targetRed", targetRed);
		nbt.putInt("targetGreen", targetGreen);
		nbt.putInt("targetBlue", targetBlue);
		nbt.putBoolean("isFailed", isFailed);
		nbt.putBoolean("isComplete", isComplete);
		nbt.putInt("warmingUp", warmingUp);
		nbt.putInt("cookProgress", cookProgress);
		ContainerHelper.saveAllItems(nbt, inventoryContents);
		if(itemOut != ItemStack.EMPTY) {
			CompoundTag itemNbt = new CompoundTag();
			itemNbt.putString("item", itemOut.getItem().getRegistryName().toString());
			itemNbt.putInt("count", itemOut.getCount());
			nbt.put("itemOut", itemNbt);
		}
		AltarPowerHelper.savePosTag(potentialAltars, nbt);
	}

	@Override
	public void load(CompoundTag nbt) {
		super.load(nbt);
		AltarPowerHelper.loadPosTag(potentialAltars, nbt);
		setWater(nbt.getInt("waterAmount"));
		targetRed = nbt.getInt("targetRed");
		targetGreen = nbt.getInt("targetGreen");
		targetBlue = nbt.getInt("targetBlue");
		isFailed = nbt.getBoolean("isFailed");
		isComplete = nbt.getBoolean("isComplete");
		warmingUp = nbt.getInt("warmingUp");
		cookProgress = nbt.getInt("cookProgress");

		// Have to load nbt weirdly because inventory is not a fixed size
		ListTag listnbt = nbt.getList("Items", 10);
		for(int i = 0; i < listnbt.size(); ++i) {
			CompoundTag compoundnbt = listnbt.getCompound(i);
			inventoryContents.add(ItemStack.of(compoundnbt));
		}

		if(nbt.contains("itemOut")) {
			CompoundTag itemNbt = (CompoundTag)nbt.get("itemOut");
			itemOut = new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemNbt.getString("item"))), itemNbt.getInt("count"));
		}
	}

	@Override
	public void onLoad() {
		super.onLoad();
		if(!level.isClientSide) {
			matchRecipes();
		}
		else {
			startRed = targetRed;
			startGreen = targetGreen;
			startBlue = targetBlue;
			Minecraft.getInstance().getSoundManager().play(new CauldronBubblingSoundInstance(this));
		}
	}

	@Nullable
	@Override
	public ClientboundBlockEntityDataPacket getUpdatePacket() {
		return ClientboundBlockEntityDataPacket.create(this);
	}

	@Override
	public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
		CompoundTag nbt = pkt.getTag();
		handleUpdateTag(nbt);
	}

	@Override
	public CompoundTag getUpdateTag() {
		CompoundTag nbt = new CompoundTag();
		nbt.putInt("waterAmount", tank.getFluidAmount());
		nbt.putInt("targetRed", targetRed);
		nbt.putInt("targetGreen", targetGreen);
		nbt.putInt("targetBlue", targetBlue);
		nbt.putBoolean("isFailed", isFailed);
		nbt.putBoolean("isComplete", isComplete);
		nbt.putInt("warmingUp", warmingUp);
		nbt.putInt("cookProgress", cookProgress);
		nbt.putBoolean("hasItems", !inventoryContents.isEmpty());
		return nbt;
	}

	@Override
	public void handleUpdateTag(CompoundTag nbt) {
		setWater(nbt.getInt("waterAmount"));
		isFailed = nbt.getBoolean("isFailed");
		isComplete = nbt.getBoolean("isComplete");
		warmingUp = nbt.getInt("warmingUp");
		cookProgress = nbt.getInt("cookProgress");
		hasItems = nbt.getBoolean("hasItems");

		int tr = nbt.getInt("targetRed");
		int tg = nbt.getInt("targetGreen");
		int tb = nbt.getInt("targetBlue");

		if(tr != targetRed || tg != targetGreen || tb != targetBlue) { // If colour changed
			updateTargetColour(tr, tg, tb);
		}
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
	@Nonnull
	public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction facing) {
		if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
			return fluidHandler.cast();
		else if(capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
			return itemHandler.cast();
		return super.getCapability(capability, facing);
	}

	@Override
	protected NonNullList<ItemStack> getItems() {
		return inventoryContents;
	}

	@Override
	protected void setItems(NonNullList<ItemStack> pItems) {
		inventoryContents = pItems;
	}

	@Override
	protected AbstractContainerMenu createMenu(int pId, Inventory pPlayer) {
		return null;
	}

	@Override
	public int getContainerSize() {
		return inventoryContents.size();
	}

	@Override
	public List<BlockPos> getAltarPositions() {
		return potentialAltars;
	}

	@Override
	public void removeAltar(BlockPos altarPos) {
		potentialAltars.remove(altarPos);
		this.setChanged();
	}

	@Override
	public void addAltar(BlockPos altarPos) {
		AltarPowerHelper.addAltarByClosest(potentialAltars, level, worldPosition, altarPos);
		this.setChanged();
	}
}
