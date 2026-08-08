package net.favouriteless.enchanted.fabric.mixin.common;

import com.llamalad7.mixinextras.sugar.Local;
import net.favouriteless.enchanted.fabric.common.CommonEventsFabric;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerPlayerGameMode;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerPlayerGameMode.class)
public class ServerPlayerGameModeMixin {

    @Shadow
    @Final
    protected ServerPlayer player;

    @Inject(method = "destroyBlock", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;mineBlock(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/entity/player/Player;)V", shift = Shift.AFTER))
    private void destroyBlock(BlockPos pos, CallbackInfoReturnable<Boolean> cir, @Local(ordinal = 0) ItemStack original, @Local(ordinal = 1) ItemStack copy) {
        CommonEventsFabric.playerDestroyItemEvent(player, copy, InteractionHand.MAIN_HAND);
    }

}
