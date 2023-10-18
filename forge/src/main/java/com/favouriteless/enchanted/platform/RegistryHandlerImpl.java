package com.favouriteless.enchanted.platform;

import com.favouriteless.enchanted.Enchanted;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistry;
import net.minecraftforge.registries.RegistryManager;
import net.minecraftforge.registries.RegistryObject;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class RegistryHandlerImpl extends RegistryHandler.Impl {

	private static final RegistryMap registryMap = new RegistryMap();

	@Override
	public <T> Supplier<T> register(Registry<? super T> registry, String name, Supplier<T> entry) {
		return registryMap.register(registry, name, entry);
	}

	@Override
	public <T extends Entity> void registerEntityRenderer(EntityType<T> type, EntityRendererProvider<T> constructor) {
		EntityRenderers.register(type, constructor);
	}

	@Override
	public <T extends BlockEntity> void registerBlockEntityRenderer(BlockEntityType<T> type, BlockEntityRendererProvider<T> constructor) {
		BlockEntityRenderers.register(type, constructor);
	}

	@Override
	public CreativeModeTab registerTab(String id, Supplier<Item> iconSupplier) {
		return null;
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

}
