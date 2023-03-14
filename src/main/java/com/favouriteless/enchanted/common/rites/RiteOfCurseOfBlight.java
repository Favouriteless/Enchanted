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

package com.favouriteless.enchanted.common.rites;

import com.favouriteless.enchanted.Enchanted;
import com.favouriteless.enchanted.EnchantedConfig;
import com.favouriteless.enchanted.api.rites.AbstractRite;
import com.favouriteless.enchanted.common.init.*;
import com.favouriteless.enchanted.common.util.rite.CirclePart;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RiteOfCurseOfBlight extends AbstractRite {

    public static final int BLIGHT_RADIUS = 50;
    public static final int BLIGHT_RADIUS_SQ = BLIGHT_RADIUS*BLIGHT_RADIUS;
    public static final int TICKS_PER_BLOCK = 3;

    public Set<LivingEntity> entities;
    public Set<BlockPos> positions;

    public int blockTicks = 0;

    public RiteOfCurseOfBlight() {
        super(EnchantedRiteTypes.CURSE_OF_BLIGHT.get(), 0, 0); // Power, power per tick
        CIRCLES_REQUIRED.put(CirclePart.MEDIUM, EnchantedBlocks.CHALK_RED.get());
        ITEMS_REQUIRED.put(EnchantedItems.ATTUNED_STONE_CHARGED.get(), 1);
        ITEMS_REQUIRED.put(EnchantedItems.REDSTONE_SOUP.get(), 1);
        ITEMS_REQUIRED.put(EnchantedItems.REEK_OF_MISFORTUNE.get(), 1);
        ITEMS_REQUIRED.put(Items.FERMENTED_SPIDER_EYE, 1);
        ITEMS_REQUIRED.put(Items.GLISTERING_MELON_SLICE, 1);
        ITEMS_REQUIRED.put(Items.ROTTEN_FLESH, 1);
        ITEMS_REQUIRED.put(Items.DIAMOND, 1);
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
                        if(entity instanceof Villager villager && Math.random() < EnchantedConfig.BLIGHT_ZOMBIE_CHANCE.get())
                            villager.convertTo(EntityType.ZOMBIE_VILLAGER, false);
                        else {
                            MobEffect effect = ForgeRegistries.MOB_EFFECTS.tags().getTag(EnchantedTags.MobEffects.BLIGHT_EFFECTS).getRandomElement(Enchanted.RANDOM).orElse(null);
                            if(effect != null) {
                                if(entity != level.getEntity(casterUUID))
                                    entity.addEffect(new MobEffectInstance(effect, 100 + Enchanted.RANDOM.nextInt(101), Enchanted.RANDOM.nextInt(3)));
                            }
                        }
                        entitiesHandled.add(entity);
                    }
                }

                List<BlockPos> positionsHandled = new ArrayList<>();
                for(BlockPos pos : positions) {
                    if(this.pos.distToCenterSqr(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D) < blockTicks*blockTicks) {
                        if(Math.random() < EnchantedConfig.BLIGHT_DECAY_CHANCE.get()) {
                            Block decayBlock = level.getBlockState(pos).getBlock();
                            if(ForgeRegistries.BLOCKS.tags().getTag(EnchantedTags.Blocks.BLIGHT_DECAYABLE_BLOCKS).contains(decayBlock)) {
                                Block block = ForgeRegistries.BLOCKS.tags().getTag(EnchantedTags.Blocks.BLIGHT_DECAY_BLOCKS).getRandomElement(Enchanted.RANDOM).orElse(null);
                                if(block != null)
                                    level.setBlockAndUpdate(pos, block.defaultBlockState());
                            }
                            else if(ForgeRegistries.BLOCKS.tags().getTag(EnchantedTags.Blocks.BLIGHT_DECAYABLE_PLANTS).contains(decayBlock))
                                level.setBlockAndUpdate(pos, Blocks.DEAD_BUSH.defaultBlockState());
                        }
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
                level.sendParticles(EnchantedParticles.CURSE_BLIGHT_SEED.get(), pos.getX()+0.5D, pos.getY()+0.5D, pos.getZ()+0.5D, 1, 0, 0, 0, 0);
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
                    if(entity.distanceToSqr(pos.getX()+0.5D, pos.getY()+0.5D, pos.getZ()+0.5D) <= BLIGHT_RADIUS_SQ) // If within circle
                        entities.add(livingEntity);
        }
        return entities;
    }

    public Set<BlockPos> getBlockPosSet() {
        Set<BlockPos> positions = new HashSet<>();
        for(int x = -BLIGHT_RADIUS; x < BLIGHT_RADIUS; x++) {
            for(int y = -BLIGHT_RADIUS; y < BLIGHT_RADIUS; y++) {
                for(int z = -BLIGHT_RADIUS; z < BLIGHT_RADIUS; z++) {
                    if(pos.distToCenterSqr(pos.getX()+x, pos.getY()+y, pos.getZ()+z) < BLIGHT_RADIUS_SQ)
                        positions.add(new BlockPos(pos.getX()+x, pos.getY()+y, pos.getZ()+z));
                }
            }
        }

        return positions;
    }

}
