package net.favouriteless.enchanted.mixin.client;

import net.favouriteless.enchanted.common.init.EAttachmentTypes;
import net.favouriteless.enchanted.platform.EServices;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LocalPlayer.class)
public abstract class LocalPlayerMixin {

    @Shadow
    public abstract boolean drop(boolean fullStack);

    @Inject(method = "aiStep", at = @At("TAIL"))
    private void aiStep(CallbackInfo ci) {
        LocalPlayer player = (LocalPlayer) (Object) this;
        player.setDeltaMovement(player.getDeltaMovement().add(0.0D, EServices.ATTACHMENT.get(player, EAttachmentTypes.SINKING_FACTOR), 0.0D));
    }


    @Inject(method = "swing", at = @At("HEAD"))
    private void swing(CallbackInfo ci) {
        LocalPlayer cast = ((LocalPlayer) (Object) this);

        if (!cast.swinging || cast.swingTime >= cast.getCurrentSwingDuration() / 2 || cast.swingTime < 0) {
            double dropChance = EServices.ATTACHMENT.get(this, EAttachmentTypes.CLUMSY_CHANCE);
            if (Math.random() < dropChance) {
                drop(true);
            }
        }
    }


}
