/*
 * Copyright (c) 2021. Favouriteless
 * Enchanted, a minecraft mod.
 * GNU GPLv3 License
 *
 *     This file is part of Enchanted.
 *
 *     Enchanted is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Enchanted is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Enchanted.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.favouriteless.enchanted.client.particles;

import com.favouriteless.enchanted.client.particles.PoppetAnimationParticleType.PoppetAnimationData;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.PacketBuffer;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Locale;

public class PoppetAnimationParticleType extends ParticleType<PoppetAnimationData> {
	public static final Codec<PoppetAnimationData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			Codec.STRING.fieldOf("particle_type").forGetter(data -> data.particleType.getRegistryName().toString()),
			Codec.INT.fieldOf("red").forGetter(data -> data.red),
			Codec.INT.fieldOf("green").forGetter(data -> data.green),
			Codec.INT.fieldOf("blue").forGetter(data -> data.blue)
	).apply(instance, (type, red, green, blue) -> new PoppetAnimationData((ParticleType<PoppetAnimationData>)ForgeRegistries.PARTICLE_TYPES.getValue(new ResourceLocation(type)), red, green, blue)));

	public PoppetAnimationParticleType(boolean alwaysShow) {
		super(alwaysShow, PoppetAnimationData.DESERIALIZER);
	}

	@Override
	public Codec<PoppetAnimationData> codec() {
		return CODEC;
	}

	public static class PoppetAnimationData implements IParticleData {

		public static final IDeserializer<PoppetAnimationData> DESERIALIZER = new IDeserializer<PoppetAnimationData>() {
			public PoppetAnimationData fromCommand(ParticleType<PoppetAnimationData> particleType, StringReader reader) throws CommandSyntaxException {
				reader.expect(' ');
				int red = reader.readInt();
				reader.expect(' ');
				int green = reader.readInt();
				reader.expect(' ');
				int blue = reader.readInt();

				return new PoppetAnimationData(particleType, red, green, blue);
			}

			public PoppetAnimationData fromNetwork(ParticleType<PoppetAnimationData> particleType, PacketBuffer buffer) {
				return new PoppetAnimationData(particleType, buffer.readInt(), buffer.readInt(), buffer.readInt());
			}
		};

		private final ParticleType<PoppetAnimationData> particleType;
		private final int red;
		private final int green;
		private final int blue;

		public PoppetAnimationData(ParticleType<PoppetAnimationData> particleType, int red, int green, int blue) {
			this.particleType = particleType;
			this.red = red;
			this.green = green;
			this.blue = blue;
		}


		@Override
		public String writeToString() {
			return String.format(Locale.ROOT, "%s %d %d %d", ForgeRegistries.PARTICLE_TYPES.getKey(getType()), red, green, blue);
		}

		@Override
		public void writeToNetwork(PacketBuffer buffer) {
			buffer.writeInt(red);
			buffer.writeInt(green);
			buffer.writeInt(blue);
		}

		@Override
		public ParticleType<?> getType() {
			return particleType;
		}

		@OnlyIn(Dist.CLIENT)
		public int getRed() {
			return red;
		}

		@OnlyIn(Dist.CLIENT)
		public int getGreen() {
			return green;
		}

		@OnlyIn(Dist.CLIENT)
		public int getBlue() {
			return blue;
		}

	}
}