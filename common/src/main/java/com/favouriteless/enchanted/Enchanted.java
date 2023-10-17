package com.favouriteless.enchanted;

import com.favouriteless.enchanted.common.init.registry.EnchantedBlocks;
import com.favouriteless.enchanted.common.init.registry.EnchantedItems;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

public class Enchanted {

    public static final String MOD_ID = "enchanted";
    public static final Logger LOG = LoggerFactory.getLogger(MOD_ID);
    public static final Random RANDOM = new Random();

    public static void init() {
    }

    public static void loadRegistries() {
        EnchantedItems.load();
        EnchantedBlocks.load();
    }

    public static ResourceLocation location(String path) {
        return new ResourceLocation(MOD_ID, path);
    }

}