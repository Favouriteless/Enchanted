package net.favouriteless.enchanted.neoforge.datagen.providers.enchanted;

import net.favouriteless.enchanted.api.datagen.providers.MutagenInfoProvider;
import net.favouriteless.enchanted.common.Enchanted;
import net.favouriteless.enchanted.common.init.EBlocks;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Blocks;

import java.util.concurrent.CompletableFuture;

public class EMutagenInfoProvider extends MutagenInfoProvider {

    public EMutagenInfoProvider(PackOutput output, CompletableFuture<Provider> registries) {
        super(Enchanted.MOD_ID, output, registries);
    }

    @Override
    protected void buildMutagens() {
        add(EBlocks.ROWAN_SAPLING, Blocks.OAK_SAPLING, false, 20, EBlocks.BELLADONNA.get(), EBlocks.MANDRAKE.get(), EBlocks.WOLFSBANE.get(), EBlocks.WATER_ARTICHOKE.get());
        add(EBlocks.HAWTHORN_SAPLING, Blocks.OAK_SAPLING, false, 5, EBlocks.BELLADONNA.get(), EBlocks.MANDRAKE.get(), EBlocks.WOLFSBANE.get(), EBlocks.WATER_ARTICHOKE.get());
        add(EBlocks.ALDER_SAPLING, Blocks.OAK_SAPLING, false, 5, EBlocks.BELLADONNA.get(), EBlocks.MANDRAKE.get(), EBlocks.WOLFSBANE.get(), EBlocks.WATER_ARTICHOKE.get());

        add(EBlocks.ROWAN_SAPLING, Blocks.BIRCH_SAPLING, false, 5, EBlocks.BELLADONNA.get(), EBlocks.MANDRAKE.get(), EBlocks.WOLFSBANE.get(), EBlocks.WATER_ARTICHOKE.get());
        add(EBlocks.HAWTHORN_SAPLING, Blocks.BIRCH_SAPLING, false, 20, EBlocks.BELLADONNA.get(), EBlocks.MANDRAKE.get(), EBlocks.WOLFSBANE.get(), EBlocks.WATER_ARTICHOKE.get());
        add(EBlocks.ALDER_SAPLING, Blocks.BIRCH_SAPLING, false, 5, EBlocks.BELLADONNA.get(), EBlocks.MANDRAKE.get(), EBlocks.WOLFSBANE.get(), EBlocks.WATER_ARTICHOKE.get());

        add(EBlocks.ROWAN_SAPLING, Blocks.ACACIA_SAPLING, false, 5, EBlocks.BELLADONNA.get(), EBlocks.MANDRAKE.get(), EBlocks.WOLFSBANE.get(), EBlocks.WATER_ARTICHOKE.get());
        add(EBlocks.HAWTHORN_SAPLING, Blocks.ACACIA_SAPLING, false, 5, EBlocks.BELLADONNA.get(), EBlocks.MANDRAKE.get(), EBlocks.WOLFSBANE.get(), EBlocks.WATER_ARTICHOKE.get());
        add(EBlocks.ALDER_SAPLING, Blocks.ACACIA_SAPLING, false, 20, EBlocks.BELLADONNA.get(), EBlocks.MANDRAKE.get(), EBlocks.WOLFSBANE.get(), EBlocks.WATER_ARTICHOKE.get());

        add(EBlocks.GLINT_WEED, Blocks.CAVE_VINES, false, 10, EBlocks.ROWAN_LEAVES.get(), EBlocks.ROWAN_LOG.get());
        add(EBlocks.GLINT_WEED, Blocks.CAVE_VINES_PLANT, false, 10, EBlocks.ROWAN_LEAVES.get(), EBlocks.ROWAN_LOG.get());

        add(EBlocks.SPANISH_MOSS, Blocks.VINE, false, 10, Blocks.GLOW_LICHEN, EBlocks.SPANISH_MOSS.get());

        add(EBlocks.EMBER_MOSS, EBlocks.SNOWBELL.get(), false, 10, Blocks.NETHERRACK, Blocks.CRIMSON_NYLIUM, Blocks.WARPED_NYLIUM);

        add(EBlocks.BLOOD_POPPY, Blocks.RED_MUSHROOM, true, 10, Blocks.SCULK_SHRIEKER);
        add(EBlocks.BLOOD_POPPY, Blocks.BROWN_MUSHROOM, true, 10, Blocks.SCULK_SHRIEKER);
    }

}
