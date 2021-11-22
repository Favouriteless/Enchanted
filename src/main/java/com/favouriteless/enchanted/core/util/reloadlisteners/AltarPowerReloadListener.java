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
import com.favouriteless.enchanted.common.tileentity.altar.AltarTileEntity.*;
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
        CompletableFuture<List<AltarPowerProvider<Block>>> futureBlocks = prepareBlocks(pResourceManager, pBackgroundExecutor);
        CompletableFuture<List<AltarPowerProvider<IOptionalNamedTag<Block>>>> futureTags = prepareTags(pResourceManager, pBackgroundExecutor);
        CompletableFuture<List<AltarUpgrade>> futureUpgrades = prepareUpgrades(pResourceManager, pBackgroundExecutor);

        return CompletableFuture.allOf(futureBlocks, futureTags, futureUpgrades).thenCompose(pStage::wait).thenAcceptAsync((result) -> {
            POWER_BLOCKS = futureBlocks.join();
            POWER_TAGS = futureTags.join();
            UPGRADES = futureUpgrades.join();

            INSTANCE = this;
        }, pGameExecutor);
    }

    private CompletableFuture<List<AltarPowerProvider<Block>>> prepareBlocks(IResourceManager resourceManager, Executor executor) {
        return CompletableFuture.supplyAsync(() -> {
            List<AltarPowerProvider<Block>> blocks = new ArrayList<>();
            List<Block> removeList = new ArrayList<>();

            for(ResourceLocation resourceLocation : resourceManager.listResources(BLOCKS_DIRECTORY, (filter) -> filter.endsWith(".json"))) {
                String path = resourceLocation.getPath();
                ResourceLocation resourceLocation1 = new ResourceLocation(resourceLocation.getNamespace(), path.substring(BLOCKS_DIRECTORY.length() + 1, path.length() - PATH_SUFFIX_LENGTH));

                try {
                    for(IResource resource : resourceManager.getResources(resourceLocation)) {
                        try (InputStream inputstream = resource.getInputStream();
                             Reader reader = new BufferedReader(new InputStreamReader(inputstream, StandardCharsets.UTF_8))) {

                            JsonObject jsonObject = JSONUtils.fromJson(GSON, reader, JsonObject.class);

                            if (jsonObject == null) {
                                Enchanted.LOGGER.error("Couldn't load power {} from {} in data pack {} as it is empty or null", resourceLocation1, resourceLocation, resource.getSourceName());
                            } else {
                                Block block = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(JSONUtils.getAsString(jsonObject, "block")));
                                int power = JSONUtils.getAsInt(jsonObject, "power");
                                int limit = JSONUtils.getAsInt(jsonObject, "limit");
                                if(block != null) {
                                    if(power != 0 && limit > 0) {
                                        AltarPowerProvider<Block> newPowerProvider = new AltarPowerProvider<>(block, power, limit);
                                        if(blocks.isEmpty()) {
                                            blocks.add(newPowerProvider);
                                        }
                                        else {
                                            // Very crude sorting for priority
                                            for(int i = 0; i < blocks.size(); i++ ) {
                                                AltarPowerProvider<Block> powerProvider = blocks.get(i);
                                                if(newPowerProvider.getPower() > powerProvider.getPower()) {
                                                    blocks.add(i, newPowerProvider);
                                                    break;
                                                }
                                                if(newPowerProvider.getPower() == powerProvider.getPower()) {
                                                    if(newPowerProvider.getLimit() > powerProvider.getLimit()) {
                                                        blocks.add(i, newPowerProvider);
                                                        break;
                                                    }
                                                }
                                                if(i == blocks.size()-1) {
                                                    blocks.add(newPowerProvider);
                                                    break;
                                                }
                                            }
                                        }
                                    }
                                    else {
                                        removeList.add(block);
                                    }
                                }
                            }

                        } catch (RuntimeException | IOException ioexception) {
                            Enchanted.LOGGER.error("Couldn't read power {} from {} in data pack {}", resourceLocation1, resourceLocation, resource.getSourceName(), ioexception);
                        } finally {
                            IOUtils.closeQuietly(resource);
                        }
                    }
                } catch (IOException ioexception1) {
                    Enchanted.LOGGER.error("Couldn't read power {} from {}", resourceLocation1, resourceLocation, ioexception1);
                }
            }

            for(Block block : removeList) {
                blocks.removeIf(provider -> provider.is(block));
            }
            return blocks;
        }, executor);
    }

    private CompletableFuture<List<AltarPowerProvider<IOptionalNamedTag<Block>>>> prepareTags(IResourceManager resourceManager, Executor executor) {
        return CompletableFuture.supplyAsync(() -> {
            List<AltarPowerProvider<IOptionalNamedTag<Block>>> tags = new ArrayList<>();
            List<IOptionalNamedTag<Block>> removeList = new ArrayList<>();

            for(ResourceLocation resourceLocation : resourceManager.listResources(TAGS_DIRECTORY, (filter) -> filter.endsWith(".json"))) {
                String path = resourceLocation.getPath();
                ResourceLocation resourceLocation1 = new ResourceLocation(resourceLocation.getNamespace(), path.substring(TAGS_DIRECTORY.length() + 1, path.length() - PATH_SUFFIX_LENGTH));

                try {
                    for(IResource resource : resourceManager.getResources(resourceLocation)) {
                        try (InputStream inputstream = resource.getInputStream();
                             Reader reader = new BufferedReader(new InputStreamReader(inputstream, StandardCharsets.UTF_8))) {

                            JsonObject jsonObject = JSONUtils.fromJson(GSON, reader, JsonObject.class);

                            if (jsonObject == null) {
                                Enchanted.LOGGER.error("Couldn't load power {} from {} in data pack {} as it is empty or null", resourceLocation1, resourceLocation, resource.getSourceName());
                            } else {
                                IOptionalNamedTag<Block> tag = BlockTags.createOptional(new ResourceLocation(JSONUtils.getAsString(jsonObject, "tag")));
                                int power = JSONUtils.getAsInt(jsonObject, "power");
                                int limit = JSONUtils.getAsInt(jsonObject, "limit");

                                if(power != 0 && limit > 0) {
                                    AltarPowerProvider<IOptionalNamedTag<Block>> newPowerProvider = new AltarPowerProvider<>(tag, power, limit);
                                    if(tags.isEmpty()) {
                                        tags.add(newPowerProvider);
                                    }
                                    else {
                                        // Very crude sorting for priority
                                        for(int i = 0; i < tags.size(); i++ ) {
                                            AltarPowerProvider<IOptionalNamedTag<Block>> powerProvider = tags.get(i);
                                            if(newPowerProvider.getPower() > powerProvider.getPower()) {
                                                tags.add(i, newPowerProvider);
                                                break;
                                            }
                                            if(newPowerProvider.getPower() == powerProvider.getPower()) {
                                                if(newPowerProvider.getLimit() > powerProvider.getLimit()) {
                                                    tags.add(i, newPowerProvider);
                                                    break;
                                                }
                                            }
                                            if(i == tags.size()-1) {
                                                tags.add(newPowerProvider);
                                                break;
                                            }
                                        }
                                    }
                                }
                                else {
                                    removeList.add(tag);
                                }
                            }

                        } catch (RuntimeException | IOException ioexception) {
                            Enchanted.LOGGER.error("Couldn't read power {} from {} in data pack {}", resourceLocation1, resourceLocation, resource.getSourceName(), ioexception);
                        } finally {
                            IOUtils.closeQuietly(resource);
                        }
                    }
                } catch (IOException ioexception1) {
                    Enchanted.LOGGER.error("Couldn't read power {} from {}", resourceLocation1, resourceLocation, ioexception1);
                }
            }

            for(IOptionalNamedTag<Block> tag : removeList) {
                tags.removeIf(provider -> provider.is(tag));
            }
            return tags;
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
