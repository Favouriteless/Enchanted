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

package com.favouriteless.enchanted.common.rites.curse;

import com.favouriteless.enchanted.Enchanted;
import com.favouriteless.enchanted.common.CommonConfig;
import com.favouriteless.enchanted.api.rites.AbstractRite;
import com.favouriteless.enchanted.common.init.EnchantedItems;
import com.favouriteless.enchanted.common.init.EnchantedTags;
import com.favouriteless.enchanted.common.init.registry.EnchantedBlocks;
import com.favouriteless.enchanted.common.init.registry.EnchantedParticles;
import com.favouriteless.enchanted.common.rites.CirclePart;
import com.favouriteless.enchanted.common.rites.RiteType;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
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

import java.util.*;

public class RiteCurseOfBlight extends AbstractRite {

    public static final int BLIGHT_RADIUS = 50;
    public static final int BLIGHT_RADIUS_SQ = BLIGHT_RADIUS*BLIGHT_RADIUS;
    public static final int TICKS_PER_BLOCK = 3;

    public Set<LivingEntity> entities;
    public Set<BlockPos> positions;

    public int blockTicks = 0;

    public RiteCurseOfBlight(RiteType<?> type, ServerLevel level, BlockPos pos, UUID caster) {
        super(type, level, pos, caster, 0, 0); // Power, power per tick
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
        ServerLevel level = getLevel();
        BlockPos pos = getPos();
        CommonConfig config = AutoConfig.getConfigHolder(CommonConfig.class).getConfig();
        if(level != null) {
            if(ticks % TICKS_PER_BLOCK == 0) {
                List<LivingEntity> entitiesHandled = new ArrayList<>();
                for(LivingEntity entity : entities) {
                    if(entity.distanceToSqr(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D) < blockTicks*blockTicks) { // Sqr distance is much faster to calculate
                        if(entity instanceof Villager villager && Math.random() < config.blightZombieChance)
                            villager.convertTo(EntityType.ZOMBIE_VILLAGER, false);
                        else {
                            MobEffect effect = ForgeRegistries.MOB_EFFECTS.tags().getTag(EnchantedTags.MobEffects.BLIGHT_EFFECTS).getRandomElement(Enchanted.RANDOM).orElse(null);
                            if(effect != null) {
                                if(entity != level.getEntity(getCasterUUID()))
                                    entity.addEffect(new MobEffectInstance(effect, 100 + Enchanted.RANDOM.nextInt(101), Enchanted.RANDOM.nextInt(3)));
                            }
                        }
                        entitiesHandled.add(entity);
                    }
                }

                List<BlockPos> positionsHandled = new ArrayList<>();
                for(BlockPos _pos : positions) {
                    if(pos.distToCenterSqr(_pos.getX() + 0.5D, _pos.getY() + 0.5D, _pos.getZ() + 0.5D) < blockTicks*blockTicks) {
                        if(Math.random() < config.blightDecayChance) {
                            Block decayBlock = level.getBlockState(_pos).getBlock();
                            if(ForgeRegistries.BLOCKS.tags().getTag(EnchantedTags.Blocks.BLIGHT_DECAYABLE_BLOCKS).contains(decayBlock)) {
                                Block block = ForgeRegistries.BLOCKS.tags().getTag(EnchantedTags.Blocks.BLIGHT_DECAY_BLOCKS).getRandomElement(Enchanted.RANDOM).orElse(null);
                                if(block != null)
                                    level.setBlockAndUpdate(_pos, block.defaultBlockState());
                            }
                            else if(ForgeRegistries.BLOCKS.tags().getTag(EnchantedTags.Blocks.BLIGHT_DECAYABLE_PLANTS).contains(decayBlock))
                                level.setBlockAndUpdate(_pos, Blocks.DEAD_BUSH.defaultBlockState());
                        }
                        positionsHandled.add(_pos);
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
        ServerLevel level = getLevel();
        BlockPos pos = getPos();
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
        BlockPos pos = getPos();
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
