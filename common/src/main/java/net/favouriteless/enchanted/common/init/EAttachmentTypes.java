package net.favouriteless.enchanted.common.init;

import com.mojang.serialization.Codec;
import net.favouriteless.enchanted.platform.EAttachmentType;
import net.favouriteless.enchanted.platform.EServices;
import net.minecraft.network.codec.ByteBufCodecs;

public class EAttachmentTypes {

    public static final EAttachmentType<Float> SINKING_FACTOR = EServices.ATTACHMENT.register("sinking_factor", () -> 0.0F, Codec.FLOAT, ByteBufCodecs.FLOAT, false);
    public static final EAttachmentType<Double> CLUMSY_CHANCE = EServices.ATTACHMENT.register("clumsy_chance", () -> 0.0D, Codec.DOUBLE, ByteBufCodecs.DOUBLE, true);

    public static void load() {} // Method which exists purely to load the class.

}
