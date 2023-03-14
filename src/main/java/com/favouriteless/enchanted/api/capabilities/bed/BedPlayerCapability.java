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

package com.favouriteless.enchanted.api.capabilities.bed;

import net.minecraft.nbt.CompoundTag;

import java.util.UUID;

public class BedPlayerCapability implements IBedPlayerCapability {

    private static final String UUID_KEY_NAME = "player_uuid";
    private static final String NAME_KEY_NAME = "player_name";
    private UUID uuid;
    private String name;

    @Override
    public UUID getUUID() {
        return uuid;
    }

    @Override
    public void setUUID(UUID uuid) {
        this.uuid = uuid;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        if(uuid != null) nbt.putUUID(UUID_KEY_NAME, uuid);
        if(name != null) nbt.putString(NAME_KEY_NAME, name);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        if(nbt.contains(UUID_KEY_NAME)) this.setUUID(nbt.getUUID(UUID_KEY_NAME));
        if(nbt.contains(NAME_KEY_NAME)) this.setName(nbt.getString(NAME_KEY_NAME));
    }

}