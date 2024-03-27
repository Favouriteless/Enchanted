package com.favouriteless.enchanted.common.blocks;

import com.favouriteless.enchanted.common.blocks.entity.DistilleryBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.stream.Stream;

public class DistilleryBlock extends SimpleContainerBlockBase {

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty LIT = BlockStateProperties.LIT;
    
    private static final VoxelShape SHAPE_NORTH  = Stream.of(Block.box(0, 0, 0, 16, 1, 5), Block.box(7, 14, 6, 9, 16, 10), Block.box(6, 12, 8, 10, 14, 12), Block.box(5, 9, 7, 11, 12, 13), Block.box(4, 5, 6, 12, 9, 14), Block.box(5, 4, 7, 11, 5, 13), Block.box(0, 1, 2, 1, 7, 3), Block.box(15, 1, 2, 16, 7, 3), Block.box(1, 8, 2, 15, 9, 3), Block.box(0, 7, 2, 2, 8, 3), Block.box(14, 7, 2, 16, 8, 3), Block.box(5, 1, 6, 7, 3, 14), Block.box(9, 1, 6, 11, 3, 14), Block.box(4, 0, 7, 12, 2, 9), Block.box(4, 0, 11, 12, 2, 13), Block.box(2, 1, 1, 5, 6, 4), Block.box(6, 1, 1, 11, 3, 4), Block.box(12, 1, 1, 14, 4, 3), Block.box(12.5, 4, 1.5, 13.5, 5, 2.5)).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();
    public static final VoxelShape SHAPE_SOUTH = Stream.of(Block.box(0, 0, 11, 16, 1, 16), Block.box(7, 14, 6, 9, 16, 10), Block.box(6, 12, 4, 10, 14, 8), Block.box(5, 9, 3, 11, 12, 9), Block.box(4, 5, 2, 12, 9, 10), Block.box(5, 4, 3, 11, 5, 9), Block.box(5, 1, 12, 10, 3, 15), Block.box(11, 1, 12, 14, 6, 15), Block.box(15, 1, 13, 16, 7, 14), Block.box(0, 7, 13, 2, 8, 14), Block.box(14, 7, 13, 16, 8, 14), Block.box(1, 8, 13, 15, 9, 14), Block.box(0, 1, 13, 1, 7, 14), Block.box(9, 1, 2, 11, 3, 10), Block.box(5, 1, 2, 7, 3, 10), Block.box(4, 0, 7, 12, 2, 9), Block.box(4, 0, 3, 12, 2, 5), Block.box(2, 1, 13, 4, 4, 15), Block.box(2.5, 4, 13.5, 3.5, 5, 14.5)).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();
    public static final VoxelShape SHAPE_WEST = Stream.of(Block.box(0, 0, 0, 5, 1, 16), Block.box(6, 14, 7, 10, 16, 9), Block.box(8, 12, 6, 12, 14, 10), Block.box(7, 9, 5, 13, 12, 11), Block.box(6, 5, 4, 14, 9, 12), Block.box(7, 4, 5, 13, 5, 11), Block.box(1, 1, 5, 4, 3, 10), Block.box(1, 1, 11, 4, 6, 14), Block.box(2, 1, 15, 3, 7, 16), Block.box(2, 7, 0, 3, 8, 2), Block.box(2, 7, 14, 3, 8, 16), Block.box(2, 8, 1, 3, 9, 15), Block.box(2, 1, 0, 3, 7, 1), Block.box(6, 1, 9, 14, 3, 11), Block.box(6, 1, 5, 14, 3, 7), Block.box(7, 0, 4, 9, 2, 12), Block.box(11, 0, 4, 13, 2, 12), Block.box(1, 1, 2, 3, 4, 4), Block.box(1.5, 4, 2.5, 2.5, 5, 3.5)).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();
    public static final VoxelShape SHAPE_EAST = Stream.of(Block.box(11, 0, 0, 16, 1, 16), Block.box(6, 14, 7, 10, 16, 9), Block.box(4, 12, 6, 8, 14, 10), Block.box(3, 9, 5, 9, 12, 11), Block.box(2, 5, 4, 10, 9, 12), Block.box(3, 4, 5, 9, 5, 11), Block.box(12, 1, 6, 15, 3, 11), Block.box(12, 1, 2, 15, 6, 5), Block.box(13, 1, 0, 14, 7, 1), Block.box(13, 7, 14, 14, 8, 16), Block.box(13, 7, 0, 14, 8, 2), Block.box(13, 8, 1, 14, 9, 15), Block.box(13, 1, 15, 14, 7, 16), Block.box(2, 1, 5, 10, 3, 7), Block.box(2, 1, 9, 10, 3, 11), Block.box(7, 0, 4, 9, 2, 12), Block.box(3, 0, 4, 5, 2, 12), Block.box(13, 1, 12, 15, 4, 14), Block.box(13.5, 4, 12.5, 14.5, 5, 13.5)).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();

    public DistilleryBlock(Properties builder) {
        super(builder);
        this.registerDefaultState(this.defaultBlockState().setValue(FACING, Direction.NORTH).setValue(LIT, false));
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new DistilleryBlockEntity(pos, state);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return switch(state.getValue(FACING)) {
            default -> SHAPE_NORTH;
            case SOUTH -> SHAPE_SOUTH;
            case WEST -> SHAPE_WEST;
            case EAST -> SHAPE_EAST;
        };
    }

    public float getShadeBrightness(BlockState state, BlockGetter worldIn, BlockPos pos) {
        return 0.7F;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, LIT);
    }

    @Override
    public RenderShape getRenderShape(BlockState iBlockState) {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return !level.isClientSide ? DistilleryBlockEntity::serverTick : null;
    }

}