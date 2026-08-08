package net.favouriteless.enchanted.datagen.providers;

import com.google.common.collect.Sets;
import com.mojang.serialization.Codec;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput.PathProvider;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

public abstract class SimpleCodecProvider<T> implements DataProvider {

    private final Codec<T> codec;
    private final PathProvider pathProvider;
    private final CompletableFuture<Provider> registries;

    protected SimpleCodecProvider(PathProvider pathProvider, CompletableFuture<Provider> registries, Codec<T> codec) {
        this.pathProvider = pathProvider;
        this.registries = registries;
        this.codec = codec;
    }

    protected abstract void build(Provider registries, BiConsumer<ResourceLocation, T> output);

    @Override
    public final CompletableFuture<?> run(CachedOutput output) {
        return registries.thenCompose(registries -> run(output, registries));
    }

    private CompletableFuture<?> run(final CachedOutput output, final HolderLookup.Provider registries) {
        final Set<ResourceLocation> set = Sets.newHashSet();
        final List<CompletableFuture<?>> generated = new ArrayList<>();

        build(
                registries, (id, t) -> {
                    if (!set.add(id)) {
                        throw new IllegalStateException("Duplicate " + getName() + ": " + id);
                    }
                    generated.add(DataProvider.saveStable(output, registries, codec, t, pathProvider.json(id)));
                }
        );
        return CompletableFuture.allOf(generated.toArray(CompletableFuture[]::new));
    }

}
