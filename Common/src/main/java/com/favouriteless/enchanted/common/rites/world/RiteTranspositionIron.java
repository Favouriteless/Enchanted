package com.favouriteless.enchanted.common.rites.world;

import com.favouriteless.enchanted.api.rites.AbstractRite;
import com.favouriteless.enchanted.common.init.registry.EnchantedBlocks;
import com.favouriteless.enchanted.common.init.registry.EnchantedItems;
import com.favouriteless.enchanted.common.init.registry.EnchantedParticles;
import com.favouriteless.enchanted.common.rites.CirclePart;
import com.favouriteless.enchanted.common.rites.RiteType;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import java.util.List;
import java.util.UUID;

public class RiteTranspositionIron extends AbstractRite {

	private int progress = 0;
	public static final double CIRCLE_RADIUS = 7.5D;

	public RiteTranspositionIron(RiteType<?> type, ServerLevel level, BlockPos pos, UUID caster) {
		super(type, level, pos, caster, 0, 0);
		CIRCLES_REQUIRED.put(CirclePart.LARGE, EnchantedBlocks.CHALK_PURPLE.get());
		ITEMS_REQUIRED.put(Items.ENDER_PEARL, 1);
		ITEMS_REQUIRED.put(Items.IRON_INGOT, 1);
		ITEMS_REQUIRED.put(Items.BLAZE_POWDER, 1);
		ITEMS_REQUIRED.put(EnchantedItems.DIAMOND_VAPOUR.get(), 1);
		ITEMS_REQUIRED.put(EnchantedItems.ATTUNED_STONE_CHARGED.get(), 1);
	}

	@Override
	protected void onTick() {
		ServerLevel level = getLevel();
		BlockPos pos = getPos();

		if(level != null) {
			List<BlockPos> circlePoints = CirclePart.LARGE.getInsideCirclePoints();
			BlockPos testPos = circlePoints.get(progress++).offset(pos);

			for(int i = level.getMinBuildHeight(); i < pos.getY() - 2; i++) {
				BlockPos blockPos = new BlockPos(testPos.getX(), i, testPos.getZ());
				Block block = level.getBlockState(blockPos).getBlock();
				if(block == Blocks.IRON_ORE) {
					level.setBlockAndUpdate(blockPos, Blocks.STONE.defaultBlockState());
					level.addFreshEntity(new ItemEntity(level, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, new ItemStack(Items.RAW_IRON)));
					level.playSound(null, pos, SoundEvents.COPPER_BREAK, SoundSource.MASTER, 1.0F, 1.0F);
					spawnMagicParticles();
				}
				else if(block == Blocks.DEEPSLATE_IRON_ORE) {
					level.setBlockAndUpdate(blockPos, Blocks.DEEPSLATE.defaultBlockState());
					level.addFreshEntity(new ItemEntity(level, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, new ItemStack(Items.RAW_IRON)));
					level.playSound(null, pos, SoundEvents.COPPER_BREAK, SoundSource.MASTER, 1.0F, 1.0F);
					spawnMagicParticles();
				}
			}

			if(this.ticks % 20 == 0) {
				level.sendParticles(EnchantedParticles.TRANSPOSITION_IRON_SEED.get(), pos.getX() + 0.5D, pos.getY() - 0.1D, pos.getZ() + 0.5D, 1, 0.0D, 0.0D, 0.0D, 0.0D);
			}

			if(progress > circlePoints.size() - 1)
				stopExecuting();
		}
		else {
			stopExecuting();
		}
	}

	@Override
	protected void saveAdditional(CompoundTag nbt) {
		nbt.putInt("progress", progress);
	}

	@Override
	protected boolean loadAdditional(CompoundTag nbt, Level level) {
		if(!nbt.contains("progress"))
			return false;

		progress = nbt.getInt("progress");
		return true;
	}
}
