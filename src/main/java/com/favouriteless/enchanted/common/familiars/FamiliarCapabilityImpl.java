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

import com.favouriteless.enchanted.api.capabilities.IFamiliarCapability;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.UUID;

public class FamiliarCapabilityImpl implements IFamiliarCapability {

    private EntityType<?> type = null;
    private UUID uuid = null;

    @Override
    public EntityType<?> getFamiliarType() {
        return type;
    }

    @Override
    public void setFamiliarType(EntityType<?> type) {
        this.type = type;
    }

    @Override
    public UUID getFamiliarUUID() {
        return uuid;
    }

    @Override
    public void setFamiliarUUID(UUID uuid) {
        this.uuid = uuid;
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        if(type != null) {
            nbt.putString("type", ForgeRegistries.ENTITIES.getKey(type).toString());
            nbt.putUUID("uuid", uuid);
        }
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        if(nbt.contains("type")) {
            type = ForgeRegistries.ENTITIES.getValue(new ResourceLocation(nbt.getString("type")));
            uuid = nbt.getUUID("uuid");
        }
    }

}