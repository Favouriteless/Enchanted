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
        // 2720 power pre-upgrades should be enough for everything.
        addEnchantedCrops(
                output, EBlocks.BELLADONNA.get(), EBlocks.GARLIC.get(), EBlocks.MANDRAKE.get(),
                EBlocks.SNOWBELL.get(), EBlocks.WATER_ARTICHOKE.get(), EBlocks.WOLFSBANE.get()
        );

        addCrops(output, Blocks.BEETROOTS, Blocks.CARROTS, Blocks.POTATOES, Blocks.WHEAT);

        addNatural(
                output, Blocks.BAMBOO, Blocks.BROWN_MUSHROOM, Blocks.BROWN_MUSHROOM_BLOCK, Blocks.CACTUS, Blocks.COCOA,
                Blocks.SHORT_GRASS, Blocks.LILY_PAD, Blocks.MELON, Blocks.PUMPKIN, Blocks.RED_MUSHROOM,
                Blocks.RED_MUSHROOM_BLOCK, Blocks.SUGAR_CANE, Blocks.TALL_GRASS, Blocks.VINE
        );

        // Mutated crops
        output.accept(EBlocks.EMBER_MOSS.get(), 8, 10);
        output.accept(EBlocks.GLINT_WEED.get(), 8, 10);
        output.accept(EBlocks.SPANISH_MOSS.get(), 8, 10);
        output.accept(EBlocks.BLOOD_POPPY.get(), 30, 5);

        // Ground blocks
        output.accept(Blocks.DIRT, 1, 100);
        output.accept(Blocks.FARMLAND, 2, 50);
        output.accept(Blocks.GRASS_BLOCK, 2, 50);
        output.accept(Blocks.MOSS_BLOCK, 2, 40);
        output.accept(Blocks.MYCELIUM, 2, 50);
        output.accept(Blocks.WATER, 1, 50);

        // Special blocks
        output.accept(Blocks.DRAGON_EGG, 200, 1);
        output.accept(Blocks.PITCHER_PLANT, 200, 1);
    }

    @Override
    protected void buildTags(Provider registries, TriConsumer<TagKey<Block>, Integer, Integer> output) {
        output.accept(ETags.Blocks.LEAVES, 3, 150);
        output.accept(ETags.Blocks.LOGS, 4, 50);

        output.accept(BlockTags.LEAVES, 2, 150);
        output.accept(BlockTags.LOGS, 4, 50);

        output.accept(BlockTags.FLOWERS, 4, 40);
        output.accept(BlockTags.SAPLINGS, 4, 20);
    }

    private void addEnchantedCrops(TriConsumer<Block, Integer, Integer> output, Block... blocks) {
        for (Block block : blocks) {
            output.accept(block, 8, 20);
        }
    }

    private void addCrops(TriConsumer<Block, Integer, Integer> output, Block... blocks) {
        for (Block block : blocks) {
            output.accept(block, 6, 20);
        }
    }

    private void addNatural(TriConsumer<Block, Integer, Integer> output, Block... blocks) {
        for (Block block : blocks) {
            output.accept(block, 3, 20);
        }
    }

}
