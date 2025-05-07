package net.favouriteless.enchanted.datagen.providers;

import net.favouriteless.enchanted.common.init.EnchantedTags;
import net.favouriteless.enchanted.common.init.registry.EItems;
import net.favouriteless.enchanted.datagen.builders.recipe.ByproductRecipeBuilder;
import net.favouriteless.enchanted.datagen.builders.recipe.CauldronTypeRecipeBuilder;
import net.favouriteless.enchanted.datagen.builders.recipe.DistillingRecipeBuilder;
import net.favouriteless.enchanted.datagen.builders.recipe.SpinningRecipeBuilder;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.crafting.StrictNBTIngredient;

import java.util.function.Consumer;

public class RecipeProvider extends net.minecraft.data.recipes.RecipeProvider {

	public RecipeProvider(PackOutput output) {
		super(output);
	}

	@Override
	protected void buildRecipes(Consumer<FinishedRecipe> consumer) {
		buildShapedRecipes(consumer);
		buildShapelessRecipes(consumer);
		buildSmeltingRecipes(consumer);
		buildByproductRecipes(consumer);
		buildSpinningRecipes(consumer);
		buildDistillingRecipes(consumer);
		buildCauldronRecipes(consumer);
		buildKettleRecipes(consumer);
	}

	protected void buildShapedRecipes(Consumer<FinishedRecipe> consumer) {
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, EItems.ALTAR.get())
				.pattern("bwe")
				.pattern("srs")
				.pattern("srs")
				.define('s', Items.STONE_BRICKS).define('r', EItems.ROWAN_LOG.get())
				.define('b', EItems.BREATH_OF_THE_GODDESS.get())
				.define('e', EItems.EXHALE_OF_THE_HORNED_ONE.get())
				.define('w', StrictNBTIngredient.of(Items.POTION.getDefaultInstance()))
				.unlockedBy(getHasName(EItems.ROWAN_LOG.get()), has(EItems.ROWAN_LOG.get())).save(consumer);
		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, EItems.ARTHANA.get())
				.pattern(" i ")
				.pattern("nen")
				.pattern(" s ")
				.define('i', Items.GOLD_INGOT).define('n', Items.GOLD_NUGGET)
				.define('e', Items.EMERALD).define('s', Items.STICK)
				.unlockedBy(getHasName(Items.GOLD_INGOT), has(Items.GOLD_INGOT)).save(consumer);
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, EItems.ATTUNED_STONE.get())
				.pattern("w")
				.pattern("d")
				.pattern("l")
				.define('w', EItems.WHIFF_OF_MAGIC.get()).define('d', Items.DIAMOND)
				.define('l', Items.LAVA_BUCKET)
				.unlockedBy(getHasName(EItems.WHIFF_OF_MAGIC.get()), has(EItems.WHIFF_OF_MAGIC.get())).save(consumer);
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, EItems.BROOM.get())
				.pattern(" s ")
				.pattern(" s ")
				.pattern("hhh")
				.define('s', Items.STICK).define('h', EItems.HAWTHORN_SAPLING.get())
				.unlockedBy(getHasName(EItems.HAWTHORN_SAPLING.get()), has(EItems.HAWTHORN_SAPLING.get())).save(consumer);
		ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, EItems.CANDELABRA.get())
				.pattern("ccc")
				.pattern("iai")
				.pattern(" i ")
				.define('c', Items.CANDLE).define('i', Items.IRON_INGOT)
				.define('a', EItems.ATTUNED_STONE.get())
				.unlockedBy("has_attuned_stone", has(EItems.ATTUNED_STONE.get())).save(consumer);
		ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, EItems.CHALICE.get())
				.pattern("nan")
				.pattern("ngn")
				.pattern(" g ")
				.define('n', Items.GOLD_NUGGET).define('g', Items.GOLD_INGOT)
				.define('a', EItems.ATTUNED_STONE.get())
				.unlockedBy(getHasName(EItems.ATTUNED_STONE.get()), has(EItems.ATTUNED_STONE.get())).save(consumer);
		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, EItems.RITUAL_CHALK.get(), 2)
				.pattern("ata")
				.pattern("aga")
				.pattern("aga")
				.define('g', EItems.GYPSUM.get())
				.define('t', EItems.TEAR_OF_THE_GODDESS.get())
				.define('a', EItems.WOOD_ASH.get())
				.unlockedBy(getHasName(EItems.TEAR_OF_THE_GODDESS.get()), has(EItems.TEAR_OF_THE_GODDESS.get())).save(consumer);
		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, EItems.CIRCLE_TALISMAN.get())
				.pattern("ngn").pattern("gag")
				.pattern("ngn").define('n', Items.GOLD_NUGGET)
				.define('g', Items.GOLD_INGOT).define('a', EItems.ATTUNED_STONE.get())
				.unlockedBy(getHasName(EItems.ATTUNED_STONE.get()), has(EItems.ATTUNED_STONE.get())).save(consumer);
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, EItems.SOFT_CLAY_JAR.get(), 4)
				.pattern(" c ")
				.pattern("ccc")
				.define('c', Items.CLAY_BALL)
				.unlockedBy(getHasName(Items.CLAY_BALL), has(Items.CLAY_BALL)).save(consumer);
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, EItems.DISTILLERY.get())
				.pattern("cic")
				.pattern("iii")
				.pattern("gag")
				.define('i', Items.IRON_INGOT).define('g', Items.GOLD_NUGGET)
				.define('c', EItems.CLAY_JAR.get())
				.define('a', EItems.ATTUNED_STONE.get())
				.unlockedBy(getHasName(EItems.ATTUNED_STONE.get()), has(EItems.ATTUNED_STONE.get())).save(consumer);
		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, EItems.EARMUFFS.get())
				.pattern(" i ")
				.pattern("whw")
				.define('i', Items.IRON_INGOT).define('w', ItemTags.WOOL)
				.define('h', Items.LEATHER_HELMET)
				.unlockedBy("has_wool", has(ItemTags.WOOL))
				.unlockedBy(getHasName(Items.LEATHER_HELMET), has(Items.LEATHER_HELMET)).save(consumer);
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, EItems.FUME_FILTER.get())
				.pattern("ggg")
				.pattern("iai")
				.pattern("ggg")
				.define('g', Items.GLASS).define('i', Items.IRON_INGOT)
				.define('a', EItems.ATTUNED_STONE.get())
				.unlockedBy(getHasName(EItems.ATTUNED_STONE.get()), has(EItems.ATTUNED_STONE.get())).save(consumer);
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, EItems.FUME_FUNNEL.get())
				.pattern("blb")
				.pattern("bgb")
				.pattern("ifi")
				.define('g', Items.GLOWSTONE).define('i', Items.IRON_BLOCK)
				.define('b', Items.BUCKET).define('l', Items.LAVA_BUCKET)
				.define('f', Items.IRON_BARS)
				.unlockedBy(getHasName(Items.GLOWSTONE), has(Items.GLOWSTONE)).save(consumer);
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, EItems.KETTLE.get())
				.pattern("wsw")
				.pattern("sas")
				.pattern(" c ").define('w', Items.STICK)
				.define('s', Items.STRING).define('c', Items.CAULDRON
				).define('a', EItems.ATTUNED_STONE.get())
				.unlockedBy(getHasName(EItems.ATTUNED_STONE.get()), has(EItems.ATTUNED_STONE.get())).save(consumer);
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, EItems.POPPET.get())
				.pattern("wmw")
				.pattern("bms")
				.pattern("waw")
				.define('w', ItemTags.WOOL).define('s', Items.STRING)
				.define('m', EItems.SPANISH_MOSS.get())
				.define('b', EItems.BONE_NEEDLE.get())
				.define('a', EItems.ATTUNED_STONE.get())
				.unlockedBy(getHasName(EItems.ATTUNED_STONE.get()), has(EItems.ATTUNED_STONE.get())).save(consumer);
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, EItems.POPPET_SHELF.get())
				.pattern("apa")
				.pattern("pwp")
				.pattern("apa")
				.define('w', Items.GREEN_WOOL).define('p', Items.DARK_OAK_PLANKS)
				.define('a', EItems.ATTUNED_STONE.get())
				.unlockedBy(getHasName(EItems.ATTUNED_STONE.get()), has(EItems.ATTUNED_STONE.get())).save(consumer);
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, EItems.SPINNING_WHEEL.get())
				.pattern("wrr")
				.pattern("srr")
				.pattern("pap")
				.define('w', ItemTags.WOOL).define('s', Items.STICK)
				.define('a', EItems.ATTUNED_STONE.get())
				.define('p', EItems.ALDER_PLANKS.get())
				.define('r', EItems.ROWAN_STAIRS.get())
				.unlockedBy(getHasName(EItems.ATTUNED_STONE.get()), has(EItems.ATTUNED_STONE.get())).save(consumer);
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, EItems.WICKER_BUNDLE.get())
				.pattern("sss")
				.pattern("sss")
				.pattern("sss")
				.define('s', ItemTags.SAPLINGS)
				.unlockedBy("has_sapling", has(ItemTags.SAPLINGS)).save(consumer);
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, EItems.WITCH_OVEN.get())
				.pattern(" b ").pattern("iii")
				.pattern("ibi")
				.define('i', Items.IRON_INGOT).define('b', Items.IRON_BARS)
				.unlockedBy(getHasName(Items.IRON_INGOT), has(Items.IRON_INGOT)).save(consumer);

		slab(consumer, RecipeCategory.BUILDING_BLOCKS, EItems.ROWAN_SLAB.get(), EItems.ROWAN_PLANKS.get());
		slab(consumer, RecipeCategory.BUILDING_BLOCKS, EItems.ALDER_SLAB.get(), EItems.ALDER_PLANKS.get());
		slab(consumer, RecipeCategory.BUILDING_BLOCKS, EItems.HAWTHORN_SLAB.get(), EItems.HAWTHORN_PLANKS.get());

		stairs(consumer, EItems.ROWAN_STAIRS.get(), EItems.ROWAN_PLANKS.get());
		stairs(consumer, EItems.ALDER_STAIRS.get(), EItems.ALDER_PLANKS.get());
		stairs(consumer, EItems.HAWTHORN_STAIRS.get(), EItems.HAWTHORN_PLANKS.get());

		pressurePlate(consumer, EItems.ALDER_PRESSURE_PLATE.get(), EItems.ALDER_PLANKS.get());
		pressurePlate(consumer, EItems.HAWTHORN_PRESSURE_PLATE.get(), EItems.HAWTHORN_PLANKS.get());
		pressurePlate(consumer, EItems.ROWAN_PRESSURE_PLATE.get(), EItems.ROWAN_PLANKS.get());

		fenceBuilder(EItems.ALDER_FENCE.get(), Ingredient.of(EItems.ALDER_PLANKS.get()))
				.unlockedBy(getHasName(EItems.ALDER_PLANKS.get()), has(EItems.ALDER_PLANKS.get())).save(consumer);
		fenceBuilder(EItems.HAWTHORN_FENCE.get(), Ingredient.of(EItems.HAWTHORN_PLANKS.get()))
				.unlockedBy(getHasName(EItems.HAWTHORN_PLANKS.get()), has(EItems.HAWTHORN_PLANKS.get())).save(consumer);
		fenceBuilder(EItems.ROWAN_FENCE.get(), Ingredient.of(EItems.ROWAN_PLANKS.get()))
				.unlockedBy(getHasName(EItems.ROWAN_PLANKS.get()), has(EItems.ROWAN_PLANKS.get())).save(consumer);

		fenceGateBuilder(EItems.ALDER_FENCE_GATE.get(), Ingredient.of(EItems.ALDER_PLANKS.get()))
				.unlockedBy(getHasName(EItems.ALDER_PLANKS.get()), has(EItems.ALDER_PLANKS.get())).save(consumer);
		fenceGateBuilder(EItems.HAWTHORN_FENCE_GATE.get(), Ingredient.of(EItems.HAWTHORN_PLANKS.get()))
				.unlockedBy(getHasName(EItems.HAWTHORN_PLANKS.get()), has(EItems.HAWTHORN_PLANKS.get())).save(consumer);
		fenceGateBuilder(EItems.ROWAN_FENCE_GATE.get(), Ingredient.of(EItems.ROWAN_PLANKS.get()))
				.unlockedBy(getHasName(EItems.ROWAN_PLANKS.get()), has(EItems.ROWAN_PLANKS.get())).save(consumer);
	}

	protected void buildShapelessRecipes(Consumer<FinishedRecipe> consumer) {
		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, EItems.ANOINTING_PASTE.get())
				.requires(EItems.WATER_ARTICHOKE_SEEDS.get()).requires(EItems.MANDRAKE_SEEDS.get())
				.requires(EItems.BELLADONNA_SEEDS.get()).requires(EItems.SNOWBELL_SEEDS.get())
				.unlockedBy(getHasName(EItems.BELLADONNA_SEEDS.get()), has(EItems.BELLADONNA_SEEDS.get())).save(consumer);
		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, EItems.BONE_NEEDLE.get(), 8)
				.requires(Items.FLINT).requires(Items.BONE)
				.unlockedBy(getHasName(Items.BONE), has(Items.BONE)).save(consumer);
		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, EItems.FUME_FUNNEL_FILTERED.get())
				.requires(EItems.FUME_FUNNEL.get()).requires(EItems.FUME_FILTER.get())
				.unlockedBy(getHasName(EItems.FUME_FUNNEL.get()), has(EItems.FUME_FUNNEL.get())).save(consumer);
		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, EItems.PURIFIED_MILK.get()).requires(Items.MILK_BUCKET)
				.requires(EItems.ODOUR_OF_PURITY.get()).requires(EItems.CLAY_JAR.get(), 3)
				.unlockedBy(getHasName(EItems.ODOUR_OF_PURITY.get()), has(EItems.ODOUR_OF_PURITY.get())).save(consumer);
		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, EItems.QUICKLIME.get())
				.requires(EItems.WOOD_ASH.get())
				.unlockedBy(getHasName(EItems.WOOD_ASH.get()), has(EItems.WOOD_ASH.get())).save(consumer);
		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, EItems.TAGLOCK.get())
				.requires(Items.GLASS_BOTTLE).requires(EItems.BONE_NEEDLE.get())
				.unlockedBy(getHasName(EItems.BONE_NEEDLE.get()), has(EItems.BONE_NEEDLE.get())).save(consumer);
		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, EItems.WAYSTONE.get())
				.requires(Items.FLINT).requires(EItems.BONE_NEEDLE.get())
				.unlockedBy(getHasName(EItems.BONE_NEEDLE.get()), has(EItems.BONE_NEEDLE.get())).save(consumer);


		buttonBuilder(EItems.ALDER_BUTTON.get(), Ingredient.of(EItems.ALDER_PLANKS.get()))
				.unlockedBy(getHasName(EItems.ALDER_PLANKS.get()), has(EItems.ALDER_PLANKS.get())).save(consumer);
		buttonBuilder(EItems.HAWTHORN_BUTTON.get(), Ingredient.of(EItems.HAWTHORN_PLANKS.get()))
				.unlockedBy(getHasName(EItems.HAWTHORN_PLANKS.get()), has(EItems.HAWTHORN_PLANKS.get())).save(consumer);
		buttonBuilder(EItems.ROWAN_BUTTON.get(), Ingredient.of(EItems.ROWAN_PLANKS.get()))
				.unlockedBy(getHasName(EItems.ROWAN_PLANKS.get()), has(EItems.ROWAN_PLANKS.get())).save(consumer);

		planksFromLog(consumer, EItems.ROWAN_PLANKS.get(), EnchantedTags.Items.ROWAN_LOGS, 4);
		planksFromLog(consumer, EItems.ALDER_PLANKS.get(), EnchantedTags.Items.ALDER_LOGS, 4);
		planksFromLog(consumer, EItems.HAWTHORN_PLANKS.get(), EnchantedTags.Items.HAWTHORN_LOGS, 4);
	}

	protected void buildSmeltingRecipes(Consumer<FinishedRecipe> consumer) {
		SimpleCookingRecipeBuilder.smelting(
						Ingredient.of(EItems.SOFT_CLAY_JAR.get()), RecipeCategory.MISC, EItems.CLAY_JAR.get(),
						0.1F, 200)
				.unlockedBy(getHasName(EItems.SOFT_CLAY_JAR.get()), has(EItems.SOFT_CLAY_JAR.get())).save(consumer);
		SimpleCookingRecipeBuilder.smelting(
						Ingredient.of(ItemTags.SAPLINGS), RecipeCategory.MISC, EItems.WOOD_ASH.get(),
						0.1F, 150)
				.unlockedBy("has_sapling", has(ItemTags.SAPLINGS)).save(consumer);
	}

	protected void buildByproductRecipes(Consumer<FinishedRecipe> consumer) {
		byproduct(consumer, EItems.BREATH_OF_THE_GODDESS.get(), Items.BIRCH_SAPLING);
		byproduct(consumer, EItems.EXHALE_OF_THE_HORNED_ONE.get(), Items.OAK_SAPLING);
		byproduct(consumer, EItems.FOUL_FUME.get(), Items.JUNGLE_SAPLING);
		byproduct(consumer, EItems.FOUL_FUME.get(), ItemTags.LOGS_THAT_BURN);
		byproduct(consumer, EItems.FOUL_FUME.get(), EnchantedTags.Items.RAW_FOODS);
		byproduct(consumer, EItems.HINT_OF_REBIRTH.get(), Items.SPRUCE_SAPLING);
		byproduct(consumer, EItems.ODOUR_OF_PURITY.get(), EItems.HAWTHORN_SAPLING.get());
		byproduct(consumer, EItems.REEK_OF_MISFORTUNE.get(), EItems.ALDER_SAPLING.get());
		byproduct(consumer, EItems.WHIFF_OF_MAGIC.get(), EItems.ROWAN_SAPLING.get());
	}

	protected void buildSpinningRecipes(Consumer<FinishedRecipe> consumer) {
		// Basic poppets
		spinning(consumer, EItems.POPPET_INFUSED.get(),
				EItems.POPPET.get(), EItems.ATTUNED_STONE.get(), Items.ENDER_PEARL);
		spinning(consumer, EItems.POPPET_STURDY.get(),
				EItems.POPPET.get(), EItems.HINT_OF_REBIRTH.get(), Items.SHIELD);
		// Effect poppets
		spinningSet(consumer, EItems.ARMOUR_POPPET.get(), EItems.ARMOUR_POPPET_INFUSED.get(), EItems.ARMOUR_POPPET_STURDY.get(),
				EItems.HINT_OF_REBIRTH.get(), Items.DIAMOND_BOOTS);
		spinningSet(consumer, EItems.EARTH_POPPET.get(), EItems.EARTH_POPPET_INFUSED.get(), EItems.EARTH_POPPET_STURDY.get(),
				stack(EItems.MANDRAKE_ROOT.get(), 1), PotionUtils.setPotion(stack(Items.POTION, 1), Potions.LONG_SLOW_FALLING));
		spinningSet(consumer, EItems.FIRE_POPPET.get(), EItems.FIRE_POPPET_INFUSED.get(), EItems.FIRE_POPPET_STURDY.get(),
				stack(EItems.EMBER_MOSS.get(), 1), PotionUtils.setPotion(stack(Items.POTION, 1), Potions.LONG_FIRE_RESISTANCE));
		spinningSet(consumer, EItems.HUNGER_POPPET.get(), EItems.HUNGER_POPPET_INFUSED.get(), EItems.HUNGER_POPPET_STURDY.get(),
				EItems.ROWAN_BERRIES.get(), Items.ROTTEN_FLESH);
		spinningSet(consumer, EItems.TOOL_POPPET.get(), EItems.TOOL_POPPET_INFUSED.get(), EItems.TOOL_POPPET_STURDY.get(),
				EItems.HINT_OF_REBIRTH.get(), Items.DIAMOND_SWORD);
		spinningSet(consumer, EItems.VOID_POPPET.get(), EItems.VOID_POPPET_INFUSED.get(), EItems.VOID_POPPET_STURDY.get(),
				EItems.OTHERWHERE_CHALK.get(), EItems.ENDER_DEW.get());
		spinningSet(consumer, EItems.WATER_POPPET.get(), EItems.WATER_POPPET_INFUSED.get(), EItems.WATER_POPPET_STURDY.get(),
				stack(EItems.WATER_ARTICHOKE.get(), 1), PotionUtils.setPotion(stack(Items.POTION, 1), Potions.LONG_WATER_BREATHING));
		spinningSet(consumer, EItems.MAGIC_POPPET.get(), EItems.MAGIC_POPPET_INFUSED.get(), EItems.MAGIC_POPPET_STURDY.get(),
				stack(EItems.ANOINTING_PASTE.get(), 1), PotionUtils.setPotion(stack(Items.POTION, 1), Potions.STRONG_HARMING));
		spinningSet(consumer, EItems.VOODOO_PROTECTION_POPPET.get(), EItems.VOODOO_PROTECTION_POPPET_INFUSED.get(), EItems.VOODOO_PROTECTION_POPPET_STURDY.get(),
				stack(EItems.BONE_NEEDLE.get(), 1), PotionUtils.setPotion(stack(Items.POTION, 1), Potions.STRONG_HEALING));

		// Special poppets
		spinning(consumer, EItems.VOODOO_POPPET.get(), EItems.POPPET_INFUSED.get(), EItems.BONE_NEEDLE.get(), EItems.BREW_OF_THE_GROTESQUE.get());
	}

	protected void buildDistillingRecipes(Consumer<FinishedRecipe> consumer) {
		DistillingRecipeBuilder.create(Items.BLAZE_POWDER, Items.GUNPOWDER)
				.results(stack(Items.GLOWSTONE_DUST, 2))
				.cookTime(300).power(750).save(consumer);
		DistillingRecipeBuilder.create(stack(EItems.CLAY_JAR.get(), 3), stack(EItems.BREATH_OF_THE_GODDESS.get()), stack(Items.LAPIS_LAZULI))
				.results(EItems.TEAR_OF_THE_GODDESS.get(), EItems.WHIFF_OF_MAGIC.get(), Items.SLIME_BALL, EItems.FOUL_FUME.get())
				.cookTime(300).power(750).save(consumer);
		DistillingRecipeBuilder.create(stack(EItems.CLAY_JAR.get(), 2), stack(EItems.DEMON_HEART.get()), stack(EItems.DIAMOND_VAPOUR.get()))
				.results(stack(EItems.DEMONIC_BLOOD.get(), 2))
				.results(EItems.REFINED_EVIL.get())
				.cookTime(300).power(750).save(consumer);
		DistillingRecipeBuilder.create(stack(EItems.CLAY_JAR.get(), 3), stack(Items.DIAMOND), stack(EItems.OIL_OF_VITRIOL.get()))
				.results(stack(EItems.DIAMOND_VAPOUR.get(), 2))
				.results(EItems.ODOUR_OF_PURITY.get())
				.cookTime(300).power(750).save(consumer);
		DistillingRecipeBuilder.create(EItems.CLAY_JAR.get(), EItems.DIAMOND_VAPOUR.get(), Items.BLAZE_ROD)
				.results(EItems.DEMONIC_BLOOD.get())
				.cookTime(300).power(750).save(consumer);
		DistillingRecipeBuilder.create(stack(EItems.CLAY_JAR.get(), 3), stack(EItems.DIAMOND_VAPOUR.get()), stack(Items.GHAST_TEAR))
				.results(EItems.ODOUR_OF_PURITY.get(), EItems.REEK_OF_MISFORTUNE.get(), EItems.FOUL_FUME.get(), EItems.REFINED_EVIL.get())
				.cookTime(300).power(750).save(consumer);
		DistillingRecipeBuilder.create(stack(EItems.CLAY_JAR.get(), 6), stack(Items.ENDER_PEARL))
				.results(stack(EItems.ENDER_DEW.get(), 5))
				.results(EItems.WHIFF_OF_MAGIC.get())
				.cookTime(300).power(750).save(consumer);
		DistillingRecipeBuilder.create(EItems.CLAY_JAR.get(), EItems.FOUL_FUME.get(), EItems.QUICKLIME.get())
				.results(EItems.GYPSUM.get(), EItems.OIL_OF_VITRIOL.get(), Items.SLIME_BALL)
				.cookTime(300).power(750).save(consumer);
	}

	protected void buildCauldronRecipes(Consumer<FinishedRecipe> consumer) {
		CauldronTypeRecipeBuilder.cauldron(stack(Items.COOKED_BEEF), 0)
				.inputs(Items.BEEF)
				.cookColor(24, 48, 22)
				.finalColor(28, 94, 22).save(consumer);
		CauldronTypeRecipeBuilder.cauldron(stack(EItems.GOLDEN_CHALK.get()), 3000)
				.inputs(EItems.MANDRAKE_ROOT.get(), Items.GOLD_NUGGET, EItems.RITUAL_CHALK.get())
				.cookColor(89, 64, 0)
				.finalColor(194, 155, 0).save(consumer);
		CauldronTypeRecipeBuilder.cauldron(stack(EItems.OTHERWHERE_CHALK.get()), 2000)
				.inputs(Items.NETHER_WART, EItems.TEAR_OF_THE_GODDESS.get(), Items.ENDER_PEARL,
						EItems.RITUAL_CHALK.get())
				.cookColor(49, 21, 74)
				.finalColor(73, 13, 130).save(consumer);
		CauldronTypeRecipeBuilder.cauldron(stack(EItems.NETHER_CHALK.get()), 2000)
				.inputs(Items.NETHER_WART, Items.BLAZE_POWDER, EItems.RITUAL_CHALK.get())
				.cookColor(84, 1, 26)
				.finalColor(156, 1, 47).save(consumer);
		CauldronTypeRecipeBuilder.cauldron(stack(Items.COOKED_CHICKEN), 0)
				.inputs(Items.CHICKEN)
				.cookColor(82, 40, 84)
				.finalColor(110, 22, 115).save(consumer);
		CauldronTypeRecipeBuilder.cauldron(stack(EItems.DROP_OF_LUCK.get()), 7000)
				.inputs(EItems.MANDRAKE_ROOT.get(), Items.NETHER_WART, EItems.TEAR_OF_THE_GODDESS.get(),
						EItems.REFINED_EVIL.get(), EItems.MUTANDIS_EXTREMIS.get())
				.cookColor(0, 69, 23)
				.finalColor(0, 117, 39).save(consumer);
		CauldronTypeRecipeBuilder.cauldron(stack(EItems.MUTANDIS.get(), 6), 0)
				.inputs(EItems.MANDRAKE_ROOT.get(), EItems.EXHALE_OF_THE_HORNED_ONE.get(), Items.EGG)
				.cookColor(26, 71, 35)
				.finalColor(62, 128, 78).save(consumer);
		CauldronTypeRecipeBuilder.cauldron(stack(EItems.MUTANDIS_EXTREMIS.get()), 7000)
				.inputs(EItems.MUTANDIS.get(), Items.NETHER_WART)
				.cookColor(84, 24, 24)
				.finalColor(128, 29, 29).save(consumer);
		CauldronTypeRecipeBuilder.cauldron(stack(Items.NETHER_WART), 0)
				.inputs(EItems.MANDRAKE_ROOT.get(), EItems.TEAR_OF_THE_GODDESS.get(),
						EItems.DIAMOND_VAPOUR.get(), Items.ENDER_PEARL, Items.WHEAT,
						EItems.MUTANDIS.get())
				.cookColor(92, 35, 0)
				.finalColor(153, 52, 0).save(consumer);
		CauldronTypeRecipeBuilder.cauldron(stack(Items.COOKED_PORKCHOP), 0)
				.inputs(Items.PORKCHOP)
				.cookColor(61, 69, 24)
				.finalColor(98, 110, 19).save(consumer);
	}

	protected void buildKettleRecipes(Consumer<FinishedRecipe> consumer) {
		CauldronTypeRecipeBuilder.kettle(stack(EItems.BREW_OF_LOVE.get(), 3), 0)
				.inputs(Items.POPPY, Items.GOLDEN_CARROT, Items.LILY_PAD, Items.COCOA_BEANS,
						EItems.WHIFF_OF_MAGIC.get(), EItems.WATER_ARTICHOKE.get())
				.cookColor(176, 70, 165)
				.finalColor(247, 143, 235).save(consumer);
		CauldronTypeRecipeBuilder.kettle(stack(EItems.BREW_OF_SPROUTING.get(), 3), 0)
				.inputs(EItems.ROWAN_SAPLING.get(), EItems.TONGUE_OF_DOG.get(),
						EItems.ALDER_SAPLING.get(), EItems.MANDRAKE_ROOT.get(),
						EItems.HAWTHORN_SAPLING.get(), Items.POPPY)
				.cookColor(92, 60, 22)
				.finalColor(128, 90, 43).save(consumer);
		CauldronTypeRecipeBuilder.kettle(stack(EItems.BREW_OF_THE_DEPTHS.get(), 3), 0)
				.inputs(Items.LILY_PAD, Items.INK_SAC, EItems.MANDRAKE_ROOT.get(),
						EItems.TEAR_OF_THE_GODDESS.get(), EItems.WATER_ARTICHOKE.get(),
						EItems.ODOUR_OF_PURITY.get())
				.cookColor(24, 110, 168)
				.finalColor(84, 186, 214).save(consumer);
		CauldronTypeRecipeBuilder.kettle(stack(EItems.BREW_OF_THE_GROTESQUE.get(), 3), 750)
				.inputs(EItems.MUTANDIS_EXTREMIS.get(), EItems.MANDRAKE_ROOT.get(),
						EItems.WATER_ARTICHOKE.get(), Items.GOLDEN_APPLE, EItems.TONGUE_OF_DOG.get(),
						Items.POISONOUS_POTATO)
				.cookColor(54, 42, 33)
				.finalColor(128, 95, 70).save(consumer);
		CauldronTypeRecipeBuilder.kettle(stack(EItems.FLYING_OINTMENT.get()), 3000)
				.inputs(EItems.REDSTONE_SOUP.get())
				.inputs(PotionUtils.setPotion(Items.POTION.getDefaultInstance(), Potions.LONG_SWIFTNESS))
				.inputs(Items.DIAMOND, Items.FEATHER, EItems.WOOL_OF_BAT.get(),
						EItems.BELLADONNA_FLOWER.get())
				.cookColor(112, 102, 21)
				.finalColor(219, 199, 42).save(consumer);
		CauldronTypeRecipeBuilder.kettle(stack(EItems.HAPPENSTANCE_OIL.get()), 2000)
				.inputs(EItems.REDSTONE_SOUP.get())
				.inputs(PotionUtils.setPotion(Items.POTION.getDefaultInstance(), Potions.LONG_NIGHT_VISION))
				.inputs(Items.ENDER_EYE, Items.GOLDEN_CARROT, Items.SPIDER_EYE, EItems.MANDRAKE_ROOT.get())
				.cookColor(50, 15, 110)
				.finalColor(84, 26, 184).save(consumer);
		CauldronTypeRecipeBuilder.kettle(stack(EItems.MYSTIC_UNGUENT.get()), 3000)
				.inputs(EItems.REDSTONE_SOUP.get())
				.inputs(PotionUtils.setPotion(Items.POTION.getDefaultInstance(), Potions.LONG_STRENGTH))
				.inputs(Items.DIAMOND, EItems.ROWAN_SAPLING.get(), EItems.CREEPER_HEART.get(),
						EItems.DEMONIC_BLOOD.get())
				.cookColor(24, 48, 22)
				.finalColor(28, 94, 22).save(consumer);
		CauldronTypeRecipeBuilder.kettle(stack(EItems.REDSTONE_SOUP.get()), 1000)
				.inputs(Items.REDSTONE, EItems.DROP_OF_LUCK.get(), EItems.WOOL_OF_BAT.get(),
						EItems.TONGUE_OF_DOG.get(), EItems.BELLADONNA_FLOWER.get(),
						EItems.MANDRAKE_ROOT.get())
				.cookColor(128, 30, 23)
				.finalColor(222, 49, 29).save(consumer);
		CauldronTypeRecipeBuilder.kettle(stack(EItems.SOUL_OF_THE_WORLD.get(), 2), 4000)
				.inputs(EItems.REDSTONE_SOUP.get())
				.inputs(PotionUtils.setPotion(Items.POTION.getDefaultInstance(), Potions.LONG_REGENERATION))
				.inputs(EItems.ROWAN_SAPLING.get(), EItems.ATTUNED_STONE.get(),
						EItems.MANDRAKE_ROOT.get(), Items.GOLDEN_APPLE)
				.cookColor(13, 92, 25)
				.finalColor(9, 153, 31).save(consumer);
		CauldronTypeRecipeBuilder.kettle(stack(EItems.SPIRIT_OF_OTHERWHERE.get(), 2), 4000)
				.inputs(EItems.REDSTONE_SOUP.get())
				.inputs(PotionUtils.setPotion(Items.POTION.getDefaultInstance(), Potions.LONG_SWIFTNESS))
				.inputs(EItems.WOOL_OF_BAT.get(), Items.ENDER_EYE, Items.ENDER_EYE,
						EItems.DROP_OF_LUCK.get())
				.cookColor(47, 22, 69)
				.finalColor(78, 22, 128).save(consumer);
	}

	protected static void spinning(Consumer<FinishedRecipe> consumer, ItemLike result, ItemLike first, Item second, Item third) {
		SpinningRecipeBuilder.create(result, 500, 300, first, second, third).save(consumer);
	}

	protected static void spinningSet(Consumer<FinishedRecipe> consumer, ItemLike normal, ItemLike infused, ItemLike sturdy, Item second, Item third) {
		SpinningRecipeBuilder.create(normal, 500, 300, EItems.POPPET.get(), second, third).save(consumer);
		SpinningRecipeBuilder.create(infused, 1000, 300, EItems.POPPET_INFUSED.get(), second, third).save(consumer);
		SpinningRecipeBuilder.create(sturdy, 1000, 300, EItems.POPPET_STURDY.get(), second, third).save(consumer);
	}

	protected static void spinningSet(Consumer<FinishedRecipe> consumer, ItemLike normal, ItemLike infused, ItemLike sturdy, ItemStack second, ItemStack third) {
		SpinningRecipeBuilder.create(normal, 500, 300, EItems.POPPET.get().getDefaultInstance(), second, third).save(consumer);
		SpinningRecipeBuilder.create(infused, 1000, 300, EItems.POPPET_INFUSED.get().getDefaultInstance(), second, third).save(consumer);
		SpinningRecipeBuilder.create(sturdy, 1000, 300, EItems.POPPET_STURDY.get().getDefaultInstance(), second, third).save(consumer);
	}

	protected static void byproduct(Consumer<FinishedRecipe> consumer, ItemLike result, ItemLike... items) {
		ByproductRecipeBuilder.create(result, items).save(consumer);
	}

	protected static void byproduct(Consumer<FinishedRecipe> consumer, ItemLike result, TagKey<Item> tag) {
		ByproductRecipeBuilder.create(result, tag).save(consumer);
	}

	protected static void stairs(Consumer<FinishedRecipe> consumer, ItemLike stairs, ItemLike material) {
		stairBuilder(stairs, Ingredient.of(material)).unlockedBy(getHasName(material), has(material)).save(consumer);
	}

	private static ItemStack stack(ItemLike item, int count) {
		return new ItemStack(item, count);
	}

	private static ItemStack stack(ItemLike item) {
		return stack(item, 1);
	}

}
