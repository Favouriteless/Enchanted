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

import com.favouriteless.enchanted.common.blocks.FumeFunnelBlock;
import com.favouriteless.enchanted.common.blocks.WitchOvenBlock;
import com.favouriteless.enchanted.common.containers.WitchOvenContainer;
import com.favouriteless.enchanted.common.init.EnchantedBlocks;
import com.favouriteless.enchanted.common.init.EnchantedTileEntityTypes;
import com.favouriteless.enchanted.common.recipes.WitchOvenRecipe;
import net.minecraft.block.AbstractFurnaceBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.AbstractCookingRecipe;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tags.ItemTags;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.IIntArray;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.Tags.IOptionalNamedTag;

import javax.annotation.Nullable;
import java.util.Random;

public class WitchOvenTileEntity extends ProcessingTileEntityBase {

    private static final Random RANDOM = new Random();
    public static final IOptionalNamedTag<Item> ORE_TAG = ItemTags.createOptional(new ResourceLocation("forge", "ores"));

    private int burnTime = 0;
    private int burnTimeTotal = 0;
    private int cookTime = 0;
    private int cookTimeTotal = 200;
    private final IIntArray data = new IIntArray() {
        @Override
        public int get(int index) {
            switch(index) {
                case 0:
                    return burnTime;
                case 1:
                    return burnTimeTotal;
                case 2:
                    return cookTime;
                case 3:
                    return cookTimeTotal;
                default:
                    return 0;
            }
        }

        @Override
        public void set(int index, int value) {
            switch(index) {
                case 0:
                    burnTime = value;
                case 1:
                    burnTimeTotal = value;
                case 2:
                    cookTime = value;
                case 3:
                    cookTimeTotal = value;
            }
        }

        @Override
        public int getCount() {
            return 4;
        }
    };


    public WitchOvenTileEntity(TileEntityType<?> typeIn) {
        super(typeIn, NonNullList.withSize(5, ItemStack.EMPTY));
    }

    public WitchOvenTileEntity() {
        this(EnchantedTileEntityTypes.WITCH_OVEN.get());
    }

    @Override
    protected void loadAdditional(CompoundNBT nbt) {
        burnTime = nbt.getInt("burnTime");
        burnTimeTotal = nbt.getInt("burnTimeTotal");
        cookTime = nbt.getInt("cookTime");
        cookTimeTotal = nbt.getInt("cookTimeTotal");
    }

    @Override
    protected void saveAdditional(CompoundNBT nbt) {
        nbt.putInt("burnTime", burnTime);
        nbt.putInt("burnTimeTotal", burnTimeTotal);
        nbt.putInt("cookTime", cookTime);
        nbt.putInt("cookTimeTotal", cookTimeTotal);
    }

    @Override
    public void tick() {
        boolean flag = isBurning();
        boolean flag1 = false;

        if (!level.isClientSide) {
            ItemStack fuelStack = inventoryContents.get(1);
            if (isBurning()) {
                burnTime--;
            }


            if (this.isBurning() || !fuelStack.isEmpty() && !inventoryContents.get(0).isEmpty()) {
                IRecipe<?> recipe = level.getRecipeManager().getRecipeFor(IRecipeType.SMELTING, this, level).orElse(null);
                if (!isBurning() && canSmelt(recipe) && !inventoryContents.get(0).getItem().is(ORE_TAG)) {
                    burnTime = getBurnTime(fuelStack);
                    burnTimeTotal = getBurnTime(fuelStack);
                    if (isBurning()) {
                        flag1 = true;
                        if (fuelStack.hasContainerItem())
                            inventoryContents.set(1, fuelStack.getContainerItem());
                        else
                        if (!fuelStack.isEmpty()) {
                            fuelStack.shrink(1);
                            if (fuelStack.isEmpty()) {
                                inventoryContents.set(1, fuelStack.getContainerItem());
                            }
                        }
                    }
                }

                if (isBurning() && canSmelt(recipe)) {
                    cookTime++;
                    if (cookTime == cookTimeTotal) {
                        cookTime = 0;
                        cookTimeTotal = (int)Math.round(getCookTime() * 0.8D);
                        smelt(recipe);
                        flag1 = true;
                    }
                } else {
                    cookTime = 0;
                }
            } else if (!isBurning() && cookTime > 0) {
                cookTime = MathHelper.clamp(cookTime - 2, 0, cookTimeTotal);
            }

            if (flag != this.isBurning()) {
                flag1 = true;
                level.setBlock(worldPosition, level.getBlockState(worldPosition).setValue(WitchOvenBlock.LIT, isBurning()), 3);
                updateFumeFunnels();
            }
        }

        if (flag1) {
            setChanged();
        }

    }

    protected boolean canSmelt(@Nullable IRecipe<?> recipeIn) {
        if (!this.inventoryContents.get(0).isEmpty() && recipeIn != null) { // If item in input & recipe not null

            ItemStack resultStack = recipeIn.getResultItem();

            if (resultStack.isEmpty()) { // If recipe makes nothing
                return false;
            } else {
                ItemStack outputStack = this.inventoryContents.get(2);
                if (outputStack.isEmpty()) { // If output is empty
                    return true;
                } else if (!outputStack.sameItem(resultStack)) { // If output is a different item
                    return false;
                } else if (outputStack.getCount() + resultStack.getCount() <= this.getMaxStackSize() && outputStack.getCount() + resultStack.getCount() <= outputStack.getMaxStackSize()) {
                    return true;
                } else {
                    return outputStack.getCount() + resultStack.getCount() <= resultStack.getMaxStackSize();
                }
            }
        } else {
            return false;
        }
    }

    protected void smelt(@Nullable IRecipe<?> recipe) {
        if (recipe != null && this.canSmelt(recipe)) {
            ItemStack itemstack = this.inventoryContents.get(0);
            ItemStack itemstack1 = recipe.getResultItem();
            ItemStack itemstack2 = this.inventoryContents.get(2);

            if(RANDOM.nextDouble() <= getByproductChance()) {
                createByproduct();
            }

            if (itemstack2.isEmpty()) {
                this.inventoryContents.set(2, itemstack1.copy());
            } else if (itemstack2.getItem() == itemstack1.getItem()) {
                itemstack2.grow(itemstack1.getCount());
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

        if(currentRecipe != null && jarStack.getCount() >= currentRecipe.getJarsNeeded()) {
            if (outputStack.isEmpty()) {
                this.inventoryContents.set(4, currentRecipe.getResultItem().copy());
                jarStack.shrink(currentRecipe.getJarsNeeded());
            } else if (outputStack.getItem() == currentRecipe.getResultItem().getItem() && outputStack.getCount() < outputStack.getMaxStackSize()) {
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
                if (this.level.getBlockState(_pos).getBlock() instanceof FumeFunnelBlock) {
                    this.level.setBlock(_pos, this.level.getBlockState(_pos).setValue(AbstractFurnaceBlock.LIT, this.isBurning()), 3);
                }
            }
        }
    }

    private double getByproductChance() {
        Direction facing = this.level.getBlockState(this.worldPosition).getValue(WitchOvenBlock.FACING);

        double byproductChance = 0.3D;

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
                    .filter((recipe) -> recipe.matches(this, level))
                    .findFirst()
                    .orElse(null);
        }
        return currentRecipe;
    }

    @Override
    public IIntArray getData() {
        return data;
    }

    @Override
    protected ITextComponent getDefaultName() {
        return new TranslationTextComponent("container.enchanted.witch_oven");
    }

    @Override
    protected Container createMenu(int id, PlayerInventory player) {
        return new WitchOvenContainer(id, player, this, this.data);
    }

    private boolean isBurning() {
        return this.burnTime > 0;
    }

    protected int getCookTime() {
        return level.getRecipeManager().getRecipeFor(IRecipeType.SMELTING, this, level).map(AbstractCookingRecipe::getCookingTime).orElse(200);
    }

}