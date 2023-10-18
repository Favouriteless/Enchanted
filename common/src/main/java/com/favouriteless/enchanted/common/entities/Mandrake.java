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

package com.favouriteless.enchanted.common.entities;

import com.favouriteless.enchanted.common.init.EnchantedItems;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.builder.ILoopType.EDefaultLoopTypes;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

import java.util.List;

public class Mandrake extends Monster implements IAnimatable {

    private final AnimationFactory animationFactory = GeckoLibUtil.createFactory(this);

    public Mandrake(EntityType<? extends Monster> type, Level world) {
        super(type, world);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 20.0D).add(Attributes.MOVEMENT_SPEED, 0.2D).add(Attributes.ATTACK_DAMAGE, 1.0D);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();

        WaterAvoidingRandomStrollGoal randomWalkingGoal = new WaterAvoidingRandomStrollGoal(this, 1.0D, 1.0F);
        randomWalkingGoal.setInterval(1);

        this.goalSelector.addGoal(0, new MandrakeAttackGoal(this));
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, randomWalkingGoal);
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.GHAST_DEATH;
    }

    @Override
    public Fallsounds getFallSounds() {
        return new Fallsounds(SoundEvents.GENERIC_SMALL_FALL, SoundEvents.GENERIC_BIG_FALL);
    }

    @Override
    protected boolean shouldDropExperience() {
        return false;
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController<>(this, "controller", 0, this::predicate));
    }

    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        if (event.isMoving()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.mandrake.walk", EDefaultLoopTypes.LOOP));
            return PlayState.CONTINUE;
        } else {
            event.getController().clearAnimationCache();
            return PlayState.STOP;
        }
    }

    @Override
    public AnimationFactory getFactory() {
        return animationFactory;
    }

    protected class MandrakeAttackGoal extends Goal {

        protected final Mandrake mob;
        protected int ticksUntilNextAttack;

        public MandrakeAttackGoal(Mandrake entity) {
            this.mob = entity;
            this.ticksUntilNextAttack = 0;
        }

        @Override
        public boolean canUse() {
            return true;
        }

        @Override
        public void stop() {
            this.ticksUntilNextAttack = 30;
        }

        @Override
        public void tick() {
            if(--this.ticksUntilNextAttack <= 0) {

                List<LivingEntity> entitiesInRange = mob.level.getEntitiesOfClass(LivingEntity.class,
                        new AABB(this.mob.position().x - 8, this.mob.position().y - 8, this.mob.position().z - 8,
                                this.mob.position().x + 8, this.mob.position().y + 8, this.mob.position().z + 8), entity -> !(entity instanceof Mandrake) );

                for(LivingEntity entity : entitiesInRange) {
                    if(entity.getItemBySlot(EquipmentSlot.HEAD).getItem() != EnchantedItems.EARMUFFS.get()) {
                        entity.hurt(DamageSource.mobAttack(this.mob), 1.0F);
                        if(entity instanceof Player player && !player.isCreative())
                            entity.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 200, 1));
                    }
                }
                this.mob.level.playSound(null, this.mob.getX(), this.mob.getY(), this.mob.getZ(), SoundEvents.GHAST_HURT, SoundSource.HOSTILE, 10.0F,0.85F + random.nextFloat() * 0.1F);

                this.ticksUntilNextAttack = 30;
            }
        }

    }

}
