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

package com.favouriteless.enchanted.common.init;

import com.favouriteless.enchanted.Enchanted;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;

public class EnchantedTags {

    public static class Blocks {
        public static final TagKey<Block> BLIGHT_DECAYABLE_BLOCKS = createBlockTag(Enchanted.location("blight_decayable_blocks"));
        public static final TagKey<Block> BLIGHT_DECAYABLE_PLANTS = createBlockTag(Enchanted.location("blight_decayable_plants"));
        public static final TagKey<Block> BLIGHT_DECAY_BLOCKS = createBlockTag(Enchanted.location("blight_decay_blocks"));
        public static final TagKey<Block> CHALICES = createBlockTag(Enchanted.location("chalices"));
        public static final TagKey<Block> CHALKS = createBlockTag(Enchanted.location("chalks"));
        public static final TagKey<Block> CROPS = createBlockTag(Enchanted.location("crops"));
        public static final TagKey<Block> LEAVES = createBlockTag(Enchanted.location("leaves"));
        public static final TagKey<Block> LOGS = createBlockTag(Enchanted.location("logs"));
        public static final TagKey<Block> MUTANDIS_BLACKLIST = createBlockTag(Enchanted.location("mutandis_blacklist_plants"));
        public static final TagKey<Block> MUTANDIS_EXTREMIS_PLANTS = createBlockTag(Enchanted.location("mutandis_extremis_plants"));
        public static final TagKey<Block> MUTANDIS_PLANTS = createBlockTag(Enchanted.location("mutandis_plants"));
        public static final TagKey<Block> PLANKS = createBlockTag(Enchanted.location("planks"));
        public static final TagKey<Block> RITE_FOREST_REPLACEABLE = createBlockTag(Enchanted.location("rite_forest_replaceable"));
        public static final TagKey<Block> SAPLINGS = createBlockTag(Enchanted.location("saplings"));
        public static final TagKey<Block> SLABS = createBlockTag(Enchanted.location("slabs"));
        public static final TagKey<Block> STAIRS = createBlockTag(Enchanted.location("stairs"));
        public static final TagKey<Block> WOODEN_SLABS = createBlockTag(Enchanted.location("wooden_slabs"));
        public static final TagKey<Block> WOODEN_STAIRS = createBlockTag(Enchanted.location("wooden_stairs"));
    }

    public static class Items {
        public static final TagKey<Item> ARMOUR_POPPET_BLACKLIST = createItemTag(Enchanted.location("armour_poppet_blacklist"));
        public static final TagKey<Item> ARMOUR_POPPET_WHITELIST = createItemTag(Enchanted.location("armour_poppet_whitelist"));
        public static final TagKey<Item> CHALICES = createItemTag(Enchanted.location("chalices"));
        public static final TagKey<Item> CHALKS = createItemTag(Enchanted.location("chalks"));
        public static final TagKey<Item> LEAVES = createItemTag(Enchanted.location("leaves"));
        public static final TagKey<Item> LOGS = createItemTag(Enchanted.location("logs"));
        public static final TagKey<Item> PLANKS = createItemTag(Enchanted.location("planks"));
        public static final TagKey<Item> SAPLINGS = createItemTag(Enchanted.location("saplings"));
        public static final TagKey<Item> SEEDS_DROPS = createItemTag(Enchanted.location("seeds_drops"));
        public static final TagKey<Item> STAIRS = createItemTag(Enchanted.location("stairs"));
        public static final TagKey<Item> TOOL_POPPET_BLACKLIST = createItemTag(Enchanted.location("tool_poppet_blacklist"));
        public static final TagKey<Item> TOOL_POPPET_WHITELIST = createItemTag(Enchanted.location("tool_poppet_whitelist"));
        public static final TagKey<Item> WITCH_OVEN_BLACKLIST = createItemTag(Enchanted.location("witch_oven_blacklist"));
        public static final TagKey<Item> WOODEN_SLABS = createItemTag(Enchanted.location("wooden_slabs"));
        public static final TagKey<Item> WOODEN_STAIRS = createItemTag(Enchanted.location("wooden_stairs"));
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
        public static final TagKey<EntityType<?>> TAGLOCK_BLACKLIST = createEntityTag(Enchanted.location("taglock_blacklist"));
    }



    private static <T> TagKey<T> createTag(ResourceKey<? extends Registry<T>> registry, ResourceLocation name) {
        return TagKey.create(registry, name);
    }

    public static TagKey<Item> createItemTag(ResourceLocation name) {
        return createTag(Registry.ITEM_REGISTRY, name);
    }

    public static TagKey<Block> createBlockTag(ResourceLocation name) {
        return createTag(Registry.BLOCK_REGISTRY, name);
    }

    public static TagKey<EntityType<?>> createEntityTag(ResourceLocation name) {
        return createTag(Registry.ENTITY_TYPE_REGISTRY, name);
    }

    public static TagKey<Biome> createBiomeTag(ResourceLocation name) {
        return createTag(Registry.BIOME_REGISTRY, name);
    }

    public static TagKey<MobEffect> createEffectTag(ResourceLocation name) {
        return createTag(Registry.MOB_EFFECT_REGISTRY, name);
    }

}
