package com.favouriteless.enchanted.common.rites.binding;

import com.favouriteless.enchanted.api.rites.AbstractCreateItemRite;
import com.favouriteless.enchanted.common.init.registry.EnchantedBlocks;
import com.favouriteless.enchanted.common.init.registry.EnchantedItems;
import com.favouriteless.enchanted.common.items.TaglockFilledItem;
import com.favouriteless.enchanted.common.rites.CirclePart;
import com.favouriteless.enchanted.common.rites.RiteType;
import com.favouriteless.enchanted.common.util.WaystoneHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.UUID;

public class RiteBindingWaystonePlayer extends AbstractCreateItemRite {

    protected RiteBindingWaystonePlayer(RiteType<?> type, ServerLevel level, BlockPos pos, UUID caster, int power) {
        super(type, level, pos, caster, power, SoundEvents.ENDER_DRAGON_GROWL, new ItemStack(EnchantedItems.BLOODED_WAYSTONE.get(), 1));
    }

    public RiteBindingWaystonePlayer(RiteType<?> type, ServerLevel level, BlockPos pos, UUID caster) {
        this(type, level, pos, caster, 500); // Power, power per tick
        CIRCLES_REQUIRED.put(CirclePart.SMALL, EnchantedBlocks.CHALK_WHITE.get());
        ITEMS_REQUIRED.put(EnchantedItems.WAYSTONE.get(), 1);
        ITEMS_REQUIRED.put(EnchantedItems.ENDER_DEW.get(), 1);
        ITEMS_REQUIRED.put(Items.SLIME_BALL, 1);
        ITEMS_REQUIRED.put(Items.SNOWBALL, 1);
        ITEMS_REQUIRED.put(EnchantedItems.TAGLOCK_FILLED.get(), 1);
    }

    @Override
    public void setupItemNbt(int index, ItemStack stack) {
        if(index == 0) {
            if(getTargetUUID() != null)
                WaystoneHelper.bind(stack, getTargetUUID(), tryFindTargetEntity() != null ? getTargetEntity().getDisplayName().getString() : "");
        }
    }

    @Override
    protected boolean checkAdditional() {
        for(ItemStack stack : itemsConsumed) {
            if(stack.getItem() == EnchantedItems.TAGLOCK_FILLED.get()) {
                setTargetUUID(TaglockFilledItem.getUUID(stack));
                if(getTargetUUID() == null)
                    return false;
            }
        }
        return true;
    }

}
