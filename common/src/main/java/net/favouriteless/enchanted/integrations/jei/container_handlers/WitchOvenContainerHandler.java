package net.favouriteless.enchanted.integrations.jei.container_handlers;

import mezz.jei.api.gui.handlers.IGuiClickableArea;
import mezz.jei.api.gui.handlers.IGuiContainerHandler;
import mezz.jei.api.recipe.IFocusFactory;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.runtime.IRecipesGui;
import net.favouriteless.enchanted.client.screens.WitchOvenScreen;
import net.favouriteless.enchanted.integrations.jei.EJeiRecipeTypes;
import net.minecraft.client.renderer.Rect2i;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class WitchOvenContainerHandler implements IGuiContainerHandler<WitchOvenScreen> {

    @Override
    public Collection<IGuiClickableArea> getGuiClickableAreas(WitchOvenScreen screen, double mouseX, double mouseY) {
        Collection<IGuiClickableArea> areas = new ArrayList<>();
        areas.add(new IGuiClickableArea() {

            @Override
            public Rect2i getArea() {
                return new Rect2i(76, 16, 24, 17);
            }

            @Override
            public void onClick(IFocusFactory focusFactory, IRecipesGui recipesGui) {
                List<RecipeType<?>> list = new ArrayList<>();
                list.add(EJeiRecipeTypes.BYPRODUCT);
                recipesGui.showTypes(list);
            }

        });
        return areas;
    }
}