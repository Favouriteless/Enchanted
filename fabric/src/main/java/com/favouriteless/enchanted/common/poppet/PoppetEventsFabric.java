package com.favouriteless.enchanted.common.poppet;

import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;

public class PoppetEventsFabric {

    public static void register() {
        ServerLivingEntityEvents.ALLOW_DAMAGE.register(((entity, source, amount) -> !PoppetEvents.onLivingEntityHurt(entity, amount, source)));
    }

}
