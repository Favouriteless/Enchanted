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

package com.favouriteless.enchanted.core.init;

import com.favouriteless.enchanted.Enchanted;
import com.favouriteless.enchanted.common.rituals.*;
import com.favouriteless.enchanted.core.init.registries.RitualRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;

import java.util.*;

public class EnchantedRituals {

    public static final DeferredRegister<AbstractRitual> RITUAL_TYPES = DeferredRegister.create(AbstractRitual.class, Enchanted.MOD_ID);

    public static final RegistryObject<AbstractRitual> RITE_OF_CHARGING_STONE = RITUAL_TYPES.register("rite_of_charging_stone", RiteOfChargingStone::new);
    public static final RegistryObject<AbstractRitual> RITE_OF_TOTAL_ECLIPSE = RITUAL_TYPES.register("rite_of_total_eclipse", RiteOfTotalEclipse::new);

    public static List<AbstractRitual> ACTIVE_RITUALS = new ArrayList<>();
    public static HashMap<Block, String> CHARACTER_MAP = new HashMap<>();

    public static void init() { // Adds ritual types to map
        CHARACTER_MAP.put(EnchantedBlocks.CHALK_GOLD.get(), "G");
        CHARACTER_MAP.put(EnchantedBlocks.CHALK_WHITE.get(), "W");
        CHARACTER_MAP.put(EnchantedBlocks.CHALK_RED.get(), "R");
        CHARACTER_MAP.put(EnchantedBlocks.CHALK_PURPLE.get(), "P");
        CHARACTER_MAP.put(Blocks.AIR, "A");
    }

    public static CompoundNBT getRitualsTag() {
        CompoundNBT tag = new CompoundNBT();
        int counter = 0;

        for(AbstractRitual ritual : ACTIVE_RITUALS) {
            if(ritual.isExecutingEffect) {
                tag.put(Integer.toString(counter), ritual.getTag());
                counter++;
            }
        }
        return tag;
    }

    public static List<AbstractRitual> forData(String[] glyphs, List<Entity> entities) {
        List<ItemEntity> itemEntities = new ArrayList<>();

        for(Entity entity : entities) {
            if(entity instanceof ItemEntity) {
                itemEntities.add((ItemEntity)entity);
            }
        }

        return null;
    }

    public static void loadRitualsTag(CompoundNBT tag, ServerWorld world) {
        for(String key : tag.getAllKeys()) {
            CompoundNBT ritualTag = tag.getCompound(key);

            AbstractRitual ritual = RitualRegistry.INSTANCE.getValue(new ResourceLocation(Enchanted.MOD_ID, tag.getString("name")));
            ritual.setData(
                    new BlockPos(tag.getDouble("xPos"), tag.getDouble("yPos"), tag.getDouble("zPos")),
                    tag.getUUID("casterUUID"),
                    tag.getUUID("targetUUID"),
                    world.getServer().getLevel(RegistryKey.create(Registry.DIMENSION_REGISTRY, new ResourceLocation(tag.getString("dimensionKey"))))
                    );

            ACTIVE_RITUALS.add(ritual);
        }
    }

}
