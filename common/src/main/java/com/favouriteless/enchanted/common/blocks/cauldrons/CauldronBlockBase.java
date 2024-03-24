package com.favouriteless.enchanted.common.blocks.cauldrons;

import com.favouriteless.enchanted.common.blocks.entity.CauldronBlockEntity;
import com.favouriteless.enchanted.common.util.PlayerInventoryHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public abstract class CauldronBlockBase extends Block implements EntityBlock {

	public CauldronBlockBase(Properties properties) {
		super(properties);
	}

	@Override
	public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		BlockEntity te = world.getBlockEntity(pos);
		ItemStack stack = player.getItemInHand(hand);
		if(te instanceof CauldronBlockEntity<?> cauldron) {
			if(cauldron.isComplete) {
				cauldron.takeContents(player);
				return InteractionResult.SUCCESS;
			}
			else if(stack.getItem() == Items.BUCKET && cauldron.isFailed) {
				cauldron.takeContents(player);
				world.playSound(null, pos, SoundEvents.BUCKET_FILL, SoundSource.BLOCKS, 1.0F, 1.0F);
				return InteractionResult.SUCCESS;
			}
			else if(stack.getItem() == Items.BUCKET && cauldron.getWater() >= 1000) {
				if (!world.isClientSide) {
					if (cauldron.removeWater(FluidAttributes.BUCKET_VOLUME)) {
						world.playSound(null, pos, SoundEvents.BUCKET_FILL, SoundSource.BLOCKS, 1.0F, 1.0F);
						stack.shrink(1);
						PlayerInventoryHelper.tryGiveItem(player, new ItemStack(Items.WATER_BUCKET));
					}
				}
				return InteractionResult.SUCCESS;
			}
			else if (stack.getItem() == Items.WATER_BUCKET) {
				if (!world.isClientSide) {
					if (cauldron.addWater(FluidAttributes.BUCKET_VOLUME)) {
						world.playSound(null, pos, SoundEvents.BUCKET_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);
						if (!player.isCreative()) player.setItemInHand(hand, Items.BUCKET.getDefaultInstance());
					}
				}
				return InteractionResult.SUCCESS;
			}
		}
		return InteractionResult.FAIL;
	}

	@Override
	public void entityInside(BlockState state, Level world, BlockPos pos, Entity entity) {
		if(!world.isClientSide && entity instanceof ItemEntity) {
			BlockEntity blockEntity = world.getBlockEntity(pos);
			if(blockEntity instanceof CauldronBlockEntity<?> cauldron) {
				if(!cauldron.isFailed && cauldron.isFull() && cauldron.isHot()) {
					cauldron.addItem((ItemEntity) entity);
				}
			}
		}
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
		return CauldronBlockEntity::tick;
	}
}
