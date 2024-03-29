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

package com.favouriteless.enchanted.api.capabilities;

import com.favouriteless.enchanted.Enchanted;
import com.favouriteless.enchanted.common.items.TaglockItem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BedBlockEntity;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.UUID;

/**
 * Stores the UUID of the last player to sleep in a {@link BedBlockEntity}, used by {@link TaglockItem}.
 */
public interface IBedCapability extends INBTSerializable<CompoundTag> {

    ResourceLocation LOCATION = Enchanted.location("bed");

    UUID getUUID();
    void setUUID(UUID uuid);
    String getName();
    void setName(String name);
}
