package net.favouriteless.enchanted.common.items;

import net.favouriteless.enchanted.common.Enchanted;
import net.favouriteless.enchanted.common.init.EnchantedTags;
import net.favouriteless.enchanted.common.init.registry.ESoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class BroomItem extends Item {

	public BroomItem() {
		super(new Properties());
	}

	@Override
	public InteractionResult useOn(UseOnContext context) {
		if(!context.getLevel().isClientSide) {
			BlockState state = context.getLevel().getBlockState(context.getClickedPos());
			if(state.is(EnchantedTags.Blocks.BROOM_SWEEPABLE))
				context.getLevel().setBlockAndUpdate(context.getClickedPos(), Blocks.AIR.defaultBlockState());
			context.getLevel().playSound(null, context.getClickedPos(), ESoundEvents.BROOM_SWEEP.get(), SoundSource.PLAYERS, 1.0F, 0.8F + Enchanted.RANDOM.nextFloat()*0.2F);
		}
		return InteractionResult.SUCCESS;
	}
}
