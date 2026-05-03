package net.favouriteless.enchanted.fabric.mixin.client;

import com.google.common.collect.ImmutableList;
import net.favouriteless.enchanted.client.init.EParticleRenderTypes;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.renderer.texture.TextureManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(ParticleEngine.class)
public class ParticleEngineMixin {

	@Shadow @Final @Mutable private static List<ParticleRenderType> RENDER_ORDER;
	
	@Inject(method = "<init>", at = @At("RETURN"))
	private void addParticleRenderTypes(ClientLevel level, TextureManager textureManager, CallbackInfo ci) {
		RENDER_ORDER = ImmutableList.<ParticleRenderType>builder().addAll(RENDER_ORDER)
				.add(EParticleRenderTypes.PARTICLE_TRANSLUCENT)
				.build();
	}

}