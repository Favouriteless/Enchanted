package net.favouriteless.enchanted.common;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import net.favouriteless.enchanted.platform.EServices;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

public class SyncedFlags {

    private static final BiMap<Integer, Flag<?>> flags = HashBiMap.create();
    private static final Map<Flag<?>, Object> values = new HashMap<>();

    public static <T> void set(Flag<T> flag, T value) {
        values.put(flag, value);
    }

    @SuppressWarnings("unchecked")
    public static <T> T get(Flag<T> flag) {
        return (T)values.get(flag);
    }

    public static <T> void update(Flag<T> flag, T value, ServerLevel level) {
        EServices.NETWORK.sendToAllPlayers(flag.payloadConstructor().apply(flag, value), level.getServer());
    }

    public static <T> void update(Flag<T> flag, T value, ServerPlayer player) {
        EServices.NETWORK.sendToPlayer(flag.payloadConstructor().apply(flag, value), player);
    }

    public static int getId(Flag<?> flag) {
        return flags.inverse().get(flag);
    }

    public static Flag<?> getFlag(int id) {
        return flags.get(id);
    }

    private static <T> Flag<T> register(Class<T> clazz, T def, BiFunction<Flag<T>, T, CustomPacketPayload> payloadConstructor) {
        Flag<T> flag = new Flag<>(clazz, def, payloadConstructor);
        flags.put(flags.size(), flag);
        values.put(flag, def);
        return flag;
    }


    public record Flag<T>(Class<T> clazz, T def, BiFunction<Flag<T>, T, CustomPacketPayload> payloadConstructor) {}

}
