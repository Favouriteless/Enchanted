package com.favouriteless.enchanted.platform;

import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class ClientRegistryHandler {

	private static Impl INSTANCE = null; // Singleton instance.

	public static <T extends Entity> void register(EntityType<T> type, EntityRendererProvider<T> constructor) {
		INSTANCE.registerEntityRenderer(type, constructor);
	}

	public static <T extends BlockEntity> void register(BlockEntityType<T> type, BlockEntityRendererProvider<T> constructor) {
		INSTANCE.registerBlockEntityRenderer(type, constructor);
	}

	public abstract static class Impl {

		protected Impl() {
			INSTANCE = this;
		}

		public abstract <T extends Entity> void registerEntityRenderer(EntityType<T> type, EntityRendererProvider<T> constructor);
		public abstract <T extends BlockEntity> void registerBlockEntityRenderer(BlockEntityType<T> type, BlockEntityRendererProvider<T> constructor);

	}

}
