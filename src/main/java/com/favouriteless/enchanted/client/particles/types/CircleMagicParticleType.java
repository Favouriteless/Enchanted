/*
 *
 *   Copyright (c) 2023. Favouriteless
 *   Enchanted, a minecraft mod.
 *   GNU GPLv3 License
 *
 *       This file is part of Enchanted.
 *
 *       Enchanted is free software: you can redistribute it and/or modify
 *       it under the terms of the GNU General Public License as published by
 *       the Free Software Foundation, either version 3 of the License, or
 *       (at your option) any later version.
 *
 *       Enchanted is distributed in the hope that it will be useful,
 *       but WITHOUT ANY WARRANTY; without even the implied warranty of
 *       MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *       GNU General Public License for more details.
 *
 *       You should have received a copy of the GNU General Public License
 *       along with Enchanted.  If not, see <https://www.gnu.org/licenses/>.
 *
 *
 */

package com.favouriteless.enchanted.client.particles.types;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Locale;

public class CircleMagicParticleType extends ParticleType<CircleMagicParticleType.CircleMagicData> {
	public static final Codec<CircleMagicData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			Codec.STRING.fieldOf("particle_type").forGetter(data -> data.particleType.getRegistryName().toString()),
			Codec.INT.fieldOf("red").forGetter(data -> data.red),
			Codec.INT.fieldOf("green").forGetter(data -> data.green),
			Codec.INT.fieldOf("blue").forGetter(data -> data.blue),
			Codec.DOUBLE.fieldOf("centerX").forGetter(data -> data.centerX),
			Codec.DOUBLE.fieldOf("centerY").forGetter(data -> data.centerY),
			Codec.DOUBLE.fieldOf("centerZ").forGetter(data -> data.centerZ),
			Codec.DOUBLE.fieldOf("radius").forGetter(data -> data.radius)
	).apply(instance, (type, red, green, blue, centerX, centerY, centerZ, radius) -> new CircleMagicData((ParticleType<CircleMagicData>)ForgeRegistries.PARTICLE_TYPES.getValue(new ResourceLocation(type)), red, green, blue, centerX, centerY, centerZ, radius)));

	public CircleMagicParticleType(boolean alwaysShow) {
		super(alwaysShow, CircleMagicData.DESERIALIZER);
	}

	@Override
	public Codec<CircleMagicData> codec() {
		return CODEC;
	}

	public static class CircleMagicData implements ParticleOptions {

		public static final ParticleOptions.Deserializer<CircleMagicData> DESERIALIZER = new ParticleOptions.Deserializer<CircleMagicData>() {
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
			return String.format(Locale.ROOT, "%s %d %d %d", ForgeRegistries.PARTICLE_TYPES.getKey(getType()), red, green, blue);
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