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
import com.favouriteless.enchanted.common.menus.DistilleryMenu;
import com.favouriteless.enchanted.common.recipes.DistilleryRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractFurnaceBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class DistilleryBlockEntity extends ProcessingBlockEntityBase implements IPowerConsumer, MenuProvider {

    private final ItemStackHandler jar = new ItemStackHandler(1);
    private final ItemStackHandler input = new ItemStackHandler(2);
    private final ItemStackHandler output = new ItemStackHandler(4);
    private final CombinedInvWrapper inputCombined = new CombinedInvWrapper(jar, input);

    private final LazyOptional<IItemHandlerModifiable> jarHandler = LazyOptional.of(() -> jar);
    private final LazyOptional<IItemHandlerModifiable> inputHandler = LazyOptional.of(() -> input);
    private final LazyOptional<IItemHandlerModifiable> outputHandler = LazyOptional.of(() -> output);

    private final RecipeWrapper recipeWrapper = new RecipeWrapper(inputCombined);
    private DistilleryRecipe currentRecipe;
    private final SimplePowerPosHolder altarPosHolder;

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
        super(EnchantedBlockEntityTypes.DISTILLERY.get(), pos, state);
        this.altarPosHolder = new SimplePowerPosHolder(pos);
    }

    public static <T extends BlockEntity> void tick(Level level, BlockPos blockPos, BlockState blockState, T t) {
        if(t instanceof DistilleryBlockEntity be) {
            boolean isBurning = be.isBurning;

            if(level != null && !level.isClientSide) {
                AltarBlockEntity altar = PowerHelper.tryGetAltar(level, be.altarPosHolder);
                be.matchRecipe();

                if(be.canDistill() && altar != null) {
                    if(altar.tryConsumePower(10.0D)) {
                        be.isBurning = true;
                        be.cookTime++;

                        if(be.cookTime == be.cookTimeTotal) {
                            be.cookTime = 0;
                            be.distill();
                        }
                    }
                }
                else {
                    be.isBurning = false;
                    be.cookTime = 0;
                }


                if(isBurning != be.isBurning)
                    level.setBlock(be.worldPosition, level.getBlockState(be.worldPosition).setValue(AbstractFurnaceBlock.LIT, be.isBurning), 3);
            }

            be.setChanged();
        }
    }

    protected void distill() {
        if (currentRecipe != null) {
            List<ItemStack> recipeItemsOut = new ArrayList<>();

            for(ItemStack itemStack : currentRecipe.getItemsOut())
                recipeItemsOut.add(itemStack.copy());

            for (ItemStack recipeResult : recipeItemsOut) {
                for (int i = 0; i < output.getSlots(); i++) { // Try to fit items into existing stacks
                    if(ItemStack.isSameItemSameTags(recipeResult, output.getStackInSlot(i)))
                        recipeResult.setCount(output.insertItem(i, recipeResult, false).getCount());
                }
            }

            for(ItemStack recipeResult : recipeItemsOut) {
                if (!recipeResult.isEmpty()) {
                    for (int i = 0; i < output.getSlots(); i++) { // Fit items into empty stacks
                        if (output.getStackInSlot(i).isEmpty()) {
                            output.setStackInSlot(i, recipeResult.copy());
                            break;
                        }
                    }
                }
            }

            for(ItemStack recipeItem : currentRecipe.getItemsIn()) {
                for (int i = 0; i < 3; i++) {
                    if(ItemStack.isSameItemSameTags(recipeItem, inputCombined.getStackInSlot(i))) {
                        inputCombined.extractItem(i, recipeItem.getCount(), false);
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

            for(ItemStack recipeStack : itemsOut) { // Check for existing ItemStacks of correct type and size.
                for(int i = 0; i < output.getSlots(); i++) { // Iterate through output slots
                    ItemStack invStack = output.getStackInSlot(i);
                    if(ItemStack.isSameItemSameTags(invStack, invStack)) {
                        if(recipeStack.getCount() + invStack.getCount() <= invStack.getMaxStackSize()) {
                            toRemoveOut.add(recipeStack);
                            break;
                        }
                        else
                            recipeStack.shrink(invStack.getMaxStackSize() - invStack.getCount());
                    }
                }
            }

            itemsOut.removeAll(toRemoveOut); // ItemStacks have been accounted for
            toRemoveOut.clear();

            boolean[] isEmpty = new boolean[] { // Cursed but quickest way to set slots as not empty without actually modifying them
                    output.getStackInSlot(0).isEmpty(),
                    output.getStackInSlot(1).isEmpty(),
                    output.getStackInSlot(2).isEmpty(),
                    output.getStackInSlot(3).isEmpty()
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

    private void matchRecipe() {
        if (level != null) {
            if(currentRecipe == null || !currentRecipe.matches(recipeWrapper, level))
                currentRecipe = level.getRecipeManager().getRecipeFor(EnchantedRecipeTypes.DISTILLERY, recipeWrapper, level).orElse(null);

            if(currentRecipe != null)
                cookTimeTotal = currentRecipe.getCookTime();
        }
    }

    public ItemStackHandler getJarInventory() {
        return jar;
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
        return getDroppableInventoryFor(jar, input, output);
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
    public void saveAdditional(CompoundTag nbt) {
        super.saveAdditional(nbt);
        nbt.put("altarPos", altarPosHolder.serializeNBT());
        nbt.putBoolean("burnTime", isBurning);
        nbt.putInt("cookTime", cookTime);
        nbt.putInt("cookTimeTotal", cookTimeTotal);
        nbt.put("jar", jar.serializeNBT());
        nbt.put("input", input.serializeNBT());
        nbt.put("output", output.serializeNBT());
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        altarPosHolder.deserializeNBT(nbt.getList("altarPos", 10));
        isBurning = nbt.getBoolean("burnTime");
        cookTime = nbt.getInt("cookTime");
        cookTimeTotal = nbt.getInt("cookTimeTotal");
        jar.deserializeNBT(nbt.getCompound("jar"));
        input.deserializeNBT(nbt.getCompound("input"));
        output.deserializeNBT(nbt.getCompound("output"));
    }

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if(cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            if(side == null)
                return super.getCapability(cap, side);
            else if(side == Direction.UP)
                return inputHandler.cast();
            else if(side == Direction.DOWN)
                return outputHandler.cast();
            else
                return jarHandler.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
        if(inputHandler != null)
            inputHandler.invalidate();
        if(jarHandler != null)
            jarHandler.invalidate();
        if(outputHandler != null)
            outputHandler.invalidate();
    }

    @Override
    public @NotNull IPowerConsumer.IPowerPosHolder getPosHolder() {
        return altarPosHolder;
    }
}
