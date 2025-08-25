package net.favouriteless.enchanted.platform.services;

import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorageUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

import java.util.Optional;

public class FabricFluidHelper implements FluidHelper {

    @Override
    public int getBucketCapacity() {
        return (int)FluidConstants.BUCKET;
    }

    @Override
    public boolean tryItemInteraction(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        return Optional.ofNullable(FluidStorage.SIDED.find(level, pos, hitResult.getDirection()))
                .map(s -> FluidStorageUtil.interactWithFluidStorage(s, player, hand))
                .orElse(false);
    }

}