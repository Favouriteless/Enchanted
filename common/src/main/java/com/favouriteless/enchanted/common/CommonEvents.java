package com.favouriteless.enchanted.common;

import com.favouriteless.enchanted.api.taglock.BedTaglockSavedData;
import com.favouriteless.enchanted.api.taglock.IBedTaglock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
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

}
