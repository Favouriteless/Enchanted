package net.favouriteless.enchanted.neoforge.datagen.providers.tag;

import net.favouriteless.enchanted.common.Enchanted;
import net.favouriteless.enchanted.common.init.ETags;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.BiomeTagsProvider;
import net.minecraft.world.level.biome.Biomes;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

public class EBiomeTagProvider extends BiomeTagsProvider {

    public EBiomeTagProvider(PackOutput output, CompletableFuture<Provider> provider, ExistingFileHelper existingFileHelper) {
        super(output, provider, Enchanted.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(Provider provider) {
        addEnchantedTags(provider);
    }

    public void addEnchantedTags(Provider provider) {
        tag(ETags.Biomes.OVERHEATING_BIOMES)
                .add(
                        Biomes.BADLANDS, Biomes.BAMBOO_JUNGLE, Biomes.DESERT, Biomes.ERODED_BADLANDS, Biomes.JUNGLE,
                        Biomes.SAVANNA, Biomes.SAVANNA_PLATEAU, Biomes.SPARSE_JUNGLE, Biomes.WINDSWEPT_SAVANNA,
                        Biomes.WOODED_BADLANDS
                );
    }

}
