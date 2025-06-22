package net.favouriteless.enchanted.api.datagen.providers;

import net.favouriteless.enchanted.common.mutandis.MutagenInfo;
import net.favouriteless.enchanted.common.mutandis.MutagenInfo.MutagenSet;
import net.favouriteless.enchanted.datagen.providers.SimpleCodecProvider;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.PackOutput.Target;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

public abstract class MutagenInfoProvider extends SimpleCodecProvider<MutagenInfo> {

    private final String modId;
    private final Map<Block, MutagenInfo> mutagens = new HashMap<>();

    protected MutagenInfoProvider(String modId, PackOutput output, CompletableFuture<Provider> registries) {
        super(output.createPathProvider(Target.DATA_PACK, "enchanted/mutagens"), registries, MutagenInfo.CODEC);
        this.modId = modId;
    }

    protected abstract void buildMutagens();

    @Override
    protected final void build(Provider registries, BiConsumer<ResourceLocation, MutagenInfo> output) {
        buildMutagens();
        mutagens.forEach((block, info) -> output.accept(BuiltInRegistries.BLOCK.getKey(block), info));
    }

    protected void add(Block result, Block mutee, boolean extremis, int weight, Block... mutagens) {
        this.mutagens.computeIfAbsent(mutee, k -> new MutagenInfo(new ArrayList<>())).sets().add(
                MutagenSet.of(result, extremis, weight, mutagens)
        );
    }

    protected void add(Block result, Supplier<? extends Block> mutee, boolean extremis, int weight, Block... mutagens) {
        add(result, mutee.get(), extremis, weight, mutagens);
    }

    protected void add(Supplier<? extends Block> result, Supplier<? extends Block> mutee, boolean extremis, int weight, Block... mutagens) {
        add(result.get(), mutee.get(), extremis, weight, mutagens);
    }

    protected void add(Supplier<? extends Block> result, Block mutee, boolean extremis, int weight, Block... mutagens) {
        add(result.get(), mutee, extremis, weight, mutagens);
    }

    @Override
    public String getName() {
        return "Enchanted MutagenInfo[" + modId + "]";
    }

}
