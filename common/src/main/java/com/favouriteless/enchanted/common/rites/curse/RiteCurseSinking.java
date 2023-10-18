package com.favouriteless.enchanted.common.rites.curse;

import com.favouriteless.enchanted.api.rites.AbstractCurseRite;
import com.favouriteless.enchanted.common.init.registry.CurseTypes;
import com.favouriteless.enchanted.common.init.registry.EnchantedBlocks;
import com.favouriteless.enchanted.common.init.registry.EnchantedItems;
import com.favouriteless.enchanted.common.rites.CirclePart;
import com.favouriteless.enchanted.common.rites.RiteType;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.Items;

import java.util.UUID;

public class RiteCurseSinking extends AbstractCurseRite {

    public RiteCurseSinking(RiteType<?> type, ServerLevel level, BlockPos pos, UUID caster) {
        super(type, level, pos, caster, 2000, CurseTypes.SINKING); // Power, curse type
        CIRCLES_REQUIRED.put(CirclePart.MEDIUM, EnchantedBlocks.CHALK_RED.get());
        ITEMS_REQUIRED.put(EnchantedItems.TAGLOCK_FILLED.get(), 1);
        ITEMS_REQUIRED.put(EnchantedItems.EXHALE_OF_THE_HORNED_ONE.get(), 1);
        ITEMS_REQUIRED.put(EnchantedItems.BREW_OF_THE_GROTESQUE.get(), 1);
        ITEMS_REQUIRED.put(Items.NETHER_WART, 1);
        ITEMS_REQUIRED.put(Items.INK_SAC, 1);
    }

    @Override
    protected boolean checkAdditional() {
        return getLevel().isRaining() && getLevel().isThundering();
    }

}
