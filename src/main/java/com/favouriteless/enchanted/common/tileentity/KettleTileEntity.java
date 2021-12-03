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

import com.favouriteless.enchanted.EnchantedConfig;
import com.favouriteless.enchanted.api.altar.IAltarPowerConsumer;
import com.favouriteless.enchanted.client.particles.SimpleColouredParticleType.SimpleColouredData;
import com.favouriteless.enchanted.common.blocks.KettleBlock;
import com.favouriteless.enchanted.common.init.EnchantedParticles;
import com.favouriteless.enchanted.common.init.EnchantedTileEntities;
import com.favouriteless.enchanted.common.recipes.kettle.KettleRecipe;
import com.favouriteless.enchanted.core.util.PlayerInventoryHelper;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CampfireBlock;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.fluid.Fluids;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.LockableLootTileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.wrapper.InvWrapper;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class KettleTileEntity extends LockableLootTileEntity implements ITickableTileEntity, IAltarPowerConsumer {

    private final FluidTank tank = new FluidTank(FluidAttributes.BUCKET_VOLUME, (fluid) -> fluid.getFluid() == Fluids.WATER);
    private final LazyOptional<IFluidHandler> fluidHandler = LazyOptional.of(() -> tank);

    private NonNullList<ItemStack> inventoryContents = NonNullList.create();
    private final IItemHandlerModifiable items = new InvWrapper(this);
    private final LazyOptional<IItemHandlerModifiable> itemHandler = LazyOptional.of(() -> items);

    private static final int WARMING_MAX = 80;
    private static final int COOK_TIME = 160;

    private final List<AltarTileEntity> potentialAltars = new ArrayList<>();
    private List<KettleRecipe> potentialRecipes = new ArrayList<>();

    private ItemStack itemOut = ItemStack.EMPTY;
    private int cookProgress = 0;
    private int warmingUp = 0;
    public boolean isFailed = false;
    public boolean isComplete = false;

    private boolean justLoaded = false;

    // Only needed client side (rendering)
    private static final Random RANDOM = new Random();
    public boolean hasItems = false;
    public int targetRenderColour = 0x3F76E4;
    public int currentRed = (targetRenderColour >> 16) & 0xFF;
    public int currentGreen = (targetRenderColour >> 8) & 0xFF;
    public int currentBlue = (targetRenderColour) & 0xFF;
    public long timeLastFrame = System.currentTimeMillis();


    public KettleTileEntity() {
        super(EnchantedTileEntities.KETTLE.get());
    }

    @Override
    public void tick() {
        if(level != null) {
            if (!level.isClientSide) {
                if (justLoaded) {
                    matchRecipes();
                    justLoaded = false;
                }
                if (!isFailed && !isComplete) {

                    BlockState stateBelow = level.getBlockState(worldPosition.below());

                    if (providesHeat(stateBelow) && tank.getFluidAmount() == tank.getCapacity()) { // On top of heat block, cauldron is full
                        if (warmingUp < WARMING_MAX) {
                            warmingUp++;
                        } else if (potentialRecipes.size() == 1 && potentialRecipes.get(0).fullMatch(this, level)) { // Has final recipe
                            if (cookProgress < COOK_TIME) {
                                cookProgress++;
                                recalculateTargetColour();
                            } else {
                                KettleRecipe recipe = potentialRecipes.get(0);
                                if(recipe.getPower() <= 0) {
                                    setComplete();
                                }
                                else if(!potentialAltars.isEmpty() && potentialAltars.get(0).currentPower > recipe.getPower()) {
                                    potentialAltars.get(0).currentPower -= recipe.getPower();
                                    setComplete();
                                }
                                else {
                                    setFailed(); // Fail if not enough power
                                }
                            }
                        }
                    } else {
                        if (cookProgress != 0)
                            setFailed(); // Fail if heat or water is lost while cooking
                        warmingUp = 0;
                    }
                }
                updateBlock();
            }
            // ------------------------ CLIENT STUFF ------------------------
            else {
                if(justLoaded) {
                    justLoaded = false;
                    currentRed = (targetRenderColour >> 16) & 0xFF;
                    currentGreen = (targetRenderColour >> 8) & 0xFF;
                    currentBlue = (targetRenderColour) & 0xFF;
                }
                double yStart = (level.getBlockState(worldPosition).getValue(KettleBlock.TYPE) == 1) ? 0.1875D : 0.0625D;

                if(warmingUp == WARMING_MAX && RANDOM.nextInt(10) > 2) {
                    double dx = worldPosition.getX() + 0.3125D + (Math.random() * 0.375D);
                    double dy = worldPosition.getY() + yStart + (0.25D * (tank.getFluidAmount() / 1000D));
                    double dz = worldPosition.getZ() + 0.3125D + (Math.random() * 0.375D);

                    level.addParticle(new SimpleColouredData(EnchantedParticles.BOILING.get(), currentRed, currentGreen, currentBlue), dx, dy, dz, 0D, 0D, 0D);
                }
                if(!isFailed) {
                    if(!isComplete && cookProgress > 0 && cookProgress < COOK_TIME && RANDOM.nextInt(10) > 5) {
                        double dx = worldPosition.getX() + Math.random();
                        double dy = worldPosition.getY() + Math.random();
                        double dz = worldPosition.getZ() + Math.random();

                        level.addParticle(new SimpleColouredData(EnchantedParticles.KETTLE_COOK.get(), currentRed, currentGreen, currentBlue), dx, dy, dz, 0D, 0D, 0D);
                    }
                    else if (warmingUp == WARMING_MAX && hasItems && RANDOM.nextInt(10) > 8) {
                        double xOffset = 0.3125D + (Math.random() * 0.375D);
                        double zOffset = 0.3125D + (Math.random() * 0.375D);
                        double dx = worldPosition.getX() + xOffset;
                        double dy = worldPosition.getY() + yStart + (0.25D * (tank.getFluidAmount() / 1000D));
                        double dz = worldPosition.getZ() + zOffset;
                        Vector3d velocity = new Vector3d(xOffset, 0, zOffset).subtract(0.5D, 0.0D, 0.5D).normalize().scale((1D + Math.random()) * 0.06D);

                        level.addParticle(new SimpleColouredData(EnchantedParticles.CAULDRON_BREW.get(), currentRed, currentGreen, currentBlue), dx, dy, dz, velocity.x, (1.0D + Math.random()) * 0.06D, velocity.z);
                    }
                }
            }
        }
    }

    private void setFailed() {
        itemOut = ItemStack.EMPTY;
        resetValues();
        isFailed = true;
        recalculateTargetColour();
    }

    private void setComplete() {
        itemOut = potentialRecipes.get(0).getResultItem().copy();
        resetValues();
        isComplete = true;
        recalculateTargetColour();
    }

    public void takeContents(PlayerEntity player) {
        if(level != null && !level.isClientSide) {
            if(isFailed) {
                setWater(0);
            }
            else {
                tank.drain(new FluidStack(Fluids.WATER, tank.getCapacity() / potentialRecipes.get(0).getResultItem().getCount()), IFluidHandler.FluidAction.EXECUTE);
            }
            if(player != null) {
                PlayerInventoryHelper.tryGiveItem(player, isFailed ? new ItemStack(Items.WATER_BUCKET) : new ItemStack(itemOut.getItem()));
            }
            else {
                level.addFreshEntity(new ItemEntity(level, worldPosition.getX(), worldPosition.getY() + 1, worldPosition.getZ(), new ItemStack(itemOut.getItem())));
            }
            itemOut.shrink(1);
            if(itemOut.isEmpty()) {
                inventoryContents.clear();
                potentialRecipes.clear();

                level.playSound(null, worldPosition, SoundEvents.BUCKET_EMPTY, SoundCategory.PLAYERS, 1.0F, 1.0F);
                resetValues();
                recalculateTargetColour();
            }
            updateBlock();
        }
    }

    private void resetValues() {
        isFailed = false;
        isComplete = false;
        cookProgress = 0;
    }

    public void addItem(ItemEntity itemEntity) {
        if(itemEntity.isAlive()) {
            inventoryContents.add(itemEntity.getItem());
            matchRecipes();

            if (potentialRecipes.isEmpty()) {
                if(EnchantedConfig.KETTLE_ITEM_SPOIL.get()) {
                    setFailed();
                }
                else {
                    inventoryContents.remove(itemEntity.getItem());
                    return;
                }
            }

            if(level != null) level.playSound(null, worldPosition, SoundEvents.BUCKET_EMPTY, SoundCategory.PLAYERS, 1.0F, 1.0F);
            itemEntity.kill();
            recalculateTargetColour();
            updateBlock();
        }
    }

    public boolean addWater(int amount) {
        if(!isComplete && !isFailed) {
            if (tank.getFluidAmount() < tank.getCapacity()) {
                tank.fill(new FluidStack(Fluids.WATER, amount), IFluidHandler.FluidAction.EXECUTE);
                updateBlock();
                return true;
            }
        }
        return false;
    }

    public boolean removeWater(int amount) {
        if(!isComplete && !isFailed) {
            if (tank.getFluidAmount() >= amount) {
                tank.drain(new FluidStack(Fluids.WATER, amount), IFluidHandler.FluidAction.EXECUTE);
                updateBlock();
                return true;
            }
        }
        return false;
    }

    public void setWater(int amount) {
        tank.setFluid(new FluidStack(Fluids.WATER, amount));
        updateBlock();
    }

    public int getWater() {
        return tank.getFluidAmount();
    }

    public boolean isFull() {
        return tank.getFluidAmount() == tank.getCapacity();
    }

    private void matchRecipes() {
        if (level != null) {
            potentialRecipes = level.getRecipeManager()
                    .getRecipes()
                    .stream()
                    .filter(recipe -> recipe instanceof KettleRecipe)
                    .map(recipe -> (KettleRecipe)recipe)
                    .filter(recipe -> recipe.matches(this, level))
                    .collect(Collectors.toList());
        }
    }

    public static boolean providesHeat(BlockState state){
        return  state.getBlock() == Blocks.FIRE ||
                state.getBlock() == Blocks.SOUL_FIRE ||
                state.getBlock() == Blocks.LAVA ||
                state.getBlock() == Blocks.CAMPFIRE && state.getValue(CampfireBlock.LIT) ||
                state.getBlock() == Blocks.SOUL_CAMPFIRE && state.getValue(CampfireBlock.LIT) ||
                state.getBlock() == Blocks.MAGMA_BLOCK;
    }

    private void recalculateTargetColour() {
        if(inventoryContents.isEmpty()) {
            targetRenderColour = 0x3F76E4;
        }
        else if(isComplete) {
            targetRenderColour = potentialRecipes.get(0).getFinalColour();
        }
        else if(isFailed) {
            targetRenderColour = 0x96642F;
        }
        else if(!potentialRecipes.isEmpty()) {
            targetRenderColour = potentialRecipes.get(0).getCookingColour();
        }
        else {
            int red = RANDOM.nextInt(80);
            int green = RANDOM.nextInt(80);
            int blue = RANDOM.nextInt(80);
            targetRenderColour = 0xFF000000 | (red << 16) & 0x00FF0000 | (green << 8) & 0x0000FF00 | blue & 0x000000FF;
        }
    }

    private void updateBlock() {
        if(level != null && !level.isClientSide) {
            BlockState state = level.getBlockState(worldPosition);
            level.sendBlockUpdated(worldPosition, state, state, 2);
        }
    }

    public boolean isHot() {
        return warmingUp == WARMING_MAX;
    }

    @Override
    public CompoundNBT save(CompoundNBT nbt) {
        nbt.putInt("waterAmount", tank.getFluidAmount());
        nbt.putInt("renderColour", targetRenderColour);
        nbt.putBoolean("isFailed", isFailed);
        nbt.putBoolean("isComplete", isComplete);
        nbt.putInt("warmingUp", warmingUp);
        nbt.putInt("cookProgress", cookProgress);
        ItemStackHelper.saveAllItems(nbt, inventoryContents);
        if(itemOut != ItemStack.EMPTY) {
            CompoundNBT itemNbt = new CompoundNBT();
            itemNbt.putString("item", itemOut.getItem().getRegistryName().toString());
            itemNbt.putInt("count", itemOut.getCount());
            nbt.put("itemOut", itemNbt);
        }

        return super.save(nbt);
    }

    @Override
    public void load(BlockState state, CompoundNBT nbt) {
        super.load(state, nbt);
        setWater(nbt.getInt("waterAmount"));
        targetRenderColour = nbt.getInt("renderColour");
        isFailed = nbt.getBoolean("isFailed");
        isComplete = nbt.getBoolean("isComplete");
        warmingUp = nbt.getInt("warmingUp");
        cookProgress = nbt.getInt("cookProgress");

        // Have to load nbt weirdly because inventory is not a fixed size
        ListNBT listnbt = nbt.getList("Items", 10);
        for(int i = 0; i < listnbt.size(); ++i) {
            CompoundNBT compoundnbt = listnbt.getCompound(i);
            inventoryContents.add(ItemStack.of(compoundnbt));
        }

        if(nbt.contains("itemOut")) {
            CompoundNBT itemNbt = (CompoundNBT)nbt.get("itemOut");
            itemOut = new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemNbt.getString("item"))), nbt.getInt("count"));
        }

        justLoaded = true;
    }

    @Nullable
    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putInt("waterAmount", tank.getFluidAmount());
        nbt.putInt("renderColour", targetRenderColour);
        nbt.putBoolean("isFailed", isFailed);
        nbt.putBoolean("isComplete", isComplete);
        nbt.putInt("warmingUp", warmingUp);
        nbt.putInt("cookProgress", cookProgress);
        nbt.putBoolean("hasItems", !inventoryContents.isEmpty());
        return new SUpdateTileEntityPacket(worldPosition, -1, nbt);
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        CompoundNBT nbt = pkt.getTag();
        setWater(nbt.getInt("waterAmount"));
        targetRenderColour = nbt.getInt("renderColour");
        isFailed = nbt.getBoolean("isFailed");
        isComplete = nbt.getBoolean("isComplete");
        warmingUp = nbt.getInt("warmingUp");
        cookProgress = nbt.getInt("cookProgress");
        hasItems = nbt.getBoolean("hasItems");
    }

    @Override
    @Nonnull
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction facing) {
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
            return fluidHandler.cast();
        else if(capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
            return itemHandler.cast();
        return super.getCapability(capability, facing);
    }

    @Override
    protected ITextComponent getDefaultName() {
        return new TranslationTextComponent("container.kettle");
    }

    @Override
    protected Container createMenu(int pId, PlayerInventory pPlayer) {
        return null;
    }

    @Override
    protected NonNullList<ItemStack> getItems() {
        return inventoryContents;
    }

    @Override
    protected void setItems(NonNullList<ItemStack> pItems) {
        inventoryContents = pItems;
    }

    @Override
    public int getContainerSize() {
        return inventoryContents.size();
    }

    @Override
    public List<AltarTileEntity> getAltars() {
        return potentialAltars;
    }

    @Override
    public void removeAltar(AltarTileEntity altar) {
        potentialAltars.remove(altar);
    }

    @Override
    public void addAltar(AltarTileEntity altar) {
        potentialAltars.add(altar);
    }
}
