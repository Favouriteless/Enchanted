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

package com.favouriteless.enchanted.common.items;

import com.favouriteless.enchanted.common.entities.BroomstickEntity;
import com.favouriteless.enchanted.common.init.EnchantedEntityTypes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.InteractionResult;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;

import net.minecraft.world.item.Item.Properties;

public class BroomstickItem extends Item {

	public BroomstickItem(Properties pProperties) {
		super(pProperties);
	}

	@Override
	public boolean isFoil(ItemStack pStack) {
		return true;
	}

	@Override
	public InteractionResult useOn(UseOnContext context) {
		if(context.getLevel().isClientSide) {
			return InteractionResult.SUCCESS;
		}
		else {
			BlockPos pos = context.getClickedPos().relative(context.getClickedFace());

			BroomstickEntity broom = EnchantedEntityTypes.BROOMSTICK.get().create(context.getLevel());
			broom.setPos(pos.getX()+0.5D, pos.getY(), pos.getZ()+0.5D);
			broom.setDeltaMovement(Vec3.ZERO);
			broom.xo = pos.getX()+0.5D;
			broom.yo = pos.getY();
			broom.zo = pos.getZ()+0.5D;
			broom.setYRot(context.getPlayer().getYRot());;
			context.getLevel().addFreshEntity(broom);

			if(!context.getPlayer().getAbilities().instabuild) // Player not in creative
				context.getItemInHand().shrink(1);
		}
		return InteractionResult.PASS;
	}
}
