package com.favouriteless.enchanted.datagen.providers.tag;

import com.favouriteless.enchanted.Enchanted;
import com.favouriteless.enchanted.common.init.EnchantedTags;
import com.favouriteless.enchanted.common.init.EnchantedTags.Blocks;
import com.favouriteless.enchanted.common.init.EnchantedTags.Items;
import com.favouriteless.enchanted.common.init.registry.EnchantedItems;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.ItemTags;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

public class ItemTagProvider extends ItemTagsProvider {

    public ItemTagProvider(DataGenerator generator, BlockTagsProvider blockTagsProvider, @Nullable ExistingFileHelper fileHelper) {
        super(generator, blockTagsProvider, Enchanted.MOD_ID, fileHelper);
    }

    @Override
    protected void addTags() {
        addEnchantedTags();
        addVanillaTags();
    }

    public void addEnchantedTags() {
        // Copied block tags
        copy(Blocks.CHALICES, Items.CHALICES);
        copy(Blocks.CHALKS, Items.CHALKS);
        copy(Blocks.LEAVES, Items.LEAVES);
        copy(Blocks.LOGS, Items.LOGS);
        copy(Blocks.PLANKS, Items.PLANKS);
        copy(Blocks.SAPLINGS, Items.SAPLINGS);
        copy(Blocks.SLABS, Items.SLABS);
        copy(Blocks.STAIRS, Items.STAIRS);
        copy(Blocks.WOODEN_SLABS, Items.WOODEN_SLABS);
        copy(Blocks.WOODEN_STAIRS, Items.WOODEN_STAIRS);

        // Other tags
        tag(Items.SEEDS_DROPS)
                .add(EnchantedItems.ARTICHOKE_SEEDS.get(), EnchantedItems.SNOWBELL_SEEDS.get(),
                        EnchantedItems.MANDRAKE_SEEDS.get(), EnchantedItems.BELLADONNA_SEEDS.get(),
                        EnchantedItems.WOLFSBANE_SEEDS.get(), EnchantedItems.GARLIC.get());
        tag(Items.TOOL_POPPET_BLACKLIST)
                .addTag(EnchantedTags.Items.CHALKS);
    }

    public void addVanillaTags() {
        tag(ItemTags.LEAVES)
                .addTag(EnchantedTags.Items.LEAVES);
        tag(ItemTags.LOGS_THAT_BURN)
                .addTag(EnchantedTags.Items.LOGS);
        tag(ItemTags.PLANKS)
                .addTag(EnchantedTags.Items.PLANKS);
        tag(ItemTags.SAPLINGS)
                .addTag(EnchantedTags.Items.SAPLINGS);
        tag(ItemTags.SLABS)
                .addTag(Items.SLABS);
        tag(ItemTags.STAIRS)
                .addTag(Items.STAIRS);
        tag(ItemTags.SMALL_FLOWERS)
                .add(EnchantedItems.BLOOD_POPPY.get());
        tag(ItemTags.WOODEN_SLABS)
                .addTag(EnchantedTags.Items.WOODEN_SLABS);
        tag(ItemTags.WOODEN_STAIRS)
                .addTag(EnchantedTags.Items.WOODEN_STAIRS);
    }

}
