package net.favouriteless.enchanted.common.enchanted.mutandis;

import it.unimi.dsi.fastutil.objects.Object2BooleanMap;
import it.unimi.dsi.fastutil.objects.Object2BooleanOpenHashMap;
import net.favouriteless.enchanted.common.Enchanted;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.saveddata.SavedData;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class MutagenSavedData extends SavedData {

    public static final String NAME = Enchanted.savedDataName("mutagens");

    private final Map<ChunkPos, Object2BooleanMap<BlockPos>> mutatingBlocks = new HashMap<>();

    public static MutagenSavedData get(ServerLevel level) {
        return level.getDataStorage().computeIfAbsent(new Factory<>(MutagenSavedData::new, MutagenSavedData::load, null), NAME);
    }

    public void add(BlockPos pos, boolean extremis) {
        mutatingBlocks.computeIfAbsent(new ChunkPos(pos), k -> new Object2BooleanOpenHashMap<>()).put(pos, extremis);
        setDirty();
    }

    public void remove(BlockPos pos) {
        Object2BooleanMap<BlockPos> map = mutatingBlocks.get(new ChunkPos(pos));
        if(map != null) {
            map.removeBoolean(pos);
            setDirty();
        }
    }

    public boolean contains(BlockPos pos) {
        Object2BooleanMap<BlockPos> map = mutatingBlocks.get(new ChunkPos(pos));
        return map != null && map.containsKey(pos);
    }

    public boolean isExtremis(BlockPos pos) {
        Object2BooleanMap<BlockPos> map = mutatingBlocks.get(new ChunkPos(pos));
        return map != null && map.getBoolean(pos);
    }

    @Override
    public CompoundTag save(CompoundTag nbt, Provider registries) {
        ListTag chunkList = new ListTag();

        for(Entry<ChunkPos, Object2BooleanMap<BlockPos>> entry : mutatingBlocks.entrySet()) {
            if(entry.getValue().isEmpty())
                continue;

            ListTag list = new ListTag();
            for(Object2BooleanMap.Entry<BlockPos> e : entry.getValue().object2BooleanEntrySet()) {
                CompoundTag tag = new CompoundTag();
                tag.putLong("pos", e.getKey().asLong());
                tag.putBoolean("extremis", e.getBooleanValue());
                list.add(tag);
            }

            CompoundTag chunk = new CompoundTag();
            chunk.putLong("chunk", entry.getKey().toLong());
            chunk.put("positions", list);
            chunkList.add(chunk);
        }
        nbt.put("chunks", chunkList);
        return nbt;
    }

    public static MutagenSavedData load(CompoundTag nbt, Provider provider) {
        MutagenSavedData data = new MutagenSavedData();

        ListTag chunkList = nbt.getList("chunks", ListTag.TAG_COMPOUND);

        for(Tag t : chunkList) {
            CompoundTag chunk = (CompoundTag)t;

            Object2BooleanMap<BlockPos> posMap = new Object2BooleanOpenHashMap<>();
            for(Tag p : chunk.getList("positions", ListTag.TAG_LONG)) {
                CompoundTag pos = (CompoundTag)p;
                posMap.put(BlockPos.of(pos.getLong("pos")), pos.getBoolean("extremis"));
            }

            ChunkPos pos = new ChunkPos(nbt.getLong("chunk"));
            data.mutatingBlocks.put(pos, posMap);
        }

        return data;
    }

}
