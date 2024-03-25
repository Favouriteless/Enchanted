package com.favouriteless.enchanted.common.recipes;

import com.favouriteless.enchanted.common.init.registry.EnchantedRecipeTypes;
import com.favouriteless.enchanted.common.util.JSONHelper;
import com.google.gson.JsonObject;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class DistilleryRecipe implements Recipe<Container> {

    protected final RecipeType<?> type;
    protected final ResourceLocation id;

    private final NonNullList<ItemStack> itemsIn;
    private final NonNullList<ItemStack> itemsOut;
    private final int cookTime;


    public DistilleryRecipe(ResourceLocation id, NonNullList<ItemStack> itemsIn, NonNullList<ItemStack> itemsOut, int cookTime) {
        this.type = EnchantedRecipeTypes.DISTILLERY;
        this.id = id;

        this.itemsIn = itemsIn;
        this.itemsOut = itemsOut;
        this.cookTime = cookTime;
    }

    /**
     * @return A {@link NonNullList} containing copies of this recipe's outputs.
     */
    public NonNullList<ItemStack> getItemsOut() {
        NonNullList<ItemStack> out = NonNullList.createWithCapacity(itemsOut.size());
        for(int i = 0; i < itemsOut.size(); i++)
            out.set(i, itemsOut.get(i).copy());
        return out;
    }

    /**
     * @return A {@link NonNullList} containing copies of this recipe's inputs.
     */
    public NonNullList<ItemStack> getItemsIn() {
        NonNullList<ItemStack> in = NonNullList.createWithCapacity(itemsIn.size());
        for(int i = 0; i < itemsIn.size(); i++)
            in.set(i, itemsIn.get(i).copy());
        return in;
    }

    public int getCookTime() {
        return cookTime;
    }

    @Override
    public boolean matches(@NotNull Container inv, @NotNull Level level) {
        int requiredItems = getItemsIn().size();

        for(ItemStack stack : getItemsIn()) {
            for(int i = 0; i < 3; i++) {
                ItemStack item = inv.getItem(i);
                if(ItemStack.isSameItemSameTags(stack, item) && item.getCount() >= stack.getCount()) {
                    requiredItems--;
                    break;
                }
            }
        }
        return requiredItems == 0;
    }

    /**
     * @deprecated Use {@link DistilleryRecipe#getItemsOut()} instead.
     */
    @Override
    @Deprecated
    public ItemStack assemble(Container inv) {
        return null; // Has multiple outputs, don't use this.
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return false;
    }

    /**
     * @deprecated Use {@link DistilleryRecipe#getItemsOut()} instead.
     */
    @Override
    @Deprecated
    public ItemStack getResultItem() {
        return ItemStack.EMPTY;
    }

    @Override
    @NotNull
    public ResourceLocation getId() {
        return id;
    }

    @Override
    @NotNull
    public RecipeSerializer<?> getSerializer() {
        return EnchantedRecipeTypes.DISTILLERY_SERIALIZER.get();
    }

    @Override
    @NotNull
    public RecipeType<?> getType() {
        return type;
    }

    @Override
    public boolean isSpecial() {
        return true;
    }



    public static class Serializer implements RecipeSerializer<DistilleryRecipe> {

        @Override
        @NotNull
        public DistilleryRecipe fromJson(@NotNull ResourceLocation recipeId, @NotNull JsonObject json) {
            NonNullList<ItemStack> itemsIn = JSONHelper.readItemStackList(GsonHelper.getAsJsonArray(json, "ingredients"));
            NonNullList<ItemStack> itemsOut = JSONHelper.readItemStackList(GsonHelper.getAsJsonArray(json, "result"));
            int cookTime = GsonHelper.getAsInt(json, "cookTime", 200);

            return new DistilleryRecipe(recipeId, itemsIn, itemsOut, cookTime);
        }

        @Override
        @NotNull
        public DistilleryRecipe fromNetwork(@NotNull ResourceLocation recipeId, FriendlyByteBuf buffer) {
            int inSize = buffer.readInt();
            NonNullList<ItemStack> itemsIn = NonNullList.create();
            for (int x = 0; x < inSize; ++x)
                itemsIn.add(buffer.readItem());

            int outSize = buffer.readInt();
            NonNullList<ItemStack> itemsOut = NonNullList.create();
            for (int x = 0; x < outSize; ++x)
                itemsOut.add(buffer.readItem());

            int cookTime = buffer.readInt();

            return new DistilleryRecipe(recipeId, itemsIn, itemsOut, cookTime);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, DistilleryRecipe recipe) {
            buffer.writeInt(recipe.getItemsIn().size());
            for (ItemStack stack : recipe.getItemsIn())
                buffer.writeItem(stack);

            buffer.writeInt(recipe.getItemsOut().size());
            for (ItemStack stack : recipe.getItemsOut())
                buffer.writeItem(stack);

            buffer.writeInt(recipe.getCookTime());
        }

    }
}
