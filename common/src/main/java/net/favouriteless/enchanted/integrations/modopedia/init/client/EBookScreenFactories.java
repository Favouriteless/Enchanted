package net.favouriteless.enchanted.integrations.modopedia.init.client;

import net.favouriteless.enchanted.integrations.modopedia.client.EClassicScreenFactory;
import net.favouriteless.enchanted.integrations.modopedia.common.EClassicBookType;
import net.favouriteless.modopedia.api.registries.client.BookScreenFactoryRegistry;

public class EBookScreenFactories {

    public static void load() {
        BookScreenFactoryRegistry registry = BookScreenFactoryRegistry.get();

        registry.register(EClassicBookType.TYPE, new EClassicScreenFactory());
    }

}
