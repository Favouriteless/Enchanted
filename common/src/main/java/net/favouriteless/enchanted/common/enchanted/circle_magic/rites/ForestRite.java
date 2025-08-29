package net.favouriteless.enchanted.common.enchanted.circle_magic.rites;

import net.favouriteless.enchanted.common.init.EParticleTypes;
import net.favouriteless.enchanted.common.util.BlockPosUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap.Types;

import java.util.ArrayList;
import java.util.List;

public class ForestRite extends Rite {

    private final int spacing;
    private final int maxSteps;

    private SaplingBlock sapling;
    private int step = 0;

    private final List<BlockPos> usedPositions = new ArrayList<>();

    public ForestRite(BaseRiteParams baseParams, RiteParams params, int radius, int spacing) {
        super(baseParams, params);
        this.spacing = spacing;
        this.maxSteps = radius / spacing;
    }

    @Override
    protected boolean onStart(RiteParams params) {
        sapling = getSapling();
        if(sapling == null)
            return cancel();

        level.playSound(null, pos, SoundEvents.ZOMBIE_VILLAGER_CURE, SoundSource.MASTER, 0.5F, 1.0F);
        return true;
    }

    @Override
    protected boolean onTick(RiteParams params) {
        if(params.ticks() % 20 != 0)
            return true;

        BlockState state = sapling.defaultBlockState();
        double r = spacing / 1.7D;
        double o = r / 2D;

        BlockPosUtils.iterableCircleHollow(pos, step*spacing, spacing).forEach(circlePos -> {
            if(Math.random() < 0.3D)
                return;
            circlePos.move((int)Math.round(Math.random() * r - o), 0, (int)Math.round(Math.random() * r - o));
            BlockPos tree = level.getHeightmapPos(Types.MOTION_BLOCKING_NO_LEAVES, circlePos);

            if(!level.getBlockState(tree).canBeReplaced() || !state.canSurvive(level, tree))
                return;

            if(sapling.treeGrower.growTree(level, level.getChunkSource().getGenerator(), tree, state, level.random)) {
                level.playSound(null, pos, SoundEvents.FUNGUS_PLACE, SoundSource.MASTER, 3.0F, 1.0F);
                usedPositions.add(tree);
            }
        });

        level.sendParticles(EParticleTypes.FERTILITY_SEED.get(), pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D,
                1, 0, 0, 0, 0);

        return step++ < maxSteps;
    }

    @Override
    protected void saveAdditional(CompoundTag tag, ServerLevel level) {
        tag.putInt("step", step);
        tag.putString("sapling", BuiltInRegistries.BLOCK.getKey(sapling).toString());
        tag.put("used", BlockPos.CODEC.listOf().encodeStart(NbtOps.INSTANCE, usedPositions).getOrThrow());
    }

    @Override
    protected void loadAdditional(CompoundTag tag, ServerLevel level) {
        step = tag.getInt("step");
        sapling = (SaplingBlock)BuiltInRegistries.BLOCK.get(ResourceLocation.parse(tag.getString("sapling")));
        usedPositions.addAll(BlockPos.CODEC.listOf().parse(NbtOps.INSTANCE, tag.get("used")).getOrThrow());
    }

    protected SaplingBlock getSapling() {
        List<ItemEntity> entities = level.getEntitiesOfClass(ItemEntity.class, type.getBounds(pos));

        for(ItemEntity entity : entities) {
            if(entity.getItem().getItem() instanceof BlockItem blockItem &&
                    blockItem.getBlock() instanceof SaplingBlock sapling) {
                consumeItem(entity);
                return sapling;
            }
        }
        return null;
    }

}
