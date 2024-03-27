package com.favouriteless.enchanted.common.rites.processing;

import com.favouriteless.enchanted.api.rites.AbstractCreateItemRite;
import com.favouriteless.enchanted.common.init.registry.EnchantedBlocks;
import com.favouriteless.enchanted.common.init.registry.EnchantedItems;
import com.favouriteless.enchanted.common.rites.CirclePart;
import com.favouriteless.enchanted.common.rites.RiteType;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ItemStack;

import java.util.UUID;

public class RiteInfusionBroom extends AbstractCreateItemRite {

    public RiteInfusionBroom(RiteType<?> type, ServerLevel level, BlockPos pos, UUID caster) {
        super(type, level, pos, caster, 3000, SoundEvents.ZOMBIE_VILLAGER_CURE, new ItemStack(EnchantedItems.ENCHANTED_BROOMSTICK.get(), 1)); // Power, power per tick
        CIRCLES_REQUIRED.put(CirclePart.SMALL, EnchantedBlocks.CHALK_WHITE.get());
        CIRCLES_REQUIRED.put(CirclePart.MEDIUM, EnchantedBlocks.CHALK_WHITE.get());
        ITEMS_REQUIRED.put(EnchantedItems.BROOM.get(), 1);
        ITEMS_REQUIRED.put(EnchantedItems.FLYING_OINTMENT.get(), 1);
    }

}
