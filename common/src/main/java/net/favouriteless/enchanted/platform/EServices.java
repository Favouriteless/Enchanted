package net.favouriteless.enchanted.platform;

import net.favouriteless.enchanted.common.Enchanted;
import net.favouriteless.enchanted.platform.services.AttachmentHelper;
import net.favouriteless.enchanted.platform.services.CommonRegistryHelper;
import net.favouriteless.enchanted.platform.services.NetworkHelper;
import net.favouriteless.enchanted.platform.services.PlatformHelper;

import java.util.ServiceLoader;

public class EServices {

    public static final AttachmentHelper ATTACHMENT = load(AttachmentHelper.class);
    public static final NetworkHelper NETWORK = load(NetworkHelper.class);
    public static final PlatformHelper PLATFORM = load(PlatformHelper.class);
    public static final CommonRegistryHelper REGISTRY = load(CommonRegistryHelper.class);

    public static <T> T load(Class<T> clazz) {
        final T loadedService = ServiceLoader.load(clazz)
                .findFirst()
                .orElseThrow(() -> new NullPointerException("Failed to load service for " + clazz.getName()));
        Enchanted.LOG.debug("Loaded {} for service {}", loadedService, clazz);
        return loadedService;
    }

}