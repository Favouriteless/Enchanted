package com.favouriteless.enchanted.common.blocks.altar;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.Random;

public class CandelabraBlock extends AltarDecorationBlock {

    public CandelabraBlock(Properties properties) {
        super(properties);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return Shapes.box(0.15625, 0, 0.15625, 0.84375, 0.71875, 0.84375);
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, Random random) {
        // North
        if(random.nextInt(10) > 7) {
            double x = pos.getX() + 0.475D + random.nextDouble() * 0.05D;
            double y = pos.getY() + 0.678D;
            double z = pos.getZ() + 0.225D + random.nextDouble() * 0.05D;
            level.addParticle(ParticleTypes.SMOKE, x, y, z, 0.0D, 0.0D, 0.0D);
            level.addParticle(ParticleTypes.FLAME, x, y, z, 0.0D, 0.0D, 0.0D);
        }
        // West
        if(random.nextInt(10) > 7) {
            double x = pos.getX() + 0.225D + random.nextDouble() * 0.05D;
            double y = pos.getY() + 0.678D;
            double z = pos.getZ() + 0.475D + random.nextDouble() * 0.05D;
            level.addParticle(ParticleTypes.SMOKE, x, y, z, 0.0D, 0.0D, 0.0D);
            level.addParticle(ParticleTypes.FLAME, x, y, z, 0.0D, 0.0D, 0.0D);
        }
        // Center
        if(random.nextInt(10) > 7) {
            double x = pos.getX() + 0.475D + random.nextDouble() * 0.05D;
            double y = pos.getY() + 0.778D;
            double z = pos.getZ() + 0.475D + random.nextDouble() * 0.05D;
            level.addParticle(ParticleTypes.SMOKE, x, y, z, 0.0D, 0.0D, 0.0D);
            level.addParticle(ParticleTypes.FLAME, x, y, z, 0.0D, 0.0D, 0.0D);
        }
        // East
        if(random.nextInt(10) > 7) {
            double x = pos.getX() + 0.725D + random.nextDouble() * 0.05D;
            double y = pos.getY() + 0.678D;
            double z = pos.getZ() + 0.475D + random.nextDouble() * 0.05D;
            level.addParticle(ParticleTypes.SMOKE, x, y, z, 0.0D, 0.0D, 0.0D);
            level.addParticle(ParticleTypes.FLAME, x, y, z, 0.0D, 0.0D, 0.0D);
        }
        // South
        if(random.nextInt(10) > 7) {
            double x = pos.getX() + 0.475D + random.nextDouble() * 0.05D;
            double y = pos.getY() + 0.678D;
            double z = pos.getZ() + 0.725D + random.nextDouble() * 0.05D;
            level.addParticle(ParticleTypes.SMOKE, x, y, z, 0.0D, 0.0D, 0.0D);
            level.addParticle(ParticleTypes.FLAME, x, y, z, 0.0D, 0.0D, 0.0D);
        }
    }
}
