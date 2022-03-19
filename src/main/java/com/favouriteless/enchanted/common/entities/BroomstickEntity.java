/*
 * Copyright (c) 2022. Favouriteless
 * Enchanted, a minecraft mod.
 * GNU GPLv3 License
 *
 *     This file is part of Enchanted.
 *
 *     Enchanted is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Enchanted is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Enchanted.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.favouriteless.enchanted.common.entities;

import com.favouriteless.enchanted.Enchanted;
import com.favouriteless.enchanted.common.init.EnchantedItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.entity.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.List;

@EventBusSubscriber(modid=Enchanted.MOD_ID, bus=Bus.FORGE, value=Dist.CLIENT)
public class BroomstickEntity extends Entity {

    public static final double ACCELERATION = 0.02D;
    public static final double MAX_SPEED = 1.0D;

    private float deltaRotX = 0.0F;
    private float deltaRotY = 0.0F;

    private boolean inputLeft = false;
    private boolean inputRight = false;
    private boolean inputUp = false;
    private boolean inputDown = false;
    private boolean inputJump = false;

    private int lerpSteps = 0;
    private double lerpX = 0.0D;
    private double lerpY = 0.0D;
    private double lerpZ = 0.0D;
    private double lerpXRot = 0.0D;
    private double lerpYRot = 0.0D;

    private static final DataParameter<Integer> DATA_ID_HURT = EntityDataManager.defineId(BroomstickEntity.class, DataSerializers.INT);
    private static final DataParameter<Integer> DATA_ID_HURTDIR = EntityDataManager.defineId(BroomstickEntity.class, DataSerializers.INT);
    private static final DataParameter<Float> DATA_ID_DAMAGE = EntityDataManager.defineId(BroomstickEntity.class, DataSerializers.FLOAT);

    public BroomstickEntity(EntityType<BroomstickEntity> type, World world) {
        super(type, world);
        this.blocksBuilding = true;
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(DATA_ID_HURT, 0);
        this.entityData.define(DATA_ID_HURTDIR, 1);
        this.entityData.define(DATA_ID_DAMAGE, 0.0F);
    }

    @Override
    public void tick() {
        if (this.getHurtTime() > 0) {
            this.setHurtTime(this.getHurtTime() - 1);
        }

        if (this.getDamage() > 0.0F) {
            this.setDamage(this.getDamage() - 1.0F);
        }

        super.tick();

        this.tickLerp();
        if(isControlledByLocalInstance()) {
            this.setPacketCoordinates(this.getX(), this.getY(), this.getZ());

            if(level.isClientSide) {
                deltaRotX *= 0.8F;
                deltaRotY *= 0.8F;
                controlBroom();
            }
            else {
                this.setDeltaMovement(this.getDeltaMovement().scale(0.75D).add(0.0D, -0.05D, 0.0D));
            }
            this.move(MoverType.SELF, this.getDeltaMovement());
        }
        else {
            this.setDeltaMovement(Vector3d.ZERO);
        }
    }

    private void tickLerp() {
        if(this.isControlledByLocalInstance()) {
            this.lerpSteps = 0;
            this.setPacketCoordinates(this.getX(), this.getY(), this.getZ());
        }

        if(this.lerpSteps > 0) {
            double x = this.getX() + (this.lerpX - this.getX()) / this.lerpSteps;
            double y = this.getY() + (this.lerpY - this.getY()) / this.lerpSteps;
            double z = this.getZ() + (this.lerpZ - this.getZ()) / this.lerpSteps;

            double xRot = MathHelper.wrapDegrees(this.lerpXRot - this.xRot);
            double yRot = MathHelper.wrapDegrees(this.lerpYRot - this.yRot);

            this.xRot = (float) (this.xRot + xRot / this.lerpSteps);
            this.yRot = (float) (this.yRot + yRot / this.lerpSteps);

            --this.lerpSteps;
            this.setPos(x, y, z);
            this.setRot(this.yRot, this.xRot);
        }
    }

    @OnlyIn(Dist.CLIENT)
    public void lerpTo(double pX, double pY, double pZ, float pYaw, float pPitch, int pPosRotationIncrements, boolean pTeleport) {
        this.lerpX = pX;
        this.lerpY = pY;
        this.lerpZ = pZ;
        this.lerpYRot = pYaw;
        this.lerpXRot = pPitch;
        this.lerpSteps = 10;
    }

    private void controlBroom() {
        if(this.isVehicle()) {
            ClientPlayerEntity player = Minecraft.getInstance().player;
            inputLeft = false;
            inputRight = false;
            inputUp = false;
            inputDown = false;
            inputJump = false;

            if(player.input.left) {
                this.inputLeft = true;
                this.deltaRotY--;
            }
            if(player.input.right) {
                this.inputRight = true;
                this.deltaRotY++;
            }
            if(player.input.down) {
                this.inputDown = true;
                this.deltaRotX--;
            }
            if(player.input.up) {
                this.inputUp = true;
                this.deltaRotX++;
            }
            if(player.input.jumping) {
                this.inputJump = true;
            }

            this.yRot += deltaRotY;
            this.xRot = MathHelper.clamp(this.xRot + deltaRotX, -30.0F, 30.0F);
            this.setDeltaMovement(getNewDeltaMovement());
        }
    }

    public Vector3d getNewDeltaMovement() {
        Vector3d delta = getDeltaMovement();
        Vector3d targetDir = getLookAngle();
        double speed = delta.length();

        Vector3d dir;
        if(speed > 0.001D) {
            dir = delta.normalize();
            double f = Math.abs(dir.subtract(targetDir).length()) / 4.0D;
            dir = new Vector3d(MathHelper.lerp(f, dir.x, targetDir.x), MathHelper.lerp(f, dir.y, targetDir.y), MathHelper.lerp(f, dir.z, targetDir.z));
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

    public void setInput(boolean up, boolean down, boolean left, boolean right) {
        this.inputUp = up;
        this.inputDown = down;
        this.inputLeft = left;
        this.inputRight = right;
    }

    @Override
    protected void readAdditionalSaveData(CompoundNBT pCompound) {

    }

    @Override
    protected float getEyeHeight(Pose pPose, EntitySize pSize) {
        return 0.7F;
    }

    @Override
    protected void addAdditionalSaveData(CompoundNBT pCompound) {

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
    public ActionResultType interact(PlayerEntity pPlayer, Hand pHand) {
        if(pPlayer.isSecondaryUseActive()) {
            return ActionResultType.SUCCESS;
        }
        else {
            if(!level.isClientSide) {
                return pPlayer.startRiding(this) ? ActionResultType.CONSUME : ActionResultType.PASS;
            }
            else {
                return ActionResultType.SUCCESS;
            }
        }
    }

    @Override
    public boolean isPushable() {
        return true;
    }

    @Override
    public boolean canBeCollidedWith() {
        return true;
    }

    @Override
    public IPacket<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public void positionRider(Entity pPassenger) {
        if(hasPassenger(pPassenger)) {
            pPassenger.setPos(getX(), getY() + getPassengersRidingOffset(), getZ());
        }
    }

    public boolean isControlledByLocalInstance() {
        Entity entity = this.getControllingPassenger();
        if(entity instanceof PlayerEntity) {
            return ((PlayerEntity) entity).isLocalPlayer();
        }
        else {
            return !this.level.isClientSide;
        }
    }

    @Nullable
    @Override
    public Entity getControllingPassenger() {
        List<Entity> list = this.getPassengers();
        return list.isEmpty() ? null : list.get(0);
    }

    protected boolean canAddPassenger(Entity pPassenger) {
        return getPassengers().size() < 1;
    }

    @OnlyIn(Dist.CLIENT)
    public void onPassengerTurned(Entity pEntityToUpdate) {
        this.clampRotation(pEntityToUpdate);
    }

    protected void clampRotation(Entity pEntityToUpdate) {
        pEntityToUpdate.setYBodyRot(this.yRot);
        float f = MathHelper.wrapDegrees(pEntityToUpdate.yRot - this.yRot);
        float f1 = MathHelper.clamp(f, -105.0F, 105.0F);
        pEntityToUpdate.yRotO += f1 - f;
        pEntityToUpdate.yRot += f1 - f;
        pEntityToUpdate.setYHeadRot(pEntityToUpdate.yRot);
    }

    @Override
    public void push(Entity pEntity) {
        if(pEntity instanceof BroomstickEntity) {
            if(pEntity.getBoundingBox().minY < this.getBoundingBox().maxY) {
                super.push(pEntity);
            }
        }
        else if(pEntity.getBoundingBox().minY <= this.getBoundingBox().minY) {
            super.push(pEntity);
        }

    }

    @Override
    public boolean hurt(DamageSource pSource, float pAmount) {
        if(this.isInvulnerableTo(pSource)) {
            return false;
        }
        else if(!this.level.isClientSide && !this.removed) {
            this.setHurtDir(-this.getHurtDir());
            this.setHurtTime(10);
            this.setDamage(this.getDamage() + pAmount * 10.0F);
            this.markHurt();
            boolean isSurvivalPlayer = pSource.getEntity() instanceof PlayerEntity && ((PlayerEntity) pSource.getEntity()).abilities.instabuild;
            if(isSurvivalPlayer || this.getDamage() > 40.0F) {
                if(!isSurvivalPlayer && this.level.getGameRules().getBoolean(GameRules.RULE_DOENTITYDROPS)) {
                    this.spawnAtLocation(EnchantedItems.ENCHANTED_BROOMSTICK.get());
                }

                this.remove();
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
        this.entityData.set(DATA_ID_HURTDIR, pForwardDirection);
    }

    /**
     * Gets the forward direction of the entity.
     */
    public int getHurtDir() {
        return this.entityData.get(DATA_ID_HURTDIR);
    }

    /**
     * Sets the time to count down from since the last time entity was hit.
     */
    public void setHurtTime(int pTimeSinceHit) {
        this.entityData.set(DATA_ID_HURT, pTimeSinceHit);
    }

    /**
     * Gets the time since the last hit.
     */
    public int getHurtTime() {
        return this.entityData.get(DATA_ID_HURT);
    }

    /**
     * Sets the damage taken from the last hit.
     */
    public void setDamage(float pDamageTaken) {
        this.entityData.set(DATA_ID_DAMAGE, pDamageTaken);
    }

    /**
     * Gets the damage taken from the last hit.
     */
    public float getDamage() {
        return this.entityData.get(DATA_ID_DAMAGE);
    }

    /**
     * Setups the entity to do the hurt animation. Only used by packets in multiplayer.
     */
    @OnlyIn(Dist.CLIENT)
    @Override
    public void animateHurt() {
        this.setHurtDir(-this.getHurtDir());
        this.setHurtTime(10);
        this.setDamage(this.getDamage() * 11.0F);
    }
}
