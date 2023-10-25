package com.favouriteless.enchanted.mixin.fabric;

import com.favouriteless.enchanted.client.events.ClientEvents;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.SoundEngine;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SoundEngine.class)
public class SoundEngineMixin {

    @Shadow private boolean loaded;

    @Inject(method="play", at=@At("HEAD"))
    private void play(SoundInstance sound, CallbackInfo ci) {
        if(loaded)
            ClientEvents.playSound(sound);
    }

}
