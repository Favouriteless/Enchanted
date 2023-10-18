package com.favouriteless.enchanted.common.entities;

import com.favouriteless.enchanted.common.init.registry.EnchantedItems;
import com.favouriteless.enchanted.common.items.brews.ThrowableBrewItem;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;

public class ThrowableBrew extends ThrowableItemProjectile {

	public ThrowableBrew(EntityType<? extends ThrowableBrew> type, Level level) {
		super(type, level);
	}

	@Override
	protected Item getDefaultItem() {
		return EnchantedItems.BREW_OF_LOVE.get();
	}

	@Override
	protected float getGravity() {
		return 0.05F;
	}

	@Override
	protected void onHitBlock(BlockHitResult result) {
		super.onHitBlock(result);
		if (!this.level.isClientSide) {
			ItemStack itemstack = this.getItem();
			if(itemstack.getItem() instanceof ThrowableBrewItem item) {
				item.applyEffect(getOwner(), level, result.getLocation());
				this.level.levelEvent(LevelEvent.PARTICLES_SPELL_POTION_SPLASH, this.blockPosition(), item.getColour());
			}
			discard();
		}
	}

	@Override
	protected void onHitEntity(EntityHitResult result) {
		super.onHitEntity(result);
		if (!this.level.isClientSide) {
			ItemStack itemstack = this.getItem();
			if(itemstack.getItem() instanceof ThrowableBrewItem item) {
				item.applyEffect(getOwner(), level, result.getLocation());
				this.level.levelEvent(LevelEvent.PARTICLES_SPELL_POTION_SPLASH, this.blockPosition(), item.getColour());
			}
			discard();
		}
	}

}
