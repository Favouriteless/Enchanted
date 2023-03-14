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

import com.favouriteless.enchanted.Enchanted;
import com.favouriteless.enchanted.common.blocks.FumeFunnelBlock;
import com.favouriteless.enchanted.common.blocks.WitchOvenBlock;
import com.favouriteless.enchanted.common.init.EnchantedBlockEntityTypes;
import com.favouriteless.enchanted.common.init.EnchantedBlocks;
import com.favouriteless.enchanted.common.init.EnchantedItems;
import com.favouriteless.enchanted.common.init.EnchantedRecipeTypes;
import com.favouriteless.enchanted.common.menus.WitchOvenMenu;
import com.favouriteless.enchanted.common.recipes.WitchOvenRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractFurnaceBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.Tags;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;

public class WitchOvenBlockEntity extends ProcessingBlockEntityBase {

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

    private static final int[] SLOTS_FOR_UP = new int[]{0};
    private static final int[] SLOTS_FOR_DOWN = new int[]{2, 4};
    private static final int[] SLOTS_FOR_WEST = new int[]{3};
    private static final int[] SLOTS_FOR_OTHER = new int[]{1};
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

            if(Enchanted.RANDOM.nextDouble() <= getByproductChance()) {
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

        if(currentRecipe != null) {
            ItemStack result = currentRecipe.getResultItem().copy();

            if(jarStack.getCount() >= result.getCount()) {
                if(outputStack.isEmpty()) {
                    this.inventoryContents.set(4, result);
                    jarStack.shrink(result.getCount());
                }
                else if(outputStack.getItem() == result.getItem() && outputStack.getCount() < (outputStack.getMaxStackSize() - result.getCount())) {
                    outputStack.grow(result.getCount());
                    jarStack.shrink(result.getCount());
                }
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
                    .getRecipeFor(EnchantedRecipeTypes.WITCH_OVEN, this, level)
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
        return new WitchOvenMenu(id, player, this, this.data);
    }

    private boolean isBurning() {
        return this.burnTime > 0;
    }

    protected int getCookTime() {
        return level.getRecipeManager().getRecipeFor(RecipeType.SMELTING, this, level).map(AbstractCookingRecipe::getCookingTime).orElse(200);
    }

    @Override
    public int[] getSlotsForFace(Direction side) {
        if(side != Direction.UP && side != Direction.DOWN)
            side = Direction.fromYRot(side.toYRot() + level.getBlockState(worldPosition).getValue(WitchOvenBlock.FACING).toYRot());
        return switch(side) {
            case UP -> SLOTS_FOR_UP;
            case DOWN -> SLOTS_FOR_DOWN;
            case WEST -> SLOTS_FOR_WEST;
            default -> SLOTS_FOR_OTHER;
        };
    }

    @Override
    public boolean canPlaceItemThroughFace(int slot, ItemStack stack, @Nullable Direction direction) {
        if(direction == Direction.UP)
            return slot == 0;
        else if(direction != Direction.DOWN)
            return (slot == 2 && stack.getItem() == EnchantedItems.CLAY_JAR.get()) || (slot == 1 && net.minecraftforge.common.ForgeHooks.getBurnTime(stack, RecipeType.SMELTING) > 0);

        return false;
    }

    @Override
    public boolean canTakeItemThroughFace(int slot, ItemStack stack, Direction direction) {
        return direction == Direction.DOWN && (slot == 2 || slot == 4);
    }

}