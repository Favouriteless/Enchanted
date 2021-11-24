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

import com.favouriteless.enchanted.Enchanted;
import com.favouriteless.enchanted.EnchantedConfig;
import com.favouriteless.enchanted.api.altar.IAltarPowerConsumer;
import com.favouriteless.enchanted.api.altar.IAltarPowerConsumerProvider;
import com.favouriteless.enchanted.common.blocks.altar.AltarBlock;
import com.favouriteless.enchanted.common.containers.AltarContainer;
import com.favouriteless.enchanted.common.init.EnchantedTileEntities;
import com.favouriteless.enchanted.common.observerlib.altar.AltarObserverProvider;
import com.favouriteless.enchanted.core.util.reloadlisteners.AltarPowerReloadListener;
import com.favouriteless.enchanted.core.util.reloadlisteners.AltarPowerReloadListener.*;
import hellfirepvp.observerlib.api.ChangeSubscriber;
import hellfirepvp.observerlib.api.ObserverHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.IIntArray;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.Tags.IOptionalNamedTag;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AltarTileEntity extends TileEntity implements ITickableTileEntity, INamedContainerProvider {

    public double maxPower;
    public double currentPower;
    public double rechargeMultiplier = 1.0D;
    public double powerMultiplier = 1.0D;
    public Vector3d centerPos;
    public final AltarBlockData altarBlockData = new AltarBlockData();

    private final IIntArray fields = new IIntArray() {
        @Override
        public int get(int pIndex) {
            switch(pIndex) {
                case 0:
                    return (int)Math.round(currentPower);
                case 1:
                    return (int)Math.round(maxPower);
                case 2:
                    return (int)Math.round(rechargeMultiplier);
                default:
                    return 0;
            }
        }

        @Override
        public void set(int pIndex, int pValue) {
            switch(pIndex) {
                case 0:
                    currentPower = pValue;
                case 1:
                    maxPower = pValue;
                case 2:
                    rechargeMultiplier = pValue;
            }
        }

        @Override
        public int getCount() {
            return 3;
        }
    };

    private ChangeSubscriber<?> changeSubscriber;
    private final List<IAltarPowerConsumer> powerConsumers = new ArrayList<>();

    private boolean loaded = false;
    private boolean facingX;
    private final double rechargeRate = EnchantedConfig.ALTAR_BASE_RECHARGE.get();

    private int ticksAlive = 0;

    public AltarTileEntity(TileEntityType<?> type) {
        super(type);
    }

    public AltarTileEntity() {
        this(EnchantedTileEntities.ALTAR.get());
    }

    @Override
    public void tick() {
        if(level != null) {

            if (!loaded) {
                facingX = level.getBlockState(worldPosition).getValue(AltarBlock.FACING_X);
                centerPos = facingX ?
                        Vector3d.atLowerCornerOf(worldPosition).add(1.0D, 0.0D, 0.5D) :
                        Vector3d.atLowerCornerOf(worldPosition).add(0.5D, 0.0D, 1.0D);

                changeSubscriber = ObserverHelper.getHelper().getSubscriber(level, worldPosition);
                if (changeSubscriber == null) {
                    createChangeSubscriber();
                }
                recalculateUpgrades();
                recalculateBlocks();
                loaded = true;
            }
            else if (!level.isClientSide) {
                if(ticksAlive % 10 == 0) {
                    changeSubscriber.isValid(level);
                }
                if(currentPower <= maxPower)
                    currentPower += rechargeRate * rechargeMultiplier;
                if(currentPower > maxPower)
                    currentPower = maxPower;
            }
            ticksAlive++;
        }
    }

    @Override
    public CompoundNBT save(CompoundNBT nbt) {
        nbt.putDouble("currentPower", currentPower);
        return super.save(nbt);
    }

    @Override
    public void load(BlockState state, CompoundNBT nbt) {
        super.load(state, nbt);
        this.currentPower = nbt.getDouble("currentPower");
    }

    public void recalculateBlocks() {
        if(level != null && !level.isClientSide) {
            int range = EnchantedConfig.ALTAR_RANGE.get();
            BlockPos startingPos = facingX ?
                    new BlockPos(centerPos.add(-(range+4), -(range+2), -(range+2))) :
                    new BlockPos(centerPos.add(-(range+2), -(range+2), -(range+4)));

            int newPower = 0;

            altarBlockData.reset();
            for (int x = 0; x < (range+2)*2; x++) {
                for (int y = 0; y < (range+2)*2; y++) {
                    for (int z = 0; z < (range+2)*2; z++) {
                        BlockPos currentPos = startingPos.offset(x, y, z);
                        if(level.getBlockState(currentPos).getBlock() instanceof IAltarPowerConsumerProvider) {
                            addConsumer(((IAltarPowerConsumer) level.getBlockEntity(currentPos)));
                        }
                        else if(posWithinRange(currentPos, range)) {
                            newPower += altarBlockData.addBlock(level.getBlockState(currentPos).getBlock(), this.powerMultiplier);
                        }
                    }
                }
            }
            setPower(newPower);
        }
    }

    public void recalculateUpgrades() {
        if (level != null) {
            int xMax;
            int zMax;
            if(facingX) {
                xMax = 3;
                zMax = 2;
            } else {
                xMax = 2;
                zMax = 3;
            }
            List<AltarUpgrade> upgradesUsed = new ArrayList<>();

            for (int x = 0; x < xMax; x++) {
                for (int z = 0; z < zMax; z++) {
                    for (AltarUpgrade newUpgrade : AltarPowerReloadListener.INSTANCE.UPGRADES) {
                        if (level.getBlockState(worldPosition.offset(x, 1, z)).getBlock().is(newUpgrade.getBlock())) { // If block is of upgrade
                            if(upgradesUsed.isEmpty()) {
                                upgradesUsed.add(newUpgrade);
                            }
                            else {
                                AltarUpgrade match = null;
                                for(AltarUpgrade upgrade : upgradesUsed) {
                                    if(newUpgrade.getType().equals(upgrade.getType())) { // If same type
                                        match = upgrade;
                                    }
                                }
                                if(match != null) {
                                    if(newUpgrade.getPriority() > match.getPriority()) {
                                        upgradesUsed.remove(match);
                                        upgradesUsed.add(newUpgrade);
                                    }
                                }
                                else {
                                    upgradesUsed.add(newUpgrade);
                                }
                            }
                        }
                    }
                }
            }

            double newPowerMultiplier = 1.0D;
            double newRechargeMultiplier = 1.0D;
            for(AltarUpgrade upgrade : upgradesUsed) {
                newPowerMultiplier += upgrade.getPowerMultiplier();
                newRechargeMultiplier += upgrade.getRechargeMultiplier();
            }

            if(this.powerMultiplier != newPowerMultiplier) {
                this.setPower(altarBlockData.recalculatePower(newPowerMultiplier));
            }
            this.powerMultiplier = newPowerMultiplier;
            this.rechargeMultiplier = newRechargeMultiplier;
        }
    }

    public boolean posWithinRange(BlockPos pos, int range) {
        if(this.level != null) {
            double rx = facingX ? range+1 : range;
            double rz = facingX ? range : range+1;
            double dx = pos.getX() - centerPos.x;
            double dy = pos.getY() - centerPos.y;
            double dz = pos.getZ() - centerPos.z;
            return (dx * dx) / (rx * rx) + (dy * dy) / (range * range) + (dz * dz) / (rz * rz) <= 1;
        }
        return false;
    }

    public void addConsumer(IAltarPowerConsumer consumer) {
        powerConsumers.add(consumer);
        consumer.addAltar(this);
    }

    public void removeConsumer(IAltarPowerConsumer consumer) {
        powerConsumers.remove(consumer);
        consumer.removeAltar(this);
    }

    /**
     * Destroys consumer references
     */
    public void clearConsumers() {
        for(IAltarPowerConsumer consumer : powerConsumers) {
            consumer.removeAltar(this);
        }
    }

    public double distanceTo(BlockPos pos) {
        double dx = pos.getX() - centerPos.x;
        double dy = pos.getY() - centerPos.y;
        double dz = pos.getZ() - centerPos.z;
        return Math.sqrt((dx * dx) + (dy * dy) + (dz * dz));
    }

    public boolean posIsUpgrade(BlockPos pos) {
        if(this.level != null) {
            int xMax;
            int zMax;

            if(facingX) {
                xMax = 2;
                zMax = 1;
            } else {
                xMax = 1;
                zMax = 2;
            }

            return pos.getY() == worldPosition.getY()+1 && pos.getX() - worldPosition.getX() <= xMax && pos.getZ() - worldPosition.getZ() <= zMax;
        }
        return false;
    }

    public void setPower(double power) {
        maxPower = power;
        if(currentPower > maxPower) currentPower = maxPower;
    }

    public void addPower(Block block) {
        this.maxPower += altarBlockData.addBlock(block, this.powerMultiplier);
    }

    public void removePower(Block block) {
        this.maxPower -= altarBlockData.removeBlock(block, this.powerMultiplier);
        if(currentPower > maxPower) currentPower = maxPower;
    }

    private void createChangeSubscriber() {
        changeSubscriber = ObserverHelper.getHelper().observeArea(level, worldPosition, new AltarObserverProvider(new ResourceLocation(Enchanted.MOD_ID, "altar_observer")));
    }

    @Override
    public ITextComponent getDisplayName() {
        return new TranslationTextComponent("container.enchanted.altar");
    }

    @Nullable
    @Override
    public Container createMenu(int id, PlayerInventory playerInventory, PlayerEntity player) {
        return new AltarContainer(id, playerInventory, this, this.fields);
    }

    public static class AltarBlockData {
        public HashMap<Block, Integer> blocksAmount = new HashMap<>();
        public HashMap<IOptionalNamedTag<Block>, Integer> tagsAmount = new HashMap<>();

        public AltarBlockData() {
            for(AltarPowerReloadListener.AltarPowerProvider<Block> provider : AltarPowerReloadListener.INSTANCE.POWER_BLOCKS) {
                blocksAmount.put(provider.getKey(), 0);
            }
            for(AltarPowerReloadListener.AltarPowerProvider<IOptionalNamedTag<Block>> provider : AltarPowerReloadListener.INSTANCE.POWER_TAGS) {
                tagsAmount.put(provider.getKey(), 0);
            }
        }

        public double addBlock(Block block, double powerMultiplier) {
            Integer amount = blocksAmount.get(block);

            if(amount == null) { // Not in blocks
                for(AltarPowerProvider<IOptionalNamedTag<Block>> provider : AltarPowerReloadListener.INSTANCE.POWER_TAGS) { // For all tag power providers
                    if(block.is(provider.getKey())) { // If block part of provider tag
                        amount = tagsAmount.get(provider.getKey());
                        tagsAmount.replace(provider.getKey(), tagsAmount.get(provider.getKey()) + 1);

                        return amount < provider.getLimit() ? provider.getPower() * powerMultiplier : 0; // Return 0 if above limit
                    }
                }
            }
            if(amount == null) {
                return 0;
            }

            AltarPowerProvider<Block> provider = AltarPowerReloadListener.INSTANCE.providerOf(block);
            blocksAmount.replace(block, amount + 1);
            return amount < provider.getLimit() ? provider.getPower() * powerMultiplier : 0; // Return 0 if above limit
        }

        public double removeBlock(Block block, double powerMultiplier) {
            Integer amount = blocksAmount.get(block);

            if(amount == null) { // Not in blocks
                for(AltarPowerProvider<IOptionalNamedTag<Block>> provider : AltarPowerReloadListener.INSTANCE.POWER_TAGS) { // For all tag power providers
                    if(block.is(provider.getKey())) { // If block part of provider tag
                        amount = tagsAmount.get(provider.getKey());
                        tagsAmount.replace(provider.getKey(), tagsAmount.get(provider.getKey()) - 1);

                        return amount <= provider.getLimit() ? provider.getPower() * powerMultiplier : 0; // Return 0 if above limit
                    }
                }
            }
            if(amount == null) {
                return 0;
            }

            AltarPowerProvider<Block> provider = AltarPowerReloadListener.INSTANCE.providerOf(block);
            blocksAmount.replace(block, amount - 1);
            return amount <= provider.getLimit() ? provider.getPower() * powerMultiplier : 0; // Return 0 if above limit
        }

        public double recalculatePower(double powerMultiplier) {
            double newPower = 0.0D;

            for(Block block : blocksAmount.keySet()) {
                AltarPowerProvider<Block> powerProvider = AltarPowerReloadListener.INSTANCE.providerOf(block);
                newPower += Math.max(0, Math.min(powerProvider.getLimit(), blocksAmount.get(block))) * powerProvider.getPower() * powerMultiplier;
            }
            for(IOptionalNamedTag<Block> tag : tagsAmount.keySet()) {
                AltarPowerProvider<IOptionalNamedTag<Block>> powerProvider = AltarPowerReloadListener.INSTANCE.providerOf(tag);
                newPower += Math.max(0, Math.min(powerProvider.getLimit(), tagsAmount.get(tag))) * powerProvider.getPower() * powerMultiplier;
            }

            return newPower;
        }

        public void reset() {
            blocksAmount.replaceAll((key,value) -> value = 0);
            tagsAmount.replaceAll((key,value) -> value = 0);
        }

    }

    public static class AltarUpgrade {

        private final ResourceLocation type;
        private final Block block;
        private final double powerMultiplier;
        private final double rechargeMultiplier;
        private final int priority;

        public AltarUpgrade(ResourceLocation type, Block block, double rechargeMultiplier, double powerMultiplier, int priority) {
            this.type = type;
            this.block = block;
            this.powerMultiplier = powerMultiplier;
            this.rechargeMultiplier = rechargeMultiplier;
            this.priority = priority;
        }

        public ResourceLocation getType() {
            return type;
        }

        public Block getBlock() {
            return block;
        }

        public double getPowerMultiplier() {
            return powerMultiplier;
        }

        public double getRechargeMultiplier() {
            return rechargeMultiplier;
        }

        public int getPriority() {
            return priority;
        }
    }
}
