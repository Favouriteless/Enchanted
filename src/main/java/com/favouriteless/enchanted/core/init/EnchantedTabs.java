package com.favouriteless.enchanted.core.init;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class EnchantedTabs {

    public static final ItemGroup MAIN = new ItemGroup(  "enchanted.main") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(EnchantedBlocks.WITCH_OVEN.get());
        }
    };

    public static final ItemGroup INGREDIENTS = new ItemGroup(  "enchanted.ingredients") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(EnchantedItems.WHIFF_OF_MAGIC.get());
        }
    };

    public static final ItemGroup PLANTS = new ItemGroup(  "enchanted.plants") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(EnchantedItems.ROWAN_SAPLING.get());
        }
    };
}
