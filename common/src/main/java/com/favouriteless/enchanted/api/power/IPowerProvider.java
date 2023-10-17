package com.favouriteless.enchanted.api.power;

import com.favouriteless.enchanted.common.altar.AltarStateObserver;
import com.favouriteless.enchanted.common.blockentities.AltarBlockEntity;
import com.favouriteless.stateobserver.api.StateObserver;
import net.minecraft.world.level.block.entity.BlockEntity;

/**
 * Represents a {@link BlockEntity} which produces power. Every {@link BlockEntity} which needs to produce power should
 * implement this.
 *
 * <p>A {@link BlockEntity} implementing {@link IPowerProvider} should also use a {@link StateObserver} to
 * notify nearby {@link IPowerConsumer}s when it is created or loaded, and when those consumers are placed.</p>
 *
 * <p>See {@link AltarBlockEntity} and {@link AltarStateObserver} for an example implementation of a power provider.</p>
 */
public interface IPowerProvider {

    /**
     * Attempt to consume power from this {@link IPowerProvider}.
     *
     * @param amount The amount of power to be consumed.
     * @return True if the power was consumed, false if the power was not consumed (e.g. there was not enough power)
     */
    boolean tryConsumePower(double amount);

}
