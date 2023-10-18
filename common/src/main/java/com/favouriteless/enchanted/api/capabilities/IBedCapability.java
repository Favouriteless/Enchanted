package com.favouriteless.enchanted.api.capabilities;

import com.favouriteless.enchanted.Enchanted;
import com.favouriteless.enchanted.api.ISerializable;
import com.favouriteless.enchanted.common.items.TaglockItem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BedBlockEntity;

import java.util.UUID;

/**
 * Stores the UUID of the last player to sleep in a {@link BedBlockEntity}, used by {@link TaglockItem}.
 */
public interface IBedCapability extends ISerializable<CompoundTag> {

    ResourceLocation LOCATION = Enchanted.location("bed");

    UUID getUUID();
    void setUUID(UUID uuid);
    String getName();
    void setName(String name);
}
