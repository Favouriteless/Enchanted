package net.favouriteless.enchanted.integrations.modopedia.client.init;

import net.favouriteless.enchanted.common.Enchanted;
import net.favouriteless.enchanted.integrations.modopedia.client.page_components.ItemCirclePageComponent;
import net.favouriteless.modopedia.api.registries.client.PageComponentRegistry;
import net.favouriteless.modopedia.client.page_components.WidgetPageComponent;
import net.minecraft.resources.ResourceLocation;

public class EPageComponents {

    public static final ResourceLocation ID_CAULDRON = Enchanted.id("cauldron");
    public static final ResourceLocation ID_DISTILLERY = Enchanted.id("distillery");
    public static final ResourceLocation ID_MUTAGEN = Enchanted.id("mutagen");

    public static void load() {
        PageComponentRegistry registry = PageComponentRegistry.get();

        registry.register(ID_CAULDRON, () -> new WidgetPageComponent("cauldron"));
        registry.register(ID_DISTILLERY, () -> new WidgetPageComponent("distillery"));
        registry.register(ID_MUTAGEN, () -> new WidgetPageComponent("mutagen"));
        registry.register(ItemCirclePageComponent.ID, ItemCirclePageComponent::new);
    }

}
