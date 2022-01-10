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
import com.favouriteless.enchanted.common.init.EnchantedPackets;
import com.favouriteless.enchanted.common.packets.CBroomstickSteerPacket;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.entity.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
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

    public BroomstickEntity(EntityType<BroomstickEntity> type, World world) {
        super(type, world);
    }

    @Override
    protected void defineSynchedData() {
    }

    @Override
    public void tick() {
        super.tick();

        this.tickLerp();
        if(isControlledByLocalInstance()) {
            this.setPacketCoordinates(this.getX(), this.getY(), this.getZ());

//            this.deltaRotX *= 0.9F;
//            this.deltaRotY *= 0.9F;
            if(level.isClientSide) {
                controlBroom();
                EnchantedPackets.INSTANCE.sendToServer(new CBroomstickSteerPacket(inputUp, inputDown, inputLeft, inputRight));
            }
            this.move(MoverType.SELF, this.getDeltaMovement());
        }
        else {
            this.setDeltaMovement(Vector3d.ZERO);
        }
    }

    private void tickLerp() {
        if (this.isControlledByLocalInstance()) {
            this.lerpSteps = 0;
            this.setPacketCoordinates(this.getX(), this.getY(), this.getZ());
        }

        if (this.lerpSteps > 0) {
            double x = this.getX() + (this.lerpX - this.getX()) / this.lerpSteps;
            double y = this.getY() + (this.lerpY - this.getY()) / this.lerpSteps;
            double z = this.getZ() + (this.lerpZ - this.getZ()) / this.lerpSteps;

            double xRot = MathHelper.wrapDegrees(this.lerpXRot - this.xRot);
            double yRot = MathHelper.wrapDegrees(this.lerpYRot - this.yRot);

            this.xRot = (float)(this.xRot + xRot / this.lerpSteps);
            this.yRot = (float)(this.yRot + yRot / this.lerpSteps);

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
                this.deltaRotY -= 3;
            }
            if(player.input.right) {
                this.inputRight = true;
                this.deltaRotY += 3;
            }
            if(player.input.down) {
                this.inputDown = true;
                this.deltaRotX -= 3;
            }
            if(player.input.up) {
                this.inputUp = true;
                this.deltaRotX += 3;
            }
            if(player.input.jumping) {
                this.inputJump = true;
            }

            this.yRot = deltaRotY;
            this.xRot = deltaRotX;
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

    public float lerpRotX(double value) {
        return Math.round(MathHelper.lerp(value, xRotO, xRot));
    }

    public float lerpRotY(double value) {
        return Math.round(MathHelper.lerp(value, yRotO, yRot));
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void onPassengerTurned(Entity pEntityToUpdate) {
        this.clampRotation(pEntityToUpdate);
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
        return 0.7D;
    }

    @Override
    public boolean isPickable() {
        return true;
    }

    @Override
    public ActionResultType interact(PlayerEntity pPlayer, Hand pHand) {
        if (pPlayer.isSecondaryUseActive()) {
            return ActionResultType.SUCCESS;
        } else {
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
            pPassenger.setPos(getX(), getY()+getPassengersRidingOffset(), getZ());
            pPassenger.yRot += deltaRotY;
            pPassenger.setYHeadRot(pPassenger.getYHeadRot() + deltaRotY);
            clampRotation(pPassenger);
        }
    }

    public boolean isControlledByLocalInstance() {
        Entity entity = this.getControllingPassenger();
        if (entity instanceof PlayerEntity) {
            return ((PlayerEntity)entity).isLocalPlayer();
        } else {
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

    protected void clampRotation(Entity pEntityToUpdate) {
        pEntityToUpdate.setYBodyRot(yRot);
        float f = MathHelper.wrapDegrees(pEntityToUpdate.yRot - yRot);
        float f1 = MathHelper.clamp(f, -105.0F, 105.0F);
        pEntityToUpdate.yRotO += f1 - f;
        pEntityToUpdate.yRot += f1 - f;
        pEntityToUpdate.setYHeadRot(pEntityToUpdate.yRot);
    }

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public static void onRenderPlayer(RenderPlayerEvent.Pre event) {
        PlayerEntity player = event.getPlayer();
        Entity vehicle = player.getVehicle();
        if(vehicle instanceof BroomstickEntity) {
            BroomstickEntity broomstick = (BroomstickEntity)vehicle;
            MatrixStack matrixStack = event.getMatrixStack();
            float partialTicks = event.getPartialRenderTick();

            matrixStack.mulPose(Vector3f.XP.rotationDegrees(broomstick.lerpRotX(partialTicks)));
            matrixStack.translate(0.0F, -0.55F, 0.0F);
        }
    }

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public static void onCameraSetup(EntityViewRenderEvent.CameraSetup event) {
        PlayerEntity player = Minecraft.getInstance().player;
        if(player != null) {
            Entity vehicle = player.getVehicle();
            if(vehicle instanceof BroomstickEntity) {
                BroomstickEntity broomstick = (BroomstickEntity)vehicle;
            }
        }
    }

}
