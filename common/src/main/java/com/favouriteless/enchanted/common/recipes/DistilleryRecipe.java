package com.favouriteless.enchanted.common.recipes;

import com.favouriteless.enchanted.common.init.registry.EnchantedRecipeTypes;
import com.favouriteless.enchanted.common.util.StaticJSONHelper;
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

import javax.annotation.Nullable;

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

    public NonNullList<ItemStack> getItemsOut() {
        return itemsOut;
    }

    public NonNullList<ItemStack> getItemsIn() {
        return this.itemsIn;
    }

    public int getCookTime() {
        return cookTime;
    }

    @Override
    public boolean matches(Container inv, Level level) {
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

    @Override
    public ItemStack assemble(Container inv) {
        return null; // Has multiple outputs, don't use this.
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return false;
    }

    @Override
    public ItemStack getResultItem() {
        return ItemStack.EMPTY;
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return EnchantedRecipeTypes.DISTILLERY_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return type;
    }

    @Override
    public boolean isSpecial() {
        return true;
    }



    public static class Serializer extends ForgeRegistryEntry<RecipeSerializer<?>> implements RecipeSerializer<DistilleryRecipe> {

        @Override
        public DistilleryRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
            NonNullList<ItemStack> itemsIn = StaticJSONHelper.readItemStackList(GsonHelper.getAsJsonArray(json, "ingredients"));
            NonNullList<ItemStack> itemsOut = StaticJSONHelper.readItemStackList(GsonHelper.getAsJsonArray(json, "result"));
            int cookTime = GsonHelper.getAsInt(json, "cookTime", 200);

            return new DistilleryRecipe(recipeId, itemsIn, itemsOut, cookTime);
        }

        @Nullable
        @Override
        public DistilleryRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
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
