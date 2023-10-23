package com.favouriteless.enchanted.common.items;

import com.favouriteless.enchanted.common.init.registry.EnchantedBlocks;
import com.favouriteless.enchanted.common.rites.CirclePart;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

public class CircleTalismanItem extends Item {

	public CircleTalismanItem(Properties properties) {
		super(properties);
	}

	@Override
	public InteractionResult useOn(UseOnContext context) {
		Level world = context.getLevel();
		if(!world.isClientSide) {
			BlockPos pos = context.getClickedPos().above();
			ItemStack stack = context.getItemInHand();


			if(stack.hasTag()) {
				CompoundTag nbt = stack.getTag();
				byte small = nbt.contains("small") ? nbt.getByte("small") : 0;
				byte medium = nbt.contains("medium") ? nbt.getByte("medium") : 0;
				byte large = nbt.contains("large") ? nbt.getByte("large") : 0;

				if(small != 0 || medium != 0 || large != 0) {
					boolean validPlace = world.getBlockState(pos).isAir() && EnchantedBlocks.CHALK_GOLD.get().canSurvive(null, world, pos);
					if(validPlace) {
						if(small != 0 && !CirclePart.SMALL.canPlace(world, pos)) validPlace = false;
						if(medium != 0 && !CirclePart.MEDIUM.canPlace(world, pos)) validPlace = false;
						if(large != 0 && !CirclePart.LARGE.canPlace(world, pos)) validPlace = false;

						if(validPlace) {
							world.setBlockAndUpdate(pos, EnchantedBlocks.CHALK_GOLD.get().getStateForPlacement(new BlockPlaceContext(context)));
							if(small != 0)
								CirclePart.SMALL.place(world, pos, small == 1 ? EnchantedBlocks.CHALK_WHITE.get() : small == 2 ? EnchantedBlocks.CHALK_RED.get() : EnchantedBlocks.CHALK_PURPLE.get(), context);
							if(medium != 0)
								CirclePart.MEDIUM.place(world, pos, medium == 1 ? EnchantedBlocks.CHALK_WHITE.get() : medium == 2 ? EnchantedBlocks.CHALK_RED.get() : EnchantedBlocks.CHALK_PURPLE.get(), context);
							if(large != 0)
								CirclePart.LARGE.place(world, pos, large == 1 ? EnchantedBlocks.CHALK_WHITE.get() : large == 2 ? EnchantedBlocks.CHALK_RED.get() : EnchantedBlocks.CHALK_PURPLE.get(), context);

							stack.setTag(new CompoundTag());

							return InteractionResult.SUCCESS;
						}
					}

					if(context.getPlayer() != null) {
						context.getPlayer().displayClientMessage(Component.literal("All blocks must be valid spots.").withStyle(ChatFormatting.RED), true);
						return InteractionResult.PASS;
					}
				}
			}
		}

		return InteractionResult.FAIL;
	}

}
