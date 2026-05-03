package net.favouriteless.enchanted.common.blocks.altar;

import com.mojang.serialization.MapCodec;
import net.favouriteless.enchanted.common.Enchanted;
import net.favouriteless.enchanted.common.blocks.entity.AltarBlockEntity;
import net.favouriteless.enchanted.common.blocks.entity.EBlockEntityTypes;
import net.favouriteless.enchanted.common.enchanted.altar.AltarHelper;
import net.favouriteless.enchanted.common.enchanted.altar.AltarPart;
import net.favouriteless.enchanted.common.init.EBlocks;
import net.favouriteless.enchanted.platform.EServices;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class AltarBlock extends BaseEntityBlock {

    public static final EnumProperty<AltarPart> PART = EnumProperty.create("formed", AltarPart.class);
    public static final BooleanProperty FACING_X = BooleanProperty.create("facing_x");

    public final MapCodec<AltarBlock> codec = simpleCodec(AltarBlock::new);

    public AltarBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(defaultBlockState().setValue(PART, AltarPart.UNFORMED).setValue(FACING_X, true));
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        if(!level.isClientSide)
            AltarHelper.tryFormAltar(level, pos);
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        AltarPart part = state.getValue(PART);

        if(part == AltarPart.UNFORMED || state.equals(newState))
            return;

        if(part == AltarPart.P000)
            level.removeBlockEntity(pos);

        // We pass newState to here to avoid re-setting this block to an altar (dupe bug)
        AltarHelper.tryUnformAltar(level, pos, state);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(PART).add(FACING_X);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
        if(state.getValue(PART) == AltarPart.UNFORMED)
            return InteractionResult.PASS;

        if(level.isClientSide)
            return InteractionResult.SUCCESS;

        BlockPos corePos = AltarHelper.getCorePos(pos, state);
        BlockState coreState = level.getBlockState(corePos);

        if(!coreState.is(EBlocks.ALTAR.get()) || coreState.getValue(PART) != AltarPart.P000) {
            Enchanted.LOG.warn("Altar located at {} was in an invalid state", corePos.toShortString());
            AltarHelper.tryUnformAltar(level, corePos, state);
            return InteractionResult.CONSUME;
        }

        if(level.getBlockEntity(corePos) instanceof AltarBlockEntity be)
            EServices.PLATFORM.openMenu((ServerPlayer)player, be, be.getBlockPos(), BlockPos.STREAM_CODEC);

        return InteractionResult.CONSUME;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return state.getValue(PART) == AltarPart.P000 ? new AltarBlockEntity(pos, state) : null;
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return codec;
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return level.isClientSide ? null : createTickerHelper(type, EBlockEntityTypes.ALTAR.get(), AltarBlockEntity::serverTick);
    }


}
