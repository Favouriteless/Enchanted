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

package com.favouriteless.enchanted.common.tileentity;

import com.favouriteless.enchanted.common.init.EnchantedTileEntityTypes;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.UUID;

public class BloodPoppyTileEntity extends BlockEntity {

    private UUID uuid = null;
    private String name = null;

    public BloodPoppyTileEntity() {
        super(EnchantedTileEntityTypes.BLOOD_POPPY.get());
    }

    public UUID getUUID() {
        return uuid;
    }

    public void setUUID(UUID uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void reset() {
        this.uuid = null;
        this.name = null;
    }

    @Override
    public CompoundTag save(CompoundTag nbt) {
        if(uuid != null) nbt.putUUID("uuid", uuid);
        return super.save(nbt);
    }

    @Override
    public void load(BlockState state, CompoundTag nbt) {
        super.load(state, nbt);
        if(nbt.contains("uuid")) this.uuid = nbt.getUUID("uuid");
    }
}
