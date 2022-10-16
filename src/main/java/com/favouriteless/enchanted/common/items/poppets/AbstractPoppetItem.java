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
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.InteractionHand;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.ChatFormatting;
import net.minecraft.world.level.Level;
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
	public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> toolTip, TooltipFlag flag) {
		toolTip.add(new TextComponent((int)(failRate * 100) + "% Chance to fail").withStyle(ChatFormatting.RED));
		if(PoppetHelper.isBound(stack)) {
			toolTip.add(new TextComponent(PoppetHelper.getBoundPlayer(stack, world).getDisplayName().getString()).withStyle(ChatFormatting.GRAY));
		}
	}

	@Override
	public ItemStack finishUsingItem(ItemStack itemStack, Level world, LivingEntity entity) {
		if(entity instanceof Player) {
			Player player = (Player)entity;
			ItemStack taglockStack = player.getOffhandItem();

			if(taglockStack.getItem() instanceof TaglockFilledItem) {
				CompoundTag nbt = taglockStack.getOrCreateTag();
				Player target = world.getPlayerByUUID(nbt.getUUID("entity"));

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
	public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
		if(hand == InteractionHand.MAIN_HAND) {
			if(!PoppetHelper.isBound(player.getMainHandItem())) {
				ItemStack taglockStack = player.getOffhandItem();
				if(taglockStack.getItem() instanceof TaglockFilledItem) {
					CompoundTag nbt = taglockStack.getOrCreateTag();
					Player target = world.getPlayerByUUID(nbt.getUUID("entity"));

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
	public UseAnim getUseAnimation(ItemStack pStack) {
		return UseAnim.BOW;
	}

	@Override
	public int getUseDuration(ItemStack item) {
		return 32;
	}
}
