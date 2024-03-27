package com.favouriteless.enchanted;

import com.favouriteless.enchanted.common.init.EnchantedData;
import com.favouriteless.enchanted.common.init.registry.*;
import com.favouriteless.enchanted.common.network.EnchantedPackets;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

public class Enchanted {

    public static final String MOD_ID = "enchanted";
    public static final Logger LOG = LoggerFactory.getLogger(MOD_ID);
    public static final Random RANDOM = new Random();
    public static final RandomSource RANDOMSOURCE = RandomSource.create();


    public static void init() {
        EnchantedPackets.register();
        Enchanted.loadRegistries();
    }

    public static void loadRegistries() {
        EnchantedSoundEvents.load();
        EnchantedItems.load();
        EnchantedBlocks.load();
        EnchantedBlockEntityTypes.load();
        EnchantedEntityTypes.load();
        EnchantedParticleTypes.load();
        EnchantedMenuTypes.load();
        EnchantedRecipeTypes.load();
        EnchantedData.load();
    }

    public static ResourceLocation location(String path) {
        return new ResourceLocation(MOD_ID, path);
    }

}