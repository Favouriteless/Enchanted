package net.favouriteless.enchanted.common.poppet;

import net.favouriteless.enchanted.common.init.EItems;
import net.favouriteless.enchanted.common.items.component.EDataComponents;
import net.favouriteless.enchanted.common.items.component.EntityRefData;
import net.favouriteless.enchanted.common.items.poppets.DeathPoppetItem;
import net.favouriteless.enchanted.common.items.poppets.ItemProtectionPoppetItem;
import net.favouriteless.enchanted.common.items.poppets.PoppetItem;
import net.favouriteless.enchanted.common.network.client.PoppetAnimationPayload;
import net.favouriteless.enchanted.common.poppet.PoppetShelfSavedData.PoppetEntry;
import net.favouriteless.enchanted.common.poppet.PoppetUseResult.ResultType;
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
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;
import java.util.UUID;
import java.util.function.Predicate;

// TODO: Rewrite poppets entirely. This code is very bad.
public class PoppetUtils {

	/**
	 * Attempts to consume a target's Voodoo Protection Poppets.
	 *
	 * @param target The {@link Player} to attack.
	 * @param attacker The voodoo poppet being used.
	 *
	 * @return false if the voodoo was blocked.
	 */
	public static boolean tryVoodooPlayer(ServerPlayer target, ServerPlayer attacker, ItemStack poppet) {
		PoppetUseResult result = PoppetUtils.tryUseItems(PoppetUtils.getPoppetQueue(target, EItems::isVoodooProtectionPoppet), target, null);

		if(!result.isSuccess() && result.type() != ResultType.FAIL)
			result = PoppetUtils.tryUseEntries(PoppetUtils.getPoppetQueue(PoppetShelfManager.getEntriesFor(target), entry -> EItems.isVoodooProtectionPoppet(entry.item().getItem())), target, null);

		if(result.isSuccess()) {
			if(attacker != null) {
				ServerLevel level = attacker.serverLevel();
				if(result.poppet() == EItems.VOODOO_PROTECTION_POPPET_INFUSED.get()) {
					LightningBolt lightningBolt = EntityType.LIGHTNING_BOLT.create(level);
					lightningBolt.moveTo(attacker.getX(), attacker.getY(), attacker.getZ());
					level.addFreshEntity(lightningBolt);

					attacker.addEffect(new MobEffectInstance(MobEffects.WITHER, 200, 0));
				}
				level.playSound(null, attacker.getX(), attacker.getY(), attacker.getZ(), SoundEvents.ENDER_DRAGON_GROWL, SoundSource.MASTER, 1.0F, 1.0F);
			}
			poppet.shrink(1);
			return false;
		}
		return true;
	}

	/**
	 * Get a queue of Poppets in a {@link Player}'s inventory, sorted by failure rate.
	 *
	 * @param player The {@link Player} to check.
	 * @param validPoppet A {@link Predicate} to filter the poppets found.
	 *
	 * @return A {@link Queue} of {@link ItemStack}s belonging to player.
	 */
	public static Queue<ItemStack> getPoppetQueue(Player player, Predicate<PoppetItem> validPoppet) {
		Queue<ItemStack> poppetQueue = new ArrayDeque<>();
		for(ItemStack stack : player.getInventory().items) {
			if(stack.getItem() instanceof PoppetItem poppet && validPoppet.test(poppet))
				poppetQueue.add(stack);
		}
		ItemStack stack = player.getInventory().offhand.getFirst();
		if(stack.getItem() instanceof PoppetItem poppet && validPoppet.test(poppet))
			poppetQueue.add(stack);
		return poppetQueue;
	}

	/**
	 * Get a queue of {@link PoppetEntry}s from a Poppet Shelf, sorted by failure rate.
	 *
	 * @param entries The {@link List} to check.
	 * @param validPoppet A {@link Predicate} to filter the poppets found.
	 *
	 * @return A {@link Queue} of {@link ItemStack}s belonging to player.
	 */
	public static Queue<PoppetEntry> getPoppetQueue(List<PoppetEntry> entries, Predicate<PoppetEntry> validPoppet) {
		Queue<PoppetEntry> poppetQueue = new ArrayDeque<>();
		for(PoppetEntry entry : entries) {
			if(validPoppet.test(entry))
				poppetQueue.add(entry);
		}
		return poppetQueue;
	}

	/**
	 * @return true if item is a bound {@link PoppetItem}
	 */
	public static boolean isBound(ItemStack item) {
		return item.has(EDataComponents.ENTITY_REF.get());
	}

	/**
	 * Check if a poppet "belongs" to a player.
	 *
	 * @param item The {@link ItemStack} to check.
	 * @param player The {@link Player} to check for.
	 *
	 * @return true if item is bound to player.
	 */
	public static boolean belongsTo(ItemStack item, Player player) {
		return belongsTo(item, player.getUUID());
	}

	/**
	 * Check if a poppet "belongs" to a UUID.
	 *
	 * @param item The {@link ItemStack} to check.
	 * @param uuid The {@link UUID} to check for.
	 *
	 * @return true if item is bound to uuid.
	 */
	public static boolean belongsTo(ItemStack item, UUID uuid) {
		if(item.getItem() instanceof PoppetItem) {
			if(item.has(EDataComponents.ENTITY_REF.get()))
				return item.get(EDataComponents.ENTITY_REF.get()).uuid().equals(uuid);
		}
		return false;
	}

	/**
	 * @param item The {@link ItemStack} to check.
	 * @param level An instance of {@link ServerLevel} to grab the player list from.
	 *
	 * @return The player item is bound to.
	 */
	public static ServerPlayer getBoundPlayer(ItemStack item, ServerLevel level) {
		if(isBound(item))
			return level.getServer().getPlayerList().getPlayer(item.get(EDataComponents.ENTITY_REF.get()).uuid());
		return null;
	}

	/**
	 * Get the name of the player a poppet is bound to.
	 *
	 * @param item The {@link ItemStack} to check.
	 *
	 * @return The name of the bound player, or "None" if no player was found.
	 */
	public static String getBoundName(ItemStack item) {
		if(isBound(item))
			return item.get(EDataComponents.ENTITY_REF.get()).name();
		return "None";
	}

	/**
	 * Bind a poppet to a {@link Player}
	 *
	 * @param item The poppet to bind.
	 * @param player The {@link Player} to bind to.
	 */
	public static void bind(ItemStack item, Player player) {
		if(item.getItem() instanceof PoppetItem) {
			item.set(EDataComponents.ENTITY_REF.get(), EntityRefData.of(player.getUUID(), player.getDisplayName().getString()));
		}
	}

	/**
	 * Remove a poppet's bound {@link Player}.
	 *
	 * @param item The poppet to unbind.
	 */
	public static void unbind(ItemStack item) {
		if(item.getItem() instanceof PoppetItem)
			item.remove(EDataComponents.ENTITY_REF.get());
	}

	/**
	 * Attempts to use a Poppet and damage it.
	 *
	 * @param owner The owner of the poppet.
	 * @param poppetStack The poppet to use.
	 * @param protectStack The {@link ItemStack} to use the poppet on (if applicable).
	 * @param shelfIdentifier The identifier of the shelf the poppet is on (if applicable).
	 *
	 * @return A {@link PoppetUseResult} describing the outcome.
	 */
	private static PoppetUseResult tryUsePoppet(@NotNull Player owner, @NotNull ItemStack poppetStack, @Nullable ItemStack protectStack, @Nullable String shelfIdentifier) {
		if(poppetStack.getItem() instanceof PoppetItem && owner.level() instanceof ServerLevel level) {
			if(protectStack != null && poppetStack.getItem() instanceof ItemProtectionPoppetItem protection)
				protection.protect(protectStack);
			else if(poppetStack.getItem() instanceof DeathPoppetItem death)
				death.protect(owner);

			level.playSound(null, owner.getX(), owner.getY(), owner.getZ(), SoundEvents.TOTEM_USE, SoundSource.PLAYERS, 0.5F, 1.0F);
			return PoppetUseResult.of(poppetStack.getItem(), damagePoppet(poppetStack, level, shelfIdentifier));
		}
		return PoppetUseResult.pass();
	}

	public static PoppetUseResult handleTryUsePoppet(@NotNull Player owner, @NotNull ItemStack poppetStack, @Nullable ItemStack protectStack, @Nullable String shelfIdentifier) {
		ItemStack original = poppetStack.copy();
		return trySendAnimation(tryUsePoppet(owner, poppetStack, protectStack, shelfIdentifier), original, owner);
	}

	public static PoppetUseResult tryUseItems(@NotNull Queue<ItemStack> queue, @NotNull Player owner) {
		return tryUseItems(queue, owner, null);
	}

	public static PoppetUseResult tryUseItems(@NotNull Queue<ItemStack> queue, @NotNull Player owner, @Nullable ItemStack protectStack) {
		while(!queue.isEmpty()) {
			ItemStack item = queue.remove();
			if(PoppetUtils.belongsTo(item, owner)) {
				PoppetUseResult result = handleTryUsePoppet(owner, item, protectStack, null);
				if(result.isSuccess() || result.type() == ResultType.FAIL)
					return result;
			}
		}
		return PoppetUseResult.pass();
	}

	public static PoppetUseResult tryUseEntries(@NotNull Queue<PoppetEntry> queue, @NotNull Player owner) {
		return tryUseEntries(queue, owner, null);
	}

	public static PoppetUseResult tryUseEntries(@NotNull Queue<PoppetEntry> queue, @NotNull Player owner, @Nullable ItemStack protectStack) {
		while(!queue.isEmpty()) {
			PoppetEntry entry = queue.remove();
			PoppetUseResult result = handleTryUsePoppet(owner, entry.item(), protectStack, entry.shelfIdentifier());
			if(result.isSuccess() || result.type() == ResultType.FAIL)
				return result;
		}
		return PoppetUseResult.pass();
	}

	/**
	 * Attempts to damage a poppet.
	 *
	 * @param item The poppet to damage.
	 *
	 * @return true if poppet is destroyed.
	 */
	public static boolean damagePoppet(ItemStack item, ServerLevel level, String shelfIdentifier) {
		item.setDamageValue(item.getDamageValue()+1);
		if(item.getDamageValue() >= item.getMaxDamage()) {
			item.shrink(1);
			if(shelfIdentifier != null && item.getCount() <= 0) {
				PoppetShelfSavedData data = PoppetShelfSavedData.get(level);
				PoppetShelfInventory inventory = data.SHELF_STORAGE.get(shelfIdentifier);
				for(int i = 0; i < inventory.getContainerSize(); i++)
					if(inventory.get(i).equals(item))
						inventory.set(i, ItemStack.EMPTY);
				data.updateShelf(shelfIdentifier);
			}
			return true;
		}
		return false;
	}

	/**
	 * Checks if a result is successful, starts animation on the owner's client if it is.
	 *
	 * @param result
	 * @param poppetItemOriginal
	 * @param player
	 */
	private static PoppetUseResult trySendAnimation(PoppetUseResult result, ItemStack poppetItemOriginal, Player player) {
		if(result.isSuccess()) {
			if(!player.level().isClientSide)
				EServices.NETWORK.sendToAllPlayers(new PoppetAnimationPayload(result.type(), poppetItemOriginal, player.getId()), player.level().getServer());
		}
		return result;
	}

}
