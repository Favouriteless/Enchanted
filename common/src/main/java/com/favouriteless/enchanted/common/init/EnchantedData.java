package com.favouriteless.enchanted.common.init;

import com.favouriteless.enchanted.common.init.registry.AltarUpgradeRegistry;
import com.favouriteless.enchanted.common.init.registry.ArthanaLootRegistry;
import com.favouriteless.enchanted.common.init.registry.PowerProviderRegistry;
import com.favouriteless.enchanted.common.loot.ArthanaLootReloadListener;
import com.favouriteless.enchanted.common.reloadlisteners.altar.AltarUpgradeReloadListener;
import com.favouriteless.enchanted.common.reloadlisteners.altar.PowerProviderReloadListener;
import com.favouriteless.enchanted.platform.RegistryHandler;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

public class EnchantedData {

    public static final PowerProviderRegistry<Block> POWER_BLOCKS = new PowerProviderRegistry<>();
    public static final PowerProviderRegistry<TagKey<Block>> POWER_TAGS = new PowerProviderRegistry<>();
    public static final AltarUpgradeRegistry ALTAR_UPGRADES = new AltarUpgradeRegistry();
    public static final ArthanaLootRegistry ARTHANA_LOOT = new ArthanaLootRegistry();



    static {
        RegistryHandler.register("altar_blocks", new PowerProviderReloadListener<>("altar/blocks", EnchantedData::createBlockKey, EnchantedData.POWER_BLOCKS));
        RegistryHandler.register("altar_tags", new PowerProviderReloadListener<>("altar/tags", EnchantedData::createBlockTagKey, EnchantedData.POWER_TAGS));
        RegistryHandler.register("altar_upgrade", new AltarUpgradeReloadListener());
        RegistryHandler.register("arthana_loot", new ArthanaLootReloadListener());
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
