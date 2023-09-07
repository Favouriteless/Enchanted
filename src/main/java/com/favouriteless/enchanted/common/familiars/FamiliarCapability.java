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

package com.favouriteless.enchanted.common.familiars;

import com.favouriteless.enchanted.api.familiars.IFamiliarCapability;
import com.favouriteless.enchanted.common.init.registry.FamiliarTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class FamiliarCapability implements IFamiliarCapability {

    private final Map<UUID, FamiliarEntry> familiars = new HashMap<>();

    @Override
    public FamiliarEntry getFamiliarFor(UUID owner) {
        return familiars.get(owner);
    }

    @Override
    public void setFamiliar(UUID owner, FamiliarType<?, ?> type, Entity familiar) {
        familiars.put(owner, new FamiliarEntry(type, familiar));
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();

        for(UUID uuid : familiars.keySet()) {
            CompoundTag tag = new CompoundTag();
            FamiliarEntry entry = familiars.get(uuid);
            tag.putUUID("uuid", entry.getUUID());
            tag.putString("type", FamiliarTypes.REGISTRY.get().getKey(entry.getType()).toString());
            tag.put("nbt", entry.getNbt());
            tag.putBoolean("isDismissed", entry.isDismissed());
            nbt.put(uuid.toString(), tag);
        }

        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        for(String key : nbt.getAllKeys()) {
            CompoundTag tag = nbt.getCompound(key);
            FamiliarEntry entry = new FamiliarEntry(
                    FamiliarTypes.REGISTRY.get().getValue(new ResourceLocation(tag.getString("type"))),
                    tag.getUUID("uuid"),
                    tag.getCompound("nbt"),
                    tag.getBoolean("isDismissed")
            );
            familiars.put(UUID.fromString(key), entry);
        }
    }

}