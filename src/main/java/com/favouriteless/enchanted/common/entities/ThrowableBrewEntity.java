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

package com.favouriteless.enchanted.common.entities;

import com.favouriteless.enchanted.common.init.EnchantedItems;
import com.favouriteless.enchanted.common.items.brews.ThrowableBrewItem;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;

public class ThrowableBrewEntity extends ThrowableItemProjectile {

	public ThrowableBrewEntity(EntityType<? extends ThrowableBrewEntity> type, Level level) {
		super(type, level);
	}

	@Override
	protected Item getDefaultItem() {
		return EnchantedItems.BREW_OF_LOVE.get();
	}

	@Override
	protected float getGravity() {
		return 0.05F;
	}

	@Override
	protected void onHitBlock(BlockHitResult result) {
		super.onHitBlock(result);
		if (!this.level.isClientSide) {
			ItemStack itemstack = this.getItem();
			if(itemstack.getItem() instanceof ThrowableBrewItem item) {
				item.applyEffect(getOwner(), level, result.getLocation());
				this.level.levelEvent(LevelEvent.PARTICLES_SPELL_POTION_SPLASH, this.blockPosition(), item.getColour());
			}
			discard();
		}
	}

	@Override
	protected void onHitEntity(EntityHitResult result) {
		super.onHitEntity(result);
		if (!this.level.isClientSide) {
			ItemStack itemstack = this.getItem();
			if(itemstack.getItem() instanceof ThrowableBrewItem item) {
				item.applyEffect(getOwner(), level, result.getLocation());
				this.level.levelEvent(LevelEvent.PARTICLES_SPELL_POTION_SPLASH, this.blockPosition(), item.getColour());
			}
			discard();
		}
	}

}
