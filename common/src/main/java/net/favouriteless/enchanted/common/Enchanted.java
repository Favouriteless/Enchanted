package net.favouriteless.enchanted.common;

import net.favouriteless.enchanted.common.blocks.EBlocks;
import net.favouriteless.enchanted.common.blocks.entity.EBlockEntityTypes;
import net.favouriteless.enchanted.common.effects.EEffects;
import net.favouriteless.enchanted.common.entities.EEntityTypes;
import net.favouriteless.enchanted.common.init.ECreativeTab;
import net.favouriteless.enchanted.common.init.EData;
import net.favouriteless.enchanted.common.init.EParticleTypes;
import net.favouriteless.enchanted.common.items.EItems;
import net.favouriteless.enchanted.common.items.component.EDataComponents;
import net.favouriteless.enchanted.common.menus.EMenuTypes;
import net.favouriteless.enchanted.common.network.EPackets;
import net.favouriteless.enchanted.common.recipes.ERecipeTypes;
import net.favouriteless.enchanted.common.circle_magic.ERiteFactories;
import net.favouriteless.enchanted.common.sounds.ESoundEvents;
import net.favouriteless.enchanted.integrations.modopedia.EPageComponents;
import net.favouriteless.enchanted.integrations.modopedia.ETemplateProcessors;
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
        EPackets.register();
        loadRegistries();
    }

    public static void loadRegistries() {
        ESoundEvents.load();
        EDataComponents.load();
        EItems.load();
        EBlocks.load();
        EBlockEntityTypes.load();
        EEntityTypes.load();
        EEffects.load();
        EParticleTypes.load();
        EMenuTypes.load();
        ERecipeTypes.load();
        EData.load();
        ECreativeTab.load();
        ERiteFactories.load();
        EPageComponents.load();
        ETemplateProcessors.load();
    }

    public static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }

    public static String translationKey(String prefix, String suffix) {
        return String.format("%s.%s.%s", prefix, MOD_ID, suffix);
    }

}