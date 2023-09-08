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

package com.favouriteless.enchanted.common.rites.entity.protection;

import com.favouriteless.enchanted.api.rites.AbstractRite;
import com.favouriteless.enchanted.client.particles.types.DoubleParticleType.DoubleParticleData;
import com.favouriteless.enchanted.common.altar.AltarStateObserver;
import com.favouriteless.enchanted.common.init.EnchantedItems;
import com.favouriteless.enchanted.common.init.registry.EnchantedBlocks;
import com.favouriteless.enchanted.common.init.registry.EnchantedParticles;
import com.favouriteless.enchanted.common.init.registry.RiteTypes;
import com.favouriteless.enchanted.common.rites.CirclePart;
import com.favouriteless.enchanted.common.rites.RiteType;
import com.favouriteless.enchanted.common.util.WaystoneHelper;
import com.favouriteless.stateobserver.StateObserverManager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import java.util.List;

public class RiteProtection extends AbstractRite {

    private RiteOfProtectionObserver stateObserver = null;
    protected final int radius;
    protected final Block block;
    protected ServerLevel targetLevel = null;
    protected BlockPos targetPos = null;

    public RiteProtection(RiteType<?> type, int power, int powerTick, int radius, Block block) {
        super(type, power, powerTick);
        this.radius = radius;
        this.block = block;
    }

    public RiteProtection() {
        this(RiteTypes.PROTECTION.get(), 500, 4, 4, EnchantedBlocks.PROTECTION_BARRIER.get()); // Power, power per tick, radius
        CIRCLES_REQUIRED.put(CirclePart.SMALL, EnchantedBlocks.CHALK_WHITE.get());
        ITEMS_REQUIRED.put(Items.OBSIDIAN, 1);
        ITEMS_REQUIRED.put(Items.REDSTONE, 1);
    }

    @Override
    public void execute() {
        findTargetPos();
        generateSphere(block);
        getOrCreateObserver();
        targetLevel.sendParticles(new DoubleParticleData(EnchantedParticles.PROTECTION_SEED.get(), radius), targetPos.getX()+0.5D, targetPos.getY()+0.6D, targetPos.getZ()+0.5D, 1, 0.0D, 0.0D, 0.0D, 0.0D);
    }

    @Override
    protected void onTick() {
        if(targetLevel.isLoaded(targetPos))
            if(ticks % 20 == 0) {
                generateSphere(block);
                stateObserver.checkChanges();
                targetLevel.sendParticles(new DoubleParticleData(EnchantedParticles.PROTECTION_SEED.get(), radius), targetPos.getX()+0.5D, targetPos.getY()+0.6D, targetPos.getZ()+0.5D, 1, 0.0D, 0.0D, 0.0D, 0.0D);
            }
    }

    @Override
    public void onStopExecuting() {
        generateSphere(Blocks.AIR);
        StateObserverManager.removeObserver(stateObserver);
    }

    protected void generateSphere(Block block) {
        MutableBlockPos spherePos = new MutableBlockPos(0, 0, 0);
        double increment = 1.0D / radius; // Total radians / circumference for radians per step
        for(double y = 0; y <= Math.PI*2 + increment/2; y += increment) {
            for(double p = 0; p <= Math.PI*2 + increment/2; p += increment) {
                double cosY = Math.cos(y);
                double sinY = Math.sin(y);
                double cosP = Math.cos(p);
                double sinP = Math.sin(p);
                spherePos.set((int) Math.round(sinY * cosP * radius) + targetPos.getX(), (int) Math.round(sinP * radius) + targetPos.getY(), (int) Math.round(cosY * cosP * radius) + targetPos.getZ());

                if(targetLevel.getBlockState(spherePos).isAir() || targetLevel.getBlockState(spherePos).is(EnchantedBlocks.PROTECTION_BARRIER.get()) || targetLevel.getBlockState(spherePos).is(EnchantedBlocks.PROTECTION_BARRIER_TEMPORARY.get()))
                    targetLevel.setBlockAndUpdate(spherePos, block.defaultBlockState());
            }
        }
    }

    protected void findTargetPos() {
        List<Entity> items = CirclePart.SMALL.getEntitiesInside(level, pos);
        for(Entity entity : items) {
            if(entity instanceof ItemEntity itemEntity) {
                ItemStack stack = itemEntity.getItem();
                if(stack.getItem() == EnchantedItems.BOUND_WAYSTONE.get()) {
                    targetLevel = (ServerLevel) WaystoneHelper.getLevel(level, stack);
                    targetPos = WaystoneHelper.getPos(stack);
                    consumeItemNoRequirement(itemEntity);
                    break;
                }
                else if(stack.getItem() == EnchantedItems.BLOODED_WAYSTONE.get()) {
                    targetEntity = WaystoneHelper.getEntity(level, stack);
                    targetLevel = (ServerLevel)targetEntity.getLevel();
                    targetPos = targetEntity.blockPosition();
                    consumeItemNoRequirement(itemEntity);
                    break;
                }
            }
        }
        if(targetLevel == null)
            targetLevel = level;
        if(targetPos == null)
            targetPos = pos;
    }

    @Override
    protected boolean loadAdditional(CompoundTag nbt, Level level) {
        if(!nbt.contains("targetLevel"))
            return false;
        if(!nbt.contains("targetX"))
            return false;
        if(!nbt.contains("targetY"))
            return false;
        if(!nbt.contains("targetZ"))
            return false;

        targetLevel = level.getServer().getLevel(ResourceKey.create(Registry.DIMENSION_REGISTRY, new ResourceLocation(nbt.getString("targetLevel"))));
        targetPos = new BlockPos(nbt.getInt("targetX"), nbt.getInt("targetY"), nbt.getInt("targetZ"));
        getOrCreateObserver();
        generateSphere(block);

        return true;
    }

    @Override
    protected void saveAdditional(CompoundTag nbt) {
        nbt.putString("targetLevel", targetLevel.dimension().location().toString());
        nbt.putInt("targetX", targetPos.getX());
        nbt.putInt("targetY", targetPos.getY());
        nbt.putInt("targetZ", targetPos.getZ());
    }

    protected void getOrCreateObserver() {
        if(stateObserver == null)
            stateObserver = (RiteOfProtectionObserver) StateObserverManager.getObserver(level, targetPos, AltarStateObserver.class);
        if(stateObserver == null)
            stateObserver = StateObserverManager.createObserver(new RiteOfProtectionObserver(targetLevel, targetPos, radius + 1, radius + 1, radius + 1, block));
    }

}
