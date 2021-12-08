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

package com.favouriteless.enchanted.common.items;

import com.favouriteless.enchanted.Enchanted;
import com.favouriteless.enchanted.common.blocks.chalk.ChalkCircleBlock;
import com.favouriteless.enchanted.common.init.EnchantedItems;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemModelsProperties;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@EventBusSubscriber(modid=Enchanted.MOD_ID, bus=Bus.MOD)
public class CircleTalismanItem extends Item {

	public CircleTalismanItem(Properties properties) {
		super(properties);
	}

	@Override
	public ActionResultType useOn(ItemUseContext context) {
		ItemStack stack = context.getItemInHand();
		byte small = stack.hasTag() ? stack.getTag().getByte("small") : -1;
		byte medium = stack.hasTag() ? stack.getTag().getByte("medium") : -1;
		byte large = stack.hasTag() ? stack.getTag().getByte("large") : -1;

		return ActionResultType.SUCCESS;
	}

	@SubscribeEvent
	public static void onClientSetup(FMLClientSetupEvent event) {
		event.enqueueWork(() ->
		{
			ItemModelsProperties.register(EnchantedItems.CIRCLE_TALISMAN.get(), new ResourceLocation(Enchanted.MOD_ID, "small"), (stack, world, living) -> (living != null && stack.hasTag()) ? (float)stack.getTag().getByte("small") : -1.0F);
			ItemModelsProperties.register(EnchantedItems.CIRCLE_TALISMAN.get(), new ResourceLocation(Enchanted.MOD_ID, "medium"), (stack, world, living) -> (living != null && stack.hasTag()) ? (float)stack.getTag().getByte("medium") : -1.0F);
			ItemModelsProperties.register(EnchantedItems.CIRCLE_TALISMAN.get(), new ResourceLocation(Enchanted.MOD_ID, "large"), (stack, world, living) -> (living != null && stack.hasTag()) ? (float)stack.getTag().getByte("large") : -1.0F);
		});
	}
}
