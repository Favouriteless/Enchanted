/*
 * Copyright (c) 2021. Favouriteless
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

package com.favouriteless.enchanted.common.capabilities.bed;

import net.minecraft.nbt.CompoundNBT;

import java.util.UUID;

public class BedPlayerCapability implements IBedPlayerCapability {

    private static final String KEY_NAME = "playeruuid";
    private UUID value;

    @Override
    public UUID getValue() {
        return value;
    }

    @Override
    public void setValue(UUID uuid) {
        value = uuid;
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();
        if(value != null) nbt.putUUID(KEY_NAME, value);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        if(nbt.contains(KEY_NAME)) this.setValue(nbt.getUUID(KEY_NAME));
    }

}
