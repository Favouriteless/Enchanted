package net.favouriteless.enchanted.common.blocks.chalk;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public abstract class AbstractChalkBlock extends Block {

    protected static VoxelShape SHAPE = Shapes.create(new AABB(0.0, 0.0, 0.0, 1.0, 0.0625, 1.0));

    public AbstractChalkBlock() {
        super(Properties.of().pushReaction(PushReaction.DESTROY).noOcclusion().noCollission().strength(0.5f, 0f).noLootTable());
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
        if (!level.isClientSide()) {
            if (!state.canSurvive(level, pos)) {
                level.removeBlock(pos, false);
            }
        }
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        BlockPos belowPos = pos.below();
        BlockState blockstate = level.getBlockState(belowPos);
        return blockstate.isFaceSturdy(level, belowPos, Direction.UP);
    }

    public abstract BlockState getRandomState();

}
