package com.favouriteless.enchanted.common.familiars;

import com.favouriteless.enchanted.api.familiars.FamiliarType;
import com.favouriteless.enchanted.api.familiars.IFamiliarCapability;
import com.favouriteless.enchanted.common.init.registry.FamiliarTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.TamableAnimal;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class FamiliarCapability implements IFamiliarCapability {

    private final Map<UUID, IFamiliarEntry> familiars = new HashMap<>();

    @Override
    public IFamiliarEntry getFamiliarFor(UUID owner) {
        return familiars.get(owner);
    }

    @Override
    public void setFamiliar(UUID owner, FamiliarType<?, ?> type, TamableAnimal familiar) {
        familiars.put(owner, new FamiliarEntry(type, familiar));
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();

        for(UUID uuid : familiars.keySet()) {
            CompoundTag tag = new CompoundTag();
            IFamiliarEntry entry = familiars.get(uuid);
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
            IFamiliarEntry entry = new FamiliarEntry(
                    FamiliarTypes.REGISTRY.get().getValue(new ResourceLocation(tag.getString("type"))),
                    tag.getUUID("uuid"),
                    tag.getCompound("nbt"),
                    tag.getBoolean("isDismissed")
            );
            familiars.put(UUID.fromString(key), entry);
        }
    }

}