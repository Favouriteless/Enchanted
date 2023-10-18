package com.favouriteless.enchanted.common.rites.binding;

import com.favouriteless.enchanted.common.init.registry.EnchantedBlocks;
import com.favouriteless.enchanted.common.init.registry.EnchantedItems;
import com.favouriteless.enchanted.common.rites.CirclePart;
import com.favouriteless.enchanted.common.rites.RiteType;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.Items;

import java.util.UUID;

public class RiteBindingWaystonePlayerCharged extends RiteBindingWaystonePlayer {

    public RiteBindingWaystonePlayerCharged(RiteType<?> type, ServerLevel level, BlockPos pos, UUID caster) {
        super(type, level, pos, caster, 0); // Power, power per tick
        CIRCLES_REQUIRED.put(CirclePart.SMALL, EnchantedBlocks.CHALK_WHITE.get());
        ITEMS_REQUIRED.put(EnchantedItems.WAYSTONE.get(), 1);
        ITEMS_REQUIRED.put(EnchantedItems.ENDER_DEW.get(), 1);
        ITEMS_REQUIRED.put(Items.SLIME_BALL, 1);
        ITEMS_REQUIRED.put(EnchantedItems.ICY_NEEDLE.get(), 1);
        ITEMS_REQUIRED.put(EnchantedItems.ATTUNED_STONE_CHARGED.get(), 1);
        ITEMS_REQUIRED.put(EnchantedItems.TAGLOCK_FILLED.get(), 1);
    }

}
