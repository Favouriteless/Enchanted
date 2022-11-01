/*
 *
 *   Copyright (c) 2022. Favouriteless
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

package com.favouriteless.enchanted.client.particles;

import com.favouriteless.enchanted.client.particles.SkyWrathParticleType.SkyWrathData;
import com.favouriteless.enchanted.common.rites.RiteOfSkyWrath;
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

public class SkyWrathParticleType extends ParticleType<SkyWrathData> {
	public static final Codec<SkyWrathData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			Codec.STRING.fieldOf("particle_type").forGetter(data -> data.particleType.getRegistryName().toString()),
			Codec.INT.fieldOf("red").forGetter(data -> data.red),
			Codec.INT.fieldOf("green").forGetter(data -> data.green),
			Codec.INT.fieldOf("blue").forGetter(data -> data.blue),
			Codec.DOUBLE.fieldOf("centerX").forGetter(data -> data.centerX),
			Codec.DOUBLE.fieldOf("centerY").forGetter(data -> data.centerY),
			Codec.DOUBLE.fieldOf("centerZ").forGetter(data -> data.centerZ),
			Codec.INT.fieldOf("explodeTicks").forGetter(data -> data.explodeTicks)
	).apply(instance, (type, red, green, blue, centerX, centerY, centerZ, explodeTicks) -> new SkyWrathData((ParticleType<SkyWrathData>)ForgeRegistries.PARTICLE_TYPES.getValue(new ResourceLocation(type)), red, green, blue, centerX, centerY, centerZ, explodeTicks)));

	public SkyWrathParticleType(boolean alwaysShow) {
		super(alwaysShow, SkyWrathData.DESERIALIZER);
	}

	@Override
	public Codec<SkyWrathData> codec() {
		return CODEC;
	}

	public static class SkyWrathData implements ParticleOptions {

		public static final Deserializer<SkyWrathData> DESERIALIZER = new Deserializer<>() {
			public SkyWrathData fromCommand(ParticleType<SkyWrathData> particleType, StringReader reader) throws CommandSyntaxException {
				reader.expect(' ');
				int red = reader.readInt();
				reader.expect(' ');
				int green = reader.readInt();
				reader.expect(' ');
				int blue = reader.readInt();
				reader.expect(' ');
				double centerX = reader.readDouble();
				reader.expect(' ');
				double centerY = reader.readDouble();
				reader.expect(' ');
				double centerZ = reader.readDouble();

				return new SkyWrathData(particleType, red, green, blue, centerX, centerY, centerZ, RiteOfSkyWrath.EXPLODE);
			}

			public SkyWrathData fromNetwork(ParticleType<SkyWrathData> particleType, FriendlyByteBuf buffer) {
				return new SkyWrathData(particleType, buffer.readInt(), buffer.readInt(), buffer.readInt(), buffer.readDouble(), buffer.readDouble(), buffer.readDouble(), buffer.readInt());
			}
		};

		private final ParticleType<SkyWrathData> particleType;
		private final int red;
		private final int green;
		private final int blue;
		private final double centerX;
		private final double centerY;
		private final double centerZ;
		private final int explodeTicks;

		public SkyWrathData(ParticleType<SkyWrathData> particleType, int red, int green, int blue, double centerX, double centerY, double centerZ, int explodeTicks) {
			this.particleType = particleType;
			this.red = red;
			this.green = green;
			this.blue = blue;
			this.centerX = centerX;
			this.centerY = centerY;
			this.centerZ = centerZ;
			this.explodeTicks = explodeTicks;
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

		public double getCenterX() {
			return centerX;
		}

		public double getCenterY() {
			return centerY;
		}

		public double getCenterZ() {
			return centerZ;
		}

		public int getExplodeTicks() {
			return explodeTicks;
		}

	}
}