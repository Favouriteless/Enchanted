package net.favouriteless.enchanted.client;

import net.favouriteless.enchanted.common.Enchanted;
import net.favouriteless.enchanted.common.init.EKeybinds;
import net.favouriteless.enchanted.common.modopedia.CenteredTitleBookType;
import net.favouriteless.enchanted.common.modopedia.EPageComponents;
import net.favouriteless.enchanted.common.modopedia.ETemplateProcessors;
import net.favouriteless.modopedia.api.registries.BookTypeRegistry;

public class EnchantedClient {

    public static void init() {
        EKeybinds.load();
        EShaders.load();

        // Modopedia
        EPageComponents.load();
        ETemplateProcessors.load();
        BookTypeRegistry.get().register(Enchanted.id("classic"), new CenteredTitleBookType());
    }

}
