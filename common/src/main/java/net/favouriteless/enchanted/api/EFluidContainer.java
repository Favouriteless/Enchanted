package net.favouriteless.enchanted.api;

import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;

/**
 * Represents an object which can store an amount of a given {@link Fluid}. This is an abstraction of the
 * loader-specific types IFluidHandler (NeoForge) and FluidStorage (Fabric).
 */
public interface EFluidContainer {

    /**
     * @return The {@link Fluid} this container is able to store.
     */
    default Fluid getFluid() {
        return Fluids.WATER;
    }

    /**
     * @return Total capacity of this container.
     */
    int getFluidCapacity();

    /**
     * @return Amount of fluid currently held by this container.
     */
    int getFluidAmount();

    /**
     * Attempt to add fluid to the container.
     *
     * @param amount The volume of fluid to add.
     * @param simulate If true, the fluid will not actually be added.
     *
     * @return The amount of fluid the container accepted.
     */
    int fill(int amount, boolean simulate);

    /**
     * Attempt to drain fluid from the container.
     *
     * @param amount The volume of fluid to drain.
     * @param simulate If true, the fluid will not actually be added.
     *
     * @return The amount of fluid the container was able to remove.
     */
    int drain(int amount, boolean simulate);

}
