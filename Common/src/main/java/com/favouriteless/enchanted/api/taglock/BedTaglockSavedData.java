package com.favouriteless.enchanted.api.taglock;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BedBlockEntity;
import net.minecraft.world.level.saveddata.SavedData;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

/**
 * {@link BedTaglockSavedData} is where the UUID and name of the last player to use a {@link BedBlockEntity} is stored,
 * which is used for taglocks.
 */
public class BedTaglockSavedData extends SavedData {

    private static final String NAME = "enchanted_bed_taglocks";
    private final Map<BlockPos, IBedTaglock> entries = new HashMap<>();

    public BedTaglockSavedData() {
        super();
    }

    /**
     * @param bed The {@link BedBlockEntity} to grab data from.
     *
     * @return The {@link IBedTaglock} associated with a specific {@link BedBlockEntity}.
     */
    public IBedTaglock getEntry(BedBlockEntity bed) {
        return getEntry(bed.getBlockPos());
    }

    /**
     * @param level The {@link Level} to grab data from.
     *
     * @return An instance of {@link BedTaglockSavedData} belonging to level.
     */
    public static BedTaglockSavedData get(Level level) {
        if(level instanceof ServerLevel serverLevel)
            return serverLevel.getDataStorage().computeIfAbsent(BedTaglockSavedData::load, BedTaglockSavedData::new, NAME);
        else
            throw new RuntimeException("Game attempted to load serverside taglock (bed) data from a clientside world.");
    }

    // -------------------- IMPLEMENTATION  DETAILS BELOW THIS POINT, NOT NEEDED FOR API USERS --------------------

    private IBedTaglock getEntry(BlockPos pos) {
        return entries.computeIfAbsent(pos, (_pos) -> new BedTaglockImpl());
    }

    private static BedTaglockSavedData load(CompoundTag nbt) {
        BedTaglockSavedData data = new BedTaglockSavedData();
        ListTag entryList = nbt.getList("entryList", Tag.TAG_COMPOUND);

        for(Tag e : entryList) {
            CompoundTag entryNbt = (CompoundTag)e; // This cast should be safe.
            data.getEntry(BlockPos.of(entryNbt.getLong("pos"))).deserialize(entryNbt);
        }

        return data;
    }

    @Override
    public CompoundTag save(CompoundTag nbt) {
        ListTag list = new ListTag();
        for(Entry<BlockPos, IBedTaglock> entry : entries.entrySet()) {
            IBedTaglock value = entry.getValue();
            if(value.getUUID() != null) {
                CompoundTag entryNbt = entry.getValue().serialize();
                entryNbt.putLong("pos", entry.getKey().asLong());
            }
        }

        nbt.put("entryList", list);
        return nbt;
    }



    private static class BedTaglockImpl implements IBedTaglock {

        private UUID uuid = null;
        private String name = null;

        private BedTaglockImpl() {}

        @Override
        public CompoundTag serialize() {
            CompoundTag nbt = new CompoundTag();
            nbt.putUUID("uuid", uuid);
            nbt.putString("name", name);
            return nbt;
        }

        @Override
        public void deserialize(CompoundTag nbt) {
            uuid = nbt.getUUID("uuid");
            name = nbt.getString("name");
        }

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

    }

}