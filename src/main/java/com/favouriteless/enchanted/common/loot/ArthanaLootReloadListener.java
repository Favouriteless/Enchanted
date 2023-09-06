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

package com.favouriteless.enchanted.common.loot;

import com.favouriteless.enchanted.Enchanted;
import com.favouriteless.enchanted.common.init.EnchantedData;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Map;

public class ArthanaLootReloadListener extends SimpleJsonResourceReloadListener {

    public ArthanaLootReloadListener() {
        super(new Gson(), "arthana");
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> jsonMap, ResourceManager manager, ProfilerFiller profiler) {
        EnchantedData.ARTHANA_LOOT.reset();
        jsonMap.forEach((resourceLocation, jsonElement) -> {
            try {
                ItemStack stack = CraftingHelper.getItemStack(jsonElement.getAsJsonObject().getAsJsonObject("result"), true);
                EntityType<?> type = ForgeRegistries.ENTITIES.getValue(resourceLocation);

                if(type != null && !stack.isEmpty())
                    EnchantedData.ARTHANA_LOOT.register(type, stack);
            } catch (IllegalArgumentException | JsonParseException jsonparseexception) {
                Enchanted.LOGGER.error("Parsing error loading altar upgrade {}: {}", resourceLocation, jsonparseexception.getMessage());
            }
        });
    }

}
