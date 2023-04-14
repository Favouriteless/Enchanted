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
import com.favouriteless.enchanted.common.menus.DistilleryMenu;
import com.favouriteless.enchanted.common.recipes.DistilleryRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractFurnaceBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.apache.commons.lang3.ArrayUtils;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class DistilleryBlockEntity extends ProcessingBlockEntityBase implements IAltarPowerConsumer {

    private DistilleryRecipe currentRecipe;
    private boolean canDistill = false; // Cache for performance
    private final List<BlockPos> potentialAltars = new ArrayList<>();

    private static final int[] INPUT_SLOTS = new int[] { 0, 1, 2 };

    private static final int[] SLOTS_FOR_UP = new int[] { 1, 2 };
    private static final int[] SLOTS_FOR_DOWN = new int[] { 3, 4, 5, 6 };
    private static final int[] SLOTS_FOR_SIDES = new int[] { 0 };

    private boolean isBurning = false;
    private int cookTime = 0;
    private int cookTimeTotal = 200;
    private final ContainerData data = new ContainerData() {
        @Override
        public int get(int index) {
            return switch(index) {
                case 0 -> cookTime;
                case 1 -> cookTimeTotal;
                default -> 0;
            };
        }

        @Override
        public void set(int index, int value) {
            switch(index) {
                case 0:
                    cookTime = value;
                case 1:
                    cookTimeTotal = value;
            }
        }

        @Override
        public int getCount() {
            return 2;
        }
    };

    public DistilleryBlockEntity(BlockPos pos, BlockState state) {
        super(EnchantedBlockEntityTypes.DISTILLERY.get(), pos, state, 7);
    }

    public static <T extends BlockEntity> void tick(Level level, BlockPos blockPos, BlockState blockState, T t) {
        if(t instanceof DistilleryBlockEntity be) {
            boolean isBurning = be.isBurning;

            if(level != null && !level.isClientSide) {
                AltarBlockEntity altar = AltarPowerHelper.tryGetAltar(level, be.potentialAltars);

                if(be.canDistill && altar != null) {
                    if(altar.currentPower > 10.0D) {
                        altar.currentPower -= 10.0D;
                        be.isBurning = true;
                        be.cookTime++;

                        if(be.cookTime == be.cookTimeTotal) {
                            be.cookTime = 0;
                            be.distill(be.currentRecipe);
                        }
                    }
                }
                else {
                    be.isBurning = false;
                    be.cookTime = 0;
                }


                if(isBurning != be.isBurning) {
                    level.setBlock(be.worldPosition, level.getBlockState(be.worldPosition).setValue(AbstractFurnaceBlock.LIT, be.isBurning), 3);
                }
            }

            be.setChanged();
        }
    }

    protected void distill(DistilleryRecipe recipeIn) {
        if (recipeIn != null) {
            List<ItemStack> itemsOut = new ArrayList<>();

            for(ItemStack itemStack : recipeIn.getItemsOut())
                itemsOut.add(itemStack.copy());

            for (ItemStack item : itemsOut) {
                for (int i = 3; i < 7; i++) { // Fit items into existing stacks
                    ItemStack stack = this.inventoryContents.getStackInSlot(i);
                    if (item.getItem() == stack.getItem()) {
                        int spaceLeft = stack.getMaxStackSize() - stack.getCount();
                        if (spaceLeft <= item.getCount()) {
                            stack.grow(spaceLeft);
                            item.shrink(spaceLeft);
                        }
                        else {
                            stack.grow(item.getCount());
                            item.shrink(item.getCount());
                            break;
                        }
                    }
                }
            }

            for(ItemStack item : itemsOut) {
                if (!item.isEmpty()) {
                    for (int i = 3; i < 7; i++) { // Fit items into empty stacks
                        if (this.inventoryContents.getStackInSlot(i).isEmpty()) {
                            this.inventoryContents.setStackInSlot(i, item.copy());
                            break;
                        }
                    }
                }
            }

            for(ItemStack item : recipeIn.getItemsIn()) {
                for (int i = 0; i < 3; i++) {
                    ItemStack stack = this.inventoryContents.getStackInSlot(i);
                    if(item.getItem() == stack.getItem()) {
                        stack.shrink(item.getCount());
                        break;
                    }
                }
            }

        }
    }

    protected boolean canDistill() {
        if(currentRecipe != null) {
            List<ItemStack> itemsOut = new ArrayList<>(currentRecipe.getItemsOut());
            List<ItemStack> toRemoveOut = new ArrayList<>();

            for(ItemStack item : itemsOut) { // Check for existing ItemStacks of correct type and size.
                for(int i = 3; i < 7; i++) { // Iterate through output slots
                    ItemStack stack = inventoryContents.getStackInSlot(i);
                    if(item.getItem() == stack.getItem()) {
                        if(item.getCount() + stack.getCount() <= stack.getMaxStackSize()) {
                            toRemoveOut.add(item);
                            break;
                        }
                        else
                            item.shrink(stack.getMaxStackSize() - stack.getCount());
                    }
                }
            }

            itemsOut.removeAll(toRemoveOut); // ItemStacks have been accounted for
            toRemoveOut.clear();

            boolean[] isEmpty = new boolean[] { // Cursed but quickest way to set stacks as not empty without actually modifying the inventory
                    inventoryContents.getStackInSlot(3).isEmpty(),
                    inventoryContents.getStackInSlot(4).isEmpty(),
                    inventoryContents.getStackInSlot(5).isEmpty(),
                    inventoryContents.getStackInSlot(6).isEmpty()
            };
            for(ItemStack item : itemsOut) { // Check for empty item slots
                for(int i = 0; i < isEmpty.length; i++) {
                    if(isEmpty[i]) {
                        toRemoveOut.add(item);
                        isEmpty[i] = false;
                        break;
                    }
                }
            }
            itemsOut.removeAll(toRemoveOut);

            return itemsOut.isEmpty();
        }
        return false;
    }

    @Override
    public void saveAdditional(CompoundTag nbt) {
        super.saveAdditional(nbt);
        AltarPowerHelper.savePosTag(potentialAltars, nbt);
        nbt.putBoolean("burnTime", isBurning);
        nbt.putInt("cookTime", cookTime);
        nbt.putInt("cookTimeTotal", cookTimeTotal);
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        AltarPowerHelper.loadPosTag(potentialAltars, nbt);
        isBurning = nbt.getBoolean("burnTime");
        cookTime = nbt.getInt("cookTime");
        cookTimeTotal = nbt.getInt("cookTimeTotal");
    }

    private void matchRecipe() {
        if (level != null) {
            currentRecipe = level.getRecipeManager().getRecipeFor(EnchantedRecipeTypes.DISTILLERY, recipeWrapper, level).orElse(null);

            if(currentRecipe != null)
                cookTimeTotal = currentRecipe.getCookTime();
        }
    }

    @Override
    protected void onInventoryChanged(int slot) {
        if(ArrayUtils.contains(INPUT_SLOTS, slot))
            matchRecipe();
        canDistill = canDistill();
    }

    @Override
    public ContainerData getData() {
        return data;
    }

    @Override
    public Component getDisplayName() {
        return new TranslatableComponent("container.enchanted.distillery");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory playerInventory, Player player) {
        return new DistilleryMenu(id, playerInventory, this, data);
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
