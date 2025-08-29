package net.favouriteless.enchanted.client.particles.types;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record DoubleOptions(ParticleType<DoubleOptions> particleType, double value) implements ParticleOptions {

    public static MapCodec<DoubleOptions> codec(ParticleType<DoubleOptions> type) {
        return Codec.DOUBLE.xmap(i -> new DoubleOptions(type, i), o -> o.value).fieldOf("value");

    }

    public static StreamCodec<? super RegistryFriendlyByteBuf, DoubleOptions> streamCodec(ParticleType<DoubleOptions> type) {
        return ByteBufCodecs.DOUBLE.map(i -> new DoubleOptions(type, i), o -> o.value);
    }

    @Override
    public ParticleType<?> getType() {
        return particleType;
    }

}