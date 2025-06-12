package net.favouriteless.enchanted.integrations.modopedia;

import net.favouriteless.enchanted.common.Enchanted;
import net.favouriteless.enchanted.integrations.modopedia.template_processors.BlockPageProcessor;
import net.favouriteless.enchanted.integrations.modopedia.template_processors.CauldronTypeRecipeProcessor;
import net.favouriteless.modopedia.api.registries.TemplateRegistry;

public class ETemplateProcessors {

    public static void load() {
        TemplateRegistry registry = TemplateRegistry.get();

        registry.registerProcessor(Enchanted.id("block_page"), new BlockPageProcessor());
        registry.registerProcessor(Enchanted.id("cauldron_type_recipe"), new CauldronTypeRecipeProcessor());
    }

}
