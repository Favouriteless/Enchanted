package net.favouriteless.enchanted.neoforge.datagen.providers.enchanted;

import net.favouriteless.enchanted.api.datagen.providers.PowerProvidersProvider;
import net.favouriteless.enchanted.common.Enchanted;
import net.favouriteless.enchanted.common.init.EBlocks;
import net.favouriteless.enchanted.common.init.ETags;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import org.apache.logging.log4j.util.TriConsumer;

import java.util.concurrent.CompletableFuture;

public class EPowerProvidersProvider extends PowerProvidersProvider {

    public EPowerProvidersProvider(PackOutput output, CompletableFuture<Provider> registries) {
        super(Enchanted.MOD_ID, output, registries);
    }

    @Override
    protected void buildBlocks(Provider registries, TriConsumer<Block, Integer, Integer> output) {
        output.accept(EBlocks.BELLADONNA.get(), 4, 20);
        output.accept(EBlocks.BLOOD_POPPY.get(), 2, 10);
        output.accept(EBlocks.EMBER_MOSS.get(), 4, 20);
        output.accept(EBlocks.GLINT_WEED.get(), 2, 20);
        output.accept(EBlocks.MANDRAKE.get(), 4, 20);
        output.accept(EBlocks.SNOWBELL.get(), 4, 20);
        output.accept(EBlocks.SPANISH_MOSS.get(), 3, 20);
        output.accept(EBlocks.WATER_ARTICHOKE.get(), 4, 20);

        output.accept(Blocks.BROWN_MUSHROOM, 3, 20);
        output.accept(Blocks.BROWN_MUSHROOM_BLOCK, 3, 20);
        output.accept(Blocks.CACTUS, 3, 50);
        output.accept(Blocks.CARROTS, 4, 20);
        output.accept(Blocks.COCOA, 3, 20);
        output.accept(Blocks.DIRT, 1, 80);
        output.accept(Blocks.DRAGON_EGG, 250, 1);
        output.accept(Blocks.FARMLAND, 1, 100);
        output.accept(Blocks.SHORT_GRASS, 3, 25);
        output.accept(Blocks.GRASS_BLOCK, 2, 80);
        output.accept(Blocks.MELON, 4, 20);
        output.accept(Blocks.MOSS_BLOCK, 2, 40);
        output.accept(Blocks.MYCELIUM, 1, 50);
        output.accept(Blocks.POTATOES, 4, 20);
        output.accept(Blocks.PUMPKIN, 4, 20);
        output.accept(Blocks.RED_MUSHROOM, 3, 20);
        output.accept(Blocks.RED_MUSHROOM_BLOCK, 3, 20);
        output.accept(Blocks.SUGAR_CANE, 3, 50);
        output.accept(Blocks.TALL_GRASS, 3, 25);
        output.accept(Blocks.VINE, 2, 50);
        output.accept(Blocks.WATER, 1, 50);
        output.accept(Blocks.WHEAT, 4, 20);
    }

    @Override
    protected void buildTags(Provider registries, TriConsumer<TagKey<Block>, Integer, Integer> output) {
        output.accept(ETags.Blocks.LEAVES, 3, 100);
        output.accept(ETags.Blocks.LOGS, 4, 50);

        output.accept(BlockTags.FLOWERS, 4, 30);
        output.accept(BlockTags.LEAVES, 3, 100);
        output.accept(BlockTags.LOGS, 2, 50);
        output.accept(BlockTags.SAPLINGS, 4, 20);
    }

}
