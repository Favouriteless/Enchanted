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

import com.favouriteless.enchanted.Enchanted;
import com.favouriteless.enchanted.common.init.PoppetColour;
import com.favouriteless.enchanted.common.items.TaglockFilledItem;
import com.favouriteless.enchanted.common.util.poppet.PoppetHelper;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

public abstract class AbstractPoppetItem extends Item {

	public final float failRate;
	public final PoppetColour colour;

	public AbstractPoppetItem(float failRate, int durability, PoppetColour colour) {
		super(new Item.Properties().tab(Enchanted.TAB).durability(durability));
		this.failRate = failRate;
		this.colour = colour;
	}

	public float getFailRate() {
		return this.failRate;
	}

	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, @Nullable World world, List<ITextComponent> toolTip, ITooltipFlag flag) {
		toolTip.add(new StringTextComponent((int)(failRate * 100) + "% Chance to fail").withStyle(TextFormatting.RED));
		if(PoppetHelper.isBound(stack)) {
			toolTip.add(new StringTextComponent(PoppetHelper.getBoundPlayer(stack, world).getDisplayName().getString()).withStyle(TextFormatting.GRAY));
		}
	}

	@Override
	public ItemStack finishUsingItem(ItemStack itemStack, World world, LivingEntity entity) {
		if(entity instanceof PlayerEntity) {
			PlayerEntity player = (PlayerEntity)entity;
			ItemStack taglockStack = player.getOffhandItem();

			if(taglockStack.getItem() instanceof TaglockFilledItem) {
				CompoundNBT nbt = taglockStack.getOrCreateTag();
				PlayerEntity target = world.getPlayerByUUID(nbt.getUUID("entity"));

				if(target != null) {
					PoppetHelper.bind(itemStack, target);
					if(!player.isCreative())
						taglockStack.shrink(1);
				}
			}
		}
		return itemStack;
	}

	@Override
	public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
		if(hand == Hand.MAIN_HAND) {
			if(!PoppetHelper.isBound(player.getMainHandItem())) {
				ItemStack taglockStack = player.getOffhandItem();
				if(taglockStack.getItem() instanceof TaglockFilledItem) {
					CompoundNBT nbt = taglockStack.getOrCreateTag();
					PlayerEntity target = world.getPlayerByUUID(nbt.getUUID("entity"));

					if(target != null)
						player.startUsingItem(hand);
				}
			}
		}
		return super.use(world, player, hand);
	}

	@Override
	public boolean isEnchantable(ItemStack stack) {
		return false;
	}

	@Override
	public UseAction getUseAnimation(ItemStack pStack) {
		return UseAction.BOW;
	}

	@Override
	public int getUseDuration(ItemStack item) {
		return 32;
	}
}
