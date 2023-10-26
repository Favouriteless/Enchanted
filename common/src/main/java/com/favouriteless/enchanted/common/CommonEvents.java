package com.favouriteless.enchanted.common;

import com.favouriteless.enchanted.api.taglock.BedTaglockSavedData;
import com.favouriteless.enchanted.api.taglock.IBedTaglock;
import com.favouriteless.enchanted.common.curses.CurseManager;
import com.favouriteless.enchanted.common.rites.RiteManager;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BedBlockEntity;

public class CommonEvents {

    public static void onPlayerSleeping(Player player, BlockPos pos) {
        if(player != null && pos != null && !player.level.isClientSide) {
            if(player.level.getBlockEntity(pos) instanceof BedBlockEntity bed) {
                BedTaglockSavedData data = BedTaglockSavedData.get(player.level);
                IBedTaglock entry = data.getEntry(bed);
                entry.setUUID(player.getUUID());
                entry.setName(player.getDisplayName().getString());
                data.setDirty();
            }
        }
    }

    public static void onLevelTick(Level level) {
        if(level instanceof ServerLevel serverLevel) {
            CurseManager.tick(serverLevel);
            RiteManager.tick(serverLevel);
        }
    }

    public static void onPlayerLoggedIn(Player player) {
        if(player instanceof ServerPlayer serverPlayer)
            CurseManager.playerLoggedIn(serverPlayer);
    }

    public static void onPlayerLoggedOut(Player player) {
        if(player instanceof ServerPlayer serverPlayer)
            CurseManager.playerLoggedOut(serverPlayer);
    }

}
