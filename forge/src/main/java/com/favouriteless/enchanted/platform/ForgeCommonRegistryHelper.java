package com.favouriteless.enchanted.platform;

import com.favouriteless.enchanted.Enchanted;
import com.favouriteless.enchanted.platform.services.ICommonRegistryHelper;
import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.SoundType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.common.util.ForgeSoundType;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.*;
import org.apache.commons.lang3.function.TriFunction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class ForgeCommonRegistryHelper implements ICommonRegistryHelper {

	public static final DataLoaderRegister DATA_LOADERS = new DataLoaderRegister();
	private static final RegistryMap registryMap = new RegistryMap();

	@Override
	public <T> Supplier<T> register(Registry<? super T> registry, String name, Supplier<T> entry) {
		return registryMap.register(registry, name, entry);
	}

	@Override
	public <T extends AbstractContainerMenu> Supplier<MenuType<T>> registerMenu(String name, TriFunction<Integer, Inventory, FriendlyByteBuf, T> create) {
		return register(Registry.MENU, name, () -> IForgeMenuType.create(create::apply));
	}

	@Override
	public void register(ResourceLocation id, SimpleJsonResourceReloadListener loader) {
		DATA_LOADERS.dataLoaders.add(loader);
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

	@Override
	public SoundType getSoundType(float volume, float pitch, Supplier<SoundEvent> breakSound, Supplier<SoundEvent> stepSound, Supplier<SoundEvent> placeSound, Supplier<SoundEvent> hitSound, Supplier<SoundEvent> fallSound) {
		return new ForgeSoundType(volume, pitch, breakSound, stepSound, placeSound, hitSound, fallSound);
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
