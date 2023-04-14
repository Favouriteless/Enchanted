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

import com.favouriteless.enchanted.EnchantedConfig;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.level.Level;
import net.tslat.smartbrainlib.api.SmartBrainOwner;
import net.tslat.smartbrainlib.api.core.BrainActivityGroup;
import net.tslat.smartbrainlib.api.core.SmartBrainProvider;
import net.tslat.smartbrainlib.api.core.sensor.ExtendedSensor;
import net.tslat.smartbrainlib.api.core.sensor.vanilla.NearbyPlayersSensor;
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

public class EntEntity extends Monster implements IAnimatable, SmartBrainOwner<EntEntity> {

    private final AnimationFactory animationFactory = GeckoLibUtil.createFactory(this);

    public EntEntity(EntityType<? extends Monster> type, Level world) {
        super(type, world);
    }

    @Override
    protected final void registerGoals() {}

    @Override
    public List<ExtendedSensor<EntEntity>> getSensors() {
        return ObjectArrayList.of(
                new NearbyPlayersSensor<>()
        );
    }

    @Override
    public BrainActivityGroup<EntEntity> getCoreTasks() {
        return BrainActivityGroup.coreTasks(

        );
    }

    @Override
    public BrainActivityGroup<EntEntity> getIdleTasks() {
        return BrainActivityGroup.idleTasks(

        );
    }

    @Override
    public BrainActivityGroup<EntEntity> getFightTasks() {
        return BrainActivityGroup.fightTasks(

        );
    }

    @Override
    protected Brain.Provider<?> brainProvider() {
        return new SmartBrainProvider<>(this);
    }

    @Override
    protected void customServerAiStep() {
        tickBrain(this);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 400.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.15D)
                .add(Attributes.ATTACK_DAMAGE, 1.0D);
    }

    @Override
    public boolean hurt(DamageSource source, float amount) { // If using an axe take bonus damage
        if(source.getEntity() instanceof LivingEntity entity)
            if(entity.getMainHandItem().getItem() instanceof AxeItem)
                return super.hurt(source, (float)(amount * EnchantedConfig.ENT_AXE_MULTIPLIER.get()));
        return super.hurt(source, amount);
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.DONKEY_DEATH;
    }

    @Override
    public Fallsounds getFallSounds() {
        return new Fallsounds(SoundEvents.GENERIC_SMALL_FALL, SoundEvents.GENERIC_BIG_FALL);
    }

    @Override
    protected boolean shouldDropExperience() {
        return true;
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController<>(this, "lowerBodyController", 0, this::lowerBodyPredicate));
        data.addAnimationController(new AnimationController<>(this, "upperBodyController", 0, this::upperBodyPredicate));
    }

    private <E extends IAnimatable> PlayState lowerBodyPredicate(AnimationEvent<E> event) {
        if(true) {//if (event.isMoving()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.ent.walk_lower", EDefaultLoopTypes.LOOP));
            return PlayState.CONTINUE;
        } else {
            event.getController().clearAnimationCache();
            return PlayState.STOP;
        }
    }

    private <E extends IAnimatable> PlayState upperBodyPredicate(AnimationEvent<E> event) {
        if(true) {//if (event.isMoving()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.ent.walk_upper", EDefaultLoopTypes.LOOP));
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


}
