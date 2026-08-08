package net.favouriteless.enchanted.common.util;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;

import java.util.UUID;

public class EntityUtils {

    public static Entity getControllingEntity(Entity entity) {
        return entity.isPassenger() ? getControllingEntity(entity.getVehicle()) : entity;
    }

    /**
     * Searches for an entity with a matching {@link UUID} across all levels.
     *
     * @param level any level.
     * @param uuid  the uuid to search for.
     *
     * @return The matching entity if one was found, otherwise {@code null}.
     */
    public static Entity tryGetEntity(ServerLevel level, UUID uuid) {
        Entity out = tryFindPlayer(level, uuid);
        if (out != null) {
            return out;
        }

        for (ServerLevel dim : level.getServer().getAllLevels()) {
            out = dim.getEntity(uuid);
            if (out != null) {
                return out;
            }
        }
        return null;
    }

    public static ServerPlayer tryFindPlayer(ServerLevel level, UUID uuid) {
        return level.getServer().getPlayerList().getPlayer(uuid);
    }

}
