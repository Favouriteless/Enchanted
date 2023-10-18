package com.favouriteless.enchanted.client.particles.types;

import com.favouriteless.enchanted.client.particles.types.DoubleParticleType.DoubleParticleData;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

import java.util.Locale;

public class DoubleParticleType extends ParticleType<DoubleParticleData> {
	public static final Codec<DoubleParticleData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			Codec.STRING.fieldOf("particle_type").forGetter(data -> Registry.PARTICLE_TYPE.getKey(data.particleType).toString()),
			Codec.DOUBLE.fieldOf("value").forGetter(data -> data.value)
	).apply(instance, (type, value) -> new DoubleParticleData((ParticleType<DoubleParticleData>)Registry.PARTICLE_TYPE.get(new ResourceLocation(type)),  value)));

	public DoubleParticleType(boolean alwaysShow) {
		super(alwaysShow, DoubleParticleData.DESERIALIZER);
	}

	@Override
	public Codec<DoubleParticleData> codec() {
		return CODEC;
	}

	public static class DoubleParticleData implements ParticleOptions {

		public static final Deserializer<DoubleParticleData> DESERIALIZER = new Deserializer<DoubleParticleData>() {
			public DoubleParticleData fromCommand(ParticleType<DoubleParticleData> particleType, StringReader reader) throws CommandSyntaxException {
				reader.expect(' ');
				double value = reader.readDouble();

				return new DoubleParticleData(particleType, value);
			}

			public DoubleParticleData fromNetwork(ParticleType<DoubleParticleData> particleType, FriendlyByteBuf buffer) {
				return new DoubleParticleData(particleType, buffer.readDouble());
			}
		};

		private final ParticleType<DoubleParticleData> particleType;
		private final double value;

		public DoubleParticleData(ParticleType<DoubleParticleData> particleType, double value) {
			this.particleType = particleType;
			this.value = value;
		}


		@Override
		public String writeToString() {
			return String.format(Locale.ROOT, "%s", Registry.PARTICLE_TYPE.getKey(getType()), value);
		}

		@Override
		public void writeToNetwork(FriendlyByteBuf buffer) {
			buffer.writeDouble(value);
		}

		@Override
		public ParticleType<?> getType() {
			return particleType;
		}

		public double getValue() {
			return value;
		}

	}
}