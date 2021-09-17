/*
 * Copyright (c) 2021. Favouriteless
 * Enchanted, a minecraft mod.
 * GNU GPLv3 License
 *
 *     This file is part of Enchanted.
 *
 *     Enchanted is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Enchanted is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Enchanted.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.favouriteless.enchanted.core.init.registries;

import com.favouriteless.enchanted.common.rituals.AbstractRitual;
import com.favouriteless.enchanted.Enchanted;
import com.favouriteless.enchanted.core.init.EnchantedRituals;

import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.ForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import net.minecraftforge.registries.RegistryManager;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

public class RitualRegistry implements Iterable<AbstractRitual>{

    private static final RegistryKey<Registry<AbstractRitual>> REGISTRY_KEY = RegistryKey.createRegistryKey(new ResourceLocation(Enchanted.MOD_ID, "ritual_type"));
    private static final Lazy<ForgeRegistry<AbstractRitual>> FORGE_REGISTRY_LAZY = () -> RegistryManager.ACTIVE.getRegistry(REGISTRY_KEY);

    public static final RitualRegistry INSTANCE = new RitualRegistry();

    public RitualRegistry() { }

    public static void init() {
        EnchantedRituals.RITUAL_TYPES.makeRegistry("ritual_type", () ->
                new RegistryBuilder<AbstractRitual>().setMaxID(Integer.MAX_VALUE - 1).setDefaultKey(new ResourceLocation(Enchanted.MOD_ID, "empty_ritual")).disableSaving()
        );
    }

    public RegistryObject<AbstractRitual> register(String id, Supplier<AbstractRitual> ritualSupplier) {
        return EnchantedRituals.RITUAL_TYPES.register(id, ritualSupplier);
    }

    public RegistryObject<AbstractRitual> register(ResourceLocation name, Supplier<AbstractRitual> ritualSupplier) {
        return register(name.getPath(), ritualSupplier);
    }

    public boolean containsKey(ResourceLocation key) {
        return FORGE_REGISTRY_LAZY.get().containsKey(key);
    }

    public boolean containsValue(AbstractRitual value) {
        return FORGE_REGISTRY_LAZY.get().containsValue(value);
    }

    public boolean isEmpty() {
        return FORGE_REGISTRY_LAZY.get().isEmpty();
    }

    @Nullable
    public AbstractRitual getValue(ResourceLocation key) {
        return FORGE_REGISTRY_LAZY.get().getValue(key);
    }

    @Nullable
    public ResourceLocation getKey(AbstractRitual value) {
        return FORGE_REGISTRY_LAZY.get().getKey(value);
    }

    public Set<ResourceLocation> getKeys() {
        return FORGE_REGISTRY_LAZY.get().getKeys();
    }

    public Collection<AbstractRitual> getValues() {
        return FORGE_REGISTRY_LAZY.get().getValues();
    }

    public Set<Map.Entry<RegistryKey<AbstractRitual>, AbstractRitual>> getEntries() {
        return FORGE_REGISTRY_LAZY.get().getEntries();
    }

    @Override
    public Iterator<AbstractRitual> iterator() {
        return FORGE_REGISTRY_LAZY.get().iterator();
    }
    
}
