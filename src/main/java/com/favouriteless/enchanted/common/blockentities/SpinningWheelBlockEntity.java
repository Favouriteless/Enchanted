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

import com.favouriteless.enchanted.api.altar.AltarPowerHelper;
import com.favouriteless.enchanted.api.altar.IAltarPowerConsumer;
import com.favouriteless.enchanted.common.init.EnchantedBlockEntityTypes;
import com.favouriteless.enchanted.common.init.EnchantedRecipeTypes;
import com.favouriteless.enchanted.common.menus.SpinningWheelMenu;
import com.favouriteless.enchanted.common.recipes.SpinningWheelRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class SpinningWheelBlockEntity extends ProcessingBlockEntityBase implements IAltarPowerConsumer {

	private SpinningWheelRecipe currentRecipe;
	private final List<BlockPos> potentialAltars = new ArrayList<>();
	private boolean isSpinning = false;

	private static final int[] SLOTS_FOR_OTHER = new int[]{0, 1, 2};
	private static final int[] SLOTS_FOR_DOWN = new int[]{3};

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
		super(EnchantedBlockEntityTypes.SPINNING_WHEEL.get(), pos, state, 4);
	}

	public static <T extends BlockEntity> void tick(Level level, BlockPos blockPos, BlockState blockState, T t) {
		if(t instanceof SpinningWheelBlockEntity be) {
			if(level != null) {
				if(!level.isClientSide) {
					AltarBlockEntity altar = AltarPowerHelper.tryGetAltar(level, be.potentialAltars);
					be.matchRecipe();

					if(be.canSpin() && be.currentRecipe.getPower() > 0 && altar != null) {
						double powerThisTick = (double)be.currentRecipe.getPower() / SPIN_TIME_TOTAL;
						if(altar.currentPower > powerThisTick) {
							altar.currentPower -= powerThisTick;
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
		inventoryContents.insertItem(3, currentRecipe.assemble(recipeWrapper), false);

		for(ItemStack recipeStack : currentRecipe.getItemsIn()) {
			for (int i = 0; i < 3; i++) {
				if(ItemStack.isSameItemSameTags(recipeStack, inventoryContents.getStackInSlot(i))) {
					inventoryContents.extractItem(i, recipeStack.getCount(), false);
					break;
				}
			}
		}
	}

	protected boolean canSpin() {
		if(currentRecipe != null) {
			ItemStack itemStack = inventoryContents.getStackInSlot(3);
			if(itemStack.isEmpty()) // Output is empty
				return true;

			if(currentRecipe.getResultItem().sameItem(itemStack)) // Output same item & fits within stack
				if(itemStack.getOrCreateTag().equals(currentRecipe.getResultItem().getOrCreateTag()))
					return itemStack.getCount() < itemStack.getMaxStackSize();
		}
		return false;
	}

	private void matchRecipe() {
		if (level != null)
			if(currentRecipe == null || !currentRecipe.matches(recipeWrapper, level))
				currentRecipe = level.getRecipeManager().getRecipeFor(EnchantedRecipeTypes.SPINNING_WHEEL, recipeWrapper, level).orElse(null);
	}

	@Override
	public void saveAdditional(CompoundTag nbt) {
		super.saveAdditional(nbt);
		AltarPowerHelper.savePosTag(potentialAltars, nbt);
		nbt.putInt("spinTime", spinTime);
	}

	@Override
	public void load(CompoundTag nbt) {
		super.load(nbt);
		AltarPowerHelper.loadPosTag(potentialAltars, nbt);
		spinTime = nbt.getInt("spinTime");
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

	@Override
	public ContainerData getData() {
		return data;
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
	public List<BlockPos> getAltarPositions() {
		return potentialAltars;
	}

	@Override
	public void removeAltar(BlockPos altarPos) {
		potentialAltars.remove(altarPos);
		setChanged();
	}

	@Override
	public void addAltar(BlockPos altarPos) {
		AltarPowerHelper.addAltarByClosest(potentialAltars, level, worldPosition, altarPos);
		setChanged();
	}

}
