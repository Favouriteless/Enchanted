package net.favouriteless.enchanted.common.enchanted.curses;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.favouriteless.enchanted.api.curses.CurseInstance;
import net.favouriteless.enchanted.common.Enchanted;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.core.UUIDUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.saveddata.SavedData;

import java.util.*;

public class CurseSavedData extends SavedData {

    private static final String NAME = Enchanted.savedDataName("curses");
    private static final Codec<CurseSavedData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.unboundedMap(UUIDUtil.STRING_CODEC, CurseInstance.codec().listOf().xmap(i -> (List<CurseInstance>) new ArrayList<>(i), l -> l)).fieldOf("entries").forGetter(data -> data.entries)
    ).apply(instance, CurseSavedData::new));

    private final Map<UUID, List<CurseInstance>> entries;

    public CurseSavedData(Map<UUID, List<CurseInstance>> entries) {
        super();
        this.entries = new HashMap<>(entries);
    }

    public CurseSavedData() {
        this(new HashMap<>());
    }

    public static CurseSavedData get(ServerLevel level) {
        return level.getServer().overworld().getDataStorage().computeIfAbsent(new Factory<>(CurseSavedData::new, CurseSavedData::load, null), NAME);
    }

    public List<CurseInstance> get(ServerPlayer player) {
        return get(player.getUUID());
    }

    public List<CurseInstance> get(UUID uuid) {
        return entries.computeIfAbsent(uuid, k -> new ArrayList<>());
    }

    // ----------------------------------------- Non-API implementations below -----------------------------------------

    private static CurseSavedData load(CompoundTag nbt, Provider registries) {
        return CODEC.parse(NbtOps.INSTANCE, nbt.get("data"))
                    .resultOrPartial(s -> Enchanted.LOG.error("Failed to load curses, discarding."))
                    .orElse(new CurseSavedData());
    }

    @Override
    public CompoundTag save(CompoundTag nbt, Provider registries) {
        nbt.put("data", CODEC.encodeStart(NbtOps.INSTANCE, this).getOrThrow());
        return nbt;
    }

}