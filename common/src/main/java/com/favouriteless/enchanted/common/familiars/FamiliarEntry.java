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