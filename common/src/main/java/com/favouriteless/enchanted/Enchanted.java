package com.favouriteless.enchanted;

import com.favouriteless.enchanted.common.CommonConfig;
import com.favouriteless.enchanted.common.init.registry.*;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import me.shedaniel.autoconfig.serializer.PartitioningSerializer;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

public class Enchanted {

    public static final String MOD_ID = "enchanted";
    public static final Logger LOG = LoggerFactory.getLogger(MOD_ID);
    public static final Random RANDOM = new Random();

    public static void init() {
        AutoConfig.register(EnchantedConfig.class, PartitioningSerializer.wrap(GsonConfigSerializer::new));
        loadRegistries();
    }

    public static void loadRegistries() {
        EnchantedItems.load();
        EnchantedBlocks.load();
        EnchantedEntityTypes.load();
        EnchantedBlockEntityTypes.load();
        EnchantedMenus.load();
    }

    public static ResourceLocation location(String path) {
        return new ResourceLocation(MOD_ID, path);
    }

}