/*
 * Copyright (c) 2022. Favouriteless
 * Enchanted, a minecraft mod.
 * GNU GPLv3 License
 *
 *     This file is part of Enchanted.
 *
 *     Enchanted is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Enchanted is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Enchanted.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.favouriteless.enchanted.core.util;

import net.minecraft.util.math.vector.Vector3d;

public class VectorHelper {

	public static Vector3d clampVector3d(Vector3d vector, double magnitude) {
 		double m = Math.sqrt(vector.x*vector.x + vector.y*vector.y + vector.z*vector.z);
		if(m >= magnitude) {
			return vector.normalize().multiply(magnitude, magnitude, magnitude);
		}
		else {
			return vector;
		}
	}

}
