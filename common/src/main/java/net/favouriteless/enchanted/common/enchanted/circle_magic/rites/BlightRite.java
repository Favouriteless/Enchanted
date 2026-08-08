package net.favouriteless.enchanted.common.enchanted.circle_magic.rites;

import net.favouriteless.enchanted.common.Enchanted;
import net.favouriteless.enchanted.common.init.EParticleTypes;
import net.favouriteless.enchanted.common.init.ETags;
import net.favouriteless.enchanted.common.init.ETags.MobEffects;
import net.favouriteless.enchanted.common.util.BlockPosUtils;
import net.favouriteless.enchanted.common.util.RandomUtils;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class BlightRite extends Rite {

    public static final int TICKS_PER_BLOCK = 3;

    protected final int radius;
    protected final int radiusSq;
    protected final double decayChance;
    protected final double zombieChance;

    protected int step = 1;

    public BlightRite(BaseRiteParams baseParams, RiteParams params, int radius, double decayChance, double zombieChance) {
        super(baseParams, params);
        this.radius = radius;
        this.radiusSq = radius * radius;
        this.decayChance = decayChance;
        this.zombieChance = zombieChance;
    }

    @Override
    protected boolean onStart(RiteParams params) {
        Entity caster = level.getEntity(params.caster);
        level.getEntitiesOfClass(LivingEntity.class, type.getBounds(pos), e -> e.position().distanceToSqr(pos.getCenter()) < radiusSq)
             .forEach(entity -> applyBlightEffects(caster, entity));

        return true;
    }

    @Override
    protected boolean onTick(RiteParams params) {
        if (params.ticks() % TICKS_PER_BLOCK != 0) {
            return true;
        }

        BlockPosUtils.iterableSphereHollow(pos, step).forEach(spherePos -> {
            if (Math.random() > decayChance) {
                return;
            }

            BlockState state = level.getBlockState(spherePos);
            if (state.isAir()) {
                return;
            }

            if (state.is(ETags.Blocks.BLIGHT_DECAYABLE_BLOCKS)) {
                Holder<Block> holder = BuiltInRegistries.BLOCK.getOrCreateTag(ETags.Blocks.BLIGHT_DECAY_BLOCKS).getRandomElement(Enchanted.RANDOMSOURCE).orElse(null);
                if (holder == null) {
                    return;
                }
                level.setBlockAndUpdate(spherePos, holder.value().defaultBlockState());
            } else if (state.is(ETags.Blocks.BLIGHT_DECAYABLE_PLANTS)) {
                level.setBlockAndUpdate(spherePos, Blocks.DEAD_BUSH.defaultBlockState());
            }
        });

        if (params.ticks() % (TICKS_PER_BLOCK * 5) == 0) {
            level.playSound(null, pos, SoundEvents.ENDER_DRAGON_GROWL, SoundSource.MASTER, 0.1F, 1.0F);
            level.sendParticles(
                    EParticleTypes.BLIGHT_SEED.get(), pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D,
                    1, 0, 0, 0, 0
            );
        }

        return step++ < radius;
    }

    @Override
    protected void saveAdditional(CompoundTag tag, ServerLevel level) {
        tag.putInt("step", step);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, ServerLevel level) {
        step = tag.getInt("step");
    }

    protected void applyBlightEffects(Entity caster, LivingEntity target) {
        if (target instanceof Villager villager && Math.random() < zombieChance) {
            villager.convertTo(EntityType.ZOMBIE_VILLAGER, false);
            return;
        }

        Holder<MobEffect> effectHolder = BuiltInRegistries.MOB_EFFECT.getOrCreateTag(MobEffects.BLIGHT_EFFECTS).getRandomElement(Enchanted.RANDOMSOURCE).orElse(null);
        if (effectHolder == null) {
            return;
        }

        if (target != caster) {
            target.addEffect(new MobEffectInstance(effectHolder, 100 + RandomUtils.nextInt(101), RandomUtils.nextInt(3)));
        }
    }

}
