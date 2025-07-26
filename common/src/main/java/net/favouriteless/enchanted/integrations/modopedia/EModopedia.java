package net.favouriteless.enchanted.integrations.modopedia;

import com.google.common.reflect.TypeToken;
import net.favouriteless.enchanted.common.circle_magic.RiteType;
import net.favouriteless.enchanted.integrations.modopedia.client.init.EBookScreenFactories;
import net.favouriteless.enchanted.integrations.modopedia.client.init.EPageComponents;
import net.favouriteless.enchanted.integrations.modopedia.client.init.ETemplateProcessors;
import net.favouriteless.enchanted.integrations.modopedia.common.init.EBookTypes;
import net.favouriteless.modopedia.api.Variable;
import net.minecraft.core.Holder;
import org.jetbrains.annotations.Nullable;

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
