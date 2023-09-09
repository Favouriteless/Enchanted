/*
 *
 *   Copyright (c) 2023. Favouriteless
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

import com.favouriteless.enchanted.api.power.PowerHelper;
import com.favouriteless.enchanted.api.power.IPowerConsumer;
import com.favouriteless.enchanted.common.altar.SimplePowerPosHolder;
import com.favouriteless.enchanted.common.init.registry.EnchantedBlockEntityTypes;
import com.favouriteless.enchanted.common.init.registry.EnchantedRecipeTypes;
import com.favouriteless.enchanted.common.menus.SpinningWheelMenu;
import com.favouriteless.enchanted.common.recipes.SpinningWheelRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class SpinningWheelBlockEntity extends ProcessingBlockEntityBase implements IPowerConsumer, MenuProvider {

	private final ItemStackHandler input = new ItemStackHandler(3);
	private final ItemStackHandler output = new ItemStackHandler(1);

	private final LazyOptional<IItemHandlerModifiable> inputHandler = LazyOptional.of(() -> input);
	private final LazyOptional<IItemHandlerModifiable> outputHandler = LazyOptional.of(() -> output);

	private final RecipeWrapper recipeWrapper = new RecipeWrapper(input);
	private SpinningWheelRecipe currentRecipe;
	private final SimplePowerPosHolder altarPosHolder;
	private boolean isSpinning = false;

	public static final int SPIN_TIME_TOTAL = 400;
	private int spinTime = 0;
	public ContainerData data = new ContainerData() {
		public int get(int index) {
			return switch(index) {
				case 0 -> spinTime;
				case 1 -> SPIN_TIME_TOTAL;
				default -> 0;
			};
		}

		public void set(int index, int value) {
			if(index == 0) {
				spinTime = value;
			}
		}

		@Override
		public int getCount() {
			return 2;
		}

	};

	public SpinningWheelBlockEntity(BlockPos pos, BlockState state) {
		super(EnchantedBlockEntityTypes.SPINNING_WHEEL.get(), pos, state);
		this.altarPosHolder = new SimplePowerPosHolder(pos);
	}

	public static <T extends BlockEntity> void tick(Level level, BlockPos blockPos, BlockState blockState, T t) {
		if(t instanceof SpinningWheelBlockEntity be) {
			if(level != null) {
				if(!level.isClientSide) {
					AltarBlockEntity altar = PowerHelper.tryGetPowerProvider(level, be.altarPosHolder);
					be.matchRecipe();

					if(be.canSpin() && be.currentRecipe.getPower() > 0 && altar != null) {
						double powerThisTick = (double)be.currentRecipe.getPower() / SPIN_TIME_TOTAL;
						if(altar.tryConsumePower(powerThisTick)) {
							be.spinTime++;

							if(be.spinTime == SPIN_TIME_TOTAL) {
								be.spinTime = 0;
								be.spin();
							}
						}
					}
					else
						be.spinTime = 0;

					be.updateBlock();
				}
				else {
					if(be.isSpinning)
						be.spinTime++;
					else
						be.spinTime = 0;
				}
			}
		}
	}

	protected void spin() {
		output.insertItem(0, currentRecipe.assemble(recipeWrapper), false);

		for(ItemStack recipeStack : currentRecipe.getItemsIn()) {
			for (int i = 0; i < input.getSlots(); i++) {
				if(ItemStack.isSameItemSameTags(recipeStack, input.getStackInSlot(i))) {
					input.extractItem(i, recipeStack.getCount(), false);
					break;
				}
			}
		}
	}

	protected boolean canSpin() {
		if(currentRecipe != null) {
			ItemStack outputStack = output.getStackInSlot(0);
			if(outputStack.isEmpty()) // Output is empty
				return true;

			ItemStack resultStack = currentRecipe.getResultItem();
			if(ItemStack.isSameItemSameTags(outputStack, resultStack)) // Output same item & fits within stack
				return outputStack.getCount() + resultStack.getCount() <= outputStack.getMaxStackSize();
		}
		return false;
	}

	private void matchRecipe() {
		if (level != null)
			if(currentRecipe == null || !currentRecipe.matches(recipeWrapper, level))
				currentRecipe = level.getRecipeManager().getRecipeFor(EnchantedRecipeTypes.SPINNING_WHEEL, recipeWrapper, level).orElse(null);
	}

	public ItemStackHandler getInputInventory() {
		return input;
	}

	public ItemStackHandler getOutputInventory() {
		return output;
	}

	@Override
	public ContainerData getData() {
		return data;
	}

	@Override
	public NonNullList<ItemStack> getDroppableInventory() {
		return getDroppableInventoryFor(input, output);
	}

	@Override
	public Component getDisplayName() {
		return new TranslatableComponent("container.enchanted.spinning_wheel");
	}

	@Nullable
	@Override
	public AbstractContainerMenu createMenu(int id, Inventory playerInventory, Player player) {
		return new SpinningWheelMenu(id, playerInventory, this, data);
	}

	@Override
	public void saveAdditional(CompoundTag nbt) {
		super.saveAdditional(nbt);
		nbt.put("altarPos", altarPosHolder.serializeNBT());
		nbt.putInt("spinTime", spinTime);
		nbt.put("input", input.serializeNBT());
		nbt.put("output", output.serializeNBT());
	}

	@Override
	public void load(CompoundTag nbt) {
		super.load(nbt);
		altarPosHolder.deserializeNBT(nbt.getList("altarPos", 10));
		spinTime = nbt.getInt("spinTime");
		input.deserializeNBT(nbt.getCompound("input"));
		output.deserializeNBT(nbt.getCompound("output"));
	}

	@Nullable
	@Override
	public ClientboundBlockEntityDataPacket getUpdatePacket() {
		return ClientboundBlockEntityDataPacket.create(this);
	}

	@Override
	public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
		CompoundTag nbt = pkt.getTag();
		isSpinning = nbt.getBoolean("isSpinning");
	}

	@Override
	public CompoundTag getUpdateTag() {
		CompoundTag nbt = super.getUpdateTag();
		nbt.putBoolean("isSpinning", spinTime > 0);
		return nbt;
	}

	@NotNull
	@Override
	public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
		if(cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			if(side == null)
				return super.getCapability(cap, side);
			else if(side == Direction.DOWN)
				return outputHandler.cast();
			else
				return inputHandler.cast();
		}
		return super.getCapability(cap, side);
	}

	@Override
	public void setRemoved() {
		super.setRemoved();
		if(inputHandler != null)
			inputHandler.invalidate();
		if(outputHandler != null)
			outputHandler.invalidate();
	}

	@Override
	public @NotNull IPowerConsumer.IPowerPosHolder getPosHolder() {
		return altarPosHolder;
	}

}
