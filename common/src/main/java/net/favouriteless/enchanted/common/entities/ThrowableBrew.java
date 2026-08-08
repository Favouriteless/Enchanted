package net.favouriteless.enchanted.common.entities;

import net.favouriteless.enchanted.common.init.EItems;
import net.favouriteless.enchanted.common.items.brews.ThrowableBrewItem;
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
        return EItems.BREW_OF_LOVE.get();
    }

    @Override
    public double getDefaultGravity() {
        return 0.05D;
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        super.onHitBlock(result);
        if (!level().isClientSide) {
            ItemStack itemstack = this.getItem();
            if (itemstack.getItem() instanceof ThrowableBrewItem item) {
                item.applyEffect(getOwner(), level(), result.getLocation());
                level().levelEvent(LevelEvent.PARTICLES_SPELL_POTION_SPLASH, this.blockPosition(), item.getColour());
            }
            discard();
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        if (!level().isClientSide) {
            ItemStack itemstack = this.getItem();
            if (itemstack.getItem() instanceof ThrowableBrewItem item) {
                item.applyEffect(getOwner(), level(), result.getLocation());
                level().levelEvent(LevelEvent.PARTICLES_SPELL_POTION_SPLASH, this.blockPosition(), item.getColour());
            }
            discard();
        }
    }

}
