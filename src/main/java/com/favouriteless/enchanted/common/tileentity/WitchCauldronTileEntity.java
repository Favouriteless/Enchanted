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
import com.favouriteless.enchanted.api.altar.AltarPowerHelper;
import com.favouriteless.enchanted.api.altar.IAltarPowerConsumer;
import com.favouriteless.enchanted.client.particles.SimpleColouredParticleType.SimpleColouredData;
import com.favouriteless.enchanted.common.init.EnchantedParticles;
import com.favouriteless.enchanted.common.init.EnchantedTileEntities;
import com.favouriteless.enchanted.common.recipes.witch_cauldron.WitchCauldronRecipe;
import com.favouriteless.enchanted.core.util.PlayerInventoryHelper;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CampfireBlock;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.fluid.Fluids;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.LockableLootTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.crafting.CraftingHelper;
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

public class WitchCauldronTileEntity extends LockableLootTileEntity implements ITickableTileEntity, IAltarPowerConsumer {

    private final FluidTank tank = new FluidTank(FluidAttributes.BUCKET_VOLUME*3, (fluid) -> fluid.getFluid() == Fluids.WATER);
    private final LazyOptional<IFluidHandler> fluidHandler = LazyOptional.of(() -> tank);

    private NonNullList<ItemStack> inventoryContents = NonNullList.create();
    private final IItemHandlerModifiable items = new InvWrapper(this);
    private final LazyOptional<IItemHandlerModifiable> itemHandler = LazyOptional.of(() -> items);

    private static final int WARMING_MAX = 80;
    private static final int COOK_TIME = 200;
    private static final long BLENDING_MILLISECONDS = 500;

    private final List<BlockPos> potentialAltars = new ArrayList<>();
    private List<WitchCauldronRecipe> potentialRecipes = new ArrayList<>();

    private ItemStack itemOut = ItemStack.EMPTY;
    private int cookProgress = 0;
    private int warmingUp = 0;
    public boolean isFailed = false;
    public boolean isComplete = false;

    private boolean justLoaded = false;

    // Only needed client side (rendering)
    private static final Random RANDOM = new Random();
    public boolean hasItems = false;

    public int targetRed = 63;
    public int targetGreen = 118;
    public int targetBlue = 228;
    public int startRed = targetRed;
    public int startGreen = targetGreen;
    public int startBlue = targetBlue;
    public long startTime = System.currentTimeMillis();

    public WitchCauldronTileEntity() {
        super(EnchantedTileEntities.WITCH_CAULDRON.get());
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
                                WitchCauldronRecipe recipe = potentialRecipes.get(0);
                                AltarTileEntity altar = AltarPowerHelper.tryGetAltar(level, potentialAltars);
                                if(recipe.getPower() <= 0) {
                                    setComplete();
                                }
                                else if(altar != null && altar.currentPower > recipe.getPower()) {
                                    altar.currentPower -= recipe.getPower();
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
                    startRed = targetRed;
                    startGreen = targetGreen;
                    startBlue = targetBlue;
                }
                long time = System.currentTimeMillis() - startTime;

                if(warmingUp == WARMING_MAX && RANDOM.nextInt(10) > 2) {
                    double dx = worldPosition.getX() + 0.25D + (Math.random() * 0.5D);
                    double dy = worldPosition.getY() + 0.1875 + (0.15625D * (tank.getFluidAmount() / 1000D));
                    double dz = worldPosition.getZ() + 0.25D + (Math.random() * 0.5D);

                    level.addParticle(new SimpleColouredData(EnchantedParticles.BOILING.get(), getRed(time), getGreen(time), getBlue(time)), dx, dy, dz, 0D, 0D, 0D);
                }
                if(!isFailed) {
                    if(!isComplete && cookProgress > 0 && cookProgress < COOK_TIME) {
                        double dx = worldPosition.getX() + 0.5D;
                        double dy = worldPosition.getY() + 0.1875 + (0.15625D * (tank.getFluidAmount() / 1000D));
                        double dz = worldPosition.getZ() + 0.5D;

                        level.addParticle(new SimpleColouredData(EnchantedParticles.CAULDRON_COOK.get(), getRed(time), getGreen(time), getBlue(time)), dx, dy, dz, 0.0D, 0.0D, 0.0D);
                    }
                    else if (warmingUp == WARMING_MAX && hasItems && RANDOM.nextInt(10) > 6) {
                        double xOffset = 0.25D + (Math.random() * 0.5D);
                        double zOffset = 0.25D + (Math.random() * 0.5D);
                        double dx = worldPosition.getX() + xOffset;
                        double dy = worldPosition.getY() + 0.1875 + (0.15625D * (tank.getFluidAmount() / 1000D));
                        double dz = worldPosition.getZ() + zOffset;
                        Vector3d velocity = new Vector3d(xOffset, 0, zOffset).subtract(0.5D, 0.0D, 0.5D).normalize().scale((1D + Math.random()) * 0.06D);

                        level.addParticle(new SimpleColouredData(EnchantedParticles.CAULDRON_BREW.get(), getRed(time), getGreen(time), getBlue(time)), dx, dy, dz, velocity.x, (1.0D + Math.random()) * 0.06D, velocity.z);
                    }
                }
            }
        }
    }

    public int getRed(long time) {
        return (int)Math.round(MathHelper.lerp(Math.min((double)time / BLENDING_MILLISECONDS, 1.0D), startRed, targetRed));
    }

    public int getGreen(long time) {
        return (int)Math.round(MathHelper.lerp(Math.min((double)time / BLENDING_MILLISECONDS, 1.0D), startGreen, targetGreen));
    }

    public int getBlue(long time) {
        return (int)Math.round(MathHelper.lerp(Math.min((double)time / BLENDING_MILLISECONDS, 1.0D), startBlue, targetBlue));
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
                level.playSound(null, worldPosition, SoundEvents.BUCKET_EMPTY, SoundCategory.PLAYERS, 1.0F, 1.0F);
            }
            else {
                tank.drain(new FluidStack(Fluids.WATER, tank.getCapacity() / potentialRecipes.get(0).getResultItem().getCount()), IFluidHandler.FluidAction.EXECUTE);
            }
            if(!itemOut.isEmpty()) {
                if(player != null) {
                    PlayerInventoryHelper.tryGiveItem(player, isFailed ? new ItemStack(Items.WATER_BUCKET) : new ItemStack(itemOut.getItem()));
                }
                else {
                    level.addFreshEntity(new ItemEntity(level, worldPosition.getX(), worldPosition.getY() + 1, worldPosition.getZ(), new ItemStack(itemOut.getItem())));
                }
                level.playSound(null, worldPosition, SoundEvents.BUCKET_EMPTY, SoundCategory.PLAYERS, 1.0F, 1.0F);
            }
            itemOut.shrink(1);
            if(itemOut.isEmpty()) {
                inventoryContents.clear();
                potentialRecipes.clear();

                resetValues();
                recalculateTargetColour();
            }
            recalculateTargetColour();
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
                if(EnchantedConfig.CAULDRON_ITEM_SPOIL.get()) {
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
                recalculateTargetColour();
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
                    .filter(recipe -> recipe instanceof WitchCauldronRecipe)
                    .map(recipe -> (WitchCauldronRecipe)recipe)
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
            targetRed = 63;
            targetGreen = 118;
            targetBlue = 228;
        }
        else if(isComplete) {
            targetRed = potentialRecipes.get(0).getFinalRed();
            targetGreen = potentialRecipes.get(0).getFinalGreen();
            targetBlue = potentialRecipes.get(0).getFinalBlue();
        }
        else if(isFailed) {
            targetRed = 150;
            targetGreen = 100;
            targetBlue = 47;
        }
        else if(!potentialRecipes.isEmpty() && cookProgress > 0) {
            targetRed = potentialRecipes.get(0).getCookingRed();
            targetGreen = potentialRecipes.get(0).getCookingGreen();
            targetBlue = potentialRecipes.get(0).getCookingBlue();
        }
        else {
            targetRed = RANDOM.nextInt(80);
            targetGreen = RANDOM.nextInt(80);
            targetBlue = RANDOM.nextInt(80);
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
        nbt.putInt("targetRed", targetRed);
        nbt.putInt("targetGreen", targetGreen);
        nbt.putInt("targetBlue", targetBlue);
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
        AltarPowerHelper.savePosTag(potentialAltars, nbt);
        return super.save(nbt);
    }

    @Override
    public void load(BlockState state, CompoundNBT nbt) {
        super.load(state, nbt);
        AltarPowerHelper.loadPosTag(potentialAltars, nbt);
        setWater(nbt.getInt("waterAmount"));
        targetRed = nbt.getInt("targetRed");
        targetGreen = nbt.getInt("targetGreen");
        targetBlue = nbt.getInt("targetBlue");
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
            itemOut = new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemNbt.getString("item"))), itemNbt.getInt("count"));
        }

        justLoaded = true;
    }

    @Nullable
    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putInt("waterAmount", tank.getFluidAmount());
        nbt.putInt("targetRed", targetRed);
        nbt.putInt("targetGreen", targetGreen);
        nbt.putInt("targetBlue", targetBlue);
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
        isFailed = nbt.getBoolean("isFailed");
        isComplete = nbt.getBoolean("isComplete");
        warmingUp = nbt.getInt("warmingUp");
        cookProgress = nbt.getInt("cookProgress");
        hasItems = nbt.getBoolean("hasItems");

        int tr = nbt.getInt("targetRed");
        int tg = nbt.getInt("targetGreen");
        int tb = nbt.getInt("targetBlue");

        if(tr != targetRed && tg != targetGreen && tb != targetBlue) {
            startTime = System.currentTimeMillis();
            startRed = getRed(startTime);
            startGreen = getGreen(startTime);
            startBlue = getBlue(startTime);
            targetRed = tr;
            targetGreen = tg;
            targetBlue = tb;
        }
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
        return new TranslationTextComponent("container.witch_cauldron");
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
    public List<BlockPos> getAltarPositions() {
        return potentialAltars;
    }

    @Override
    public void removeAltar(BlockPos altarPos) {
        potentialAltars.remove(altarPos);
    }

    @Override
    public void addAltar(BlockPos altarPos) {
        AltarPowerHelper.addAltarByClosest(potentialAltars, level, worldPosition, altarPos);
    }
}
