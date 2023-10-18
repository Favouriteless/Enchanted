package com.favouriteless.enchanted.platform;

import com.favouriteless.enchanted.Enchanted;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.core.Registry;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.function.Supplier;

public class RegistryHandlerImpl extends RegistryHandler.Impl {

	@Override
	public <T> Supplier<T> register(Registry<? super T> registry, String name, Supplier<T> entry) {
		Registry.register(registry, Enchanted.location(name), entry.get());
		return entry;
	}

	@Override
	public <T extends Entity> void registerEntityRenderer(EntityType<T> type, EntityRendererProvider<T> constructor) {
		EntityRendererRegistry.register(type, constructor);
	}

	@Override
	public <T extends BlockEntity> void registerBlockEntityRenderer(BlockEntityType<T> type, BlockEntityRendererProvider<T> constructor) {
		BlockEntityRenderers.register(type, constructor);
	}

	@Override
	public CreativeModeTab registerTab(String id, Supplier<Item> iconSupplier) {
		return null;
	}

}
