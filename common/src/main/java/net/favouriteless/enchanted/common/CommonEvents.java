package net.favouriteless.enchanted.common;

import net.favouriteless.enchanted.common.enchanted.circle_magic.RiteManager;
import net.favouriteless.enchanted.common.enchanted.curses.CurseManagerImpl;
import net.favouriteless.enchanted.common.enchanted.BedTaglockSavedData;
import net.favouriteless.enchanted.common.enchanted.BedTaglockSavedData.BedTaglockData;
import net.favouriteless.enchanted.common.items.component.EntityRefData;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BedBlockEntity;

public class CommonEvents {

    public static void onPlayerSleeping(Player player, BlockPos pos) {
        if(player == null || pos == null || player.level().isClientSide)
            return;

        if(player.level().getBlockEntity(pos) instanceof BedBlockEntity bed) {
            BedTaglockSavedData data = BedTaglockSavedData.get(player.level());
            BedTaglockData entry = data.getEntry(bed);
            entry.setData(EntityRefData.of(player.getUUID(), player.getDisplayName().getString()));
            data.setDirty();
        }
    }

    public static void onLevelTick(Level level) {
        if(level instanceof ServerLevel serverLevel) {
            RiteManager.tick(serverLevel);
            CurseManagerImpl.INSTANCE.tick(serverLevel);
        }
    }

    public static void onPlayerLoggedIn(Player player) {
        if(player instanceof ServerPlayer sp)
            CurseManagerImpl.INSTANCE.initialisePlayer(sp);
    }

}
