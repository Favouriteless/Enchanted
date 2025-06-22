package net.favouriteless.enchanted.common.mutandis;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;

import java.util.List;

public record MutagenInfo(List<MutagenSet> sets) {

    public static final Codec<MutagenInfo> CODEC = MutagenSet.CODEC.listOf().xmap(MutagenInfo::new, MutagenInfo::sets);

    public record MutagenSet(Block result, boolean extremis, int weight, List<Block> mutagens) {

        public static final Codec<MutagenSet> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                BuiltInRegistries.BLOCK.byNameCodec().fieldOf("result").forGetter(MutagenSet::result),
                Codec.BOOL.fieldOf("extremis").forGetter(MutagenSet::extremis),
                Codec.INT.fieldOf("weight").forGetter(MutagenSet::weight),
                BuiltInRegistries.BLOCK.byNameCodec().listOf().fieldOf("mutagens").forGetter(MutagenSet::mutagens)
        ).apply(instance, MutagenSet::new));

        public static MutagenSet of(Block result, boolean extremis, int weight, Block... mutagens) {
            return new MutagenSet(result, extremis, weight, List.of(mutagens));
        }

    }

}
