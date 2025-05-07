package net.favouriteless.enchanted.datagen.providers.tag;

import net.favouriteless.enchanted.common.Enchanted;
import net.favouriteless.enchanted.common.blocks.access.EnchantedButtonBlock;
import net.favouriteless.enchanted.common.init.EnchantedTags;
import net.favouriteless.enchanted.common.init.registry.EBlocks;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.IntrinsicHolderTagsProvider;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class BlockTagProvider extends IntrinsicHolderTagsProvider<Block> {

    public BlockTagProvider(PackOutput output, CompletableFuture<Provider> lookupProvider, ExistingFileHelper existingFileHelper) {
        super(output, Registries.BLOCK, lookupProvider, block -> block.builtInRegistryHolder().key(), Enchanted.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(@NotNull Provider provider) {
        addEnchantedTags(provider);
        addVanillaTags(provider);
    }

    public void addEnchantedTags(Provider provider) {
        tag(EnchantedTags.Blocks.ALDER_LOGS)
                .add(EBlocks.ALDER_LOG.get(), EBlocks.STRIPPED_ALDER_LOG.get());
        tag(EnchantedTags.Blocks.BLIGHT_DECAY_BLOCKS)
                .add(Blocks.SAND, Blocks.COARSE_DIRT, Blocks.ROOTED_DIRT);
        tag(EnchantedTags.Blocks.BLIGHT_DECAYABLE_BLOCKS)
                .addTag(BlockTags.DIRT);
        tag(EnchantedTags.Blocks.BLIGHT_DECAYABLE_PLANTS)
                .addTag(BlockTags.SAPLINGS)
                .addTag(BlockTags.SMALL_FLOWERS)
                .add(Blocks.GRASS, Blocks.FERN, Blocks.SWEET_BERRY_BUSH)
                .add(EBlocks.GLINT_WEED.get());
        tag(EnchantedTags.Blocks.CHALICES)
                .add(EBlocks.CHALICE.get(), EBlocks.CHALICE_FILLED.get());
        tag(EnchantedTags.Blocks.CHALKS)
                .add(EBlocks.GOLDEN_CHALK.get(), EBlocks.RITUAL_CHALK.get(),
                        EBlocks.NETHER_CHALK.get(), EBlocks.OTHERWHERE_CHALK.get());
        tag(EnchantedTags.Blocks.CROPS)
                .add(EBlocks.BELLADONNA.get(), EBlocks.SNOWBELL.get(), EBlocks.MANDRAKE.get(),
                        EBlocks.GARLIC.get(), EBlocks.WOLFSBANE.get());
        tag(EnchantedTags.Blocks.FENCES)
                .addTag(EnchantedTags.Blocks.WOODEN_FENCES);
        tag(EnchantedTags.Blocks.FENCE_GATES)
                .add(EBlocks.ALDER_FENCE_GATE.get(), EBlocks.HAWTHORN_FENCE_GATE.get(),
                        EBlocks.ROWAN_FENCE_GATE.get());
        tag(EnchantedTags.Blocks.HAWTHORN_LOGS)
                .add(EBlocks.HAWTHORN_LOG.get(), EBlocks.STRIPPED_HAWTHORN_LOG.get());
        tag(EnchantedTags.Blocks.LEAVES)
                .add(EBlocks.ALDER_LEAVES.get(), EBlocks.ROWAN_LEAVES.get(),
                        EBlocks.HAWTHORN_LEAVES.get());
        tag(EnchantedTags.Blocks.LOGS)
                .addTags(EnchantedTags.Blocks.ALDER_LOGS, EnchantedTags.Blocks.HAWTHORN_LOGS,
                        EnchantedTags.Blocks.ROWAN_LOGS);
        tag(EnchantedTags.Blocks.MUTANDIS_BLACKLIST)
                .add(Blocks.WITHER_ROSE)
                .add(EBlocks.BLOOD_POPPY.get());
        tag(EnchantedTags.Blocks.MUTANDIS_EXTREMIS_PLANTS)
                .addTag(EnchantedTags.Blocks.MUTANDIS_PLANTS)
                .addTag(EnchantedTags.Blocks.CROPS)
                .addTag(BlockTags.CROPS)
                .add(Blocks.SUGAR_CANE, Blocks.CACTUS)
                .add(EBlocks.BLOOD_POPPY.get());
        tag(EnchantedTags.Blocks.MUTANDIS_PLANTS)
                .addTag(BlockTags.SAPLINGS)
                .addTag(BlockTags.SMALL_FLOWERS)
                .add(Blocks.GRASS, Blocks.BROWN_MUSHROOM, Blocks.RED_MUSHROOM)
                .add(EBlocks.EMBER_MOSS.get(), EBlocks.GLINT_WEED.get(),
                        EBlocks.SPANISH_MOSS.get());
        tag(EnchantedTags.Blocks.PLANKS)
                .add(EBlocks.ROWAN_PLANKS.get(), EBlocks.ALDER_PLANKS.get(),
                        EBlocks.HAWTHORN_PLANKS.get());
        tag(EnchantedTags.Blocks.RITE_FOREST_REPLACEABLE)
                .addTag(BlockTags.SMALL_FLOWERS)
                .add(Blocks.FERN)
                .add(EBlocks.GLINT_WEED.get());
        tag(EnchantedTags.Blocks.ROWAN_LOGS)
                .add(EBlocks.ROWAN_LOG.get(), EBlocks.STRIPPED_ROWAN_LOG.get());
        tag(EnchantedTags.Blocks.SAPLINGS)
                .add(EBlocks.ROWAN_SAPLING.get(), EBlocks.ALDER_SAPLING.get(),
                        EBlocks.HAWTHORN_SAPLING.get());
        tag(EnchantedTags.Blocks.SLABS)
                .addTag(EnchantedTags.Blocks.WOODEN_SLABS);
        tag(EnchantedTags.Blocks.STAIRS)
                .addTag(EnchantedTags.Blocks.WOODEN_SLABS);
        tag(EnchantedTags.Blocks.WOODEN_SLABS)
                .add(EBlocks.ROWAN_SLAB.get(), EBlocks.ALDER_SLAB.get(),
                        EBlocks.HAWTHORN_SLAB.get());
        tag(EnchantedTags.Blocks.WOODEN_FENCES)
                .add(EBlocks.ROWAN_FENCE.get(), EBlocks.ALDER_FENCE.get(),
                        EBlocks.HAWTHORN_FENCE.get());
        tag(EnchantedTags.Blocks.WOODEN_STAIRS)
                .add(EBlocks.ROWAN_STAIRS.get(), EBlocks.ALDER_STAIRS.get(),
                        EBlocks.HAWTHORN_STAIRS.get());
    }

    public void addVanillaTags(Provider provider) {
        // Mineable
        tag(BlockTags.MINEABLE_WITH_AXE)
                .add(EBlocks.SPINNING_WHEEL.get());
        tag(BlockTags.MINEABLE_WITH_HOE)
                .addTag(EnchantedTags.Blocks.LOGS)
                .add(EBlocks.WICKER_BUNDLE.get());
        tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .addTag(EnchantedTags.Blocks.CHALICES)
                .add(EBlocks.WITCH_OVEN.get(), EBlocks.FUME_FUNNEL.get(),
                        EBlocks.FUME_FUNNEL_FILTERED.get(), EBlocks.WITCH_CAULDRON.get(),
                        EBlocks.DISTILLERY.get(), EBlocks.KETTLE.get(), EBlocks.ALTAR.get(),
                        EBlocks.POPPET_SHELF.get());

        // Other tags
        tag(BlockTags.CROPS)
                .addTag(EnchantedTags.Blocks.CROPS);
        tag(BlockTags.FENCES)
                .addTag(EnchantedTags.Blocks.FENCES);
        tag(BlockTags.FENCE_GATES)
                .addTag(EnchantedTags.Blocks.FENCE_GATES);
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
                .add(EBlocks.BLOOD_POPPY.get());
        tag(BlockTags.WOODEN_FENCES)
                .addTag(EnchantedTags.Blocks.WOODEN_FENCES);
        tag(BlockTags.WOODEN_SLABS)
                .addTag(EnchantedTags.Blocks.WOODEN_SLABS);
        tag(BlockTags.WOODEN_STAIRS)
                .addTag(EnchantedTags.Blocks.WOODEN_STAIRS);

    }
}
