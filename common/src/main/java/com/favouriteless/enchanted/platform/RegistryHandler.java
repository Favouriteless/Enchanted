package com.favouriteless.enchanted.platform;

import com.favouriteless.enchanted.Enchanted;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.core.Registry;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.function.Supplier;

public class RegistryHandler {

	private static Impl INSTANCE = null; // Singleton instance.

	public static <T> Supplier<T> register(Registry<? super T> registry, String name, Supplier<T> entry) {
		return INSTANCE.register(registry, name, entry);
	}

	public static <T extends Entity> void register(EntityType<T> type, EntityRendererProvider<T> constructor) {
		INSTANCE.registerEntityRenderer(type, constructor);
	}

	public static <T extends BlockEntity> void register(BlockEntityType<T> type, BlockEntityRendererProvider<T> constructor) {
		INSTANCE.registerBlockEntityRenderer(type, constructor);
	}

	public static CreativeModeTab registerTab(String name, Supplier<Item> iconSupplier) {
		return INSTANCE.registerTab(Enchanted.MOD_ID + "." + name, iconSupplier);
	}

	public abstract static class Impl {

		protected Impl() {
			INSTANCE = this;
		}

		public abstract <T> Supplier<T> register(Registry<? super T> registry, String name, Supplier<T> entry);
		public abstract <T extends Entity> void registerEntityRenderer(EntityType<T> type, EntityRendererProvider<T> constructor);
		public abstract <T extends BlockEntity> void registerBlockEntityRenderer(BlockEntityType<T> type, BlockEntityRendererProvider<T> constructor);
		public abstract CreativeModeTab registerTab(String id, Supplier<Item> iconSupplier);

	}

}
