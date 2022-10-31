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
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class EnchantedTags {

    public static final TagKey<Block> POWER_CONSUMERS = BlockTags.create(new ResourceLocation(Enchanted.MOD_ID, "power_consumers"));
    public static final TagKey<Block> MUTANDIS_PLANTS = BlockTags.create(new ResourceLocation(Enchanted.MOD_ID, "mutandis_plants"));
    public static final TagKey<Block> MUTANDIS_EXTREMIS_PLANTS = BlockTags.create(new ResourceLocation(Enchanted.MOD_ID, "mutandis_extremis_plants"));
    public static final TagKey<Block> MUTANDIS_BLACKLIST = BlockTags.create(new ResourceLocation(Enchanted.MOD_ID, "mutandis_blacklist_plants"));
    public static final TagKey<Block> CHALKS = BlockTags.create(new ResourceLocation(Enchanted.MOD_ID, "chalks"));

    public static final TagKey<EntityType<?>> MONSTERS = createEntityTag(new ResourceLocation(Enchanted.MOD_ID, "monsters"));

    public static final TagKey<Item> TOOL_POPPET_WHITELIST = ItemTags.create(new ResourceLocation(Enchanted.MOD_ID, "tool_poppet_whitelist"));
    public static final TagKey<Item> ARMOUR_POPPET_WHITELIST = ItemTags.create(new ResourceLocation(Enchanted.MOD_ID, "armour_poppet_whitelist"));
    public static final TagKey<Item> TOOL_POPPET_BLACKLIST = ItemTags.create(new ResourceLocation(Enchanted.MOD_ID, "tool_poppet_blacklist"));
    public static final TagKey<Item> ARMOUR_POPPET_BLACKLIST = ItemTags.create(new ResourceLocation(Enchanted.MOD_ID, "armour_poppet_blacklist"));

    private static TagKey<EntityType<?>> createEntityTag(ResourceLocation name) {
        return TagKey.create(Registry.ENTITY_TYPE_REGISTRY, name);
    }

}
