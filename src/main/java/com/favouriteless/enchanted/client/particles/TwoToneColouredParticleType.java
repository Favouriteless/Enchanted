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

public class TwoToneColouredParticleType extends ParticleType<TwoToneColouredParticleType.TwoToneColouredData> {
    public static final Codec<TwoToneColouredData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.fieldOf("particle_type").forGetter(data -> data.particleType.getRegistryName().toString()),
            Codec.INT.fieldOf("red0").forGetter(data -> data.red),
            Codec.INT.fieldOf("green0").forGetter(data -> data.green),
            Codec.INT.fieldOf("blue0").forGetter(data -> data.blue),
            Codec.INT.fieldOf("red1").forGetter(data -> data.red1),
            Codec.INT.fieldOf("green1").forGetter(data -> data.green1),
            Codec.INT.fieldOf("blue1").forGetter(data -> data.blue1)
    ).apply(instance, (type, red, green, blue, red1, green1, blue1) -> new TwoToneColouredData((ParticleType<TwoToneColouredData>)ForgeRegistries.PARTICLE_TYPES.getValue(new ResourceLocation(type)), red, green, blue, red1, green1, blue1)));

    public TwoToneColouredParticleType(boolean alwaysShow) {
        super(alwaysShow, TwoToneColouredData.DESERIALIZER);
    }

    @Override
    public Codec<TwoToneColouredData> codec() {
        return CODEC;
    }

    public static class TwoToneColouredData implements IParticleData {

        public static final IDeserializer<TwoToneColouredData> DESERIALIZER = new IDeserializer<TwoToneColouredData>() {
            public TwoToneColouredData fromCommand(ParticleType<TwoToneColouredData> particleType, StringReader reader) throws CommandSyntaxException {
                reader.expect(' ');
                int red = reader.readInt();
                reader.expect(' ');
                int green = reader.readInt();
                reader.expect(' ');
                int blue = reader.readInt();
                reader.expect(' ');
                int red1 = reader.readInt();
                reader.expect(' ');
                int green1 = reader.readInt();
                reader.expect(' ');
                int blue1 = reader.readInt();

                return new TwoToneColouredData(particleType, red, green, blue, red1, green1, blue1);
            }

            public TwoToneColouredData fromNetwork(ParticleType<TwoToneColouredData> particleType, PacketBuffer buffer) {
                return new TwoToneColouredData(particleType, buffer.readInt(), buffer.readInt(), buffer.readInt(), buffer.readInt(), buffer.readInt(), buffer.readInt());
            }
        };

        private final ParticleType<TwoToneColouredData> particleType;
        private final int red;
        private final int green;
        private final int blue;
        private final int red1;
        private final int green1;
        private final int blue1;

        public TwoToneColouredData(ParticleType<TwoToneColouredData> particleType, int red, int green, int blue, int red1, int green1, int blue1) {
            this.particleType = particleType;
            this.red = red;
            this.green = green;
            this.blue = blue;
            this.red1 = red1;
            this.green1 = green1;
            this.blue1 = blue1;
        }


        @Override
        public String writeToString() {
            return String.format(Locale.ROOT, "%s Primary: %d %d %d Secondary: %d %d %d", ForgeRegistries.PARTICLE_TYPES.getKey(getType()), red, green, blue, red1, green1, blue1);
        }

        @Override
        public void writeToNetwork(PacketBuffer buffer) {
            buffer.writeInt(red);
            buffer.writeInt(green);
            buffer.writeInt(blue);
            buffer.writeInt(red1);
            buffer.writeInt(green1);
            buffer.writeInt(blue1);
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

        @OnlyIn(Dist.CLIENT)
        public int getRed1() {
            return red1;
        }

        @OnlyIn(Dist.CLIENT)
        public int getGreen1() {
            return green1;
        }

        @OnlyIn(Dist.CLIENT)
        public int getBlue1() {
            return blue1;
        }

    }
}