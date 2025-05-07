package net.favouriteless.enchanted.neoforge.datagen.providers.tag;

import net.favouriteless.enchanted.common.Enchanted;
import net.favouriteless.enchanted.common.init.ETags;
import net.favouriteless.enchanted.common.init.ETags.Blocks;
import net.favouriteless.enchanted.common.items.EItems;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

public class EItemTagProvider extends ItemTagsProvider {

    public EItemTagProvider(PackOutput output, CompletableFuture<Provider> lookupProvider, ExistingFileHelper existingFileHelper, CompletableFuture<TagLookup<Block>> blockTags) {
        super(output, lookupProvider, blockTags, Enchanted.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(Provider provider) {
        addEnchantedTags(provider);
        addVanillaTags(provider);
    }

    @SuppressWarnings("unchecked")
    public void addEnchantedTags(Provider provider) {
        // Copied block tags
        copy(Blocks.ALDER_LOGS, ETags.Items.ALDER_LOGS);
        copy(Blocks.CHALICES, ETags.Items.CHALICES);
        copy(Blocks.CHALKS, ETags.Items.CHALKS);
        copy(Blocks.FENCE_GATES, ETags.Items.FENCE_GATES);
        copy(Blocks.HAWTHORN_LOGS, ETags.Items.HAWTHORN_LOGS);
        copy(Blocks.LEAVES, ETags.Items.LEAVES);
        copy(Blocks.LOGS, ETags.Items.LOGS);
        copy(Blocks.PLANKS, ETags.Items.PLANKS);
        copy(Blocks.ROWAN_LOGS, ETags.Items.ROWAN_LOGS);
        copy(Blocks.SAPLINGS, ETags.Items.SAPLINGS);
        copy(Blocks.SLABS, ETags.Items.SLABS);
        copy(Blocks.STAIRS, ETags.Items.STAIRS);
        copy(Blocks.WOODEN_BUTTONS, ETags.Items.WOODEN_BUTTONS);
        copy(Blocks.WOODEN_FENCES, ETags.Items.WOODEN_FENCES);
        copy(Blocks.WOODEN_PRESSURE_PLATES, ETags.Items.WOODEN_PRESSURE_PLATES);
        copy(Blocks.WOODEN_SLABS, ETags.Items.WOODEN_SLABS);
        copy(Blocks.WOODEN_STAIRS, ETags.Items.WOODEN_STAIRS);

        // Other tags
        tag(ETags.Items.ARMORS)
                .add(EItems.EARMUFFS.get(), Items.LEATHER_BOOTS, Items.LEATHER_LEGGINGS, Items.LEATHER_CHESTPLATE,
                        Items.LEATHER_HELMET, Items.GOLDEN_BOOTS, Items.GOLDEN_LEGGINGS, Items.GOLDEN_CHESTPLATE,
                        Items.GOLDEN_HELMET, Items.CHAINMAIL_BOOTS, Items.CHAINMAIL_LEGGINGS, Items.CHAINMAIL_CHESTPLATE,
                        Items.CHAINMAIL_HELMET, Items.IRON_BOOTS, Items.IRON_LEGGINGS, Items.IRON_CHESTPLATE,
                        Items.IRON_HELMET, Items.DIAMOND_BOOTS, Items.DIAMOND_LEGGINGS, Items.DIAMOND_CHESTPLATE,
                        Items.DIAMOND_HELMET, Items.NETHERITE_BOOTS, Items.NETHERITE_LEGGINGS, Items.NETHERITE_CHESTPLATE,
                        Items.NETHERITE_HELMET)
                .addOptionalTag(ResourceLocation.fromNamespaceAndPath("c", "armors"));
        tag(ETags.Items.RAW_FOODS)
                .add(Items.BEEF, Items.KELP, Items.POTATO, Items.CHORUS_FRUIT, Items.CHICKEN, Items.COD, Items.MUTTON,
                        Items.PORKCHOP, Items.RABBIT, Items.SALMON);
        tag(ETags.Items.SWORDS)
                .add(EItems.ARTHANA.get());
        tag(ETags.Items.TOOLS)
                .addTag(ETags.Items.SWORDS);
        tag(ETags.Items.TOOL_POPPET_BLACKLIST)
                .addTag(ETags.Items.CHALKS);
        tag(ETags.Items.TOOL_POPPET_WHITELIST)
                .addTags(ItemTags.SWORDS, ItemTags.PICKAXES, ItemTags.SHOVELS, ItemTags.AXES, ItemTags.HOES)
                .addOptionalTag(Tags.Items.TOOLS)
                .addOptionalTag(ResourceLocation.fromNamespaceAndPath("c", "tools"))
                .addOptionalTag(ResourceLocation.fromNamespaceAndPath("c", "hoes"))
                .addOptionalTag(ResourceLocation.fromNamespaceAndPath("c", "shears"))
                .addOptionalTag(ResourceLocation.fromNamespaceAndPath("c", "shields"))
                .addOptionalTag(ResourceLocation.fromNamespaceAndPath("c", "spears"));
        tag(ETags.Items.WITCH_OVEN_BLACKLIST)
                .addOptionalTag(ResourceLocation.fromNamespaceAndPath("c", "ores"))
                .addOptionalTag(ResourceLocation.fromNamespaceAndPath("c", "raw_materials"));
    }

    public void addVanillaTags(Provider provider) {
        tag(ItemTags.FENCE_GATES)
                .addTag(ETags.Items.FENCE_GATES);
        tag(ItemTags.LEAVES)
                .addTag(ETags.Items.LEAVES);
        tag(ItemTags.LOGS)
                .addTag(ETags.Items.LOGS);
        tag(ItemTags.LOGS_THAT_BURN)
                .addTag(ETags.Items.LOGS);
        tag(ItemTags.PLANKS)
                .addTag(ETags.Items.PLANKS);
        tag(ItemTags.SAPLINGS)
                .addTag(ETags.Items.SAPLINGS);
        tag(ItemTags.SLABS)
                .addTag(ETags.Items.SLABS);
        tag(ItemTags.SMALL_FLOWERS)
                .add(EItems.BLOOD_POPPY.get());
        tag(ItemTags.STAIRS)
                .addTag(ETags.Items.STAIRS);
        tag(ItemTags.SWORDS)
                .add(EItems.ARTHANA.get());
        tag(ItemTags.WOODEN_BUTTONS)
                .addTag(ETags.Items.WOODEN_BUTTONS);
        tag(ItemTags.WOODEN_FENCES)
                .addTag(ETags.Items.WOODEN_FENCES);
        tag(ItemTags.WOODEN_PRESSURE_PLATES)
                .addTag(ETags.Items.WOODEN_PRESSURE_PLATES);
        tag(ItemTags.WOODEN_SLABS)
                .addTag(ETags.Items.WOODEN_SLABS);
        tag(ItemTags.WOODEN_STAIRS)
                .addTag(ETags.Items.WOODEN_STAIRS);
    }

}
