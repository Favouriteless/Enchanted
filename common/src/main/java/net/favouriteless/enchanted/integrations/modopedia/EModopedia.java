package net.favouriteless.enchanted.integrations.modopedia;

import net.favouriteless.enchanted.integrations.modopedia.client.init.EBookScreenFactories;
import net.favouriteless.enchanted.integrations.modopedia.client.init.EPageComponents;
import net.favouriteless.enchanted.integrations.modopedia.client.init.ETemplateProcessors;
import net.favouriteless.enchanted.integrations.modopedia.common.init.EBookTypes;

public class EModopedia {

    public static void initClient() {
        EPageComponents.load();
        ETemplateProcessors.load();
        EBookScreenFactories.load();;
    }

    public static void initCommon() {
        EBookTypes.load();
    }

}
