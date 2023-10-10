package com.favouriteless.enchanted.platform;

import com.favouriteless.enchanted.Enchanted;
import net.minecraft.core.Registry;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;

import java.util.function.Supplier;

public class RegistryHandler {

	private static Impl INSTANCE = null; // Singleton instance.

	public static <T> Supplier<T> register(Registry<? super T> registry, String name, Supplier<T> entry) {
		return INSTANCE.register(registry, name, entry);
	}

	public static CreativeModeTab registerTab(String name, Supplier<Item> iconSupplier) {
		return INSTANCE.registerTab(Enchanted.MOD_ID + "." + name, iconSupplier);
	}



	public abstract static class Impl {

		protected Impl() {
			INSTANCE = this;
		}

		public abstract <T> Supplier<T> register(Registry<? super T> registry, String name, Supplier<T> entry);
		public abstract CreativeModeTab registerTab(String id, Supplier<Item> iconSupplier);

	}

}
