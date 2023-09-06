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

package com.favouriteless.enchanted.common.capabilities;

import com.favouriteless.enchanted.Enchanted;
import com.favouriteless.enchanted.api.capabilities.IBedCapability;
import com.favouriteless.enchanted.api.capabilities.IFamiliarCapability;
import com.favouriteless.enchanted.common.capabilities.bed.BedCapabilityImpl;
import com.favouriteless.enchanted.common.capabilities.bed.BedCapabilityProvider;
import com.favouriteless.enchanted.common.familiars.FamiliarCapabilityImpl;
import com.favouriteless.enchanted.common.familiars.FamiliarCapabilityProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BedBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(modid=Enchanted.MOD_ID, bus=Bus.FORGE)
public class CapabilityEvents {

	@SubscribeEvent
	public static void registerCapabilities(RegisterCapabilitiesEvent event) {
		event.register(BedCapabilityImpl.class);
		event.register(FamiliarCapabilityImpl.class);
	}

	@SubscribeEvent
	public static void onAttachCapabilitiesBE(AttachCapabilitiesEvent<BlockEntity> event) {
		BlockEntity be = event.getObject();
		if(be instanceof BedBlockEntity)
			event.addCapability(IBedCapability.LOCATION, new BedCapabilityProvider());
	}

	@SubscribeEvent
	public static void onAttachCapabilitiesEntity(AttachCapabilitiesEvent<Entity> event) {
		Entity entity = event.getObject();

		if(entity instanceof Player)
			event.addCapability(IFamiliarCapability.LOCATION, new FamiliarCapabilityProvider());
	}

}
