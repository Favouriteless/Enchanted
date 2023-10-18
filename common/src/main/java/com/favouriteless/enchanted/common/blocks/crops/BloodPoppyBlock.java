package com.favouriteless.enchanted.common.blocks.crops;

import com.favouriteless.enchanted.common.blockentities.BloodPoppyBlockEntity;
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
import org.jetbrains.annotations.Nullable;

public class BloodPoppyBlock extends FlowerBlock implements EntityBlock {

    public static final BooleanProperty FILLED = BooleanProperty.create("filled");

    public BloodPoppyBlock(Properties properties) {
        super(MobEffects.WITHER, 1200, properties);
        registerDefaultState(defaultBlockState().setValue(FILLED, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(FILLED);
    }

    @Override
    public void entityInside(BlockState state, Level world, BlockPos pos, Entity entity) {
        if(!world.isClientSide && entity instanceof LivingEntity) {
            if(!state.getValue(FILLED)) {
                BlockEntity t = world.getBlockEntity(pos);
                if(t instanceof BloodPoppyBlockEntity be) {
                    be.setUUID(entity.getUUID());
                    be.setName(entity.getDisplayName().getString());
                    world.setBlockAndUpdate(pos, state.setValue(FILLED, true));
                } else {
                    throw new IllegalStateException(String.format("Blood poppy at %s, %s, %s has invalid BlockEntity", pos.getX(), pos.getY(), pos.getZ()));
                }
            }
        }
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return Block.box(5.0D, 0.0D, 5.0D, 11.0D, 10.0D, 11.0D);
    }

    public static void reset(Level world, BlockPos pos) {
        if(!world.isClientSide) {
            BloodPoppyBlockEntity blockEntity = (BloodPoppyBlockEntity)world.getBlockEntity(pos);
            BlockState state = world.getBlockState(pos);

            world.setBlockAndUpdate(pos, state.setValue(FILLED, false));
            blockEntity.reset();
        }
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new BloodPoppyBlockEntity(pos, state);
    }

}
