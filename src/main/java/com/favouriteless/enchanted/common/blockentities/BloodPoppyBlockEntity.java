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

package com.favouriteless.enchanted.common.blockentities;

import com.favouriteless.enchanted.common.init.EnchantedBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.UUID;

public class BloodPoppyBlockEntity extends BlockEntity {

    private UUID uuid = null;
    private String name = null;

    public BloodPoppyBlockEntity(BlockPos pos, BlockState state) {
        super(EnchantedBlockEntityTypes.BLOOD_POPPY.get(), pos, state);
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
    public void saveAdditional(CompoundTag nbt) {
        super.saveAdditional(nbt);
        if(uuid != null)
            nbt.putUUID("uuid", uuid);
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        if(nbt.contains("uuid"))
            this.uuid = nbt.getUUID("uuid");
    }
}
