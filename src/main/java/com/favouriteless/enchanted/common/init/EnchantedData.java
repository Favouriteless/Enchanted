/*
 *
 *   Copyright (c) 2022. Favouriteless
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

package com.favouriteless.enchanted.common.init;

import com.favouriteless.enchanted.Enchanted;
import com.favouriteless.enchanted.core.util.reloadlisteners.ArthanaLootManager;
import com.favouriteless.enchanted.core.util.reloadlisteners.altar.AltarPowerProviderManager;
import com.favouriteless.enchanted.core.util.reloadlisteners.altar.AltarUpgradeManager;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.tags.BlockTags;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(modid=Enchanted.MOD_ID, bus=Mod.EventBusSubscriber.Bus.FORGE)
public class EnchantedData {

    public static final AltarPowerProviderManager<Block> ALTAR_POWER_BLOCKS = new AltarPowerProviderManager<>("altar/blocks", ForgeRegistries.BLOCKS::getValue);
    public static final AltarPowerProviderManager<TagKey<Block>> ALTAR_POWER_TAGS = new AltarPowerProviderManager<>("altar/tags", BlockTags::create);
    public static final AltarUpgradeManager ALTAR_UPGRADES = new AltarUpgradeManager("altar/upgrades");
    public static final ArthanaLootManager ARTHANA_LOOT = new ArthanaLootManager("arthana");

    @SubscribeEvent
    public static void addReloadListenerEvent(AddReloadListenerEvent event) {
        event.addListener(ALTAR_POWER_BLOCKS);
        event.addListener(ALTAR_POWER_TAGS);
        event.addListener(ALTAR_UPGRADES);
        event.addListener(ARTHANA_LOOT);
    }

}
