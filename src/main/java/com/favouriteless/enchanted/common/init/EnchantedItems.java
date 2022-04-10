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
import com.favouriteless.enchanted.common.items.poppets.BasicPoppetItem;
import com.favouriteless.enchanted.common.items.poppets.HungerPoppetItem;
import com.favouriteless.enchanted.common.items.poppets.PoppetInfusedItem;
import net.minecraft.item.*;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class EnchantedItems {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Enchanted.MOD_ID);

    public static final RegistryObject<Item> ALTAR = ITEMS.register("altar", () -> new BlockItem(EnchantedBlocks.ALTAR.get(), new Item.Properties().tab(Enchanted.TAB)));
    public static final RegistryObject<Item> WITCH_OVEN = ITEMS.register("witch_oven", () -> new BlockItem(EnchantedBlocks.WITCH_OVEN.get(), new Item.Properties().tab(Enchanted.TAB)));
    public static final RegistryObject<Item> FUME_FUNNEL = ITEMS.register("fume_funnel", () -> new BlockItem(EnchantedBlocks.FUME_FUNNEL.get(), new Item.Properties().tab(Enchanted.TAB)));
    public static final RegistryObject<Item> FUME_FUNNEL_FILTERED = ITEMS.register("fume_funnel_filtered", () -> new BlockItem(EnchantedBlocks.FUME_FUNNEL_FILTERED.get(), new Item.Properties().tab(Enchanted.TAB)));
    public static final RegistryObject<Item> DISTILLERY = ITEMS.register("distillery", () -> new BlockItem(EnchantedBlocks.DISTILLERY.get(), new Item.Properties().tab(Enchanted.TAB)));
    public static final RegistryObject<Item> WITCH_CAULDRON = ITEMS.register("witch_cauldron", () -> new BlockItem(EnchantedBlocks.WITCH_CAULDRON.get(), new Item.Properties().tab(Enchanted.TAB)));
    public static final RegistryObject<Item> KETTLE = ITEMS.register("kettle", () -> new BlockItem(EnchantedBlocks.KETTLE.get(), new Item.Properties().tab(Enchanted.TAB)));

    public static final RegistryObject<Item> CHALICE = ITEMS.register("chalice", () -> new BlockItem(EnchantedBlocks.CHALICE.get(), new Item.Properties().tab(Enchanted.TAB)));
    public static final RegistryObject<Item> CHALICE_FILLED = ITEMS.register("chalice_filled", () -> new BlockItem(EnchantedBlocks.CHALICE_FILLED.get(), new Item.Properties().tab(Enchanted.TAB)));
    public static final RegistryObject<Item> CHALICE_FILLED_MILK = ITEMS.register("chalice_filled_milk", () -> new BlockItem(EnchantedBlocks.CHALICE_FILLED_MILK.get(), new Item.Properties().tab(Enchanted.TAB)));
    public static final RegistryObject<Item> CANDELABRA = ITEMS.register("candelabra", () -> new BlockItem(EnchantedBlocks.CANDELABRA.get(), new Item.Properties().tab(Enchanted.TAB)));


    public static final RegistryObject<Item> ROWAN_LOG = ITEMS.register("rowan_log", () -> new BlockItem(EnchantedBlocks.ROWAN_LOG.get(), new Item.Properties().tab(Enchanted.TAB)));
    public static final RegistryObject<Item> ROWAN_PLANKS = ITEMS.register("rowan_planks", () -> new BlockItem(EnchantedBlocks.ROWAN_PLANKS.get(), new Item.Properties().tab(Enchanted.TAB)));
    public static final RegistryObject<Item> ROWAN_STAIRS = ITEMS.register("rowan_stairs", () -> new BlockItem(EnchantedBlocks.ROWAN_STAIRS.get(), new Item.Properties().tab(Enchanted.TAB)));
    public static final RegistryObject<Item> ROWAN_SLAB = ITEMS.register("rowan_slab", () -> new BlockItem(EnchantedBlocks.ROWAN_SLAB.get(), new Item.Properties().tab(Enchanted.TAB)));
    public static final RegistryObject<Item> ROWAN_LEAVES = ITEMS.register("rowan_leaves", () -> new BlockItem(EnchantedBlocks.ROWAN_LEAVES.get(), new Item.Properties().tab(Enchanted.TAB)));
    public static final RegistryObject<Item> ROWAN_SAPLING = ITEMS.register("rowan_sapling", () -> new BlockItem(EnchantedBlocks.ROWAN_SAPLING.get(), new Item.Properties().tab(Enchanted.TAB)));
    public static final RegistryObject<Item> HAWTHORN_LOG = ITEMS.register("hawthorn_log", () -> new BlockItem(EnchantedBlocks.HAWTHORN_LOG.get(), new Item.Properties().tab(Enchanted.TAB)));
    public static final RegistryObject<Item> HAWTHORN_PLANKS = ITEMS.register("hawthorn_planks", () -> new BlockItem(EnchantedBlocks.HAWTHORN_PLANKS.get(), new Item.Properties().tab(Enchanted.TAB)));
    public static final RegistryObject<Item> HAWTHORN_STAIRS = ITEMS.register("hawthorn_stairs", () -> new BlockItem(EnchantedBlocks.HAWTHORN_STAIRS.get(), new Item.Properties().tab(Enchanted.TAB)));
    public static final RegistryObject<Item> HAWTHORN_SLAB = ITEMS.register("hawthorn_slab", () -> new BlockItem(EnchantedBlocks.HAWTHORN_SLAB.get(), new Item.Properties().tab(Enchanted.TAB)));
    public static final RegistryObject<Item> HAWTHORN_LEAVES = ITEMS.register("hawthorn_leaves", () -> new BlockItem(EnchantedBlocks.HAWTHORN_LEAVES.get(), new Item.Properties().tab(Enchanted.TAB)));
    public static final RegistryObject<Item> HAWTHORN_SAPLING = ITEMS.register("hawthorn_sapling", () -> new BlockItem(EnchantedBlocks.HAWTHORN_SAPLING.get(), new Item.Properties().tab(Enchanted.TAB)));
    public static final RegistryObject<Item> ALDER_LOG = ITEMS.register("alder_log", () -> new BlockItem(EnchantedBlocks.ALDER_LOG.get(), new Item.Properties().tab(Enchanted.TAB)));
    public static final RegistryObject<Item> ALDER_PLANKS = ITEMS.register("alder_planks", () -> new BlockItem(EnchantedBlocks.ALDER_PLANKS.get(), new Item.Properties().tab(Enchanted.TAB)));
    public static final RegistryObject<Item> ALDER_STAIRS = ITEMS.register("alder_stairs", () -> new BlockItem(EnchantedBlocks.ALDER_STAIRS.get(), new Item.Properties().tab(Enchanted.TAB)));
    public static final RegistryObject<Item> ALDER_SLAB = ITEMS.register("alder_slab", () -> new BlockItem(EnchantedBlocks.ALDER_SLAB.get(), new Item.Properties().tab(Enchanted.TAB)));
    public static final RegistryObject<Item> ALDER_LEAVES = ITEMS.register("alder_leaves", () -> new BlockItem(EnchantedBlocks.ALDER_LEAVES.get(), new Item.Properties().tab(Enchanted.TAB)));
    public static final RegistryObject<Item> ALDER_SAPLING = ITEMS.register("alder_sapling", () -> new BlockItem(EnchantedBlocks.ALDER_SAPLING.get(), new Item.Properties().tab(Enchanted.TAB)));



    public static final RegistryObject<Item> ARTICHOKE_SEEDS = ITEMS.register("artichoke_seeds", () -> new ArtichokeSeedsItem(EnchantedBlocks.ARTICHOKE.get(), new Item.Properties().tab(Enchanted.TAB)));
    public static final RegistryObject<Item> BELLADONNA_SEEDS = ITEMS.register("belladonna_seeds", () -> new BlockNamedItem(EnchantedBlocks.BELLADONNA.get(), new Item.Properties().tab(Enchanted.TAB)));
    public static final RegistryObject<Item> MANDRAKE_SEEDS = ITEMS.register("mandrake_seeds", () -> new BlockNamedItem(EnchantedBlocks.MANDRAKE.get(), new Item.Properties().tab(Enchanted.TAB)));
    public static final RegistryObject<Item> MANDRAKE_ROOT = ITEMS.register("mandrake_root", () -> new Item(new Item.Properties().tab(Enchanted.TAB)));
    public static final RegistryObject<Item> SNOWBELL_SEEDS = ITEMS.register("snowbell_seeds", () -> new BlockNamedItem(EnchantedBlocks.SNOWBELL.get(), new Item.Properties().tab(Enchanted.TAB)));
    public static final RegistryObject<Item> WOLFSBANE_SEEDS = ITEMS.register("wolfsbane_seeds", () -> new BlockNamedItem(EnchantedBlocks.WOLFSBANE.get(), new Item.Properties().tab(Enchanted.TAB)));

    public static final RegistryObject<Item> ARTICHOKE = ITEMS.register("artichoke", () -> new Item(new Item.Properties().tab(Enchanted.TAB)));
    public static final RegistryObject<Item> BELLADONNA_FLOWER = ITEMS.register("belladonna_flower", () -> new Item(new Item.Properties().tab(Enchanted.TAB)));
    public static final RegistryObject<Item> EMBER_MOSS = ITEMS.register("ember_moss", () -> new BlockItem(EnchantedBlocks.EMBER_MOSS.get(), new Item.Properties().tab(Enchanted.TAB)));
    public static final RegistryObject<Item> GARLIC = ITEMS.register("garlic", () -> new BlockNamedItem(EnchantedBlocks.GARLIC.get(), new Item.Properties().tab(Enchanted.TAB)));
    public static final RegistryObject<Item> GLINT_WEED = ITEMS.register("glint_weed", () -> new BlockItem(EnchantedBlocks.GLINT_WEED.get(), new Item.Properties().tab(Enchanted.TAB)));
    public static final RegistryObject<Item> ICY_NEEDLE = ITEMS.register("icy_needle", () -> new Item(new Item.Properties().tab(Enchanted.TAB)));
    public static final RegistryObject<Item> SPANISH_MOSS = ITEMS.register("spanish_moss", () -> new BlockItem(EnchantedBlocks.SPANISH_MOSS.get(), new Item.Properties().tab(Enchanted.TAB)));
    public static final RegistryObject<Item> WOLFSBANE_FLOWER = ITEMS.register("wolfsbane_flower", () -> new Item(new Item.Properties().tab(Enchanted.TAB)));
    public static final RegistryObject<Item> BLOOD_POPPY = ITEMS.register("blood_poppy", () -> new BlockItem(EnchantedBlocks.BLOOD_POPPY.get(), new Item.Properties().tab(Enchanted.TAB)));

    public static final RegistryObject<Item> ANOINTING_PASTE = ITEMS.register("anointing_paste", () -> new AnointingPasteItem(new Item.Properties().tab(Enchanted.TAB)));
    public static final RegistryObject<Item> MUTANDIS = ITEMS.register("mutandis", () -> new MutandisItem(new Item.Properties().tab(Enchanted.TAB), EnchantedTags.MUTANDIS_PLANTS));
    public static final RegistryObject<Item> MUTANDIS_EXTREMIS = ITEMS.register("mutandis_extremis", () -> new MutandisItem(new Item.Properties().tab(Enchanted.TAB), EnchantedTags.MUTANDIS_EXTREMIS_PLANTS));
    public static final RegistryObject<Item> TAGLOCK = ITEMS.register("taglock", () -> new TaglockItem(new Item.Properties().tab(Enchanted.TAB)));
    public static final RegistryObject<Item> TAGLOCK_FILLED = ITEMS.register("taglock_filled", () -> new TaglockItemFilled(new Item.Properties()));



    public static final RegistryObject<Item> CHALK_GOLD = ITEMS.register("chalk_gold", () -> new ChalkItem(new Item.Properties().tab(Enchanted.TAB).stacksTo(1).durability(3), EnchantedBlocks.CHALK_GOLD.get()));
    public static final RegistryObject<Item> CHALK_WHITE = ITEMS.register("chalk_white", () -> new ChalkItem(new Item.Properties().tab(Enchanted.TAB).stacksTo(1).durability(40), EnchantedBlocks.CHALK_WHITE.get()));
    public static final RegistryObject<Item> CHALK_RED = ITEMS.register("chalk_red", () -> new ChalkItem(new Item.Properties().tab(Enchanted.TAB).stacksTo(1).durability(40), EnchantedBlocks.CHALK_RED.get()));
    public static final RegistryObject<Item> CHALK_PURPLE = ITEMS.register("chalk_purple", () -> new ChalkItem(new Item.Properties().tab(Enchanted.TAB).stacksTo(1).durability(40), EnchantedBlocks.CHALK_PURPLE.get()));
    public static final RegistryObject<Item> ARTHANA = ITEMS.register("arthana", () -> new SwordItem(ItemTier.GOLD, 3, -2.4F, (new Item.Properties()).tab(Enchanted.TAB)));
    public static final RegistryObject<Item> EARMUFFS = ITEMS.register("earmuffs", () -> new EarmuffsItem(new Item.Properties().tab(Enchanted.TAB)));

    public static final RegistryObject<Item> CIRCLE_TALISMAN = ITEMS.register("circle_talisman", () -> new CircleTalismanItem(new Item.Properties().tab(Enchanted.TAB)));
    public static final RegistryObject<Item> WAYSTONE = ITEMS.register("waystone", () -> new Item(new Item.Properties().tab(Enchanted.TAB)));
    public static final RegistryObject<Item> BOUND_WAYSTONE = ITEMS.register("bound_waystone", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> BROOM = ITEMS.register("broom", () -> new BroomItem(new Item.Properties().tab(Enchanted.TAB)));
    public static final RegistryObject<Item> ENCHANTED_BROOMSTICK = ITEMS.register("enchanted_broomstick", () -> new BroomstickItem(new Item.Properties().tab(Enchanted.TAB)));



    public static final RegistryObject<Item> DEMON_HEART = ITEMS.register("demon_heart",  () -> new Item(new Item.Properties().tab(Enchanted.TAB)));

    public static final RegistryObject<Item> CLAY_JAR_SOFT = ITEMS.register("clay_jar_soft", () -> new Item(new Item.Properties().tab(Enchanted.TAB)));
    public static final RegistryObject<Item> CLAY_JAR = ITEMS.register("clay_jar", () -> new Item(new Item.Properties().tab(Enchanted.TAB)));
    public static final RegistryObject<Item> BREATH_OF_THE_GODDESS = ITEMS.register("breath_of_the_goddess", () -> new Item(new Item.Properties().tab(Enchanted.TAB)));
    public static final RegistryObject<Item> EXHALE_OF_THE_HORNED_ONE = ITEMS.register("exhale_of_the_horned_one", () -> new Item(new Item.Properties().tab(Enchanted.TAB)));
    public static final RegistryObject<Item> FOUL_FUME = ITEMS.register("foul_fume", () -> new Item(new Item.Properties().tab(Enchanted.TAB)));
    public static final RegistryObject<Item> HINT_OF_REBIRTH = ITEMS.register("hint_of_rebirth", () -> new Item(new Item.Properties().tab(Enchanted.TAB)));
    public static final RegistryObject<Item> TEAR_OF_THE_GODDESS = ITEMS.register("tear_of_the_goddess", () -> new Item(new Item.Properties().tab(Enchanted.TAB)));
    public static final RegistryObject<Item> WHIFF_OF_MAGIC = ITEMS.register("whiff_of_magic", () -> new Item(new Item.Properties().tab(Enchanted.TAB)));
    public static final RegistryObject<Item> CONDENSED_FEAR = ITEMS.register("condensed_fear", () -> new Item(new Item.Properties().tab(Enchanted.TAB)));
    public static final RegistryObject<Item> DIAMOND_VAPOUR = ITEMS.register("diamond_vapour", () -> new Item(new Item.Properties().tab(Enchanted.TAB)));
    public static final RegistryObject<Item> DROP_OF_LUCK = ITEMS.register("drop_of_luck", () -> new Item(new Item.Properties().tab(Enchanted.TAB)));
    public static final RegistryObject<Item> ENDER_DEW = ITEMS.register("ender_dew", () -> new Item(new Item.Properties().tab(Enchanted.TAB)));
    public static final RegistryObject<Item> FOCUSED_WILL = ITEMS.register("focused_will", () -> new Item(new Item.Properties().tab(Enchanted.TAB)));
    public static final RegistryObject<Item> DEMONIC_BLOOD = ITEMS.register("demonic_blood", () -> new Item(new Item.Properties().tab(Enchanted.TAB)));
    public static final RegistryObject<Item> MELLIFLUOUS_HUNGER = ITEMS.register("mellifluous_hunger", () -> new Item(new Item.Properties().tab(Enchanted.TAB)));
    public static final RegistryObject<Item> ODOUR_OF_PURITY = ITEMS.register("odour_of_purity", () -> new Item(new Item.Properties().tab(Enchanted.TAB)));
    public static final RegistryObject<Item> OIL_OF_VITRIOL = ITEMS.register("oil_of_vitriol", () -> new Item(new Item.Properties().tab(Enchanted.TAB)));
    public static final RegistryObject<Item> PURIFIED_MILK = ITEMS.register("purified_milk", () -> new Item(new Item.Properties().tab(Enchanted.TAB)));
    public static final RegistryObject<Item> REEK_OF_MISFORTUNE = ITEMS.register("reek_of_misfortune", () -> new Item(new Item.Properties().tab(Enchanted.TAB)));
    public static final RegistryObject<Item> ATTUNED_STONE = ITEMS.register("attuned_stone", () -> new Item(new Item.Properties().tab(Enchanted.TAB)));
    public static final RegistryObject<Item> ATTUNED_STONE_CHARGED = ITEMS.register("attuned_stone_charged", () -> new SimpleFoiledItem(new Item.Properties().stacksTo(1).tab(Enchanted.TAB)));
    public static final RegistryObject<Item> GYPSUM = ITEMS.register("gypsum", () -> new Item(new Item.Properties().tab(Enchanted.TAB)));
    public static final RegistryObject<Item> QUICKLIME = ITEMS.register("quicklime", () -> new Item(new Item.Properties().tab(Enchanted.TAB)));
    public static final RegistryObject<Item> REFINED_EVIL = ITEMS.register("refined_evil", () -> new Item(new Item.Properties().tab(Enchanted.TAB)));
    public static final RegistryObject<Item> ROWAN_BERRIES = ITEMS.register("rowan_berries", () -> new Item((new Item.Properties()).tab(Enchanted.TAB).food(new Food.Builder().nutrition(3).build())));
    public static final RegistryObject<Item> WOOD_ASH = ITEMS.register("wood_ash", () -> new Item(new Item.Properties().tab(Enchanted.TAB)));
    public static final RegistryObject<Item> FUME_FILTER = ITEMS.register("fume_filter", () -> new Item(new Item.Properties().tab(Enchanted.TAB)));
    public static final RegistryObject<Item> WOOL_OF_BAT = ITEMS.register("wool_of_bat", () -> new Item(new Item.Properties().tab(Enchanted.TAB)));
    public static final RegistryObject<Item> TONGUE_OF_DOG = ITEMS.register("tongue_of_dog", () -> new Item(new Item.Properties().tab(Enchanted.TAB)));
    public static final RegistryObject<Item> CREEPER_HEART = ITEMS.register("creeper_heart", () -> new Item(new Item.Properties().tab(Enchanted.TAB)));
    public static final RegistryObject<Item> BONE_NEEDLE = ITEMS.register("bone_needle", () -> new Item(new Item.Properties().tab(Enchanted.TAB)));

    public static final RegistryObject<Item> REDSTONE_SOUP = ITEMS.register("redstone_soup", () -> new SimpleEffectBrewItem(Effects.ABSORPTION, 2400, 1, new Item.Properties().tab(Enchanted.TAB)));
    public static final RegistryObject<Item> FLYING_OINTMENT = ITEMS.register("flying_ointment", () -> new SimpleEffectBrewItem(Effects.LEVITATION, 400, 0, new Item.Properties().tab(Enchanted.TAB)));
    public static final RegistryObject<Item> MYSTIC_UNGUENT = ITEMS.register("mystic_unguent", () -> new SimpleEffectBrewItem(Effects.WEAKNESS, 1200, 1, new Item.Properties().tab(Enchanted.TAB)));
    public static final RegistryObject<Item> HAPPENSTANCE_OIL = ITEMS.register("happenstance_oil", () -> new SimpleEffectBrewItem(Effects.NIGHT_VISION, 1200, 0, new Item.Properties().tab(Enchanted.TAB)));

    public static final RegistryObject<Item> GHOST_OF_THE_LIGHT = ITEMS.register("ghost_of_the_light", () -> new SimpleEffectBrewItem(Effects.POISON, 1200, 1, new Item.Properties().tab(Enchanted.TAB)));
    public static final RegistryObject<Item> SOUL_OF_THE_WORLD = ITEMS.register("soul_of_the_world", () -> new SimpleEffectBrewItem(Effects.POISON, 1200, 1, new Item.Properties().tab(Enchanted.TAB)));
    public static final RegistryObject<Item> SPIRIT_OF_OTHERWHERE = ITEMS.register("spirit_of_otherwhere", () -> new SimpleEffectBrewItem(Effects.POISON, 1200, 1, new Item.Properties().tab(Enchanted.TAB)));
    public static final RegistryObject<Item> INFERNAL_ANIMUS = ITEMS.register("infernal_animus", () -> new SimpleEffectBrewItem(Effects.WITHER, 1200, 2, new Item.Properties().tab(Enchanted.TAB)));

    public static final RegistryObject<Item> POPPET = ITEMS.register("poppet", () -> new Item(new Item.Properties().tab(Enchanted.TAB)));
    public static final RegistryObject<Item> EARTH_POPPET = ITEMS.register("earth_poppet", () -> new BasicPoppetItem(0.3F, source -> source == DamageSource.FALL, new Item.Properties().tab(Enchanted.TAB)));
    public static final RegistryObject<Item> EARTH_POPPET_INFUSED = ITEMS.register("earth_poppet_infused", () -> new PoppetInfusedItem(EnchantedEffects.FALL_RESISTANCE, source -> source == DamageSource.FALL, new Item.Properties().tab(Enchanted.TAB)));
    public static final RegistryObject<Item> FIRE_POPPET = ITEMS.register("fire_poppet", () -> new BasicPoppetItem(0.3F, DamageSource::isFire, new Item.Properties().tab(Enchanted.TAB)));
    public static final RegistryObject<Item> FIRE_POPPET_INFUSED = ITEMS.register("fire_poppet_infused", () -> new PoppetInfusedItem(() -> Effects.FIRE_RESISTANCE, DamageSource::isFire, new Item.Properties().tab(Enchanted.TAB)));
    public static final RegistryObject<Item> WATER_POPPET = ITEMS.register("water_poppet", () -> new BasicPoppetItem(0.3F, source -> source == DamageSource.DROWN, new Item.Properties().tab(Enchanted.TAB)));
    public static final RegistryObject<Item> WATER_POPPET_INFUSED = ITEMS.register("water_poppet_infused", () -> new PoppetInfusedItem(EnchantedEffects.DROWN_RESISTANCE, source -> source == DamageSource.DROWN, new Item.Properties().tab(Enchanted.TAB)));
    public static final RegistryObject<Item> HUNGER_POPPET = ITEMS.register("hunger_poppet", () -> new HungerPoppetItem(0.3F, source -> source == DamageSource.STARVE, new Item.Properties().tab(Enchanted.TAB)));

}
