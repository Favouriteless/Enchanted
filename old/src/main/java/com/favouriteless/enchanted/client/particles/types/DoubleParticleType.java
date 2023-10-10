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

import com.favouriteless.enchanted.client.particles.types.DoubleParticleType.DoubleParticleData;
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

public class DoubleParticleType extends ParticleType<DoubleParticleData> {
	public static final Codec<DoubleParticleData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			Codec.STRING.fieldOf("particle_type").forGetter(data -> data.particleType.getRegistryName().toString()),
			Codec.DOUBLE.fieldOf("value").forGetter(data -> data.value)
	).apply(instance, (type, value) -> new DoubleParticleData((ParticleType<DoubleParticleData>)ForgeRegistries.PARTICLE_TYPES.getValue(new ResourceLocation(type)),  value)));

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
			return String.format(Locale.ROOT, "%s", ForgeRegistries.PARTICLE_TYPES.getKey(getType()), value);
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