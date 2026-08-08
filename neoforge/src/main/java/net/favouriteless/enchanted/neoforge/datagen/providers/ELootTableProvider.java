package net.favouriteless.enchanted.neoforge.datagen.providers;

import net.favouriteless.enchanted.neoforge.datagen.providers.loot_tables.EBlockLoot;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class ELootTableProvider {

    public static LootTableProvider create(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        return new LootTableProvider(
                output, Set.of(), List.of(
                new LootTableProvider.SubProviderEntry(EBlockLoot::new, LootContextParamSets.BLOCK)
        ),
                registries
        );
    }

}
