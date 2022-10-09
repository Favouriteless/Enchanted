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

package com.favouriteless.enchanted.api.altar;

public class AltarPowerProvider<T> {

    private final T key;
    private final int power;
    private final int limit;

    public AltarPowerProvider(T key, int power, int limit) {
        this.key = key;
        this.power = power;
        this.limit = limit;
    }

    public boolean sameKey(AltarPowerProvider<?> powerProvider) {
        return powerProvider.key.equals(this.key);
    }

    public boolean is(T key) {
        return this.key == key;
    }

    public T getKey() {
        return this.key;
    }

    public int getPower() {
        return this.power;
    }

    public int getLimit() {
        return this.limit;
    }
}