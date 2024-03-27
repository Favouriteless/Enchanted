package com.favouriteless.enchanted.datagen.providers.tag;

import com.favouriteless.enchanted.Enchanted;
import com.favouriteless.enchanted.common.init.EnchantedTags;
import com.favouriteless.enchanted.common.init.registry.EnchantedBlocks;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

public class BlockTagProvider extends BlockTagsProvider {

    public BlockTagProvider(DataGenerator generator, @Nullable ExistingFileHelper fileHelper) {
        super(generator, Enchanted.MOD_ID, fileHelper);
    }

    @Override
    protected void addTags() {
        addEnchantedTags();
        addVanillaTags();
    }

    public void addEnchantedTags() {
        tag(EnchantedTags.Blocks.BLIGHT_DECAY_BLOCKS)
                .add(Blocks.SAND, Blocks.COARSE_DIRT, Blocks.ROOTED_DIRT);
        tag(EnchantedTags.Blocks.BLIGHT_DECAYABLE_BLOCKS)
                .addTag(BlockTags.DIRT);
        tag(EnchantedTags.Blocks.BLIGHT_DECAYABLE_PLANTS)
                .addTag(BlockTags.SAPLINGS)
                .addTag(BlockTags.SMALL_FLOWERS)
                .add(Blocks.GRASS, Blocks.FERN, Blocks.SWEET_BERRY_BUSH)
                .add(EnchantedBlocks.GLINT_WEED.get());
        tag(EnchantedTags.Blocks.CHALICES)
                .add(EnchantedBlocks.CHALICE.get(), EnchantedBlocks.CHALICE_FILLED.get(),
                        EnchantedBlocks.CHALICE_FILLED_MILK.get());
        tag(EnchantedTags.Blocks.CHALKS)
                .add(EnchantedBlocks.CHALK_GOLD.get(), EnchantedBlocks.CHALK_WHITE.get(),
                        EnchantedBlocks.CHALK_RED.get(), EnchantedBlocks.CHALK_PURPLE.get());
        tag(EnchantedTags.Blocks.CROPS)
                .add(EnchantedBlocks.BELLADONNA.get(), EnchantedBlocks.SNOWBELL.get(), EnchantedBlocks.MANDRAKE.get(),
                        EnchantedBlocks.GARLIC.get(), EnchantedBlocks.WOLFSBANE.get());
        tag(EnchantedTags.Blocks.LEAVES)
                .add(EnchantedBlocks.ALDER_LEAVES.get(), EnchantedBlocks.ROWAN_LEAVES.get(),
                        EnchantedBlocks.HAWTHORN_LEAVES.get());
        tag(EnchantedTags.Blocks.LOGS)
                .add(EnchantedBlocks.ALDER_LOG.get(), EnchantedBlocks.ROWAN_LOG.get(),
                        EnchantedBlocks.HAWTHORN_LOG.get());
        tag(EnchantedTags.Blocks.MUTANDIS_BLACKLIST)
                .add(Blocks.WITHER_ROSE)
                .add(EnchantedBlocks.BLOOD_POPPY.get());
        tag(EnchantedTags.Blocks.MUTANDIS_EXTREMIS_PLANTS)
                .addTag(EnchantedTags.Blocks.MUTANDIS_PLANTS)
                .addTag(EnchantedTags.Blocks.CROPS)
                .addTag(BlockTags.CROPS)
                .add(Blocks.SUGAR_CANE)
                .add(EnchantedBlocks.BLOOD_POPPY.get());
        tag(EnchantedTags.Blocks.MUTANDIS_PLANTS)
                .addTag(BlockTags.SAPLINGS)
                .addTag(BlockTags.SMALL_FLOWERS)
                .add(Blocks.GRASS, Blocks.BROWN_MUSHROOM, Blocks.RED_MUSHROOM)
                .add(EnchantedBlocks.EMBER_MOSS.get(), EnchantedBlocks.GLINT_WEED.get(),
                        EnchantedBlocks.SPANISH_MOSS.get());
        tag(EnchantedTags.Blocks.PLANKS)
                .add(EnchantedBlocks.ROWAN_PLANKS.get(), EnchantedBlocks.ALDER_PLANKS.get(),
                        EnchantedBlocks.HAWTHORN_PLANKS.get());
        tag(EnchantedTags.Blocks.RITE_FOREST_REPLACEABLE)
                .addTag(BlockTags.SMALL_FLOWERS)
                .add(Blocks.FERN)
                .add(EnchantedBlocks.GLINT_WEED.get());
        tag(EnchantedTags.Blocks.SAPLINGS)
                .add(EnchantedBlocks.ROWAN_SAPLING.get(), EnchantedBlocks.ALDER_SAPLING.get(),
                        EnchantedBlocks.HAWTHORN_SAPLING.get());
        tag(EnchantedTags.Blocks.SLABS)
                .addTag(EnchantedTags.Blocks.WOODEN_SLABS);
        tag(EnchantedTags.Blocks.STAIRS)
                .addTag(EnchantedTags.Blocks.WOODEN_SLABS);
        tag(EnchantedTags.Blocks.WOODEN_SLABS)
                .add(EnchantedBlocks.ROWAN_SLAB.get(), EnchantedBlocks.ALDER_SLAB.get(),
                        EnchantedBlocks.HAWTHORN_SLAB.get());
        tag(EnchantedTags.Blocks.WOODEN_STAIRS)
                .add(EnchantedBlocks.ROWAN_STAIRS.get(), EnchantedBlocks.ALDER_STAIRS.get(),
                        EnchantedBlocks.HAWTHORN_STAIRS.get());
    }

    public void addVanillaTags() {
        // Mineable
        tag(BlockTags.MINEABLE_WITH_AXE)
                .add(EnchantedBlocks.SPINNING_WHEEL.get());
        tag(BlockTags.MINEABLE_WITH_HOE)
                .addTag(EnchantedTags.Blocks.LOGS)
                .add(EnchantedBlocks.WICKER_BUNDLE.get());
        tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .addTag(EnchantedTags.Blocks.CHALICES)
                .add(EnchantedBlocks.WITCH_OVEN.get(), EnchantedBlocks.FUME_FUNNEL.get(),
                        EnchantedBlocks.FUME_FUNNEL_FILTERED.get(), EnchantedBlocks.WITCH_CAULDRON.get(),
                        EnchantedBlocks.DISTILLERY.get(), EnchantedBlocks.KETTLE.get(), EnchantedBlocks.ALTAR.get(),
                        EnchantedBlocks.POPPET_SHELF.get());

        // Other tags
        tag(BlockTags.CROPS)
                .addTag(EnchantedTags.Blocks.CROPS);
        tag(BlockTags.LEAVES)
                .addTag(EnchantedTags.Blocks.LEAVES);
        tag(BlockTags.LOGS_THAT_BURN)
                .addTag(EnchantedTags.Blocks.LOGS);
        tag(BlockTags.PLANKS)
                .addTag(EnchantedTags.Blocks.PLANKS);
        tag(BlockTags.SAPLINGS)
                .addTag(EnchantedTags.Blocks.SAPLINGS);
        tag(BlockTags.SLABS)
                .addTag(EnchantedTags.Blocks.SLABS);
        tag(BlockTags.STAIRS)
                .addTag(EnchantedTags.Blocks.STAIRS);
        tag(BlockTags.SMALL_FLOWERS)
                .add(EnchantedBlocks.BLOOD_POPPY.get());
        tag(BlockTags.WOODEN_SLABS)
                .addTag(EnchantedTags.Blocks.WOODEN_SLABS);
        tag(BlockTags.WOODEN_STAIRS)
                .addTag(EnchantedTags.Blocks.WOODEN_STAIRS);

    }

}
