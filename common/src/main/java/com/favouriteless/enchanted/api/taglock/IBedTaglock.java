package com.favouriteless.enchanted.api.taglock;

import com.favouriteless.enchanted.api.ISerializable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;

import java.util.UUID;

public interface IBedTaglock extends ISerializable<CompoundTag> {

    /**
     * @return The {@link UUID} of the last {@link Entity} to use this bed.
     */
    UUID getUUID();

    /**
     * Set the {@link UUID} of the last {@link Entity} to use this bed.
     */
    void setUUID(UUID uuid);

    /**
     * @return The display name of the last {@link Entity} to use this bed.
     */
    String getName();

    /**
     * Set the display name of the last {@link Entity} to use this bed.
     */
    void setName(String name);

}
