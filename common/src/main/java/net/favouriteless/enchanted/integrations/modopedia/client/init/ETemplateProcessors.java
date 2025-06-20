package net.favouriteless.enchanted.integrations.modopedia.client.init;

import net.favouriteless.enchanted.integrations.modopedia.client.template_processors.ByproductRecipeProcessor;
import net.favouriteless.enchanted.integrations.modopedia.client.template_processors.CauldronTypeRecipeProcessor;
import net.favouriteless.enchanted.integrations.modopedia.client.template_processors.DistillingRecipeProcessor;
import net.favouriteless.modopedia.api.registries.client.TemplateRegistry;

public class ETemplateProcessors {

    public static void load() {
        TemplateRegistry registry = TemplateRegistry.get();

        registry.registerProcessor(CauldronTypeRecipeProcessor.ID, new CauldronTypeRecipeProcessor());
        registry.registerProcessor(ByproductRecipeProcessor.ID, new ByproductRecipeProcessor());
        registry.registerProcessor(DistillingRecipeProcessor.ID, new DistillingRecipeProcessor());
    }

}
