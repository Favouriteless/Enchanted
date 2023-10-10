package com.favouriteless.enchanted.common.blockentities;

import com.favouriteless.enchanted.common.init.registry.EnchantedBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.UUID;

public class BloodPoppyBlockEntity extends BlockEntity {

    private UUID uuid = null;
    private String name = null;

    public BloodPoppyBlockEntity(BlockPos pos, BlockState state) {
        super(EnchantedBlockEntityTypes.BLOOD_POPPY.get(), pos, state);
    }

    public UUID getUUID() {
        return uuid;
    }

    public void setUUID(UUID uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void reset() {
        this.uuid = null;
        this.name = null;
    }

    @Override
    public void saveAdditional(CompoundTag nbt) {
        super.saveAdditional(nbt);
        if(uuid != null)
            nbt.putUUID("uuid", uuid);
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        if(nbt.contains("uuid"))
            this.uuid = nbt.getUUID("uuid");
    }

}
