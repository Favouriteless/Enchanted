package net.favouriteless.enchanted.common.enchanted.circle_magic;

import com.mojang.serialization.Codec;
import net.minecraft.util.StringRepresentable;

public enum RiteTargetingType implements StringRepresentable {
    DEFAULT,
    LOCATION,
    ENTITY;

    public static final Codec<RiteTargetingType> CODEC = StringRepresentable.fromEnum(RiteTargetingType::values);

    @Override
    public String getSerializedName() {
        return this.name().toLowerCase();
    }

}
