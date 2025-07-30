package net.favouriteless.enchanted.common.curses;

import net.favouriteless.enchanted.api.curses.CurseManager;
import net.favouriteless.enchanted.common.curses.curses.CurseMisfortune;
import net.favouriteless.enchanted.common.curses.curses.CurseOverheating;
import net.favouriteless.enchanted.common.curses.curses.CurseSinking;

public class ECurses {

    public static void load() {
        CurseManager manager = CurseManager.get();

        manager.register(CurseMisfortune.TYPE);
        manager.register(CurseOverheating.TYPE);
        manager.register(CurseSinking.TYPE);
    }

}
