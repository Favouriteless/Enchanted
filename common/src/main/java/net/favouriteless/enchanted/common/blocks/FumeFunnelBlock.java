package net.favouriteless.enchanted.common.blocks;

import net.favouriteless.enchanted.common.init.EBlocks;
import net.favouriteless.enchanted.common.init.EItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class FumeFunnelBlock extends Block {

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty LIT = BlockStateProperties.LIT;
    public static final IntegerProperty TYPE = IntegerProperty.create("type", 0, 3 );

    private static final VoxelShape SHAPE = Shapes.box(1.0D/16, 0.0D, 1.0D/16, 15.0D/16, 14.0D/16, 15.0D/16);
    private static final VoxelShape SHAPE_TOP_NORTH = Shapes.box(5.0D/16, 0.0D, 8.0D/16, 11.0D/16, 8.0D/16, 14.0D/16);
    private static final VoxelShape SHAPE_TOP_SOUTH = Shapes.box(5.0D/16, 0.0D, 2.0D/16, 11.0D/16, 8.0D/16, 8.0D/16);
    private static final VoxelShape SHAPE_TOP_EAST = Shapes.box(2.0D/16, 0.0D, 5.0D/16, 8.0D/16, 8.0D/16, 11.0D/16);
    private static final VoxelShape SHAPE_TOP_WEST = Shapes.box(8.0D/16, 0.0D, 5.0D/16, 14.0D/16, 8.0D/16, 11.0D/16);

    private final double byproductChance;

    public FumeFunnelBlock(double byproductChance, Properties properties) {
        super(properties);
        this.registerDefaultState(this.getStateDefinition().any().setValue(FACING, Direction.NORTH).setValue(LIT, false).setValue(TYPE, 0));
        this.byproductChance = byproductChance;
    }

    public double getByproductChance() {
        return byproductChance;
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if(stack.is(EItems.FUME_FILTER.get())) {
            if(!level.isClientSide) {
                Direction facing = state.getValue(FACING);
                int type = state.getValue(TYPE);
                boolean lit = state.getValue(LIT);

                level.setBlockAndUpdate(pos, EBlocks.FUME_FUNNEL_FILTERED.get().defaultBlockState()
                        .setValue(TYPE, type)
                        .setValue(FACING, facing)
                        .setValue(LIT, lit)
                );

                if(!player.isCreative())
                    stack.shrink(1);
            }
            return ItemInteractionResult.sidedSuccess(level.isClientSide);
        }
        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return state.getValue(TYPE) == 1 ?
                switch(state.getValue(FACING)) {
                    default -> SHAPE_TOP_NORTH;
                    case SOUTH -> SHAPE_TOP_SOUTH;
                    case EAST -> SHAPE_TOP_EAST;
                    case WEST -> SHAPE_TOP_WEST;
                } : SHAPE;
    }

    public float getShadeBrightness(BlockState state, BlockGetter worldIn, BlockPos pos) {
        return 1.0F;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState state = this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
        return getWitchOvenState(state, context.getLevel(), context.getClickedPos());
    }

    @Override
    public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor level, BlockPos currentPos, BlockPos facingPos) {
        return getWitchOvenState(stateIn, level, currentPos);
    }

    /**
     * Checks for adjacent witch oven and returns appropriate state
     */
    public BlockState getWitchOvenState(BlockState state, LevelAccessor world, BlockPos pos) {
        int type = 3;
        boolean lit = false;

        for(Direction dir : Direction.values()) {
            if(dir != Direction.UP) {
                BlockState ovenState = world.getBlockState(pos.offset(dir.getNormal()));
                if (ovenState.is(EBlocks.WITCH_OVEN.get())) {
                    if (dir != ovenState.getValue(WitchOvenBlock.FACING) && dir != ovenState.getValue(WitchOvenBlock.FACING).getOpposite()) {

                        state = state.setValue(FACING, ovenState.getValue(WitchOvenBlock.FACING));
                        lit = ovenState.getValue(WitchOvenBlock.LIT);

                        if (dir == Direction.DOWN)
                            type = 1;
                        else if(dir == state.getValue(FACING).getClockWise())
                            type = 2;
                        else if(dir == state.getValue(FACING).getCounterClockWise())
                            type = 0;
                    }
                    break;
                }
            }
        }
        return state.setValue(TYPE, type).setValue(LIT, lit);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, LIT, TYPE);
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (state.getValue(LIT)) {
            if (state.getValue(TYPE) != 1) {
                if (random.nextInt(8) == 0) {
                    double d0 = pos.getX() + 0.5D;
                    double d1 = pos.getY();
                    double d2 = pos.getZ() + 0.5D;
                    if (random.nextDouble() < 0.1D)
                        level.playLocalSound(d0, d1, d2, SoundEvents.FURNACE_FIRE_CRACKLE, SoundSource.BLOCKS, 1.0F, 1.0F, false);

                    Direction direction = state.getValue(FACING);
                    Direction.Axis axis = direction.getAxis();
                    double d4 = random.nextDouble() * 0.6D - 0.3D;
                    double d5 = axis == Direction.Axis.X ? (double) direction.getStepX() * 0.49D : d4;
                    double d6 = random.nextDouble() * 6.0D / 16.0D;
                    double d7 = axis == Direction.Axis.Z ? (double) direction.getStepZ() * 0.49D : d4;
                    level.addParticle(ParticleTypes.SMOKE, d0 + d5, d1 + d6, d2 + d7, 0.0D, 0.0D, 0.0D);
                    level.addParticle(ParticleTypes.FLAME, d0 + d5, d1 + d6, d2 + d7, 0.0D, 0.0D, 0.0D);
                }
            }
        }
    }

}
