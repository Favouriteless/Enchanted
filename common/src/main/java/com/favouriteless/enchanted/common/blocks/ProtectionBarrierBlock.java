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

public class ProtectionBarrierBlock extends TemporaryProtectionBarrierBlock {

	public ProtectionBarrierBlock(Properties properties) {
		super(properties);
	}

	@Override
	public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		if (context instanceof EntityCollisionContext entityContext) {
			Entity entity = entityContext.getEntity();
			if(entity != null) {
				if(ForgeRegistries.ENTITIES.tags().getTag(EnchantedTags.EntityTypes.MONSTERS).contains(entityContext.getEntity().getType()))
					return super.getCollisionShape(state, level, pos, context);
				else if(entityContext.getEntity() instanceof Player player && player.isCrouching())
					return Shapes.empty();
			}
		}
		return super.getCollisionShape(state, level, pos, context);
	}

	@Override
	public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
		if(entity instanceof Player player && player.isCrouching())
			player.makeStuckInBlock(state, new Vec3(0.75D, 0.15F, 0.75D));
	}

}
