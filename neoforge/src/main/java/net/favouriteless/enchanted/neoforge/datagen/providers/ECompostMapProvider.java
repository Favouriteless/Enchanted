package net.favouriteless.enchanted.neoforge.datagen.providers;

import net.favouriteless.enchanted.common.init.EItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.DataMapProvider;
import net.neoforged.neoforge.registries.datamaps.builtin.Compostable;
import net.neoforged.neoforge.registries.datamaps.builtin.NeoForgeDataMaps;

import javax.annotation.Nonnull;
import java.util.concurrent.CompletableFuture;

public class ECompostMapProvider extends DataMapProvider {

    public ECompostMapProvider(PackOutput packOutput, CompletableFuture<Provider> lookupProvider) {
        super(packOutput, lookupProvider);
    }

    @Override
    protected void gather(HolderLookup.Provider provider) {
        builder(NeoForgeDataMaps.COMPOSTABLES)
                .add(EItems.WATER_ARTICHOKE_SEEDS.get().builtInRegistryHolder(), new Compostable(0.3F), false)
                .add(EItems.WATER_ARTICHOKE.get().builtInRegistryHolder(), new Compostable(0.65F), false)
                .add(EItems.SNOWBELL_SEEDS.get().builtInRegistryHolder(), new Compostable(0.3F), false)
                .add(EItems.BELLADONNA_SEEDS.get().builtInRegistryHolder(), new Compostable(0.3F), false)
                .add(EItems.BELLADONNA_FLOWER.get().builtInRegistryHolder(), new Compostable(0.65F), false)
                .add(EItems.MANDRAKE_SEEDS.get().builtInRegistryHolder(), new Compostable(0.3F), false)
                .add(EItems.MANDRAKE_ROOT.get().builtInRegistryHolder(), new Compostable(0.65F), false)
                .add(EItems.WOLFSBANE_SEEDS.get().builtInRegistryHolder(), new Compostable(0.3F), false)
                .add(EItems.WOLFSBANE_FLOWER.get().builtInRegistryHolder(), new Compostable(0.65F), false)
                .add(EItems.GARLIC.get().builtInRegistryHolder(), new Compostable(0.45F), false);
    }

}