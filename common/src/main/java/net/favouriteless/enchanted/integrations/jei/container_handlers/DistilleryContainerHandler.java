package net.favouriteless.enchanted.integrations.jei.container_handlers;

import mezz.jei.api.gui.handlers.IGuiClickableArea;
import mezz.jei.api.gui.handlers.IGuiContainerHandler;
import mezz.jei.api.recipe.IFocusFactory;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.runtime.IRecipesGui;
import net.favouriteless.enchanted.client.screens.DistilleryScreen;
import net.favouriteless.enchanted.integrations.jei.EJeiRecipeTypes;
import net.minecraft.client.renderer.Rect2i;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class DistilleryContainerHandler implements IGuiContainerHandler<DistilleryScreen> {

    public Collection<IGuiClickableArea> getGuiClickableAreas(DistilleryScreen screen, double mouseX, double mouseY) {
        Collection<IGuiClickableArea> areas = new ArrayList<>();
        areas.add(new IGuiClickableArea() {

            @Override
            public Rect2i getArea() {
                return new Rect2i(69, 12, 56, 62);
            }

            @Override
            public void onClick(IFocusFactory focusFactory, IRecipesGui recipesGui) {
                List<RecipeType<?>> list = new ArrayList<>();
                list.add(EJeiRecipeTypes.DISTILLING);
                recipesGui.showTypes(list);
            }

        });
        return areas;
    }

}