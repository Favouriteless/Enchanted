package net.favouriteless.enchanted.common.items;

import net.favouriteless.enchanted.common.Enchanted;
import net.favouriteless.enchanted.common.init.ESoundEvents;
import net.favouriteless.enchanted.common.init.ETags;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;

public class BroomItem extends Item {

	public BroomItem(Properties properties) {
		super(properties);
	}

	@Override
	public InteractionResult useOn(UseOnContext context) {
		Level level = context.getLevel();
		BlockPos pos = context.getClickedPos();

		if(!level.isClientSide) {
			if(level.getBlockState(pos).is(ETags.Blocks.BROOM_SWEEPABLE))
				level.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
			level.playSound(null, pos, ESoundEvents.BROOM_SWEEP.value(), SoundSource.PLAYERS, 1.0F, 0.8F + Enchanted.RANDOM.nextFloat() * 0.2F);
		}
		return InteractionResult.sidedSuccess(level.isClientSide);
	}
}
