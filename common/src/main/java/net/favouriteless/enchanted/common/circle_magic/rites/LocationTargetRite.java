package net.favouriteless.enchanted.common.circle_magic.rites;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;

public abstract class LocationTargetRite extends Rite {

    protected ServerLevel targetLevel = null;
    protected BlockPos targetPos = null;

    protected LocationTargetRite(BaseRiteParams baseParams, RiteParams params) {
        super(baseParams, params);
    }

    @Override
    protected boolean onStart(RiteParams params) {
        findTargetLocation(params);
        if(targetLevel == null || targetPos == null)
            cancel();
        return false;
    }

    protected void findTargetLocation(RiteParams params) {
        targetLevel = level;
        targetPos = pos;
    }

    @Override
    protected void saveAdditional(CompoundTag tag, ServerLevel level) {
        if(targetPos != null)
            tag.put("targetPos", BlockPos.CODEC.encodeStart(NbtOps.INSTANCE, targetPos).getOrThrow());
        if(targetLevel != null)
            tag.putString("targetLevel", targetLevel.dimension().location().toString());
    }

    @Override
    protected void loadAdditional(CompoundTag tag, ServerLevel level) {
        if(tag.contains("targetPos"))
            targetPos = BlockPos.CODEC.parse(NbtOps.INSTANCE, tag.get("targetPos")).getOrThrow();
        if(tag.contains("targetLevel"))
            targetLevel = level.getServer().getLevel(ResourceKey.create(Registries.DIMENSION, ResourceLocation.parse(tag.getString("targetLevel"))));
    }

}
