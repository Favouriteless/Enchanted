/*
 *
 *   Copyright (c) 2022. Favouriteless
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

package com.favouriteless.enchanted.api.capabilities.bed;

import com.favouriteless.enchanted.Enchanted;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BedBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerSleepInBedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;

@Mod.EventBusSubscriber(modid=Enchanted.MOD_ID)
public class BedPlayerCapabilityManager {

    public static Capability<IBedPlayerCapability> INSTANCE = CapabilityManager.get(new CapabilityToken<>() {});
    public static final ResourceLocation NAME = Enchanted.location("player_bed");

    public BedPlayerCapabilityManager() {}

    @SubscribeEvent
    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.register(BedPlayerCapability.class);
    }

    @SubscribeEvent
    public static void onAttachCapabilities(@Nonnull final AttachCapabilitiesEvent<BlockEntity> event) {
        final BlockEntity obj = event.getObject();
        if(obj instanceof BedBlockEntity) {
            event.addCapability(NAME, new BedPlayerCapabilityProvider());
        }
    }

    @SubscribeEvent
    public static void onPlayerSleeping(PlayerSleepInBedEvent event){
        BlockPos pos = event.getPos();
        Player player = event.getPlayer();
        if(pos != null){
            event.getPlayer().level.getBlockEntity(pos).getCapability(INSTANCE).ifPresent(source ->  {
                source.setUUID(player.getUUID());
                source.setName(player.getDisplayName().getString());
            });
        }
    }

    public static class BedPlayerCapabilityProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {

        private final IBedPlayerCapability backend = new BedPlayerCapability();
        private final LazyOptional<IBedPlayerCapability> optionalData = LazyOptional.of(() -> backend);

        @NotNull
        @Override
        public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
            return BedPlayerCapabilityManager.INSTANCE.orEmpty(cap, optionalData);
        }

        @Override
        public CompoundTag serializeNBT() {
            return backend.serializeNBT();
        }

        @Override
        public void deserializeNBT(CompoundTag nbt) {
            backend.deserializeNBT(nbt);
        }

    }

}
