package net.favouriteless.enchanted.common.blocks.chalk;

import net.favouriteless.enchanted.common.blocks.entity.EBlockEntityTypes;
import net.favouriteless.enchanted.common.blocks.entity.GoldChalkBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public class GoldChalkBlock extends AbstractChalkBlock implements EntityBlock {

    public static final IntegerProperty GLYPH = IntegerProperty.create("glyph", 0, 3);
    public static final Random random = new Random();

    public GoldChalkBlock() {
        super();
        this.registerDefaultState(getStateDefinition().any().setValue(GLYPH, 0));
    }

    @Override
    public BlockState getRandomState() {
        return defaultBlockState().setValue(GLYPH, random.nextInt(4));
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new GoldChalkBlockEntity(pos, state);
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (state != newState) {
            //            if(level.getBlockEntity(key) instanceof ChalkGoldBlockEntity be)
            //                be.clearRite();
        }
        super.onRemove(state, level, pos, newState, isMoving);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(GLYPH);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return getRandomState();
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (level.getBlockEntity(pos) instanceof GoldChalkBlockEntity be) {
            be.use(level, pos, player);
        }
        return InteractionResult.SUCCESS;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return !level.isClientSide ? createTickerHelper(type, EBlockEntityTypes.GOLD_CHALK.get(), GoldChalkBlockEntity::serverTick) : null;
    }

    @Nullable
    @SuppressWarnings("unchecked")
    protected static <E extends BlockEntity, A extends BlockEntity> BlockEntityTicker<A> createTickerHelper(
            BlockEntityType<A> serverType, BlockEntityType<E> clientType, BlockEntityTicker<? super E> ticker) {
        return clientType == serverType ? (BlockEntityTicker<A>) ticker : null;
    }

}