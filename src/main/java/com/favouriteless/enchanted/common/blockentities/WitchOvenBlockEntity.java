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
import com.favouriteless.enchanted.common.init.EnchantedRecipeTypes;
import com.favouriteless.enchanted.common.menus.WitchOvenMenu;
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
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractFurnaceBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class WitchOvenBlockEntity extends ProcessingBlockEntityBase implements MenuProvider {

    private final ItemStackHandler input = new ItemStackHandler(1);
    private final ItemStackHandler jar = new ItemStackHandler(1);
    private final ItemStackHandler fuel = new ItemStackHandler(1);
    private final ItemStackHandler output = new ItemStackHandler(2);

    private final LazyOptional<IItemHandlerModifiable> inputHandler = LazyOptional.of(() -> input);
    private final LazyOptional<IItemHandlerModifiable> jarHandler = LazyOptional.of(() -> jar);
    private final LazyOptional<IItemHandlerModifiable> fuelHandler = LazyOptional.of(() -> fuel);
    private final LazyOptional<IItemHandlerModifiable> outputHandler = LazyOptional.of(() -> output);

    private final RecipeWrapper recipeWrapper = new RecipeWrapper(input);
    private AbstractCookingRecipe currentSmeltingRecipe;

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
        super(EnchantedBlockEntityTypes.WITCH_OVEN.get(), pos, state);
    }

    public static <T extends BlockEntity> void tick(Level level, BlockPos blockPos, BlockState blockState, T t) {
        if(t instanceof WitchOvenBlockEntity be) {
            boolean startedBurning = be.isBurning();

            if(!level.isClientSide) {
                ItemStack fuelStack = be.fuel.getStackInSlot(0);

                if(be.isBurning())
                    be.burnTime--;

                if(be.isBurning() || !fuelStack.isEmpty()) {
                    be.matchRecipe();

                    if(be.canSmelt()) {
                        if(!be.isBurning()) { // Handle start burning fuel
                            be.burnTime = be.getBurnTime(fuelStack);
                            be.burnTimeTotal = be.burnTime;
                            if(be.isBurning()) { // In case item is not fuel (for some reason)
                                be.fuel.extractItem(0, 1, false);
                            }
                        }

                        if(be.isBurning()) {
                            be.cookTime++;
                            if(be.cookTime == be.cookTimeTotal) { // If finished cooking
                                be.cookTime = 0;
                                be.cookTimeTotal = 0;
                                be.smelt();
                            }
                        }
                        else if(be.cookTime > 0)
                            be.cookTime = Math.min(be.cookTime - 2, 0); // Was cooking but not burning anymore
                    }
                    else
                        be.cookTime = 0;

                }

                if(startedBurning != be.isBurning()) { // Update fume funnels and self if the burn state changed
                    level.setBlock(be.worldPosition, level.getBlockState(be.worldPosition).setValue(WitchOvenBlock.LIT, be.isBurning()), 3);
                    be.updateFumeFunnels();
                }
            }

            be.setChanged();
        }
    }

    protected boolean canSmelt() {
        if (currentSmeltingRecipe != null) { // If recipe not null
            ItemStack resultStack = currentSmeltingRecipe.getResultItem();

            if(resultStack.isEmpty()) // If recipe makes nothing
                return false;
            if(ForgeRegistries.ITEMS.tags().getTag(Tags.Items.ORES).contains(input.getStackInSlot(0).getItem())) // Can't smelt ores
                return false;

            ItemStack outputStack = output.getStackInSlot(0);
            if (outputStack.isEmpty()) // If output is empty
                return true;
            else if (!outputStack.sameItem(resultStack)) // If output is a different item
                return false;

            return outputStack.getCount() + resultStack.getCount() <= outputStack.getMaxStackSize(); // Can fit output into slot
        }
        return false;
    }

    protected void smelt() {
        if (currentSmeltingRecipe != null) {
            ItemStack inputStack = input.getStackInSlot(0);
            ItemStack fuelStack = fuel.getStackInSlot(0);
            ItemStack recipeResult = currentSmeltingRecipe.assemble(recipeWrapper);

            if(Enchanted.RANDOM.nextDouble() <= getByproductChance())
                createByproduct();

            output.insertItem(0, recipeResult, false);

            if (inputStack.getItem() == Blocks.WET_SPONGE.asItem() && !fuelStack.isEmpty() && fuelStack.getItem() == Items.BUCKET)
                fuel.setStackInSlot(0, new ItemStack(Items.WATER_BUCKET));

            input.extractItem(0, 1, false);
        }
    }

    private void createByproduct() {
        if(level != null) {
            level.getRecipeManager().getRecipeFor(EnchantedRecipeTypes.WITCH_OVEN, recipeWrapper, level).ifPresent(recipe -> {
                ItemStack result = recipe.assemble(recipeWrapper);

                int toTransfer = output.insertItem(1, result, true).getCount();
                if(!jar.extractItem(0, recipe.getResultItem().getCount() - toTransfer, false).isEmpty())
                    output.insertItem(1, result, false);
            });
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

    private void matchRecipe() {
        if (level != null) {
            if(currentSmeltingRecipe == null || !currentSmeltingRecipe.matches(recipeWrapper, level))
                currentSmeltingRecipe = level.getRecipeManager().getRecipeFor(RecipeType.SMELTING, recipeWrapper, level).orElse(null);

            if(currentSmeltingRecipe != null)
                cookTimeTotal = (int)Math.round(currentSmeltingRecipe.getCookingTime() * 0.8D);
        }
    }

    public ItemStackHandler getInputInventory() {
        return input;
    }

    public ItemStackHandler getFuelInventory() {
        return fuel;
    }

    public ItemStackHandler getJarInventory() {
        return jar;
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
        return getDroppableInventoryFor(input, jar, fuel, output);
    }

    @Override
    public Component getDisplayName() {
        return new TranslatableComponent("container.enchanted.witch_oven");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory playerInventory, Player player) {
        return new WitchOvenMenu(id, playerInventory, this, data);
    }


    @Override
    public void saveAdditional(CompoundTag nbt) {
        super.saveAdditional(nbt);
        nbt.putInt("burnTime", burnTime);
        nbt.putInt("burnTimeTotal", burnTimeTotal);
        nbt.putInt("cookTime", cookTime);
        nbt.putInt("cookTimeTotal", cookTimeTotal);
        nbt.put("input", input.serializeNBT());
        nbt.put("jar", jar.serializeNBT());
        nbt.put("fuel", fuel.serializeNBT());
        nbt.put("output", output.serializeNBT());
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        burnTime = nbt.getInt("burnTime");
        burnTimeTotal = nbt.getInt("burnTimeTotal");
        cookTime = nbt.getInt("cookTime");
        cookTimeTotal = nbt.getInt("cookTimeTotal");
        input.deserializeNBT(nbt.getCompound("input"));
        jar.deserializeNBT(nbt.getCompound("jar"));
        fuel.deserializeNBT(nbt.getCompound("fuel"));
        output.deserializeNBT(nbt.getCompound("output"));
    }

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if(cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            if(side == null)
                return super.getCapability(cap, side);

            if(side == Direction.UP)
                return inputHandler.cast();
            if(side == Direction.DOWN)
                return outputHandler.cast();
            if(side == level.getBlockState(worldPosition).getValue(WitchOvenBlock.FACING))
                return jarHandler.cast();

            return fuelHandler.cast();
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
        if(fuelHandler != null)
            fuelHandler.invalidate();
        if(outputHandler != null)
            outputHandler.invalidate();
    }

    private boolean isBurning() {
        return this.burnTime > 0;
    }

}