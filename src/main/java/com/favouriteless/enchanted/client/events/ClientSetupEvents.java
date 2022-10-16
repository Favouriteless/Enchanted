/*
 * Copyright (c) 2022. Favouriteless
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

package com.favouriteless.enchanted.client.events;

import com.favouriteless.enchanted.Enchanted;
import com.favouriteless.enchanted.client.render.tileentity.CauldronWaterRenderer;
import com.favouriteless.enchanted.client.render.tileentity.PoppetShelfRenderer;
import com.favouriteless.enchanted.client.render.tileentity.SpinningWheelRenderer;
import com.favouriteless.enchanted.client.screens.*;
import com.favouriteless.enchanted.common.init.EnchantedBlockEntityTypes;
import com.favouriteless.enchanted.common.init.EnchantedBlocks;
import com.favouriteless.enchanted.common.init.EnchantedContainers;
import com.favouriteless.enchanted.common.init.EnchantedEntityTypes;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@EventBusSubscriber(modid=Enchanted.MOD_ID, bus=Bus.MOD, value=Dist.CLIENT)
public class ClientSetupEvents {

	@SubscribeEvent
	public static void clientSetup(FMLClientSetupEvent event) {
		MenuScreens.register(EnchantedContainers.WITCH_OVEN.get(), WitchOvenScreen::new);
		MenuScreens.register(EnchantedContainers.DISTILLERY.get(), DistilleryScreen::new);
		MenuScreens.register(EnchantedContainers.ALTAR.get(), AltarScreen::new);
		MenuScreens.register(EnchantedContainers.SPINNING_WHEEL.get(), SpinningWheelScreen::new);
		MenuScreens.register(EnchantedContainers.POPPET_SHELF.get(), PoppetShelfScreen::new);

		EnchantedBlocks.initRender();
		EnchantedEntityTypes.registerEntityRenderers();

		ClientRegistry.bindTileEntityRenderer(EnchantedBlockEntityTypes.WITCH_CAULDRON.get(), dispatcher -> new CauldronWaterRenderer<>(dispatcher, 10));
		ClientRegistry.bindTileEntityRenderer(EnchantedBlockEntityTypes.KETTLE.get(), dispatcher -> new CauldronWaterRenderer<>(dispatcher, 8));
		ClientRegistry.bindTileEntityRenderer(EnchantedBlockEntityTypes.SPINNING_WHEEL.get(), SpinningWheelRenderer::new);
		ClientRegistry.bindTileEntityRenderer(EnchantedBlockEntityTypes.POPPET_SHELF.get(), PoppetShelfRenderer::new);
	}

	@SubscribeEvent
	public static void registerBlockColors(ColorHandlerEvent.Block event) {
		event.getBlockColors().register((a, b, c, d) -> 0xF0F0F0, EnchantedBlocks.CHALK_WHITE.get());
		event.getBlockColors().register((a, b, c, d) -> 0x801818, EnchantedBlocks.CHALK_RED.get());
		event.getBlockColors().register((a, b, c, d) -> 0x4F2F78, EnchantedBlocks.CHALK_PURPLE.get());
	}

}
