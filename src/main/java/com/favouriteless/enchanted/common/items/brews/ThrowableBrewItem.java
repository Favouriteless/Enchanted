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

package com.favouriteless.enchanted.common.items.brews;

import com.favouriteless.enchanted.common.entities.ThrowableBrewEntity;
import com.favouriteless.enchanted.common.init.EnchantedEntityTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public abstract class ThrowableBrewItem extends Item {

	public ThrowableBrewItem(Properties properties) {
		super(properties);
	}

	public abstract void applyEffect(Entity owner, Level level, Vec3 pos);

	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.SPLASH_POTION_THROW, SoundSource.PLAYERS, 0.5F, 0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F));

		ItemStack itemstack = player.getItemInHand(hand);
		if (!level.isClientSide) {
			ThrowableBrewEntity entity = EnchantedEntityTypes.THROWABLE_BREW.get().create(level);
			entity.setOwner(player);
			entity.setPos(player.getEyePosition().add(0.0D, -0.1D, 0.0D));
			entity.setItem(itemstack);
			entity.shootFromRotation(player, player.getXRot(), player.getYRot(), -20.0F, 0.5F, 1.0F);
			level.addFreshEntity(entity);
		}

		player.awardStat(Stats.ITEM_USED.get(this));
		if (!player.getAbilities().instabuild) {
			itemstack.shrink(1);
		}

		return InteractionResultHolder.sidedSuccess(itemstack, level.isClientSide());
	}

	public abstract int getColour();

}
