package net.favouriteless.enchanted.api.curses;

import com.mojang.serialization.Codec;
import net.favouriteless.enchanted.common.curses.CurseInstanceImpl;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

/**
 * Represents an instance of a {@link Curse}.
 */
public interface CurseInstance {

    /**
     * @return Curse this instance is for.
     */
    Curse getCurse();

    /**
     * @return Level of this instance.
     */
    int getLevel();

    /**
     * Set the level of this instance. Will be clamped to {@link Curse#getMaxStrength()}.
     *
     * @param level level to be set as.
     */
    void setLevel(int level);

    /**
     * @return Age (in ticks) of this instance, only increments when the target is online.
     */
    long getAge();

    /**
     * @return {@link UUID} of the player this instance is targeting.
     */
    UUID getTargetUuid();

    /**
     * @return {@link ServerPlayer} this instance is targeting, or null if they are offline.
     *
     * @param level {@link ServerLevel} to grab the player lists from.
     */
    @Nullable ServerPlayer getTarget(ServerLevel level);

    /**
     * @return True if this instance is marked for removal.
     */
    boolean isRemoved();

    static Codec<CurseInstance> codec() {
        return CurseInstanceImpl.CODEC;
    }

}
