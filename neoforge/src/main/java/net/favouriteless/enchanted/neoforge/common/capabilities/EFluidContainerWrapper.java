package net.favouriteless.enchanted.neoforge.common.capabilities;

import net.favouriteless.enchanted.api.EFluidContainer;
import net.minecraft.core.Direction;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

public record EFluidContainerWrapper(EFluidContainer container, Direction side) implements IFluidHandler {

    @Override
    public int getTanks() {
        return 1;
    }

    @Override
    public FluidStack getFluidInTank(int tank) {
        return new FluidStack(container.getFluid(), container.getFluidAmount());
    }

    @Override
    public int getTankCapacity(int tank) {
        return container.getFluidCapacity();
    }

    @Override
    public boolean isFluidValid(int tank, FluidStack stack) {
        return stack.is(container.getFluid());
    }

    @Override
    public int fill(FluidStack stack, FluidAction action) {
        if (stack.isEmpty() || !stack.is(container.getFluid())) {
            return 0;
        }
        return container.fill(stack.getAmount(), action == FluidAction.SIMULATE);
    }

    @Override
    public FluidStack drain(FluidStack stack, FluidAction action) {
        if (!stack.is(container.getFluid())) {
            return stack;
        }
        return new FluidStack(container.getFluid(), container.drain(stack.getAmount(), action == FluidAction.SIMULATE));
    }

    @Override
    public FluidStack drain(int drain, FluidAction action) {
        return new FluidStack(container.getFluid(), container.drain(drain, action == FluidAction.SIMULATE));
    }

}
