package net.favouriteless.enchanted.common.mutandis;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;

import java.util.List;
import java.util.Map;

public record MutagenInfo(Map<Block, MutagenSet> mutagenSets) {

    public static final Codec<MutagenInfo> CODEC = Codec.unboundedMap(BuiltInRegistries.BLOCK.byNameCodec(), MutagenSet.CODEC).xmap(MutagenInfo::new, MutagenInfo::mutagenSets);

    public record MutagenSet(List<Block> mutagens, int weight, boolean extremis) {

        public static final Codec<MutagenSet> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                BuiltInRegistries.BLOCK.byNameCodec().listOf().fieldOf("mutagens").forGetter(MutagenSet::mutagens),
                Codec.INT.fieldOf("weight").forGetter(MutagenSet::weight),
                Codec.BOOL.fieldOf("extremis").forGetter(MutagenSet::extremis)
        ).apply(instance, MutagenSet::new));

    }

}
