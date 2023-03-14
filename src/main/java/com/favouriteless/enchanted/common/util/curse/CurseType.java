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

package com.favouriteless.enchanted.common.util.curse;

import com.favouriteless.enchanted.api.curses.AbstractCurse;
import net.minecraftforge.registries.ForgeRegistryEntry;

import java.util.function.Supplier;

public class CurseType<T extends AbstractCurse> extends ForgeRegistryEntry<CurseType<?>> {

	private final Supplier<AbstractCurse> supplier;

	public CurseType(Supplier<AbstractCurse> supplier) {
		this.supplier = supplier;
	}

	public AbstractCurse create() {
		return this.supplier.get();
	}

}
