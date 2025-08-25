package net.favouriteless.enchanted.fabric.common.transfer;

import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleVariantStorage;
import net.favouriteless.enchanted.api.EFluidContainer;

public class EFluidContainerWrapper extends SingleVariantStorage<FluidVariant> {

    private final EFluidContainer container;

    public EFluidContainerWrapper(EFluidContainer container) {
        this.container = container;
        this.amount = container.getFluidAmount();
        this.variant = FluidVariant.of(container.getFluid());
    }

    @Override
    protected FluidVariant getBlankVariant() {
        return FluidVariant.blank();
    }

    @Override
    protected long getCapacity(FluidVariant variant) {
        return container.getFluidCapacity();
    }

    @Override
    protected boolean canInsert(FluidVariant variant) {
        return variant.isOf(container.getFluid());
    }

    @Override
    protected boolean canExtract(FluidVariant variant) {
        return variant.isOf(container.getFluid());
    }

    @Override
    protected void onFinalCommit() {
        int diff = (int)amount - container.getFluidAmount();

        if(diff < 0) // We don't care about diff if nothing changed.
            container.drain(-diff, false);
        else if(diff > 0)
            container.fill(diff, false);
    }

}
