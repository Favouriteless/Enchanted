package com.favouriteless.enchanted.common.rites.world;

import com.favouriteless.enchanted.Enchanted;
import com.favouriteless.enchanted.api.rites.AbstractRite;
import com.favouriteless.enchanted.common.CommonConfig;
import com.favouriteless.enchanted.common.init.EnchantedTags.MobEffects;
import com.favouriteless.enchanted.common.init.registry.EnchantedBlocks;
import com.favouriteless.enchanted.common.init.registry.EnchantedItems;
import com.favouriteless.enchanted.common.init.registry.EnchantedParticles;
import com.favouriteless.enchanted.common.rites.CirclePart;
import com.favouriteless.enchanted.common.rites.RiteType;
import com.favouriteless.enchanted.mixin.ZombieVillagerAccessor;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.server.level.ServerLevel;
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

import java.util.*;

public class RiteFertility extends AbstractRite {

    public static final int RADIUS = 25;
    public static final int RADIUS_SQ = RADIUS * RADIUS;
    public static final int TICKS_PER_BLOCK = 5;

    public Set<LivingEntity> entities;
    public Set<BlockPos> positions;

    public int blockTicks = 0;

    public RiteFertility(RiteType<?> type, ServerLevel level, BlockPos pos, UUID caster, int power) {
        super(type, level, pos, caster, power, 0);
    }

    public RiteFertility(RiteType<?> type, ServerLevel level, BlockPos pos, UUID caster) {
        this(type, level, pos, caster, 3000); // Power, power per tick
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
        ServerLevel level = getLevel();
        BlockPos pos = getPos();
        if(level != null) {
            if(ticks % TICKS_PER_BLOCK == 0) {
                List<LivingEntity> entitiesHandled = new ArrayList<>();
                for(LivingEntity entity : entities) {
                    if(entity.distanceToSqr(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D) < blockTicks*blockTicks) { // Sqr distance is much faster to calculate
                        if(entity instanceof ZombieVillager villager)
                            ((ZombieVillagerAccessor)villager).startConverting(getCasterUUID(), Enchanted.RANDOM.nextInt(2401) + 3600);

                        Collection<MobEffectInstance> effects = entity.getActiveEffects();
                        List<MobEffect> toRemove = new ArrayList<>();
                        for(MobEffectInstance effect : effects) {

                            if(Registry.MOB_EFFECT.getHolder(MobEffect.getId(effect.getEffect())).get().is(MobEffects.FERTILITY_CURE_EFFECTS))
                                toRemove.add(effect.getEffect());
                        }
                        for(MobEffect effect : toRemove) {
                            entity.removeEffect(effect);
                        }

                        entitiesHandled.add(entity);
                    }
                }

                List<BlockPos> positionsHandled = new ArrayList<>();
                for(BlockPos _pos : positions) {
                    if(pos.distToCenterSqr(_pos.getX() + 0.5D, _pos.getY() + 0.5D, _pos.getZ() + 0.5D) < blockTicks*blockTicks) {
                        BlockState state = level.getBlockState(_pos);
                        if(state.getBlock() instanceof BonemealableBlock block)
                            if(Math.random() < AutoConfig.getConfigHolder(CommonConfig.class).getConfig().fertilityBonemealChance)
                                block.performBonemeal(level, level.random, _pos, state);
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
                level.sendParticles(EnchantedParticles.FERTILITY_SEED.get(), pos.getX()+0.5D, pos.getY()+2D, pos.getZ()+0.5D, 1, 0, 0, 0, 0);
            }
        }
        else
            stopExecuting();
    }

    public Set<LivingEntity> getLivingEntities() {
        Set<LivingEntity> entities = new HashSet<>();
        ServerLevel level = getLevel();
        BlockPos pos = getPos();
        if(level != null) {
            for(Entity entity : level.getAllEntities())
                if(entity instanceof LivingEntity livingEntity)
                    if(entity.distanceToSqr(pos.getX()+0.5D, pos.getY()+0.5D, pos.getZ()+0.5D) <= RADIUS_SQ) // If within circle
                        entities.add(livingEntity);
        }
        return entities;
    }

    public Set<BlockPos> getBlockPosSet() {
        BlockPos pos = getPos();
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
