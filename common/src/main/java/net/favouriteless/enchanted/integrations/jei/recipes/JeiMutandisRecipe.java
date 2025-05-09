package net.favouriteless.enchanted.integrations.jei.recipes;

import net.favouriteless.enchanted.common.init.EnchantedTags;
import net.favouriteless.enchanted.integrations.jei.EJeiRecipeTypes;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.core.HolderSet.Named;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

import java.util.ArrayList;
import java.util.List;

public class JeiMutandisRecipe {
    private final List<ItemStack> inputs;
    private final Component description;

    private final ItemStack output;

    public JeiMutandisRecipe(TagKey<Block> inputs, ItemStack output, Component description) {
        Named<Block> tagContents = BuiltInRegistries.BLOCK.getTag(inputs).orElse(null);
        this.inputs = tagContents != null ? tagContents.stream().map(holder -> new ItemStack(holder.value())).toList() : new ArrayList<>();
        this.output = output;
        this.description = description;
    }

    public List<ItemStack> getInputs() {
        return inputs;
    }

    public ItemStack getOutput() {
        return output;
    }

    public String getDescription() {
        return this.description.getString();
    }

    public static void register(IRecipeRegistration registration) {
        BuiltInRegistries.BLOCK.getTag(EnchantedTags.Blocks.MUTANDIS).ifPresent(tag -> registration.addRecipes(
                EJeiRecipeTypes.MUTANDIS,
                tag.stream()
                        .filter(block -> !BuiltInRegistries.BLOCK.getTag(EnchantedTags.Blocks.MUTANDIS_BLACKLIST).map(t -> t.contains(block)).orElse(false))
                        .map(block -> new JeiMutandisRecipe(EnchantedTags.Blocks.MUTANDIS, new ItemStack(block.value()), Component.translatable("jei.enchanted.mutandis.description")))
                        .toList())
        );

        BuiltInRegistries.BLOCK.getTag(EnchantedTags.Blocks.MUTANDIS_EXTREMIS).ifPresent(tag -> registration.addRecipes(
                EJeiRecipeTypes.MUTANDIS_EXTREMIS,
                tag.stream()
                        .filter(block -> !BuiltInRegistries.BLOCK.getTag(EnchantedTags.Blocks.MUTANDIS_EXTREMIS_BLACKLIST).map(t -> t.contains(block)).orElse(false))
                        .map(block -> new JeiMutandisRecipe(EnchantedTags.Blocks.MUTANDIS_EXTREMIS, new ItemStack(block.value()), Component.translatable("jei.enchanted.mutandis.description")))
                        .toList())
        );
    }
}