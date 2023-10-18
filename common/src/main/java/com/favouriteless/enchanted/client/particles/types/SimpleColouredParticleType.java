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

public class SimpleColouredParticleType extends ParticleType<SimpleColouredParticleType.SimpleColouredData> {
    public static final Codec<SimpleColouredData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.fieldOf("particle_type").forGetter(data -> Registry.PARTICLE_TYPE.getKey(data.particleType).toString()),
            Codec.INT.fieldOf("red").forGetter(data -> data.red),
            Codec.INT.fieldOf("green").forGetter(data -> data.green),
            Codec.INT.fieldOf("blue").forGetter(data -> data.blue)
    ).apply(instance, (type, red, green, blue) -> new SimpleColouredData((ParticleType<SimpleColouredData>)Registry.PARTICLE_TYPE.get(new ResourceLocation(type)), red, green, blue)));

    public SimpleColouredParticleType(boolean alwaysShow) {
        super(alwaysShow, SimpleColouredData.DESERIALIZER);
    }

    @Override
    public Codec<SimpleColouredData> codec() {
        return CODEC;
    }

    public static class SimpleColouredData implements ParticleOptions {

        public static final Deserializer<SimpleColouredData> DESERIALIZER = new Deserializer<>() {
            public SimpleColouredData fromCommand(ParticleType<SimpleColouredData> particleType, StringReader reader) throws CommandSyntaxException {
                reader.expect(' ');
                int red = reader.readInt();
                reader.expect(' ');
                int green = reader.readInt();
                reader.expect(' ');
                int blue = reader.readInt();

                return new SimpleColouredData(particleType, red, green, blue);
            }

            public SimpleColouredData fromNetwork(ParticleType<SimpleColouredData> particleType, FriendlyByteBuf buffer) {
                return new SimpleColouredData(particleType, buffer.readInt(), buffer.readInt(), buffer.readInt());
            }
        };

        private final ParticleType<SimpleColouredData> particleType;
        private final int red;
        private final int green;
        private final int blue;

        public SimpleColouredData(ParticleType<SimpleColouredData> particleType, int red, int green, int blue) {
            this.particleType = particleType;
            this.red = red;
            this.green = green;
            this.blue = blue;
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

    }
}