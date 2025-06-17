package net.favouriteless.enchanted.client;

import net.favouriteless.enchanted.common.init.EKeybinds;
import net.favouriteless.enchanted.integrations.modopedia.client.init.EBookScreenFactories;
import net.favouriteless.enchanted.integrations.modopedia.client.init.EPageComponents;
import net.favouriteless.enchanted.integrations.modopedia.client.init.ETemplateProcessors;

public class EnchantedClient {

    public static void init() {
        EKeybinds.load();
        EShaders.load();

        EPageComponents.load();
        ETemplateProcessors.load();
        EBookScreenFactories.load();;
    }

}
