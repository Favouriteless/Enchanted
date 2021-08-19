/*
 * Copyright (c) 2021. Favouriteless
 * Enchanted, a minecraft mod.
 * GNU GPLv3 License
 *
 *     This file is part of Enchanted.
 *
 *     Enchanted is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Enchanted is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Enchanted.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.favouriteless.enchanted.core.util;

public enum SetBlockStateFlag {
    /**
     * Sets a block state into this world.Flags are as follows:
     * 1 will cause a block update.
     * 2 will send the change to clients.
     * 4 will prevent the block from being re-rendered.
     * 8 will force any re-renders to run on the main thread instead
     * 16 will prevent neighbor reactions (e.g. fences connecting, observers pulsing).
     * 32 will prevent neighbor reactions from spawning drops.
     * 64 will signify the block is being moved.
     * Flags can be OR-ed
     */

    BLOCK_UPDATE(1),
    SEND_TO_CLIENTS(2),
    DO_NOT_RENDER(4),
    RUN_RENDER_ON_MAIN_THREAD(8),
    PREVENT_NEIGHBOUR_REACTIONS(16),
    NEIGHBOUR_REACTIONS_DONT_SPAWN_DROPS(32),
    BLOCK_IS_BEING_MOVED(64);

    public static int get(SetBlockStateFlag... flags) {
        int result = 0;
        for (SetBlockStateFlag flag : flags) {
            result |= flag.flagValue;
        }
        return result;
    }

    SetBlockStateFlag(int flagValue) {this.flagValue = flagValue;}
    private int flagValue;
}
