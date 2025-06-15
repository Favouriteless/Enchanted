package net.favouriteless.enchanted.integrations.jei;

import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.transfer.IRecipeTransferInfo;
import net.favouriteless.enchanted.common.menus.DistilleryMenu;
import net.favouriteless.enchanted.common.init.EMenuTypes;
import net.favouriteless.enchanted.common.recipes.DistillingRecipe;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;

import java.util.List;
import java.util.Optional;

public class DistilleryTransferInfo implements IRecipeTransferInfo<DistilleryMenu, DistillingRecipe> {

    @Override
    public Class<DistilleryMenu> getContainerClass() {
        return DistilleryMenu.class;
    }

    @Override
    public boolean canHandle(DistilleryMenu container, DistillingRecipe recipe) {
        return true;
    }

    @Override
    public List<Slot> getRecipeSlots(DistilleryMenu container, DistillingRecipe recipe) {
        return container.slots.subList(0, 3);
    }

    @Override
    public boolean requireCompleteSets(DistilleryMenu container, DistillingRecipe recipe) {
        return false;
    }

    @Override
    public List<Slot> getInventorySlots(DistilleryMenu container, DistillingRecipe recipe) {
        return container.slots.subList(5, container.slots.size());
    }

    @Override
    public RecipeType<DistillingRecipe> getRecipeType() {
        return EJeiRecipeTypes.DISTILLING;
    }

    @Override
    public Optional<MenuType<DistilleryMenu>> getMenuType() {
        return Optional.of(EMenuTypes.DISTILLERY.get());
    }

}