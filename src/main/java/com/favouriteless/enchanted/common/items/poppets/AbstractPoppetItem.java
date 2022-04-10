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

package com.favouriteless.enchanted.common.items.poppets;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Predicate;

public abstract class AbstractPoppetItem extends Item {

	protected float failRate;
	protected final Predicate<DamageSource> sourcePredicate;

	public AbstractPoppetItem(Properties properties, float failRate, Predicate<DamageSource> sourcePredicate) {
		super(properties);
		this.failRate = failRate;
		this.sourcePredicate = sourcePredicate;
	}

	/**
	 * True if this poppet can protect against the specified DamageSource
	 * @param damageSource
	 * @return
	 */
	public boolean protectsAgainst(DamageSource damageSource) {
		return sourcePredicate.test(damageSource);
	}

	/**
	 * True if extra requirements are met (for example, random chance)
	 * @return
	 */
	public abstract boolean canProtect(PlayerEntity player);

	/**
	 * Apply protection effects to the given player
	 * @param player
	 */
	public abstract void protect(PlayerEntity player);

	public float getFailRate() {
		return this.failRate;
	}

	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, @Nullable World world, List<ITextComponent> toolTip, ITooltipFlag flag) {
		toolTip.add(new StringTextComponent((int)(failRate * 100) + "% Chance to fail").withStyle(TextFormatting.RED));
		if(PoppetUtils.isBound(stack)) {
			toolTip.add(new StringTextComponent(PoppetUtils.getBoundPlayer(stack, world).getDisplayName().getString()).withStyle(TextFormatting.GRAY));
		}
	}

}
