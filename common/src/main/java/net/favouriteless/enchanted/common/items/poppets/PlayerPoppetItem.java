package net.favouriteless.enchanted.common.items.poppets;

import net.favouriteless.enchanted.api.poppets.PlayerPoppetEffect;
import net.favouriteless.enchanted.common.enchanted.poppet.PoppetColour;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;

public class PlayerPoppetItem extends PoppetItem {

    private final PlayerPoppetEffect effect;

    public PlayerPoppetItem(PoppetColour colour, PlayerPoppetEffect effect, Properties properties) {
        super(colour, properties);
        this.effect = effect;
    }

    public boolean protectsAgainst(DamageSource damageSource) {
        return effect.protectsAgainst(damageSource);
    }

    public void protect(Player player) {
        effect.protect(player);
    }

}
