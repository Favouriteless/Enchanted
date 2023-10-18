package com.favouriteless.enchanted.common.rites.processing;

import com.favouriteless.enchanted.common.init.registry.EnchantedBlocks;
import com.favouriteless.enchanted.common.init.registry.EnchantedItems;
import com.favouriteless.enchanted.common.rites.CirclePart;
import com.favouriteless.enchanted.common.rites.RiteType;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.Items;

import java.util.UUID;

public class RiteBroilingCharged extends RiteBroiling {

    public RiteBroilingCharged(RiteType<?> type, ServerLevel level, BlockPos pos, UUID caster) {
        super(type, level, pos, caster, 0); // Power, power per tick
        CIRCLES_REQUIRED.put(CirclePart.SMALL, EnchantedBlocks.CHALK_RED.get());
        ITEMS_REQUIRED.put(Items.COAL, 1);
        ITEMS_REQUIRED.put(Items.BLAZE_ROD, 1);
        ITEMS_REQUIRED.put(EnchantedItems.WOOD_ASH.get(), 1);
        ITEMS_REQUIRED.put(EnchantedItems.ATTUNED_STONE_CHARGED.get(), 1);
    }

}
