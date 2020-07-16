package com.favouriteless.magicraft.init;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class MagicraftTabs {

    public static final ItemGroup MAIN = new ItemGroup(  "magicraft.main") {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(MagicraftBlocks.WITCH_OVEN.get());
        }
    };

    public static final ItemGroup INGREDIENTS = new ItemGroup(  "magicraft.ingredients") {
        @Override
        public ItemStack createIcon()
        {
            return new ItemStack(MagicraftItems.WHIFF_OF_MAGIC.get());
        }
    };

    public static final ItemGroup PLANTS = new ItemGroup(  "magicraft.plants") {
        @Override
        public ItemStack createIcon()
        {
            return new ItemStack(MagicraftItems.ROWAN_SAPLING.get());
        }
    };

}
