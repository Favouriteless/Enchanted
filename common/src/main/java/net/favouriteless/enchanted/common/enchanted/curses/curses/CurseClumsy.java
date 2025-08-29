package net.favouriteless.enchanted.common.enchanted.curses.curses;

import com.mojang.serialization.MapCodec;
import net.favouriteless.enchanted.api.curses.Curse;
import net.favouriteless.enchanted.common.Enchanted;
import net.favouriteless.enchanted.common.ServerConfig;
import net.favouriteless.enchanted.common.init.EAttachmentTypes;
import net.favouriteless.enchanted.platform.EServices;
import net.minecraft.server.level.ServerPlayer;

public class CurseClumsy implements Curse {

    private static final MapCodec<CurseClumsy> CODEC = MapCodec.unit(CurseClumsy::new);
    public static final Type<CurseClumsy> TYPE = new Type<>(Enchanted.id("clumsy"), CurseClumsy::new, CODEC);

    @Override
    public void onInitialise(ServerPlayer target, int strength, long age) {
        EServices.ATTACHMENT.set(target, EAttachmentTypes.CLUMSY_CHANCE, ServerConfig.INSTANCE.clumsyPerLevel.get() * strength);
    }

    @Override
    public void onRemove(ServerPlayer target, int strength, long age) {
        EServices.ATTACHMENT.set(target, EAttachmentTypes.CLUMSY_CHANCE, 0.0D);
    }

    @Override
    public Type<?> type() {
        return TYPE;
    }

}
