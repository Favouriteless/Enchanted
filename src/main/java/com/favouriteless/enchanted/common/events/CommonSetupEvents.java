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

package com.favouriteless.enchanted.common.events;

import com.favouriteless.enchanted.Enchanted;
import com.favouriteless.enchanted.common.entities.EntEntity;
import com.favouriteless.enchanted.common.entities.MandrakeEntity;
import com.favouriteless.enchanted.common.init.registry.EnchantedBlocks;
import com.favouriteless.enchanted.common.init.registry.EnchantedEntityTypes;
import com.favouriteless.enchanted.common.init.EnchantedItems;
import com.favouriteless.enchanted.common.init.registry.EnchantedRecipeTypes;
import com.favouriteless.enchanted.common.rites.CirclePart;
import com.favouriteless.enchanted.jei.EnchantedJEITextures;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@EventBusSubscriber(modid=Enchanted.MOD_ID, bus=Bus.MOD)
public class CommonSetupEvents {

    @SubscribeEvent
    public static void commonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(EnchantedRecipeTypes::init);

        EnchantedJEITextures.registerCirclePartPrefix(CirclePart.SMALL, "small");
        EnchantedJEITextures.registerCirclePartPrefix(CirclePart.MEDIUM, "medium");
        EnchantedJEITextures.registerCirclePartPrefix(CirclePart.LARGE, "large");
        EnchantedJEITextures.registerBlockSuffix(EnchantedBlocks.CHALK_WHITE.get(), "white");
        EnchantedJEITextures.registerBlockSuffix(EnchantedBlocks.CHALK_RED.get(), "red");
        EnchantedJEITextures.registerBlockSuffix(EnchantedBlocks.CHALK_PURPLE.get(), "purple");

        EnchantedItems.registerCompostables();
    }

    @SubscribeEvent
    public static void registerEntityAttributes(EntityAttributeCreationEvent event) {
        event.put(EnchantedEntityTypes.MANDRAKE.get(), MandrakeEntity.createAttributes().build());
        event.put(EnchantedEntityTypes.ENT.get(), EntEntity.createAttributes().build());
    }

}
