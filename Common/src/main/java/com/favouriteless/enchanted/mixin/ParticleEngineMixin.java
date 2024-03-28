package com.favouriteless.enchanted.mixin;

import com.favouriteless.enchanted.client.ClientRegistry;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ParticleEngine.class)
public class ParticleEngineMixin {

    @Inject(method="registerProviders", at=@At("TAIL"))
    public void init(CallbackInfo ci) {
        ClientRegistry.registerParticleFactories((ParticleEngine)(Object)this);
    }

}
