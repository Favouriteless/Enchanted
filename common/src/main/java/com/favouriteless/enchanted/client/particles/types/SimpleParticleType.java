package com.favouriteless.enchanted.client.particles.types;

/**
 * Wrapper for the vanilla SimpleParticleType so I don't have to do any dodgy changes to access it.
 */
public class SimpleParticleType extends net.minecraft.core.particles.SimpleParticleType {

    public SimpleParticleType(boolean alwaysShow) {
        super(alwaysShow);
    }

}
