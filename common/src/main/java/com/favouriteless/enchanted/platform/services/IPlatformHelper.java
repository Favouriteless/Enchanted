package com.favouriteless.enchanted.platform.services;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.level.block.entity.BlockEntity;

public interface IPlatformHelper {

    String getPlatformName();

    boolean isModLoaded(String modId);

    boolean isDevelopmentEnvironment();

    /**
     * Open a {@link Screen} for a {@link BlockEntity}, the {@link BlockPos} of the entity will be written to the buffer
     * sent to clients.
     *
     * @param player The {@link ServerPlayer} to open the screen for.
     * @param provider The {@link MenuProvider} the screen is being opened for.
     * @param pos The {@link BlockPos} of the provider.
     */
    void openMenuScreen(ServerPlayer player, MenuProvider provider, BlockPos pos);


}