package net.favouriteless.enchanted.platform.services;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public interface FluidHelper {

    /**
     * @return Capacity of a bucket in fluid units (mB on NeoForge, droplets on Fabric)
     */
    int getBucketCapacity();

    /**
     * Attempt to fill or drain an in-world fluid container using an {@link ItemStack}. It will attempt to fill the item
     * from the block first, then attempt to fill the block from the item if that fails.
     *
     * @return True if an interaction took place, otherwise false.
     */
    boolean tryItemInteraction(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult);

    /**
     * @return True if the given player is holding a fluid container in the given hand.
     */
    boolean playerHoldingFluidContainer(Player player, InteractionHand hand);

}