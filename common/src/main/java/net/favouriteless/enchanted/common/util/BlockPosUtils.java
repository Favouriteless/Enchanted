package net.favouriteless.enchanted.common.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

public class BlockPosUtils {

    public static Iterable<MutableBlockPos> iterableSphereHollow(BlockPos center, int radius) {
        return new Iterable<>() {
            @Override
            public @NotNull Iterator<MutableBlockPos> iterator() {
                return iteratorSphereHollow(center, radius);
            }
        };
    }

    public static Iterator<MutableBlockPos> iteratorSphereHollow(BlockPos center, int radius) {
        return new Iterator<>() {

            private final MutableBlockPos pos = new MutableBlockPos(0, 0, 0);
            private final float increment = 1.0f / radius;
            private float theta = 0;
            private float pitch = -Mth.PI;

            @Override
            public boolean hasNext() {
                return theta < Mth.PI && pitch < Mth.PI;
            }

            @Override
            public MutableBlockPos next() {
                float theta = this.theta;
                float pitch = this.pitch;

                this.pitch += increment;
                if (this.pitch > Mth.PI) {
                    this.theta += increment;
                    this.pitch = -Mth.PI;
                }

                float cosP = Mth.cos(pitch);
                pos.set(
                        Math.round(Mth.sin(theta) * cosP * radius),
                        Math.round(Mth.sin(pitch) * radius),
                        Math.round(Mth.cos(theta) * cosP * radius)
                );
                return pos.move(center);
            }

        };
    }

    public static Iterable<MutableBlockPos> iterableCircleHollow(BlockPos center, int radius, int increment) {
        return new Iterable<>() {

            @Override
            public @NotNull Iterator<MutableBlockPos> iterator() {
                return iteratorCircleHollow(center, radius, increment);
            }

        };
    }

    public static Iterator<MutableBlockPos> iteratorCircleHollow(BlockPos center, int radius, int step) {
        return new Iterator<>() {

            private final MutableBlockPos pos = new MutableBlockPos(0, 0, 0);
            private final float increment = 1.0f / radius;
            private float angle = 0;

            @Override
            public boolean hasNext() {
                return angle < Mth.TWO_PI;
            }

            @Override
            public MutableBlockPos next() {
                float theta = this.angle;
                this.angle += increment * step;

                pos.set(
                        Math.round(Mth.sin(theta) * radius),
                        0,
                        Math.round(Mth.cos(theta) * radius)
                );

                return pos.move(center);
            }

        };
    }

    public static Iterable<MutableBlockPos> iterableCircleHollow(BlockPos center, int radius) {
        return iterableCircleHollow(center, radius, 1);
    }

    public static Iterator<MutableBlockPos> iteratorCircleHollow(BlockPos center, int radius) {
        return iteratorCircleHollow(center, radius, 1);
    }

}