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

package com.favouriteless.enchanted.client.events;

import com.favouriteless.enchanted.Enchanted;
import com.favouriteless.enchanted.client.render.blockentity.CauldronWaterRenderer;
import com.favouriteless.enchanted.client.render.blockentity.PoppetShelfRenderer;
import com.favouriteless.enchanted.client.render.blockentity.SpinningWheelRenderer;
import com.favouriteless.enchanted.client.render.entity.BroomstickRenderer;
import com.favouriteless.enchanted.client.render.entity.ent.EntRenderer;
import com.favouriteless.enchanted.client.render.entity.FamiliarCatRenderer;
import com.favouriteless.enchanted.client.render.entity.SimpleAnimatedGeoRenderer;
import com.favouriteless.enchanted.client.render.model.BroomstickModel;
import com.favouriteless.enchanted.client.render.model.ModelLayerLocations;
import com.favouriteless.enchanted.client.render.model.armor.EarmuffsModel;
import com.favouriteless.enchanted.client.screens.*;
import com.favouriteless.enchanted.common.init.*;
import com.favouriteless.enchanted.common.init.registry.EnchantedBlockEntityTypes;
import com.favouriteless.enchanted.common.init.registry.EnchantedBlocks;
import com.favouriteless.enchanted.common.init.registry.EnchantedEntityTypes;
import com.favouriteless.enchanted.common.init.registry.EnchantedMenus;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.EntityRenderersEvent.RegisterLayerDefinitions;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class ClientSetupEvents {

	@SubscribeEvent
	public static void registerLayerDefinitions(RegisterLayerDefinitions event) {
		event.registerLayerDefinition(ModelLayerLocations.BROOMSTICK, BroomstickModel::createLayerDefinition);
		event.registerLayerDefinition(ModelLayerLocations.SPINNING_WHEEL, SpinningWheelRenderer::createLayerDefinition);
		event.registerLayerDefinition(ModelLayerLocations.EARMUFFS, EarmuffsModel::createLayerDefinition);
	}

}
