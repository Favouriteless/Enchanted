package com.favouriteless.enchanted.common.blocks.crops;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.VineBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

import javax.annotation.Nullable;

public class SpanishMossBlock extends VineBlock {

    public SpanishMossBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState().setValue(SOUTH, true));
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState clickedState = context.getLevel().getBlockState(context.getClickedPos());
        boolean isMoss = clickedState.is(this);
        BlockState newState = isMoss ? clickedState : this.defaultBlockState().setValue(SOUTH, false);

        for(Direction direction : context.getNearestLookingDirections()) {
            if (direction != Direction.DOWN) {
                BooleanProperty booleanproperty = getPropertyForFace(direction);
                boolean flag1 = isMoss && clickedState.getValue(booleanproperty);

                if (!flag1 && this.canSupportAtFace(context.getLevel(), context.getClickedPos(), direction))
                    return newState.setValue(booleanproperty, true);
            }
        }

        return isMoss ? newState : null;
    }

    private boolean canSupportAtFace(BlockGetter level, BlockPos pos, Direction direction) {
        if (direction == Direction.DOWN)
            return false;
        else {
            BlockPos blockpos = pos.relative(direction);
            if (isAcceptableNeighbour(level, blockpos, direction))
                return true;
            else if (direction.getAxis() == Direction.Axis.Y)
                return false;
            else {
                BooleanProperty booleanproperty = PROPERTY_BY_DIRECTION.get(direction);
                BlockState blockstate = level.getBlockState(pos.above());
                return blockstate.is(this) && blockstate.getValue(booleanproperty);
            }
        }
    }

}
