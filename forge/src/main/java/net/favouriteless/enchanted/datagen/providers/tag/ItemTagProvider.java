package net.favouriteless.enchanted.datagen.providers.tag;

import net.favouriteless.enchanted.common.Enchanted;
import net.favouriteless.enchanted.common.init.EnchantedTags;
import net.favouriteless.enchanted.common.init.EnchantedTags.Blocks;
import net.favouriteless.enchanted.common.init.registry.EItems;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

public class ItemTagProvider extends ItemTagsProvider {

    public ItemTagProvider(PackOutput output, CompletableFuture<Provider> lookupProvider, ExistingFileHelper existingFileHelper, CompletableFuture<TagLookup<Block>> blockTags) {
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
        copy(Blocks.ALDER_LOGS, EnchantedTags.Items.ALDER_LOGS);
        copy(Blocks.CHALICES, EnchantedTags.Items.CHALICES);
        copy(Blocks.CHALKS, EnchantedTags.Items.CHALKS);
        copy(Blocks.HAWTHORN_LOGS, EnchantedTags.Items.HAWTHORN_LOGS);
        copy(Blocks.LEAVES, EnchantedTags.Items.LEAVES);
        copy(Blocks.LOGS, EnchantedTags.Items.LOGS);
        copy(Blocks.PLANKS, EnchantedTags.Items.PLANKS);
        copy(Blocks.ROWAN_LOGS, EnchantedTags.Items.ROWAN_LOGS);
        copy(Blocks.SAPLINGS, EnchantedTags.Items.SAPLINGS);
        copy(Blocks.SLABS, EnchantedTags.Items.SLABS);
        copy(Blocks.STAIRS, EnchantedTags.Items.STAIRS);
        copy(Blocks.WOODEN_SLABS, EnchantedTags.Items.WOODEN_SLABS);
        copy(Blocks.WOODEN_STAIRS, EnchantedTags.Items.WOODEN_STAIRS);

        // Other tags
        tag(EnchantedTags.Items.ARMORS)
                .add(EItems.EARMUFFS.get(),
                        Items.LEATHER_BOOTS, Items.LEATHER_LEGGINGS, Items.LEATHER_CHESTPLATE, Items.LEATHER_HELMET,
                        Items.GOLDEN_BOOTS, Items.GOLDEN_LEGGINGS, Items.GOLDEN_CHESTPLATE, Items.GOLDEN_HELMET,
                        Items.CHAINMAIL_BOOTS, Items.CHAINMAIL_LEGGINGS, Items.CHAINMAIL_CHESTPLATE, Items.CHAINMAIL_HELMET,
                        Items.IRON_BOOTS, Items.IRON_LEGGINGS, Items.IRON_CHESTPLATE, Items.IRON_HELMET,
                        Items.DIAMOND_BOOTS, Items.DIAMOND_LEGGINGS, Items.DIAMOND_CHESTPLATE, Items.DIAMOND_HELMET,
                        Items.NETHERITE_BOOTS, Items.NETHERITE_LEGGINGS, Items.NETHERITE_CHESTPLATE, Items.NETHERITE_HELMET);
        tag(EnchantedTags.Items.ARMOR_POPPET_WHITELIST)
                .addTag(EnchantedTags.Items.ARMORS)
                .addOptionalTag(new ResourceLocation("forge", "armors"));
        tag(EnchantedTags.Items.RAW_FOODS)
                .add(Items.BEEF, Items.KELP, Items.POTATO, Items.CHORUS_FRUIT, Items.CHICKEN, Items.COD, Items.MUTTON,
                        Items.PORKCHOP, Items.RABBIT, Items.SALMON);
        tag(EnchantedTags.Items.SWORDS)
                .add(EItems.ARTHANA.get());
        tag(EnchantedTags.Items.TOOLS)
                .addTag(EnchantedTags.Items.SWORDS);
        tag(EnchantedTags.Items.TOOL_POPPET_BLACKLIST)
                .addTag(EnchantedTags.Items.CHALKS);
        tag(EnchantedTags.Items.TOOL_POPPET_WHITELIST)
                .addTags(ItemTags.SWORDS, ItemTags.PICKAXES, ItemTags.SHOVELS, ItemTags.AXES, ItemTags.HOES)
                .addOptionalTag(new ResourceLocation("forge", "tools"))
                .addOptionalTag(new ResourceLocation("c", "hoes"))
                .addOptionalTag(new ResourceLocation("c", "shears"))
                .addOptionalTag(new ResourceLocation("c", "shields"))
                .addOptionalTag(new ResourceLocation("c", "spears"));
        tag(EnchantedTags.Items.WITCH_OVEN_BLACKLIST)
                .addOptionalTag(new ResourceLocation("forge", "ores"))
                .addOptionalTag(new ResourceLocation("c", "ores"));
    }

    public void addVanillaTags(Provider provider) {
        tag(ItemTags.LEAVES)
                .addTag(EnchantedTags.Items.LEAVES);
        tag(ItemTags.LOGS_THAT_BURN)
                .addTag(EnchantedTags.Items.LOGS);
        tag(ItemTags.PLANKS)
                .addTag(EnchantedTags.Items.PLANKS);
        tag(ItemTags.SAPLINGS)
                .addTag(EnchantedTags.Items.SAPLINGS);
        tag(ItemTags.SLABS)
                .addTag(EnchantedTags.Items.SLABS);
        tag(ItemTags.STAIRS)
                .addTag(EnchantedTags.Items.STAIRS);
        tag(ItemTags.SMALL_FLOWERS)
                .add(EItems.BLOOD_POPPY.get());
        tag(ItemTags.WOODEN_SLABS)
                .addTag(EnchantedTags.Items.WOODEN_SLABS);
        tag(ItemTags.WOODEN_STAIRS)
                .addTag(EnchantedTags.Items.WOODEN_STAIRS);
    }

}
