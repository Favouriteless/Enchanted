package net.favouriteless.enchanted.common.util;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;

public class ContainerUtils {

    public static void loadAllItems(CompoundTag tag, NonNullList<ItemStack> items, HolderLookup.Provider levelRegistry) {
        ListTag list = tag.getList("Items", Tag.TAG_COMPOUND);

        for(int i = 0; i < list.size(); i++) {
            CompoundTag item = list.getCompound(i);
            items.add(ItemStack.parse(levelRegistry, item).orElse(ItemStack.EMPTY));
        }
    }

}
