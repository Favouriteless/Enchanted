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

package com.favouriteless.enchanted.core.util.reloadlisteners;

import com.favouriteless.enchanted.Enchanted;
import com.favouriteless.enchanted.common.tileentity.AltarTileEntity.*;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.block.Block;
import net.minecraft.profiler.IProfiler;
import net.minecraft.resources.IFutureReloadListener;
import net.minecraft.resources.IResource;
import net.minecraft.resources.IResourceManager;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags.IOptionalNamedTag;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class AltarPowerReloadListener implements IFutureReloadListener {

    public static volatile AltarPowerReloadListener INSTANCE;

    private static final String BLOCKS_DIRECTORY = "altar/blocks";
    private static final String TAGS_DIRECTORY = "altar/tags";
    private static final String UPGRADES_DIRECTORY = "altar/upgrades";

    private static final int PATH_SUFFIX_LENGTH = ".json".length();
    private static final Gson GSON = new Gson();

    public List<AltarPowerProvider<Block>> POWER_BLOCKS;
    public List<AltarPowerProvider<IOptionalNamedTag<Block>>> POWER_TAGS;
    public List<AltarUpgrade> UPGRADES = new ArrayList<>();


    public AltarPowerProvider<Block> providerOf(Block block) {
        for(AltarPowerProvider<Block> provider : POWER_BLOCKS) {
            if(provider.is(block)) return provider;
        }
        return null;
    }

    public AltarPowerProvider<IOptionalNamedTag<Block>> providerOf(IOptionalNamedTag<Block> tag) {
        for(AltarPowerProvider<IOptionalNamedTag<Block>> provider : POWER_TAGS) {
            if(provider.is(tag)) return provider;
        }
        return null;
    }

    @Override
    public CompletableFuture<Void> reload(IStage pStage, IResourceManager pResourceManager, IProfiler pPreparationsProfiler, IProfiler pReloadProfiler, Executor pBackgroundExecutor, Executor pGameExecutor) {
        CompletableFuture<List<AltarPowerProvider<Block>>> futureBlocks = prepare(BLOCKS_DIRECTORY, pResourceManager, pBackgroundExecutor, ForgeRegistries.BLOCKS::getValue);
        CompletableFuture<List<AltarPowerProvider<IOptionalNamedTag<Block>>>> futureTags = prepare(TAGS_DIRECTORY, pResourceManager, pBackgroundExecutor, BlockTags::createOptional);
        CompletableFuture<List<AltarUpgrade>> futureUpgrades = prepareUpgrades(pResourceManager, pBackgroundExecutor);

        return CompletableFuture.allOf(futureBlocks, futureTags, futureUpgrades).thenCompose(pStage::wait).thenAcceptAsync((result) -> {
            POWER_BLOCKS = futureBlocks.join();
            POWER_TAGS = futureTags.join();
            UPGRADES = futureUpgrades.join();

            INSTANCE = this;
        }, pGameExecutor);
    }

    @FunctionalInterface
    private interface KeySupplier<T> {
        T get(ResourceLocation resourceLocation);
    }

    private <T> CompletableFuture<List<AltarPowerProvider<T>>> prepare(String directory, IResourceManager resourceManager, Executor executor, KeySupplier<T> keySupplier) {
        return CompletableFuture.supplyAsync(() -> {
            List<AltarPowerProvider<T>> outputList = new ArrayList<>();
            List<T> removeList = new ArrayList<>();

            for(ResourceLocation resourceLocation : resourceManager.listResources(directory, (filter) ->
                    filter.endsWith(".json")))
            {
                String path = resourceLocation.getPath();
                ResourceLocation resourceLocation1 = new ResourceLocation(resourceLocation.getNamespace(), path.substring(directory.length() + 1, path.length() - PATH_SUFFIX_LENGTH));

                try (
                        IResource resource = resourceManager.getResource(resourceLocation);
                        InputStream inputstream = resource.getInputStream();
                        Reader reader = new BufferedReader(new InputStreamReader(inputstream, StandardCharsets.UTF_8));
                ) {
                    JsonObject jsonObject = JSONUtils.fromJson(GSON, reader, JsonObject.class);

                    if (jsonObject == null) {
                        Enchanted.LOGGER.error("Couldn't load power {} from {} in data pack {} as it is empty or null", resourceLocation1, resourceLocation, resource.getSourceName());
                    } else {
                        T key = keySupplier.get(resourceLocation1);
                        int power = JSONUtils.getAsInt(jsonObject, "power");
                        int limit = JSONUtils.getAsInt(jsonObject, "limit");

                        if(key != null) {
                            if(power != 0 && limit > 0) {
                                AltarPowerProvider<T> newPowerProvider = new AltarPowerProvider<>(key, power, limit);
                                if(outputList.isEmpty()) {
                                    outputList.add(newPowerProvider);
                                }
                                else {
                                    // Very crude sorting for priority
                                    for(int i = 0; i < outputList.size(); i++ ) {
                                        AltarPowerProvider<T> powerProvider = outputList.get(i);
                                        if(newPowerProvider.getPower() > powerProvider.getPower()) {
                                            outputList.add(i, newPowerProvider);
                                            break;
                                        }
                                        if(newPowerProvider.getPower() == powerProvider.getPower()) {
                                            if(newPowerProvider.getLimit() > powerProvider.getLimit()) {
                                                outputList.add(i, newPowerProvider);
                                                break;
                                            }
                                        }
                                        if(i == outputList.size()-1) {
                                            outputList.add(newPowerProvider);
                                            break;
                                        }
                                    }
                                }
                            }
                            else {
                                removeList.add(key);
                            }
                        }
                    }
                } catch (RuntimeException | IOException ioexception) {
                    Enchanted.LOGGER.error("Couldn't read power {} from {} in data pack {}", resourceLocation1, resourceLocation, ioexception);
                }
            }

            for(T key : removeList) {
                outputList.removeIf(provider -> provider.is(key));
            }
            return outputList;
        }, executor);
    }

    private CompletableFuture<List<AltarUpgrade>> prepareUpgrades(IResourceManager resourceManager, Executor executor) {
        return CompletableFuture.supplyAsync(() -> {

            List<AltarUpgrade> upgradesList = new ArrayList<>();

            for(ResourceLocation resourceLocation : resourceManager.listResources(UPGRADES_DIRECTORY, (filter) -> filter.endsWith(".json"))) {
                String path = resourceLocation.getPath();
                ResourceLocation resourceLocation1 = new ResourceLocation(resourceLocation.getNamespace(), path.substring(UPGRADES_DIRECTORY.length() + 1, path.length() - PATH_SUFFIX_LENGTH));

                try {
                    for(IResource resource : resourceManager.getResources(resourceLocation)) {
                        try (InputStream inputstream = resource.getInputStream();
                             Reader reader = new BufferedReader(new InputStreamReader(inputstream, StandardCharsets.UTF_8))) {
                            JsonObject jsonObject = JSONUtils.fromJson(GSON, reader, JsonObject.class);

                            if (jsonObject == null) {
                                Enchanted.LOGGER.error("Couldn't load upgrade {} from {} in data pack {} as it is empty or null", resourceLocation1, resourceLocation, resource.getSourceName());
                            } else {
                                for(JsonElement element : JSONUtils.getAsJsonArray(jsonObject, "values")) {
                                    if(element.isJsonObject()) {
                                        JsonObject upgradeObject = (JsonObject)element;

                                        Block block = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(JSONUtils.getAsString(upgradeObject, "block")));
                                        float rechargeMultiplier = JSONUtils.getAsFloat(upgradeObject, "rechargeMultiplier");
                                        float powerMultiplier = JSONUtils.getAsFloat(upgradeObject, "powerMultiplier");
                                        int priority = JSONUtils.getAsInt(upgradeObject, "priority");

                                        if(block != null) {
                                            if(powerMultiplier > 0 || rechargeMultiplier > 0) {
                                                upgradesList.add(new AltarUpgrade(resourceLocation1, block, rechargeMultiplier, powerMultiplier, priority));
                                            }
                                        }
                                    }
                                }
                            }

                        } catch (RuntimeException | IOException ioexception) {
                            Enchanted.LOGGER.error("Couldn't read upgrade {} from {} in data pack {}", resourceLocation1, resourceLocation, resource.getSourceName(), ioexception);
                        } finally {
                            IOUtils.closeQuietly(resource);
                        }
                    }
                } catch (IOException ioexception1) {
                    Enchanted.LOGGER.error("Couldn't read upgrade {} from {}", resourceLocation1, resourceLocation, ioexception1);
                }
            }

            return upgradesList;
        }, executor);
    }

    public static class AltarPowerProvider<T> {

        private final T key;
        private final int power;
        private final int limit;

        public AltarPowerProvider(T key, int power, int limit) {
            this.key = key;
            this.power = power;
            this.limit = limit;
        }

        public boolean sameKey(AltarPowerProvider<?> powerProvider) {
            return powerProvider.key.equals(this.key);
        }

        public boolean is(T key) {
            return this.key == key;
        }

        public T getKey() {
            return this.key;
        }

        public int getPower() {
            return this.power;
        }

        public int getLimit() {
            return this.limit;
        }
    }
}
