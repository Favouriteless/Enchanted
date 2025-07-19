package net.favouriteless.enchanted.common.entities;

import net.favouriteless.enchanted.client.client_handlers.entities.BroomstickEntityClientHandler;
import net.favouriteless.enchanted.common.init.EItems;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.network.syncher.SynchedEntityData.Builder;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class Broomstick extends Entity {

    private static final EntityDataAccessor<Integer> DATA_ID_HURT = SynchedEntityData.defineId(Broomstick.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> DATA_ID_HURTDIR = SynchedEntityData.defineId(Broomstick.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Float> DATA_ID_DAMAGE = SynchedEntityData.defineId(Broomstick.class, EntityDataSerializers.FLOAT);

    public static final double ACCELERATION = 0.02D;
    public static final double MAX_SPEED = 1.0D;
    public static final double MAX_TILT = 20.0D;

    public int inputAcceleration = 0;
    public int inputClimb = 0;

    public int inputTurn = 0;

    private float deltaRotX = 0.0F;
    private float deltaRotY = 0.0F;

    private int lerpSteps = 0;
    private double lerpX = 0.0D;
    private double lerpY = 0.0D;
    private double lerpZ = 0.0D;
    private double lerpXRot = 0.0D;
    private double lerpYRot = 0.0D;

    public Broomstick(EntityType<Broomstick> type, Level world) {
        super(type, world);
        blocksBuilding = true;
    }

    @Override
    protected void defineSynchedData(Builder builder) {
        builder.define(DATA_ID_HURT, 0);
        builder.define(DATA_ID_HURTDIR, 1);
        builder.define(DATA_ID_DAMAGE, 0.0F);
    }

    @Override
    public void tick() {
        if(getHurtTime() > 0)
            setHurtTime(getHurtTime() - 1);
        if(getDamage() > 0.0F)
            setDamage(getDamage() - 1.0F);

        super.tick();

        tickLerp();
        if(isControlledByLocalInstance()) {
            if(level().isClientSide) {
                BroomstickEntityClientHandler.controlBroom(this);
                handleRotationTick();
                handleMovementTick();
            }
            else {
                setDeltaMovement(getDeltaMovement().scale(0.75D));

                if(level().getEntitiesOfClass(Player.class, getBoundingBox().inflate(0, 1.0D, 0)).isEmpty())
                    setDeltaMovement(getDeltaMovement().add(0.0D, -0.01D, 0.0D));
            }
            move(MoverType.SELF, getDeltaMovement());
        }
        else {
            setDeltaMovement(Vec3.ZERO);
        }
    }

    protected void handleMovementTick() {
        Vec3 velocity = getDeltaMovement();

        Vec3 forward = Vec3.directionFromRotation(0, getYRot());
        Vec3 up = new Vec3(0.0D, 1.0D, 0.0D);
        Vec3 left = up.cross(forward);

        double acceleration = inputAcceleration * ACCELERATION;

        // If W or S is held, only decelerate the sideways components of the velocity.
        velocity = forward.scale(velocity.dot(forward) * (inputAcceleration == 0 ? 0.85D : 1.0D) + acceleration)
                .add(up.scale(velocity.dot(up) * (inputClimb == 0 ? 0.85D : 1.0D)))
                .add(left.scale(velocity.dot(left) * 0.85D));

        velocity = velocity.add(0, inputClimb * ACCELERATION, 0); // Include vertical movement

        double speed = Math.max(Math.min(velocity.length(), MAX_SPEED), 0); // Clamp to maximum velocity.
        setDeltaMovement(velocity.normalize().scale(speed));
    }

    protected void handleRotationTick() {
        LivingEntity controller = getControllingPassenger();

        deltaRotY += inputTurn;
        deltaRotX += inputClimb * -inputAcceleration;

        if(inputClimb == 0 || inputAcceleration == 0) {
            if(Math.abs(getXRot()) < Math.abs(deltaRotX))
                deltaRotX = -getXRot();
            else if(getXRot() > 0) {
                if(deltaRotX > 0)
                    deltaRotX = 0;
                deltaRotX--;
            }
            else if(getXRot() < 0) {
                if(deltaRotX < 0)
                    deltaRotX = 0;
                deltaRotX++;
            }
        }

        setYRot(getYRot() + deltaRotY);
        controller.setYRot(controller.getYRot() + deltaRotY);
        setXRot((float)Mth.clamp(getXRot() + deltaRotX, -MAX_TILT, MAX_TILT));
        deltaRotX *= 0.8F;
        deltaRotY *= 0.8F;
    }

    @Override
    public Vec3 getDismountLocationForPassenger(LivingEntity passenger) {
        return new Vec3(getX(), getBoundingBox().maxY + 0.05D, getZ());
    }

    @Override
    public boolean causeFallDamage(float fallDistance, float multiplier, DamageSource source) {
        return false;
    }

    @Override
    protected void playSwimSound(float pVolume) {}

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {}

    protected void tickLerp() {
        if(isControlledByLocalInstance()) {
            lerpSteps = 0;
            syncPacketPositionCodec(getX(), getY(), getZ());
        }

        if(lerpSteps > 0) {
            lerpPositionAndRotationStep(lerpSteps, lerpX, lerpY, lerpZ, lerpYRot, lerpXRot);
            lerpSteps--;
        }
    }

    @Override
    public void lerpTo(double x, double y, double z, float yaw, float pitch, int steps) {
        lerpX = x;
        lerpY = y;
        lerpZ = z;
        lerpYRot = yaw;
        lerpXRot = pitch;
        lerpSteps = steps;
    }

    @Override
    public double getEyeY() {
        return 0.7D;
    }

    @Override
    protected Vec3 getPassengerAttachmentPoint(Entity entity, EntityDimensions dimensions, float partialTick) {
        return new Vec3(0, getEyeY(), 0);
    }

    @Override
    public boolean isPickable() {
        return true;
    }

    @Override
    public InteractionResult interact(Player player, InteractionHand hand) {
        if(player.isSecondaryUseActive()) {
            return InteractionResult.SUCCESS;
        }
        else {
            if(!level().isClientSide)
                return player.startRiding(this) ? InteractionResult.CONSUME : InteractionResult.PASS;
            else
                return InteractionResult.SUCCESS;
        }
    }

    @Override
    public boolean isPushable() {
        return true;
    }

    @Override
    protected void readAdditionalSaveData(@NotNull CompoundTag nbt) {
    }

    @Override
    protected void addAdditionalSaveData(@NotNull CompoundTag nbt) {
    }

    @Override
    public boolean canBeCollidedWith() {
        return true;
    }

    @Override
    public void positionRider(@NotNull Entity passenger, Entity.MoveFunction moveFunction) {
        super.positionRider(passenger, moveFunction);
        clampRotation(passenger);
    }

    public boolean isControlledByLocalInstance() {
        Entity entity = getControllingPassenger();
        if(entity instanceof Player)
            return ((Player) entity).isLocalPlayer();

        return !level().isClientSide;
    }

    @Nullable
    @Override
    public LivingEntity getControllingPassenger() {
        List<Entity> list = getPassengers();
        if(list.isEmpty())
            return null;
        return list.getFirst() instanceof LivingEntity le ? le : null;
    }

    @Override
    protected boolean canAddPassenger(@NotNull Entity passenger) {
        return getPassengers().isEmpty();
    }

    @Override
    public void onPassengerTurned(@NotNull Entity entity) {
        clampRotation(entity);
    }

    protected void clampRotation(Entity entity) {
        entity.setYBodyRot(getYRot());
        float f = Mth.wrapDegrees(entity.getYRot() - getYRot());
        float f1 = Mth.clamp(f, -105.0F, 105.0F);
        entity.yRotO += f1 - f;
        entity.setYRot(entity.getYRot() + f1 - f);
        entity.setYHeadRot(entity.getYRot());
    }

    @Override
    public void push(@NotNull Entity entity) {
        if(entity instanceof Broomstick) {
            if(entity.getBoundingBox().minY < getBoundingBox().maxY)
                super.push(entity);
        }
        else if(entity.getBoundingBox().minY <= getBoundingBox().minY) {
            super.push(entity);
        }
    }

    @Override
    public boolean hurt(@NotNull DamageSource source, float pAmount) {
        if(isInvulnerableTo(source)) {
            return false;
        }
        else if(!level().isClientSide && !isRemoved()) {
            setHurtDir(-getHurtDir());
            setHurtTime(10);
            setDamage(getDamage() + pAmount * 10.0F);
            markHurt();
            boolean isSurvivalPlayer = source.getEntity() instanceof Player && ((Player) source.getEntity()).getAbilities().instabuild;
            if(isSurvivalPlayer || getDamage() > 40.0F) {
                if(!isSurvivalPlayer && level().getGameRules().getBoolean(GameRules.RULE_DOENTITYDROPS))
                    spawnAtLocation(EItems.ENCHANTED_BROOMSTICK.get());

                discard();
            }

            return true;
        }
        else {
            return true;
        }
    }

    public void setHurtDir(int pForwardDirection) {
        entityData.set(DATA_ID_HURTDIR, pForwardDirection);
    }

    public int getHurtDir() {
        return entityData.get(DATA_ID_HURTDIR);
    }

    public void setHurtTime(int pTimeSinceHit) {
        entityData.set(DATA_ID_HURT, pTimeSinceHit);
    }

    public int getHurtTime() {
        return entityData.get(DATA_ID_HURT);
    }

    public void setDamage(float pDamageTaken) {
        entityData.set(DATA_ID_DAMAGE, pDamageTaken);
    }

    public float getDamage() {
        return entityData.get(DATA_ID_DAMAGE);
    }

    @Override
    public void animateHurt(float yaw) {
        setHurtDir(-getHurtDir());
        setHurtTime(10);
        setDamage(getDamage() * 11.0F);
    }

    public void setInputs(boolean forward, boolean backward, boolean left, boolean right, boolean up, boolean down) {
        inputAcceleration = 0;
        if(forward)
            inputAcceleration++;
        if(backward)
            inputAcceleration--;

        inputTurn = 0;
        if(left)
            inputTurn--;
        if(right)
            inputTurn++;

        inputClimb = 0;
        if(up)
            inputClimb++;
        if(down)
            inputClimb--;
    }

}
