package net.favouriteless.enchanted.common.circle_magic.rites;

import net.favouriteless.enchanted.common.init.EParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;

public class SkyWrathRite extends LocationTargetRite {

    public static final int START_RAINING = 120;
    public static final int EXPLODE = 180;
    public static final double LIGHTNING_RADIUS = 5;

    public SkyWrathRite(BaseRiteParams baseParams, RiteParams params) {
        super(baseParams, params);
    }

    @Override
    protected boolean onStart(RiteParams params) {
        super.onStart(params);

        targetLevel.sendParticles(EParticleTypes.SKY_WRATH_SEED.get(),
                pos.getX() + 0.5D, pos.getY() + 2.0D, pos.getZ() + 0.5D,
                1, 0, 0, 0, 0);

        return true;
    }

    @Override
    protected boolean onTick(RiteParams params) {
        if(params.ticks() == START_RAINING) {
            level.setWeatherParameters(0, 6000, true, true);
        }
        else if(params.ticks() > EXPLODE) {
            findTargetLocation(params); // Just in case the entity variant has it's target move position
            spawnLightning(targetLevel, targetPos.getX() + 0.5D, targetPos.getY(), targetPos.getZ() + 0.5D);
            return false;
        }

        return true;
    }

    /**
     * Spawn 6 lightning bolts in a circle around xyz.
     */
    protected void spawnLightning(ServerLevel level, double x, double y, double z) {
        for(int a = 0; a < 360; a += 60) {
            double angle = Math.toRadians(a);
            double cx = x + Math.sin(angle) * LIGHTNING_RADIUS;
            double cz = z + Math.cos(angle) * LIGHTNING_RADIUS;

            LightningBolt bolt = EntityType.LIGHTNING_BOLT.create(level);
            bolt.setPos(cx, y, cz);
            level.addFreshEntity(bolt);
        }
    }

}
