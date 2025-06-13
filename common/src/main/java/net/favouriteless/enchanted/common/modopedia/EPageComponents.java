package net.favouriteless.enchanted.common.modopedia;

import net.favouriteless.enchanted.common.Enchanted;
import net.favouriteless.modopedia.api.registries.PageComponentRegistry;
import net.favouriteless.modopedia.book.page_components.WidgetPageComponent;

public class EPageComponents {

    public static void load() {
        PageComponentRegistry registry = PageComponentRegistry.get();

        registry.register(Enchanted.id("cauldron"), () -> new WidgetPageComponent("cauldron"));
    }

}
