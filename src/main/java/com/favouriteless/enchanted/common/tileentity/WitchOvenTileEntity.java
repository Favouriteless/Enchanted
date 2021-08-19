/*
 * Copyright (c) 2021. Favouriteless
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

import com.favouriteless.enchanted.common.blocks.FumeFunnel;
import com.favouriteless.enchanted.common.blocks.WitchOvenBlock;
import com.favouriteless.enchanted.common.containers.WitchOvenContainer;
import com.favouriteless.enchanted.common.recipes.witch_oven.WitchOvenRecipe;
import com.favouriteless.enchanted.core.init.EnchantedBlocks;
import com.favouriteless.enchanted.core.init.EnchantedTileEntities;
import net.minecraft.block.AbstractFurnaceBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import javax.annotation.Nullable;
import java.util.Random;

public class WitchOvenTileEntity extends FurnaceTileEntityBase {

    private static final Random random = new Random();

    public WitchOvenTileEntity(TileEntityType<?> typeIn) {
        super(typeIn, NonNullList.withSize(5, ItemStack.EMPTY));
    }

    public WitchOvenTileEntity() {
        this(EnchantedTileEntities.WITCH_OVEN.get());
    }


    @Override
    protected ITextComponent getDefaultName() {
        return new TranslationTextComponent("container.witch_oven");
    }

    @Override
    protected Container createMenu(int id, PlayerInventory player) {
        return new WitchOvenContainer(id, player, this, this.furnaceData);
    }

    @Override
    public void tick() {
        boolean flag = this.isBurning();
        boolean flag1 = false;
        if (this.isBurning()) {
            --this.burnTime;
        }

        if (!this.level.isClientSide) {
            ItemStack fuelStack = this.inventoryContents.get(1);
            if (this.isBurning() || !fuelStack.isEmpty() && !this.inventoryContents.get(0).isEmpty()) {
                IRecipe<?> irecipe = this.level.getRecipeManager().getRecipeFor(IRecipeType.SMELTING, this, this.level).orElse(null);
                if (!this.isBurning() && this.canSmelt(irecipe)) {
                    this.burnTime = this.getBurnTime(fuelStack);
                    this.recipesUsed = this.burnTime;
                    if (this.isBurning()) {
                        flag1 = true;
                        if (fuelStack.hasContainerItem())
                            this.inventoryContents.set(1, fuelStack.getContainerItem());
                        else
                        if (!fuelStack.isEmpty()) {
                            fuelStack.shrink(1);
                            if (fuelStack.isEmpty()) {
                                this.inventoryContents.set(1, fuelStack.getContainerItem());
                            }
                        }
                    }
                }

                if (this.isBurning() && this.canSmelt(irecipe)) {
                    ++this.cookTime;
                    if (this.cookTime == this.cookTimeTotal) {
                        this.cookTime = 0;
                        this.cookTimeTotal = this.getCookTime();
                        this.smelt(irecipe);
                        flag1 = true;
                    }
                } else {
                    this.cookTime = 0;
                }
            } else if (!this.isBurning() && this.cookTime > 0) {
                this.cookTime = MathHelper.clamp(this.cookTime - 2, 0, this.cookTimeTotal);
            }

            if (flag != this.isBurning()) {
                flag1 = true;
                this.level.setBlock(this.worldPosition, this.level.getBlockState(this.worldPosition).setValue(WitchOvenBlock.LIT, this.isBurning()), 3);
                this.updateFumeFunnels();
            }
        }

        if (flag1) {
            this.setChanged();
        }

    }

    @Override
    protected void smelt(@Nullable IRecipe<?> recipe) {
        if (recipe != null && this.canSmelt(recipe)) {
            ItemStack itemstack = this.inventoryContents.get(0);
            ItemStack itemstack1 = recipe.getResultItem();
            ItemStack itemstack2 = this.inventoryContents.get(2);

            if(random.nextDouble() <= getByproductChance()) {
                createByproduct();
            }

            if (itemstack2.isEmpty()) {
                this.inventoryContents.set(2, itemstack1.copy());
            } else if (itemstack2.getItem() == itemstack1.getItem()) {
                itemstack2.grow(itemstack1.getCount());
            }

            if (!this.level.isClientSide) {
                this.setRecipeUsed(recipe);
            }

            if (itemstack.getItem() == Blocks.WET_SPONGE.asItem() && !this.inventoryContents.get(1).isEmpty() && this.inventoryContents.get(1).getItem() == Items.BUCKET) {
                this.inventoryContents.set(1, new ItemStack(Items.WATER_BUCKET));
            }

            itemstack.shrink(1);
        }
    }

    private void createByproduct() {
        WitchOvenRecipe currentRecipe = matchRecipe();
        ItemStack outputStack = this.inventoryContents.get(4);
        ItemStack jarStack = this.inventoryContents.get(3);

        if(currentRecipe != null && !jarStack.isEmpty()) {
            if (outputStack.isEmpty()) {
                this.inventoryContents.set(4, currentRecipe.getOutput().copy());
                jarStack.shrink(currentRecipe.getJarsNeeded());
            } else if (outputStack.getItem() == currentRecipe.getOutput().getItem() && outputStack.getCount() < outputStack.getMaxStackSize()) {
                outputStack.grow(1);
                jarStack.shrink(currentRecipe.getJarsNeeded());
            }
        }
    }

    /**
     * Updates lit property of fume funnels
     */
    private void updateFumeFunnels() {
        if(!level.isClientSide) {
            BlockState state = this.level.getBlockState(this.worldPosition);
            Direction facing = state.getValue(WitchOvenBlock.FACING);

            BlockPos[] potentialFilters = new BlockPos[]{
                    new BlockPos(this.worldPosition.offset(facing.getClockWise().getNormal())),
                    new BlockPos(this.worldPosition.offset(facing.getCounterClockWise().getNormal())),
                    new BlockPos(this.worldPosition.offset(Direction.UP.getNormal()))
            };

            for (BlockPos _pos : potentialFilters) {
                if (this.level.getBlockState(_pos).getBlock() instanceof FumeFunnel) {
                    this.level.setBlock(_pos, this.level.getBlockState(_pos).setValue(AbstractFurnaceBlock.LIT, this.isBurning()), 3);
                }
            }
        }
    }

    private double getByproductChance() {
        Direction facing = this.level.getBlockState(this.worldPosition).getValue(WitchOvenBlock.FACING);

        double byproductChance = 1.0D;

        Block[] potentialFilters = new Block[] { // Top filter is just decoration
                this.level.getBlockState(this.worldPosition.offset(facing.getClockWise().getNormal())).getBlock(),
                this.level.getBlockState(this.worldPosition.offset(facing.getCounterClockWise().getNormal())).getBlock()
        };

        for(Block block : potentialFilters) {
            if(block == EnchantedBlocks.FUME_FUNNEL.get()) {
                byproductChance += 0.25D;
            }
            else if(block == EnchantedBlocks.FUME_FUNNEL_FILTERED.get()) {
                byproductChance += 0.3D;
            }
        }
        return byproductChance;
    }

    private WitchOvenRecipe matchRecipe() {
        WitchOvenRecipe currentRecipe = null;
        if (level != null) {
            currentRecipe = level.getRecipeManager()
                    .getRecipes()
                    .stream()
                    .filter(recipe -> recipe instanceof WitchOvenRecipe)
                    .map(recipe -> (WitchOvenRecipe) recipe)
                    .filter(recipe -> matchRecipe(recipe))
                    .findFirst()
                    .orElse(null);
        }
        return currentRecipe;
    }

    private boolean matchRecipe(WitchOvenRecipe recipe) {
        return recipe.matches(this.inventoryContents.get(0));
    }

}