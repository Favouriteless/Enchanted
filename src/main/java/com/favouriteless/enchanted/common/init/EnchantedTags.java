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
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;

public class EnchantedTags {

    public static class Blocks {
        public static final TagKey<Block> BLIGHT_DECAYABLE_BLOCKS = BlockTags.create(Enchanted.location("blight_decayable_blocks"));
        public static final TagKey<Block> BLIGHT_DECAYABLE_PLANTS = BlockTags.create(Enchanted.location("blight_decayable_plants"));
        public static final TagKey<Block> BLIGHT_DECAY_BLOCKS = BlockTags.create(Enchanted.location("blight_decay_blocks"));
        public static final TagKey<Block> CHALICES = BlockTags.create(Enchanted.location("chalices"));
        public static final TagKey<Block> CHALKS = BlockTags.create(Enchanted.location("chalks"));
        public static final TagKey<Block> CROPS = BlockTags.create(Enchanted.location("crops"));
        public static final TagKey<Block> LEAVES = BlockTags.create(Enchanted.location("leaves"));
        public static final TagKey<Block> LOGS = BlockTags.create(Enchanted.location("logs"));
        public static final TagKey<Block> MUTANDIS_BLACKLIST = BlockTags.create(Enchanted.location("mutandis_blacklist_plants"));
        public static final TagKey<Block> MUTANDIS_EXTREMIS_PLANTS = BlockTags.create(Enchanted.location("mutandis_extremis_plants"));
        public static final TagKey<Block> MUTANDIS_PLANTS = BlockTags.create(Enchanted.location("mutandis_plants"));
        public static final TagKey<Block> PLANKS = BlockTags.create(Enchanted.location("planks"));
        public static final TagKey<Block> POWER_CONSUMERS = BlockTags.create(Enchanted.location("power_consumers"));
        public static final TagKey<Block> RITE_FOREST_REPLACEABLE = BlockTags.create(Enchanted.location("rite_forest_replaceable"));
        public static final TagKey<Block> SAPLINGS = BlockTags.create(Enchanted.location("saplings"));
        public static final TagKey<Block> SLABS = BlockTags.create(Enchanted.location("slabs"));
        public static final TagKey<Block> STAIRS = BlockTags.create(Enchanted.location("stairs"));
        public static final TagKey<Block> WOODEN_SLABS = BlockTags.create(Enchanted.location("wooden_slabs"));
        public static final TagKey<Block> WOODEN_STAIRS = BlockTags.create(Enchanted.location("wooden_stairs"));
    }

    public static class Items {
        public static final TagKey<Item> ARMOUR_POPPET_BLACKLIST = ItemTags.create(Enchanted.location("armour_poppet_blacklist"));
        public static final TagKey<Item> ARMOUR_POPPET_WHITELIST = ItemTags.create(Enchanted.location("armour_poppet_whitelist"));
        public static final TagKey<Item> AXES = ItemTags.create(Enchanted.location("axes"));
        public static final TagKey<Item> CHALICES = ItemTags.create(Enchanted.location("chalices"));
        public static final TagKey<Item> CHALKS = ItemTags.create(Enchanted.location("chalks"));
        public static final TagKey<Item> HOES = ItemTags.create(Enchanted.location("hoes"));
        public static final TagKey<Item> LEAVES = ItemTags.create(Enchanted.location("leaves"));
        public static final TagKey<Item> LOGS = ItemTags.create(Enchanted.location("logs"));
        public static final TagKey<Item> PLANKS = ItemTags.create(Enchanted.location("planks"));
        public static final TagKey<Item> RAW_FOODS = ItemTags.create(Enchanted.location("raw_foods"));
        public static final TagKey<Item> SAPLINGS = ItemTags.create(Enchanted.location("saplings"));
        public static final TagKey<Item> STAIRS = ItemTags.create(Enchanted.location("stairs"));
        public static final TagKey<Item> TOOL_POPPET_BLACKLIST = ItemTags.create(Enchanted.location("tool_poppet_blacklist"));
        public static final TagKey<Item> TOOL_POPPET_WHITELIST = ItemTags.create(Enchanted.location("tool_poppet_whitelist"));
        public static final TagKey<Item> WOODEN_SLABS = ItemTags.create(Enchanted.location("wooden_slabs"));
        public static final TagKey<Item> WOODEN_STAIRS = ItemTags.create(Enchanted.location("wooden_stairs"));
    }

    public static class MobEffects {
        public static final TagKey<MobEffect> MISFORTUNE_EFFECTS = createEffectTag(Enchanted.location("misfortune_effects"));
        public static final TagKey<MobEffect> BLIGHT_EFFECTS = createEffectTag(Enchanted.location("blight_effects"));
        public static final TagKey<MobEffect> FERTILITY_CURE_EFFECTS = createEffectTag(Enchanted.location("fertility_cure_effects"));
    }

    public static class Biomes {
        public static final TagKey<Biome> OVERHEATING_BIOMES = createBiomeTag(Enchanted.location("overheating_biomes"));
    }

    public static class EntityTypes {
        public static final TagKey<EntityType<?>> MONSTERS = createEntityTag(Enchanted.location("monsters"));
    }



    private static TagKey<EntityType<?>> createEntityTag(ResourceLocation name) {
        return TagKey.create(Registry.ENTITY_TYPE_REGISTRY, name);
    }

    private static TagKey<Biome> createBiomeTag(ResourceLocation name) {
        return TagKey.create(Registry.BIOME_REGISTRY, name);
    }

    private static TagKey<MobEffect> createEffectTag(ResourceLocation name) {
        return TagKey.create(Registry.MOB_EFFECT_REGISTRY, name);
    }

}
