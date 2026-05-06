package net.favouriteless.enchanted.common.blocks;

import net.favouriteless.enchanted.common.blocks.entity.EBlockEntityTypes;
import net.favouriteless.enchanted.common.blocks.entity.MortarBlockEntity;
import net.favouriteless.enchanted.common.util.ItemUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class MortarBlock extends EBaseEntityBlock<MortarBlock> {

    public static final VoxelShape SHAPE = Shapes.box(0.25D, 0.0D, 0.25D, 0.75D, 0.3125D, 0.75D);

    public MortarBlock(Properties properties) {
        super(MortarBlock::new, properties);
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if(level.getBlockEntity(pos) instanceof MortarBlockEntity be && !stack.isEmpty())
            return be.addIngredient(stack) ? ItemInteractionResult.sidedSuccess(level.isClientSide) : ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;

        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if(level.getBlockEntity(pos) instanceof MortarBlockEntity be) {
            if(player.isCrouching()) {
                ItemStack out = be.takeResult();

                if(out.isEmpty())
                    out = be.takeIngredient();

                if(!out.isEmpty()) {
                    ItemUtils.giveOrDrop(player, out, EquipmentSlot.MAINHAND);
                    return InteractionResult.sidedSuccess(level.isClientSide);
                }
            }

            if(be.getInput().isEmpty())
                return InteractionResult.PASS;
            if(!level.isClientSide)
                be.grind();

            return InteractionResult.sidedSuccess(level.isClientSide);
        }
        return InteractionResult.PASS;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new MortarBlockEntity(pos, state);
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return createSidedTickerHelper(level, type, EBlockEntityTypes.MORTAR.get(), MortarBlockEntity::serverTick, MortarBlockEntity::clientTick);
    }

}
