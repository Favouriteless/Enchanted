/*
 * Copyright (c) 2022. Favouriteless
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

package com.favouriteless.enchanted.core.util.reloadlisteners.altar;

import com.favouriteless.enchanted.Enchanted;
import com.favouriteless.enchanted.api.altar.AltarPowerProvider;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.client.resources.JsonReloadListener;
import net.minecraft.profiler.IProfiler;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class AltarPowerProviderManager<T> extends JsonReloadListener implements Supplier<List<AltarPowerProvider<T>>> {

    private static final Gson GSON = new Gson();
    private List<AltarPowerProvider<T>> powerProviders = new ArrayList<>();
    private final KeySupplier<T> keySupplier;

    public AltarPowerProviderManager(String directory, KeySupplier<T> keySupplier) {
        super(GSON, directory);
        this.keySupplier = keySupplier;
    }

    public AltarPowerProvider<T> providerOf(T key) {
        for(AltarPowerProvider<T> provider : powerProviders) {
            if(provider.is(key)) return provider;
        }
        return null;
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> jsonMap, IResourceManager resourceManager, IProfiler profiler) {
        List<AltarPowerProvider<T>> outputList = new ArrayList<>();
        List<T> removeList = new ArrayList<>();

        jsonMap.forEach((resourceLocation, jsonElement) -> {
            try {
                JsonObject jsonObject = JSONUtils.convertToJsonObject(jsonElement, "powerprovider");

                T key = keySupplier.get(resourceLocation);
                int power = JSONUtils.getAsInt(jsonObject, "power");
                int limit = JSONUtils.getAsInt(jsonObject, "limit");

                if(key != null) {
                    if (power != 0 && limit > 0) {
                        AltarPowerProvider<T> newPowerProvider = new AltarPowerProvider<>(key, power, limit);
                        if (outputList.isEmpty()) {
                            outputList.add(newPowerProvider);
                        } else {
                            // Very crude sorting for priority
                            for (int i = 0; i < outputList.size(); i++) {
                                AltarPowerProvider<T> powerProvider = outputList.get(i);
                                if (newPowerProvider.getPower() > powerProvider.getPower()) {
                                    outputList.add(i, newPowerProvider);
                                    break;
                                }
                                if (newPowerProvider.getPower() == powerProvider.getPower()) {
                                    if (newPowerProvider.getLimit() > powerProvider.getLimit()) {
                                        outputList.add(i, newPowerProvider);
                                        break;
                                    }
                                }
                                if (i == outputList.size() - 1) {
                                    outputList.add(newPowerProvider);
                                    break;
                                }
                            }
                        }
                    } else {
                        removeList.add(key);
                    }
                }
            } catch (IllegalArgumentException | JsonParseException jsonparseexception) {
                Enchanted.LOGGER.error("Parsing error loading altar power provider {}: {}", resourceLocation, jsonparseexception.getMessage());
            }

        });

        for(T key : removeList) {
            outputList.removeIf(provider -> provider.is(key));
        }
        this.powerProviders = outputList;
    }

    public List<AltarPowerProvider<T>> get() {
        return powerProviders;
    }

    @FunctionalInterface
    public interface KeySupplier<T> {
        T get(ResourceLocation resourceLocation);
    }
}
