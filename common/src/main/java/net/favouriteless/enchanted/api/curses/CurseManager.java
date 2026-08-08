package net.favouriteless.enchanted.api.curses;

import com.mojang.serialization.Codec;
import net.favouriteless.enchanted.api.curses.Curse.Type;
import net.favouriteless.enchanted.common.enchanted.curses.CurseManagerImpl;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;

import java.util.Collection;
import java.util.UUID;

/**
 * CurseManager handles the registration, application, removal and ticking of {@link Curse}s.
 */
public interface CurseManager {

    static CurseManager get() {
        return CurseManagerImpl.INSTANCE;
    }

    /**
     * Register a {@link Curse.Type}. Should be called from the common entrypoint.
     *
     * @param type Type to be registered.
     *
     * @throws IllegalArgumentException if a type with a duplicate ID is registered.
     */
    void register(Type<?> type);

    /**
     * Apply a curse to a given target player. Initialisation of the curse may be deferred if the target player is not
     * online.
     *
     * @param type       Type of curse to apply.
     * @param target     Target to apply the curse to.
     * @param curseLevel Level to apply the curse at.
     * @param level      {@link ServerLevel} to grab save data from.
     */
    void applyCurse(Type<?> type, UUID target, int curseLevel, ServerLevel level);

    /**
     * Remove a curse from a given target player. Removal of the curse may be deferred if the target player is not
     * online.
     *
     * @param type   Type of curse to remove.
     * @param target Target to remove the curse from.
     * @param level  {@link ServerLevel} to grab save data from.
     */
    void removeCurse(Type<?> type, UUID target, ServerLevel level);

    /**
     * Get all curses currently attached to a given player.
     *
     * @param target {@link UUID} of the target player.
     * @param level  {@link ServerLevel} to grab save data from.
     *
     * @return A {@link Collection} containing all curses targeting the given player.
     */
    Collection<CurseInstance> getCurses(UUID target, ServerLevel level);

    Type<?> getType(ResourceLocation id);

    Codec<Type<?>> typeCodec();

    Codec<Curse> codec();

}
