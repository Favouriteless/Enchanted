package com.favouriteless.enchanted.platform;

import com.favouriteless.enchanted.Enchanted;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.world.damagesource.DamageSource;
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

	public static void register(String id, SimpleJsonResourceReloadListener reloadListener) {
		INSTANCE.register(Enchanted.location(id), reloadListener);
	}

	public static DamageSource getDamageSource(String id, boolean bypassArmour, boolean bypassMagic, boolean bypassInvul, boolean isMagic) {
		return INSTANCE.getDamageSource(id, bypassArmour, bypassMagic, bypassInvul, isMagic);
	}

	public abstract static class Impl {

		protected Impl() {
			INSTANCE = this;
		}

		public abstract <T> Supplier<T> register(Registry<? super T> registry, String name, Supplier<T> entry);
		public abstract void register(ResourceLocation id, SimpleJsonResourceReloadListener loader);
		public abstract CreativeModeTab registerTab(String id, Supplier<Item> iconSupplier);
		public abstract DamageSource getDamageSource(String id, boolean bypassArmour, boolean bypassMagic, boolean bypassInvul, boolean isMagic);

	}

}
