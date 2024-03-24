package com.favouriteless.enchanted.common.blocks;

import com.favouriteless.enchanted.common.blocks.entity.WitchOvenBlockEntity;
import com.favouriteless.enchanted.common.init.registry.EnchantedBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
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

public class WitchOvenBlock extends SimpleContainerBlockBase {

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty LIT = BlockStateProperties.LIT;

    private static final VoxelShape SHAPE_NORTH = Shapes.join(Shapes.box(1.0D/16, 0.0D, 1.0D/16, 15.0D/16, 12.0D/16, 15.0D/16),
            Shapes.box(5.0D/16, 0.0D, 8.0D/16, 11.0D/16, 1.0D, 14.0D/16), BooleanOp.OR);
    private static final VoxelShape SHAPE_SOUTH = Shapes.join(Shapes.box(1.0D/16, 0.0D, 1.0D/16, 15.0D/16, 12.0D/16, 15.0D/16),
            Shapes.box(5.0D/16, 0.0D, 2.0D/16, 11.0D/16, 1.0D, 8.0D/16), BooleanOp.OR);
    private static final VoxelShape SHAPE_EAST = Shapes.join(Shapes.box(1.0D/16, 0.0D, 1.0D/16, 15.0D/16, 12.0D/16, 15.0D/16),
            Shapes.box(2.0D/16, 0.0D, 5.0D/16, 8.0D/16, 1.0D, 11.0D/16), BooleanOp.OR);
    private static final VoxelShape SHAPE_WEST = Shapes.join(Shapes.box(1.0D/16, 0.0D, 1.0D/16, 15.0D/16, 12.0D/16, 15.0D/16),
            Shapes.box(8.0D/16, 0.0D, 5.0D/16, 14.0D/16, 1.0D, 11.0D/16), BooleanOp.OR);

    public WitchOvenBlock(Properties builder) {
        super(builder);
        this.registerDefaultState(this.getStateDefinition().any().setValue(FACING, Direction.NORTH).setValue(LIT, false));
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new WitchOvenBlockEntity(pos, state);
    }

    @Override
    public RenderShape getRenderShape(BlockState iBlockState) {
        return RenderShape.MODEL;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, LIT);
    }

    @Override
    public void animateTick(BlockState state, Level world, BlockPos pos, RandomSource random) {
        if (state.getValue(LIT)) {
            double d0 = (double)pos.getX() + 0.5D;
            double d1 = pos.getY();
            double d2 = (double)pos.getZ() + 0.5D;
            if (random.nextDouble() < 0.1D)
                world.playLocalSound(d0, d1, d2, SoundEvents.FURNACE_FIRE_CRACKLE, SoundSource.BLOCKS, 1.0F, 1.0F, false);

            Direction direction = state.getValue(FACING);
            Direction.Axis direction$axis = direction.getAxis();
            double d4 = random.nextDouble() * 0.6D - 0.3D;
            double d5 = direction$axis == Direction.Axis.X ? (double) direction.getStepX() * 0.49D : d4;
            double d6 = random.nextDouble() * 6.0D / 16.0D;
            double d7 = direction$axis == Direction.Axis.Z ? (double) direction.getStepZ() * 0.49D : d4;
            world.addParticle(ParticleTypes.SMOKE, d0 + d5, d1 + d6, d2 + d7, 0.0D, 0.0D, 0.0D);
            world.addParticle(ParticleTypes.FLAME, d0 + d5, d1 + d6, d2 + d7, 0.0D, 0.0D, 0.0D);
        }
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return switch(state.getValue(FACING)) {
            case SOUTH -> SHAPE_SOUTH;
            case EAST -> SHAPE_EAST;
            case WEST -> SHAPE_WEST;
            default -> SHAPE_NORTH;
        };
    }

    @Override
    public float getShadeBrightness(BlockState state, BlockGetter worldIn, BlockPos pos) {
        return 0.7f;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return type == EnchantedBlockEntityTypes.WITCH_OVEN.get() && !level.isClientSide ? WitchOvenBlockEntity::serverTick : null;
    }
}
