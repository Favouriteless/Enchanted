package net.favouriteless.enchanted.common.enchanted.circle_magic.rites;

import net.favouriteless.enchanted.client.particles.ImprisonmentCageParticle;
import net.favouriteless.enchanted.common.init.EParticleTypes;
import net.favouriteless.enchanted.common.init.ETags.EntityTypes;
import net.favouriteless.enchanted.common.util.EntityUtils;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class ImprisonmentRite extends Rite {

    public static final double ATTRACT_FACTOR = 0.6D;
    public static final double INNER_RADIUS = 3.0F;
    public static final double OUTER_RADIUS = 4.0F;
    private static final double INNER_RADIUS_SQR = INNER_RADIUS * INNER_RADIUS;
    private static final double OUTER_RADIUS_SQR = OUTER_RADIUS * OUTER_RADIUS;

    public ImprisonmentRite(BaseRiteParams baseParams, RiteParams params) {
        super(baseParams, params);
    }

    @Override
    protected boolean onStart(RiteParams params) {
        level.playSound(null, pos, SoundEvents.ZOMBIE_VILLAGER_CURE, SoundSource.MASTER, 0.5F, 1.0F);
        return true;
    }

    @Override
    protected boolean onTick(RiteParams params) {
        Vec3 center = pos.getBottomCenter();
        List<Entity> entities = level.getEntities(
                (Entity)null,
                new AABB(center.subtract(OUTER_RADIUS, 0, OUTER_RADIUS), center.add(OUTER_RADIUS, 4, OUTER_RADIUS)),
                e -> e.getType().is(EntityTypes.MONSTERS) &&
                        new Vec3(e.getX()-center.x, 0, e.getZ()-center.z).lengthSqr() > INNER_RADIUS_SQR &&
                        new Vec3(e.getX()-center.x, 0, e.getZ()-center.z).lengthSqr() < OUTER_RADIUS_SQR
        );

        for(Entity entity : entities) {
            Entity toApply = EntityUtils.getControllingEntity(entity);

            Vec3 local = new Vec3(toApply.getX()-center.x, 0, toApply.getZ()-center.z);
            Vec3 vel = toApply.getDeltaMovement();
            vel = new Vec3(vel.x, 0, vel.z);

            double dot = vel.dot(local) / local.dot(local); // + if entity moving away from center, - if towards
            if(dot > 0)
                toApply.addDeltaMovement(local.scale(-dot));

            double d = local.lengthSqr();
            if(d > (INNER_RADIUS - 0.3F) * (INNER_RADIUS - 0.3F))
                toApply.addDeltaMovement(local.scale(-ATTRACT_FACTOR / d));
        }


        if(params.ticks() % (ImprisonmentCageParticle.LIFETIME + 15) == 0) { // 15 ticks for the fade time
            level.sendParticles(EParticleTypes.IMPRISONMENT_CAGE_SEED.get(), pos.getX()+0.5D,
                    pos.getY()+0.2D, pos.getZ()+0.5D, 1, 0, 0, 0, 0);
        }
        return true;
    }
}
