package net.favouriteless.enchanted.api.curses;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.favouriteless.enchanted.common.enchanted.circle_magic.rites.Rite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

import java.util.function.Supplier;

/**
 * <p>
 *     CurseEffect is a server-side object which attaches itself to a target player and ticks. Not to be confused
 *     with {@link Rite}.
 * </p>
 * <p>
 *     <b>IMPORTANT:</b> CurseEffects must be registered via {@link CurseManager#register(Type)} to work.
 * </p>
 */
public interface Curse {

    static Codec<Curse> codec() {
        return CurseManager.get().codec();
    }

    /**
     * Called when this curse's target logs on, or when they are cloned (e.g. by respawning). Use this for updating
     * data attachments, attribute modifiers, etc. Will not be called if the curse has been marked for removal.
     *
     * @param target {@link ServerPlayer} this curse is targeting.
     * @param strength Level of this curse.
     * @param age Age (in ticks) of this curse, only increments when the target is online.
     */
    default void onInitialise(ServerPlayer target, int strength, long age) {}

    /**
     * Called when this curse is removed from its target, either immediately or when the target next joins the server.
     * Use this for updating data attachments, attribute modifiers, etc.
     *
     * @param target {@link ServerPlayer} this curse is targeting.
     * @param strength Level of this curse.
     * @param age Age (in ticks) of this curse, only increments when the target is online.
     */
    default void onRemove(ServerPlayer target, int strength, long age) {}

    /**
     * Called once per tick where this curse's target player is online. Will not be called if the curse has been marked
     * for removal.
     *
     * @param target {@link ServerPlayer} this curse is targeting.
     * @param strength Level of this curse.
     * @param age Age (in ticks) of this curse, only increments when the target is online.
     */
    default void tick(ServerPlayer target, int strength, long age) {}

    /**
     * @return The maximum level this curse is allowed to be. The default will be raised when more methods of increasing
     * removal rite level have been added.
     */
    default int getMaxStrength() {
        return 2;
    }

    /**
     * @return {@link Type} of this Curse.
     */
    Type<?> type();


    record Type<T extends Curse>(ResourceLocation id, Supplier<T> supplier, MapCodec<T> codec) {}

}
