package net.favouriteless.enchanted.common;

import net.favouriteless.enchanted.common.blocks.entity.EBlockEntityTypes;
import net.favouriteless.enchanted.common.curses.ECurses;
import net.favouriteless.enchanted.common.init.*;
import net.favouriteless.enchanted.common.items.component.EDataComponents;
import net.favouriteless.enchanted.common.mutandis.MutagenSavedData;
import net.favouriteless.enchanted.integrations.modopedia.EModopedia;
import net.favouriteless.stateobserver.api.StateObserverManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Enchanted {

    public static final String MOD_ID = "enchanted";
    public static final Logger LOG = LoggerFactory.getLogger(MOD_ID);

    public static final RandomSource RANDOMSOURCE = RandomSource.create();

    public static void init() {
        EPackets.register();
        loadRegistries();

        StateObserverManager.get().registerGlobalListener((level, pos, old, state) -> {
            if(old.getBlock() != state.getBlock() || !state.isRandomlyTicking())
                MutagenSavedData.get(level).remove(pos);
        });
    }

    public static void loadRegistries() {
        ESoundEvents.load();
        EDataComponents.load();
        EItems.load();
        EBlocks.load();
        EBlockEntityTypes.load();
        EEntityTypes.load();
        EMobEffects.load();
        EParticleTypes.load();
        EMenuTypes.load();
        ERecipeTypes.load();
        EData.load();
        ECreativeTab.load();
        ERiteFactories.load();
        EAttachmentTypes.load();

        ECurses.load();

        EModopedia.initCommon();
    }

    public static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }

    public static String savedDataName(String suffix) {
        return MOD_ID + "_" + suffix;
    }

}