package com.favouriteless.enchanted.datagen.providers;

import com.favouriteless.enchanted.common.init.EnchantedTags;
import com.favouriteless.enchanted.common.init.registry.EnchantedItems;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.crafting.StrictNBTIngredient;

import java.util.function.Consumer;

public class RecipeProvider extends net.minecraft.data.recipes.RecipeProvider {

	public RecipeProvider(DataGenerator generator) {
		super(generator);
	}

	@Override
	protected void buildCraftingRecipes(Consumer<FinishedRecipe> recipeConsumer) {
		// Shaped recipes
		ShapedRecipeBuilder.shaped(EnchantedItems.ALTAR.get())
				.pattern("bwe")
				.pattern("srs")
				.pattern("srs")
				.define('s', Items.STONE_BRICKS).define('r', EnchantedItems.ROWAN_LOG.get())
				.define('b', EnchantedItems.BREATH_OF_THE_GODDESS.get())
				.define('e', EnchantedItems.EXHALE_OF_THE_HORNED_ONE.get())
				.define('w', StrictNBTIngredient.of(Items.POTION.getDefaultInstance()))
				.unlockedBy("has_rowan_log", has(EnchantedItems.ROWAN_LOG.get())).save(recipeConsumer);
		ShapedRecipeBuilder.shaped(EnchantedItems.ARTHANA.get())
				.pattern(" i ")
				.pattern("nen")
				.pattern(" s ")
				.define('i', Items.GOLD_INGOT).define('n', Items.GOLD_NUGGET)
				.define('e', Items.EMERALD).define('s', Items.STICK)
				.unlockedBy("has_gold_ingot", has(Items.GOLD_INGOT)).save(recipeConsumer);
		ShapedRecipeBuilder.shaped(EnchantedItems.ATTUNED_STONE.get())
				.pattern("w")
				.pattern("d")
				.pattern("l")
				.define('w', EnchantedItems.WHIFF_OF_MAGIC.get()).define('d', Items.DIAMOND)
				.define('l', Items.LAVA_BUCKET)
				.unlockedBy("has_whiff_of_magic", has(EnchantedItems.WHIFF_OF_MAGIC.get())).save(recipeConsumer);
		ShapedRecipeBuilder.shaped(EnchantedItems.BROOM.get())
				.pattern(" s ")
				.pattern(" s ")
				.pattern("hhh")
				.define('s', Items.STICK).define('h', EnchantedItems.HAWTHORN_SAPLING.get())
				.unlockedBy("has_hawthorn_sapling", has(EnchantedItems.HAWTHORN_SAPLING.get())).save(recipeConsumer);
		ShapedRecipeBuilder.shaped(EnchantedItems.CANDELABRA.get())
				.pattern("ccc")
				.pattern("iai")
				.pattern(" i ")
				.define('c', Items.CANDLE).define('i', Items.IRON_INGOT)
				.define('a', EnchantedItems.ATTUNED_STONE.get())
				.unlockedBy("has_attuned_stone", has(EnchantedItems.ATTUNED_STONE.get())).save(recipeConsumer);
		ShapedRecipeBuilder.shaped(EnchantedItems.CHALICE.get())
				.pattern("nan")
				.pattern("ngn")
				.pattern(" g ")
				.define('n', Items.GOLD_NUGGET).define('g', Items.GOLD_INGOT)
				.define('a', EnchantedItems.ATTUNED_STONE.get())
				.unlockedBy("has_attuned_stone", has(EnchantedItems.ATTUNED_STONE.get())).save(recipeConsumer);
		ShapedRecipeBuilder.shaped(EnchantedItems.CHALK_WHITE.get(), 2)
				.pattern("ata")
				.pattern("aga")
				.pattern("aga")
				.define('g', EnchantedItems.GYPSUM.get())
				.define('t', EnchantedItems.TEAR_OF_THE_GODDESS.get())
				.define('a', EnchantedItems.WOOD_ASH.get())
				.unlockedBy("has_tear_of_the_Goddess", has(EnchantedItems.TEAR_OF_THE_GODDESS.get())).save(recipeConsumer);
		ShapedRecipeBuilder.shaped(EnchantedItems.CIRCLE_TALISMAN.get())
				.pattern("ngn").pattern("gag")
				.pattern("ngn").define('n', Items.GOLD_NUGGET)
				.define('g', Items.GOLD_INGOT).define('a', EnchantedItems.ATTUNED_STONE.get())
				.unlockedBy("has_attuned_stone", has(EnchantedItems.ATTUNED_STONE.get())).save(recipeConsumer);
		ShapedRecipeBuilder.shaped(EnchantedItems.CLAY_JAR_SOFT.get(), 4)
				.pattern(" c ")
				.pattern("ccc")
				.define('c', Items.CLAY)
				.unlockedBy("has_clay", has(Items.CLAY)).save(recipeConsumer);
		ShapedRecipeBuilder.shaped(EnchantedItems.DISTILLERY.get())
				.pattern("cic")
				.pattern("iii")
				.pattern("gag")
				.define('i', Items.IRON_INGOT).define('g', Items.GOLD_NUGGET)
				.define('c', EnchantedItems.CLAY_JAR.get())
				.define('a', EnchantedItems.ATTUNED_STONE.get())
				.unlockedBy("has_attuned_stone", has(EnchantedItems.ATTUNED_STONE.get())).save(recipeConsumer);
		ShapedRecipeBuilder.shaped(EnchantedItems.EARMUFFS.get())
				.pattern(" i ")
				.pattern("whw")
				.define('i', Items.IRON_INGOT).define('w', ItemTags.WOOL)
				.define('h', Items.LEATHER_HELMET)
				.unlockedBy("has_wool", has(ItemTags.WOOL))
				.unlockedBy("has_leather_helmet", has(Items.LEATHER_HELMET)).save(recipeConsumer);
		ShapedRecipeBuilder.shaped(EnchantedItems.FUME_FILTER.get())
				.pattern("ggg")
				.pattern("iai")
				.pattern("ggg")
				.define('g', Items.GLASS).define('i', Items.IRON_INGOT)
				.define('a', EnchantedItems.ATTUNED_STONE.get())
				.unlockedBy("has_attuned_stone", has(EnchantedItems.ATTUNED_STONE.get())).save(recipeConsumer);
		ShapedRecipeBuilder.shaped(EnchantedItems.FUME_FUNNEL.get())
				.pattern("blb")
				.pattern("bgb")
				.pattern("ifi")
				.define('g', Items.GLOWSTONE).define('i', Items.IRON_BLOCK)
				.define('b', Items.BUCKET).define('l', Items.LAVA_BUCKET)
				.define('f', Items.IRON_BARS)
				.unlockedBy("has_glowstone", has(Items.GLOWSTONE)).save(recipeConsumer);
		ShapedRecipeBuilder.shaped(EnchantedItems.KETTLE.get())
				.pattern("wsw")
				.pattern("sas")
				.pattern(" c ").define('w', Items.STICK)
				.define('s', Items.STRING).define('c', Items.CAULDRON
				).define('a', EnchantedItems.ATTUNED_STONE.get())
				.unlockedBy("has_attuned_stone", has(EnchantedItems.ATTUNED_STONE.get())).save(recipeConsumer);
		ShapedRecipeBuilder.shaped(EnchantedItems.POPPET.get())
				.pattern("wmw")
				.pattern("bms")
				.pattern("waw")
				.define('w', ItemTags.WOOL).define('s', Items.STRING)
				.define('m', EnchantedItems.SPANISH_MOSS.get())
				.define('b', EnchantedItems.BONE_NEEDLE.get())
				.define('a', EnchantedItems.ATTUNED_STONE.get())
				.unlockedBy("has_attuned_stone", has(EnchantedItems.ATTUNED_STONE.get())).save(recipeConsumer);
		ShapedRecipeBuilder.shaped(EnchantedItems.POPPET_SHELF.get())
				.pattern("apa")
				.pattern("pwp")
				.pattern("apa")
				.define('w', Items.GREEN_WOOL).define('p', Items.DARK_OAK_PLANKS)
				.define('a', EnchantedItems.ATTUNED_STONE.get())
				.unlockedBy("has_attuned_stone", has(EnchantedItems.ATTUNED_STONE.get())).save(recipeConsumer);
		ShapedRecipeBuilder.shaped(EnchantedItems.SPINNING_WHEEL.get())
				.pattern("wrr")
				.pattern("srr")
				.pattern("pap")
				.define('w', ItemTags.WOOL).define('s', Items.STICK)
				.define('a', EnchantedItems.ATTUNED_STONE.get())
				.define('p', EnchantedItems.ALDER_PLANKS.get())
				.define('r', EnchantedItems.ROWAN_STAIRS.get())
				.unlockedBy("has_attuned_stone", has(EnchantedItems.ATTUNED_STONE.get())).save(recipeConsumer);
		ShapedRecipeBuilder.shaped(EnchantedItems.WICKER_BUNDLE.get())
				.pattern("sss")
				.pattern("sss")
				.pattern("sss")
				.define('s', ItemTags.SAPLINGS)
				.unlockedBy("has_sapling", has(ItemTags.SAPLINGS)).save(recipeConsumer);
		ShapedRecipeBuilder.shaped(EnchantedItems.WITCH_OVEN.get())
				.pattern(" b ").pattern("iii")
				.pattern("ibi")
				.define('i', Items.IRON_INGOT).define('b', Items.IRON_BARS)
				.unlockedBy("has_iron_ingot", has(Items.IRON_INGOT)).save(recipeConsumer);

		planksFromLog(recipeConsumer, EnchantedItems.ROWAN_PLANKS.get(), EnchantedItems.ROWAN_LOG.get());
		planksFromLog(recipeConsumer, EnchantedItems.ALDER_PLANKS.get(), EnchantedItems.ALDER_LOG.get());
		planksFromLog(recipeConsumer, EnchantedItems.HAWTHORN_PLANKS.get(), EnchantedItems.HAWTHORN_LOG.get());

		slab(recipeConsumer, EnchantedItems.ROWAN_SLAB.get(), EnchantedItems.ROWAN_PLANKS.get());
		slab(recipeConsumer, EnchantedItems.ALDER_SLAB.get(), EnchantedItems.ALDER_PLANKS.get());
		slab(recipeConsumer, EnchantedItems.HAWTHORN_SLAB.get(), EnchantedItems.HAWTHORN_PLANKS.get());

		stairs(recipeConsumer, EnchantedItems.ROWAN_STAIRS.get(), EnchantedItems.ROWAN_PLANKS.get());
		stairs(recipeConsumer, EnchantedItems.ALDER_STAIRS.get(), EnchantedItems.ALDER_PLANKS.get());
		stairs(recipeConsumer, EnchantedItems.HAWTHORN_STAIRS.get(), EnchantedItems.HAWTHORN_PLANKS.get());

		// Shapeless recipes
		ShapelessRecipeBuilder.shapeless(EnchantedItems.ANOINTING_PASTE.get())
				.requires(EnchantedItems.ARTICHOKE_SEEDS.get()).requires(EnchantedItems.MANDRAKE_SEEDS.get())
				.requires(EnchantedItems.BELLADONNA_SEEDS.get()).requires(EnchantedItems.SNOWBELL_SEEDS.get())
				.unlockedBy("has_seeds", has(EnchantedTags.Items.SEEDS_DROPS)).save(recipeConsumer);
		ShapelessRecipeBuilder.shapeless(EnchantedItems.BONE_NEEDLE.get(), 8)
				.requires(Items.FLINT).requires(Items.BONE)
				.unlockedBy("has_bone", has(Items.BONE)).save(recipeConsumer);
		ShapelessRecipeBuilder.shapeless(EnchantedItems.FUME_FUNNEL_FILTERED.get())
				.requires(EnchantedItems.FUME_FUNNEL.get()).requires(EnchantedItems.FUME_FILTER.get())
				.unlockedBy("has_fume_funnel", has(EnchantedItems.FUME_FUNNEL.get())).save(recipeConsumer);
		ShapelessRecipeBuilder.shapeless(EnchantedItems.PURIFIED_MILK.get()).requires(Items.MILK_BUCKET)
				.requires(EnchantedItems.ODOUR_OF_PURITY.get()).requires(EnchantedItems.CLAY_JAR.get(), 3)
				.unlockedBy("has_odour_of_purity", has(EnchantedItems.ODOUR_OF_PURITY.get())).save(recipeConsumer);
		ShapelessRecipeBuilder.shapeless(EnchantedItems.QUICKLIME.get())
				.requires(EnchantedItems.WOOD_ASH.get())
				.unlockedBy("has_wood_ash", has(EnchantedItems.WOOD_ASH.get())).save(recipeConsumer);
		ShapelessRecipeBuilder.shapeless(EnchantedItems.TAGLOCK.get())
				.requires(Items.GLASS_BOTTLE).requires(EnchantedItems.BONE_NEEDLE.get())
				.unlockedBy("has_bone_needle", has(EnchantedItems.BONE_NEEDLE.get())).save(recipeConsumer);
		ShapelessRecipeBuilder.shapeless(EnchantedItems.WAYSTONE.get())
				.requires(Items.FLINT).requires(EnchantedItems.BONE_NEEDLE.get())
				.unlockedBy("has_bone_needle", has(EnchantedItems.BONE_NEEDLE.get())).save(recipeConsumer);

		// Smelting recipes
		SimpleCookingRecipeBuilder.smelting(
				Ingredient.of(EnchantedItems.CLAY_JAR_SOFT.get()), EnchantedItems.CLAY_JAR.get(),
						0.1F, 200)
				.unlockedBy("has_clay_jar_soft", has(EnchantedItems.CLAY_JAR_SOFT.get())).save(recipeConsumer);
		SimpleCookingRecipeBuilder.smelting(
				Ingredient.of(ItemTags.SAPLINGS), EnchantedItems.WOOD_ASH.get(),
				0.1F, 150)
				.unlockedBy("has_sapling", has(ItemTags.SAPLINGS)).save(recipeConsumer);
	}

	protected static void planksFromLog(Consumer<FinishedRecipe> recipeConsumer, ItemLike planks, ItemLike logs) {
		ShapelessRecipeBuilder.shapeless(planks, 4).requires(logs).group("planks").unlockedBy("has_log", has(logs)).save(recipeConsumer);
	}

	protected static void stairs(Consumer<FinishedRecipe> recipeConsumer, ItemLike stairs, ItemLike material) {
		stairBuilder(stairs, Ingredient.of(material)).unlockedBy(getHasName(material), has(material)).save(recipeConsumer);
	}

}
