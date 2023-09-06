/*
 *
 *   Copyright (c) 2023. Favouriteless
 *   Enchanted, a minecraft mod.
 *   GNU GPLv3 License
 *
 *       This file is part of Enchanted.
 *
 *       Enchanted is free software: you can redistribute it and/or modify
 *       it under the terms of the GNU General Public License as published by
 *       the Free Software Foundation, either version 3 of the License, or
 *       (at your option) any later version.
 *
 *       Enchanted is distributed in the hope that it will be useful,
 *       but WITHOUT ANY WARRANTY; without even the implied warranty of
 *       MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *       GNU General Public License for more details.
 *
 *       You should have received a copy of the GNU General Public License
 *       along with Enchanted.  If not, see <https://www.gnu.org/licenses/>.
 *
 *
 */

package com.favouriteless.enchanted.common.capabilities.bed;

import com.favouriteless.enchanted.Enchanted;
import com.favouriteless.enchanted.api.capabilities.EnchantedCapabilities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BedBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerSleepInBedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

import javax.annotation.Nonnull;

@EventBusSubscriber(modid=Enchanted.MOD_ID, bus=Bus.FORGE)
public class BedPlayerCapabilityManager {

    public BedPlayerCapabilityManager() {}

    @SubscribeEvent
    public static void onAttachCapabilities(@Nonnull final AttachCapabilitiesEvent<BlockEntity> event) {
        final BlockEntity obj = event.getObject();
        if(obj instanceof BedBlockEntity) {
            event.addCapability(Enchanted.location("bed"), new BedPlayerCapabilityProvider());
        }
    }

    @SubscribeEvent
    public static void onPlayerSleeping(PlayerSleepInBedEvent event){
        BlockPos pos = event.getPos();
        Player player = event.getPlayer();
        if(pos != null) {
            event.getPlayer().level.getBlockEntity(pos).getCapability(EnchantedCapabilities.BED).ifPresent(source ->  {
                source.setUUID(player.getUUID());
                source.setName(player.getDisplayName().getString());
            });
        }
    }

}
