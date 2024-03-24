package com.favouriteless.enchanted.common.blocks.cauldrons;

import com.favouriteless.enchanted.common.blocks.entity.KettleBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SupportType;
import net.minecraft.world.level.block.WallBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class KettleBlock extends CauldronBlockBase {

    public static DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static IntegerProperty TYPE = IntegerProperty.create("type", 0, 2);
    public static final VoxelShape TYPE_1_SHAPE = Shapes.box(0.1875, 0.125, 0.1875, 0.8125, 0.5, 0.8125);
    public static final VoxelShape TYPE_2_SHAPE = Shapes.box(0.1875, 0, 0.1875, 0.8125, 0.375, 0.8125);

    public KettleBlock(Properties properties) {
        super(properties);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new KettleBlockEntity(pos, state);
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return (pState.getValue(TYPE) == 1) ? TYPE_1_SHAPE : TYPE_2_SHAPE;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, TYPE);
    }

    @Override
    public void neighborChanged(BlockState state, Level world, BlockPos pos, Block block, BlockPos fromPos, boolean pIsMoving) {
        BlockState newState = getKettleState(world, pos, state.getValue(FACING));
        if(state != newState)
            world.setBlock(pos, newState, 2);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        return getKettleState(pContext.getLevel(), pContext.getClickedPos(), pContext.getHorizontalDirection().getOpposite());
    }

    public BlockState getKettleState(Level world, BlockPos pos, @Nullable Direction facing) {
        if(facing == null) facing = Direction.NORTH;
        int type = 0;
        Direction left = facing.getCounterClockWise();
        Direction right = facing.getClockWise();

        if(world.getBlockState(pos.above()).isFaceSturdy(world, pos.above(), Direction.DOWN)) { // Face above is sturdy
            type = 1;
        }
        else if((world.getBlockState(pos.relative(left)).isFaceSturdy(world, pos.relative(left), right, SupportType.CENTER)
                && world.getBlockState(pos.relative(right)).isFaceSturdy(world, pos.relative(right), left, SupportType.CENTER))
                || (world.getBlockState(pos.relative(left)).getBlock() instanceof WallBlock && world.getBlockState(pos.relative(right)).getBlock() instanceof WallBlock)) { // Horizontal perpendicular to place direction is sturdy
            type = 2;
        }
        else if((world.getBlockState(pos.relative(facing)).isFaceSturdy(world, pos.relative(facing), facing.getOpposite(), SupportType.CENTER)
                && world.getBlockState(pos.relative(facing.getOpposite())).isFaceSturdy(world, pos.relative(facing.getOpposite()), facing, SupportType.CENTER))
                || (world.getBlockState(pos.relative(facing)).getBlock() instanceof WallBlock && world.getBlockState(pos.relative(facing.getOpposite())).getBlock() instanceof WallBlock)) { // Horizontal towards place direction is sturdy
            type = 2;
            facing = facing.getClockWise();
        }

        return defaultBlockState().setValue(FACING, facing).setValue(TYPE, type);
    }

}

