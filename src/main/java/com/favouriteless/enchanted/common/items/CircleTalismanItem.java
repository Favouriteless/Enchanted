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

package com.favouriteless.enchanted.common.items;

import com.favouriteless.enchanted.Enchanted;
import com.favouriteless.enchanted.common.init.EnchantedBlocks;
import com.favouriteless.enchanted.common.init.EnchantedItems;
import com.favouriteless.enchanted.common.util.CirclePart;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
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
		World world = context.getLevel();
		BlockPos pos = context.getClickedPos().above();
		ItemStack stack = context.getItemInHand();


		if(stack.hasTag()) {
			CompoundNBT nbt = stack.getTag();
			byte small = nbt.contains("small") ? nbt.getByte("small") : 0;
			byte medium = nbt.contains("medium") ? nbt.getByte("medium") : 0;
			byte large = nbt.contains("large") ? nbt.getByte("large") : 0;

			if(small != 0 || medium != 0 || large != 0) {
				boolean validPlace = world.getBlockState(pos).isAir() && EnchantedBlocks.CHALK_GOLD.get().canSurvive(null, world, pos);
				if(validPlace) {
					if(small != 0 && !CirclePart.SMALL.canPlace(world, pos)) validPlace = false;
					if(medium != 0 && !CirclePart.MEDIUM.canPlace(world, pos)) validPlace = false;
					if(large != 0 && !CirclePart.LARGE.canPlace(world, pos)) validPlace = false;
				}

				if(validPlace) {
					if(!world.isClientSide) {
						world.setBlockAndUpdate(pos, EnchantedBlocks.CHALK_GOLD.get().getStateForPlacement(new BlockItemUseContext(context)));
						if(small != 0)
							CirclePart.SMALL.place(world, pos, small == 1 ? EnchantedBlocks.CHALK_WHITE.get() : small == 2 ? EnchantedBlocks.CHALK_RED.get() : EnchantedBlocks.CHALK_PURPLE.get(), context);
						if(medium != 0)
							CirclePart.MEDIUM.place(world, pos, medium == 1 ? EnchantedBlocks.CHALK_WHITE.get() : medium == 2 ? EnchantedBlocks.CHALK_RED.get() : EnchantedBlocks.CHALK_PURPLE.get(), context);
						if(large != 0)
							CirclePart.LARGE.place(world, pos, large == 1 ? EnchantedBlocks.CHALK_WHITE.get() : large == 2 ? EnchantedBlocks.CHALK_RED.get() : EnchantedBlocks.CHALK_PURPLE.get(), context);

						stack.setTag(new CompoundNBT());
					}

					return ActionResultType.SUCCESS;
				}
				else {
					if(context.getPlayer() != null) {
						context.getPlayer().displayClientMessage(new StringTextComponent("All blocks must be valid spots.").withStyle(TextFormatting.RED), true);
					}
				}
			}
		}

		return ActionResultType.FAIL;
	}

	@SubscribeEvent
	public static void onClientSetup(FMLClientSetupEvent event) {
		event.enqueueWork(() ->
		{
			ItemModelsProperties.register(EnchantedItems.CIRCLE_TALISMAN.get(), new ResourceLocation(Enchanted.MOD_ID, "small"), (stack, world, living) -> stack.hasTag() ? stack.getTag().getByte("small")*0.3F : 0F);
			ItemModelsProperties.register(EnchantedItems.CIRCLE_TALISMAN.get(), new ResourceLocation(Enchanted.MOD_ID, "medium"), (stack, world, living) -> stack.hasTag() ? stack.getTag().getByte("medium")*0.3F : 0F);
			ItemModelsProperties.register(EnchantedItems.CIRCLE_TALISMAN.get(), new ResourceLocation(Enchanted.MOD_ID, "large"), (stack, world, living) -> stack.hasTag() ? stack.getTag().getByte("large")*0.3F : 0F);
		});
	}
}
