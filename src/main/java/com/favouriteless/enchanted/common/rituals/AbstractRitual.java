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

package com.favouriteless.enchanted.common.rituals;

import com.favouriteless.enchanted.common.blocks.chalk.ChalkCircleBlock;

import net.minecraft.client.renderer.texture.ITickable;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import java.util.*;

public abstract class AbstractRitual implements ITickable {

    public ChalkCircleBlock.ChalkColour[] CIRCLES_REQUIRED;
    public HashMap<EntityType<?>, Integer> ENTITIES_REQUIRED;
    public HashMap<Item, Integer> ITEMS_REQUIRED = new HashMap<Item, Integer>();

    public ServerWorld world; // World ritual started in
    public BlockPos pos; // Position ritual started at
    public UUID casterUUID; // Player who started ritual
    public UUID targetUUID; // Target of the ritual
    public int power; // Power required to start
    public int powerTick; // Power required per tick

    public AbstractRitual() { }

    public abstract void execute();
    public abstract void onTick();

    @Override
    public void tick() {

    }


}
