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
import com.favouriteless.enchanted.common.init.registry.EnchantedBlocks;
import com.favouriteless.enchanted.common.init.registry.EnchantedEffects;
import com.favouriteless.enchanted.common.init.registry.EnchantedEntityTypes;
import com.favouriteless.enchanted.common.items.*;
import com.favouriteless.enchanted.common.items.brews.SimpleEffectBrewItem;
import com.favouriteless.enchanted.common.items.brews.throwable.LoveBrewItem;
import com.favouriteless.enchanted.common.items.poppets.*;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ComposterBlock;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Predicate;
import java.util.function.Supplier;

public class EnchantedItems {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Enchanted.MOD_ID);

    public static final RegistryObject<Item> ALTAR = registerBlockItem("altar", EnchantedBlocks.ALTAR);
    public static final RegistryObject<Item> WITCH_OVEN = registerBlockItem("witch_oven", EnchantedBlocks.WITCH_OVEN);
    public static final RegistryObject<Item> FUME_FUNNEL = registerBlockItem("fume_funnel", EnchantedBlocks.FUME_FUNNEL);
    public static final RegistryObject<Item> FUME_FUNNEL_FILTERED = registerBlockItem("fume_funnel_filtered", EnchantedBlocks.FUME_FUNNEL_FILTERED);
    public static final RegistryObject<Item> DISTILLERY = registerBlockItem("distillery", EnchantedBlocks.DISTILLERY);
    public static final RegistryObject<Item> WITCH_CAULDRON = registerBlockItem("witch_cauldron", EnchantedBlocks.WITCH_CAULDRON);
    public static final RegistryObject<Item> KETTLE = registerBlockItem("kettle", EnchantedBlocks.KETTLE);
    public static final RegistryObject<Item> SPINNING_WHEEL = ITEMS.register("spinning_wheel", () -> new SpinningWheelBlockItem(defaultProperties()));
    public static final RegistryObject<Item> POPPET_SHELF = registerBlockItem("poppet_shelf", EnchantedBlocks.POPPET_SHELF);

    public static final RegistryObject<Item> CHALICE = registerBlockItem("chalice", EnchantedBlocks.CHALICE);
    public static final RegistryObject<Item> CHALICE_FILLED = registerBlockItem("chalice_filled", EnchantedBlocks.CHALICE_FILLED);
    public static final RegistryObject<Item> CHALICE_FILLED_MILK = registerBlockItem("chalice_filled_milk", EnchantedBlocks.CHALICE_FILLED_MILK);
    public static final RegistryObject<Item> CANDELABRA = registerBlockItem("candelabra", EnchantedBlocks.CANDELABRA);
    public static final RegistryObject<Item> INFINITY_EGG = registerBlockItem("infinity_egg", EnchantedBlocks.INFINITY_EGG);

    public static final RegistryObject<Item> ROWAN_LOG = registerBlockItem("rowan_log", EnchantedBlocks.ROWAN_LOG);
    public static final RegistryObject<Item> ROWAN_PLANKS = registerBlockItem("rowan_planks", EnchantedBlocks.ROWAN_PLANKS);
    public static final RegistryObject<Item> ROWAN_STAIRS = registerBlockItem("rowan_stairs", EnchantedBlocks.ROWAN_STAIRS);
    public static final RegistryObject<Item> ROWAN_SLAB = registerBlockItem("rowan_slab", EnchantedBlocks.ROWAN_SLAB);
    public static final RegistryObject<Item> ROWAN_LEAVES = registerBlockItem("rowan_leaves", EnchantedBlocks.ROWAN_LEAVES);
    public static final RegistryObject<Item> ROWAN_SAPLING = registerBlockItem("rowan_sapling", EnchantedBlocks.ROWAN_SAPLING);
    public static final RegistryObject<Item> HAWTHORN_LOG = registerBlockItem("hawthorn_log", EnchantedBlocks.HAWTHORN_LOG);
    public static final RegistryObject<Item> HAWTHORN_PLANKS = registerBlockItem("hawthorn_planks", EnchantedBlocks.HAWTHORN_PLANKS);
    public static final RegistryObject<Item> HAWTHORN_STAIRS = registerBlockItem("hawthorn_stairs", EnchantedBlocks.HAWTHORN_STAIRS);
    public static final RegistryObject<Item> HAWTHORN_SLAB = registerBlockItem("hawthorn_slab", EnchantedBlocks.HAWTHORN_SLAB);
    public static final RegistryObject<Item> HAWTHORN_LEAVES = registerBlockItem("hawthorn_leaves", EnchantedBlocks.HAWTHORN_LEAVES);
    public static final RegistryObject<Item> HAWTHORN_SAPLING = registerBlockItem("hawthorn_sapling", EnchantedBlocks.HAWTHORN_SAPLING);
    public static final RegistryObject<Item> ALDER_LOG = registerBlockItem("alder_log", EnchantedBlocks.ALDER_LOG);
    public static final RegistryObject<Item> ALDER_PLANKS = registerBlockItem("alder_planks", EnchantedBlocks.ALDER_PLANKS);
    public static final RegistryObject<Item> ALDER_STAIRS = registerBlockItem("alder_stairs", EnchantedBlocks.ALDER_STAIRS);
    public static final RegistryObject<Item> ALDER_SLAB = registerBlockItem("alder_slab", EnchantedBlocks.ALDER_SLAB);
    public static final RegistryObject<Item> ALDER_LEAVES = registerBlockItem("alder_leaves", EnchantedBlocks.ALDER_LEAVES);
    public static final RegistryObject<Item> ALDER_SAPLING = registerBlockItem("alder_sapling", EnchantedBlocks.ALDER_SAPLING);

    public static final RegistryObject<Item> WICKER_BUNDLE = registerBlockItem("wicker_bundle", EnchantedBlocks.WICKER_BUNDLE);

    public static final RegistryObject<Item> ARTICHOKE_SEEDS = ITEMS.register("artichoke_seeds", () -> new ArtichokeSeedsItem(EnchantedBlocks.ARTICHOKE.get(), defaultProperties()));
    public static final RegistryObject<Item> BELLADONNA_SEEDS = registerBlockNamedItem("belladonna_seeds", EnchantedBlocks.BELLADONNA);
    public static final RegistryObject<Item> MANDRAKE_SEEDS = registerBlockNamedItem("mandrake_seeds", EnchantedBlocks.MANDRAKE);
    public static final RegistryObject<Item> MANDRAKE_ROOT = registerItem("mandrake_root");
    public static final RegistryObject<Item> SNOWBELL_SEEDS = registerBlockNamedItem("snowbell_seeds", EnchantedBlocks.SNOWBELL);
    public static final RegistryObject<Item> WOLFSBANE_SEEDS = registerBlockNamedItem("wolfsbane_seeds", EnchantedBlocks.WOLFSBANE);

    public static final RegistryObject<Item> ARTICHOKE = registerFoodItem("artichoke", 3, MobEffects.HUNGER, 100, 0, 1.0F);
    public static final RegistryObject<Item> BELLADONNA_FLOWER = registerItem("belladonna_flower");
    public static final RegistryObject<Item> EMBER_MOSS = registerBlockItem("ember_moss", EnchantedBlocks.EMBER_MOSS);
    public static final RegistryObject<Item> GARLIC = registerBlockNamedItem("garlic", EnchantedBlocks.GARLIC);
    public static final RegistryObject<Item> GLINT_WEED = registerBlockItem("glint_weed", EnchantedBlocks.GLINT_WEED);
    public static final RegistryObject<Item> ICY_NEEDLE = registerItem("icy_needle");
    public static final RegistryObject<Item> SPANISH_MOSS = registerBlockItem("spanish_moss", EnchantedBlocks.SPANISH_MOSS);
    public static final RegistryObject<Item> WOLFSBANE_FLOWER = registerItem("wolfsbane_flower");
    public static final RegistryObject<Item> BLOOD_POPPY = registerBlockItem("blood_poppy", EnchantedBlocks.BLOOD_POPPY);

    public static final RegistryObject<Item> ANOINTING_PASTE = ITEMS.register("anointing_paste", () -> new AnointingPasteItem(defaultProperties()));
    public static final RegistryObject<Item> MUTANDIS = ITEMS.register("mutandis", () -> new MutandisItem(EnchantedTags.Blocks.MUTANDIS_PLANTS, defaultProperties()));
    public static final RegistryObject<Item> MUTANDIS_EXTREMIS = ITEMS.register("mutandis_extremis", () -> new MutandisItem(EnchantedTags.Blocks.MUTANDIS_EXTREMIS_PLANTS, defaultProperties()));
    public static final RegistryObject<Item> TAGLOCK = ITEMS.register("taglock", () -> new TaglockItem(defaultProperties()));
    public static final RegistryObject<Item> TAGLOCK_FILLED = ITEMS.register("taglock_filled", () -> new TaglockFilledItem(new Properties()));

    public static final RegistryObject<Item> CHALK_GOLD = registerChalkItem("chalk_gold", EnchantedBlocks.CHALK_GOLD, 3);
    public static final RegistryObject<Item> CHALK_WHITE = registerChalkItem("chalk_white", EnchantedBlocks.CHALK_WHITE, 40);
    public static final RegistryObject<Item> CHALK_RED = registerChalkItem("chalk_red", EnchantedBlocks.CHALK_RED, 40);
    public static final RegistryObject<Item> CHALK_PURPLE = registerChalkItem("chalk_purple", EnchantedBlocks.CHALK_PURPLE, 40);
    public static final RegistryObject<Item> ARTHANA = registerSwordItem("arthana", Tiers.GOLD, 3, -2.4F);
    public static final RegistryObject<Item> EARMUFFS = ITEMS.register("earmuffs", () -> new EarmuffsItem(defaultProperties()));

    public static final RegistryObject<Item> CIRCLE_TALISMAN = ITEMS.register("circle_talisman", () -> new CircleTalismanItem(defaultProperties().stacksTo(1)));
    public static final RegistryObject<Item> WAYSTONE = registerItem("waystone");
    public static final RegistryObject<Item> BOUND_WAYSTONE = registerItemNoTab("bound_waystone");
    public static final RegistryObject<Item> BLOODED_WAYSTONE = ITEMS.register("blooded_waystone", () -> new BloodedWaystoneItem(new Properties()));
    public static final RegistryObject<Item> BROOM = ITEMS.register("broom", () -> new BroomItem(defaultProperties()));
    public static final RegistryObject<Item> ENCHANTED_BROOMSTICK = ITEMS.register("enchanted_broomstick", () -> new BroomstickItem(defaultProperties()));

    public static final RegistryObject<Item> DEMON_HEART = registerItem("demon_heart");

    public static final RegistryObject<Item> CLAY_JAR_SOFT = registerItem("clay_jar_soft");
    public static final RegistryObject<Item> CLAY_JAR = registerItem("clay_jar");
    public static final RegistryObject<Item> BREATH_OF_THE_GODDESS = registerItem("breath_of_the_goddess");
    public static final RegistryObject<Item> EXHALE_OF_THE_HORNED_ONE = registerItem("exhale_of_the_horned_one");
    public static final RegistryObject<Item> FOUL_FUME = registerItem("foul_fume");
    public static final RegistryObject<Item> HINT_OF_REBIRTH = registerItem("hint_of_rebirth");
    public static final RegistryObject<Item> TEAR_OF_THE_GODDESS = registerItem("tear_of_the_goddess");
    public static final RegistryObject<Item> WHIFF_OF_MAGIC = registerItem("whiff_of_magic");
    public static final RegistryObject<Item> CONDENSED_FEAR = registerItem("condensed_fear");
    public static final RegistryObject<Item> DIAMOND_VAPOUR = registerItem("diamond_vapour");
    public static final RegistryObject<Item> DROP_OF_LUCK = registerItem("drop_of_luck");
    public static final RegistryObject<Item> ENDER_DEW = registerItem("ender_dew");
    public static final RegistryObject<Item> FOCUSED_WILL = registerItem("focused_will");
    public static final RegistryObject<Item> DEMONIC_BLOOD = registerItem("demonic_blood");
    public static final RegistryObject<Item> MELLIFLUOUS_HUNGER = registerItem("mellifluous_hunger");
    public static final RegistryObject<Item> ODOUR_OF_PURITY = registerItem("odour_of_purity");
    public static final RegistryObject<Item> OIL_OF_VITRIOL = registerItem("oil_of_vitriol");
    public static final RegistryObject<Item> PURIFIED_MILK = registerItem("purified_milk");
    public static final RegistryObject<Item> REEK_OF_MISFORTUNE = registerItem("reek_of_misfortune");
    public static final RegistryObject<Item> ATTUNED_STONE = registerItem("attuned_stone");
    public static final RegistryObject<Item> ATTUNED_STONE_CHARGED = ITEMS.register("attuned_stone_charged", () -> new SimpleFoiledItem(defaultProperties()));
    public static final RegistryObject<Item> GYPSUM = registerItem("gypsum");
    public static final RegistryObject<Item> QUICKLIME = registerItem("quicklime");
    public static final RegistryObject<Item> REFINED_EVIL = registerItem("refined_evil");
    public static final RegistryObject<Item> ROWAN_BERRIES = registerFoodItem("rowan_berries", 3);
    public static final RegistryObject<Item> WOOD_ASH = registerItem("wood_ash");
    public static final RegistryObject<Item> FUME_FILTER = registerItem("fume_filter");
    public static final RegistryObject<Item> WOOL_OF_BAT = registerItem("wool_of_bat");
    public static final RegistryObject<Item> TONGUE_OF_DOG = registerItem("tongue_of_dog");
    public static final RegistryObject<Item> CREEPER_HEART = registerItem("creeper_heart");
    public static final RegistryObject<Item> BONE_NEEDLE = registerItem("bone_needle");
    public static final RegistryObject<Item> ENT_TWIG = registerItem("ent_twig");

    public static final RegistryObject<Item> REDSTONE_SOUP = registerBrew("redstone_soup", MobEffects.ABSORPTION, 2400, 1);
    public static final RegistryObject<Item> FLYING_OINTMENT = registerBrew("flying_ointment", MobEffects.LEVITATION, 400, 0);
    public static final RegistryObject<Item> MYSTIC_UNGUENT = registerBrew("mystic_unguent", MobEffects.WEAKNESS, 1200, 1);
    public static final RegistryObject<Item> HAPPENSTANCE_OIL = registerBrew("happenstance_oil", MobEffects.NIGHT_VISION, 1200, 0);

    public static final RegistryObject<Item> BREW_OF_THE_GROTESQUE = registerItem("brew_of_the_grotesque");
    public static final RegistryObject<Item> BREW_OF_SPROUTING = registerItem("brew_of_sprouting");
    public static final RegistryObject<Item> BREW_OF_LOVE = ITEMS.register("brew_of_love", () -> new LoveBrewItem(defaultProperties()));
    public static final RegistryObject<Item> BREW_OF_THE_DEPTHS = registerBrew("brew_of_the_depths", MobEffects.WATER_BREATHING, 6000, 0);

    public static final RegistryObject<Item> GHOST_OF_THE_LIGHT = registerBrew("ghost_of_the_light", MobEffects.POISON, 1200, 1);
    public static final RegistryObject<Item> SOUL_OF_THE_WORLD = registerBrew("soul_of_the_world", MobEffects.POISON, 1200, 1);
    public static final RegistryObject<Item> SPIRIT_OF_OTHERWHERE = registerBrew("spirit_of_otherwhere", MobEffects.POISON, 1200, 1);
    public static final RegistryObject<Item> INFERNAL_ANIMUS = registerBrew("infernal_animus", MobEffects.WITHER, 1200, 2);

    public static final RegistryObject<Item> POPPET = registerItem("poppet");
    public static final RegistryObject<Item> POPPET_INFUSED = registerItem("poppet_infused");
    public static final RegistryObject<Item> POPPET_STURDY = registerItem("poppet_sturdy");
    public static final RegistryObject<Item> EARTH_POPPET = registerDeathPoppet("earth_poppet", 0.3F, 1, PoppetColour.EARTH, DamageSource.FALL, DamageSource.FLY_INTO_WALL);
    public static final RegistryObject<Item> EARTH_POPPET_INFUSED = registerDeathPoppetEffect("earth_poppet_infused", 0.0F, 1, PoppetColour.EARTH, EnchantedEffects.FALL_RESISTANCE, 200, 0, DamageSource.FALL, DamageSource.FLY_INTO_WALL);
    public static final RegistryObject<Item> EARTH_POPPET_STURDY = registerDeathPoppet("earth_poppet_sturdy", 0.0F, 2, PoppetColour.EARTH, DamageSource.FALL, DamageSource.FLY_INTO_WALL);
    public static final RegistryObject<Item> FIRE_POPPET = registerFirePoppet("fire_poppet", 0.3F, 1, PoppetColour.FIRE, DamageSource::isFire);
    public static final RegistryObject<Item> FIRE_POPPET_INFUSED = registerFirePoppetEffect("fire_poppet_infused", 0.0F, 1, PoppetColour.FIRE, MobEffects.FIRE_RESISTANCE, 200, 0, DamageSource::isFire);
    public static final RegistryObject<Item> FIRE_POPPET_STURDY = registerFirePoppet("fire_poppet_sturdy", 0.0F, 2, PoppetColour.FIRE, DamageSource::isFire);
    public static final RegistryObject<Item> WATER_POPPET = registerDeathPoppetEffect("water_poppet", 0.3F, 1, PoppetColour.WATER, EnchantedEffects.DROWN_RESISTANCE, 100, 0, DamageSource.DROWN);
    public static final RegistryObject<Item> WATER_POPPET_INFUSED = registerDeathPoppetEffect("water_poppet_infused", 0.0F, 1, PoppetColour.WATER, MobEffects.WATER_BREATHING, 200, 0, DamageSource.DROWN);
    public static final RegistryObject<Item> WATER_POPPET_STURDY = registerDeathPoppetEffect("water_poppet_sturdy", 0.0F, 2, PoppetColour.WATER, EnchantedEffects.DROWN_RESISTANCE, 100, 0, DamageSource.DROWN);
    public static final RegistryObject<Item> HUNGER_POPPET = registerDeathPoppet("hunger_poppet", 0.3F, 1, PoppetColour.HUNGER, DamageSource.STARVE);
    public static final RegistryObject<Item> HUNGER_POPPET_INFUSED = registerDeathPoppetEffect("hunger_poppet_infused", 0.0F, 1, PoppetColour.HUNGER, MobEffects.SATURATION, 100, 0, DamageSource.STARVE);
    public static final RegistryObject<Item> HUNGER_POPPET_STURDY = registerDeathPoppet("hunger_poppet_sturdy", 0.0F, 2, PoppetColour.HUNGER, DamageSource.STARVE);
    public static final RegistryObject<Item> VOID_POPPET = registerVoidPoppet("void_poppet", 0.3F, 1, PoppetColour.VOID, DamageSource.OUT_OF_WORLD);
    public static final RegistryObject<Item> VOID_POPPET_INFUSED = registerVoidPoppetEffect("void_poppet_infused", 0.0F, 1, PoppetColour.VOID, EnchantedEffects.FALL_RESISTANCE, 150, 0, DamageSource.OUT_OF_WORLD);
    public static final RegistryObject<Item> VOID_POPPET_STURDY = registerVoidPoppet("void_poppet_sturdy", 0.0F, 2, PoppetColour.VOID, DamageSource.OUT_OF_WORLD);
    public static final RegistryObject<Item> TOOL_POPPET = registerItemProtectionPoppet("tool_poppet", 0.3F, 1, 0.9F, PoppetColour.EQUIPMENT);
    public static final RegistryObject<Item> TOOL_POPPET_INFUSED = registerItemProtectionPoppet("tool_poppet_infused", 0.0F, 1, 0.0F, PoppetColour.EQUIPMENT);
    public static final RegistryObject<Item> TOOL_POPPET_STURDY = registerItemProtectionPoppet("tool_poppet_sturdy", 0.0F, 2, 0.9F, PoppetColour.EQUIPMENT);
    public static final RegistryObject<Item> ARMOUR_POPPET = registerItemProtectionPoppet("armour_poppet", 0.3F, 1, 0.9F, PoppetColour.EQUIPMENT);
    public static final RegistryObject<Item> ARMOUR_POPPET_INFUSED = registerItemProtectionPoppet("armour_poppet_infused", 0.0F, 1, 0.0F, PoppetColour.EQUIPMENT);
    public static final RegistryObject<Item> ARMOUR_POPPET_STURDY = registerItemProtectionPoppet("armour_poppet_sturdy", 0.0F, 2, 0.9F, PoppetColour.EQUIPMENT);

    public static final RegistryObject<Item> MANDRAKE_SPAWN_EGG = registerSpawnEgg("mandrake_spawn_egg", EnchantedEntityTypes.MANDRAKE::get, 0x634C39, 0x291A0E);
    public static final RegistryObject<Item> ENT_SPAWN_EGG = registerSpawnEgg("ent_spawn_egg", EnchantedEntityTypes.ENT::get, 0x2A5422, 0x183812);




    private static Properties defaultProperties() {
        return new Properties().tab(TAB);
    }

    private static RegistryObject<Item> registerSpawnEgg(String name, Supplier<EntityType<? extends Mob>> type, int backgroundColour, int highlightColour) {
        return ITEMS.register(name, () -> new ForgeSpawnEggItem(type, backgroundColour, highlightColour, defaultProperties()));
    }

    private static RegistryObject<Item> registerItem(String name) {
        return ITEMS.register(name, () -> new Item(defaultProperties()));
    }

    private static RegistryObject<Item> registerItemNoTab(String name) {
        return ITEMS.register(name, () -> new Item(new Properties()));
    }

    private static RegistryObject<Item> registerItem(String name, Properties properties) {
        return ITEMS.register(name, () -> new Item(properties));
    }

    private static RegistryObject<Item> registerBlockItem(String name, Supplier<? extends Block> block) {
        return ITEMS.register(name, () -> new BlockItem(block.get(), defaultProperties()));
    }

    private static RegistryObject<Item> registerBlockItemProperties(String name, Supplier<? extends Block> block, Properties properties) {
        return ITEMS.register(name, () -> new BlockItem(block.get(), properties));
    }

    private static RegistryObject<Item> registerBlockNamedItem(String name, Supplier<? extends Block> block) {
        return ITEMS.register(name, () -> new ItemNameBlockItem(block.get(), defaultProperties()));
    }

    private static RegistryObject<Item> registerFoodItem(String name, int nutrition) {
        return ITEMS.register(name, () -> new Item(defaultProperties().food(new FoodProperties.Builder().nutrition(nutrition).build())));
    }

    private static RegistryObject<Item> registerFoodItem(String name, int nutrition, MobEffect effect, int duration, int amplification, float chance) {
        return ITEMS.register(name, () -> new Item(defaultProperties().food(new FoodProperties.Builder().nutrition(nutrition).effect(() -> new MobEffectInstance(effect, duration, amplification), chance).build())));
    }

    private static RegistryObject<Item> registerChalkItem(String name, Supplier<? extends Block> block, int durability) {
        return ITEMS.register(name, () -> new ChalkItem(block.get(), defaultProperties().stacksTo(1).durability(durability)));
    }

    private static RegistryObject<Item> registerSwordItem(String name, Tier tier, int attackDamageModifier, float attackSpeedModifier) {
        return ITEMS.register(name, () -> new SwordItem(tier, attackDamageModifier, attackSpeedModifier, defaultProperties()));
    }

    private static RegistryObject<Item> registerBrew(String name, MobEffect effect, int duration, int amplifier) {
        return ITEMS.register(name, () -> new SimpleEffectBrewItem(effect, duration, amplifier, defaultProperties()));
    }

    private static RegistryObject<Item> registerItemProtectionPoppet(String name, float failRate, int durability, float damageMultiplier, PoppetColour colour) {
        return ITEMS.register(name, () -> new ItemProtectionPoppetItem(failRate, durability, damageMultiplier, colour));
    }

    private static RegistryObject<Item> registerDeathPoppet(String name, float failRate, int durability, PoppetColour colour, DamageSource... damageSources) {
        return ITEMS.register(name, () -> new DeathPoppetItem(failRate, durability, colour, source -> containsSource(damageSources, source)));
    }

    private static RegistryObject<Item> registerDeathPoppetEffect(String name, float failRate, int durability, PoppetColour colour, MobEffect effect, int duration, int amplifier, DamageSource... damageSources) {
        return ITEMS.register(name, () -> new DeathPoppetEffectItem(failRate, durability, colour, source -> containsSource(damageSources, source), () -> new MobEffectInstance(effect, duration, amplifier)));
    }

    private static RegistryObject<Item> registerDeathPoppetEffect(String name, float failRate, int durability, PoppetColour colour, Supplier<MobEffect> effect, int duration, int amplifier, DamageSource... damageSources) {
        return ITEMS.register(name, () -> new DeathPoppetEffectItem(failRate, durability, colour, source -> containsSource(damageSources, source), () -> new MobEffectInstance(effect.get(), duration, amplifier)));
    }

    private static RegistryObject<Item> registerVoidPoppet(String name, float failRate, int durability, PoppetColour colour, DamageSource... damageSources) {
        return ITEMS.register(name, () -> new VoidPoppetItem(failRate, durability, colour, source -> containsSource(damageSources, source)));
    }

    private static RegistryObject<Item> registerVoidPoppetEffect(String name, float failRate, int durability, PoppetColour colour, Supplier<MobEffect> effect, int duration, int amplifier, DamageSource... damageSources) {
        return ITEMS.register(name, () -> new VoidPoppetEffectItem(failRate, durability, colour, source -> containsSource(damageSources, source), () -> new MobEffectInstance(effect.get(), duration, amplifier)));
    }

    private static RegistryObject<Item> registerFirePoppet(String name, float failRate, int durability, PoppetColour colour, Predicate<DamageSource> sourcePredicate) {
        return ITEMS.register(name, () -> new FirePoppetItem(failRate, durability, colour, sourcePredicate));
    }

    private static RegistryObject<Item> registerFirePoppetEffect(String name, float failRate, int durability, PoppetColour colour, MobEffect effect, int duration, int amplifier, Predicate<DamageSource> sourcePredicate) {
        return ITEMS.register(name, () -> new FirePoppetEffectItem(failRate, durability, colour, sourcePredicate, () -> new MobEffectInstance(effect, duration, amplifier)));
    }

    private static boolean containsSource(DamageSource[] sources, DamageSource source) {
        for(DamageSource i : sources) {
            if(i == source)
                return true;
        }
        return false;
    }

    public static boolean isToolPoppet(Item item) {
        return item == TOOL_POPPET.get() || item == TOOL_POPPET_INFUSED.get() || item == TOOL_POPPET_STURDY.get();
    }

    public static  boolean isArmourPoppet(Item item) {
        return item == ARMOUR_POPPET.get() || item == ARMOUR_POPPET_INFUSED.get() || item == ARMOUR_POPPET_STURDY.get();
    }

    public static void registerCompostables() {
        ComposterBlock.COMPOSTABLES.put(ARTICHOKE_SEEDS.get(), 0.3F);
        ComposterBlock.COMPOSTABLES.put(ARTICHOKE.get(), 0.65F);
        ComposterBlock.COMPOSTABLES.put(SNOWBELL_SEEDS.get(), 0.3F);
        ComposterBlock.COMPOSTABLES.put(BELLADONNA_SEEDS.get(), 0.3F);
        ComposterBlock.COMPOSTABLES.put(BELLADONNA_FLOWER.get(), 0.65F);
        ComposterBlock.COMPOSTABLES.put(MANDRAKE_SEEDS.get(), 0.3F);
        ComposterBlock.COMPOSTABLES.put(MANDRAKE_ROOT.get(), 0.65F);
        ComposterBlock.COMPOSTABLES.put(WOLFSBANE_SEEDS.get(), 0.3F);
        ComposterBlock.COMPOSTABLES.put(WOLFSBANE_FLOWER.get(), 0.65F);
        ComposterBlock.COMPOSTABLES.put(GARLIC.get(), 0.45F);
    }



    public static final CreativeModeTab TAB = new CreativeModeTab(Enchanted.MOD_ID + ".main") {

        @Override
        public ItemStack makeIcon() {
            return new ItemStack(EnchantedItems.ENCHANTED_BROOMSTICK.get());
        }

        @Override
        public void fillItemList(NonNullList<ItemStack> items) {
            super.fillItemList(items);

            String[] keys = new String[] { "small", "medium", "large" };
            for(int i = 1; i < 4; i++) {
                for(String key : keys) {
                    ItemStack stack = new ItemStack(CIRCLE_TALISMAN.get());
                    CompoundTag nbt = stack.getOrCreateTag();
                    nbt.putInt(key, i);
                    items.add(stack);
                }
            }
        }
    };

}
