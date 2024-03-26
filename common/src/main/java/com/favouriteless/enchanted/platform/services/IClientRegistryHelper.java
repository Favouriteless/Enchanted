package com.favouriteless.enchanted.platform.services;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import software.bernie.geckolib3.renderers.geo.GeoArmorRenderer;

import java.util.function.Supplier;

public interface IClientRegistryHelper {

    /**
     * Register an {@link EntityRenderer}.
     * @param type The {@link EntityType} to use this renderer for.
     * @param constructor A {@link EntityRendererProvider} for the {@link EntityRenderer} being registered.
     */
    <T extends Entity> void register(EntityType<T> type, EntityRendererProvider<T> constructor);

    /**
     * Register a {@link BlockEntityRenderer}.
     * @param type The {@link BlockEntityType} to use this renderer for.
     * @param constructor A {@link BlockEntityRendererProvider} for the {@link BlockEntityRenderer} being registered.
     */
    <T extends BlockEntity> void register(BlockEntityType<T> type, BlockEntityRendererProvider<T> constructor);

    /**
     * Register a {@link KeyMapping}.
     *
     * @param name The name translation key of the {@link KeyMapping} being created.
     * @param keyCode Unicode keycode of the default key for this {@link KeyMapping}.
     * @param category The category translation key of this {@link KeyMapping}.
     * @param conflictContext {@link KeyConflictContext} representing Forge's enum by the same name, only used by Forge.
     *                        This is used to determine key conflicts.
     *
     * @return The {@link KeyMapping} after it has been registered.
     */
    KeyMapping register(String name, int keyCode, String category, KeyConflictContext conflictContext);

    /**
     * Register a LayerDefinition
     * @param layerLocation Location for the layer.
     * @param supplier The layer to be registered.
     */
    void register(ModelLayerLocation layerLocation, Supplier<LayerDefinition> supplier);

    /**
     * Register a {@link GeoArmorRenderer}.
     * @param clazz The class of the armor items.
     * @param rendererSupplier A {@link Supplier} returning an instance of the renderer.
     * @param items The items this render is applicable for (only used on fabric)
     */
    @SuppressWarnings("rawtypes")
    void register(Class<? extends ArmorItem> clazz, Supplier<GeoArmorRenderer> rendererSupplier, Item... items);


    /**
     * Represents Forge's enum of the same name, so this can actually compile. Forge implementation will grab the ordinal
     * of this and convert it into its own KeyConflictContext.
     */
    enum KeyConflictContext {
        UNIVERSAL,
        GUI,
        IN_GAME
    }

}
