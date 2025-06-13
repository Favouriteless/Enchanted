package net.favouriteless.enchanted.integrations.modopedia.init.client;

import net.favouriteless.enchanted.common.Enchanted;
import net.favouriteless.modopedia.api.registries.client.PageComponentRegistry;
import net.favouriteless.modopedia.client.page_components.WidgetPageComponent;

public class EPageComponents {

    public static void load() {
        PageComponentRegistry registry = PageComponentRegistry.get();

        registry.register(Enchanted.id("cauldron"), () -> new WidgetPageComponent("cauldron"));
    }

}
