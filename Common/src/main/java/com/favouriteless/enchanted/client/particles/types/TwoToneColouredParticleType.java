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

public class TwoToneColouredParticleType extends ParticleType<TwoToneColouredParticleType.TwoToneColouredData> {
    public static final Codec<TwoToneColouredData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.fieldOf("particle_type").forGetter(data -> Registry.PARTICLE_TYPE.getKey(data.particleType).toString()),
            Codec.INT.fieldOf("red0").forGetter(data -> data.red),
            Codec.INT.fieldOf("green0").forGetter(data -> data.green),
            Codec.INT.fieldOf("blue0").forGetter(data -> data.blue),
            Codec.INT.fieldOf("red1").forGetter(data -> data.red1),
            Codec.INT.fieldOf("green1").forGetter(data -> data.green1),
            Codec.INT.fieldOf("blue1").forGetter(data -> data.blue1)
    ).apply(instance, (type, red, green, blue, red1, green1, blue1) -> new TwoToneColouredData((ParticleType<TwoToneColouredData>)Registry.PARTICLE_TYPE.get(new ResourceLocation(type)), red, green, blue, red1, green1, blue1)));

    public TwoToneColouredParticleType(boolean alwaysShow) {
        super(alwaysShow, TwoToneColouredData.DESERIALIZER);
    }

    @Override
    public Codec<TwoToneColouredData> codec() {
        return CODEC;
    }

    public static class TwoToneColouredData implements ParticleOptions {

        public static final Deserializer<TwoToneColouredData> DESERIALIZER = new Deserializer<>() {
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

            public TwoToneColouredData fromNetwork(ParticleType<TwoToneColouredData> particleType, FriendlyByteBuf buffer) {
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
            return String.format(Locale.ROOT, "%s Primary: %d %d %d Secondary: %d %d %d", Registry.PARTICLE_TYPE.getKey(getType()), red, green, blue, red1, green1, blue1);
        }

        @Override
        public void writeToNetwork(FriendlyByteBuf buffer) {
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

        public int getRed() {
            return red;
        }

        public int getGreen() {
            return green;
        }

        public int getBlue() {
            return blue;
        }

        public int getRed1() {
            return red1;
        }

        public int getGreen1() {
            return green1;
        }

        public int getBlue1() {
            return blue1;
        }

    }
}