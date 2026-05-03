package net.favouriteless.enchanted.common.enchanted.poppet.shelf;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

public record PoppetShelfIdentifier(ResourceKey<Level> dimension, BlockPos pos) {

    public static final Codec<PoppetShelfIdentifier> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ResourceKey.codec(Registries.DIMENSION).fieldOf("dimension").forGetter(PoppetShelfIdentifier::dimension),
            BlockPos.CODEC.fieldOf("pos").forGetter(PoppetShelfIdentifier::pos)
    ).apply(instance, PoppetShelfIdentifier::new));

    @Override
    public boolean equals(Object obj) {
        return obj instanceof PoppetShelfIdentifier(ResourceKey<Level> dimension1, BlockPos pos1) &&
                pos1.equals(pos) && dimension1.equals(dimension);
    }

}