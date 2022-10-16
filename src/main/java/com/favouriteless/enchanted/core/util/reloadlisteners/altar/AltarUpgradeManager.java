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
import com.favouriteless.enchanted.common.tileentity.AltarTileEntity.AltarUpgrade;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.world.level.block.Block;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.GsonHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class AltarUpgradeManager extends SimpleJsonResourceReloadListener implements Supplier<List<AltarUpgrade>> {

    private static final Gson GSON = new Gson();
    private List<AltarUpgrade> upgrades = new ArrayList<>();

    public AltarUpgradeManager(String directory) {
        super(GSON, directory);
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> jsonMap, ResourceManager resourceManager, ProfilerFiller profiler) {
        List<AltarUpgrade> outputList = new ArrayList<>();

        jsonMap.forEach((resourceLocation, jsonElement) -> {
            try {
                JsonObject jsonObject = GsonHelper.convertToJsonObject(jsonElement, "altarupgrade");

                for(JsonElement element : GsonHelper.getAsJsonArray(jsonObject, "values")) {
                    if(element.isJsonObject()) {
                        JsonObject upgradeObject = (JsonObject)element;

                        Block block = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(GsonHelper.getAsString(upgradeObject, "block")));
                        float rechargeMultiplier = GsonHelper.getAsFloat(upgradeObject, "rechargeMultiplier");
                        float powerMultiplier = GsonHelper.getAsFloat(upgradeObject, "powerMultiplier");
                        int priority = GsonHelper.getAsInt(upgradeObject, "priority");

                        if(block != null) {
                            if(powerMultiplier > 0 || rechargeMultiplier > 0) {
                                outputList.add(new AltarUpgrade(resourceLocation, block, rechargeMultiplier, powerMultiplier, priority));
                            }
                        }
                    }
                }
            } catch (IllegalArgumentException | JsonParseException jsonparseexception) {
                Enchanted.LOGGER.error("Parsing error loading altar upgrade {}: {}", resourceLocation, jsonparseexception.getMessage());
            }
        });
        this.upgrades = outputList;
    }
    public List<AltarUpgrade> get() {
        return upgrades;
    }
}
