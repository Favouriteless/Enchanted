package com.favouriteless.enchanted.common.rites.curse;

import com.favouriteless.enchanted.Enchanted;
import com.favouriteless.enchanted.common.CommonConfig;
import com.favouriteless.enchanted.api.rites.AbstractRite;
import com.favouriteless.enchanted.common.init.EnchantedTags;
import com.favouriteless.enchanted.common.init.EnchantedTags.MobEffects;
import com.favouriteless.enchanted.common.init.registry.EnchantedParticleTypes;
import com.favouriteless.enchanted.common.init.registry.EnchantedBlocks;
import com.favouriteless.enchanted.common.init.registry.EnchantedItems;
import com.favouriteless.enchanted.common.rites.CirclePart;
import com.favouriteless.enchanted.common.rites.RiteType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
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
import net.minecraft.world.level.block.state.BlockState;

import java.util.*;

public class RiteCurseBlight extends AbstractRite {

    public static final int BLIGHT_RADIUS = 50;
    public static final int BLIGHT_RADIUS_SQ = BLIGHT_RADIUS*BLIGHT_RADIUS;
    public static final int TICKS_PER_BLOCK = 3;

    public Set<LivingEntity> entities;
    public Set<BlockPos> positions;

    public int blockTicks = 0;

    public RiteCurseBlight(RiteType<?> type, ServerLevel level, BlockPos pos, UUID caster) {
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
        if(level != null) {
            if(ticks % TICKS_PER_BLOCK == 0) {
                List<LivingEntity> entitiesHandled = new ArrayList<>();
                for(LivingEntity entity : entities) {
                    if(entity.distanceToSqr(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D) < blockTicks*blockTicks) { // Sqr distance is much faster to calculate
                        if(entity instanceof Villager villager && Math.random() < CommonConfig.BLIGHT_ZOMBIE_CHANCE.get())
                            villager.convertTo(EntityType.ZOMBIE_VILLAGER, false);
                        else {
                            Holder<MobEffect> effectHolder = Registry.MOB_EFFECT.getOrCreateTag(MobEffects.BLIGHT_EFFECTS).getRandomElement(Enchanted.RANDOMSOURCE).orElse(null);
                            if(effectHolder == null) {
                                Enchanted.LOG.error("Curse of Blight could not find any valid debuff effects! Check the enchanted:blight_effects tag.");
                                stopExecuting();
                                return;
                            }
                            if (entity != level.getEntity(getCasterUUID()))
                                entity.addEffect(new MobEffectInstance(effectHolder.value(), 100 + Enchanted.RANDOM.nextInt(101), Enchanted.RANDOM.nextInt(3)));
                        }
                        entitiesHandled.add(entity);
                    }
                }

                List<BlockPos> positionsHandled = new ArrayList<>();
                for(BlockPos _pos : positions) {
                    if(pos.distToCenterSqr(_pos.getX() + 0.5D, _pos.getY() + 0.5D, _pos.getZ() + 0.5D) < blockTicks*blockTicks) {
                        if(Math.random() < CommonConfig.BLIGHT_DECAY_CHANCE.get()) {
                            BlockState decayState = level.getBlockState(_pos);
                            if(decayState.is(EnchantedTags.Blocks.BLIGHT_DECAYABLE_BLOCKS)) {
                                Holder<Block> blockHolder = Registry.BLOCK.getOrCreateTag(EnchantedTags.Blocks.BLIGHT_DECAY_BLOCKS).getRandomElement(Enchanted.RANDOMSOURCE).orElse(null);
                                if(blockHolder == null) {
                                    Enchanted.LOG.error("Curse of Blight could not find any valid blocks to decay into! Check the enchanted:blight_decay_blocks tag.");
                                    stopExecuting();
                                    return;
                                }
                                level.setBlockAndUpdate(_pos, blockHolder.value().defaultBlockState());
                            }
                            else if(decayState.is(EnchantedTags.Blocks.BLIGHT_DECAYABLE_PLANTS))
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
                level.sendParticles(EnchantedParticleTypes.CURSE_BLIGHT_SEED.get(), pos.getX()+0.5D, pos.getY()+0.5D, pos.getZ()+0.5D, 1, 0, 0, 0, 0);
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
            for(Entity entity : level.getAllEntities()) // Don't use AABB here since it would just result in all entities being checked twice anyway.
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
