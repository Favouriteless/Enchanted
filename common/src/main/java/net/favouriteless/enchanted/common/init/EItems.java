package net.favouriteless.enchanted.common.init;

import net.favouriteless.enchanted.common.blocks.chalk.AbstractChalkBlock;
import net.favouriteless.enchanted.common.items.*;
import net.favouriteless.enchanted.common.items.brews.SimpleEffectBrewItem;
import net.favouriteless.enchanted.common.items.brews.throwable.LoveBrewItem;
import net.favouriteless.enchanted.common.items.component.EDataComponents;
import net.favouriteless.enchanted.common.items.poppets.*;
import net.favouriteless.enchanted.common.enchanted.poppet.PoppetColour;
import net.favouriteless.enchanted.platform.EServices;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ArmorItem.Type;
import net.minecraft.world.item.*;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ComposterBlock;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class EItems {

	public static final Supplier<BlockItem> ALDER_BUTTON = registerBlock("alder_button", EBlocks.ALDER_BUTTON);
	public static final Supplier<BlockItem> ALDER_FENCE = registerBlock("alder_fence", EBlocks.ALDER_FENCE);
	public static final Supplier<BlockItem> ALDER_FENCE_GATE = registerBlock("alder_fence_gate", EBlocks.ALDER_FENCE_GATE);
	public static final Supplier<BlockItem> ALDER_LEAVES = registerBlock("alder_leaves", EBlocks.ALDER_LEAVES);
	public static final Supplier<BlockItem> ALDER_LOG = registerBlock("alder_log", EBlocks.ALDER_LOG);
	public static final Supplier<BlockItem> ALDER_PLANKS = registerBlock("alder_planks", EBlocks.ALDER_PLANKS);
	public static final Supplier<BlockItem> ALDER_PRESSURE_PLATE = registerBlock("alder_pressure_plate", EBlocks.ALDER_PRESSURE_PLATE);
	public static final Supplier<BlockItem> ALDER_SAPLING = registerBlock("alder_sapling", EBlocks.ALDER_SAPLING);
	public static final Supplier<BlockItem> ALDER_SLAB = registerBlock("alder_slab", EBlocks.ALDER_SLAB);
	public static final Supplier<BlockItem> ALDER_STAIRS = registerBlock("alder_stairs", EBlocks.ALDER_STAIRS);
	public static final Supplier<BlockItem> ALTAR = registerBlock("altar", EBlocks.ALTAR);
	public static final Supplier<Item> ANOINTING_PASTE = register("anointing_paste", () -> new AnointingPasteItem(props()));
	public static final Supplier<Item> ARMOUR_POPPET = register("armour_poppet", () -> new ItemProtectionPoppetItem(0.9F, PoppetColour.EQUIPMENT, poppetProps(1)));
	public static final Supplier<Item> ARMOUR_POPPET_INFUSED = register("infused_armour_poppet", () -> new ItemProtectionPoppetItem(0.0F, PoppetColour.EQUIPMENT, poppetProps(1)));
	public static final Supplier<Item> ARMOUR_POPPET_STURDY = register("sturdy_armour_poppet", () -> new ItemProtectionPoppetItem(0.9F, PoppetColour.EQUIPMENT, poppetProps(2)));
	public static final Supplier<SwordItem> ARTHANA = registerSword("arthana", Tiers.GOLD, 3, -2.4F);
	public static final Supplier<Item> ATTUNED_STONE = registerItem("attuned_stone");
	public static final Supplier<Item> ATTUNED_STONE_CHARGED = registerItem("attuned_stone_charged", props().component(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true).rarity(Rarity.UNCOMMON));
	public static final Supplier<Item> BELLADONNA_FLOWER = registerItem("belladonna_flower");
	public static final Supplier<ItemNameBlockItem> BELLADONNA_SEEDS = registerBlockNamed("belladonna_seeds", EBlocks.BELLADONNA);
	public static final Supplier<BloodedWaystoneItem> BLOODED_WAYSTONE = register("blooded_waystone", () -> new BloodedWaystoneItem(props()));
	public static final Supplier<BoundWaystoneItem> BOUND_WAYSTONE = register("bound_waystone", () -> new BoundWaystoneItem(props()));
	public static final Supplier<BlockItem> BLOOD_POPPY = registerBlock("blood_poppy", EBlocks.BLOOD_POPPY, props().rarity(Rarity.UNCOMMON));
	public static final Supplier<Item> BONE_NEEDLE = registerItem("bone_needle");
	public static final Supplier<Item> BREATH_OF_THE_GODDESS = registerItem("breath_of_the_goddess");
	public static final Supplier<Item> BREW_OF_LOVE = register("brew_of_love", LoveBrewItem::new);
	public static final Supplier<Item> BREW_OF_SPROUTING = registerItem("brew_of_sprouting");
	public static final Supplier<SimpleEffectBrewItem> BREW_OF_THE_DEPTHS = registerBrew("brew_of_the_depths", () -> MobEffects.WATER_BREATHING, 12000, 0);
	public static final Supplier<SimpleEffectBrewItem> BREW_OF_THE_GROTESQUE = registerBrew("brew_of_the_grotesque", () -> EMobEffects.GROTESQUE, 6000, 0);
	public static final Supplier<Item> BROOM = register("broom", () -> new BroomItem(props().stacksTo(1)));
	public static final Supplier<BlockItem> CANDELABRA = registerBlock("candelabra", EBlocks.CANDELABRA);
	public static final Supplier<BlockItem> CHALICE = registerBlock("chalice", EBlocks.CHALICE);
	public static final Supplier<BlockItem> CHALICE_FILLED = registerBlock("chalice_filled", EBlocks.CHALICE_FILLED, props().rarity(Rarity.UNCOMMON));
	public static final Supplier<Item> CIRCLE_TALISMAN = register("circle_talisman", () -> new CircleTalismanItem(props().stacksTo(1).component(EDataComponents.CIRCLE_MAGIC_SHAPE_MAP.get(), new HashMap<>())));
	public static final Supplier<Item> CLAY_JAR = registerItem("clay_jar");
	public static final Supplier<Item> CONDENSED_FEAR = registerItem("condensed_fear");
	public static final Supplier<Item> CREEPER_HEART = register("creeper_heart", () -> new CreeperHeartItem(props()));
	public static final Supplier<Item> DEMONIC_BLOOD = registerItem("demonic_blood");
	public static final Supplier<Item> DEMON_HEART = registerItem("demon_heart");
	public static final Supplier<Item> DIAMOND_VAPOUR = registerItem("diamond_vapour");
	public static final Supplier<BlockItem> DISTILLERY = registerBlock("distillery", EBlocks.DISTILLERY);
	public static final Supplier<Item> DROP_OF_LUCK = registerItem("drop_of_luck");
	public static final Supplier<EarmuffsItem> EARMUFFS = register("earmuffs", () -> new EarmuffsItem(ArmorMaterials.LEATHER, Type.HELMET, props().stacksTo(1).rarity(Rarity.RARE)));
	public static final Supplier<Item> EARTH_POPPET = register("earth_poppet", () -> new DeathPoppetItem(PoppetColour.EARTH, source -> source.is(DamageTypeTags.IS_FALL) || source.is(DamageTypes.FLY_INTO_WALL), poppetProps(1)));
	public static final Supplier<Item> EARTH_POPPET_INFUSED = register("infused_earth_poppet", () -> new DeathPoppetEffectItem(PoppetColour.EARTH, () -> new MobEffectInstance(EMobEffects.FALL_RESISTANCE, 200), source -> source.is(DamageTypeTags.IS_FALL) || source.is(DamageTypes.FLY_INTO_WALL), poppetProps(1)));
	public static final Supplier<Item> EARTH_POPPET_STURDY = register("sturdy_earth_poppet", () -> new DeathPoppetItem(PoppetColour.EARTH, source -> source.is(DamageTypeTags.IS_FALL) || source.is(DamageTypes.FLY_INTO_WALL), poppetProps(2)));
	public static final Supplier<BlockItem> EMBER_MOSS = registerBlock("ember_moss", EBlocks.EMBER_MOSS);
	public static final Supplier<Item> ENCHANTED_BROOMSTICK = register("enchanted_broomstick", () -> new BroomstickItem(props().stacksTo(1).rarity(Rarity.EPIC)));
	public static final Supplier<Item> ENDER_DEW = registerItem("ender_dew");
	public static final Supplier<Item> ENT_TWIG = registerItem("ent_twig");
	public static final Supplier<Item> EXHALE_OF_THE_HORNED_ONE = registerItem("exhale_of_the_horned_one");
	public static final Supplier<Item> FIRE_POPPET = register("fire_poppet", () -> new FirePoppetItem(PoppetColour.FIRE, source -> source.is(DamageTypeTags.IS_FIRE), poppetProps(1)));
	public static final Supplier<Item> FIRE_POPPET_INFUSED = register("infused_fire_poppet", () -> new FirePoppetEffectItem(PoppetColour.FIRE, () -> new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 200), source -> source.is(DamageTypeTags.IS_FIRE), poppetProps(1)));
	public static final Supplier<Item> FIRE_POPPET_STURDY =  register("sturdy_fire_poppet", () -> new FirePoppetItem(PoppetColour.FIRE, source -> source.is(DamageTypeTags.IS_FIRE), poppetProps(2)));
	public static final Supplier<SimpleEffectBrewItem> FLYING_OINTMENT = registerBrew("flying_ointment", () -> MobEffects.LEVITATION, 1200, 0);
	public static final Supplier<Item> FOCUSED_WILL = registerItem("focused_will");
	public static final Supplier<Item> FOUL_FUME = registerItem("foul_fume");
	public static final Supplier<Item> FUME_FILTER = registerItem("fume_filter");
	public static final Supplier<BlockItem> FUME_FUNNEL = registerBlock("fume_funnel", EBlocks.FUME_FUNNEL);
	public static final Supplier<BlockItem> FUME_FUNNEL_FILTERED = registerBlock("fume_funnel_filtered", EBlocks.FUME_FUNNEL_FILTERED);
	public static final Supplier<ItemNameBlockItem> GARLIC = registerBlockNamed("garlic", EBlocks.GARLIC);
	public static final Supplier<SimpleEffectBrewItem> GHOST_OF_THE_LIGHT = registerBrew("ghost_of_the_light", () -> MobEffects.POISON, 1200, 1);
	public static final Supplier<BlockItem> GLINT_WEED = registerBlock("glint_weed", EBlocks.GLINT_WEED);
	public static final Supplier<ChalkItem> GOLDEN_CHALK = registerChalk("golden_chalk", EBlocks.GOLDEN_CHALK, 3);
	public static final Supplier<Item> GYPSUM = registerItem("gypsum");
	public static final Supplier<SimpleEffectBrewItem> HAPPENSTANCE_OIL = registerBrew("happenstance_oil", () -> MobEffects.NIGHT_VISION, 1200, 0);
	public static final Supplier<BlockItem> HAWTHORN_BUTTON = registerBlock("hawthorn_button", EBlocks.HAWTHORN_BUTTON);
	public static final Supplier<BlockItem> HAWTHORN_FENCE = registerBlock("hawthorn_fence", EBlocks.HAWTHORN_FENCE);
	public static final Supplier<BlockItem> HAWTHORN_FENCE_GATE = registerBlock("hawthorn_fence_gate", EBlocks.HAWTHORN_FENCE_GATE);
	public static final Supplier<BlockItem> HAWTHORN_LEAVES = registerBlock("hawthorn_leaves", EBlocks.HAWTHORN_LEAVES);
	public static final Supplier<BlockItem> HAWTHORN_LOG = registerBlock("hawthorn_log", EBlocks.HAWTHORN_LOG);
	public static final Supplier<BlockItem> HAWTHORN_PLANKS = registerBlock("hawthorn_planks", EBlocks.HAWTHORN_PLANKS);
	public static final Supplier<BlockItem> HAWTHORN_PRESSURE_PLATE = registerBlock("hawthorn_pressure_plate", EBlocks.HAWTHORN_PRESSURE_PLATE);
	public static final Supplier<BlockItem> HAWTHORN_SAPLING = registerBlock("hawthorn_sapling", EBlocks.HAWTHORN_SAPLING);
	public static final Supplier<BlockItem> HAWTHORN_SLAB = registerBlock("hawthorn_slab", EBlocks.HAWTHORN_SLAB);
	public static final Supplier<BlockItem> HAWTHORN_STAIRS = registerBlock("hawthorn_stairs", EBlocks.HAWTHORN_STAIRS);
	public static final Supplier<Item> HINT_OF_REBIRTH = registerItem("hint_of_rebirth");
	public static final Supplier<Item> HUNGER_POPPET = register("hunger_poppet", () -> new DeathPoppetEffectItem(PoppetColour.HUNGER, () -> new MobEffectInstance(MobEffects.SATURATION, 100, 4), source -> source.is(DamageTypes.STARVE), poppetProps(1)));
	public static final Supplier<Item> HUNGER_POPPET_INFUSED = register("infused_hunger_poppet", () -> new DeathPoppetEffectItem(PoppetColour.HUNGER, () -> new MobEffectInstance(MobEffects.SATURATION, 2400, 4), source -> source.is(DamageTypes.STARVE), poppetProps(1)));
	public static final Supplier<Item> HUNGER_POPPET_STURDY = register("sturdy_hunger_poppet", () -> new DeathPoppetItem(PoppetColour.HUNGER, source -> source.is(DamageTypes.STARVE), poppetProps(2)));
	public static final Supplier<Item> ICY_NEEDLE = registerItem("icy_needle");
	public static final Supplier<SimpleEffectBrewItem> INFERNAL_ANIMUS = registerBrew("infernal_animus", () -> MobEffects.WITHER, 1200, 2);
	public static final Supplier<BlockItem> INFINITY_EGG = registerBlock("infinity_egg", EBlocks.INFINITY_EGG);
	public static final Supplier<BlockItem> KETTLE = registerBlock("kettle", EBlocks.KETTLE);
	public static final Supplier<DeathPoppetItem> MAGIC_POPPET = register("magic_poppet", () -> new DeathPoppetItem(PoppetColour.MAGIC, EMobEffects::isMagic, poppetProps(1)));
	public static final Supplier<DeathPoppetEffectItem> MAGIC_POPPET_INFUSED = register("infused_magic_poppet", () -> new DeathPoppetEffectItem(PoppetColour.MAGIC, () -> new MobEffectInstance(EMobEffects.MAGIC_RESISTANCE, 200), EMobEffects::isMagic, poppetProps(1)));
	public static final Supplier<DeathPoppetItem> MAGIC_POPPET_STURDY = register("sturdy_magic_poppet", () -> new DeathPoppetItem(PoppetColour.MAGIC, EMobEffects::isMagic, poppetProps(2)));
	public static final Supplier<Item> MANDRAKE_ROOT = registerItem("mandrake_root");
	public static final Supplier<ItemNameBlockItem> MANDRAKE_SEEDS = registerBlockNamed("mandrake_seeds", EBlocks.MANDRAKE);
	public static final Supplier<Item> MELLIFLUOUS_HUNGER = registerItem("mellifluous_hunger");
	public static final Supplier<Item> MUTANDIS = register("mutandis", () -> new MutandisItem(false));
	public static final Supplier<Item> MUTANDIS_EXTREMIS = register("mutandis_extremis", () -> new MutandisItem(true));
	public static final Supplier<SimpleEffectBrewItem> MYSTIC_UNGUENT = registerBrew("mystic_unguent", () -> MobEffects.WEAKNESS, 1200, 1);
	public static final Supplier<ChalkItem> NETHER_CHALK = registerChalk("nether_chalk", EBlocks.NETHER_CHALK, 40);
	public static final Supplier<Item> ODOUR_OF_PURITY = registerItem("odour_of_purity");
	public static final Supplier<Item> OIL_OF_VITRIOL = registerItem("oil_of_vitriol");
	public static final Supplier<ChalkItem> OTHERWHERE_CHALK = registerChalk("otherwhere_chalk", EBlocks.OTHERWHERE_CHALK, 40);
	public static final Supplier<Item> POPPET = registerItem("poppet");
	public static final Supplier<Item> POPPET_INFUSED = registerItem("infused_poppet");
	public static final Supplier<BlockItem> POPPET_SHELF = registerBlock("poppet_shelf", EBlocks.POPPET_SHELF);
	public static final Supplier<Item> POPPET_STURDY = registerItem("sturdy_poppet");
	public static final Supplier<Item> PURIFIED_MILK = registerItem("purified_milk");
	public static final Supplier<Item> QUICKLIME = registerItem("quicklime");
	public static final Supplier<SimpleEffectBrewItem> REDSTONE_SOUP = registerBrew("redstone_soup", () -> MobEffects.ABSORPTION, 2400, 1);
	public static final Supplier<Item> REEK_OF_MISFORTUNE = registerItem("reek_of_misfortune");
	public static final Supplier<Item> REFINED_EVIL = registerItem("refined_evil");
	public static final Supplier<ChalkItem> RITUAL_CHALK = registerChalk("ritual_chalk", EBlocks.RITUAL_CHALK, 40);
	public static final Supplier<Item> ROWAN_BERRIES = registerFood("rowan_berries", 3, true);
	public static final Supplier<BlockItem> ROWAN_BUTTON = registerBlock("rowan_button", EBlocks.ROWAN_BUTTON);
	public static final Supplier<BlockItem> ROWAN_FENCE = registerBlock("rowan_fence", EBlocks.ROWAN_FENCE);
	public static final Supplier<BlockItem> ROWAN_FENCE_GATE = registerBlock("rowan_fence_gate", EBlocks.ROWAN_FENCE_GATE);
	public static final Supplier<BlockItem> ROWAN_LEAVES = registerBlock("rowan_leaves", EBlocks.ROWAN_LEAVES);
	public static final Supplier<BlockItem> ROWAN_LOG = registerBlock("rowan_log", EBlocks.ROWAN_LOG);
	public static final Supplier<BlockItem> ROWAN_PLANKS = registerBlock("rowan_planks", EBlocks.ROWAN_PLANKS);
	public static final Supplier<BlockItem> ROWAN_PRESSURE_PLATE = registerBlock("rowan_pressure_plate", EBlocks.ROWAN_PRESSURE_PLATE);
	public static final Supplier<BlockItem> ROWAN_SAPLING = registerBlock("rowan_sapling", EBlocks.ROWAN_SAPLING);
	public static final Supplier<BlockItem> ROWAN_SLAB = registerBlock("rowan_slab", EBlocks.ROWAN_SLAB);
	public static final Supplier<BlockItem> ROWAN_STAIRS = registerBlock("rowan_stairs", EBlocks.ROWAN_STAIRS);
	public static final Supplier<ItemNameBlockItem> SNOWBELL_SEEDS = registerBlockNamed("snowbell_seeds", EBlocks.SNOWBELL);
	public static final Supplier<Item> SOFT_CLAY_JAR = registerItem("soft_clay_jar");
	public static final Supplier<SimpleEffectBrewItem> SOUL_OF_THE_WORLD = registerBrew("soul_of_the_world", () -> MobEffects.POISON, 1200, 1);
	public static final Supplier<BlockItem> SPANISH_MOSS = registerBlock("spanish_moss", EBlocks.SPANISH_MOSS);
	public static final Supplier<Item> SPINNING_WHEEL = register("spinning_wheel", () -> new BlockItem(EBlocks.SPINNING_WHEEL.get(), props()));
	public static final Supplier<SimpleEffectBrewItem> SPIRIT_OF_OTHERWHERE = registerBrew("spirit_of_otherwhere", () -> MobEffects.POISON, 1200, 1);
	public static final Supplier<BlockItem> STRIPPED_ALDER_LOG = registerBlock("stripped_alder_log", EBlocks.STRIPPED_ALDER_LOG);
	public static final Supplier<BlockItem> STRIPPED_HAWTHORN_LOG = registerBlock("stripped_hawthorn_log", EBlocks.STRIPPED_HAWTHORN_LOG);
	public static final Supplier<BlockItem> STRIPPED_ROWAN_LOG = registerBlock("stripped_rowan_log", EBlocks.STRIPPED_ROWAN_LOG);
	public static final Supplier<Item> TAGLOCK = register("taglock_kit", () -> new EmptyTaglockItem(props()));
	public static final Supplier<Item> TAGLOCK_FILLED =  register("taglock", () -> new TaglockFilledItem(props()));
	public static final Supplier<Item> TEAR_OF_THE_GODDESS = registerItem("tear_of_the_goddess");
	public static final Supplier<Item> TONGUE_OF_DOG = registerItem("tongue_of_dog");
	public static final Supplier<Item> TOOL_POPPET = register("tool_poppet", () -> new ItemProtectionPoppetItem(0.9F, PoppetColour.EQUIPMENT, poppetProps(1)));
	public static final Supplier<Item> TOOL_POPPET_INFUSED = register("infused_tool_poppet", () -> new ItemProtectionPoppetItem(0, PoppetColour.EQUIPMENT, poppetProps(1)));
	public static final Supplier<Item> TOOL_POPPET_STURDY = register("sturdy_tool_poppet", () -> new ItemProtectionPoppetItem(0.9F, PoppetColour.EQUIPMENT, poppetProps(2)));
	public static final Supplier<VoidPoppetItem> VOID_POPPET = register("void_poppet", () -> new VoidPoppetItem(PoppetColour.VOID, source -> source.is(DamageTypes.FELL_OUT_OF_WORLD), poppetProps(1)));
	public static final Supplier<VoidPoppetEffectItem> VOID_POPPET_INFUSED = register("infused_void_poppet", () -> new VoidPoppetEffectItem(PoppetColour.VOID, () -> new MobEffectInstance(EMobEffects.FALL_RESISTANCE, 200), source -> source.is(DamageTypes.FELL_OUT_OF_WORLD), poppetProps(1)));
	public static final Supplier<VoidPoppetItem> VOID_POPPET_STURDY = register("sturdy_void_poppet", () -> new VoidPoppetItem(PoppetColour.VOID, source -> source.is(DamageTypes.FELL_OUT_OF_WORLD), poppetProps(2)));
	public static final Supplier<VoodooPoppetItem> VOODOO_POPPET = register("voodoo_poppet", () -> new VoodooPoppetItem(props().stacksTo(1).durability(40).fireResistant()));
	public static final Supplier<Item> VOODOO_PROTECTION_POPPET = register("voodoo_protection_poppet", () -> new PoppetItem(PoppetColour.VOODOO_PROTECTION, poppetProps(1)));
	public static final Supplier<Item> VOODOO_PROTECTION_POPPET_INFUSED = register("infused_voodoo_protection_poppet", () -> new PoppetItem(PoppetColour.VOODOO_PROTECTION, poppetProps(1)));
	public static final Supplier<Item> VOODOO_PROTECTION_POPPET_STURDY = register("sturdy_voodoo_protection_poppet", () -> new PoppetItem(PoppetColour.VOODOO_PROTECTION, poppetProps(2)));
	public static final Supplier<Item> WATER_ARTICHOKE = registerFood("water_artichoke", 3, MobEffects.HUNGER, 100, 0, 1.0F);
	public static final Supplier<ArtichokeSeedsItem> WATER_ARTICHOKE_SEEDS = register("water_artichoke_seeds", () -> new ArtichokeSeedsItem(props()));
	public static final Supplier<DeathPoppetEffectItem> WATER_POPPET = register("water_poppet", () -> new DeathPoppetEffectItem(PoppetColour.WATER, () -> new MobEffectInstance(EMobEffects.DROWN_RESISTANCE, 100), source -> source.is(DamageTypeTags.IS_DROWNING), poppetProps(1)));
	public static final Supplier<DeathPoppetEffectItem> WATER_POPPET_INFUSED = register("infused_water_poppet", () -> new DeathPoppetEffectItem(PoppetColour.WATER, () -> new MobEffectInstance(MobEffects.WATER_BREATHING, 200), source -> source.is(DamageTypeTags.IS_DROWNING), poppetProps(1)));
	public static final Supplier<DeathPoppetEffectItem> WATER_POPPET_STURDY = register("sturdy_water_poppet", () -> new DeathPoppetEffectItem(PoppetColour.WATER, () -> new MobEffectInstance(EMobEffects.DROWN_RESISTANCE, 100), source -> source.is(DamageTypeTags.IS_DROWNING), poppetProps(2)));
	public static final Supplier<WaystoneItem> WAYSTONE = register("waystone", () -> new WaystoneItem(props()));
	public static final Supplier<Item> WHIFF_OF_MAGIC = registerItem("whiff_of_magic");
	public static final Supplier<BlockItem> WICKER_BUNDLE = registerBlock("wicker_bundle", EBlocks.WICKER_BUNDLE);
	public static final Supplier<BlockItem> WITCH_CAULDRON = registerBlock("witch_cauldron", EBlocks.WITCH_CAULDRON);
	public static final Supplier<BlockItem> WITCH_OVEN = registerBlock("witch_oven", EBlocks.WITCH_OVEN);
	public static final Supplier<Item> WOLFSBANE_FLOWER = registerItem("wolfsbane_flower");
	public static final Supplier<ItemNameBlockItem> WOLFSBANE_SEEDS = registerBlockNamed("wolfsbane_seeds", EBlocks.WOLFSBANE);
	public static final Supplier<Item> WOOD_ASH = registerItem("wood_ash");
	public static final Supplier<Item> WOOL_OF_BAT = registerItem("wool_of_bat");


//	public static final Supplier<Item> MANDRAKE_SPAWN_EGG = registerSpawnEgg("mandrake_spawn_egg", EnchantedEntityTypes.MANDRAKE::get, 0x634C39, 0x291A0E);
//	public static final Supplier<Item> ENT_SPAWN_EGG = registerSpawnEgg("ent_spawn_egg", EnchantedEntityTypes.ENT::get, 0x2A5422, 0x183812);



	private static <T extends Item> Supplier<T> register(String name, Supplier<T> itemSupplier) {
		return EServices.REGISTRY.register(BuiltInRegistries.ITEM, name, itemSupplier);
	}

	private static Supplier<Item> registerItem(String name) {
		return register(name, () -> new Item(new Properties()));
	}

	private static Supplier<Item> registerItem(String name, Properties properties) {
		return register(name, () -> new Item(properties));
	}

	private static Supplier<BlockItem> registerBlock(String name, Supplier<? extends Block> block) {
		return register(name, () -> new BlockItem(block.get(), new Properties()));
	}

	private static Supplier<BlockItem> registerBlock(String name, Supplier<? extends Block> block, Properties properties) {
		return register(name, () -> new BlockItem(block.get(), properties));
	}

	private static Supplier<ItemNameBlockItem> registerBlockNamed(String name, Supplier<? extends Block> block) {
		return register(name, () -> new ItemNameBlockItem(block.get(), new Properties()));
	}

	private static Supplier<Item> registerFood(String name, int nutrition, boolean fast) {
		FoodProperties.Builder builder = new FoodProperties.Builder().nutrition(nutrition);
		if(fast)
			builder.fast();
		return register(name, () -> new Item(new Properties().food(builder.build())));
	}

	private static Supplier<Item> registerFood(String name, int nutrition, Holder<MobEffect> effect, int duration, int amplification, float chance) {
		return register(name, () -> new Item(new Properties().food(new FoodProperties.Builder().nutrition(nutrition).effect(new MobEffectInstance(effect, duration, amplification), chance).build())));
	}

	private static Supplier<ChalkItem> registerChalk(String name, Supplier<? extends AbstractChalkBlock> block, int durability) {
		return register(name, () -> new ChalkItem(block.get(), new Properties().stacksTo(1).durability(durability)));
	}

	private static Supplier<SwordItem> registerSword(String name, Tier tier, int attackDamage, float attackSpeed) {
		return register(name, () -> new SwordItem(tier, new Properties().attributes(SwordItem.createAttributes(tier, attackDamage, attackSpeed))));
	}

	private static Supplier<SimpleEffectBrewItem> registerBrew(String name, Supplier<Holder<MobEffect>> effect, int duration, int amplifier) {
		return register(name, () -> new SimpleEffectBrewItem(effect, duration, amplifier, new Properties()));
	}

	public static boolean isToolPoppet(Item item) {
		return item == TOOL_POPPET.get() || item == TOOL_POPPET_INFUSED.get() || item == TOOL_POPPET_STURDY.get();
	}

	public static boolean isArmourPoppet(Item item) {
		return item == ARMOUR_POPPET.get() || item == ARMOUR_POPPET_INFUSED.get() || item == ARMOUR_POPPET_STURDY.get();
	}

	public static boolean isVoodooProtectionPoppet(Item item) {
		return item == VOODOO_PROTECTION_POPPET.get() || item == VOODOO_PROTECTION_POPPET_INFUSED.get() || item == VOODOO_PROTECTION_POPPET_STURDY.get();
	}

	public static void registerCompostables() {
		// See ECompostMapProvider in data generation for Neoforge registration, as Neoforge does not use this map
		ComposterBlock.COMPOSTABLES.put(WATER_ARTICHOKE_SEEDS.get(), 0.3F);
		ComposterBlock.COMPOSTABLES.put(WATER_ARTICHOKE.get(), 0.65F);
		ComposterBlock.COMPOSTABLES.put(SNOWBELL_SEEDS.get(), 0.3F);
		ComposterBlock.COMPOSTABLES.put(BELLADONNA_SEEDS.get(), 0.3F);
		ComposterBlock.COMPOSTABLES.put(BELLADONNA_FLOWER.get(), 0.65F);
		ComposterBlock.COMPOSTABLES.put(MANDRAKE_SEEDS.get(), 0.3F);
		ComposterBlock.COMPOSTABLES.put(MANDRAKE_ROOT.get(), 0.65F);
		ComposterBlock.COMPOSTABLES.put(WOLFSBANE_SEEDS.get(), 0.3F);
		ComposterBlock.COMPOSTABLES.put(WOLFSBANE_FLOWER.get(), 0.65F);
		ComposterBlock.COMPOSTABLES.put(GARLIC.get(), 0.45F);
	}

	public static void registerFuel(Map<Item, Integer> map) {

	}

	public static Properties props() {
		return new Properties();
	}

	public static Properties poppetProps(int durability) {
		return props().durability(durability).stacksTo(1);
	}

	public static void load() {} // Method which exists purely to load the class.

}
