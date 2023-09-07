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

package com.favouriteless.enchanted.common.familiars.types;

import com.favouriteless.enchanted.common.entities.FamiliarCat;
import com.favouriteless.enchanted.common.familiars.FamiliarType;
import com.favouriteless.enchanted.common.init.registry.EnchantedEntityTypes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Cat;

public class CatFamiliarType extends FamiliarType<Cat, FamiliarCat> {

	public CatFamiliarType() {
		super(() -> EntityType.CAT, EnchantedEntityTypes.FAMILIAR_CAT);
	}

	@Override
	public FamiliarCat create(Cat from) {
		FamiliarCat familiar = EnchantedEntityTypes.FAMILIAR_CAT.get().create(from.getLevel());
		familiar.setCatType(from.getCatType());
		familiar.setCustomName(from.getCustomName());
		familiar.setCollarColor(from.getCollarColor());
		familiar.setCustomNameVisible(from.isCustomNameVisible());
		return familiar;
	}

}
