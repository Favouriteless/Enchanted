package net.favouriteless.enchanted.common.circle_magic.rites;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;

public class ProtectionEntityRite extends ProtectionRite {

    public ProtectionEntityRite(BaseRiteParams baseParams, RiteParams params, int radius, int duration, boolean blocking) {
        super(baseParams, params, radius, duration, blocking);
    }

    @Override
    protected void findTargetLocation(RiteParams params) {
        if(params.target != null) {
            Entity target = findEntity(params.target);
            if(target == null)
                return;

            targetLevel = (ServerLevel)target.level();
            targetPos = target.blockPosition();
        }
    }

}
