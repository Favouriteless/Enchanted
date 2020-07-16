package com.favouriteless.magicraft.tileentity;

import com.favouriteless.magicraft.containers.DistilleryContainer;
import com.favouriteless.magicraft.init.MagicraftTileEntities;
import com.favouriteless.magicraft.recipe.distillery.DistilleryRecipe;
import net.minecraft.block.AbstractFurnaceBlock;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class DistilleryTileEntity extends FurnaceTileEntityBase  {

    private DistilleryRecipe currentRecipe;

    public DistilleryTileEntity(TileEntityType<?> typeIn) {
        super(typeIn, NonNullList.withSize(7, ItemStack.EMPTY));
    }

    public DistilleryTileEntity() {
        this(MagicraftTileEntities.DISTILLERY.get());
    }


    @Override
    protected ITextComponent getDefaultName() {
        return new TranslationTextComponent("container.distillery");
    }

    @Override
    protected Container createMenu(int id, PlayerInventory player) {
        return new DistilleryContainer(id, player, this, this.furnaceData);
    }

    @Override
    public void tick() {
        boolean flag = this.isBurning();
        boolean flag1 = false;

        if (!this.world.isRemote) {
            this.matchRecipe();
            if(this.canDistill(this.currentRecipe)) {
                this.burnTime = 1;
                this.cookTime++;

                if(this.cookTime == this.cookTimeTotal) {
                    this.cookTime = 0;
                    this.distill(this.currentRecipe);
                }
            }
            else {
                this.burnTime = 0;
                this.cookTime = 0;
            }


            if (flag != this.isBurning()) {
                flag1 = true;
                this.world.setBlockState(this.pos, this.world.getBlockState(this.pos).with(AbstractFurnaceBlock.LIT, this.isBurning()), 3);
            }
        }

        if (flag1) {
            this.markDirty();
        }

    }

    protected void distill(DistilleryRecipe recipeIn) {
        if (recipeIn != null) {
            List<ItemStack> itemsOut = new ArrayList<>();

            for(ItemStack itemStack : recipeIn.getItemsOut()) {
                itemsOut.add(itemStack.copy());
            }

            for (ItemStack item : itemsOut) {
                for (int i = 3; i < 7; i++) { // Fit items into existing stacks
                    ItemStack stack = this.inventoryContents.get(i);
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
                        if (this.inventoryContents.get(i).isEmpty()) {
                            this.inventoryContents.set(i, item.copy());
                            break;
                        }
                    }
                }
            }

            for(ItemStack item : recipeIn.getItemsIn()) {
                for (int i = 0; i < 3; i++) {
                    ItemStack stack = this.inventoryContents.get(i);
                    if(item.getItem() == stack.getItem()) {
                        stack.shrink(item.getCount());
                        break;
                    }
                }
            }

        }
    }

    protected boolean canDistill(@Nullable DistilleryRecipe recipeIn) {
        if(recipeIn != null) {
            List<ItemStack> itemsOut = new ArrayList<>(recipeIn.getItemsOut());
            List<ItemStack> outputSlots = new ArrayList<>(this.inventoryContents.subList(3, 7));

            List<ItemStack> toRemoveOut = new ArrayList<>();

            for(ItemStack item : itemsOut) { // Check for existing itemstacks of correct type and size.
                for(ItemStack stack : outputSlots) {
                    if(item.getItem() == stack.getItem()) {
                        if(item.getCount() + stack.getCount() <= stack.getMaxStackSize()) {
                            toRemoveOut.add(item);
                            break;
                        }
                        else {
                            item.shrink(stack.getMaxStackSize() - stack.getCount());
                        }
                    }
                }
            }

            itemsOut.removeAll(toRemoveOut); // Itemstacks have been accounted for
            toRemoveOut.clear();

            boolean[] isEmpty = new boolean[] { outputSlots.get(0).isEmpty(),  outputSlots.get(1).isEmpty(),  outputSlots.get(2).isEmpty(),  outputSlots.get(3).isEmpty() };
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
        if (world != null) {
            currentRecipe = world.getRecipeManager()
                    .getRecipes()
                    .stream()
                    .filter(recipe -> recipe instanceof DistilleryRecipe)
                    .map(recipe -> (DistilleryRecipe) recipe)
                    .filter(recipe -> matchRecipe(recipe, getItemsIn()))
                    .findFirst()
                    .orElse(null);
            if(currentRecipe != null) {
                this.cookTimeTotal = this.currentRecipe.getCookTime();
            }
        }
    }

    private boolean matchRecipe(DistilleryRecipe recipe, List<ItemStack> list) {
        return recipe.matches(list);
    }

    public DistilleryRecipe getCurrentRecipe() { return this.currentRecipe; }

    public List<ItemStack> getItemsIn() {
        return this.inventoryContents.subList(0, 3);
    }

}
