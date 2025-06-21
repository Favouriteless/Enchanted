package net.favouriteless.enchanted.platform.services;

import com.mojang.serialization.Codec;
import net.favouriteless.enchanted.common.Enchanted;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTab.DisplayItemsGenerator;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FireBlock;
import net.minecraft.world.level.block.SoundType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.util.DeferredSoundType;
import net.neoforged.neoforge.network.IContainerFactory;
import net.neoforged.neoforge.registries.DataPackRegistryEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.apache.commons.lang3.function.TriFunction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Supplier;

public class NeoCommonRegistryHelper implements ICommonRegistryHelper {

	private static final RegistryMap registryMap = new RegistryMap();

	public static final List<SimpleJsonResourceReloadListener> dataLoaders = new ArrayList<>();
	public static final List<DataRegistryRegisterable<?>> dataRegistryRegisterables = new ArrayList<>();


	public <C, T extends C> Supplier<T> register(Registry<C> registry, String name, Supplier<T> entry) {
		return registryMap.register(registry, name, entry);
	}

	@Override
	public <C, T extends C> Holder<C> registerHolder(Registry<C> registry, String name, Supplier<T> entry) {
		return registryMap.register(registry, name, entry);
	}

	@Override
	public <T extends AbstractContainerMenu, C> Supplier<MenuType<T>> registerMenu(String name,
																				   TriFunction<Integer, Inventory, C, T> factory,
																				   StreamCodec<? super RegistryFriendlyByteBuf, C> codec) {
		return register(BuiltInRegistries.MENU, name, () -> new MenuType<>((IContainerFactory<T>)(id, inv, buf) -> factory.apply(id, inv, codec.decode(buf)), FeatureFlags.DEFAULT_FLAGS));
	}

	@Override
	public <T extends AbstractContainerMenu> Supplier<MenuType<T>> registerMenu(String name, BiFunction<Integer, Inventory, T> factory) {
		return register(BuiltInRegistries.MENU, name, () -> new MenuType<>(factory::apply, FeatureFlags.DEFAULT_FLAGS));
	}

	@Override
	public void register(ResourceLocation id, SimpleJsonResourceReloadListener loader) {
		dataLoaders.add(loader);
	}

	@Override
	public SoundType createSoundType(float volume, float pitch, Supplier<SoundEvent> breakSound, Supplier<SoundEvent> stepSound, Supplier<SoundEvent> placeSound, Supplier<SoundEvent> hitSound, Supplier<SoundEvent> fallSound) {
		return new DeferredSoundType(volume, pitch, breakSound, stepSound, placeSound, hitSound, fallSound);
	}

	@Override
	public Supplier<CreativeModeTab> registerCreativeTab(String name, Supplier<ItemStack> iconSupplier, DisplayItemsGenerator itemGenerator) {
		return register(BuiltInRegistries.CREATIVE_MODE_TAB, name, () -> CreativeModeTab.builder()
				.title(Component.translatable("tab." + Enchanted.MOD_ID + "." + name))
				.icon(iconSupplier)
				.displayItems(itemGenerator)
				.build());
	}

	@Override
	public <T> ResourceKey<Registry<T>> registerDataRegistry(ResourceKey<Registry<T>> key, Codec<T> codec) {
		dataRegistryRegisterables.add(new DataRegistryRegisterable<>(key, codec, null));
		return key;
	}

	@Override
	public <T> ResourceKey<Registry<T>> registerSyncedDataRegistry(ResourceKey<Registry<T>> key, Codec<T> codec, Codec<T> networkCodec) {
		dataRegistryRegisterables.add(new DataRegistryRegisterable<>(key, codec, networkCodec));
		return key;
	}

	@Override
	public void setFlammable(Block block, int igniteOdds, int burnOdds) {
		((FireBlock)Blocks.FIRE).setFlammable(block, igniteOdds, burnOdds);
	}

	public static RegistryMap getRegistryMap() {
		return registryMap;
	}



	public static class RegistryMap {

		private final Map<ResourceLocation, DeferredRegister<?>> registries = new HashMap<>();

		private <C, T extends C> DeferredHolder<C, T> register(Registry<C> registry, String name, Supplier<T> entry) {
			DeferredRegister<C> reg = getDeferred(registry);
			return reg != null ? reg.register(name, entry) : null;
		}

		@SuppressWarnings({"unchecked"})
		public <T> DeferredRegister<T> getDeferred(Registry<? super T> registry) {
            return (DeferredRegister<T>)registries.computeIfAbsent(registry.key().location(), (key) ->
					DeferredRegister.create(registry.key().location(), Enchanted.MOD_ID)
			);
		}

		public void register(IEventBus bus) {
			registries.values().forEach(reg -> reg.register(bus));
		}

	}

	public record DataRegistryRegisterable<T>(ResourceKey<Registry<T>> key, Codec<T> codec, Codec<T> networkCodec) {

		public void register(DataPackRegistryEvent.NewRegistry event) {
			if(networkCodec == null)
				event.dataPackRegistry(key, codec);
			else
				event.dataPackRegistry(key, codec, networkCodec);
		}

	}

}
