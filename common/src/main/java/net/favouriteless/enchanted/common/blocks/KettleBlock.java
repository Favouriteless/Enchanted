package net.favouriteless.enchanted.common.blocks;

import net.favouriteless.enchanted.common.blocks.entity.EBlockEntityTypes;
import net.favouriteless.enchanted.common.blocks.entity.KettleBlockEntity;
import net.favouriteless.enchanted.common.util.ItemUtils;
import net.favouriteless.enchanted.platform.EServices;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SupportType;
import net.minecraft.world.level.block.WallBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class KettleBlock extends EBaseEntityBlock<KettleBlock> {

    public static EnumProperty<Type> TYPE = EnumProperty.create("type", Type.class);
    public static DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    public static final VoxelShape SHAPE = Shapes.box(0.25D, 0.0D, 0.25D, 0.75D, 0.375D, 0.75D);

    public KettleBlock(Properties properties) {
        super(KettleBlock::new, properties);
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        // INFO: Interactions involving fluids are handled by the respective loader APIs; transfer on fabric & caps on neoforge
        if(level.getBlockEntity(pos) instanceof KettleBlockEntity be) {
            if(!be.isComplete() && EServices.FLUID.playerHoldingFluidContainer(player, hand)) {
                EServices.FLUID.tryItemInteraction(stack, state, level, pos, player, hand, hitResult);
                return ItemInteractionResult.sidedSuccess(level.isClientSide);
            }
        }
        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
         if(level.getBlockEntity(pos) instanceof KettleBlockEntity kettle) {
             if(level.isClientSide)
                 return kettle.isComplete() ? InteractionResult.SUCCESS : InteractionResult.PASS;

             ItemStack result = kettle.takeItem(player.getItemInHand(InteractionHand.MAIN_HAND), false);
             if(result.isEmpty())
                 return InteractionResult.PASS;

             ItemUtils.giveOrDrop(player, result, EquipmentSlot.MAINHAND);
             level.playSound(null, pos, SoundEvents.BUCKET_EMPTY, SoundSource.PLAYERS, 1.0F, 1.0F);
             return InteractionResult.CONSUME;
         }
        return InteractionResult.PASS;
    }

    @Override
    protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if(level.isClientSide)
            return;

        if(entity instanceof ItemEntity itemEntity && level.getBlockEntity(pos) instanceof KettleBlockEntity kettle) {
            if(kettle.addItem(itemEntity.getItem())) {
                level.playSound(null, pos, SoundEvents.BUCKET_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);
                itemEntity.discard();
            }
        }
    }

    public BlockState getKettleState(Level level, BlockPos pos, Direction facing) {
        BlockPos below = pos.below();

        if(level.getBlockState(below).isFaceSturdy(level, below, Direction.UP, SupportType.CENTER))
            return withValues(Type.GROUND, facing);

        Direction left = facing.getCounterClockWise();
        Direction right = facing.getClockWise();
        BlockPos leftPos = pos.relative(left);
        BlockPos rightPos = pos.relative(right);
        BlockState leftState = level.getBlockState(leftPos);
        BlockState rightState = level.getBlockState(rightPos);

        if(leftState.getBlock() instanceof WallBlock && rightState.getBlock() instanceof WallBlock)
            return withValues(Type.HANGING_BEAM, facing);

        if(leftState.isFaceSturdy(level, leftPos, right) && rightState.isFaceSturdy(level, rightPos, left))
            return withValues(Type.HANGING_BEAM, facing);

        Direction back = facing.getOpposite();
        BlockPos frontPos = pos.relative(facing);
        BlockPos backPos = pos.relative(back);
        BlockState frontState = level.getBlockState(frontPos);
        BlockState backState = level.getBlockState(backPos);

        if(frontState.getBlock() instanceof WallBlock && backState.getBlock() instanceof WallBlock)
            return withValues(Type.HANGING_BEAM, right);

        if(frontState.isFaceSturdy(level, frontPos, back) && backState.isFaceSturdy(level, backPos, facing))
            return withValues(Type.HANGING_BEAM, right);


        BlockPos above = pos.above();

        if(level.getBlockState(above).isFaceSturdy(level, above, Direction.DOWN, SupportType.RIGID))
            return withValues(Type.HANGING, facing);

        return withValues(Type.GROUND, facing);
    }

    private BlockState withValues(Type type, Direction facing) {
        return defaultBlockState().setValue(TYPE, type).setValue(FACING, facing);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return createSidedTickerHelper(level, type, EBlockEntityTypes.KETTLE.get(), KettleBlockEntity::serverTick, KettleBlockEntity::clientTick);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new KettleBlockEntity(pos, state);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, TYPE);
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
        BlockState newState = getKettleState(level, pos, state.getValue(FACING));
        if(state != newState)
            level.setBlock(pos, newState, 2);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return getKettleState(context.getLevel(), context.getClickedPos(), context.getHorizontalDirection().getOpposite());
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    public enum Type implements StringRepresentable {
        GROUND,
        HANGING,
        HANGING_BEAM;

        @Override
        public String getSerializedName() {
            return name().toLowerCase();
        }

    }

}

