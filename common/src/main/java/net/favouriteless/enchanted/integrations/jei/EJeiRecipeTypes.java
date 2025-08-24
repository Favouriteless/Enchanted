package net.favouriteless.enchanted.integrations.jei;

import mezz.jei.api.recipe.RecipeType;
import net.favouriteless.enchanted.common.Enchanted;
import net.favouriteless.enchanted.common.recipes.ByproductRecipe;
import net.favouriteless.enchanted.common.recipes.DistillingRecipe;
import net.favouriteless.enchanted.common.recipes.KettleRecipe;
import net.favouriteless.enchanted.common.recipes.SpinningRecipe;
import net.favouriteless.enchanted.integrations.jei.recipes.JeiMutandisRecipe;
import net.favouriteless.enchanted.integrations.jei.recipes.JeiRiteRecipe;

public class EJeiRecipeTypes {

    public static final RecipeType<ByproductRecipe> BYPRODUCT = new RecipeType<>(Enchanted.id("byproduct"), ByproductRecipe.class);
    public static final RecipeType<DistillingRecipe> DISTILLING = new RecipeType<>(Enchanted.id("distilling"), DistillingRecipe.class);
    public static final RecipeType<SpinningRecipe> SPINNING = new RecipeType<>(Enchanted.id("spinning"), SpinningRecipe.class);
    public static final RecipeType<KettleRecipe> KETTLE = new RecipeType<>(Enchanted.id("kettle"), KettleRecipe.class);
    public static final RecipeType<JeiRiteRecipe> RITE = new RecipeType<>(Enchanted.id("rite"), JeiRiteRecipe.class);
    public static final RecipeType<JeiMutandisRecipe> MUTANDIS = new RecipeType<>(Enchanted.id("mutandis"), JeiMutandisRecipe.class);
    public static final RecipeType<JeiMutandisRecipe> MUTANDIS_EXTREMIS = new RecipeType<>(Enchanted.id("mutandis_extremis"), JeiMutandisRecipe.class);


}
