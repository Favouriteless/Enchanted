package net.favouriteless.enchanted.client.particles.types;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.FastColor;

public class TwoColourOptions implements ParticleOptions {

    private final ParticleType<TwoColourOptions> particleType;
    private final int first;
    private final int second;

    public TwoColourOptions(ParticleType<TwoColourOptions> particleType, int first, int second) {
        this.particleType = particleType;
        this.first = first;
        this.second = first;
    }

    public static MapCodec<TwoColourOptions> codec(ParticleType<TwoColourOptions> type) {
        return RecordCodecBuilder.mapCodec(instance -> instance.group(
                Codec.INT.fieldOf("first").forGetter(data -> data.first),
                Codec.INT.fieldOf("second").forGetter(data -> data.second)
        ).apply(instance, (first, second) -> new TwoColourOptions(type, first, second)));
    }

    public static StreamCodec<? super RegistryFriendlyByteBuf, TwoColourOptions> streamCodec(ParticleType<TwoColourOptions> type) {
        return StreamCodec.composite(
                ByteBufCodecs.INT, data -> data.first,
                ByteBufCodecs.INT, data -> data.second,
                (first, second) -> new TwoColourOptions(type, first, second)
        );
    }

    public float getRedFirst() {
        return FastColor.ARGB32.red(first) / 255.0F;
    }

    public float getGreenFirst() {
        return FastColor.ARGB32.green(first) / 255.0F;
    }

    public float getBlueFirst() {
        return FastColor.ARGB32.blue(first) / 255.0F;
    }

    public float getAlphaFirst() {
        return FastColor.ARGB32.alpha(first) / 255.0F;
    }

    public float getRedSecond() {
        return FastColor.ARGB32.red(second) / 255.0F;
    }

    public float getGreenSecond() {
        return FastColor.ARGB32.green(second) / 255.0F;
    }

    public float getBlueSecond() {
        return FastColor.ARGB32.blue(second) / 255.0F;
    }

    public float getAlphaSecond() {
        return FastColor.ARGB32.alpha(second) / 255.0F;
    }

    @Override
    public ParticleType<?> getType() {
        return particleType;
    }

}