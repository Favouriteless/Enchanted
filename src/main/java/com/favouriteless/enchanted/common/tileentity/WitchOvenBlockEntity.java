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
import com.favouriteless.enchanted.common.init.EnchantedBlockEntityTypes;
import com.favouriteless.enchanted.common.recipes.WitchOvenRecipe;
import net.minecraft.core.Registry;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractFurnaceBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.Direction;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraftforge.common.Tags;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.Random;

public class WitchOvenBlockEntity extends ProcessingBlockEntityBase {

    private static final Random RANDOM = new Random();

    private int burnTime = 0;
    private int burnTimeTotal = 0;
    private int cookTime = 0;
    private int cookTimeTotal = 200;
    private final ContainerData data = new ContainerData() {
        @Override
        public int get(int index) {
            return switch(index) {
                case 0 -> burnTime;
                case 1 -> burnTimeTotal;
                case 2 -> cookTime;
                case 3 -> cookTimeTotal;
                default -> 0;
            };
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

    public WitchOvenBlockEntity(BlockPos pos, BlockState state) {
        super(EnchantedBlockEntityTypes.WITCH_OVEN.get(), pos, state, NonNullList.withSize(5, ItemStack.EMPTY));
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        burnTime = nbt.getInt("burnTime");
        burnTimeTotal = nbt.getInt("burnTimeTotal");
        cookTime = nbt.getInt("cookTime");
        cookTimeTotal = nbt.getInt("cookTimeTotal");
    }

    @Override
    public void saveAdditional(CompoundTag nbt) {
        super.saveAdditional(nbt);
        nbt.putInt("burnTime", burnTime);
        nbt.putInt("burnTimeTotal", burnTimeTotal);
        nbt.putInt("cookTime", cookTime);
        nbt.putInt("cookTimeTotal", cookTimeTotal);
    }

    public static <T extends BlockEntity> void tick(Level level, BlockPos blockPos, BlockState blockState, T t) {
        if(t instanceof WitchOvenBlockEntity blockEntity) {
            boolean flag = blockEntity.isBurning();
            boolean flag1 = false;

            if(!level.isClientSide) {
                ItemStack fuelStack = blockEntity.inventoryContents.get(1);
                if(blockEntity.isBurning()) {
                    blockEntity.burnTime--;
                }


                if(blockEntity.isBurning() || !fuelStack.isEmpty() && !blockEntity.inventoryContents.get(0).isEmpty()) {
                    Recipe<?> recipe = level.getRecipeManager().getRecipeFor(RecipeType.SMELTING, blockEntity, level).orElse(null);
                    if(!blockEntity.isBurning() && blockEntity.canSmelt(recipe) && !ForgeRegistries.ITEMS.tags().getTag(Tags.Items.ORES).contains(blockEntity.inventoryContents.get(0).getItem())) {
                        blockEntity.burnTime = blockEntity.getBurnTime(fuelStack);
                        blockEntity.burnTimeTotal = blockEntity.getBurnTime(fuelStack);
                        if(blockEntity.isBurning()) {
                            flag1 = true;
                            if(fuelStack.hasContainerItem())
                                blockEntity.inventoryContents.set(1, fuelStack.getContainerItem());
                            else if(!fuelStack.isEmpty()) {
                                fuelStack.shrink(1);
                                if(fuelStack.isEmpty()) {
                                    blockEntity.inventoryContents.set(1, fuelStack.getContainerItem());
                                }
                            }
                        }
                    }

                    if(blockEntity.isBurning() && blockEntity.canSmelt(recipe)) {
                        blockEntity.cookTime++;
                        if(blockEntity.cookTime == blockEntity.cookTimeTotal) {
                            blockEntity.cookTime = 0;
                            blockEntity.cookTimeTotal = (int) Math.round(blockEntity.getCookTime() * 0.8D);
                            blockEntity.smelt(recipe);
                            flag1 = true;
                        }
                    }
                    else {
                        blockEntity.cookTime = 0;
                    }
                }
                else if(!blockEntity.isBurning() && blockEntity.cookTime > 0) {
                    blockEntity.cookTime = Mth.clamp(blockEntity.cookTime - 2, 0, blockEntity.cookTimeTotal);
                }

                if(flag != blockEntity.isBurning()) {
                    flag1 = true;
                    level.setBlock(blockEntity.worldPosition, level.getBlockState(blockEntity.worldPosition).setValue(WitchOvenBlock.LIT, blockEntity.isBurning()), 3);
                    blockEntity.updateFumeFunnels();
                }
            }

            if(flag1) {
                blockEntity.setChanged();
            }
        }
    }

    protected boolean canSmelt(@Nullable Recipe<?> recipeIn) {
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

    protected void smelt(@Nullable Recipe<?> recipe) {
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
    public ContainerData getData() {
        return data;
    }

    @Override
    protected Component getDefaultName() {
        return new TranslatableComponent("container.enchanted.witch_oven");
    }

    @Override
    protected AbstractContainerMenu createMenu(int id, Inventory player) {
        return new WitchOvenContainer(id, player, this, this.data);
    }

    private boolean isBurning() {
        return this.burnTime > 0;
    }

    protected int getCookTime() {
        return level.getRecipeManager().getRecipeFor(RecipeType.SMELTING, this, level).map(AbstractCookingRecipe::getCookingTime).orElse(200);
    }
}