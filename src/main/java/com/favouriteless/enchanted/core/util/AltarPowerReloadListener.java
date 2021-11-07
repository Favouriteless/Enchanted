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

package com.favouriteless.enchanted.core.util;

import com.favouriteless.enchanted.Enchanted;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import net.minecraft.profiler.IProfiler;
import net.minecraft.resources.IFutureReloadListener;
import net.minecraft.resources.IResource;
import net.minecraft.resources.IResourceManager;
import net.minecraft.block.Block;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags.IOptionalNamedTag;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.commons.io.IOUtils;

import java.awt.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class AltarPowerReloadListener implements IFutureReloadListener {

    public static volatile AltarPowerReloadListener INSTANCE;

    private static final String BLOCKS_DIRECTORY = "altar/blocks";
    private static final String TAGS_DIRECTORY = "altar/tags";
    private static final int PATH_SUFFIX_LENGTH = "json".length();
    private static final Gson GSON = new Gson();

    public HashMap<Block, Point> BLOCKS = new HashMap<>();
    public HashMap<IOptionalNamedTag<Block>, Point> TAGS = new HashMap<>();

    @Override
    public CompletableFuture<Void> reload(IStage pStage, IResourceManager pResourceManager, IProfiler pPreparationsProfiler, IProfiler pReloadProfiler, Executor pBackgroundExecutor, Executor pGameExecutor) {
        CompletableFuture<HashMap<Block, Point>> futureBlocks = prepareBlocks(pResourceManager, pBackgroundExecutor);
        CompletableFuture<HashMap<IOptionalNamedTag<Block>, Point>> futureTags = prepareTags(pResourceManager, pBackgroundExecutor);

        return CompletableFuture.allOf(futureBlocks, futureTags).thenCompose(pStage::wait).thenAcceptAsync((result) -> {
            BLOCKS = futureBlocks.join();
            TAGS = futureTags.join();

            INSTANCE = this;
        }, pGameExecutor);
    }

    public CompletableFuture<HashMap<Block, Point>> prepareBlocks(IResourceManager resourceManager, Executor executor) {
        return CompletableFuture.supplyAsync(() -> {
            HashMap<Block, Point> blocksMap = new HashMap<>();
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
                                        blocksMap.put(block, new Point(power, limit));
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
                blocksMap.remove(block);
            }
            return blocksMap;
        }, executor);
    }

    public CompletableFuture<HashMap<IOptionalNamedTag<Block>, Point>> prepareTags(IResourceManager resourceManager, Executor executor) {
        return CompletableFuture.supplyAsync(() -> {
            HashMap<IOptionalNamedTag<Block>, Point> tagsMap = new HashMap<>();
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
                                    tagsMap.put(tag, new Point(power, limit));
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
                tagsMap.remove(tag);
            }
            return tagsMap;
        }, executor);
    }


}
