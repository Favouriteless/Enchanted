/*
 *
 *   Copyright (c) 2022. Favouriteless
 *   Enchanted, a minecraft mod.
 *   GNU GPLv3 License
 *
 *       This file is part of Enchanted.
 *
 *       Enchanted is free software: you can redistribute it and/or modify
 *       it under the terms of the GNU General Public License as published by
 *       the Free Software Foundation, either version 3 of the License, or
 *       (at your option) any later version.
 *
 *       Enchanted is distributed in the hope that it will be useful,
 *       but WITHOUT ANY WARRANTY; without even the implied warranty of
 *       MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *       GNU General Public License for more details.
 *
 *       You should have received a copy of the GNU General Public License
 *       along with Enchanted.  If not, see <https://www.gnu.org/licenses/>.
 *
 *
 */

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