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

import com.favouriteless.enchanted.api.altar.AltarPowerHelper;
import com.favouriteless.enchanted.api.altar.IAltarPowerConsumer;
import com.favouriteless.enchanted.common.containers.SpinningWheelContainer;
import com.favouriteless.enchanted.common.init.EnchantedBlockEntityTypes;
import com.favouriteless.enchanted.common.recipes.SpinningWheelRecipe;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.core.NonNullList;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class SpinningWheelBlockEntity extends ProcessingBlockEntityBase implements IAltarPowerConsumer {

	private SpinningWheelRecipe currentRecipe;
	private final List<BlockPos> potentialAltars = new ArrayList<>();
	private boolean isSpinning = false;

	public static final int COOK_TIME_TOTAL = 400;
	private int cookTime = 0;
	public ContainerData data = new ContainerData() {
		public int get(int index) {
			return switch(index) {
				case 0 -> cookTime;
				case 1 -> COOK_TIME_TOTAL;
				default -> 0;
			};
		}

		public void set(int index, int value) {
			if(index == 0) {
				cookTime = value;
			}
		}

		@Override
		public int getCount() {
			return 2;
		}

	};

	public SpinningWheelBlockEntity(BlockPos pos, BlockState state) {
		super(EnchantedBlockEntityTypes.SPINNING_WHEEL.get(), pos, state, NonNullList.withSize(4, ItemStack.EMPTY));
	}

	@Override
	public void saveAdditional(CompoundTag nbt) {
		super.saveAdditional(nbt);
		AltarPowerHelper.savePosTag(potentialAltars, nbt);
		nbt.putInt("cookTime", cookTime);
	}

	@Override
	public void load(CompoundTag nbt) {
		super.load(nbt);
		AltarPowerHelper.loadPosTag(potentialAltars, nbt);
		cookTime = nbt.getInt("cookTime");
	}

	public static <T extends BlockEntity> void tick(Level level, BlockPos blockPos, BlockState blockState, T t) {
		if(t instanceof SpinningWheelBlockEntity blockEntity) {

			if(level != null) {
				if(!level.isClientSide) {
					blockEntity.matchRecipe();
					AltarBlockEntity altar = AltarPowerHelper.tryGetAltar(level, blockEntity.potentialAltars);

					if(blockEntity.canSpin(blockEntity.currentRecipe) && blockEntity.currentRecipe.getPower() > 0 && altar != null) {
						double powerThisTick = (double)blockEntity.currentRecipe.getPower() / COOK_TIME_TOTAL;
						if(altar.currentPower > powerThisTick) {
							altar.currentPower -= powerThisTick;
							blockEntity.cookTime++;

							if(blockEntity.cookTime == COOK_TIME_TOTAL) {
								blockEntity.cookTime = 0;
								blockEntity.spin();
							}
						}
					}
					else {
						blockEntity.cookTime = 0;
					}

					blockEntity.updateBlock();
				}
				else {
					if(blockEntity.isSpinning)
						blockEntity.cookTime++;
					else
						blockEntity.cookTime = 0;
				}
			}
		}
	}

	protected void spin() {
		currentRecipe.assemble(this);

		for(ItemStack item : currentRecipe.getItemsIn()) {
			for (int i = 0; i < inventoryContents.size()-1; i++) {
				ItemStack stack = inventoryContents.get(i);
				if(item.getItem() == stack.getItem()) {
					stack.shrink(item.getCount());
					break;
				}
			}
		}
	}

	protected boolean canSpin(SpinningWheelRecipe recipeIn) {
		if(recipeIn != null) {
			ItemStack itemStack = inventoryContents.get(3);
			if(itemStack.isEmpty())
				return true;

			if(recipeIn.getResultItem().sameItem(itemStack))
				if(itemStack.getOrCreateTag().equals(recipeIn.getResultItem().getOrCreateTag()))
					return itemStack.getCount() < itemStack.getMaxStackSize();
		}
		return false;
	}

	private void matchRecipe() {
		if (level != null) {
			currentRecipe = level.getRecipeManager()
					.getRecipes()
					.stream()
					.filter(recipe -> recipe instanceof SpinningWheelRecipe)
					.map(recipe -> (SpinningWheelRecipe) recipe)
					.filter(this::matchRecipe)
					.findFirst()
					.orElse(null);
		}
	}

	private boolean matchRecipe(SpinningWheelRecipe recipe) {
		return recipe.matches(this, level);
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
		nbt.putBoolean("isSpinning", cookTime > 0);
		return nbt;
	}

	@Override
	public ContainerData getData() {
		return data;
	}

	@Override
	protected Component getDefaultName() {
		return new TranslatableComponent("container.enchanted.spinning_wheel");
	}

	@Override
	protected AbstractContainerMenu createMenu(int id, Inventory player) {
		return new SpinningWheelContainer(id, player, this, data);
	}
}
