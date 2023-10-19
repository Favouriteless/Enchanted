package com.favouriteless.enchanted.platform.services;

import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

public interface IClientRegistryHelper {

    /**
     * Register an {@link EntityRenderer}.
     * @param type The {@link EntityType} to use this renderer for.
     * @param constructor A {@link EntityRendererProvider} for the {@link EntityRenderer} being registered.
     */
    <T extends Entity> void register(EntityType<T> type, EntityRendererProvider<T> constructor);

    /**
     * Register an {@link BlockEntityRenderer}.
     * @param type The {@link BlockEntityType} to use this renderer for.
     * @param constructor A {@link BlockEntityRendererProvider} for the {@link BlockEntityRenderer} being registered.
     */
    <T extends BlockEntity> void register(BlockEntityType<T> type, BlockEntityRendererProvider<T> constructor);

}
