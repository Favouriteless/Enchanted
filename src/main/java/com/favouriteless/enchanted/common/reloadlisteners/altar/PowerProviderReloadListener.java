/*
 *
 *   Copyright (c) 2023. Favouriteless
 *   Enchanted, a minecraft mod.
 *   GNU GPLv3 License
 *
 *       This file is part of Enchanted.
 *
 *       Enchanted is free software: you can redistribute it and/or modify
 *       it under the terms of the GNU General Public License as published by
 *       the Free Software Foundation, either version 3 of the License, or
 *       (at your option) any later version.
 *
 *       Enchanted is distributed in the hope that it will be useful,
 *       but WITHOUT ANY WARRANTY; without even the implied warranty of
 *       MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *       GNU General Public License for more details.
 *
 *       You should have received a copy of the GNU General Public License
 *       along with Enchanted.  If not, see <https://www.gnu.org/licenses/>.
 *
 *
 */

package com.favouriteless.enchanted.common.reloadlisteners.altar;

import com.favouriteless.enchanted.Enchanted;
import com.favouriteless.enchanted.common.altar.AltarPowerProvider;
import com.favouriteless.enchanted.common.init.registry.PowerProviderRegistry;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.profiling.ProfilerFiller;

import java.util.Map;

public class PowerProviderReloadListener<T> extends SimpleJsonResourceReloadListener {

    private final KeySupplier<T> keySupplier;
    private final PowerProviderRegistry<T> registry;

    public PowerProviderReloadListener(String directory, KeySupplier<T> keySupplier, PowerProviderRegistry<T> registry) {
        super(new Gson(), directory);
        this.keySupplier = keySupplier;
        this.registry = registry;
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> jsonMap, ResourceManager manager, ProfilerFiller profiler) {
        registry.reset();
        jsonMap.forEach((resourceLocation, jsonElement) -> {
            try {
                JsonObject jsonObject = GsonHelper.convertToJsonObject(jsonElement, "powerprovider");

                T key = keySupplier.get(resourceLocation);
                int power = GsonHelper.getAsInt(jsonObject, "power");
                int limit = GsonHelper.getAsInt(jsonObject, "limit");

                if(key != null) {
                    if (power != 0 && limit > 0) {
                        AltarPowerProvider<T> provider = new AltarPowerProvider<>(key, power, limit);
                        registry.register(provider);
                    }
                }
            } catch (IllegalArgumentException | JsonParseException jsonparseexception) {
                Enchanted.LOGGER.error("Parsing error loading altar power provider {}: {}", resourceLocation, jsonparseexception.getMessage());
            }

        });
    }

    @FunctionalInterface
    public interface KeySupplier<T> {
        T get(ResourceLocation resourceLocation);
    }

}
