package net.favouriteless.enchanted.api.curses;

import net.favouriteless.enchanted.common.curses.CurseManagerImpl;
import net.favouriteless.enchanted.common.curses.CurseType;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

import java.util.List;
import java.util.UUID;

public interface CurseManager {

    static CurseManager get() {
        return CurseManagerImpl.INSTANCE;
    }

    void createCurse(CurseType<?> type, ServerLevel level, UUID target, int strength);

    List<Curse> getCursesFor(ServerPlayer player);

    void removeCurse(ServerLevel level, Curse curse);

}
