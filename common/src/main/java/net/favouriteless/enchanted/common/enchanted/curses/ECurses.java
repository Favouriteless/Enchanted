package net.favouriteless.enchanted.common.enchanted.curses;

import net.favouriteless.enchanted.api.curses.CurseManager;
import net.favouriteless.enchanted.common.enchanted.curses.curses.CurseClumsy;
import net.favouriteless.enchanted.common.enchanted.curses.curses.CurseMisfortune;
import net.favouriteless.enchanted.common.enchanted.curses.curses.CurseOverheating;
import net.favouriteless.enchanted.common.enchanted.curses.curses.CurseSinking;

public class ECurses {

    public static void load() {
        CurseManager manager = CurseManager.get();

        manager.register(CurseClumsy.TYPE);
        manager.register(CurseMisfortune.TYPE);
        manager.register(CurseOverheating.TYPE);
        manager.register(CurseSinking.TYPE);
    }

}
