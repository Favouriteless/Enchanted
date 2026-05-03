package net.favouriteless.enchanted.api.poppets;

import net.favouriteless.enchanted.common.items.poppets.PlayerPoppetItem;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;

/**
 * Protects a player from taking fatal damage via any DamageSources the poppet protects against.
 *
 * @see PlayerPoppetItem
 */
public interface PlayerPoppetEffect {

    boolean protectsAgainst(DamageSource damageSource);

    void protect(Player player);

}
