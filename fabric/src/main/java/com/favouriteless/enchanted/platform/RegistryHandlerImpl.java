package com.favouriteless.enchanted.platform;

import com.favouriteless.enchanted.Enchanted;
import com.favouriteless.enchanted.mixin.fabric.DamageSourceAccessor;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.SoundType;

import java.util.function.Supplier;

public class RegistryHandlerImpl extends RegistryHandler.Impl {

	@Override
	public <T> Supplier<T> register(Registry<? super T> registry, String name, Supplier<T> entry) {
		Registry.register(registry, Enchanted.location(name), entry.get());
		return entry;
	}

	@Override
	public void register(ResourceLocation id, SimpleJsonResourceReloadListener loader) {
		ResourceManagerHelper.get(PackType.SERVER_DATA).registerReloadListener(new JsonDataLoaderWrapper(id, loader)); // Fabric impl adds a wrapper for loaders.
	}

	@Override
	public CreativeModeTab registerTab(String id, Supplier<Item> iconSupplier) {
		return null;
	}

	@Override
	public DamageSource getDamageSource(String id, boolean bypassArmour, boolean bypassMagic, boolean bypassInvul, boolean isMagic) {
		DamageSource source = new EnchantedDamageSource(id);
		DamageSourceAccessor accessor = (DamageSourceAccessor)source;

		if(bypassArmour)
			accessor.setBypassArmor();
		if(bypassMagic)
			accessor.setBypassMagic();
		if(bypassInvul)
			accessor.setBypassInvul();
		if(isMagic)
			accessor.setMagic();

		return source;
	}

	@Override
	public SoundType getSoundType(float volume, float pitch, Supplier<SoundEvent> breakSound, Supplier<SoundEvent> stepSound, Supplier<SoundEvent> placeSound, Supplier<SoundEvent> hitSound, Supplier<SoundEvent> fallSound) {
		return new SoundType(volume, pitch, breakSound.get(), stepSound.get(), placeSound.get(), hitSound.get(), fallSound.get());
	}


	private static class EnchantedDamageSource extends DamageSource {

		public EnchantedDamageSource(String string) {
			super(string);
		}

	}

}
