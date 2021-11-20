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

package com.favouriteless.enchanted.common.capabilities.player;

import com.favouriteless.enchanted.Enchanted;
import com.favouriteless.enchanted.common.capabilities.SimpleCapabilityStorage;
import com.favouriteless.enchanted.common.capabilities.SimplePersistentCapabilityProvider;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.BedTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerSleepInBedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nonnull;

@Mod.EventBusSubscriber(modid=Enchanted.MOD_ID)
public class PlayerCapabilityManager {

    @CapabilityInject(IPlayerCapability.class)
    public static Capability<IPlayerCapability> INSTANCE = null;
    public static final ResourceLocation NAME = new ResourceLocation(Enchanted.MOD_ID, "player_bed");

    public PlayerCapabilityManager() {}

    public static void registerCapabilities() {
        CapabilityManager.INSTANCE.register(
                IPlayerCapability.class,
                SimpleCapabilityStorage.create(() -> INSTANCE, Constants.NBT.TAG_COMPOUND),
                PlayerCapability::new
        );
    }

    @SubscribeEvent
    public static void onAttachCapabilities(@Nonnull final AttachCapabilitiesEvent<TileEntity> event) {
        final TileEntity obj = event.getObject();
        if(obj instanceof BedTileEntity) {
            event.addCapability(NAME, SimplePersistentCapabilityProvider.from(INSTANCE, PlayerCapability::new));
        }
    }

    @SubscribeEvent
    public static void playerSleepInBed(PlayerSleepInBedEvent event){
        BlockPos pos = event.getPos();
        PlayerEntity player = event.getPlayer();
        if(pos != null){
            event.getPlayer().level.getBlockEntity(pos).getCapability(INSTANCE).ifPresent(source -> source.setValue(player.getUUID()));
        }
    }
}
