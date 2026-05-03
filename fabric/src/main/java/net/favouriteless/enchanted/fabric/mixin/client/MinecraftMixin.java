package net.favouriteless.enchanted.fabric.mixin.client;

import net.favouriteless.enchanted.client.init.ClientRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.main.GameConfig;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MinecraftMixin {

    @Inject(method = "<init>",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/client/Minecraft;particleEngine:Lnet/minecraft/client/particle/ParticleEngine;",
                    opcode = Opcodes.PUTFIELD,
                    shift = Shift.AFTER
            )
    )
    private void init(GameConfig gameConfig, CallbackInfo ci) {
        ClientRegistry.registerParticleProviders();
    }

}
