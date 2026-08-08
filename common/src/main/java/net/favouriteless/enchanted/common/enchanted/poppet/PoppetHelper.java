package net.favouriteless.enchanted.common.enchanted.poppet;

import net.favouriteless.enchanted.common.enchanted.poppet.shelf.PoppetReference;
import net.favouriteless.enchanted.common.enchanted.poppet.shelf.PoppetShelfManager;
import net.favouriteless.enchanted.common.init.EItems;
import net.favouriteless.enchanted.common.items.component.EDataComponents;
import net.favouriteless.enchanted.common.items.component.EntityRefData;
import net.favouriteless.enchanted.common.items.poppets.PoppetItem;
import net.favouriteless.enchanted.common.network.client.PoppetAnimationPayload;
import net.favouriteless.enchanted.platform.EServices;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;
import java.util.function.Predicate;

public class PoppetHelper {

    /**
     * Bind a poppet to the given player. Player must be online so their username can be grabbed.
     *
     * @param stack  Item to bind, will be ignored if it is not a poppet.
     * @param player Player to bind the poppet to.
     */
    public static void bind(ItemStack stack, Player player) {
        if (stack.getItem() instanceof PoppetItem) {
            stack.set(EDataComponents.ENTITY_REF.get(), new EntityRefData(player.getUUID(), player.getDisplayName().getString()));
        }
    }

    /**
     * Unbind a given poppet.
     *
     * @param stack Item to unbind, will be ignored if it is not a poppet.
     */
    public static void unbind(ItemStack stack) {
        if (stack.getItem() instanceof PoppetItem) {
            stack.set(EDataComponents.ENTITY_REF.get(), null);
        }
    }

    /**
     * @return True if the given {@link ItemStack} is bound to an entity.
     */
    public static boolean isBound(ItemStack stack) {
        return stack.has(EDataComponents.ENTITY_REF.get());
    }

    /**
     * @return True if the given {@link ItemStack} is bound to the given {@link Player}.
     */
    public static boolean isBoundTo(ItemStack stack, Player player) {
        return isBoundTo(stack, player.getUUID());
    }

    /**
     * @return True if the given {@link ItemStack} is bound to the given {@link UUID}.
     */
    public static boolean isBoundTo(ItemStack stack, UUID uuid) {
        EntityRefData data = getData(stack);
        return data != null && data.uuid().equals(uuid);
    }

    /**
     * @return The {@link EntityRefData} of the given stack, or null if it is not bound.
     */
    public static @Nullable EntityRefData getData(ItemStack stack) {
        return stack.get(EDataComponents.ENTITY_REF.get());
    }

    public static void doTriggerAnimation(ServerPlayer player, Item poppet) {
        EServices.NETWORK.sendToAllPlayers(new PoppetAnimationPayload(player.getId(), poppet), player.getServer());
    }

    public static boolean tryTriggerCarried(ServerPlayer player, Predicate<ItemStack> usePredicate) {
        for (ItemStack stack : player.getInventory().items) {
            Item item = stack.getItem(); // Grab item early because if it shrinks to 0 this will return air

            if (usePredicate.test(stack) && PoppetHelper.isBoundTo(stack, player)) {
                stack.setDamageValue(stack.getDamageValue() + 1);
                if (stack.getDamageValue() >= stack.getMaxDamage()) {
                    stack.shrink(1);
                }

                PoppetHelper.doTriggerAnimation(player, item);
                return true;
            }
        }
        return false;
    }

    public static boolean tryTriggerShelved(ServerPlayer player, Predicate<ItemStack> usePredicate) {
        PoppetShelfManager manager = PoppetShelfManager.get(player.serverLevel());
        for (PoppetReference ref : manager.getPoppets(player)) {
            if (ref.tryConsume(player, usePredicate)) {
                return true;
            }
        }
        return false;
    }

    public static boolean tryTriggerAll(ServerPlayer player, Predicate<ItemStack> usePredicate) {
        return tryTriggerCarried(player, usePredicate) || tryTriggerShelved(player, usePredicate);
    }

    public static boolean tryUseVoodoo(ServerPlayer attacker, ServerPlayer target) {
        // Attempt to use infused poppets first to prioritise them over regular ones.
        boolean blockedInfused = tryTriggerAll(target, stack -> stack.getItem() == EItems.VOODOO_PROTECTION_POPPET_INFUSED.get());
        boolean blocked = blockedInfused || tryTriggerAll(
                target, stack ->
                        stack.getItem() == EItems.VOODOO_PROTECTION_POPPET.get() ||
                                stack.getItem() == EItems.VOODOO_PROTECTION_POPPET_STURDY.get()
        );

        if (!blockedInfused) {
            return !blocked;
        }

        ServerLevel level = attacker.serverLevel();
        LightningBolt lightning = EntityType.LIGHTNING_BOLT.create(level);
        lightning.moveTo(attacker.getX(), attacker.getY(), attacker.getZ());
        level.addFreshEntity(lightning);

        attacker.addEffect(new MobEffectInstance(MobEffects.WITHER, 200, 0));
        level.playSound(null, attacker.getX(), attacker.getY(), attacker.getZ(), SoundEvents.ENDER_DRAGON_GROWL, SoundSource.MASTER, 1.0F, 1.0F);

        return false;
    }

}
