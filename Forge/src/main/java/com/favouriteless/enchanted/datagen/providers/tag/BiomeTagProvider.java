package com.favouriteless.enchanted.datagen.providers.tag;

import com.favouriteless.enchanted.Enchanted;
import com.favouriteless.enchanted.common.init.EnchantedTags;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BiomeTagsProvider;
import net.minecraft.world.level.biome.Biomes;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

public class BiomeTagProvider extends BiomeTagsProvider {

    public BiomeTagProvider(DataGenerator generator, @Nullable ExistingFileHelper fileHelper) {
        super(generator, Enchanted.MOD_ID, fileHelper);
    }

    @Override
    protected void addTags() {
        addEnchantedTags();
        addVanillaTags();
    }

    public void addEnchantedTags() {
        tag(EnchantedTags.Biomes.OVERHEATING_BIOMES)
                .add(Biomes.BADLANDS, Biomes.BAMBOO_JUNGLE, Biomes.DESERT, Biomes.ERODED_BADLANDS, Biomes.JUNGLE,
                        Biomes.SAVANNA, Biomes.SAVANNA_PLATEAU, Biomes.SPARSE_JUNGLE, Biomes.WINDSWEPT_SAVANNA,
                        Biomes.WOODED_BADLANDS);
    }

    public void addVanillaTags() {

    }

}
