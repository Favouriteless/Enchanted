package com.favouriteless.enchanted.common.init;

public enum PoppetColour {
	EARTH(112, 80, 54, 59, 43, 30),
	EQUIPMENT(227, 227, 227, 163, 163, 163),
	FIRE(255, 157, 38, 222, 70, 53),
	HUNGER(113, 184, 59, 78, 128, 65),
	VOID(112, 63, 143, 58, 33, 74),
	WATER(73, 180, 230, 24, 52, 196);

	public final int rPrimary;
	public final int gPrimary;
	public final int bPrimary;
	public final int rSecondary;
	public final int gSecondary;
	public final int bSecondary;

	PoppetColour(int rPrimary, int gPimary, int bPrimary, int rSecondary, int gSecondary, int bSecondary) {
		this.rPrimary = rPrimary;
		this.gPrimary = gPimary;
		this.bPrimary = bPrimary;
		this.rSecondary = rSecondary;
		this.gSecondary = gSecondary;
		this.bSecondary = bSecondary;
	}

}