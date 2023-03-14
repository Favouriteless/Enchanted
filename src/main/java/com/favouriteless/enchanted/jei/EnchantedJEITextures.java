/*
 *
 *   Copyright (c) 2023. Favouriteless
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

package com.favouriteless.enchanted.jei;

import com.favouriteless.enchanted.Enchanted;
import com.favouriteless.enchanted.common.util.rite.CirclePart;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

import java.util.HashMap;
import java.util.Map;

public class EnchantedJEITextures {

	private static final Map<CirclePart, String> CIRCLE_PART_TEXTURE_NAMES = new HashMap<>();
	private static final Map<Block, String> BLOCK_TEXTURE_SUFFIXES = new HashMap<>();

	public static void registerCirclePartPrefix(CirclePart part, String name) {
		CIRCLE_PART_TEXTURE_NAMES.putIfAbsent(part, name);
	}

	public static void registerBlockSuffix(Block block, String name) {
		BLOCK_TEXTURE_SUFFIXES.putIfAbsent(block, name);
	}

	public static ResourceLocation getCircleTextureLocation(CirclePart circle, Block block) {
		return (CIRCLE_PART_TEXTURE_NAMES.containsKey(circle) && BLOCK_TEXTURE_SUFFIXES.containsKey(block)) ?
				Enchanted.location("textures/gui/jei/" + CIRCLE_PART_TEXTURE_NAMES.get(circle) + "_" + BLOCK_TEXTURE_SUFFIXES.get(block) + ".png") : null;
	}

}
