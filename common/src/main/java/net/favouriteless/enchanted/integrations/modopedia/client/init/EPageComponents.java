package net.favouriteless.enchanted.integrations.modopedia.client.init;

import net.favouriteless.enchanted.common.Enchanted;
import net.favouriteless.modopedia.api.registries.client.PageComponentRegistry;
import net.favouriteless.modopedia.client.page_components.WidgetPageComponent;

public class EPageComponents {

    public static void load() {
        PageComponentRegistry registry = PageComponentRegistry.get();

        registry.register(Enchanted.id("cauldron"), () -> new WidgetPageComponent("cauldron"));
        registry.register(Enchanted.id("distillery"), () -> new WidgetPageComponent("distillery"));
    }

}
