package net.favouriteless.enchanted.common.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BarrierBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

public class ProtectionBarrierBlock extends BarrierBlock {

    private final boolean blocksPlayers;

    public ProtectionBarrierBlock(boolean blocksPlayers, Properties properties) {
        super(properties);
        this.blocksPlayers = blocksPlayers;
    }

    @Override
    @NotNull
    public VoxelShape getCollisionShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        if (context instanceof EntityCollisionContext entityContext) {
            Entity entity = entityContext.getEntity();
            if (!blocksPlayers && entity instanceof Player player && player.isCrouching()) {
                return Shapes.empty();
            }
        }
        return super.getCollisionShape(state, level, pos, context);
    }

    @Override
    public void entityInside(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Entity entity) {
        if (entity instanceof Player player && player.isCrouching()) {
            player.makeStuckInBlock(state, new Vec3(0.75D, 0.15F, 0.75D));
        }
    }

    @Override
    protected boolean isPathfindable(BlockState state, PathComputationType pathComputationType) {
        return false;
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (state.getValue(WATERLOGGED)) {
            level.setBlockAndUpdate(pos, Blocks.WATER.defaultBlockState());
        } else {
            level.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
        }
    }

}
