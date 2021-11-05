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

package com.favouriteless.enchanted.common.events;

import com.favouriteless.enchanted.Enchanted;
import com.favouriteless.enchanted.common.tileentity.AltarTileEntity;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(modid=Enchanted.MOD_ID, bus=Mod.EventBusSubscriber.Bus.FORGE)
public class AltarEvents {

    public static List<WeakReference<AltarTileEntity>> LISTENERS = new ArrayList<>();

    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        for(WeakReference<AltarTileEntity> tileEntity : LISTENERS) {
            if(tileEntity.get().posWithinRange(event.getPos())) {
                tileEntity.get().removePower(event.getState().getBlock());
            }
        }
    }

    @SubscribeEvent
    public static void onBlockPlace(BlockEvent.EntityPlaceEvent event) {
        for(WeakReference<AltarTileEntity> tileEntity : LISTENERS) {
            if(tileEntity.get().posWithinRange(event.getPos())) {
                tileEntity.get().addPower(event.getState().getBlock());
            }
        }
    }

}
