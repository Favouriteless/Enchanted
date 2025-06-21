package net.favouriteless.enchanted.common.mutandis;

import net.favouriteless.enchanted.common.curses.CurseSavedData;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MutatingSavedData extends SavedData {

    private static final String NAME = "enchanted_mutating_plants";
    private final Map<ChunkPos, List<BlockPos>> entries = new HashMap<>();

    public MutatingSavedData() {
        super();
    }



    public boolean isMutating(ServerLevel level, int x, int y, int z) {
        return get(level).entries.get()
    }

    /**
     * @param level The {@link Level} to access {@link MinecraftServer} from.
     *
     * @return An instance of {@link CurseSavedData} belonging to {@link Level#OVERWORLD} {@link Level}.
     */
    public static CurseSavedData get(Level level) {
        if(level instanceof ServerLevel)
            return level.getServer().overworld().getDataStorage().computeIfAbsent(new Factory<>(CurseSavedData::new, CurseSavedData::load, null), NAME);
        else
            throw new RuntimeException("Game attempted to load serverside curse data from a clientside world.");
    }


    @Override
    public CompoundTag save(CompoundTag tag, Provider registries) {
        return null;
    }

}
