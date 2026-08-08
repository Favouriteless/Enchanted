package net.favouriteless.enchanted.common.blocks.entity;

import net.favouriteless.enchanted.common.Enchanted;
import net.favouriteless.enchanted.common.items.component.EntityRefData;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class BloodPoppyBlockEntity extends BlockEntity {

    private EntityRefData data = null;

    public BloodPoppyBlockEntity(BlockPos pos, BlockState state) {
        super(EBlockEntityTypes.BLOOD_POPPY.get(), pos, state);
    }

    public void setTaglockData(EntityRefData data) {
        this.data = data;
    }

    public EntityRefData getTaglockData() {
        return data;
    }

    public void reset() {
        data = null;
    }

    @Override
    protected void saveAdditional(CompoundTag tag, @NotNull Provider registries) {
        if (data != null) {
            tag.put("taglockData", EntityRefData.CODEC.encode(data, NbtOps.INSTANCE, new CompoundTag()).getOrThrow());
        }
    }

    @Override
    protected void loadAdditional(CompoundTag tag, @NotNull Provider registries) {
        if (tag.contains("taglockData")) {
            data = EntityRefData.CODEC.parse(NbtOps.INSTANCE, tag.get("taglockData"))
                                      .resultOrPartial(e -> Enchanted.LOG.error("Tried to load invalid taglock data: '{}'", e))
                                      .orElse(null);
        }
    }

}
