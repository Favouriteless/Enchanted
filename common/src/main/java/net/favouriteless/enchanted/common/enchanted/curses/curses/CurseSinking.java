package net.favouriteless.enchanted.common.enchanted.curses.curses;

import com.mojang.serialization.MapCodec;
import net.favouriteless.enchanted.api.curses.Curse;
import net.favouriteless.enchanted.common.Enchanted;
import net.favouriteless.enchanted.common.init.EAttachmentTypes;
import net.favouriteless.enchanted.platform.EServices;
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

        float sink = isSwimming ? -0.025F * (strength + 1) : isFlying ? -0.05F * (strength + 1) : 0;
        EServices.ATTACHMENT.set(target, EAttachmentTypes.SINKING_FACTOR, sink);

        wasSwimming = isSwimming;
        wasFlying = isFlying;
    }

    @Override
    public void onRemove(ServerPlayer target, int strength, long age) {
        EServices.ATTACHMENT.set(target, EAttachmentTypes.SINKING_FACTOR, 0.0F);
    }

    @Override
    public Type<?> type() {
        return TYPE;
    }

}
