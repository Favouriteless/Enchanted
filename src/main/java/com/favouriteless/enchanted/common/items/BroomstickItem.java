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

import com.favouriteless.enchanted.common.entities.BroomstickEntity;
import com.favouriteless.enchanted.common.init.EnchantedEntityTypes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;

public class BroomstickItem extends Item {

	public BroomstickItem(Properties pProperties) {
		super(pProperties);
	}

	@Override
	public boolean isFoil(ItemStack pStack) {
		return true;
	}

	@Override
	public ActionResultType useOn(ItemUseContext context) {
		if(context.getLevel().isClientSide) {
			return ActionResultType.SUCCESS;
		}
		else {
			BlockPos pos = context.getClickedPos().relative(context.getClickedFace());

			BroomstickEntity broom = EnchantedEntityTypes.BROOMSTICK.get().create(context.getLevel());
			broom.setPos(pos.getX()+0.5D, pos.getY(), pos.getZ()+0.5D);
			broom.setDeltaMovement(Vector3d.ZERO);
			broom.xo = pos.getX()+0.5D;
			broom.yo = pos.getY();
			broom.zo = pos.getZ()+0.5D;
			broom.yRot = context.getPlayer().yRot;
			context.getLevel().addFreshEntity(broom);

			if(!context.getPlayer().abilities.instabuild) // Player not in creative
				context.getItemInHand().shrink(1);
		}
		return ActionResultType.PASS;
	}
}
