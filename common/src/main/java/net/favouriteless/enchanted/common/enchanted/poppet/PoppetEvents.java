package net.favouriteless.enchanted.common.enchanted.poppet;

import net.favouriteless.enchanted.common.init.EItems;
import net.favouriteless.enchanted.common.init.ETags.Items;
import net.favouriteless.enchanted.common.items.poppets.DeathPoppetItem;
import net.favouriteless.enchanted.common.enchanted.poppet.PoppetUseResult.ResultType;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class PoppetEvents {

	/**
	 * @return True if event should be cancelled.
	 */
	public static boolean onLivingEntityHurt(LivingEntity entity, float amount, DamageSource source) {
		if(entity instanceof Player player) {
			if(amount >= player.getHealth()) { // Player would be killed by damage
				PoppetUseResult result = PoppetUtils.tryUseItems(PoppetUtils.getPoppetQueue(player,
						item -> item instanceof DeathPoppetItem poppet && poppet.protectsAgainst(source)), player);
				if(result.type() != ResultType.PASS)
					return result.isSuccess();

				return PoppetUtils.tryUseEntries(PoppetUtils.getPoppetQueue(PoppetShelfManager.getEntriesFor(player),
								entry -> entry.item().getItem() instanceof DeathPoppetItem poppet &&
										poppet.protectsAgainst(source)), player).isSuccess();
			}
		}
		return false;
	}

	public static void onPlayerItemBreak(Player player, ItemStack item, InteractionHand hand) {
		if(item.is(Items.TOOL_POPPET_WHITELIST) && !item.is(Items.TOOL_POPPET_BLACKLIST)) {
			PoppetUseResult result = PoppetUtils.tryUseItems(PoppetUtils.getPoppetQueue(player, EItems::isToolPoppet), player, item);
			if(result.type() == ResultType.FAIL)
				return;

			boolean cancelled = result.isSuccess();

			if(!cancelled)
				cancelled = PoppetUtils.tryUseEntries(PoppetUtils.getPoppetQueue(PoppetShelfManager.getEntriesFor(player),
						entry -> EItems.isToolPoppet(entry.item().getItem())), player, item).isSuccess();

			if(cancelled)
				player.setItemInHand(hand, item);
		}
	}

	public static boolean onArmourHurt(LivingEntity entity, EquipmentSlot slot, ItemStack item, float damage) {
		if(entity instanceof Player player) {
 			if((item.getMaxDamage() - item.getDamageValue()) <= damage && !item.is(Items.ARMOR_POPPET_BLACKLIST)) {
				PoppetUseResult result = PoppetUtils.tryUseItems(PoppetUtils.getPoppetQueue(player, EItems::isArmourPoppet), player, item);
				if(result.type() == ResultType.FAIL)
					return false;

				boolean cancelled = result.isSuccess();

				if(!cancelled)
					cancelled = PoppetUtils.tryUseEntries(PoppetUtils.getPoppetQueue(PoppetShelfManager.getEntriesFor(player),
							entry -> EItems.isArmourPoppet(entry.item().getItem())), player, item).isSuccess();

				if(cancelled) {
					player.setItemSlot(slot, item);
					return true;
				}
			}
		}
		return false;
	}

}
