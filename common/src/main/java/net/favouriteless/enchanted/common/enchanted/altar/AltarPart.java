package net.favouriteless.enchanted.common.enchanted.altar;

import net.favouriteless.enchanted.common.blocks.altar.AltarBlock;
import net.minecraft.util.StringRepresentable;

/**
 * Represents possible states for an {@link AltarBlock}, where the numbers refer to the position offset from the
 * north-western corner (0, 0) of the altar.
 */
public enum AltarPart implements StringRepresentable {

    UNFORMED("unformed", 0, 0, 0),
    P000("p000", 0, 0, 0),
    P001("p001", 0, 0, 1),
    P002("p002", 0, 0, 2),
    P100("p100", 1, 0, 0),
    P101("p101", 1, 0, 1),
    P102("p102", 1, 0, 2),
    P200("p200", 2, 0, 0),
    P201("p201", 2, 0, 1),
    P202("p202", 2, 0, 2);

    // Optimization
    public static final AltarPart[] VALUES = AltarPart.values();

    private final String name;
    private final int x;
    private final int y;
    private final int z;

    AltarPart(String name, int x, int y, int z) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public static AltarPart getByOffset(int dx, int dz) {
        return VALUES[dx*3 + dz + 1];
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    @Override
    public String getSerializedName() {
        return name;
    }

}
