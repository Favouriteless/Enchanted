package com.favouriteless.enchanted.platform.services;

public interface IPlatformHelper {

    String getPlatformName();
    boolean isModLoaded(String modId);
    boolean isDevelopmentEnvironment();

}