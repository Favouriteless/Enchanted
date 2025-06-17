package net.favouriteless.enchanted.integrations.modopedia.common.init;

import net.favouriteless.enchanted.integrations.modopedia.common.EClassicBookType;
import net.favouriteless.modopedia.api.registries.common.BookTypeRegistry;

public class EBookTypes {

    public static void load() {
        BookTypeRegistry registry = BookTypeRegistry.get();

        registry.register(EClassicBookType.TYPE);
    }

}
