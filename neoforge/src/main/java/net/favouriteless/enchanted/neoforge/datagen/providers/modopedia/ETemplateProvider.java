package net.favouriteless.enchanted.neoforge.datagen.providers.modopedia;

import com.google.gson.JsonElement;
import net.favouriteless.enchanted.api.datagen.builders.modopedia.components.CauldronBuilder;
import net.favouriteless.enchanted.api.datagen.builders.modopedia.components.DistilleryBuilder;
import net.favouriteless.enchanted.api.datagen.builders.modopedia.templates.page.ByproductPageBuilder;
import net.favouriteless.enchanted.api.datagen.builders.modopedia.templates.page.DistilleryPageBuilder;
import net.favouriteless.enchanted.api.datagen.builders.modopedia.templates.page.DoubleByproductPageBuilder;
import net.favouriteless.enchanted.api.datagen.builders.modopedia.templates.recipe.ByproductRecipeBuilder;
import net.favouriteless.enchanted.api.datagen.builders.modopedia.templates.recipe.DistillingRecipeBuilder;
import net.favouriteless.enchanted.api.datagen.builders.modopedia.templates.recipe.KettleRecipeBuilder;
import net.favouriteless.enchanted.api.datagen.builders.modopedia.templates.recipe.WitchCauldronRecipeBuilder;
import net.favouriteless.enchanted.common.Enchanted;
import net.favouriteless.enchanted.integrations.modopedia.client.template_processors.ByproductRecipeProcessor;
import net.favouriteless.enchanted.integrations.modopedia.client.template_processors.CauldronTypeRecipeProcessor;
import net.favouriteless.enchanted.integrations.modopedia.client.template_processors.DistillingRecipeProcessor;
import net.favouriteless.modopedia.api.datagen.builders.TemplateBuilder;
import net.favouriteless.modopedia.api.datagen.builders.page_components.components.*;
import net.favouriteless.modopedia.api.datagen.builders.templates.FramedItemBuilder;
import net.favouriteless.modopedia.api.datagen.providers.TemplateProvider;
import net.favouriteless.modopedia.book.text.Justify;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.PackOutput;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

public class ETemplateProvider extends TemplateProvider {

    public ETemplateProvider(CompletableFuture<Provider> registries, PackOutput output) {
        super(Enchanted.MOD_ID, registries, output);
    }

    @Override
    protected void build(BiConsumer<String, JsonElement> output) {
        buildRecipes(output);
        buildPages(output);
    }

    protected void buildRecipes(BiConsumer<String, JsonElement> output) {
        TemplateBuilder.of(ByproductRecipeBuilder.ID.getPath())
                .processor(ByproductRecipeProcessor.ID)
                .components(
                        CraftingArrowBuilder.of()
                                .x(22).y(2),
                        CraftingFlameBuilder.of()
                                .x(23).y(25),
                        TooltipBuilder.of(new String[] { "tooltip.enchanted.byproduct_recipe" })
                                .x(22).y(3)
                                .width(16).height(13),
                        FramedItemBuilder.of("#inputs"),
                        FramedItemBuilder.of("#jars")
                                .y(24),
                        FramedItemBuilder.of("#output")
                                .x(44),
                        FramedItemBuilder.of("#byproduct")
                                .x(44).y(24)
                )
                .build(output);

        TemplateBuilder.of(WitchCauldronRecipeBuilder.ID.getPath())
                .processor(CauldronTypeRecipeProcessor.ID)
                .components(
                        CauldronBuilder.of()
                                .x(5).y(49),
                        SeparatorBuilder.of()
                                        .y(10),
                        HeaderBuilder.of("book.enchanted.header.witch_cauldron_recipe"),
                        ItemBuilder.of("#inputs").
                                x(50).y("#inputs_y")
                                .rowMax(5)
                                .centered(true),
                        ItemBuilder.of("#output")
                                .x(42).y(84),
                        TextBuilder.of("#power")
                                .y(126)
                                .justify(Justify.CENTER)
                )
                .build(output);

        TemplateBuilder.of(KettleRecipeBuilder.ID.getPath())
                .processor(CauldronTypeRecipeProcessor.ID)
                .components(
                        CauldronBuilder.of()
                                .x(5).y(49),
                        SeparatorBuilder.of()
                                .y(10),
                        HeaderBuilder.of("book.enchanted.header.kettle_recipe"),
                        ItemBuilder.of("#inputs")
                                .x(50).y("#inputs_y")
                                .rowMax(5)
                                .centered(true),
                        ItemBuilder.of("#output")
                                .x(42).y(84),
                        TextBuilder.of("#power")
                                .y(126)
                                .justify(Justify.CENTER)
                )
                .build(output);

        TemplateBuilder.of(DistillingRecipeBuilder.ID.getPath())
                .processor(DistillingRecipeProcessor.ID)
                .components(
                        DistilleryBuilder.of(),
                        ItemBuilder.of("#firstInput")
                                .x(31).y(2),
                        ItemBuilder.of("#inputs")
                                .x(21).y(22)
                                .padding(20),
                        ItemBuilder.of("#outputs")
                                .x(2).y(95)
                                .padding(19)
                )
                .build(output);
    }

    protected void buildPages(BiConsumer<String, JsonElement> output) {
        TemplateBuilder.of(ByproductPageBuilder.ID.getPath())
                .components(
                        HeaderBuilder.of("book.enchanted.header.fume_extraction"),
                        SeparatorBuilder.of()
                                .y(10),
                        ByproductRecipeBuilder.of("#recipe")
                                .x(19).y(40)
                )
                .build(output);

        TemplateBuilder.of(DoubleByproductPageBuilder.ID.getPath())
                .components(
                        HeaderBuilder.of("book.enchanted.header.fume_extraction"),
                        SeparatorBuilder.of()
                                .y(10),
                        ByproductRecipeBuilder.of("#recipe")
                                .x(19).y(25),
                        ByproductRecipeBuilder.of("#recipe2")
                                .x(19).y(75)
                )
                .build(output);

        TemplateBuilder.of(DistilleryPageBuilder.ID.getPath())
                .components(
                        HeaderBuilder.of("book.enchanted.header.distillation"),
                        SeparatorBuilder.of()
                                .y(10),
                        DistillingRecipeBuilder.of("#recipe")
                                .x(11).y(18)
                )
                .build(output);
    }

}
