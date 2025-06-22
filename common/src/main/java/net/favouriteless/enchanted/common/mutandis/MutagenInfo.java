package net.favouriteless.enchanted.common.mutandis;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;

import java.util.List;

public record MutagenInfo(List<MutagenSet> sets) {

    public static final Codec<MutagenInfo> CODEC = MutagenSet.CODEC.listOf().xmap(MutagenInfo::new, MutagenInfo::sets);

    public record MutagenSet(Block result, List<Block> mutagens, int weight, boolean extremis) {

        public static final Codec<MutagenSet> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                BuiltInRegistries.BLOCK.byNameCodec().fieldOf("result").forGetter(MutagenSet::result),
                BuiltInRegistries.BLOCK.byNameCodec().listOf().fieldOf("mutagens").forGetter(MutagenSet::mutagens),
                Codec.INT.fieldOf("weight").forGetter(MutagenSet::weight),
                Codec.BOOL.fieldOf("extremis").forGetter(MutagenSet::extremis)
        ).apply(instance, MutagenSet::new));

    }

}
