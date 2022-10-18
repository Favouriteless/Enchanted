/*
 *
 *   Copyright (c) 2022. Favouriteless
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
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
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
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.List;

public class BroomstickEntity extends Entity {

    public static final double ACCELERATION = 0.02D;
    public static final double MAX_SPEED = 1.0D;

    private float deltaRotX = 0.0F;
    private float deltaRotY = 0.0F;

    private boolean inputJump = false;

    private int lerpSteps = 0;
    private double lerpX = 0.0D;
    private double lerpY = 0.0D;
    private double lerpZ = 0.0D;
    private double lerpXRot = 0.0D;
    private double lerpYRot = 0.0D;

    private static final EntityDataAccessor<Integer> DATA_ID_HURT = SynchedEntityData.defineId(BroomstickEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> DATA_ID_HURTDIR = SynchedEntityData.defineId(BroomstickEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Float> DATA_ID_DAMAGE = SynchedEntityData.defineId(BroomstickEntity.class, EntityDataSerializers.FLOAT);

    public BroomstickEntity(EntityType<BroomstickEntity> type, Level world) {
        super(type, world);
        blocksBuilding = true;
    }

    @Override
    protected void defineSynchedData() {
        entityData.define(DATA_ID_HURT, 0);
        entityData.define(DATA_ID_HURTDIR, 1);
        entityData.define(DATA_ID_DAMAGE, 0.0F);
    }

    @Override
    public void tick() {
        if (getHurtTime() > 0) {
            setHurtTime(getHurtTime() - 1);
        }

        if (getDamage() > 0.0F) {
            setDamage(getDamage() - 1.0F);
        }

        super.tick();

        tickLerp();
        if(isControlledByLocalInstance()) {
            setPacketCoordinates(getX(), getY(), getZ());

            if(level.isClientSide) {
                deltaRotX *= 0.8F;
                deltaRotY *= 0.8F;
                controlBroom();
            }
            else {
                setDeltaMovement(getDeltaMovement().scale(0.75D).add(0.0D, -0.05D, 0.0D));
            }
            move(MoverType.SELF, getDeltaMovement());
        }
        else {
            setDeltaMovement(Vec3.ZERO);
        }
    }

    @Override
    public boolean canTrample(BlockState state, BlockPos pos, float fallDistance) {
        return false;
    }

    @Override
    public boolean causeFallDamage(float fallDistance, float multiplier, DamageSource source) {
        return false;
    }

    @Override
    protected void playSwimSound(float pVolume) {}

    @Override
    protected void playStepSound(BlockPos pPos, BlockState pState) {}

    private void tickLerp() {
        if(isControlledByLocalInstance()) {
            lerpSteps = 0;
            setPacketCoordinates(getX(), getY(), getZ());
        }

        if(lerpSteps > 0) {
            double x = getX() + (lerpX - getX()) / lerpSteps;
            double y = getY() + (lerpY - getY()) / lerpSteps;
            double z = getZ() + (lerpZ - getZ()) / lerpSteps;

            double xRot = Mth.wrapDegrees(lerpXRot - getXRot());
            double yRot = Mth.wrapDegrees(lerpYRot - getYRot());

            setXRot((float)(getXRot() + xRot / lerpSteps));
            setYRot((float)(getYRot() + yRot / lerpSteps));;

            --lerpSteps;
            setPos(x, y, z);
            setRot(getYRot(), getXRot());
        }
    }

    @OnlyIn(Dist.CLIENT)
    public void lerpTo(double pX, double pY, double pZ, float pYaw, float pPitch, int pPosRotationIncrements, boolean pTeleport) {
        lerpX = pX;
        lerpY = pY;
        lerpZ = pZ;
        lerpYRot = pYaw;
        lerpXRot = pPitch;
        lerpSteps = 10;
    }

    private void controlBroom() {
        if(isVehicle()) {
            LocalPlayer player = Minecraft.getInstance().player;
            inputJump = false;

            if(player.input.left) {
                deltaRotY--;
            }
            if(player.input.right) {
                deltaRotY++;
            }
            if(player.input.down) {
                deltaRotX--;
            }
            if(player.input.up) {
                deltaRotX++;
            }
            if(player.input.jumping) {
                inputJump = true;
            }

            setYRot(getYRot() + deltaRotY);
            setXRot(Mth.clamp(getXRot() + deltaRotX, -30.0F, 30.0F));
            if(this.hasPassenger(player)) {
                player.setYRot(player.getYRot() + deltaRotY);
            }

            setDeltaMovement(getNewDeltaMovement());
        }
    }

    public Vec3 getNewDeltaMovement() {
        Vec3 delta = getDeltaMovement();
        Vec3 targetDir = getLookAngle();
        double speed = delta.length();

        Vec3 dir;
        if(speed > 0.001D) {
            dir = delta.normalize();
            double f = Math.abs(dir.subtract(targetDir).length()) / 4.0D;
            dir = new Vec3(Mth.lerp(f, dir.x, targetDir.x), Mth.lerp(f, dir.y, targetDir.y), Mth.lerp(f, dir.z, targetDir.z));
        }
        else {
            dir = targetDir;
        }

        if(inputJump)
            speed += ACCELERATION;
        else
            speed *= 0.85D;


        speed = Math.max(Math.min(MAX_SPEED, speed), 0);
        return dir.multiply(speed, speed, speed);
    }

    @Override
    protected float getEyeHeight(Pose pose, EntityDimensions size) {
        return 0.7F;
    }

    @Override
    public double getPassengersRidingOffset() {
        return 0.15D;
    }

    @Override
    public boolean isPickable() {
        return true;
    }

    @Override
    public InteractionResult interact(Player pPlayer, InteractionHand pHand) {
        if(pPlayer.isSecondaryUseActive()) {
            return InteractionResult.SUCCESS;
        }
        else {
            if(!level.isClientSide) {
                return pPlayer.startRiding(this) ? InteractionResult.CONSUME : InteractionResult.PASS;
            }
            else {
                return InteractionResult.SUCCESS;
            }
        }
    }

    @Override
    public boolean isPushable() {
        return true;
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag nbt) {

    }

    @Override
    protected void addAdditionalSaveData(CompoundTag nbt) {

    }

    @Override
    public boolean canBeCollidedWith() {
        return true;
    }

    @Override
    public Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public void positionRider(Entity passenger) {
        if(hasPassenger(passenger)) {
            passenger.setPos(getX(), getY() + getPassengersRidingOffset(), getZ());

            clampRotation(passenger);
        }
    }

    public boolean isControlledByLocalInstance() {
        Entity entity = getControllingPassenger();
        if(entity instanceof Player) {
            return ((Player) entity).isLocalPlayer();
        }
        else {
            return !level.isClientSide;
        }
    }

    @Nullable
    @Override
    public Entity getControllingPassenger() {
        List<Entity> list = getPassengers();
        return list.isEmpty() ? null : list.get(0);
    }

    protected boolean canAddPassenger(Entity passenger) {
        return getPassengers().size() < 1;
    }

    @Override
    public void onPassengerTurned(Entity entity) {
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
    public void push(Entity entity) {
        if(entity instanceof BroomstickEntity) {
            if(entity.getBoundingBox().minY < getBoundingBox().maxY) {
                super.push(entity);
            }
        }
        else if(entity.getBoundingBox().minY <= getBoundingBox().minY) {
            super.push(entity);
        }

    }

    @Override
    public boolean hurt(DamageSource source, float pAmount) {
        if(isInvulnerableTo(source)) {
            return false;
        }
        else if(!level.isClientSide && !isRemoved()) {
            setHurtDir(-getHurtDir());
            setHurtTime(10);
            setDamage(getDamage() + pAmount * 10.0F);
            markHurt();
            boolean isSurvivalPlayer = source.getEntity() instanceof Player && ((Player) source.getEntity()).getAbilities().instabuild;
            if(isSurvivalPlayer || getDamage() > 40.0F) {
                if(!isSurvivalPlayer && level.getGameRules().getBoolean(GameRules.RULE_DOENTITYDROPS)) {
                    spawnAtLocation(EnchantedItems.ENCHANTED_BROOMSTICK.get());
                }

                discard();
            }

            return true;
        }
        else {
            return true;
        }
    }

    /**
     * Sets the forward direction of the entity.
     */
    public void setHurtDir(int pForwardDirection) {
        entityData.set(DATA_ID_HURTDIR, pForwardDirection);
    }

    /**
     * Gets the forward direction of the entity.
     */
    public int getHurtDir() {
        return entityData.get(DATA_ID_HURTDIR);
    }

    /**
     * Sets the time to count down from since the last time entity was hit.
     */
    public void setHurtTime(int pTimeSinceHit) {
        entityData.set(DATA_ID_HURT, pTimeSinceHit);
    }

    /**
     * Gets the time since the last hit.
     */
    public int getHurtTime() {
        return entityData.get(DATA_ID_HURT);
    }

    /**
     * Sets the damage taken from the last hit.
     */
    public void setDamage(float pDamageTaken) {
        entityData.set(DATA_ID_DAMAGE, pDamageTaken);
    }

    /**
     * Gets the damage taken from the last hit.
     */
    public float getDamage() {
        return entityData.get(DATA_ID_DAMAGE);
    }

    /**
     * Setups the entity to do the hurt animation. Only used by packets in multiplayer.
     */
    @OnlyIn(Dist.CLIENT)
    @Override
    public void animateHurt() {
        setHurtDir(-getHurtDir());
        setHurtTime(10);
        setDamage(getDamage() * 11.0F);
    }
}
