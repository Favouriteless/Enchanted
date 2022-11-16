/*
 *
 *   Copyright (c) 2022. Favouriteless
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

package com.favouriteless.enchanted.common.events;

import com.favouriteless.enchanted.Enchanted;
import com.favouriteless.enchanted.EnchantedConfig;
import com.favouriteless.enchanted.common.init.EnchantedItems;
import com.favouriteless.enchanted.common.init.EnchantedTags;
import com.favouriteless.enchanted.common.items.poppets.AbstractDeathPoppetItem;
import com.favouriteless.enchanted.common.items.poppets.AbstractPoppetItem;
import com.favouriteless.enchanted.common.items.poppets.ItemProtectionPoppetItem;
import com.favouriteless.enchanted.common.util.poppet.PoppetHelper;
import com.favouriteless.enchanted.common.util.poppet.PoppetShelfManager;
import com.favouriteless.enchanted.common.util.poppet.PoppetShelfSavedData.PoppetEntry;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerDestroyItemEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;

@EventBusSubscriber(modid=Enchanted.MOD_ID, bus=Bus.FORGE)
public class PoppetEvents {

	@SubscribeEvent
	public static void onEntityHurt(LivingHurtEvent event) {
		if(event.getEntity() instanceof Player player) {
			if(event.getAmount() >= player.getHealth()) { // Player would be killed by damage

				DamageSource source = event.getSource();

				Queue<ItemStack> poppetQueue = new PriorityQueue<>(new PoppetComparator());
				for(ItemStack itemStack : player.getInventory().items)
					if(itemStack.getItem() instanceof AbstractDeathPoppetItem)
						if(((AbstractDeathPoppetItem)itemStack.getItem()).protectsAgainst(source))
							poppetQueue.add(itemStack);

				boolean cancel = PoppetHelper.tryUseDeathPoppetQueue(poppetQueue, player);
				if(!cancel) {
					Queue<PoppetEntry> poppetEntryQueue = new PriorityQueue<>(new PoppetEntryComparator());
					for(PoppetEntry entry : PoppetShelfManager.getEntriesFor(player))
						if(entry.item().getItem() instanceof AbstractDeathPoppetItem)
							if(((AbstractDeathPoppetItem)entry.item().getItem()).protectsAgainst(source))
								poppetEntryQueue.add(entry);
					cancel = PoppetHelper.tryUseDeathPoppetEntryQueue(poppetEntryQueue, player);
				}


				event.setCanceled(cancel);
			}
		}
	}

	@SubscribeEvent
	public static void onItemBreak(PlayerDestroyItemEvent event) {
		ItemStack tool = event.getOriginal();
		if(!ForgeRegistries.ITEMS.tags().getTag(EnchantedTags.Items.TOOL_POPPET_BLACKLIST).contains(tool.getItem())
				&& !(EnchantedConfig.WHITELIST_TOOL_POPPET.get() && !ForgeRegistries.ITEMS.tags().getTag(EnchantedTags.Items.TOOL_POPPET_WHITELIST).contains(tool.getItem()))) {
			Player player = event.getPlayer();

			Queue<ItemStack> poppetQueue = new PriorityQueue<>(new PoppetComparator());
			for(ItemStack itemStack : player.getInventory().items)
				if(EnchantedItems.isToolPoppet(itemStack.getItem()))
					poppetQueue.add(itemStack);

			boolean canceled = PoppetHelper.tryUseItemProtectionPoppetQueue(poppetQueue, player, tool);
			if(!canceled) {
				Queue<PoppetEntry> poppetEntryQueue = new PriorityQueue<>(new PoppetEntryComparator());
				for(PoppetEntry entry : PoppetShelfManager.getEntriesFor(player))
					if(EnchantedItems.isToolPoppet(entry.item().getItem()))
						poppetEntryQueue.add(entry);
				canceled = PoppetHelper.tryUseItemProtectionPoppetEntryQueue(poppetEntryQueue, player, tool);
			}

			if(canceled)
				event.getPlayer().setItemInHand(event.getHand(), tool);
		}
	}

	public static void onLivingEntityBreak(LivingEntity entity, EquipmentSlot slot) {
		if(entity instanceof Player player) {
			ItemStack armourStack = entity.getItemBySlot(slot).copy();
			if(!ForgeRegistries.ITEMS.tags().getTag(EnchantedTags.Items.ARMOUR_POPPET_BLACKLIST).contains(armourStack.getItem())
					&& !(EnchantedConfig.WHITELIST_ARMOUR_POPPET.get() && !ForgeRegistries.ITEMS.tags().getTag(EnchantedTags.Items.ARMOUR_POPPET_WHITELIST).contains(armourStack.getItem()))) {

				Queue<ItemStack> poppetQueue = new PriorityQueue<>(new PoppetComparator());
				for(ItemStack itemStack : player.getInventory().items)
					if(itemStack.getItem() instanceof ItemProtectionPoppetItem)
						if(EnchantedItems.isArmourPoppet(itemStack.getItem()))
							poppetQueue.add(itemStack);

				boolean canceled = PoppetHelper.tryUseItemProtectionPoppetQueue(poppetQueue, player, armourStack);
				if(!canceled) {
					Queue<PoppetEntry> poppetEntryQueue = new PriorityQueue<>(new PoppetEntryComparator());
					for(PoppetEntry entry : PoppetShelfManager.getEntriesFor(player))
						if(EnchantedItems.isArmourPoppet(armourStack.getItem()))
							poppetEntryQueue.add(entry);
					canceled = PoppetHelper.tryUseItemProtectionPoppetEntryQueue(poppetEntryQueue, player, armourStack);
				}

				if(canceled)
					player.setItemSlot(slot, armourStack);
			}
		}
	}

	private static class PoppetComparator implements Comparator<ItemStack> {
		@Override
		public int compare(ItemStack o1, ItemStack o2) {
			if(!(o1.getItem() instanceof AbstractPoppetItem) || !(o1.getItem() instanceof AbstractPoppetItem))
				throw new IllegalStateException("Non-poppet item inside the poppet use queue");
			return Math.round(Math.signum(((AbstractPoppetItem)o1.getItem()).failRate - ((AbstractPoppetItem)o2.getItem()).failRate));
		}
	}

	private static class PoppetEntryComparator implements Comparator<PoppetEntry> {
		@Override
		public int compare(PoppetEntry o1, PoppetEntry o2) {
			return Math.round(Math.signum(((AbstractPoppetItem)o1.item().getItem()).failRate - ((AbstractPoppetItem)o2.item().getItem()).failRate));
		}
	}

}
