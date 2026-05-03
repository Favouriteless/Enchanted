package net.favouriteless.enchanted.common.items.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.favouriteless.enchanted.common.util.EntityUtils;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;

import java.util.UUID;

public record EntityRefData(UUID uuid, String name) {

    public static final Codec<EntityRefData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            UUIDUtil.CODEC.fieldOf("uuid").forGetter(data -> data.uuid),
            Codec.STRING.fieldOf("name").forGetter(data -> data.name)
    ).apply(instance, EntityRefData::of));

    public static final StreamCodec<ByteBuf, EntityRefData> STREAM_CODEC = StreamCodec.composite(
            UUIDUtil.STREAM_CODEC, data -> data.uuid,
            ByteBufCodecs.STRING_UTF8, data -> data.name,
            EntityRefData::new
    );

    public static EntityRefData of(UUID uuid, String name) {
        return new EntityRefData(uuid, name);
    }

    /**
     * Searches for the entity this {@link EntityRefData} is for across all levels.
     *
     * @param level any level.
     *
     * @return The matching entity if one was found, otherwise {@code null}.
     */
    public Entity tryGetEntity(ServerLevel level) {
        return EntityUtils.tryGetEntity(level, uuid);
    }


}
