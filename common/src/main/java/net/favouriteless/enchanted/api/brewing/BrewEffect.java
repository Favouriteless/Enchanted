package net.favouriteless.enchanted.api.brewing;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

/**
 * A custom brew effect placed on the cauldron's brewing map.
 */
public interface BrewEffect {

    /**
     * Called when a brew with this effect is consumed by a player.
     *
     * @param player   The {@link Player} who consumed the brew.
     * @param strength The strength of the brew effect.
     */
    void onConsumed(Player player, int strength);

    /**
     * Called when a brew with this effect is splashed in a level.
     *
     * @param level    The {@link Level} the brew was splashed in.
     * @param pos      The position the brew impacted a block or entity at.
     * @param type     The {@link SplashType} of the impact.
     * @param strength The strength of the brew effect.
     */
    void onSplashed(Level level, Vec3 pos, SplashType type, int strength);


    /**
     * Represents a type of splash effect, determined by the type of brew being splashed. It is up to the implementer to
     * define these, but the javadocs contain general guidelines.
     * <p>
     * <b>IMPORTANT:</b> New splash types may be added in the future. Implementers should provide a sensible default
     * case, such as standard {@link SplashType#SPLASH}.
     * </p>
     */
    enum SplashType {
        /**
         * Standard splash potion behaviour, as with vanilla splash potions.
         */
        SPLASH,
        /**
         * Creates a lingering cloud of gas which can fill spaces, affecting any entity standing in it.
         */
        GAS,
        /**
         * Creates a pool on the floor, affecting any entity standing in it.
         */
        POOL
    }

}
