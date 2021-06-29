package com.favouriteless.enchanted.common.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Random;

public class ChalkCircleBlock extends ChalkBlockBase {

    // Regular chalk types - white red purple
    public static final IntegerProperty GLYPH = IntegerProperty.create("glyph", 0, 47);
    public static final Random random = new Random();
    private final BasicParticleType particleType;

    public ChalkCircleBlock(BasicParticleType particleType) {
        super();
        this.particleType = particleType;
        this.registerDefaultState(getStateDefinition().any().setValue(GLYPH, 0));
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(GLYPH);
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return defaultBlockState().setValue(GLYPH, random.nextInt(48));
    }

    @OnlyIn(Dist.CLIENT)
    public void animateTick(BlockState state, World world, BlockPos pos, Random rand) {

        if(this.particleType != null) {
            if(random.nextInt(8) == 1)
                world.addParticle(particleType, pos.getX() + 0.5D, pos.getY() + 0.1D, pos.getZ() + 0.5D, 0, 0, 0);
        }

    }

}