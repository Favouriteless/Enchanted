package com.favouriteless.enchanted.common.blocks;

import com.favouriteless.enchanted.common.init.EnchantedTags;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

public class ProtectionBarrierBlock extends TemporaryProtectionBarrierBlock {

	public ProtectionBarrierBlock(Properties properties) {
		super(properties);
	}

	@Override
	@NotNull
	public VoxelShape getCollisionShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
		if (context instanceof EntityCollisionContext entityContext) {
			Entity entity = entityContext.getEntity();
			if(entity != null) {
				if(entityContext.getEntity().getType().is(EnchantedTags.EntityTypes.MONSTERS))
					return super.getCollisionShape(state, level, pos, context);
				else if(entityContext.getEntity() instanceof Player player && player.isCrouching())
					return Shapes.empty();
			}
		}
		return super.getCollisionShape(state, level, pos, context);
	}

	@Override
	public void entityInside(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Entity entity) {
		if(entity instanceof Player player && player.isCrouching())
			player.makeStuckInBlock(state, new Vec3(0.75D, 0.15F, 0.75D));
	}

}
