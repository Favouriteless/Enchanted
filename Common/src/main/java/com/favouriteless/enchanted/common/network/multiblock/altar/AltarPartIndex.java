package com.favouriteless.enchanted.common.network.multiblock.altar;

import net.minecraft.util.StringRepresentable;

public enum AltarPartIndex implements StringRepresentable {

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
    public static final AltarPartIndex[] VALUES = AltarPartIndex.values();

    private final String name;
    private final int dx;
    private final int dy;
    private final int dz;

    AltarPartIndex(String name, int dx, int dy, int dz) {
        this.name = name;
        this.dx = dx;
        this.dy = dy;
        this.dz = dz;
    }

    public static AltarPartIndex getIndex(int dx, int dz) {
        return VALUES[dx*3 + dz + 1];
    }

    public int getDx() {
        return dx;
    }

    public int getDy() {
        return dy;
    }

    public int getDz() {
        return dz;
    }

    @Override
    public String getSerializedName() {
        return name;
    }
}
