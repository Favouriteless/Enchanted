package com.favouriteless.enchanted.common.entities;

import com.favouriteless.enchanted.Enchanted;
import com.favouriteless.enchanted.common.CommonConfig;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.level.Level;
import net.tslat.smartbrainlib.api.SmartBrainOwner;
import net.tslat.smartbrainlib.api.core.BrainActivityGroup;
import net.tslat.smartbrainlib.api.core.SmartBrainProvider;
import net.tslat.smartbrainlib.api.core.behaviour.AllApplicableBehaviours;
import net.tslat.smartbrainlib.api.core.behaviour.FirstApplicableBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.OneRandomBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.custom.look.LookAtTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.misc.Idle;
import net.tslat.smartbrainlib.api.core.behaviour.custom.move.MoveToWalkTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetRandomWalkTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetWalkTargetToAttackTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.InvalidateAttackTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.SetPlayerLookTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.SetRandomLookTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.TargetOrRetaliate;
import net.tslat.smartbrainlib.api.core.sensor.ExtendedSensor;
import net.tslat.smartbrainlib.api.core.sensor.vanilla.NearbyLivingEntitySensor;
import net.tslat.smartbrainlib.api.core.sensor.vanilla.NearbyPlayersSensor;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.IAnimationTickable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.builder.ILoopType.EDefaultLoopTypes;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

import java.util.List;

public class Ent extends Monster implements IAnimatable, IAnimationTickable, SmartBrainOwner<Ent> {

    public static final EntityDataAccessor<Integer> ATTACK_STATE = SynchedEntityData.defineId(Ent.class, EntityDataSerializers.INT);
    public static final EntityDataAccessor<Integer> TYPE = SynchedEntityData.defineId(Ent.class, EntityDataSerializers.INT);

    public static final ResourceLocation[] TEXTURE_LOCATIONS = new ResourceLocation[] {
            Enchanted.location("textures/entity/ent_alder.png"),
            Enchanted.location("textures/entity/ent_hawthorn.png"),
            Enchanted.location("textures/entity/ent_rowan.png")
    };

    public static final float PHASE_2_THRESHOLD = 0.75F;
    public static final float PHASE_3_THRESHOLD = 0.5F;

    public static final int ATTACK_SWIPE_LEFT = 1;
    public static final int ATTACK_SWIPE_RIGHT = 2;
    public static final int ATTACK_SLAM = 3;
    public static final int ATTACK_JUMP_SLAM = 4;
    public static final int ATTACK_FLING = 5;
    public static final int ATTACK_ROOT_BURST = 6;
    public static final int ATTACK_ROOT_TARGET = 7;

    private final AnimationFactory animationFactory = GeckoLibUtil.createFactory(this);

    public Ent(EntityType<? extends Monster> type, Level world) {
        super(type, world);
    }

    @Override
    protected final void registerGoals() {}

    @Override
    public List<ExtendedSensor<Ent>> getSensors() {
        return ObjectArrayList.of(
                new NearbyPlayersSensor<>(),
                new NearbyLivingEntitySensor<Ent>()
                        .setPredicate((target, entity) -> target instanceof Player)
        );
    }

    @Override
    public BrainActivityGroup<Ent> getCoreTasks() {
        return BrainActivityGroup.coreTasks(
                new LookAtTarget<>(),
                new MoveToWalkTarget<>().startCondition(ent -> ent.getEntityData().get(ATTACK_STATE) == 0)
        );
    }

    @Override
    public BrainActivityGroup<Ent> getIdleTasks() {
        return BrainActivityGroup.idleTasks(
                new FirstApplicableBehaviour<Ent>(
                        new TargetOrRetaliate<>(),
                        new SetPlayerLookTarget<>(),
                        new SetRandomLookTarget<>()),
                new OneRandomBehaviour<>(
                        new SetRandomWalkTarget<>().speedModifier(1.0F),
                        new Idle<>().runFor(entity -> entity.getRandom().nextInt(60, 90))
                )
        );
    }

    @Override
    public BrainActivityGroup<Ent> getFightTasks() {
        float fHealth = getHealth() / getMaxHealth();

        return BrainActivityGroup.fightTasks(
                new InvalidateAttackTarget<>(),
                new FirstApplicableBehaviour<Ent>(

                        new AllApplicableBehaviours<Ent>(
                                new SetWalkTargetToAttackTarget<>()
                                        .speedMod(2.5F)
                        ).startCondition(ent -> fHealth < PHASE_3_THRESHOLD),


                        new AllApplicableBehaviours<Ent>(
                                new SetWalkTargetToAttackTarget<>()
                                        .speedMod(2.0F)
                        ).startCondition(ent -> fHealth < PHASE_2_THRESHOLD),


                        new AllApplicableBehaviours<Ent>(
                                new SetWalkTargetToAttackTarget<>()
                                        .speedMod(1.5F)
                        )
                )
        );
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController<>(this, "controller", 20, this::predicate));
    }

    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        if (event.isMoving()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.ent.walk", EDefaultLoopTypes.LOOP));
        }
        else {
            return PlayState.STOP;
        }
        return PlayState.CONTINUE;
    }

    @Override
    protected Brain.Provider<?> brainProvider() {
        return new SmartBrainProvider<>(this);
    }

    @Override
    protected void customServerAiStep() {
        tickBrain(this);
    }

    public static AttributeSupplier createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 400.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.15D)
                .add(Attributes.ATTACK_DAMAGE, 1.0D)
                .build();
    }

    @Override
    public boolean hurt(DamageSource source, float amount) { // If using an axe take bonus damage
        if(source.getEntity() instanceof LivingEntity entity)
            if(entity.getMainHandItem().getItem() instanceof AxeItem)
                return super.hurt(source, (float)(amount * AutoConfig.getConfigHolder(CommonConfig.class).getConfig().entAxeMultiplier));
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
    public AnimationFactory getFactory() {
        return animationFactory;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(ATTACK_STATE, 0);
    }


    @Override
    public int tickTimer() {
        return 0;
    }
}
