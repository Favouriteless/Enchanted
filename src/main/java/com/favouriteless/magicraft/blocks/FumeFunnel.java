package com.favouriteless.magicraft.blocks;

import com.favouriteless.magicraft.init.MagicraftBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ToolType;

import java.util.Random;
import java.util.stream.Stream;

public class FumeFunnel extends Block {

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty LIT = BlockStateProperties.LIT;
    public static final IntegerProperty TYPE = IntegerProperty.create("type", 0, 3 );

    private static final VoxelShape SHAPE_0_NORTH = Stream.of(
            Block.makeCuboidShape(14, 3, 0, 15, 6, 1),
            Block.makeCuboidShape(12, 6, 0, 15, 7, 1), Block.makeCuboidShape(1, 0, 1, 15, 1, 15), Block.makeCuboidShape(1, 12, 1, 15, 13, 15),
            Block.makeCuboidShape(3, 13, 3, 13, 14, 13), Block.makeCuboidShape(11, 1, 1, 13, 5, 2), Block.makeCuboidShape(3, 1, 1, 5, 5, 2),
            Block.makeCuboidShape(5, 1, 1, 11, 6, 2), Block.makeCuboidShape(2, 1, 2, 14, 12, 14), Block.makeCuboidShape(2, 0, 0, 14, 1, 1),
            Block.makeCuboidShape(0, 5, 9, 2, 8, 12), Block.makeCuboidShape(-2, 5, 9, 0, 8, 12), Block.makeCuboidShape(12, 6, 1, 13, 7, 2),
            Block.makeCuboidShape(5, 9, 1, 11, 10, 2), Block.makeCuboidShape(10, 3, 15, 14, 4, 16), Block.makeCuboidShape(10, 7, 14, 11, 8, 15),
            Block.makeCuboidShape(4, 3, 14, 7, 10, 15), Block.makeCuboidShape(10, 4, 15, 11, 8, 16), Block.makeCuboidShape(14, 3, 1, 15, 4, 16)
    ).reduce((v1, v2) -> {return VoxelShapes.combineAndSimplify(v1, v2, IBooleanFunction.OR);}).get();
    private static final VoxelShape SHAPE_0_SOUTH = Stream.of(
            Block.makeCuboidShape(1, 3, 15, 2, 6, 16),
            Block.makeCuboidShape(1, 6, 15, 4, 7, 16), Block.makeCuboidShape(1, 0, 1, 15, 1, 15), Block.makeCuboidShape(1, 12, 1, 15, 13, 15),
            Block.makeCuboidShape(3, 13, 3, 13, 14, 13), Block.makeCuboidShape(3, 1, 14, 5, 5, 15), Block.makeCuboidShape(11, 1, 14, 13, 5, 15),
            Block.makeCuboidShape(5, 1, 14, 11, 6, 15), Block.makeCuboidShape(2, 1, 2, 14, 12, 14), Block.makeCuboidShape(2, 0, 15, 14, 1, 16),
            Block.makeCuboidShape(14, 5, 4, 16, 8, 7), Block.makeCuboidShape(16, 5, 4, 18, 8, 7), Block.makeCuboidShape(3, 6, 14, 4, 7, 15),
            Block.makeCuboidShape(5, 9, 14, 11, 10, 15), Block.makeCuboidShape(2, 3, 0, 6, 4, 1), Block.makeCuboidShape(5, 7, 1, 6, 8, 2),
            Block.makeCuboidShape(9, 3, 1, 12, 10, 2), Block.makeCuboidShape(5, 4, 0, 6, 8, 1), Block.makeCuboidShape(1, 3, 0, 2, 4, 15)
    ).reduce((v1, v2) -> {return VoxelShapes.combineAndSimplify(v1, v2, IBooleanFunction.OR);}).get();
    private static final VoxelShape SHAPE_0_EAST = Stream.of(
            Block.makeCuboidShape(15, 3, 14, 16, 6, 15),
            Block.makeCuboidShape(15, 6, 12, 16, 7, 15), Block.makeCuboidShape(1, 0, 1, 15, 1, 15), Block.makeCuboidShape(1, 12, 1, 15, 13, 15),
            Block.makeCuboidShape(3, 13, 3, 13, 14, 13), Block.makeCuboidShape(14, 1, 11, 15, 5, 13), Block.makeCuboidShape(14, 1, 3, 15, 5, 5),
            Block.makeCuboidShape(14, 1, 5, 15, 6, 11), Block.makeCuboidShape(2, 1, 2, 14, 12, 14), Block.makeCuboidShape(15, 0, 2, 16, 1, 14),
            Block.makeCuboidShape(4, 5, 0, 7, 8, 2), Block.makeCuboidShape(4, 5, -2, 7, 8, 0), Block.makeCuboidShape(14, 6, 12, 15, 7, 13),
            Block.makeCuboidShape(14, 9, 5, 15, 10, 11), Block.makeCuboidShape(0, 3, 10, 1, 4, 14), Block.makeCuboidShape(1, 7, 10, 2, 8, 11),
            Block.makeCuboidShape(1, 3, 4, 2, 10, 7), Block.makeCuboidShape(0, 4, 10, 1, 8, 11), Block.makeCuboidShape(0, 3, 14, 15, 4, 15)
    ).reduce((v1, v2) -> {return VoxelShapes.combineAndSimplify(v1, v2, IBooleanFunction.OR);}).get();
    private static final VoxelShape SHAPE_0_WEST = Stream.of(
            Block.makeCuboidShape(0, 3, 1, 1, 6, 2),
            Block.makeCuboidShape(0, 6, 1, 1, 7, 4), Block.makeCuboidShape(1, 0, 1, 15, 1, 15), Block.makeCuboidShape(1, 12, 1, 15, 13, 15),
            Block.makeCuboidShape(3, 13, 3, 13, 14, 13), Block.makeCuboidShape(1, 1, 3, 2, 5, 5), Block.makeCuboidShape(1, 1, 11, 2, 5, 13),
            Block.makeCuboidShape(1, 1, 5, 2, 6, 11), Block.makeCuboidShape(2, 1, 2, 14, 12, 14), Block.makeCuboidShape(0, 0, 2, 1, 1, 14),
            Block.makeCuboidShape(9, 5, 14, 12, 8, 16), Block.makeCuboidShape(9, 5, 16, 12, 8, 18), Block.makeCuboidShape(1, 6, 3, 2, 7, 4),
            Block.makeCuboidShape(1, 9, 5, 2, 10, 11), Block.makeCuboidShape(15, 3, 2, 16, 4, 6), Block.makeCuboidShape(14, 7, 5, 15, 8, 6),
            Block.makeCuboidShape(14, 3, 9, 15, 10, 12), Block.makeCuboidShape(15, 4, 5, 16, 8, 6), Block.makeCuboidShape(1, 3, 1, 16, 4, 2)
    ).reduce((v1, v2) -> {return VoxelShapes.combineAndSimplify(v1, v2, IBooleanFunction.OR);}).get();

    private static final VoxelShape SHAPE_1_NORTH = VoxelShapes.combineAndSimplify(Block.makeCuboidShape(5, 6, 8, 11, 8, 14), Block.makeCuboidShape(6, 0, 9, 10, 6, 13), IBooleanFunction.OR);
    private static final VoxelShape SHAPE_1_SOUTH = VoxelShapes.combineAndSimplify(Block.makeCuboidShape(5, 6, 2, 11, 8, 8), Block.makeCuboidShape(6, 0, 3, 10, 6, 7), IBooleanFunction.OR);
    private static final VoxelShape SHAPE_1_EAST = VoxelShapes.combineAndSimplify(Block.makeCuboidShape(2, 6, 5, 8, 8, 11), Block.makeCuboidShape(3, 0, 6, 7, 6, 10), IBooleanFunction.OR);
    private static final VoxelShape SHAPE_1_WEST = VoxelShapes.combineAndSimplify(Block.makeCuboidShape(8, 6, 5, 14, 8, 11), Block.makeCuboidShape(9, 0, 6, 13, 6, 10), IBooleanFunction.OR);

    private static final VoxelShape SHAPE_2_NORTH = Stream.of(
            Block.makeCuboidShape(13, 13, 13, 14, 16, 14), Block.makeCuboidShape(1, 0, 1, 15, 1, 15),
            Block.makeCuboidShape(1, 12, 1, 15, 13, 15), Block.makeCuboidShape(3, 13, 3, 13, 14, 13), Block.makeCuboidShape(11, 1, 1, 13, 5, 2),
            Block.makeCuboidShape(3, 1, 1, 5, 5, 2), Block.makeCuboidShape(5, 1, 1, 11, 6, 2), Block.makeCuboidShape(2, 1, 2, 14, 12, 14),
            Block.makeCuboidShape(2, 0, 0, 14, 1, 1), Block.makeCuboidShape(16, 6, 5, 18, 9, 8), Block.makeCuboidShape(14, 6, 5, 16, 9, 8),
            Block.makeCuboidShape(1, 3, 11, 2, 7, 12), Block.makeCuboidShape(1, 3, 9, 2, 7, 10), Block.makeCuboidShape(5, 9, 1, 11, 10, 2),
            Block.makeCuboidShape(9, 3, 14, 12, 10, 15), Block.makeCuboidShape(1, 3, 5, 2, 4, 6), Block.makeCuboidShape(0, 3, 4, 1, 4, 6),
            Block.makeCuboidShape(0, 3, 3, 1, 15, 4), Block.makeCuboidShape(0, 15, 3, 13, 16, 4), Block.makeCuboidShape(13, 15, 3, 14, 16, 13)
    ).reduce((v1, v2) -> {return VoxelShapes.combineAndSimplify(v1, v2, IBooleanFunction.OR);}).get();
    private static final VoxelShape SHAPE_2_SOUTH = Stream.of(
            Block.makeCuboidShape(2, 13, 2, 3, 16, 3), Block.makeCuboidShape(1, 0, 1, 15, 1, 15),
            Block.makeCuboidShape(1, 12, 1, 15, 13, 15), Block.makeCuboidShape(3, 13, 3, 13, 14, 13), Block.makeCuboidShape(3, 1, 14, 5, 5, 15),
            Block.makeCuboidShape(11, 1, 14, 13, 5, 15), Block.makeCuboidShape(5, 1, 14, 11, 6, 15), Block.makeCuboidShape(2, 1, 2, 14, 12, 14),
            Block.makeCuboidShape(2, 0, 15, 14, 1, 16), Block.makeCuboidShape(-2, 6, 8, 0, 9, 11), Block.makeCuboidShape(0, 6, 8, 2, 9, 11),
            Block.makeCuboidShape(14, 3, 4, 15, 7, 5), Block.makeCuboidShape(14, 3, 6, 15, 7, 7), Block.makeCuboidShape(5, 9, 14, 11, 10, 15),
            Block.makeCuboidShape(4, 3, 1, 7, 10, 2), Block.makeCuboidShape(14, 3, 10, 15, 4, 11), Block.makeCuboidShape(15, 3, 10, 16, 4, 12),
            Block.makeCuboidShape(15, 3, 12, 16, 15, 13), Block.makeCuboidShape(3, 15, 12, 16, 16, 13), Block.makeCuboidShape(2, 15, 3, 3, 16, 13)
    ).reduce((v1, v2) -> {return VoxelShapes.combineAndSimplify(v1, v2, IBooleanFunction.OR);}).get();
    private static final VoxelShape SHAPE_2_EAST = Stream.of(
            Block.makeCuboidShape(2, 13, 13, 3, 16, 14), Block.makeCuboidShape(1, 0, 1, 15, 1, 15),
            Block.makeCuboidShape(1, 12, 1, 15, 13, 15), Block.makeCuboidShape(3, 13, 3, 13, 14, 13), Block.makeCuboidShape(14, 1, 11, 15, 5, 13),
            Block.makeCuboidShape(14, 1, 3, 15, 5, 5), Block.makeCuboidShape(14, 1, 5, 15, 6, 11), Block.makeCuboidShape(2, 1, 2, 14, 12, 14),
            Block.makeCuboidShape(15, 0, 2, 16, 1, 14), Block.makeCuboidShape(8, 6, 16, 11, 9, 18), Block.makeCuboidShape(8, 6, 14, 11, 9, 16),
            Block.makeCuboidShape(4, 3, 1, 5, 7, 2), Block.makeCuboidShape(6, 3, 1, 7, 7, 2), Block.makeCuboidShape(14, 9, 5, 15, 10, 11),
            Block.makeCuboidShape(1, 3, 9, 2, 10, 12), Block.makeCuboidShape(10, 3, 1, 11, 4, 2), Block.makeCuboidShape(10, 3, 0, 12, 4, 1),
            Block.makeCuboidShape(12, 3, 0, 13, 15, 1), Block.makeCuboidShape(12, 15, 0, 13, 16, 13), Block.makeCuboidShape(3, 15, 13, 13, 16, 14)
    ).reduce((v1, v2) -> {return VoxelShapes.combineAndSimplify(v1, v2, IBooleanFunction.OR);}).get();
    private static final VoxelShape SHAPE_2_WEST = Stream.of(
            Block.makeCuboidShape(13, 13, 2, 14, 16, 3), Block.makeCuboidShape(1, 0, 1, 15, 1, 15),
            Block.makeCuboidShape(1, 12, 1, 15, 13, 15), Block.makeCuboidShape(3, 13, 3, 13, 14, 13), Block.makeCuboidShape(1, 1, 3, 2, 5, 5),
            Block.makeCuboidShape(1, 1, 11, 2, 5, 13), Block.makeCuboidShape(1, 1, 5, 2, 6, 11), Block.makeCuboidShape(2, 1, 2, 14, 12, 14),
            Block.makeCuboidShape(0, 0, 2, 1, 1, 14), Block.makeCuboidShape(5, 6, -2, 8, 9, 0), Block.makeCuboidShape(5, 6, 0, 8, 9, 2),
            Block.makeCuboidShape(11, 3, 14, 12, 7, 15), Block.makeCuboidShape(9, 3, 14, 10, 7, 15), Block.makeCuboidShape(1, 9, 5, 2, 10, 11),
            Block.makeCuboidShape(14, 3, 4, 15, 10, 7), Block.makeCuboidShape(5, 3, 14, 6, 4, 15), Block.makeCuboidShape(4, 3, 15, 6, 4, 16),
            Block.makeCuboidShape(3, 3, 15, 4, 15, 16), Block.makeCuboidShape(3, 15, 3, 4, 16, 16), Block.makeCuboidShape(3, 15, 2, 13, 16, 3)
    ).reduce((v1, v2) -> {return VoxelShapes.combineAndSimplify(v1, v2, IBooleanFunction.OR);}).get();

    private static final VoxelShape SHAPE_DEFAULT_NORTH = Stream.of(
            Block.makeCuboidShape(14, 3, 0, 15, 6, 1), Block.makeCuboidShape(12, 6, 0, 15, 7, 1),
            Block.makeCuboidShape(1, 0, 1, 15, 1, 15), Block.makeCuboidShape(1, 12, 1, 15, 13, 15), Block.makeCuboidShape(3, 13, 3, 13, 14, 13),
            Block.makeCuboidShape(11, 1, 1, 13, 5, 2), Block.makeCuboidShape(3, 1, 1, 5, 5, 2), Block.makeCuboidShape(5, 1, 1, 11, 6, 2),
            Block.makeCuboidShape(2, 1, 2, 14, 12, 14), Block.makeCuboidShape(2, 0, 0, 14, 1, 1), Block.makeCuboidShape(12, 6, 1, 13, 7, 2),
            Block.makeCuboidShape(5, 9, 1, 11, 10, 2), Block.makeCuboidShape(10, 3, 15, 14, 4, 16), Block.makeCuboidShape(10, 7, 14, 11, 8, 15),
            Block.makeCuboidShape(4, 3, 14, 7, 10, 15), Block.makeCuboidShape(10, 4, 15, 11, 8, 16), Block.makeCuboidShape(14, 3, 1, 15, 4, 16)
    ).reduce((v1, v2) -> {return VoxelShapes.combineAndSimplify(v1, v2, IBooleanFunction.OR);}).get();
    private static final VoxelShape SHAPE_DEFAULT_SOUTH = Stream.of(
            Block.makeCuboidShape(1, 3, 15, 2, 6, 16), Block.makeCuboidShape(1, 6, 15, 4, 7, 16),
            Block.makeCuboidShape(1, 0, 1, 15, 1, 15), Block.makeCuboidShape(1, 12, 1, 15, 13, 15), Block.makeCuboidShape(3, 13, 3, 13, 14, 13),
            Block.makeCuboidShape(3, 1, 14, 5, 5, 15), Block.makeCuboidShape(11, 1, 14, 13, 5, 15), Block.makeCuboidShape(5, 1, 14, 11, 6, 15),
            Block.makeCuboidShape(2, 1, 2, 14, 12, 14), Block.makeCuboidShape(2, 0, 15, 14, 1, 16), Block.makeCuboidShape(3, 6, 14, 4, 7, 15),
            Block.makeCuboidShape(5, 9, 14, 11, 10, 15), Block.makeCuboidShape(2, 3, 0, 6, 4, 1), Block.makeCuboidShape(5, 7, 1, 6, 8, 2),
            Block.makeCuboidShape(9, 3, 1, 12, 10, 2), Block.makeCuboidShape(5, 4, 0, 6, 8, 1), Block.makeCuboidShape(1, 3, 0, 2, 4, 15)
    ).reduce((v1, v2) -> {return VoxelShapes.combineAndSimplify(v1, v2, IBooleanFunction.OR);}).get();
    private static final VoxelShape SHAPE_DEFAULT_WEST = Stream.of(
            Block.makeCuboidShape(0, 3, 1, 1, 6, 2), Block.makeCuboidShape(0, 6, 1, 1, 7, 4),
            Block.makeCuboidShape(1, 0, 1, 15, 1, 15), Block.makeCuboidShape(1, 12, 1, 15, 13, 15), Block.makeCuboidShape(3, 13, 3, 13, 14, 13),
            Block.makeCuboidShape(1, 1, 3, 2, 5, 5), Block.makeCuboidShape(1, 1, 11, 2, 5, 13), Block.makeCuboidShape(1, 1, 5, 2, 6, 11),
            Block.makeCuboidShape(2, 1, 2, 14, 12, 14), Block.makeCuboidShape(0, 0, 2, 1, 1, 14), Block.makeCuboidShape(1, 6, 3, 2, 7, 4),
            Block.makeCuboidShape(1, 9, 5, 2, 10, 11), Block.makeCuboidShape(15, 3, 2, 16, 4, 6), Block.makeCuboidShape(14, 7, 5, 15, 8, 6),
            Block.makeCuboidShape(14, 3, 9, 15, 10, 12), Block.makeCuboidShape(15, 4, 5, 16, 8, 6), Block.makeCuboidShape(1, 3, 1, 16, 4, 2)
    ).reduce((v1, v2) -> {return VoxelShapes.combineAndSimplify(v1, v2, IBooleanFunction.OR);}).get();
    private static final VoxelShape SHAPE_DEFAULT_EAST = Stream.of(
            Block.makeCuboidShape(15, 3, 14, 16, 6, 15), Block.makeCuboidShape(15, 6, 12, 16, 7, 15),
            Block.makeCuboidShape(1, 0, 1, 15, 1, 15), Block.makeCuboidShape(1, 12, 1, 15, 13, 15), Block.makeCuboidShape(3, 13, 3, 13, 14, 13),
            Block.makeCuboidShape(14, 1, 11, 15, 5, 13), Block.makeCuboidShape(14, 1, 3, 15, 5, 5), Block.makeCuboidShape(14, 1, 5, 15, 6, 11),
            Block.makeCuboidShape(2, 1, 2, 14, 12, 14), Block.makeCuboidShape(15, 0, 2, 16, 1, 14), Block.makeCuboidShape(14, 6, 12, 15, 7, 13),
            Block.makeCuboidShape(14, 9, 5, 15, 10, 11), Block.makeCuboidShape(0, 3, 10, 1, 4, 14), Block.makeCuboidShape(1, 7, 10, 2, 8, 11),
            Block.makeCuboidShape(1, 3, 4, 2, 10, 7), Block.makeCuboidShape(0, 4, 10, 1, 8, 11), Block.makeCuboidShape(0, 3, 14, 15, 4, 15)
    ).reduce((v1, v2) -> {return VoxelShapes.combineAndSimplify(v1, v2, IBooleanFunction.OR);}).get();

    public FumeFunnel() {
        super(Block.Properties.create(Material.IRON)
                .hardnessAndResistance(5.0F, 6.0F)
                .sound(SoundType.ANVIL)
                .notSolid()
                .harvestLevel(1)
                .harvestTool(ToolType.PICKAXE)
        );
        this.setDefaultState(this.getStateContainer().getBaseState().with(FACING, Direction.NORTH).with(LIT, false).with(TYPE, 0));
    }

    //------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        switch(state.get(FACING)) {
            default:
                switch(state.get(TYPE)) {
                    default:
                        return SHAPE_0_NORTH;
                    case 1:
                        return SHAPE_1_NORTH;
                    case 2:
                        return SHAPE_2_NORTH;
                    case 3:
                        return SHAPE_DEFAULT_NORTH;
                }
            case SOUTH:
                switch(state.get(TYPE)) {
                    default:
                        return SHAPE_0_SOUTH;
                    case 1:
                        return SHAPE_1_SOUTH;
                    case 2:
                        return SHAPE_2_SOUTH;
                    case 3:
                        return SHAPE_DEFAULT_SOUTH;
                }
            case WEST:
                switch(state.get(TYPE)) {
                    default:
                        return SHAPE_0_WEST;
                    case 1:
                        return SHAPE_1_WEST;
                    case 2:
                        return SHAPE_2_WEST;
                    case 3:
                        return SHAPE_DEFAULT_WEST;
                }
            case EAST:
                switch(state.get(TYPE)) {
                    default:
                        return SHAPE_0_EAST;
                    case 1:
                        return SHAPE_1_EAST;
                    case 2:
                        return SHAPE_2_EAST;
                    case 3:
                        return SHAPE_DEFAULT_EAST;
                }
        }
    }

    @Override
    public int getLightValue(BlockState state) {
        return state.get(LIT) ? 13 : 0;
    }

    @OnlyIn(Dist.CLIENT)
    public float getAmbientOcclusionLightValue(BlockState state, IBlockReader worldIn, BlockPos pos) {
        return 1.0F;
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        BlockPos pos = context.getPos();
        World world = context.getWorld();

        Direction _facing = context.getPlacementHorizontalFacing().getOpposite();
        int _type = 3;
        boolean _lit = false;

        for(Direction dir : Direction.values()) {
            if(dir != Direction.UP) {
                if (world.getBlockState(pos.add(dir.getDirectionVec())).getBlock() == MagicraftBlocks.WITCH_OVEN.get()) {
                    BlockState ovenState = world.getBlockState(pos.add(dir.getDirectionVec()));

                    if (dir != ovenState.get(WitchOven.FACING) && dir != ovenState.get(WitchOven.FACING).getOpposite()) {

                        _facing = ovenState.get(WitchOven.FACING);
                        _lit = ovenState.get(WitchOven.LIT);

                        if (dir == Direction.DOWN) {
                            _type = 1;
                        }
                        else if(dir == _facing.rotateY()) {
                            _type = 2;
                        }
                        else if(dir == _facing.rotateYCCW()) {
                            _type = 0;
                        }
                    }
                    break;
                }
            }
        }

        return this.getDefaultState().with(FACING, _facing).with(TYPE, _type).with(LIT, _lit);
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(FACING, LIT, TYPE);
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
            if (state.get(TYPE) != 1) {
                if (rand.nextInt(8) == 0) {
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
    }

    @Override
    public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {

        BlockPos pos = currentPos;
        World world = worldIn.getWorld();

        Direction _facing = stateIn.get(FACING);
        int _type = 3;
        boolean _lit = false;

        for(Direction dir : Direction.values()) {
            if(dir != Direction.UP) {
                if (world.getBlockState(pos.add(dir.getDirectionVec())).getBlock() == MagicraftBlocks.WITCH_OVEN.get()) {
                    BlockState ovenState = world.getBlockState(pos.add(dir.getDirectionVec()));

                    if (dir != ovenState.get(WitchOven.FACING) && dir != ovenState.get(WitchOven.FACING).getOpposite()) {

                        _facing = ovenState.get(WitchOven.FACING);
                        _lit = ovenState.get(WitchOven.LIT);

                        if (dir == Direction.DOWN) {
                            _type = 1;
                        }
                        else if(dir == _facing.rotateY()) {
                            _type = 2;
                        }
                        else if(dir == _facing.rotateYCCW()) {
                            _type = 0;
                        }
                    }
                    break;
                }
            }
        }

        return this.getDefaultState().with(FACING, _facing).with(TYPE, _type).with(LIT, _lit);
    }

}
