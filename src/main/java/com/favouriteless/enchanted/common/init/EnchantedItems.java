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

package com.favouriteless.enchanted.common.init;

import com.favouriteless.enchanted.Enchanted;
import com.favouriteless.enchanted.common.items.*;
import com.favouriteless.enchanted.common.items.brews.SimpleEffectBrewItem;
import com.favouriteless.enchanted.common.items.poppets.*;
import net.minecraft.block.Block;
import net.minecraft.item.*;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public class EnchantedItems {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Enchanted.MOD_ID);

    public static final RegistryObject<Item> ALTAR = ITEMS.register("altar", simpleBlockItem(EnchantedBlocks.ALTAR));
    public static final RegistryObject<Item> WITCH_OVEN = ITEMS.register("witch_oven", simpleBlockItem(EnchantedBlocks.WITCH_OVEN));
    public static final RegistryObject<Item> FUME_FUNNEL = ITEMS.register("fume_funnel", simpleBlockItem(EnchantedBlocks.FUME_FUNNEL));
    public static final RegistryObject<Item> FUME_FUNNEL_FILTERED = ITEMS.register("fume_funnel_filtered", simpleBlockItem(EnchantedBlocks.FUME_FUNNEL_FILTERED));
    public static final RegistryObject<Item> DISTILLERY = ITEMS.register("distillery", simpleBlockItem(EnchantedBlocks.DISTILLERY));
    public static final RegistryObject<Item> WITCH_CAULDRON = ITEMS.register("witch_cauldron", simpleBlockItem(EnchantedBlocks.WITCH_CAULDRON));
    public static final RegistryObject<Item> KETTLE = ITEMS.register("kettle", simpleBlockItem(EnchantedBlocks.KETTLE));

    public static final RegistryObject<Item> CHALICE = ITEMS.register("chalice", simpleBlockItem(EnchantedBlocks.CHALICE));
    public static final RegistryObject<Item> CHALICE_FILLED = ITEMS.register("chalice_filled", simpleBlockItem(EnchantedBlocks.CHALICE_FILLED));
    public static final RegistryObject<Item> CHALICE_FILLED_MILK = ITEMS.register("chalice_filled_milk", simpleBlockItem(EnchantedBlocks.CHALICE_FILLED_MILK));
    public static final RegistryObject<Item> CANDELABRA = ITEMS.register("candelabra", simpleBlockItem(EnchantedBlocks.CANDELABRA));

    public static final RegistryObject<Item> ROWAN_LOG = ITEMS.register("rowan_log", simpleBlockItem(EnchantedBlocks.ROWAN_LOG));
    public static final RegistryObject<Item> ROWAN_PLANKS = ITEMS.register("rowan_planks", simpleBlockItem(EnchantedBlocks.ROWAN_PLANKS));
    public static final RegistryObject<Item> ROWAN_STAIRS = ITEMS.register("rowan_stairs", simpleBlockItem(EnchantedBlocks.ROWAN_STAIRS));
    public static final RegistryObject<Item> ROWAN_SLAB = ITEMS.register("rowan_slab", simpleBlockItem(EnchantedBlocks.ROWAN_SLAB));
    public static final RegistryObject<Item> ROWAN_LEAVES = ITEMS.register("rowan_leaves", simpleBlockItem(EnchantedBlocks.ROWAN_LEAVES));
    public static final RegistryObject<Item> ROWAN_SAPLING = ITEMS.register("rowan_sapling", simpleBlockItem(EnchantedBlocks.ROWAN_SAPLING));
    public static final RegistryObject<Item> HAWTHORN_LOG = ITEMS.register("hawthorn_log", simpleBlockItem(EnchantedBlocks.HAWTHORN_LOG));
    public static final RegistryObject<Item> HAWTHORN_PLANKS = ITEMS.register("hawthorn_planks", simpleBlockItem(EnchantedBlocks.HAWTHORN_PLANKS));
    public static final RegistryObject<Item> HAWTHORN_STAIRS = ITEMS.register("hawthorn_stairs", simpleBlockItem(EnchantedBlocks.HAWTHORN_STAIRS));
    public static final RegistryObject<Item> HAWTHORN_SLAB = ITEMS.register("hawthorn_slab", simpleBlockItem(EnchantedBlocks.HAWTHORN_SLAB));
    public static final RegistryObject<Item> HAWTHORN_LEAVES = ITEMS.register("hawthorn_leaves", simpleBlockItem(EnchantedBlocks.HAWTHORN_LEAVES));
    public static final RegistryObject<Item> HAWTHORN_SAPLING = ITEMS.register("hawthorn_sapling", simpleBlockItem(EnchantedBlocks.HAWTHORN_SAPLING));
    public static final RegistryObject<Item> ALDER_LOG = ITEMS.register("alder_log", simpleBlockItem(EnchantedBlocks.ALDER_LOG));
    public static final RegistryObject<Item> ALDER_PLANKS = ITEMS.register("alder_planks", simpleBlockItem(EnchantedBlocks.ALDER_PLANKS));
    public static final RegistryObject<Item> ALDER_STAIRS = ITEMS.register("alder_stairs", simpleBlockItem(EnchantedBlocks.ALDER_STAIRS));
    public static final RegistryObject<Item> ALDER_SLAB = ITEMS.register("alder_slab", simpleBlockItem(EnchantedBlocks.ALDER_SLAB));
    public static final RegistryObject<Item> ALDER_LEAVES = ITEMS.register("alder_leaves", simpleBlockItem(EnchantedBlocks.ALDER_LEAVES));
    public static final RegistryObject<Item> ALDER_SAPLING = ITEMS.register("alder_sapling", simpleBlockItem(EnchantedBlocks.ALDER_SAPLING));

    public static final RegistryObject<Item> ARTICHOKE_SEEDS = ITEMS.register("artichoke_seeds", () -> new ArtichokeSeedsItem(EnchantedBlocks.ARTICHOKE.get(), simpleProperties()));
    public static final RegistryObject<Item> BELLADONNA_SEEDS = ITEMS.register("belladonna_seeds", simpleBlockNamedItem(EnchantedBlocks.BELLADONNA));
    public static final RegistryObject<Item> MANDRAKE_SEEDS = ITEMS.register("mandrake_seeds", simpleBlockNamedItem(EnchantedBlocks.MANDRAKE));
    public static final RegistryObject<Item> MANDRAKE_ROOT = ITEMS.register("mandrake_root", simpleItem());
    public static final RegistryObject<Item> SNOWBELL_SEEDS = ITEMS.register("snowbell_seeds", simpleBlockNamedItem(EnchantedBlocks.SNOWBELL));
    public static final RegistryObject<Item> WOLFSBANE_SEEDS = ITEMS.register("wolfsbane_seeds", simpleBlockNamedItem(EnchantedBlocks.WOLFSBANE));

    public static final RegistryObject<Item> ARTICHOKE = ITEMS.register("artichoke", simpleItem());
    public static final RegistryObject<Item> BELLADONNA_FLOWER = ITEMS.register("belladonna_flower", simpleItem());
    public static final RegistryObject<Item> EMBER_MOSS = ITEMS.register("ember_moss", simpleBlockItem(EnchantedBlocks.EMBER_MOSS));
    public static final RegistryObject<Item> GARLIC = ITEMS.register("garlic", simpleBlockNamedItem(EnchantedBlocks.GARLIC));
    public static final RegistryObject<Item> GLINT_WEED = ITEMS.register("glint_weed", simpleBlockItem(EnchantedBlocks.GLINT_WEED));
    public static final RegistryObject<Item> ICY_NEEDLE = ITEMS.register("icy_needle", simpleItem());
    public static final RegistryObject<Item> SPANISH_MOSS = ITEMS.register("spanish_moss", simpleBlockItem(EnchantedBlocks.SPANISH_MOSS));
    public static final RegistryObject<Item> WOLFSBANE_FLOWER = ITEMS.register("wolfsbane_flower", simpleItem());
    public static final RegistryObject<Item> BLOOD_POPPY = ITEMS.register("blood_poppy", simpleBlockItem(EnchantedBlocks.BLOOD_POPPY));

    public static final RegistryObject<Item> ANOINTING_PASTE = ITEMS.register("anointing_paste", () -> new AnointingPasteItem(simpleProperties()));
    public static final RegistryObject<Item> MUTANDIS = ITEMS.register("mutandis", () -> new MutandisItem(EnchantedTags.MUTANDIS_PLANTS, simpleProperties()));
    public static final RegistryObject<Item> MUTANDIS_EXTREMIS = ITEMS.register("mutandis_extremis", () -> new MutandisItem(EnchantedTags.MUTANDIS_EXTREMIS_PLANTS, simpleProperties()));
    public static final RegistryObject<Item> TAGLOCK = ITEMS.register("taglock", () -> new TaglockItem(simpleProperties()));
    public static final RegistryObject<Item> TAGLOCK_FILLED = ITEMS.register("taglock_filled", () -> new TaglockItemFilled(new Item.Properties()));

    public static final RegistryObject<Item> CHALK_GOLD = ITEMS.register("chalk_gold", () -> new ChalkItem(EnchantedBlocks.CHALK_GOLD.get(), simpleProperties().stacksTo(1).durability(3)));
    public static final RegistryObject<Item> CHALK_WHITE = ITEMS.register("chalk_white", () -> new ChalkItem(EnchantedBlocks.CHALK_WHITE.get(), simpleProperties().stacksTo(1).durability(40)));
    public static final RegistryObject<Item> CHALK_RED = ITEMS.register("chalk_red", () -> new ChalkItem(EnchantedBlocks.CHALK_RED.get(), simpleProperties().stacksTo(1).durability(40)));
    public static final RegistryObject<Item> CHALK_PURPLE = ITEMS.register("chalk_purple", () -> new ChalkItem(EnchantedBlocks.CHALK_PURPLE.get(), simpleProperties().stacksTo(1).durability(40)));
    public static final RegistryObject<Item> ARTHANA = ITEMS.register("arthana", () -> new SwordItem(ItemTier.GOLD, 3, -2.4F, simpleProperties()));
    public static final RegistryObject<Item> EARMUFFS = ITEMS.register("earmuffs", () -> new EarmuffsItem(simpleProperties()));

    public static final RegistryObject<Item> CIRCLE_TALISMAN = ITEMS.register("circle_talisman", () -> new CircleTalismanItem(simpleProperties()));
    public static final RegistryObject<Item> WAYSTONE = ITEMS.register("waystone", simpleItem());
    public static final RegistryObject<Item> BOUND_WAYSTONE = ITEMS.register("bound_waystone", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> BROOM = ITEMS.register("broom", () -> new BroomItem(simpleProperties()));
    public static final RegistryObject<Item> ENCHANTED_BROOMSTICK = ITEMS.register("enchanted_broomstick", () -> new BroomstickItem(simpleProperties()));

    public static final RegistryObject<Item> DEMON_HEART = ITEMS.register("demon_heart",  simpleItem());

    public static final RegistryObject<Item> CLAY_JAR_SOFT = ITEMS.register("clay_jar_soft", simpleItem());
    public static final RegistryObject<Item> CLAY_JAR = ITEMS.register("clay_jar", simpleItem());
    public static final RegistryObject<Item> BREATH_OF_THE_GODDESS = ITEMS.register("breath_of_the_goddess", simpleItem());
    public static final RegistryObject<Item> EXHALE_OF_THE_HORNED_ONE = ITEMS.register("exhale_of_the_horned_one", simpleItem());
    public static final RegistryObject<Item> FOUL_FUME = ITEMS.register("foul_fume", simpleItem());
    public static final RegistryObject<Item> HINT_OF_REBIRTH = ITEMS.register("hint_of_rebirth", simpleItem());
    public static final RegistryObject<Item> TEAR_OF_THE_GODDESS = ITEMS.register("tear_of_the_goddess", simpleItem());
    public static final RegistryObject<Item> WHIFF_OF_MAGIC = ITEMS.register("whiff_of_magic", simpleItem());
    public static final RegistryObject<Item> CONDENSED_FEAR = ITEMS.register("condensed_fear", simpleItem());
    public static final RegistryObject<Item> DIAMOND_VAPOUR = ITEMS.register("diamond_vapour", simpleItem());
    public static final RegistryObject<Item> DROP_OF_LUCK = ITEMS.register("drop_of_luck", simpleItem());
    public static final RegistryObject<Item> ENDER_DEW = ITEMS.register("ender_dew", simpleItem());
    public static final RegistryObject<Item> FOCUSED_WILL = ITEMS.register("focused_will", simpleItem());
    public static final RegistryObject<Item> DEMONIC_BLOOD = ITEMS.register("demonic_blood", simpleItem());
    public static final RegistryObject<Item> MELLIFLUOUS_HUNGER = ITEMS.register("mellifluous_hunger", simpleItem());
    public static final RegistryObject<Item> ODOUR_OF_PURITY = ITEMS.register("odour_of_purity", simpleItem());
    public static final RegistryObject<Item> OIL_OF_VITRIOL = ITEMS.register("oil_of_vitriol", simpleItem());
    public static final RegistryObject<Item> PURIFIED_MILK = ITEMS.register("purified_milk", simpleItem());
    public static final RegistryObject<Item> REEK_OF_MISFORTUNE = ITEMS.register("reek_of_misfortune", simpleItem());
    public static final RegistryObject<Item> ATTUNED_STONE = ITEMS.register("attuned_stone", simpleItem());
    public static final RegistryObject<Item> ATTUNED_STONE_CHARGED = ITEMS.register("attuned_stone_charged", () -> new SimpleFoiledItem(simpleProperties().stacksTo(1)));
    public static final RegistryObject<Item> GYPSUM = ITEMS.register("gypsum", simpleItem());
    public static final RegistryObject<Item> QUICKLIME = ITEMS.register("quicklime", simpleItem());
    public static final RegistryObject<Item> REFINED_EVIL = ITEMS.register("refined_evil", simpleItem());
    public static final RegistryObject<Item> ROWAN_BERRIES = ITEMS.register("rowan_berries", () -> new Item(simpleProperties().food(new Food.Builder().nutrition(3).build())));
    public static final RegistryObject<Item> WOOD_ASH = ITEMS.register("wood_ash", simpleItem());
    public static final RegistryObject<Item> FUME_FILTER = ITEMS.register("fume_filter", simpleItem());
    public static final RegistryObject<Item> WOOL_OF_BAT = ITEMS.register("wool_of_bat", simpleItem());
    public static final RegistryObject<Item> TONGUE_OF_DOG = ITEMS.register("tongue_of_dog", simpleItem());
    public static final RegistryObject<Item> CREEPER_HEART = ITEMS.register("creeper_heart", simpleItem());
    public static final RegistryObject<Item> BONE_NEEDLE = ITEMS.register("bone_needle", simpleItem());

    public static final RegistryObject<Item> REDSTONE_SOUP = ITEMS.register("redstone_soup", () -> new SimpleEffectBrewItem(Effects.ABSORPTION, 2400, 1, simpleProperties()));
    public static final RegistryObject<Item> FLYING_OINTMENT = ITEMS.register("flying_ointment", () -> new SimpleEffectBrewItem(Effects.LEVITATION, 400, 0, simpleProperties()));
    public static final RegistryObject<Item> MYSTIC_UNGUENT = ITEMS.register("mystic_unguent", () -> new SimpleEffectBrewItem(Effects.WEAKNESS, 1200, 1, simpleProperties()));
    public static final RegistryObject<Item> HAPPENSTANCE_OIL = ITEMS.register("happenstance_oil", () -> new SimpleEffectBrewItem(Effects.NIGHT_VISION, 1200, 0, simpleProperties()));

    public static final RegistryObject<Item> GHOST_OF_THE_LIGHT = ITEMS.register("ghost_of_the_light", () -> new SimpleEffectBrewItem(Effects.POISON, 1200, 1, simpleProperties()));
    public static final RegistryObject<Item> SOUL_OF_THE_WORLD = ITEMS.register("soul_of_the_world", () -> new SimpleEffectBrewItem(Effects.POISON, 1200, 1, simpleProperties()));
    public static final RegistryObject<Item> SPIRIT_OF_OTHERWHERE = ITEMS.register("spirit_of_otherwhere", () -> new SimpleEffectBrewItem(Effects.POISON, 1200, 1, simpleProperties()));
    public static final RegistryObject<Item> INFERNAL_ANIMUS = ITEMS.register("infernal_animus", () -> new SimpleEffectBrewItem(Effects.WITHER, 1200, 2, simpleProperties()));

    public static final RegistryObject<Item> POPPET = ITEMS.register("poppet", simpleItem());
    public static final RegistryObject<Item> EARTH_POPPET = ITEMS.register("earth_poppet", () -> new DeathPoppetItem(0.3F, 1, source -> source == DamageSource.FALL || source == DamageSource.FLY_INTO_WALL));
    public static final RegistryObject<Item> EARTH_POPPET_INFUSED = ITEMS.register("earth_poppet_infused", () -> new DeathPoppetInfusedItem(() -> new EffectInstance(EnchantedEffects.FALL_RESISTANCE.get(), 100, 0), source -> source == DamageSource.FALL || source == DamageSource.FLY_INTO_WALL));
    public static final RegistryObject<Item> EARTH_POPPET_STURDY = ITEMS.register("earth_poppet_sturdy", () -> new DeathPoppetItem(0.0F, 2, source -> source == DamageSource.FALL || source == DamageSource.FLY_INTO_WALL));
    public static final RegistryObject<Item> FIRE_POPPET = ITEMS.register("fire_poppet", () -> new DeathPoppetItem(0.3F, 1, DamageSource::isFire));
    public static final RegistryObject<Item> FIRE_POPPET_INFUSED = ITEMS.register("fire_poppet_infused", () -> new DeathPoppetInfusedItem(() -> new EffectInstance(Effects.FIRE_RESISTANCE), DamageSource::isFire));
    public static final RegistryObject<Item> FIRE_POPPET_STURDY = ITEMS.register("fire_poppet_sturdy", () -> new DeathPoppetItem(0.0F, 2, DamageSource::isFire));
    public static final RegistryObject<Item> WATER_POPPET = ITEMS.register("water_poppet", () -> new DeathPoppetItem(0.3F, 1, source -> source == DamageSource.DROWN));
    public static final RegistryObject<Item> WATER_POPPET_INFUSED = ITEMS.register("water_poppet_infused", () -> new DeathPoppetInfusedItem(() -> new EffectInstance(EnchantedEffects.DROWN_RESISTANCE.get(), 100, 0), source -> source == DamageSource.DROWN));
    public static final RegistryObject<Item> WATER_POPPET_STURDY = ITEMS.register("water_poppet_sturdy", () -> new DeathPoppetItem(0.0F, 2, source -> source == DamageSource.DROWN));
    public static final RegistryObject<Item> HUNGER_POPPET = ITEMS.register("hunger_poppet", () -> new DeathPoppetItem(0.3F, 1, source -> source == DamageSource.STARVE));
    public static final RegistryObject<Item> HUNGER_POPPET_INFUSED = ITEMS.register("hunger_poppet_infused", () -> new DeathPoppetInfusedItem(() -> new EffectInstance(Effects.SATURATION, 100, 0), source -> source == DamageSource.STARVE));
    public static final RegistryObject<Item> HUNGER_POPPET_STURDY = ITEMS.register("hunger_poppet_sturdy", () -> new DeathPoppetItem(0.0F, 2, source -> source == DamageSource.STARVE));
    public static final RegistryObject<Item> VOID_POPPET = ITEMS.register("void_poppet", () -> new VoidPoppetItem(0.3F, 1, source -> source == DamageSource.OUT_OF_WORLD));
    public static final RegistryObject<Item> VOID_POPPET_INFUSED = ITEMS.register("void_poppet_infused", () -> new VoidPoppetInfusedItem(() -> new EffectInstance(EnchantedEffects.FALL_RESISTANCE.get(), 100, 0), source -> source == DamageSource.OUT_OF_WORLD));
    public static final RegistryObject<Item> VOID_POPPET_STURDY = ITEMS.register("void_poppet_sturdy", () -> new VoidPoppetItem(0.0F, 2, source -> source == DamageSource.OUT_OF_WORLD));
    public static final RegistryObject<Item> TOOL_POPPET = ITEMS.register("tool_poppet", () -> new ToolPoppetItem(0.3F, 1, 0.9F));
    public static final RegistryObject<Item> TOOL_POPPET_INFUSED = ITEMS.register("tool_poppet_infused", () -> new ToolPoppetItem(0.0F, 1, 0.0F));
    public static final RegistryObject<Item> TOOL_POPPET_STURDY = ITEMS.register("tool_poppet_sturdy", () -> new ToolPoppetItem(0.0F, 2, 0.9F));
    
    public static Supplier<Item> simpleItem() {
        return () -> new Item(simpleProperties());
    }

    private static Item.Properties simpleProperties() {
        return new Item.Properties().tab(Enchanted.TAB);
    }

    private static Supplier<BlockItem> simpleBlockItem(Supplier<? extends Block> block) {
        return () -> new BlockItem(block.get(), simpleProperties());
    }

    private static Supplier<BlockNamedItem> simpleBlockNamedItem(Supplier<? extends Block> block) {
        return () -> new BlockNamedItem(block.get(), simpleProperties());
    }

}
