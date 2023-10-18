package com.favouriteless.enchanted.common.rites.binding;

import com.favouriteless.enchanted.api.rites.AbstractCreateItemRite;
import com.favouriteless.enchanted.common.init.registry.EnchantedBlocks;
import com.favouriteless.enchanted.common.init.registry.EnchantedItems;
import com.favouriteless.enchanted.common.rites.CirclePart;
import com.favouriteless.enchanted.common.rites.RiteType;
import com.favouriteless.enchanted.common.util.WaystoneHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.UUID;

public class RiteBindingWaystone extends AbstractCreateItemRite {

    protected RiteBindingWaystone(RiteType<?> type, ServerLevel level, BlockPos pos, UUID caster, int power) {
        super(type, level, pos, caster, power, SoundEvents.ZOMBIE_VILLAGER_CURE, new ItemStack(EnchantedItems.BOUND_WAYSTONE.get()));
    }

    public RiteBindingWaystone(RiteType<?> type, ServerLevel level, BlockPos pos, UUID caster) {
        this(type, level, pos, caster, 500); // Power, power per tick
        CIRCLES_REQUIRED.put(CirclePart.SMALL, EnchantedBlocks.CHALK_WHITE.get());
        ITEMS_REQUIRED.put(EnchantedItems.WAYSTONE.get(), 1);
        ITEMS_REQUIRED.put(EnchantedItems.ENDER_DEW.get(), 1);
        ITEMS_REQUIRED.put(Items.GLOWSTONE_DUST, 1);
    }

    @Override
    public void setupItemNbt(int index, ItemStack stack) {
        if(index == 0) {
            if(getLevel() != null)
                WaystoneHelper.bind(stack, getLevel(), getPos());
        }
    }

}
