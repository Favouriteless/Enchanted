package net.favouriteless.enchanted.common.blocks.crops;

import net.favouriteless.enchanted.common.blocks.entity.BloodPoppyBlockEntity;
import net.favouriteless.enchanted.common.items.component.EntityRefData;
import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.FlowerBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BloodPoppyBlock extends FlowerBlock implements EntityBlock {

    public static final BooleanProperty FILLED = BooleanProperty.create("filled");
    private static final VoxelShape SHAPE = Block.box(5.0D, 0.0D, 5.0D, 11.0D, 10.0D, 11.0D);

    public BloodPoppyBlock(Properties properties) {
        super(MobEffects.WITHER, 1200, properties);
        registerDefaultState(defaultBlockState().setValue(FILLED, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(FILLED);
    }

    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if (level.isClientSide || state.getValue(FILLED) || !(entity instanceof LivingEntity)) {
            return;
        }

        if (level.getBlockEntity(pos) instanceof BloodPoppyBlockEntity poppy) {
            poppy.setTaglockData(EntityRefData.of(entity.getUUID(), entity.getDisplayName().getString()));
            level.setBlockAndUpdate(pos, state.setValue(FILLED, true));
        }
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return SHAPE;
    }

    public static void reset(Level level, BlockPos pos) {
        if (level.isClientSide) {
            return;
        }

        if (level.getBlockEntity(pos) instanceof BloodPoppyBlockEntity poppy) {
            BlockState state = level.getBlockState(pos);
            level.setBlockAndUpdate(pos, state.setValue(FILLED, false));

            poppy.reset();
        }
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new BloodPoppyBlockEntity(pos, state);
    }

}
