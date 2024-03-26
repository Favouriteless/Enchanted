package com.favouriteless.enchanted.api.familiars;

import com.favouriteless.enchanted.api.familiars.IFamiliarCapability.IFamiliarEntry;
import com.favouriteless.enchanted.api.taglock.BedTaglockSavedData;
import com.favouriteless.enchanted.common.init.registry.FamiliarTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class FamiliarSavedData extends SavedData {

    private static final String NAME = "enchanted_familiars";
    private final Map<UUID, IFamiliarEntry> entries = new HashMap<>();

    public FamiliarSavedData() {
        super();
    }

    /**
     * @param uuid The {@link UUID} of the {@link Player} to grab data from.
     *
     * @return The {@link IFamiliarEntry} associated with the specified {@link Player}, or null if the player does
     * not own a familiar.
     */
    public IFamiliarEntry getEntry(UUID uuid) {
        return entries.get(uuid);
    }

    /**
     * Set a {@link Player}'s familiar directly by specifying the {@link FamiliarType} and {@link TamableAnimal}.
     *
     * @param owner The {@link UUID} of the familiar's owner.
     * @param type The {@link FamiliarType} of the familiar.
     * @param familiar The familiar to be used.
     */
    public void setFamiliar(UUID owner, FamiliarType<?, ?> type, TamableAnimal familiar) {
        entries.put(owner, new FamiliarEntry(type, familiar));
    }

    /**
     * @param level The {@link Level} to grab data from.
     *
     * @return An instance of {@link BedTaglockSavedData} belonging to level.
     */
    public static FamiliarSavedData get(Level level) {
        if(level instanceof ServerLevel serverLevel)
            return serverLevel.getDataStorage().computeIfAbsent(FamiliarSavedData::load, FamiliarSavedData::new, NAME);
        else
            throw new RuntimeException("Game attempted to load serverside familiar data from a clientside world.");
    }


    // -------------------- IMPLEMENTATION  DETAILS BELOW THIS POINT, NOT NEEDED FOR API USERS --------------------

    private static FamiliarSavedData load(CompoundTag nbt) {
        FamiliarSavedData data = new FamiliarSavedData();
        for(String key : nbt.getAllKeys()) {
            CompoundTag tag = nbt.getCompound(key);
            IFamiliarEntry entry = new FamiliarEntry(
                    FamiliarTypes.get(new ResourceLocation(tag.getString("type"))),
                    tag.getUUID("uuid"),
                    tag.getCompound("nbt"),
                    tag.getBoolean("isDismissed")
            );
            data.entries.put(UUID.fromString(key), entry);
        }

        return data;
    }

    @Override
    @NotNull
    public CompoundTag save(@NotNull CompoundTag nbt) {
        for(UUID uuid : entries.keySet()) {
            CompoundTag tag = new CompoundTag();
            IFamiliarEntry entry = entries.get(uuid);
            if(entry != null) {
                tag.putUUID("uuid", entry.getUUID());
                tag.putString("type", entry.getType().getId().toString());
                tag.put("nbt", entry.getNbt());
                tag.putBoolean("isDismissed", entry.isDismissed());
                nbt.put(uuid.toString(), tag);
            }
        }
        return nbt;
    }



    public static class FamiliarEntry implements IFamiliarEntry {

        private final FamiliarType<?, ?> type;
        private UUID uuid;
        private CompoundTag nbt;
        private boolean isDismissed = false;

        private FamiliarEntry(FamiliarType<?, ?> type, TamableAnimal familiar) {
            this.uuid = familiar.getUUID();
            this.type = type;
            setNbt(familiar.saveWithoutId(new CompoundTag()));
        }

        private FamiliarEntry(FamiliarType<?, ?> type, UUID uuid, CompoundTag nbt, boolean isDismissed) {
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
            this.nbt = nbt.copy(); // Work on copy of nbt, so we don't actually modify the entity.
            this.nbt.remove("UUID"); // Never ever store uuid, we don't want it to get replicated.
        }

        public boolean isDismissed() {
            return isDismissed;
        }

        public void setDismissed(boolean value) {
            this.isDismissed = value;
        }

    }
}