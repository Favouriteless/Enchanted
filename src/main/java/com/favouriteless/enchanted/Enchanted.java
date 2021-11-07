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

package com.favouriteless.enchanted;

import com.favouriteless.enchanted.common.init.*;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(Enchanted.MOD_ID)
public class Enchanted
{
    public static final Logger LOGGER = LogManager.getLogger();
    public static final String MOD_ID = "enchanted";
    public static Enchanted instance;

    public Enchanted() {
        registerAll();

        instance = this;
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void registerAll() {

        final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        EnchantedRecipeTypes.init();
        EnchantedRecipeTypes.RECIPE_SERIALIZERS.register(modEventBus);
        EnchantedEntityTypes.ENTITY_TYPES.register(modEventBus);
        EnchantedBlocks.BLOCKS.register(modEventBus);
        EnchantedItems.ITEMS.register(modEventBus);
        EnchantedTileEntities.TILE_ENTITY_TYPES.register(modEventBus);
        EnchantedContainers.CONTAINER_TYPES.register(modEventBus);
    }


}
