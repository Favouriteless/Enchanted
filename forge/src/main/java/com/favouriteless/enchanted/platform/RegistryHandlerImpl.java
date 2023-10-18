package com.favouriteless.enchanted.platform;

import com.favouriteless.enchanted.Enchanted;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistry;
import net.minecraftforge.registries.RegistryManager;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class RegistryHandlerImpl extends RegistryHandler.Impl {

	public static final DataLoaderRegister DATA_LOADERS = new DataLoaderRegister();
	private static final RegistryMap registryMap = new RegistryMap();

	@Override
	public <T> Supplier<T> register(Registry<? super T> registry, String name, Supplier<T> entry) {
		return registryMap.register(registry, name, entry);
	}

	@Override
	public void register(ResourceLocation id, SimpleJsonResourceReloadListener loader) {
		DATA_LOADERS.dataLoaders.add(loader);
	}

	@Override
	public CreativeModeTab registerTab(String id, Supplier<Item> iconSupplier) {
		return null;
	}

	@Override
	public DamageSource getDamageSource(String id, boolean bypassArmour, boolean bypassMagic, boolean bypassInvul, boolean isMagic) {
		DamageSource source = new DamageSource(id);
		if(bypassArmour)
			source.bypassArmor();
		if(bypassMagic)
			source.bypassMagic();
		if(bypassInvul)
			source.bypassInvul();
		if(isMagic)
			source.setMagic();
		return source;
	}


	private static class RegistryMap {

		private final Map<ResourceLocation, DeferredRegister<?>> registries = new HashMap<>();

		@SuppressWarnings({"unchecked", "rawtypes"})
		private <T> RegistryObject<T> register(Registry<? super T> registry, String name, Supplier<T> entry) {
			DeferredRegister<T> reg = (DeferredRegister<T>)registries.computeIfAbsent(registry.key().location(), (key) -> {
				ForgeRegistry forgeReg = RegistryManager.ACTIVE.getRegistry(key);

				if(forgeReg == null)
					return null;

				DeferredRegister<T> defReg = DeferredRegister.create(forgeReg, Enchanted.MOD_ID);
				defReg.register(FMLJavaModLoadingContext.get().getModEventBus());

				return defReg;
			});

			return reg != null ? reg.register(name, entry) : null;
		}

	}

	public static class DataLoaderRegister {

		private final List<SimpleJsonResourceReloadListener> dataLoaders = new ArrayList<>(); // Doing no setter means only the RegistrationImpl class can get access to registering more loaders.

		public List<SimpleJsonResourceReloadListener> getLoaders() {
			return dataLoaders;
		}

	}

}
