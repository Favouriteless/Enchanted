package com.favouriteless.enchanted.common.items;

import com.favouriteless.enchanted.Enchanted;
import com.favouriteless.enchanted.common.init.EnchantedTags;
import com.favouriteless.enchanted.common.init.registry.EnchantedSoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class BroomItem extends Item {

	public BroomItem(Properties properties) {
		super(properties);
	}

	@Override
	public InteractionResult useOn(UseOnContext context) {
		if(!context.getLevel().isClientSide) {
			BlockState state = context.getLevel().getBlockState(context.getClickedPos());
			if(state.is(EnchantedTags.Blocks.CHALKS))
				context.getLevel().setBlockAndUpdate(context.getClickedPos(), Blocks.AIR.defaultBlockState());
			context.getLevel().playSound(null, context.getClickedPos(), EnchantedSoundEvents.BROOM_SWEEP.get(), SoundSource.PLAYERS, 1.0F, 0.8F + Enchanted.RANDOM.nextFloat()*0.2F);
		}
		return InteractionResult.SUCCESS;
	}
}
