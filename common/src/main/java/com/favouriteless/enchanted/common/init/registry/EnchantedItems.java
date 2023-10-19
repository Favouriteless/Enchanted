package com.favouriteless.enchanted.common.init.registry;

import com.favouriteless.enchanted.common.init.EnchantedTags;
import com.favouriteless.enchanted.common.items.*;
import com.favouriteless.enchanted.common.items.brews.SimpleEffectBrewItem;
import com.favouriteless.enchanted.common.items.brews.throwable.LoveBrewItem;
import com.favouriteless.enchanted.platform.Services;
import net.minecraft.core.Registry;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.level.block.Block;

import java.util.function.Supplier;

public class EnchantedItems {

	public static final Supplier<Item> ALTAR = registerBlockItem("altar", EnchantedBlocks.ALTAR);
	public static final Supplier<Item> WITCH_OVEN = registerBlockItem("witch_oven", EnchantedBlocks.WITCH_OVEN);
	public static final Supplier<Item> FUME_FUNNEL = registerBlockItem("fume_funnel", EnchantedBlocks.FUME_FUNNEL);
	public static final Supplier<Item> FUME_FUNNEL_FILTERED = registerBlockItem("fume_funnel_filtered", EnchantedBlocks.FUME_FUNNEL_FILTERED);
	public static final Supplier<Item> DISTILLERY = registerBlockItem("distillery", EnchantedBlocks.DISTILLERY);
	public static final Supplier<Item> WITCH_CAULDRON = registerBlockItem("witch_cauldron", EnchantedBlocks.WITCH_CAULDRON);
	public static final Supplier<Item> KETTLE = registerBlockItem("kettle", EnchantedBlocks.KETTLE);
	public static final Supplier<Item> SPINNING_WHEEL = register("spinning_wheel", () -> new SpinningWheelBlockItem(defaultProperties()));
	public static final Supplier<Item> POPPET_SHELF = registerBlockItem("poppet_shelf", EnchantedBlocks.POPPET_SHELF);

	public static final Supplier<Item> CHALICE = registerBlockItem("chalice", EnchantedBlocks.CHALICE);
	public static final Supplier<Item> CHALICE_FILLED = registerBlockItem("chalice_filled", EnchantedBlocks.CHALICE_FILLED);
	public static final Supplier<Item> CHALICE_FILLED_MILK = registerBlockItem("chalice_filled_milk", EnchantedBlocks.CHALICE_FILLED_MILK);
	public static final Supplier<Item> CANDELABRA = registerBlockItem("candelabra", EnchantedBlocks.CANDELABRA);
	public static final Supplier<Item> INFINITY_EGG = registerBlockItem("infinity_egg", EnchantedBlocks.INFINITY_EGG);
//
	public static final Supplier<Item> ROWAN_LOG = registerBlockItem("rowan_log", EnchantedBlocks.ROWAN_LOG);
	public static final Supplier<Item> ROWAN_PLANKS = registerBlockItem("rowan_planks", EnchantedBlocks.ROWAN_PLANKS);
	public static final Supplier<Item> ROWAN_STAIRS = registerBlockItem("rowan_stairs", EnchantedBlocks.ROWAN_STAIRS);
	public static final Supplier<Item> ROWAN_SLAB = registerBlockItem("rowan_slab", EnchantedBlocks.ROWAN_SLAB);
	public static final Supplier<Item> ROWAN_LEAVES = registerBlockItem("rowan_leaves", EnchantedBlocks.ROWAN_LEAVES);
	public static final Supplier<Item> ROWAN_SAPLING = registerBlockItem("rowan_sapling", EnchantedBlocks.ROWAN_SAPLING);
	public static final Supplier<Item> HAWTHORN_LOG = registerBlockItem("hawthorn_log", EnchantedBlocks.HAWTHORN_LOG);
	public static final Supplier<Item> HAWTHORN_PLANKS = registerBlockItem("hawthorn_planks", EnchantedBlocks.HAWTHORN_PLANKS);
	public static final Supplier<Item> HAWTHORN_STAIRS = registerBlockItem("hawthorn_stairs", EnchantedBlocks.HAWTHORN_STAIRS);
	public static final Supplier<Item> HAWTHORN_SLAB = registerBlockItem("hawthorn_slab", EnchantedBlocks.HAWTHORN_SLAB);
	public static final Supplier<Item> HAWTHORN_LEAVES = registerBlockItem("hawthorn_leaves", EnchantedBlocks.HAWTHORN_LEAVES);
	public static final Supplier<Item> HAWTHORN_SAPLING = registerBlockItem("hawthorn_sapling", EnchantedBlocks.HAWTHORN_SAPLING);
	public static final Supplier<Item> ALDER_LOG = registerBlockItem("alder_log", EnchantedBlocks.ALDER_LOG);
	public static final Supplier<Item> ALDER_PLANKS = registerBlockItem("alder_planks", EnchantedBlocks.ALDER_PLANKS);
	public static final Supplier<Item> ALDER_STAIRS = registerBlockItem("alder_stairs", EnchantedBlocks.ALDER_STAIRS);
	public static final Supplier<Item> ALDER_SLAB = registerBlockItem("alder_slab", EnchantedBlocks.ALDER_SLAB);
	public static final Supplier<Item> ALDER_LEAVES = registerBlockItem("alder_leaves", EnchantedBlocks.ALDER_LEAVES);
	public static final Supplier<Item> ALDER_SAPLING = registerBlockItem("alder_sapling", EnchantedBlocks.ALDER_SAPLING);

	public static final Supplier<Item> WICKER_BUNDLE = registerBlockItem("wicker_bundle", EnchantedBlocks.WICKER_BUNDLE);

	public static final Supplier<Item> ARTICHOKE_SEEDS = register("artichoke_seeds",  () -> new ArtichokeSeedsItem(EnchantedBlocks.ARTICHOKE.get(), defaultProperties()));
	public static final Supplier<Item> BELLADONNA_SEEDS = registerBlockNamedItem("belladonna_seeds", EnchantedBlocks.BELLADONNA);
	public static final Supplier<Item> MANDRAKE_SEEDS = registerBlockNamedItem("mandrake_seeds", EnchantedBlocks.MANDRAKE);
	public static final Supplier<Item> MANDRAKE_ROOT = registerItem("mandrake_root");
	public static final Supplier<Item> SNOWBELL_SEEDS = registerBlockNamedItem("snowbell_seeds", EnchantedBlocks.SNOWBELL);
	public static final Supplier<Item> WOLFSBANE_SEEDS = registerBlockNamedItem("wolfsbane_seeds", EnchantedBlocks.WOLFSBANE);

	public static final Supplier<Item> ARTICHOKE = registerFoodItem("artichoke", 3, MobEffects.HUNGER, 100, 0, 1.0F);
	public static final Supplier<Item> BELLADONNA_FLOWER = registerItem("belladonna_flower");
	public static final Supplier<Item> EMBER_MOSS = registerBlockItem("ember_moss", EnchantedBlocks.EMBER_MOSS);
	public static final Supplier<Item> GARLIC = registerBlockNamedItem("garlic", EnchantedBlocks.GARLIC);
	public static final Supplier<Item> GLINT_WEED = registerBlockItem("glint_weed", EnchantedBlocks.GLINT_WEED);
	public static final Supplier<Item> ICY_NEEDLE = registerItem("icy_needle");
	public static final Supplier<Item> SPANISH_MOSS = registerBlockItem("spanish_moss", EnchantedBlocks.SPANISH_MOSS);
	public static final Supplier<Item> WOLFSBANE_FLOWER = registerItem("wolfsbane_flower");
	public static final Supplier<Item> BLOOD_POPPY = registerBlockItem("blood_poppy", EnchantedBlocks.BLOOD_POPPY);

	public static final Supplier<Item> ANOINTING_PASTE = register("anointing_paste",  () -> new AnointingPasteItem(defaultProperties()));
	public static final Supplier<Item> MUTANDIS = register("mutandis",  () -> new MutandisItem(EnchantedTags.Blocks.MUTANDIS_PLANTS, defaultProperties()));
	public static final Supplier<Item> MUTANDIS_EXTREMIS = register("mutandis_extremis",  () -> new MutandisItem(EnchantedTags.Blocks.MUTANDIS_EXTREMIS_PLANTS, defaultProperties()));
	public static final Supplier<Item> TAGLOCK = register("taglock",  () -> new TaglockItem(defaultProperties()));
	public static final Supplier<Item> TAGLOCK_FILLED =  register("taglock_filled",  () -> new TaglockFilledItem(new Properties()));

//	public static final Supplier<Item> CHALK_GOLD = registerChalkItem("chalk_gold", EnchantedBlocks.CHALK_GOLD, 3);
//	public static final Supplier<Item> CHALK_WHITE = registerChalkItem("chalk_white", EnchantedBlocks.CHALK_WHITE, 40);
//	public static final Supplier<Item> CHALK_RED = registerChalkItem("chalk_red", EnchantedBlocks.CHALK_RED, 40);
//	public static final Supplier<Item> CHALK_PURPLE = registerChalkItem("chalk_purple", EnchantedBlocks.CHALK_PURPLE, 40);
	public static final Supplier<Item> ARTHANA = registerSwordItem("arthana", Tiers.GOLD, 3, -2.4F);
	public static final Supplier<Item> EARMUFFS = register("earmuffs",  () -> new EarmuffsItem(defaultProperties()));

	public static final Supplier<Item> CIRCLE_TALISMAN = register("circle_talisman",  () -> new CircleTalismanItem(defaultProperties().stacksTo(1)));
	public static final Supplier<Item> WAYSTONE = registerItem("waystone");
	public static final Supplier<Item> BOUND_WAYSTONE = registerItemNoTab("bound_waystone");
	public static final Supplier<Item> BLOODED_WAYSTONE = register("blooded_waystone",  () -> new BloodedWaystoneItem(new Properties()));
	public static final Supplier<Item> BROOM = register("broom",  () -> new BroomItem(defaultProperties()));
	public static final Supplier<Item> ENCHANTED_BROOMSTICK = register("enchanted_broomstick",  () -> new BroomstickItem(defaultProperties()));

	public static final Supplier<Item> DEMON_HEART = registerItem("demon_heart");

	public static final Supplier<Item> CLAY_JAR_SOFT = registerItem("clay_jar_soft");
	public static final Supplier<Item> CLAY_JAR = registerItem("clay_jar");
	public static final Supplier<Item> BREATH_OF_THE_GODDESS = registerItem("breath_of_the_goddess");
	public static final Supplier<Item> EXHALE_OF_THE_HORNED_ONE = registerItem("exhale_of_the_horned_one");
	public static final Supplier<Item> FOUL_FUME = registerItem("foul_fume");
	public static final Supplier<Item> HINT_OF_REBIRTH = registerItem("hint_of_rebirth");
	public static final Supplier<Item> TEAR_OF_THE_GODDESS = registerItem("tear_of_the_goddess");
	public static final Supplier<Item> WHIFF_OF_MAGIC = registerItem("whiff_of_magic");
	public static final Supplier<Item> CONDENSED_FEAR = registerItem("condensed_fear");
	public static final Supplier<Item> DIAMOND_VAPOUR = registerItem("diamond_vapour");
	public static final Supplier<Item> DROP_OF_LUCK = registerItem("drop_of_luck");
	public static final Supplier<Item> ENDER_DEW = registerItem("ender_dew");
	public static final Supplier<Item> FOCUSED_WILL = registerItem("focused_will");
	public static final Supplier<Item> DEMONIC_BLOOD = registerItem("demonic_blood");
	public static final Supplier<Item> MELLIFLUOUS_HUNGER = registerItem("mellifluous_hunger");
	public static final Supplier<Item> ODOUR_OF_PURITY = registerItem("odour_of_purity");
	public static final Supplier<Item> OIL_OF_VITRIOL = registerItem("oil_of_vitriol");
	public static final Supplier<Item> PURIFIED_MILK = registerItem("purified_milk");
	public static final Supplier<Item> REEK_OF_MISFORTUNE = registerItem("reek_of_misfortune");
	public static final Supplier<Item> ATTUNED_STONE = registerItem("attuned_stone");
	public static final Supplier<Item> ATTUNED_STONE_CHARGED = register("attuned_stone_charged",  () -> new SimpleFoiledItem(defaultProperties()));
	public static final Supplier<Item> GYPSUM = registerItem("gypsum");
	public static final Supplier<Item> QUICKLIME = registerItem("quicklime");
	public static final Supplier<Item> REFINED_EVIL = registerItem("refined_evil");
	public static final Supplier<Item> ROWAN_BERRIES = registerFoodItem("rowan_berries", 3);
	public static final Supplier<Item> WOOD_ASH = registerItem("wood_ash");
	public static final Supplier<Item> FUME_FILTER = registerItem("fume_filter");
	public static final Supplier<Item> WOOL_OF_BAT = registerItem("wool_of_bat");
	public static final Supplier<Item> TONGUE_OF_DOG = registerItem("tongue_of_dog");
	public static final Supplier<Item> CREEPER_HEART = registerItem("creeper_heart");
	public static final Supplier<Item> BONE_NEEDLE = registerItem("bone_needle");
	public static final Supplier<Item> ENT_TWIG = registerItem("ent_twig");

	public static final Supplier<Item> REDSTONE_SOUP = registerBrew("redstone_soup", MobEffects.ABSORPTION, 2400, 1);
	public static final Supplier<Item> FLYING_OINTMENT = registerBrew("flying_ointment", MobEffects.LEVITATION, 400, 0);
	public static final Supplier<Item> MYSTIC_UNGUENT = registerBrew("mystic_unguent", MobEffects.WEAKNESS, 1200, 1);
	public static final Supplier<Item> HAPPENSTANCE_OIL = registerBrew("happenstance_oil", MobEffects.NIGHT_VISION, 1200, 0);

	public static final Supplier<Item> BREW_OF_THE_GROTESQUE = registerItem("brew_of_the_grotesque");
	public static final Supplier<Item> BREW_OF_SPROUTING = registerItem("brew_of_sprouting");
	public static final Supplier<Item> BREW_OF_LOVE = register("brew_of_love",  () -> new LoveBrewItem(defaultProperties()));
	public static final Supplier<Item> BREW_OF_THE_DEPTHS = registerBrew("brew_of_the_depths", MobEffects.WATER_BREATHING, 6000, 0);

	public static final Supplier<Item> GHOST_OF_THE_LIGHT = registerBrew("ghost_of_the_light", MobEffects.POISON, 1200, 1);
	public static final Supplier<Item> SOUL_OF_THE_WORLD = registerBrew("soul_of_the_world", MobEffects.POISON, 1200, 1);
	public static final Supplier<Item> SPIRIT_OF_OTHERWHERE = registerBrew("spirit_of_otherwhere", MobEffects.POISON, 1200, 1);
	public static final Supplier<Item> INFERNAL_ANIMUS = registerBrew("infernal_animus", MobEffects.WITHER, 1200, 2);

	public static final Supplier<Item> POPPET = registerItem("poppet");
	public static final Supplier<Item> POPPET_INFUSED = registerItem("poppet_infused");
	public static final Supplier<Item> POPPET_STURDY = registerItem("poppet_sturdy");
//	public static final Supplier<Item> EARTH_POPPET = registerDeathPoppet("earth_poppet", 0.3F, 1, PoppetColour.EARTH, DamageSource.FALL, DamageSource.FLY_INTO_WALL);
//	public static final Supplier<Item> EARTH_POPPET_INFUSED = registerDeathPoppetEffect("earth_poppet_infused", 0.0F, 1, PoppetColour.EARTH, EnchantedEffects.FALL_RESISTANCE, 200, 0, DamageSource.FALL, DamageSource.FLY_INTO_WALL);
//	public static final Supplier<Item> EARTH_POPPET_STURDY = registerDeathPoppet("earth_poppet_sturdy", 0.0F, 2, PoppetColour.EARTH, DamageSource.FALL, DamageSource.FLY_INTO_WALL);
//	public static final Supplier<Item> FIRE_POPPET = registerFirePoppet("fire_poppet", 0.3F, 1, PoppetColour.FIRE, DamageSource::isFire);
//	public static final Supplier<Item> FIRE_POPPET_INFUSED = registerFirePoppetEffect("fire_poppet_infused", 0.0F, 1, PoppetColour.FIRE, MobEffects.FIRE_RESISTANCE, 200, 0, DamageSource::isFire);
//	public static final Supplier<Item> FIRE_POPPET_STURDY = registerFirePoppet("fire_poppet_sturdy", 0.0F, 2, PoppetColour.FIRE, DamageSource::isFire);
//	public static final Supplier<Item> WATER_POPPET = registerDeathPoppetEffect("water_poppet", 0.3F, 1, PoppetColour.WATER, EnchantedEffects.DROWN_RESISTANCE, 100, 0, DamageSource.DROWN);
//	public static final Supplier<Item> WATER_POPPET_INFUSED = registerDeathPoppetEffect("water_poppet_infused", 0.0F, 1, PoppetColour.WATER, MobEffects.WATER_BREATHING, 200, 0, DamageSource.DROWN);
//	public static final Supplier<Item> WATER_POPPET_STURDY = registerDeathPoppetEffect("water_poppet_sturdy", 0.0F, 2, PoppetColour.WATER, EnchantedEffects.DROWN_RESISTANCE, 100, 0, DamageSource.DROWN);
//	public static final Supplier<Item> HUNGER_POPPET = registerDeathPoppet("hunger_poppet", 0.3F, 1, PoppetColour.HUNGER, DamageSource.STARVE);
//	public static final Supplier<Item> HUNGER_POPPET_INFUSED = registerDeathPoppetEffect("hunger_poppet_infused", 0.0F, 1, PoppetColour.HUNGER, MobEffects.SATURATION, 100, 0, DamageSource.STARVE);
//	public static final Supplier<Item> HUNGER_POPPET_STURDY = registerDeathPoppet("hunger_poppet_sturdy", 0.0F, 2, PoppetColour.HUNGER, DamageSource.STARVE);
//	public static final Supplier<Item> VOID_POPPET = registerVoidPoppet("void_poppet", 0.3F, 1, PoppetColour.VOID, DamageSource.OUT_OF_WORLD);
//	public static final Supplier<Item> VOID_POPPET_INFUSED = registerVoidPoppetEffect("void_poppet_infused", 0.0F, 1, PoppetColour.VOID, EnchantedEffects.FALL_RESISTANCE, 150, 0, DamageSource.OUT_OF_WORLD);
//	public static final Supplier<Item> VOID_POPPET_STURDY = registerVoidPoppet("void_poppet_sturdy", 0.0F, 2, PoppetColour.VOID, DamageSource.OUT_OF_WORLD);
//	public static final Supplier<Item> TOOL_POPPET = registerItemProtectionPoppet("tool_poppet", 0.3F, 1, 0.9F, PoppetColour.EQUIPMENT);
//	public static final Supplier<Item> TOOL_POPPET_INFUSED = registerItemProtectionPoppet("tool_poppet_infused", 0.0F, 1, 0.0F, PoppetColour.EQUIPMENT);
//	public static final Supplier<Item> TOOL_POPPET_STURDY = registerItemProtectionPoppet("tool_poppet_sturdy", 0.0F, 2, 0.9F, PoppetColour.EQUIPMENT);
//	public static final Supplier<Item> ARMOUR_POPPET = registerItemProtectionPoppet("armour_poppet", 0.3F, 1, 0.9F, PoppetColour.EQUIPMENT);
//	public static final Supplier<Item> ARMOUR_POPPET_INFUSED = registerItemProtectionPoppet("armour_poppet_infused", 0.0F, 1, 0.0F, PoppetColour.EQUIPMENT);
//	public static final Supplier<Item> ARMOUR_POPPET_STURDY = registerItemProtectionPoppet("armour_poppet_sturdy", 0.0F, 2, 0.9F, PoppetColour.EQUIPMENT);
//
//	public static final Supplier<Item> MANDRAKE_SPAWN_EGG = registerSpawnEgg("mandrake_spawn_egg", EnchantedEntityTypes.MANDRAKE::get, 0x634C39, 0x291A0E);
//	public static final Supplier<Item> ENT_SPAWN_EGG = registerSpawnEgg("ent_spawn_egg", EnchantedEntityTypes.ENT::get, 0x2A5422, 0x183812);



	private static <T extends Item> Supplier<T> register(String name, Supplier<T> itemSupplier) {
		return Services.COMMON_REGISTRY.register(Registry.ITEM, name, itemSupplier);
	}

	private static Properties defaultProperties() {
		return new Properties().tab(TAB);
	}

	private static Supplier<Item> registerItem(String name) {
		return register(name, () -> new Item(defaultProperties()));
	}

	private static Supplier<Item> registerItemNoTab(String name) {
		return register(name, () -> new Item(new Properties()));
	}

	private static Supplier<Item> registerItem(String name, Properties properties) {
		return register(name, () -> new Item(properties));
	}

//	private static Supplier<Item> registerSpawnEgg(String name, Supplier<EntityType<? extends Mob>> type, int backgroundColour, int highlightColour) {
//		return ITEMS.register(name, () -> new ForgeSpawnEggItem(type, backgroundColour, highlightColour, defaultProperties()));
//	}

	private static Supplier<Item> registerBlockItem(String name, Supplier<? extends Block> block) {
		return register(name, () -> new BlockItem(block.get(), defaultProperties()));
	}

	private static Supplier<Item> registerBlockItemProperties(String name, Supplier<? extends Block> block, Properties properties) {
		return register(name, () -> new BlockItem(block.get(), properties));
	}

	private static Supplier<Item> registerBlockNamedItem(String name, Supplier<? extends Block> block) {
		return register(name, () -> new ItemNameBlockItem(block.get(), defaultProperties()));
	}

	private static Supplier<Item> registerFoodItem(String name, int nutrition) {
		return register(name, () -> new Item(defaultProperties().food(new FoodProperties.Builder().nutrition(nutrition).build())));
	}

	private static Supplier<Item> registerFoodItem(String name, int nutrition, MobEffect effect, int duration, int amplification, float chance) {
		return register(name, () -> new Item(defaultProperties().food(new FoodProperties.Builder().nutrition(nutrition).effect(new MobEffectInstance(effect, duration, amplification), chance).build())));
	}

//	private static Supplier<Item> registerChalkItem(String name, Supplier<? extends Block> block, int durability) {
//		return ITEMS.register(name, () -> new ChalkItem(block.get(), defaultProperties().stacksTo(1).durability(durability)));
//	}

	private static Supplier<Item> registerSwordItem(String name, Tier tier, int attackDamageModifier, float attackSpeedModifier) {
		return register(name, () -> new SwordItem(tier, attackDamageModifier, attackSpeedModifier, defaultProperties()));
	}

	private static Supplier<Item> registerBrew(String name, MobEffect effect, int duration, int amplifier) {
		return register(name, () -> new SimpleEffectBrewItem(effect, duration, amplifier, defaultProperties()));
	}

//	private static Supplier<Item> registerItemProtectionPoppet(String name, float failRate, int durability, float damageMultiplier, PoppetColour colour) {
//		return ITEMS.register(name, () -> new ItemProtectionPoppetItem(failRate, durability, damageMultiplier, colour));
//	}

//	private static Supplier<Item> registerDeathPoppet(String name, float failRate, int durability, PoppetColour colour, DamageSource... damageSources) {
//		return ITEMS.register(name, () -> new DeathPoppetItem(failRate, durability, colour, source -> containsSource(damageSources, source)));
//	}

//	private static Supplier<Item> registerDeathPoppetEffect(String name, float failRate, int durability, PoppetColour colour, MobEffect effect, int duration, int amplifier, DamageSource... damageSources) {
//		return ITEMS.register(name, () -> new DeathPoppetEffectItem(failRate, durability, colour, source -> containsSource(damageSources, source), () -> new MobEffectInstance(effect, duration, amplifier)));
//	}

//	private static Supplier<Item> registerDeathPoppetEffect(String name, float failRate, int durability, PoppetColour colour, Supplier<MobEffect> effect, int duration, int amplifier, DamageSource... damageSources) {
//		return ITEMS.register(name, () -> new DeathPoppetEffectItem(failRate, durability, colour, source -> containsSource(damageSources, source), () -> new MobEffectInstance(effect.get(), duration, amplifier)));
//	}

//	private static Supplier<Item> registerVoidPoppet(String name, float failRate, int durability, PoppetColour colour, DamageSource... damageSources) {
//		return ITEMS.register(name, () -> new VoidPoppetItem(failRate, durability, colour, source -> containsSource(damageSources, source)));
//	}

//	private static Supplier<Item> registerVoidPoppetEffect(String name, float failRate, int durability, PoppetColour colour, Supplier<MobEffect> effect, int duration, int amplifier, DamageSource... damageSources) {
//		return ITEMS.register(name, () -> new VoidPoppetEffectItem(failRate, durability, colour, source -> containsSource(damageSources, source), () -> new MobEffectInstance(effect.get(), duration, amplifier)));
//	}

//	private static Supplier<Item> registerFirePoppet(String name, float failRate, int durability, PoppetColour colour, Predicate<DamageSource> sourcePredicate) {
//		return ITEMS.register(name, () -> new FirePoppetItem(failRate, durability, colour, sourcePredicate));
//	}

//	private static Supplier<Item> registerFirePoppetEffect(String name, float failRate, int durability, PoppetColour colour, MobEffect effect, int duration, int amplifier, Predicate<DamageSource> sourcePredicate) {
//		return ITEMS.register(name, () -> new FirePoppetEffectItem(failRate, durability, colour, sourcePredicate, () -> new MobEffectInstance(effect, duration, amplifier)));
//	}

//	private static boolean containsSource(DamageSource[] sources, DamageSource source) {
//		for(DamageSource i : sources) {
//			if(i == source)
//				return true;
//		}
//		return false;
//	}

//	public static boolean isToolPoppet(Item item) {
//		return item == TOOL_POPPET.get() || item == TOOL_POPPET_INFUSED.get() || item == TOOL_POPPET_STURDY.get();
//	}

//	public static  boolean isArmourPoppet(Item item) {
//		return item == ARMOUR_POPPET.get() || item == ARMOUR_POPPET_INFUSED.get() || item == ARMOUR_POPPET_STURDY.get();
//	}

//	public static void registerCompostables() {
//		ComposterBlock.COMPOSTABLES.put(ARTICHOKE_SEEDS.get(), 0.3F);
//		ComposterBlock.COMPOSTABLES.put(ARTICHOKE.get(), 0.65F);
//		ComposterBlock.COMPOSTABLES.put(SNOWBELL_SEEDS.get(), 0.3F);
//		ComposterBlock.COMPOSTABLES.put(BELLADONNA_SEEDS.get(), 0.3F);
//		ComposterBlock.COMPOSTABLES.put(BELLADONNA_FLOWER.get(), 0.65F);
//		ComposterBlock.COMPOSTABLES.put(MANDRAKE_SEEDS.get(), 0.3F);
//		ComposterBlock.COMPOSTABLES.put(MANDRAKE_ROOT.get(), 0.65F);
//		ComposterBlock.COMPOSTABLES.put(WOLFSBANE_SEEDS.get(), 0.3F);
//		ComposterBlock.COMPOSTABLES.put(WOLFSBANE_FLOWER.get(), 0.65F);
//		ComposterBlock.COMPOSTABLES.put(GARLIC.get(), 0.45F);
//	}



	public static final CreativeModeTab TAB = Services.COMMON_REGISTRY.registerTab("main", ATTUNED_STONE_CHARGED);
/*	public static final CreativeModeTab TAB = new CreativeModeTab(Enchanted.MOD_ID + ".main") {

		@Override
		public ItemStack makeIcon() {
			return new ItemStack(EnchantedItems.ATTUNED_STONE.get());
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
	};*/

	public static void load() {} // Method which exists purely to load the class.

}
