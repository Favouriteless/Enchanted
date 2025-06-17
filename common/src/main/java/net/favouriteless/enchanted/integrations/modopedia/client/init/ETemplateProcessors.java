package net.favouriteless.enchanted.integrations.modopedia.client.init;

import net.favouriteless.enchanted.common.Enchanted;
import net.favouriteless.enchanted.integrations.modopedia.client.template_processors.BlockPageProcessor;
import net.favouriteless.enchanted.integrations.modopedia.client.template_processors.ByproductRecipeProcessor;
import net.favouriteless.enchanted.integrations.modopedia.client.template_processors.CauldronTypeRecipeProcessor;
import net.favouriteless.enchanted.integrations.modopedia.client.template_processors.DistillingRecipeProcessor;
import net.favouriteless.modopedia.api.registries.client.TemplateRegistry;

public class ETemplateProcessors {

    public static void load() {
        TemplateRegistry registry = TemplateRegistry.get();

        registry.registerProcessor(Enchanted.id("block_page"), new BlockPageProcessor());
        registry.registerProcessor(Enchanted.id("cauldron_type_recipe"), new CauldronTypeRecipeProcessor());
        registry.registerProcessor(Enchanted.id("byproduct_recipe"), new ByproductRecipeProcessor());
        registry.registerProcessor(Enchanted.id("distilling_recipe"), new DistillingRecipeProcessor());
    }

}
