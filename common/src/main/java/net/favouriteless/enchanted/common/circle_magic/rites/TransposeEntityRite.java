package net.favouriteless.enchanted.common.circle_magic.rites;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public abstract class TransposeEntityRite extends LocationTargetRite {

    public TransposeEntityRite(BaseRiteParams baseParams, RiteParams params) {
        super(baseParams, params);
    }

    @Override
    protected boolean onStart(RiteParams params) {
        Entity transposee = getTransposee(params);
        if (transposee == null)
            return cancel();

        findTargetLocation(params);
        if (targetLevel == null || targetPos == null)
            return cancel();

        portalParticles((ServerLevel) transposee.level(), transposee.blockPosition());
        portalParticles(targetLevel, targetPos);

        transposee.level().playSound(null, transposee.blockPosition(), SoundEvents.ENDERMAN_TELEPORT, SoundSource.MASTER, 1, 1);

        Vec3 destination = targetPos.getCenter().add(0, 0.01d, 0);
        if (targetLevel != transposee.level())
            transposee.changeDimension(targetLevel);
        else
            transposee.teleportTo(destination.x, destination.y, destination.z);

        level.playSound(null, targetPos, SoundEvents.ENDERMAN_TELEPORT, SoundSource.MASTER, 1, 1);

        return false;
    }

    protected abstract Entity getTransposee(RiteParams params);

    protected void portalParticles(ServerLevel level, BlockPos pos) {
        for (int i = 0; i < 25; i++) {
            double dx = pos.getX() + (Math.random() * 1.5D);
            double dy = pos.getY() + (Math.random() * 2.0D);
            double dz = pos.getZ() + (Math.random() * 1.5D);
            level.sendParticles(ParticleTypes.PORTAL, dx, dy, dz, 1, 0.0D, 0.0D, 0.0D, 0.0D);
        }
    }

}
