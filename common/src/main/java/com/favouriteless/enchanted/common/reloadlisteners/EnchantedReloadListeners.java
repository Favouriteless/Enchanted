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

package com.favouriteless.enchanted.common.reloadlisteners;

import com.favouriteless.enchanted.Enchanted;
import com.favouriteless.enchanted.common.init.EnchantedData;
import com.favouriteless.enchanted.common.loot.ArthanaLootReloadListener;
import com.favouriteless.enchanted.common.reloadlisteners.altar.PowerProviderReloadListener;
import com.favouriteless.enchanted.common.reloadlisteners.altar.AltarUpgradeReloadListener;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(modid=Enchanted.MOD_ID, bus=Bus.FORGE)
public class EnchantedReloadListeners {

    @SubscribeEvent
    public static void addReloadListenerEvent(AddReloadListenerEvent event) {
        event.addListener(new PowerProviderReloadListener<>("altar/blocks", EnchantedReloadListeners::createBlockKey, EnchantedData.POWER_BLOCKS));
        event.addListener(new PowerProviderReloadListener<>("altar/tags", BlockTags::create, EnchantedData.POWER_TAGS));
        event.addListener(new AltarUpgradeReloadListener());
        event.addListener(new ArthanaLootReloadListener());
    }

    private static Block createBlockKey(ResourceLocation key) {
        Block block = ForgeRegistries.BLOCKS.getValue(key);
        if(block != Blocks.AIR)
            return block;
        else
            return null;
    }

}
