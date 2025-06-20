package net.favouriteless.enchanted.integrations.modopedia.client.init;

import net.favouriteless.enchanted.common.Enchanted;
import net.favouriteless.modopedia.api.registries.client.PageComponentRegistry;
import net.favouriteless.modopedia.client.page_components.WidgetPageComponent;
import net.minecraft.resources.ResourceLocation;

public class EPageComponents {

    public static final ResourceLocation ID_CAULDRON = Enchanted.id("cauldron");
    public static final ResourceLocation ID_DISTILLERY = Enchanted.id("distillery");

    public static void load() {
        PageComponentRegistry registry = PageComponentRegistry.get();

        registry.register(ID_CAULDRON, () -> new WidgetPageComponent("cauldron"));
        registry.register(ID_DISTILLERY, () -> new WidgetPageComponent("distillery"));
    }

}
