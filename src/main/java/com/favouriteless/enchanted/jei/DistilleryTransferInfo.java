/*
 *
 *   Copyright (c) 2023. Favouriteless
 *   Enchanted, a minecraft mod.
 *   GNU GPLv3 License
 *
 *       This file is part of Enchanted.
 *
 *       Enchanted is free software: you can redistribute it and/or modify
 *       it under the terms of the GNU General Public License as published by
 *       the Free Software Foundation, either version 3 of the License, or
 *       (at your option) any later version.
 *
 *       Enchanted is distributed in the hope that it will be useful,
 *       but WITHOUT ANY WARRANTY; without even the implied warranty of
 *       MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *       GNU General Public License for more details.
 *
 *       You should have received a copy of the GNU General Public License
 *       along with Enchanted.  If not, see <https://www.gnu.org/licenses/>.
 *
 *
 */

package com.favouriteless.enchanted.jei;

import com.favouriteless.enchanted.common.menus.DistilleryMenu;
import com.favouriteless.enchanted.common.recipes.DistilleryRecipe;
import mezz.jei.api.recipe.transfer.IRecipeTransferInfo;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.Slot;

import java.util.List;

import static com.favouriteless.enchanted.jei.JEIRecipeTypes.RECIPE_TYPE_DISTILLERY;

public class DistilleryTransferInfo implements IRecipeTransferInfo<DistilleryMenu,DistilleryRecipe> {

    @Override
    public Class<DistilleryMenu> getContainerClass() {
        return DistilleryMenu.class;
    }

    @Override
    public boolean canHandle(DistilleryMenu container, DistilleryRecipe recipe) {
        return true;
    }

    @Override
    public List<Slot> getRecipeSlots(DistilleryMenu container, DistilleryRecipe recipe) {
        return container.slots.subList(0,3);
    }

    @Override
    public boolean requireCompleteSets(DistilleryMenu container, DistilleryRecipe recipe) {
        return false;
    }

    @Override
    public List<Slot> getInventorySlots(DistilleryMenu container, DistilleryRecipe recipe) {
        return container.slots.subList(5,container.slots.size());
    }

    @Override
    public Class<DistilleryRecipe> getRecipeClass() {
        return DistilleryRecipe.class;
    }

    @Override
    public ResourceLocation getRecipeCategoryUid() {
        return RECIPE_TYPE_DISTILLERY.getUid();
    }
}
