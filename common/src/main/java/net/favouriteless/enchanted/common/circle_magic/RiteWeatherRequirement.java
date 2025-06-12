package net.favouriteless.enchanted.common.circle_magic;

import com.mojang.serialization.Codec;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.Level;

import java.util.function.Predicate;

public enum RiteWeatherRequirement implements StringRepresentable {
    NONE(level -> true),
    CLEAR(level -> !level.isRaining() && !level.isThundering()),
    RAINING(Level::isRaining),
    THUNDERING(Level::isThundering);

    public static final Codec<RiteWeatherRequirement> CODEC = StringRepresentable.fromEnum(RiteWeatherRequirement::values);

    private final Predicate<Level> check;

    RiteWeatherRequirement(Predicate<Level> check) {
        this.check = check;
    }

    @Override
    public String getSerializedName() {
        return this.name().toLowerCase();
    }

    public boolean check(Level level) {
        return check.test(level);
    }

}
