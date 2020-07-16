package com.favouriteless.magicraft.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Random;

public class ChalkCircle extends ChalkBase {

    // Regular chalk types - red white purple
    public static final IntegerProperty GLYPH = IntegerProperty.create("glyph", 0, 47);
    public static final Random random = new Random();
    private final BasicParticleType particleType;

    public ChalkCircle(Material material, BasicParticleType particleType) {
        super(material);
        this.particleType = particleType;
        this.setDefaultState(this.stateContainer.getBaseState().with(GLYPH, 0));
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) { builder.add(GLYPH); }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return this.getDefaultState().with(GLYPH, random.nextInt(48));
    }

    @OnlyIn(Dist.CLIENT)
    public void animateTick(BlockState state, World world, BlockPos pos, Random rand) {

        if(this.particleType != null) {
            if(random.nextInt(10) == 1)
                world.addParticle(this.particleType, pos.getX() + 0.5D, pos.getY() + 0.1D, pos.getZ() + 0.5D, 0, 0, 0);
        }

    }

}