package net.favouriteless.enchanted.neoforge.datagen.providers.modopedia;

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
import net.favouriteless.modopedia.api.datagen.BookContentOutput;
import net.favouriteless.modopedia.api.datagen.builders.TemplateBuilder;
import net.favouriteless.modopedia.api.datagen.builders.page_components.components.*;
import net.favouriteless.modopedia.api.datagen.builders.templates.FramedItemBuilder;
import net.favouriteless.modopedia.api.datagen.providers.TemplateProvider;
import net.favouriteless.modopedia.book.text.Justify;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.PackOutput;

import java.util.concurrent.CompletableFuture;

public class ETemplateProvider extends TemplateProvider {

    public ETemplateProvider(CompletableFuture<Provider> registries, PackOutput output) {
        super(Enchanted.MOD_ID, registries, output);
    }

    @Override
    protected void build(Provider provider, BookContentOutput output) {
        buildRecipes(output);
        buildPages(output);
    }

    protected void buildRecipes(BookContentOutput output) {
        TemplateBuilder.of()
                .processor(ByproductRecipeProcessor.ID)
                .components(
                        CraftingArrowBuilder.of()
                                .x(22).y(2),
                        CraftingFlameBuilder.of()
                                .x(23).y(25),
                        TooltipBuilder.of(new String[] { "tooltip.enchanted.byproduct_recipe" })
                                .x(22).y(3)
                                .width(16).height(13),
                        FramedItemBuilder.of("#p_inputs"),
                        FramedItemBuilder.of("#p_jars")
                                .y(24),
                        FramedItemBuilder.of("#p_output")
                                .x(44),
                        FramedItemBuilder.of("#p_byproduct")
                                .x(44).y(24)
                )
                .build(ByproductRecipeBuilder.ID.getPath(), output);

        TemplateBuilder.of()
                .processor(CauldronTypeRecipeProcessor.ID)
                .components(
                        CauldronBuilder.of()
                                .x(5).y(49),
                        SeparatorBuilder.of()
                                        .y(10),
                        HeaderBuilder.of("book.enchanted.header.witch_cauldron_recipe"),
                        ItemBuilder.of("#p_inputs").
                                x(50).y("#p_inputs_y")
                                .rowMax(5)
                                .centered(true),
                        ItemBuilder.of("#p_output")
                                .x(42).y(84),
                        TextBuilder.of("#p_power")
                                .y(126)
                                .justify(Justify.CENTER)
                )
                .build(WitchCauldronRecipeBuilder.ID.getPath(), output);

        TemplateBuilder.of()
                .processor(CauldronTypeRecipeProcessor.ID)
                .components(
                        CauldronBuilder.of()
                                .x(5).y(49),
                        SeparatorBuilder.of()
                                .y(10),
                        HeaderBuilder.of("book.enchanted.header.kettle_recipe"),
                        ItemBuilder.of("#p_inputs")
                                .x(50).y("#p_inputs_y")
                                .rowMax(5)
                                .centered(true),
                        ItemBuilder.of("#p_output")
                                .x(42).y(84),
                        TextBuilder.of("#p_power")
                                .y(126)
                                .justify(Justify.CENTER)
                )
                .build(KettleRecipeBuilder.ID.getPath(), output);

        TemplateBuilder.of()
                .processor(DistillingRecipeProcessor.ID)
                .components(
                        DistilleryBuilder.of()
                                .x("#p_x"),
                        ItemBuilder.of("#p_input1")
                                .x("#p_input1_x").y(2),
                        ItemBuilder.of("#p_input2")
                                .x("#p_input2_x").y(22)
                                .padding(20),
                        ItemBuilder.of("#p_output1")
                                .x("#p_output1_x").y(95)
                                .padding(19),
                        ItemBuilder.of("#p_output2")
                                .x("#p_output2_x").y(95)
                                .padding(19),
                        TextBuilder.of("#p_power")
                                .y(113)
                                .justify(Justify.CENTER)
                )
                .build(DistillingRecipeBuilder.ID.getPath(), output);
    }

    protected void buildPages(BookContentOutput output) {
        TemplateBuilder.of()
                .components(
                        HeaderBuilder.of("book.enchanted.header.fume_extraction"),
                        SeparatorBuilder.of()
                                .y(10),
                        ByproductRecipeBuilder.of("#recipe")
                                .x(19).y(40)
                )
                .build(ByproductPageBuilder.ID.getPath(), output);

        TemplateBuilder.of()
                .components(
                        HeaderBuilder.of("book.enchanted.header.fume_extraction"),
                        SeparatorBuilder.of()
                                .y(10),
                        ByproductRecipeBuilder.of("#recipe1")
                                .x(19).y(25),
                        ByproductRecipeBuilder.of("#recipe2")
                                .x(19).y(75)
                )
                .build(DoubleByproductPageBuilder.ID.getPath(), output);

        TemplateBuilder.of()
                .components(
                        HeaderBuilder.of("book.enchanted.header.distillation"),
                        SeparatorBuilder.of().y(10),
                        DistillingRecipeBuilder.of("#recipe").y(18)
                )
                .build(DistilleryPageBuilder.ID.getPath(), output);
    }

}
