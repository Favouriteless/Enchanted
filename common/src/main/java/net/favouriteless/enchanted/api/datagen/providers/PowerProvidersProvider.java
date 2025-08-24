package net.favouriteless.enchanted.api.datagen.providers;

import com.google.common.collect.Sets;
import net.favouriteless.enchanted.common.altar.PowerProvider;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.PackOutput.PathProvider;
import net.minecraft.data.PackOutput.Target;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import org.apache.logging.log4j.util.TriConsumer;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public abstract class PowerProvidersProvider implements DataProvider {

    private final String modId;
    private final PathProvider pathProvider;
    private final CompletableFuture<Provider> registries;

    protected PowerProvidersProvider(String modId, PackOutput output, CompletableFuture<Provider> registries) {
        this.modId = modId;
        this.pathProvider = output.createPathProvider(Target.DATA_PACK, "enchanted/altar");
        this.registries = registries;
    }

    protected abstract void buildBlocks(Provider registries, TriConsumer<Block, Integer, Integer> output);

    protected abstract void buildTags(Provider registries, TriConsumer<TagKey<Block>, Integer, Integer> output);

    @Override
    public final CompletableFuture<?> run(CachedOutput output) {
        return registries.thenCompose(registries -> run(output, registries));
    }

    private CompletableFuture<?> run(final CachedOutput output, final Provider registries) {
        final Set<ResourceLocation> set = Sets.newHashSet();
        final List<CompletableFuture<?>> generated = new ArrayList<>();

        buildBlocks(registries, (block, power, limit) -> {
            ResourceLocation id = getId(block);
            if(!set.add(id))
                throw new IllegalStateException("Duplicate " + getName() + ": " + id);
            generated.add(DataProvider.saveStable(output, registries, PowerProvider.CODEC, new PowerProvider(power, limit), pathProvider.json(id)));
        });

        buildTags(registries, (tag, power, limit) -> {
            ResourceLocation id = getId(tag);
            if(!set.add(id))
                throw new IllegalStateException("Duplicate " + getName() + ": " + id);
            generated.add(DataProvider.saveStable(output, registries, PowerProvider.CODEC, new PowerProvider(power, limit), pathProvider.json(id)));
        });

        return CompletableFuture.allOf(generated.toArray(CompletableFuture[]::new));
    }

    private ResourceLocation getId(Block block) {
        ResourceLocation id = BuiltInRegistries.BLOCK.getKey(block);
        return ResourceLocation.fromNamespaceAndPath(id.getNamespace(), "block/" + id.getPath());
    }

    private ResourceLocation getId(TagKey<Block> tag) {
        ResourceLocation id = tag.location();
        return ResourceLocation.fromNamespaceAndPath(id.getNamespace(), "tag/" + id.getPath());
    }

    @Override
    public String getName() {
        return "Enchanted PowerProvider[" + modId + "]";
    }

}
