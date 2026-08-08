package net.favouriteless.enchanted.client.particles.types;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.favouriteless.enchanted.common.util.EExtraCodecs;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.FastColor.ARGB32;
import net.minecraft.world.phys.Vec3;

public record ColouredCircleOptions(ParticleType<ColouredCircleOptions> particleType, int colour, Vec3 center,
                                    float radius) implements ParticleOptions {

    public static MapCodec<ColouredCircleOptions> codec(ParticleType<ColouredCircleOptions> type) {
        return RecordCodecBuilder.mapCodec(instance -> instance.group(
                Codec.INT.fieldOf("colour").forGetter(data -> data.colour),
                EExtraCodecs.VEC3.fieldOf("center").forGetter(data -> data.center),
                Codec.FLOAT.fieldOf("radius").forGetter(data -> data.radius)
        ).apply(instance, (colour, center, radius) -> new ColouredCircleOptions(type, colour, center, radius)));
    }

    public static StreamCodec<? super RegistryFriendlyByteBuf, ColouredCircleOptions> streamCodec(ParticleType<ColouredCircleOptions> type) {
        return StreamCodec.composite(
                ByteBufCodecs.INT, data -> data.colour,
                EExtraCodecs.STREAM_VEC3, data -> data.center,
                ByteBufCodecs.FLOAT, data -> data.radius,
                (colour, center, radius) -> new ColouredCircleOptions(type, colour, center, radius)
        );
    }

    public float getRed() {
        return ARGB32.red(colour) / 255.0F;
    }

    public float getGreen() {
        return ARGB32.green(colour) / 255.0F;
    }

    public float getBlue() {
        return ARGB32.blue(colour) / 255.0F;
    }

    public float getAlpha() {
        return ARGB32.alpha(colour) / 255.0F;
    }


    @Override
    public ParticleType<?> getType() {
        return particleType;
    }

}