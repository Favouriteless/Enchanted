/*
 * Copyright (c) 2022. Favouriteless
 * Enchanted, a minecraft mod.
 * GNU GPLv3 License
 *
 *     This file is part of Enchanted.
 *
 *     Enchanted is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Enchanted is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Enchanted.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.favouriteless.enchanted.common.tileentity;

import com.favouriteless.enchanted.api.altar.AltarPowerHelper;
import com.favouriteless.enchanted.api.altar.IAltarPowerConsumer;
import com.favouriteless.enchanted.common.blocks.SpinningWheelBlock;
import com.favouriteless.enchanted.common.containers.SpinningWheelContainer;
import com.favouriteless.enchanted.common.init.EnchantedTileEntities;
import com.favouriteless.enchanted.common.recipes.SpinningWheelRecipe;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.ArrayList;
import java.util.List;

public class SpinningWheelTileEntity extends FurnaceTileEntityBase implements IAltarPowerConsumer {

	private SpinningWheelRecipe currentRecipe;
	private final List<BlockPos> potentialAltars = new ArrayList<>();

	public SpinningWheelTileEntity(TileEntityType<?> typeIn) {
		super(typeIn, NonNullList.withSize(4, ItemStack.EMPTY));
	}

	public SpinningWheelTileEntity() {
		this(EnchantedTileEntities.SPINNING_WHEEL.get());
	}

	@Override
	protected ITextComponent getDefaultName() {
		return new TranslationTextComponent("container.enchanted.spinning_wheel");
	}

	@Override
	protected Container createMenu(int id, PlayerInventory player) {
		return new SpinningWheelContainer(id, player, this, this.furnaceData);
	}

	@Override
	public void tick() {
		cookTimeTotal = 100;
		boolean isBurning = this.isBurning();
		boolean flag1 = false;

		if (this.level != null && !this.level.isClientSide) {
			this.matchRecipe();
			AltarTileEntity altar = AltarPowerHelper.tryGetAltar(level, potentialAltars);

			if(this.canSpin(this.currentRecipe) && currentRecipe.getPower() > 0 && altar != null) {
				double powerThisTick = (double)currentRecipe.getPower() / cookTimeTotal;
				if(altar.currentPower > powerThisTick) {
					altar.currentPower -= powerThisTick;
					this.burnTime = 1;
					this.cookTime++;

					if(cookTime == cookTimeTotal) {
						cookTime = 0;
						spin();
					}
				}
			}
			else {
				burnTime = 0;
				cookTime = 0;
			}


			if (isBurning != this.isBurning()) {
				flag1 = true;
				this.level.setBlock(this.worldPosition, this.level.getBlockState(this.worldPosition).setValue(SpinningWheelBlock.LIT, this.isBurning()), 3);
			}
		}

		if (flag1) {
			this.setChanged();
		}

	}

	protected void spin() {
		currentRecipe.assemble(this);

		for(ItemStack item : currentRecipe.getItemsIn()) {
			for (int i = 0; i < inventoryContents.size()-1; i++) {
				ItemStack stack = this.inventoryContents.get(i);
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
					if(itemStack.getCount() < itemStack.getMaxStackSize())
						return true;
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
	protected CompoundNBT saveAdditional(CompoundNBT nbt) {
		return AltarPowerHelper.savePosTag(potentialAltars, nbt);
	}

	@Override
	protected void loadAdditional(CompoundNBT nbt) {
		AltarPowerHelper.loadPosTag(potentialAltars, nbt);
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
