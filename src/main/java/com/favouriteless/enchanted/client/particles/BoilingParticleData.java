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

import com.favouriteless.enchanted.common.init.EnchantedParticles;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.PacketBuffer;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Locale;

public class BoilingParticleData implements IParticleData {

    public static final IParticleData.IDeserializer<BoilingParticleData> DESERIALIZER = new IDeserializer<BoilingParticleData>() {
        @Override
        public BoilingParticleData fromCommand(ParticleType<BoilingParticleData> pParticleType, StringReader pReader) throws CommandSyntaxException {
            pReader.expect(' ');
            int red = pReader.readInt();
            pReader.expect(' ');
            int green = pReader.readInt();
            pReader.expect(' ');
            int blue = pReader.readInt();
            return new BoilingParticleData(red, green, blue);
        }

        @Override
        public BoilingParticleData fromNetwork(ParticleType<BoilingParticleData> pParticleType, PacketBuffer pBuffer) {
            return new BoilingParticleData(pBuffer.readInt(), pBuffer.readInt(), pBuffer.readInt());
        }
    };

    public static final Codec<BoilingParticleData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.fieldOf("red").forGetter(data -> data.red),
            Codec.INT.fieldOf("green").forGetter(data -> data.green),
            Codec.INT.fieldOf("blue").forGetter(data -> data.blue)
    ).apply(instance, BoilingParticleData::new));

    private final int red;
    private final int green;
    private final int blue;

    public BoilingParticleData(int red, int green, int blue) {
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    @Override
    public ParticleType<?> getType() {
        return EnchantedParticles.BOILING.get();
    }

    @Override
    public void writeToNetwork(PacketBuffer pBuffer) {
        pBuffer.writeInt(red);
        pBuffer.writeInt(green);
        pBuffer.writeInt(blue);
    }

    @Override
    public String writeToString() {
        return String.format(Locale.ROOT, "%s %d %d %d", ForgeRegistries.PARTICLE_TYPES.getKey(getType()), red, green, blue);
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
