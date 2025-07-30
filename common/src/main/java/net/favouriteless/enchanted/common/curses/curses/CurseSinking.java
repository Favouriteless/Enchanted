package net.favouriteless.enchanted.common.curses.curses;

import com.mojang.serialization.MapCodec;
import net.favouriteless.enchanted.api.curses.Curse;
import net.favouriteless.enchanted.common.Enchanted;
import net.favouriteless.enchanted.common.network.client.SinkingCursePayload;
import net.favouriteless.enchanted.platform.CommonServices;
import net.minecraft.server.level.ServerPlayer;

public class CurseSinking implements Curse {

    private static final MapCodec<CurseSinking> CODEC = MapCodec.unit(CurseSinking::new);
    public static final Type<CurseSinking> TYPE = new Type<>(Enchanted.id("sinking"), CurseSinking::new, CODEC);

    public boolean wasSwimming = false;
    public boolean wasFlying = false;

    @Override
    public void tick(ServerPlayer target, int strength, long age) {
        boolean isSwimming = target.isInWater();
        boolean isFlying = target.isFallFlying();

        if(isSwimming == wasSwimming && isFlying == wasFlying)
            return;

        if(isSwimming)
            CommonServices.NETWORK.sendToPlayer(new SinkingCursePayload(-0.025F * (strength + 1)), target);
        else if(isFlying)
            CommonServices.NETWORK.sendToPlayer(new SinkingCursePayload(-0.05F * (strength + 1)), target);
        else
            CommonServices.NETWORK.sendToPlayer(new SinkingCursePayload(0.0F), target);

        wasSwimming = isSwimming;
        wasFlying = isFlying;
    }

    @Override
    public void onRemove(ServerPlayer target, int strength, long age) {
        CommonServices.NETWORK.sendToPlayer(new SinkingCursePayload(0), target);
    }

    @Override
    public Type<?> type() {
        return null;
    }

}
