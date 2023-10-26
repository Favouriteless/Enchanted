package com.favouriteless.enchanted.platform;

import com.favouriteless.enchanted.Enchanted;
import com.favouriteless.enchanted.platform.services.*;

import java.util.ServiceLoader;

public class Services {

    public static final IPlatformHelper PLATFORM = load(IPlatformHelper.class);
    public static final ICommonRegistryHelper COMMON_REGISTRY = load(ICommonRegistryHelper.class);
    public static final IClientRegistryHelper CLIENT_REGISTRY = load(IClientRegistryHelper.class);
    public static final INetworkHelper NETWORK = load(INetworkHelper.class);

    public static <T> T load(Class<T> clazz) {
        final T loadedService = ServiceLoader.load(clazz).findFirst().orElseThrow(() -> new NullPointerException("Failed to load service for " + clazz.getName()));
        Enchanted.LOG.debug("Loaded {} for service {}", loadedService, clazz);
        return loadedService;
    }

}