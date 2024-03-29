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

import com.favouriteless.enchanted.api.familiars.FamiliarType;
import com.favouriteless.enchanted.api.familiars.IFamiliarCapability.IFamiliarEntry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.TamableAnimal;

import java.util.UUID;

public class FamiliarEntry implements IFamiliarEntry {

		private final FamiliarType<?, ?> type;
		private UUID uuid;
		private CompoundTag nbt;
		private boolean isDismissed = false;

		public FamiliarEntry(FamiliarType<?, ?> type, TamableAnimal familiar) {
			this.uuid = familiar.getUUID();
			this.type = type;
			setNbt(familiar.saveWithoutId(new CompoundTag()));
		}

		public FamiliarEntry(FamiliarType<?, ?> type, UUID uuid, CompoundTag nbt, boolean isDismissed) {
			this.uuid = uuid;
			this.type = type;
			this.isDismissed = isDismissed;
			setNbt(nbt);
		}

		public UUID getUUID() {
			return uuid;
		}

		public void setUUID(UUID uuid) {
			this.uuid = uuid;
		}

		public FamiliarType<?, ?> getType() {
			return type;
		}

		public CompoundTag getNbt() {
			return nbt;
		}

		public void setNbt(CompoundTag nbt) {
			this.nbt = nbt.copy(); // Work on copy of nbt so we don't actually modify the entity.
			this.nbt.remove("UUID"); // Never ever store uuid, we don't want it to get replicated.
		}

		public boolean isDismissed() {
			return isDismissed;
		}

		public void setDismissed(boolean value) {
			this.isDismissed = value;
		}

	}