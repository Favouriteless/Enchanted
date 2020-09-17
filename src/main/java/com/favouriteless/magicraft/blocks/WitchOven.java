package com.favouriteless.magicraft.blocks;

import com.favouriteless.magicraft.init.MagicraftTileEntities;
import com.favouriteless.magicraft.tileentity.WitchOvenTileEntity;
import net.minecraft.block.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.Random;
import java.util.stream.Stream;

public class WitchOven extends ContainerBlock {

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty LIT = BlockStateProperties.LIT;

    private static final VoxelShape SHAPE_NORTH = Stream.of(
            Block.makeCuboidShape(2, 2, 2, 14, 3, 14), Block.makeCuboidShape(14, 0, 1, 15, 2, 2), Block.makeCuboidShape(14, 2, 1, 15, 3, 15),
            Block.makeCuboidShape(1, 2, 1, 2, 3, 15), Block.makeCuboidShape(1, 0, 1, 2, 2, 2), Block.makeCuboidShape(14, 0, 14, 15, 2, 15),
            Block.makeCuboidShape(1, 0, 14, 2, 2, 15), Block.makeCuboidShape(1, 9, 1, 15, 10, 15), Block.makeCuboidShape(2, 3, 2, 14, 9, 14),
            Block.makeCuboidShape(3, 10, 3, 13, 11, 13), Block.makeCuboidShape(6, 10, 9, 10, 16, 13), Block.makeCuboidShape(5, 10, 8, 11, 12, 14)
    ).reduce((v1, v2) -> {return VoxelShapes.combineAndSimplify(v1, v2, IBooleanFunction.OR);}).get();
    public static final VoxelShape SHAPE_SOUTH = Stream.of(
            Block.makeCuboidShape(2, 2, 2, 14, 3, 14), Block.makeCuboidShape(1, 0, 14, 2, 2, 15), Block.makeCuboidShape(1, 2, 1, 2, 3, 15),
            Block.makeCuboidShape(14, 2, 1, 15, 3, 15), Block.makeCuboidShape(14, 0, 14, 15, 2, 15), Block.makeCuboidShape(1, 0, 1, 2, 2, 2),
            Block.makeCuboidShape(14, 0, 1, 15, 2, 2), Block.makeCuboidShape(1, 9, 1, 15, 10, 15), Block.makeCuboidShape(2, 3, 2, 14, 9, 14),
            Block.makeCuboidShape(3, 10, 3, 13, 11, 13), Block.makeCuboidShape(6, 10, 3, 10, 16, 7), Block.makeCuboidShape(5, 10, 2, 11, 12, 8)
    ).reduce((v1, v2) -> {return VoxelShapes.combineAndSimplify(v1, v2, IBooleanFunction.OR);}).get();
    public static final VoxelShape SHAPE_WEST = Stream.of(
            Block.makeCuboidShape(2, 2, 2, 14, 3, 14), Block.makeCuboidShape(1, 0, 1, 2, 2, 2), Block.makeCuboidShape(1, 2, 1, 15, 3, 2),
            Block.makeCuboidShape(1, 2, 14, 15, 3, 15), Block.makeCuboidShape(1, 0, 14, 2, 2, 15), Block.makeCuboidShape(14, 0, 1, 15, 2, 2),
            Block.makeCuboidShape(14, 0, 14, 15, 2, 15), Block.makeCuboidShape(1, 9, 1, 15, 10, 15), Block.makeCuboidShape(2, 3, 2, 14, 9, 14),
            Block.makeCuboidShape(3, 10, 3, 13, 11, 13), Block.makeCuboidShape(9, 10, 6, 13, 16, 10), Block.makeCuboidShape(8, 10, 5, 14, 12, 11)
    ).reduce((v1, v2) -> {return VoxelShapes.combineAndSimplify(v1, v2, IBooleanFunction.OR);}).get();
    public static final VoxelShape SHAPE_EAST = Stream.of(
            Block.makeCuboidShape(2, 2, 2, 14, 3, 14), Block.makeCuboidShape(14, 0, 14, 15, 2, 15), Block.makeCuboidShape(1, 2, 14, 15, 3, 15),
            Block.makeCuboidShape(1, 2, 1, 15, 3, 2), Block.makeCuboidShape(14, 0, 1, 15, 2, 2), Block.makeCuboidShape(1, 0, 14, 2, 2, 15),
            Block.makeCuboidShape(1, 0, 1, 2, 2, 2), Block.makeCuboidShape(1, 9, 1, 15, 10, 15), Block.makeCuboidShape(2, 3, 2, 14, 9, 14),
            Block.makeCuboidShape(3, 10, 3, 13, 11, 13), Block.makeCuboidShape(3, 10, 6, 7, 16, 10), Block.makeCuboidShape(2, 10, 5, 8, 12, 11)
    ).reduce((v1, v2) -> {return VoxelShapes.combineAndSimplify(v1, v2, IBooleanFunction.OR);}).get();



    public WitchOven(AbstractBlock.Properties builder) {
        super(builder);
        this.setDefaultState(this.getStateContainer().getBaseState().with(FACING, Direction.NORTH).with(LIT, false));
    }


    //--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------


    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return MagicraftTileEntities.WITCH_OVEN.get().create();
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(IBlockReader worldIn) {
        return new WitchOvenTileEntity();
    }


    //--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------


    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult rayTraceResult) {
        if(!worldIn.isRemote()) {
            TileEntity tileEntity = worldIn.getTileEntity(pos);
            if(tileEntity instanceof WitchOvenTileEntity) {
                NetworkHooks.openGui((ServerPlayerEntity)player, (WitchOvenTileEntity)tileEntity, pos);
                return ActionResultType.SUCCESS;
            }
        }
        return ActionResultType.SUCCESS;
    }

    @Override
    public void onReplaced(BlockState state, World world, BlockPos blockPos, BlockState newState, boolean isMoving) {
        if (state.getBlock() != newState.getBlock()) {
            TileEntity tileentity = world.getTileEntity(blockPos);
            if (tileentity instanceof WitchOvenTileEntity) {
                WitchOvenTileEntity tileEntity = (WitchOvenTileEntity)tileentity;
                InventoryHelper.dropInventoryItems(world, blockPos, tileEntity);
            }
            super.onReplaced(state, world, blockPos, newState, isMoving);  // call it last, because it removes the TileEntity
        }
    }


    //--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------


    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        switch(state.get(FACING)) {
            default:
                return SHAPE_NORTH;
            case SOUTH:
                return SHAPE_SOUTH;
            case WEST:
                return SHAPE_WEST;
            case EAST:
                return SHAPE_EAST;
        }
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public float getAmbientOcclusionLightValue(BlockState state, IBlockReader worldIn, BlockPos pos) {
        return 0.7f;
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return this.getDefaultState().with(FACING, context.getPlacementHorizontalFacing().getOpposite());
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(FACING, LIT);
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rot) {
        return state;
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirrorIn) {
        return state;
    }

    @Override
    public BlockRenderType getRenderType(BlockState iBlockState) {
        return BlockRenderType.MODEL;
    }

    @OnlyIn(Dist.CLIENT)
    public void animateTick(BlockState state, World world, BlockPos pos, Random rand) {
        if (state.get(LIT)) {
            double d0 = (double) pos.getX() + 0.5D;
            double d1 = (double) pos.getY();
            double d2 = (double) pos.getZ() + 0.5D;
            if (rand.nextDouble() < 0.1D) {
                world.playSound(d0, d1, d2, SoundEvents.BLOCK_FURNACE_FIRE_CRACKLE, SoundCategory.BLOCKS, 1.0F, 1.0F, false);
            }

            Direction direction = state.get(FACING);
            Direction.Axis direction$axis = direction.getAxis();
            double d3 = 0.52D;
            double d4 = rand.nextDouble() * 0.6D - 0.3D;
            double d5 = direction$axis == Direction.Axis.X ? (double) direction.getXOffset() * 0.49D : d4;
            double d6 = rand.nextDouble() * 6.0D / 16.0D;
            double d7 = direction$axis == Direction.Axis.Z ? (double) direction.getZOffset() * 0.49D : d4;
            world.addParticle(ParticleTypes.SMOKE, d0 + d5, d1 + d6, d2 + d7, 0.0D, 0.0D, 0.0D);
            world.addParticle(ParticleTypes.FLAME, d0 + d5, d1 + d6, d2 + d7, 0.0D, 0.0D, 0.0D);
        }
    }

}