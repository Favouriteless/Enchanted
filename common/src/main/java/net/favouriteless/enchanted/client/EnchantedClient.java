package net.favouriteless.enchanted.client;

import net.favouriteless.enchanted.common.init.EKeybinds;
import net.favouriteless.enchanted.integrations.modopedia.init.client.EBookScreenFactories;
import net.favouriteless.enchanted.integrations.modopedia.init.client.EPageComponents;
import net.favouriteless.enchanted.integrations.modopedia.init.client.ETemplateProcessors;

public class EnchantedClient {

    public static void init() {
        EKeybinds.load();
        EShaders.load();

        EPageComponents.load();
        ETemplateProcessors.load();
        EBookScreenFactories.load();;
    }

}
