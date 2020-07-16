package com.favouriteless.magicraft.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.state.IntegerProperty;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ChalkBase extends Block {

    public ChalkBase(Material material) {
        super(Block.Properties.create(material)
                .hardnessAndResistance(2.5f, 0f)
                .sound(SoundType.STONE)
                .notSolid()
                .doesNotBlockMovement()
        );
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
    {
        return VoxelShapes.create(new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 0.0625, 1.0));
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public float getAmbientOcclusionLightValue(BlockState state, IBlockReader worldIn, BlockPos pos) {
        return 1.0F;
    }

    @Override
    public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
        if(!worldIn.getBlockState(pos.add(0, -1, 0)).getBlock().isNormalCube(worldIn.getBlockState(pos.add(0, -1, 0)), worldIn, pos.add(0, -1, 0))) {
            worldIn.destroyBlock(pos, false);
        }
    }

}
