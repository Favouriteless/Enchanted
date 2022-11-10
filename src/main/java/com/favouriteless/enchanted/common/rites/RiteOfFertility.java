/*
 *
 *   Copyright (c) 2022. Favouriteless
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

package com.favouriteless.enchanted.common.rites;

import com.favouriteless.enchanted.Enchanted;
import com.favouriteless.enchanted.EnchantedConfig;
import com.favouriteless.enchanted.api.rites.AbstractRite;
import com.favouriteless.enchanted.common.init.*;
import com.favouriteless.enchanted.common.util.rite.CirclePart;
import com.favouriteless.enchanted.common.util.rite.RiteType;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.ZombieVillager;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.*;

public class RiteOfFertility extends AbstractRite {

    public static final int RADIUS = 25;
    public static final int RADIUS_SQ = RADIUS * RADIUS;
    public static final int TICKS_PER_BLOCK = 5;

    public Set<LivingEntity> entities;
    public Set<BlockPos> positions;

    public int blockTicks = 0;

    public RiteOfFertility(RiteType<?> type, int power) {
        super(type, power, 0);
    }

    public RiteOfFertility() {
        this(EnchantedRiteTypes.FERTILITY.get(), 3000); // Power, power per tick
        CIRCLES_REQUIRED.put(CirclePart.SMALL, EnchantedBlocks.CHALK_WHITE.get());
        ITEMS_REQUIRED.put(Items.BONE_MEAL, 1);
        ITEMS_REQUIRED.put(EnchantedItems.HINT_OF_REBIRTH.get(), 1);
        ITEMS_REQUIRED.put(EnchantedItems.DIAMOND_VAPOUR.get(), 1);
        ITEMS_REQUIRED.put(EnchantedItems.QUICKLIME.get(), 1);
        ITEMS_REQUIRED.put(EnchantedItems.GYPSUM.get(), 1);
        ITEMS_REQUIRED.put(EnchantedItems.MUTANDIS.get(), 1);
    }

    @Override
    protected void execute() {
        entities = getLivingEntities();
        positions = getBlockPosSet();
    }

    @Override
    protected void onTick() {
        if(level != null) {
            if(ticks % TICKS_PER_BLOCK == 0) {
                List<LivingEntity> entitiesHandled = new ArrayList<>();
                for(LivingEntity entity : entities) {
                    if(entity.distanceToSqr(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D) < blockTicks*blockTicks) { // Sqr distance is much faster to calculate
                        if(entity instanceof ZombieVillager villager)
                            villager.startConverting(casterUUID, Enchanted.RANDOM.nextInt(2401) + 3600);

                        Collection<MobEffectInstance> effects = entity.getActiveEffects();
                        List<MobEffect> toRemove = new ArrayList<>();
                        for(MobEffectInstance effect : effects) {
                            if(ForgeRegistries.MOB_EFFECTS.tags().getTag(EnchantedTags.FERTILITY_CURE_EFFECTS).contains(effect.getEffect()))
                                toRemove.add(effect.getEffect());
                        }
                        for(MobEffect effect : toRemove) {
                            entity.removeEffect(effect);
                        }

                        entitiesHandled.add(entity);
                    }
                }

                List<BlockPos> positionsHandled = new ArrayList<>();
                for(BlockPos pos : positions) {
                    if(this.pos.distToCenterSqr(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D) < blockTicks*blockTicks) {
                        BlockState state = level.getBlockState(pos);
                        if(state.getBlock() instanceof BonemealableBlock block)
                            if(Math.random() < EnchantedConfig.FERTILITY_BONE_MEAL_CHANCE.get())
                                block.performBonemeal(level, level.random, pos, state);
                        positionsHandled.add(pos);
                    }
                }

                entitiesHandled.forEach(entities::remove);
                positionsHandled.forEach(positions::remove);
                blockTicks++;

                if(positions.isEmpty())
                    stopExecuting();
            }

            if(ticks % (TICKS_PER_BLOCK*5) == 0) {
                level.playSound(null, pos, SoundEvents.ENDER_DRAGON_GROWL, SoundSource.MASTER, 0.1F, 1.0F);
                level.sendParticles(EnchantedParticles.FERTILITY_SEED.get(), pos.getX()+0.5D, pos.getY()+2D, pos.getZ()+0.5D, 1, 0, 0, 0, 0);
            }
        }
        else
            stopExecuting();
    }

    public Set<LivingEntity> getLivingEntities() {
        Set<LivingEntity> entities = new HashSet<>();
        if(level != null) {
            for(Entity entity : level.getEntities().getAll())
                if(entity instanceof LivingEntity livingEntity)
                    if(entity.distanceToSqr(pos.getX()+0.5D, pos.getY()+0.5D, pos.getZ()+0.5D) <= RADIUS_SQ) // If within circle
                        entities.add(livingEntity);
        }
        return entities;
    }

    public Set<BlockPos> getBlockPosSet() {
        Set<BlockPos> positions = new HashSet<>();
        for(int x = -RADIUS; x < RADIUS; x++) {
            for(int y = -RADIUS; y < RADIUS; y++) {
                for(int z = -RADIUS; z < RADIUS; z++) {
                    if(pos.distToCenterSqr(pos.getX()+x, pos.getY()+y, pos.getZ()+z) < RADIUS_SQ)
                        positions.add(new BlockPos(pos.getX()+x, pos.getY()+y, pos.getZ()+z));
                }
            }
        }

        return positions;
    }

}
