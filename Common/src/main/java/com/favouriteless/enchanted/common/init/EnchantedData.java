package com.favouriteless.enchanted.common.init;

import com.favouriteless.enchanted.Enchanted;
import com.favouriteless.enchanted.common.init.registry.AltarUpgradeRegistry;
import com.favouriteless.enchanted.common.init.registry.PowerProviderRegistry;
import com.favouriteless.enchanted.common.reloadlisteners.altar.AltarUpgradeReloadListener;
import com.favouriteless.enchanted.common.reloadlisteners.altar.PowerProviderReloadListener;
import com.favouriteless.enchanted.platform.Services;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

public class EnchantedData {

    public static final PowerProviderRegistry<Block> POWER_BLOCKS = new PowerProviderRegistry<>();
    public static final PowerProviderRegistry<TagKey<Block>> POWER_TAGS = new PowerProviderRegistry<>();
    public static final AltarUpgradeRegistry ALTAR_UPGRADES = new AltarUpgradeRegistry();



    static {
        register("altar_blocks", new PowerProviderReloadListener<>("altar/blocks", EnchantedData::createBlockKey, EnchantedData.POWER_BLOCKS));
        register("altar_tags", new PowerProviderReloadListener<>("altar/tags", EnchantedData::createBlockTagKey, EnchantedData.POWER_TAGS));
        register("altar_upgrade", new AltarUpgradeReloadListener());
    }

    public static void register(String id, SimpleJsonResourceReloadListener reloadListener) {
        Services.COMMON_REGISTRY.register(Enchanted.location(id), reloadListener);
    }

    private static Block createBlockKey(ResourceLocation key) {
        Block block = Registry.BLOCK.get(key);
        if(block != Blocks.AIR)
            return block;
        else
            return null;
    }

    private static TagKey<Block> createBlockTagKey(ResourceLocation key) {
        return TagKey.create(Registry.BLOCK_REGISTRY, key);
    }

    public static void load() {} // Method which exists purely to load the class.

}
