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

public class CauldronBrewParticleData implements IParticleData {

    public static final IParticleData.IDeserializer<CauldronBrewParticleData> DESERIALIZER = new IDeserializer<CauldronBrewParticleData>() {
        @Override
        public CauldronBrewParticleData fromCommand(ParticleType<CauldronBrewParticleData> pParticleType, StringReader pReader) throws CommandSyntaxException {
            pReader.expect(' ');
            int red = pReader.readInt();
            pReader.expect(' ');
            int green = pReader.readInt();
            pReader.expect(' ');
            int blue = pReader.readInt();
            return new CauldronBrewParticleData(red, green, blue);
        }

        @Override
        public CauldronBrewParticleData fromNetwork(ParticleType<CauldronBrewParticleData> pParticleType, PacketBuffer pBuffer) {
            return new CauldronBrewParticleData(pBuffer.readInt(), pBuffer.readInt(), pBuffer.readInt());
        }
    };

    public static final Codec<CauldronBrewParticleData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.fieldOf("red").forGetter(data -> data.red),
            Codec.INT.fieldOf("green").forGetter(data -> data.green),
            Codec.INT.fieldOf("blue").forGetter(data -> data.blue)
    ).apply(instance, CauldronBrewParticleData::new));

    int red;
    int green;
    int blue;

    double xSpeed;
    double ySpeed;
    double zSpeed;

    public CauldronBrewParticleData(int red, int green, int blue) {
        this.red = red;
        this.green = green;
        this.blue = blue;
    }


    @Override
    public ParticleType<?> getType() {
        return EnchantedParticles.CAULDRON_BREW.get();
    }

    @Override
    public void writeToNetwork(PacketBuffer pBuffer) {
        pBuffer.writeInt(red);
        pBuffer.writeInt(green);
        pBuffer.writeInt(blue);
        pBuffer.writeDouble(xSpeed);
        pBuffer.writeDouble(ySpeed);
        pBuffer.writeDouble(zSpeed);
    }

    @Override
    public String writeToString() {
        return String.format(Locale.ROOT, "%s r%d g%d b%d x%s y%s z%s", ForgeRegistries.PARTICLE_TYPES.getKey(getType()), red, green, blue, xSpeed, ySpeed, zSpeed);
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
