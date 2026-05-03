package net.favouriteless.enchanted.common.enchanted.poppet;

import net.favouriteless.enchanted.common.items.poppets.ItemPoppetItem;
import net.favouriteless.enchanted.common.items.poppets.PlayerPoppetItem;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class PoppetEvents {

    public static boolean onLivingEntityHurt(LivingEntity entity, float amount, DamageSource source) {
        if(entity instanceof ServerPlayer player) {
            if(amount < player.getHealth())
                return false;

            return PoppetHelper.tryTriggerAll(player, stack -> {
                if(stack.getItem() instanceof PlayerPoppetItem poppet && poppet.protectsAgainst(source)) {
                    poppet.protect(player);
                    return true;
                }
                return false;
            });
        }
        return false;
    }


    public static void onPlayerItemBreak(Player p, ItemStack item, InteractionHand hand) {
        if(p instanceof ServerPlayer player) {
            PoppetHelper.tryTriggerAll(player, stack -> {
                if(stack.getItem() instanceof ItemPoppetItem poppet && poppet.canProtect(item)) {
                    poppet.protect(stack);
                    player.setItemInHand(hand, stack);
                    return true;
                }
                return false;
            });
        }
    }

    public static boolean onArmourHurt(LivingEntity entity, EquipmentSlot slot, ItemStack item, float damage) {
        if(entity instanceof ServerPlayer player && (item.getMaxDamage() - item.getDamageValue()) <= damage) {
            PoppetHelper.tryTriggerAll(player, stack -> {
                if(stack.getItem() instanceof ItemPoppetItem poppet && poppet.canProtect(item)) {
                    poppet.protect(item);
                    player.setItemSlot(slot, item);
                    return true;
                }
                return false;
            });
        }
        return false;
    }

}
