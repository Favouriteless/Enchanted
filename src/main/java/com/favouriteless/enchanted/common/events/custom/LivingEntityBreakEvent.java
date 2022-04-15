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

package com.favouriteless.enchanted.common.events.custom;

import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingEvent;

/**
 * Called from LivingEntity#broadcastBreakEvent whenever an Item in the entity's armour or hand slots is broken
 */
public class LivingEntityBreakEvent extends LivingEvent {

	private final EquipmentSlotType slot;
	private final ItemStack itemStack;

	public LivingEntityBreakEvent(LivingEntity entity, EquipmentSlotType slot) {
		super(entity);
		this.slot = slot;
		this.itemStack = entity.getItemBySlot(slot).copy();
	}

	public EquipmentSlotType getSlot() {
		return slot;
	}

	public ItemStack getItemStack() {
		return itemStack;
	}

}
