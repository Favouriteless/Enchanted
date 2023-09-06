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

package com.favouriteless.enchanted.common.init.registry;

import com.favouriteless.enchanted.api.altar.AltarPowerProvider;

import java.util.ArrayList;
import java.util.List;

public class PowerProviderRegistry<T> {

	private final List<AltarPowerProvider<T>> providers = new ArrayList<>();

	public void register(AltarPowerProvider<T> provider) {
		if(!providers.isEmpty()) {
			for(int i = 0; i < providers.size(); i++) {
				AltarPowerProvider<T> oldProvider = providers.get(i);

				if(provider.getPower() > oldProvider.getPower()) { // Prioritise the highest power per block first.
					providers.add(i, provider);
					break;
				}
				else if(provider.getPower() == oldProvider.getPower()) { // Then prioritise whatever has the highest limit
					if(provider.getLimit() > oldProvider.getLimit()) {
						providers.add(i, provider);
						break;
					}
				}
				else if(i == providers.size() - 1) { // Lastly tack on end if at end of list
					providers.add(provider);
				}
			}
		}
		else {
			providers.add(provider);
		}
	}

	public AltarPowerProvider<T> get(T key) {
		for(AltarPowerProvider<T> provider : providers) {
			if(provider.is(key))
				return provider;
		}
		return null;
	}

	public List<AltarPowerProvider<T>> getAll() {
		return providers;
	}

	public void reset() {
		providers.clear();
	}

}
