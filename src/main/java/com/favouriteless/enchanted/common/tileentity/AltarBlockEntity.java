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

import com.favouriteless.enchanted.Enchanted;
import com.favouriteless.enchanted.EnchantedConfig;
import com.favouriteless.enchanted.api.altar.IAltarPowerConsumer;
import com.favouriteless.enchanted.common.blocks.altar.AltarBlock;
import com.favouriteless.enchanted.common.containers.AltarContainer;
import com.favouriteless.enchanted.common.init.EnchantedTags;
import com.favouriteless.enchanted.common.init.EnchantedBlockEntityTypes;
import com.favouriteless.enchanted.common.multiblock.observerlib.altar.AltarObserverProvider;
import com.favouriteless.enchanted.common.init.EnchantedData;
import com.favouriteless.enchanted.api.altar.AltarPowerProvider;
import hellfirepvp.observerlib.api.ChangeSubscriber;
import hellfirepvp.observerlib.api.ObserverHelper;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.MenuProvider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AltarBlockEntity extends BlockEntity implements MenuProvider {

    public double maxPower;
    public double currentPower;
    public double rechargeMultiplier = 1.0D;
    public double powerMultiplier = 1.0D;
    public Vec3 centerPos;
    public final AltarBlockData altarBlockData = new AltarBlockData();

    private final ContainerData fields = new ContainerData() {
        @Override
        public int get(int pIndex) {
            return switch(pIndex) {
                case 0 -> (int) Math.round(currentPower);
                case 1 -> (int) Math.round(maxPower);
                case 2 -> (int) Math.round(rechargeMultiplier);
                default -> 0;
            };
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

    private boolean loaded = false;
    private boolean powerLoaded = false;
    private boolean facingX;
    private final double rechargeRate = EnchantedConfig.ALTAR_BASE_RECHARGE.get();

    private int ticksAlive = 0;

    public AltarBlockEntity(BlockPos pos, BlockState state) {
        super(EnchantedBlockEntityTypes.ALTAR.get(), pos, state);
    }

    public static <T extends BlockEntity> void tick(Level level, BlockPos blockPos, BlockState blockState, T t) {
        if(t instanceof AltarBlockEntity blockEntity) {
            if(level != null && !level.isClientSide) {

                if(!blockEntity.loaded) {
                    blockEntity.facingX = level.getBlockState(blockEntity.worldPosition).getValue(AltarBlock.FACING_X);
                    blockEntity.centerPos = blockEntity.facingX ?
                            Vec3.atLowerCornerOf(blockEntity.worldPosition).add(1.0D, 0.0D, 0.5D) :
                            Vec3.atLowerCornerOf(blockEntity.worldPosition).add(0.5D, 0.0D, 1.0D);

                    blockEntity.changeSubscriber = ObserverHelper.getHelper().getSubscriber(level, blockEntity.worldPosition);
                    if(blockEntity.changeSubscriber == null) {
                        blockEntity.createChangeSubscriber();
                    }
                    blockEntity.recalculateUpgrades();
                    if(!blockEntity.powerLoaded)
                        blockEntity.recalculateBlocks();
                    blockEntity.loaded = true;
                }
                else {
                    if(blockEntity.ticksAlive % 10 == 0) {
                        blockEntity.changeSubscriber.isValid(level);
                    }
                    if(blockEntity.currentPower <= blockEntity.maxPower)
                        blockEntity.currentPower += blockEntity.rechargeRate * blockEntity.rechargeMultiplier;
                    if(blockEntity.currentPower > blockEntity.maxPower)
                        blockEntity.currentPower = blockEntity.maxPower;
                }
                blockEntity.ticksAlive++;
            }
        }
    }

    @Override
    public void saveAdditional(CompoundTag nbt) {
        super.saveAdditional(nbt);
        nbt.putDouble("currentPower", currentPower);
        nbt.put("blockData", altarBlockData.getSaveTag());
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        if(nbt.contains("blockData")) {
            altarBlockData.loadTag((CompoundTag) nbt.get("blockData"));
            powerLoaded = true;
        }
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
                        if(ForgeRegistries.BLOCKS.tags().getTag(EnchantedTags.POWER_CONSUMERS).contains(level.getBlockState(currentPos).getBlock())) {
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
                    for (AltarUpgrade newUpgrade : EnchantedData.ALTAR_UPGRADES.get()) {
                        if (level.getBlockState(worldPosition.offset(x, 1, z)).getBlock() == newUpgrade.block()) { // If block is of upgrade
                            if(upgradesUsed.isEmpty()) {
                                upgradesUsed.add(newUpgrade);
                            }
                            else {
                                AltarUpgrade match = null;
                                for(AltarUpgrade upgrade : upgradesUsed) {
                                    if(newUpgrade.type().equals(upgrade.type())) { // If same type
                                        match = upgrade;
                                    }
                                }
                                if(match != null) {
                                    if(newUpgrade.priority() > match.priority()) {
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
                newPowerMultiplier += upgrade.powerMultiplier();
                newRechargeMultiplier += upgrade.rechargeMultiplier();
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
        consumer.addAltar(worldPosition);
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
    public Component getDisplayName() {
        return new TranslatableComponent("container.enchanted.altar");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory playerInventory, Player player) {
        return new AltarContainer(id, this, this.fields);
    }

    public static class AltarBlockData {
        public HashMap<Block, Integer> blocksAmount = new HashMap<>();
        public HashMap<TagKey<Block>, Integer> tagsAmount = new HashMap<>();

        public AltarBlockData() {
            for(AltarPowerProvider<Block> provider : EnchantedData.ALTAR_POWER_BLOCKS.get()) {
                blocksAmount.put(provider.getKey(), 0);
            }
            for(AltarPowerProvider<TagKey<Block>> provider : EnchantedData.ALTAR_POWER_TAGS.get()) {
                tagsAmount.put(provider.getKey(), 0);
            }
        }

        public CompoundTag getSaveTag() {
            CompoundTag nbt = new CompoundTag();
            CompoundTag blockNbt = new CompoundTag();
            CompoundTag tagNbt = new CompoundTag();

            for(Block block : blocksAmount.keySet()) {
                blockNbt.putInt(block.getRegistryName().toString(), blocksAmount.get(block));
            }
            for(TagKey<Block> tag : tagsAmount.keySet()) {
                tagNbt.putInt(tag.location().toString(), tagsAmount.get(tag));
            }

            nbt.put("blocksAmount", blockNbt);
            nbt.put("tagsAmount", tagNbt);
            return nbt;
        }

        public void loadTag(CompoundTag nbt) {
            CompoundTag blockNbt = (CompoundTag)nbt.get("blocksAmount");
            CompoundTag tagNbt = (CompoundTag)nbt.get("tagsAmount");

            for(String name : blockNbt.getAllKeys()) {
                blocksAmount.put(ForgeRegistries.BLOCKS.getValue(new ResourceLocation(name)), blockNbt.getInt(name));
            }
            for(String name : tagNbt.getAllKeys()) {
                tagsAmount.put(BlockTags.create(new ResourceLocation(name)), tagNbt.getInt(name));
            }

        }

        public double addBlock(Block block, double powerMultiplier) {
            Integer amount = blocksAmount.get(block);

            if(amount == null) { // Not in blocks
                for(AltarPowerProvider<TagKey<Block>> provider : EnchantedData.ALTAR_POWER_TAGS.get()) { // For all tag power providers
                    if(ForgeRegistries.BLOCKS.tags().getTag(provider.getKey()).contains(block)) { // If block part of provider tag
                        amount = tagsAmount.get(provider.getKey());
                        tagsAmount.replace(provider.getKey(), tagsAmount.get(provider.getKey()) + 1);

                        return amount < provider.getLimit() ? provider.getPower() * powerMultiplier : 0; // Return 0 if above limit
                    }
                }
            }
            if(amount == null) {
                return 0;
            }

            AltarPowerProvider<Block> provider = EnchantedData.ALTAR_POWER_BLOCKS.providerOf(block);
            blocksAmount.replace(block, amount + 1);
            return amount < provider.getLimit() ? provider.getPower() * powerMultiplier : 0; // Return 0 if above limit
        }

        public double removeBlock(Block block, double powerMultiplier) {
            Integer amount = blocksAmount.get(block);

            if(amount == null) { // Not in blocks
                for(AltarPowerProvider<TagKey<Block>> provider : EnchantedData.ALTAR_POWER_TAGS.get()) { // For all tag power providers
                    if(ForgeRegistries.BLOCKS.tags().getTag(provider.getKey()).contains(block)) { // If block part of provider tag
                        amount = tagsAmount.get(provider.getKey());
                        tagsAmount.replace(provider.getKey(), tagsAmount.get(provider.getKey()) - 1);

                        return amount <= provider.getLimit() ? provider.getPower() * powerMultiplier : 0; // Return 0 if above limit
                    }
                }
            }
            if(amount == null) {
                return 0;
            }

            AltarPowerProvider<Block> provider = EnchantedData.ALTAR_POWER_BLOCKS.providerOf(block);
            blocksAmount.replace(block, amount - 1);
            return amount <= provider.getLimit() ? provider.getPower() * powerMultiplier : 0; // Return 0 if above limit
        }

        public double recalculatePower(double powerMultiplier) {
            double newPower = 0.0D;

            for(Block block : blocksAmount.keySet()) {
                AltarPowerProvider<Block> powerProvider = EnchantedData.ALTAR_POWER_BLOCKS.providerOf(block);
                newPower += Math.max(0, Math.min(powerProvider.getLimit(), blocksAmount.get(block))) * powerProvider.getPower() * powerMultiplier;
            }
            for(TagKey<Block> tag : tagsAmount.keySet()) {
                AltarPowerProvider<TagKey<Block>> powerProvider = EnchantedData.ALTAR_POWER_TAGS.providerOf(tag);
                newPower += Math.max(0, Math.min(powerProvider.getLimit(), tagsAmount.get(tag))) * powerProvider.getPower() * powerMultiplier;
            }

            return newPower;
        }

        public void reset() {
            blocksAmount.replaceAll((key,value) -> value = 0);
            tagsAmount.replaceAll((key,value) -> value = 0);
        }

    }

    public record AltarUpgrade(ResourceLocation type, Block block, double rechargeMultiplier, double powerMultiplier, int priority) { }

}
