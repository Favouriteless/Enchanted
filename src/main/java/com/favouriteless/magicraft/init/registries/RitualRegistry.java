package com.favouriteless.magicraft.init.registries;

import com.favouriteless.magicraft.Magicraft;
import com.favouriteless.magicraft.init.MagicraftRituals;
import com.favouriteless.magicraft.rituals.Ritual;
import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.ForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import net.minecraftforge.registries.RegistryManager;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

public class RitualRegistry implements Iterable<Ritual>{

    private static final RegistryKey<Registry<Ritual>> REGISTRY_KEY = RegistryKey.getOrCreateRootKey(new ResourceLocation(Magicraft.MOD_ID, "ritual_type"));
    private static final Lazy<ForgeRegistry<Ritual>> FORGE_REGISTRY_LAZY = () -> RegistryManager.ACTIVE.getRegistry(REGISTRY_KEY);

    public static final RitualRegistry INSTANCE = new RitualRegistry();

    private RitualRegistry() {
        MagicraftRituals.RITUAL_TYPES.makeRegistry("ritual_type", () ->
                new RegistryBuilder<Ritual>().setMaxID(Integer.MAX_VALUE - 1).setDefaultKey(new ResourceLocation(Magicraft.MOD_ID, "empty_ritual")).disableSaving()
        );
    }

    public static void subscribe(IEventBus modBus) {
        MagicraftRituals.RITUAL_TYPES.register(modBus);
    }

    public RegistryObject<Ritual> register(String id, Supplier<Ritual> ritualSupplier) {
        return MagicraftRituals.RITUAL_TYPES.register(id, ritualSupplier);
    }

    public RegistryObject<Ritual> register(ResourceLocation name, Supplier<Ritual> ritualSupplier) {
        return register(name.getPath(), ritualSupplier);
    }

    public boolean containsKey(ResourceLocation key) {
        return FORGE_REGISTRY_LAZY.get().containsKey(key);
    }

    public boolean containsValue(Ritual value) {
        return FORGE_REGISTRY_LAZY.get().containsValue(value);
    }

    public boolean isEmpty() {
        return FORGE_REGISTRY_LAZY.get().isEmpty();
    }

    @Nullable
    public Ritual getValue(ResourceLocation key) {
        return FORGE_REGISTRY_LAZY.get().getValue(key);
    }

    @Nullable
    public ResourceLocation getKey(Ritual value) {
        return FORGE_REGISTRY_LAZY.get().getKey(value);
    }

    @NotNull
    public Set<ResourceLocation> getKeys() {
        return FORGE_REGISTRY_LAZY.get().getKeys();
    }

    @NotNull
    public Collection<Ritual> getValues() {
        return FORGE_REGISTRY_LAZY.get().getValues();
    }

    @NotNull
    public Set<Map.Entry<RegistryKey<Ritual>, Ritual>> getEntries() {
        return FORGE_REGISTRY_LAZY.get().getEntries();
    }

    @NotNull
    @Override
    public Iterator<Ritual> iterator() {
        return FORGE_REGISTRY_LAZY.get().iterator();
    }
    
}
