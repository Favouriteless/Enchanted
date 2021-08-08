package com.favouriteless.enchanted.common.entities.mandrake;

import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.ai.goal.WaterAvoidingRandomWalkingGoal;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;

import java.util.EnumSet;
import java.util.List;

public class MandrakeEntity extends MonsterEntity {

    public WaterAvoidingRandomWalkingGoal randomWalkingGoal;

    public MandrakeEntity(EntityType<? extends MonsterEntity> type, World world) {
        super(type, world);
    }

    public static AttributeModifierMap.MutableAttribute createAttributes() {
        return MonsterEntity.createMonsterAttributes().add(Attributes.MAX_HEALTH, 20.0D).add(Attributes.MOVEMENT_SPEED, 0.25D).add(Attributes.ATTACK_DAMAGE, 1.0D);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();

        this.randomWalkingGoal = new WaterAvoidingRandomWalkingGoal(this, 1.0D, 1.0F);
        this.randomWalkingGoal.setInterval(1);

        this.goalSelector.addGoal(0, new MandrakeAttackGoal(this));
        this.goalSelector.addGoal(1, new SwimGoal(this));
        this.goalSelector.addGoal(2, randomWalkingGoal);
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.GHAST_DEATH;
    }

    @Override
    protected SoundEvent getFallDamageSound(int pHeightIn) {
        return SoundEvents.GHAST_HURT;
    }

    @Override
    protected boolean shouldDropExperience() {
        return false;
    }

    protected class MandrakeAttackGoal extends Goal {

        protected final MandrakeEntity mob;
        protected int ticksUntilNextAttack;

        public MandrakeAttackGoal(MandrakeEntity entity) {
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

                List<Entity> playersInRange = mob.level.getEntities( (Entity)null,
                        new AxisAlignedBB(
                                this.mob.position().x - 8, this.mob.position().y - 8, this.mob.position().z - 8,
                                this.mob.position().x + 8, this.mob.position().y + 8, this.mob.position().z + 8),
                        this::isPlayer);

                for(Entity entity : playersInRange) {
                    entity.hurt(DamageSource.mobAttack(this.mob), 1.0F);
                    ((PlayerEntity) entity).addEffect(new EffectInstance(Effect.byId(9), 200, 1));
                }
                this.mob.level.playSound(null, this.mob.getX(), this.mob.getY(), this.mob.getZ(), SoundEvents.GHAST_HURT, SoundCategory.HOSTILE, 10.0F,0.85F + random.nextFloat() * 0.1F);

                this.ticksUntilNextAttack = 30;
            }
        }

        private boolean isPlayer(Entity entity) {
            return entity instanceof PlayerEntity;
        }

    }

}
