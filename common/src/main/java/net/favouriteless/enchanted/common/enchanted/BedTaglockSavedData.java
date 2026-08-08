package net.favouriteless.enchanted.common.enchanted;

import net.favouriteless.enchanted.api.ISerializable;
import net.favouriteless.enchanted.common.Enchanted;
import net.favouriteless.enchanted.common.items.component.EntityRefData;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BedBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.saveddata.SavedData;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

/**
 * {@link BedTaglockSavedData} is where the UUID and name of the last player to use a {@link BedBlockEntity} is stored,
 * which is used for taglocks.
 */
public class BedTaglockSavedData extends SavedData {

    private static final String NAME = Enchanted.savedDataName("bed_taglocks");
    private final Map<BlockPos, BedTaglockData> entries = new HashMap<>();

    public BedTaglockSavedData() {
        super();
    }

    /**
     * @param bed The {@link BlockEntity} to grab data from.
     *
     * @return The {@link BedTaglockData} associated with a specific {@link BlockEntity}.
     */
    public BedTaglockData getEntry(BlockEntity bed) {
        return getEntry(bed.getBlockPos());
    }

    /**
     * @param level The {@link Level} to grab data from.
     *
     * @return An instance of {@link BedTaglockSavedData} belonging to level.
     */
    public static BedTaglockSavedData get(Level level) {
        if (level instanceof ServerLevel serverLevel) {
            return serverLevel.getDataStorage().computeIfAbsent(new Factory<>(BedTaglockSavedData::new, BedTaglockSavedData::load, null), NAME);
        } else {
            throw new RuntimeException("Game attempted to load serverside taglock (bed) data from a clientside world.");
        }
    }

    // -------------------- IMPLEMENTATION  DETAILS BELOW THIS POINT, NOT NEEDED FOR API USERS --------------------

    private BedTaglockData getEntry(BlockPos pos) {
        return entries.computeIfAbsent(pos, (_pos) -> new BedTaglockData());
    }

    private static BedTaglockSavedData load(CompoundTag nbt, Provider registries) {
        BedTaglockSavedData data = new BedTaglockSavedData();
        ListTag entryList = nbt.getList("entryList", Tag.TAG_COMPOUND);

        for (Tag e : entryList) {
            CompoundTag entryNbt = (CompoundTag) e; // This cast should be safe.
            data.getEntry(BlockPos.of(entryNbt.getLong("key"))).deserialize(entryNbt);
        }

        return data;
    }

    @Override
    public CompoundTag save(CompoundTag tag, Provider registries) {
        ListTag list = new ListTag();

        entries.forEach((pos, data) -> {
            if (data.getData() != null) {
                CompoundTag entryTag = data.serialize();
                entryTag.putLong("key", pos.asLong());
            }
        });

        tag.put("entries", list);
        return tag;
    }


    public static class BedTaglockData implements ISerializable<CompoundTag> {

        private EntityRefData data = null;

        private BedTaglockData() {
        }

        @Override
        public CompoundTag serialize() {
            CompoundTag tag = new CompoundTag();
            tag.put("data", EntityRefData.CODEC.encode(data, NbtOps.INSTANCE, new CompoundTag()).getOrThrow());
            return tag;
        }

        @Override
        public void deserialize(CompoundTag tag) {
            data = EntityRefData.CODEC.parse(NbtOps.INSTANCE, tag.get("data"))
                                      .resultOrPartial(e -> Enchanted.LOG.error("Tried to load invalid Taglock data: '{}'", e))
                                      .orElse(null);
        }

        public EntityRefData getData() {
            return data;
        }

        public void setData(@Nullable EntityRefData data) {
            this.data = data;
        }

    }

}