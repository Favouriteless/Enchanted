package com.favouriteless.enchanted.client.particles.types;

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

public class CircleMagicParticleType extends ParticleType<CircleMagicParticleType.CircleMagicData> {
	public static final Codec<CircleMagicData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			Codec.STRING.fieldOf("particle_type").forGetter(data -> Registry.PARTICLE_TYPE.getKey(data.particleType).toString()),
			Codec.INT.fieldOf("red").forGetter(data -> data.red),
			Codec.INT.fieldOf("green").forGetter(data -> data.green),
			Codec.INT.fieldOf("blue").forGetter(data -> data.blue),
			Codec.DOUBLE.fieldOf("centerX").forGetter(data -> data.centerX),
			Codec.DOUBLE.fieldOf("centerY").forGetter(data -> data.centerY),
			Codec.DOUBLE.fieldOf("centerZ").forGetter(data -> data.centerZ),
			Codec.DOUBLE.fieldOf("radius").forGetter(data -> data.radius)
	).apply(instance, (type, red, green, blue, centerX, centerY, centerZ, radius) -> new CircleMagicData((ParticleType<CircleMagicData>)Registry.PARTICLE_TYPE.get(new ResourceLocation(type)), red, green, blue, centerX, centerY, centerZ, radius)));

	public CircleMagicParticleType(boolean alwaysShow) {
		super(alwaysShow, CircleMagicData.DESERIALIZER);
	}

	@Override
	public Codec<CircleMagicData> codec() {
		return CODEC;
	}

	public static class CircleMagicData implements ParticleOptions {

		public static final Deserializer<CircleMagicData> DESERIALIZER = new Deserializer<>() {
            public CircleMagicData fromCommand(ParticleType<CircleMagicData> particleType, StringReader reader) throws CommandSyntaxException {
                reader.expect(' ');
                int red = reader.readInt();
                reader.expect(' ');
                int green = reader.readInt();
                reader.expect(' ');
                int blue = reader.readInt();
                reader.expect(' ');
                double radius = reader.readDouble();
                reader.expect(' ');
                double centerX = reader.readDouble();
                reader.expect(' ');
                double centerY = reader.readDouble();
                reader.expect(' ');
                double centerZ = reader.readDouble();

                return new CircleMagicData(particleType, red, green, blue, centerX, centerY, centerZ, radius);
            }

            public CircleMagicData fromNetwork(ParticleType<CircleMagicData> particleType, FriendlyByteBuf buffer) {
                return new CircleMagicData(particleType, buffer.readInt(), buffer.readInt(), buffer.readInt(), buffer.readDouble(), buffer.readDouble(), buffer.readDouble(), buffer.readDouble());
            }
        };

		private final ParticleType<CircleMagicData> particleType;
		private final int red;
		private final int green;
		private final int blue;
		private final double radius;
		private final double centerX;
		private final double centerY;
		private final double centerZ;

		public CircleMagicData(ParticleType<CircleMagicData> particleType, int red, int green, int blue, double centerX, double centerY, double centerZ, double radius) {
			this.particleType = particleType;
			this.red = red;
			this.green = green;
			this.blue = blue;
			this.radius = radius;
			this.centerX = centerX;
			this.centerY = centerY;
			this.centerZ = centerZ;
		}


		@Override
		public String writeToString() {
			return String.format(Locale.ROOT, "%s %d %d %d", Registry.PARTICLE_TYPE.getKey(getType()), red, green, blue);
		}

		@Override
		public void writeToNetwork(FriendlyByteBuf buffer) {
			buffer.writeInt(red);
			buffer.writeInt(green);
			buffer.writeInt(blue);
			buffer.writeDouble(centerX);
			buffer.writeDouble(centerY);
			buffer.writeDouble(centerZ);
			buffer.writeDouble(radius);
		}

		@Override
		public ParticleType<?> getType() {
			return particleType;
		}

		public int getRed() {
			return red;
		}

		public int getGreen() {
			return green;
		}

		public int getBlue() {
			return blue;
		}

		public double getRadius() {
			return radius;
		}

		public double getCenterX() {
			return centerX;
		}

		public double getCenterY() {
			return centerY;
		}

		public double getCenterZ() {
			return centerZ;
		}

	}
}