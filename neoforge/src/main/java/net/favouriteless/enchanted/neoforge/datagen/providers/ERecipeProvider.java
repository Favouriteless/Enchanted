package net.favouriteless.enchanted.neoforge.datagen.providers;

import net.favouriteless.enchanted.common.Enchanted;
import net.favouriteless.enchanted.common.init.EItems;
import net.favouriteless.enchanted.common.init.ETags;
import net.favouriteless.enchanted.neoforge.datagen.builders.recipe.*;
import net.favouriteless.modopedia.common.init.MDataComponents;
import net.favouriteless.modopedia.common.init.MItems;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;

import java.util.concurrent.CompletableFuture;

public class ERecipeProvider extends RecipeProvider {

	public ERecipeProvider(PackOutput output, CompletableFuture<Provider> registries) {
		super(output, registries);
	}
	

	@Override
	protected void buildRecipes(RecipeOutput output, Provider registries) {
		buildShapedRecipes(output, registries);
		buildShapelessRecipes(output, registries);
		buildSmeltingRecipes(output, registries);
		buildByproductRecipes(output, registries);
		buildSpinningRecipes(output, registries);
		buildDistillingRecipes(output, registries);
		buildCauldronRecipes(output, registries);
		buildKettleRecipes(output, registries);
	}

	protected void buildShapedRecipes(RecipeOutput output, Provider registries) {
		EShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, EItems.ALTAR.get())
				.pattern("bwe")
				.pattern("srs")
				.pattern("srs")
				.define('s', Items.STONE_BRICKS).define('r', EItems.ROWAN_LOG.get())
				.define('b', EItems.BREATH_OF_THE_GODDESS.get())
				.define('e', EItems.EXHALE_OF_THE_HORNED_ONE.get())
				.define('w', Items.POTION).save(output);
		EShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, EItems.ARTHANA.get())
				.pattern(" i ")
				.pattern("nen")
				.pattern(" s ")
				.define('i', Items.GOLD_INGOT).define('n', Items.GOLD_NUGGET)
				.define('e', Items.EMERALD).define('s', Items.STICK).save(output);
		EShapedRecipeBuilder.shaped(RecipeCategory.MISC, EItems.ATTUNED_STONE.get())
				.pattern("w")
				.pattern("d")
				.pattern("l")
				.define('w', EItems.WHIFF_OF_MAGIC.get()).define('d', Items.DIAMOND)
				.define('l', Items.LAVA_BUCKET).save(output);
		EShapedRecipeBuilder.shaped(RecipeCategory.MISC, EItems.BROOM.get())
				.pattern(" s ")
				.pattern(" s ")
				.pattern("hhh")
				.define('s', Items.STICK).define('h', EItems.HAWTHORN_SAPLING.get()).save(output);
		EShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, EItems.CANDELABRA.get())
				.pattern("ccc")
				.pattern("iai")
				.pattern(" i ")
				.define('c', Items.CANDLE).define('i', Items.IRON_INGOT)
				.define('a', EItems.ATTUNED_STONE.get()).save(output);
		EShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, EItems.CHALICE.get())
				.pattern("nan")
				.pattern("ngn")
				.pattern(" g ")
				.define('n', Items.GOLD_NUGGET).define('g', Items.GOLD_INGOT)
				.define('a', EItems.ATTUNED_STONE.get()).save(output);
		EShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, EItems.RITUAL_CHALK.get())
				.pattern("ata")
				.pattern("aga")
				.pattern("aga")
				.define('g', EItems.GYPSUM.get())
				.define('t', EItems.TEAR_OF_THE_GODDESS.get())
				.define('a', EItems.WOOD_ASH.get()).save(output);
		EShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, EItems.CIRCLE_TALISMAN.get())
				.pattern("ngn")
				.pattern("gag")
				.pattern("ngn").define('n', Items.GOLD_NUGGET)
				.define('g', Items.GOLD_INGOT).define('a', EItems.ATTUNED_STONE.get()).save(output);
		EShapedRecipeBuilder.shaped(RecipeCategory.MISC, EItems.SOFT_CLAY_JAR.get(), 4)
				.pattern(" c ")
				.pattern("ccc")
				.define('c', Items.CLAY_BALL).save(output);
		EShapedRecipeBuilder.shaped(RecipeCategory.MISC, EItems.DISTILLERY.get())
				.pattern("cic")
				.pattern("iii")
				.pattern("gag")
				.define('i', Items.IRON_INGOT).define('g', Items.GOLD_NUGGET)
				.define('c', EItems.CLAY_JAR.get())
				.define('a', EItems.ATTUNED_STONE.get()).save(output);
		EShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, EItems.EARMUFFS.get())
				.pattern(" i ")
				.pattern("whw")
				.define('i', Items.IRON_INGOT).define('w', ItemTags.WOOL)
				.define('h', Items.LEATHER_HELMET).save(output);
		EShapedRecipeBuilder.shaped(RecipeCategory.MISC, EItems.FUME_FILTER.get())
				.pattern("ggg")
				.pattern("iai")
				.pattern("ggg")
				.define('g', Items.GLASS).define('i', Items.IRON_INGOT)
				.define('a', EItems.ATTUNED_STONE.get()).save(output);
		EShapedRecipeBuilder.shaped(RecipeCategory.MISC, EItems.FUME_FUNNEL.get())
				.pattern("blb")
				.pattern("bgb")
				.pattern("ifi")
				.define('g', Items.GLOWSTONE).define('i', Items.IRON_BLOCK)
				.define('b', Items.BUCKET).define('l', Items.LAVA_BUCKET)
				.define('f', Items.IRON_BARS).save(output);
		EShapedRecipeBuilder.shaped(RecipeCategory.MISC, EItems.KETTLE.get())
				.pattern("wsw")
				.pattern("sas")
				.pattern(" c ").define('w', Items.STICK)
				.define('s', Items.STRING).define('c', Items.CAULDRON)
				.define('a', EItems.ATTUNED_STONE.get()).save(output);
		EShapedRecipeBuilder.shaped(RecipeCategory.MISC, EItems.POPPET.get())
				.pattern("wmw")
				.pattern("bms")
				.pattern("waw")
				.define('w', ItemTags.WOOL).define('s', Items.STRING)
				.define('m', EItems.SPANISH_MOSS.get())
				.define('b', EItems.BONE_NEEDLE.get())
				.define('a', EItems.ATTUNED_STONE.get()).save(output);
		EShapedRecipeBuilder.shaped(RecipeCategory.MISC, EItems.POPPET_SHELF.get())
				.pattern("apa")
				.pattern("pwp")
				.pattern("apa")
				.define('w', Items.GREEN_WOOL).define('p', Items.DARK_OAK_PLANKS)
				.define('a', EItems.ATTUNED_STONE.get()).save(output);
		EShapedRecipeBuilder.shaped(RecipeCategory.MISC, EItems.SPINNING_WHEEL.get())
				.pattern("wrr")
				.pattern("srr")
				.pattern("pap")
				.define('w', ItemTags.WOOL).define('s', Items.STICK)
				.define('a', EItems.ATTUNED_STONE.get())
				.define('p', EItems.ALDER_PLANKS.get())
				.define('r', EItems.ROWAN_STAIRS.get()).save(output);
		EShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, EItems.WICKER_BUNDLE.get())
				.pattern("sss")
				.pattern("sss")
				.pattern("sss")
				.define('s', ItemTags.SAPLINGS)
				.unlockedBy("has_sapling", has(ItemTags.SAPLINGS)).save(output);
		EShapedRecipeBuilder.shaped(RecipeCategory.MISC, EItems.WITCH_OVEN.get())
				.pattern(" b ").pattern("iii")
				.pattern("ibi")
				.define('i', Items.IRON_INGOT).define('b', Items.IRON_BARS).save(output);

		slab(output, RecipeCategory.BUILDING_BLOCKS, EItems.ROWAN_SLAB.get(), EItems.ROWAN_PLANKS.get());
		slab(output, RecipeCategory.BUILDING_BLOCKS, EItems.ALDER_SLAB.get(), EItems.ALDER_PLANKS.get());
		slab(output, RecipeCategory.BUILDING_BLOCKS, EItems.HAWTHORN_SLAB.get(), EItems.HAWTHORN_PLANKS.get());

		stairs(output, EItems.ROWAN_STAIRS.get(), EItems.ROWAN_PLANKS.get());
		stairs(output, EItems.ALDER_STAIRS.get(), EItems.ALDER_PLANKS.get());
		stairs(output, EItems.HAWTHORN_STAIRS.get(), EItems.HAWTHORN_PLANKS.get());

		pressurePlate(output, EItems.ALDER_PRESSURE_PLATE.get(), EItems.ALDER_PLANKS.get());
		pressurePlate(output, EItems.HAWTHORN_PRESSURE_PLATE.get(), EItems.HAWTHORN_PLANKS.get());
		pressurePlate(output, EItems.ROWAN_PRESSURE_PLATE.get(), EItems.ROWAN_PLANKS.get());

		fence(output, EItems.ALDER_FENCE.get(), Ingredient.of(EItems.ALDER_PLANKS.get()));
		fence(output, EItems.HAWTHORN_FENCE.get(), Ingredient.of(EItems.HAWTHORN_PLANKS.get()));
		fence(output, EItems.ROWAN_FENCE.get(), Ingredient.of(EItems.ROWAN_PLANKS.get()));

		fenceGate(output, EItems.ALDER_FENCE_GATE.get(), Ingredient.of(EItems.ALDER_PLANKS.get()));
		fenceGate(output, EItems.HAWTHORN_FENCE_GATE.get(), Ingredient.of(EItems.HAWTHORN_PLANKS.get()));
		fenceGate(output, EItems.ROWAN_FENCE_GATE.get(), Ingredient.of(EItems.ROWAN_PLANKS.get()));
	}

	protected void buildShapelessRecipes(RecipeOutput output, Provider registries) {
		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, EItems.ANOINTING_PASTE.get())
				.requires(EItems.WATER_ARTICHOKE_SEEDS.get()).requires(EItems.MANDRAKE_SEEDS.get())
				.requires(EItems.BELLADONNA_SEEDS.get()).requires(EItems.SNOWBELL_SEEDS.get())
				.unlockedBy(getHasName(EItems.BELLADONNA_SEEDS.get()), has(EItems.BELLADONNA_SEEDS.get())).save(output);
		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, EItems.BONE_NEEDLE.get(), 8)
				.requires(Items.FLINT).requires(Items.BONE)
				.unlockedBy(getHasName(Items.BONE), has(Items.BONE)).save(output);
		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, EItems.PURIFIED_MILK.get()).requires(Items.MILK_BUCKET)
				.requires(EItems.ODOUR_OF_PURITY.get()).requires(EItems.CLAY_JAR.get(), 3)
				.unlockedBy(getHasName(EItems.ODOUR_OF_PURITY.get()), has(EItems.ODOUR_OF_PURITY.get())).save(output);
		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, EItems.QUICKLIME.get())
				.requires(EItems.WOOD_ASH.get())
				.unlockedBy(getHasName(EItems.WOOD_ASH.get()), has(EItems.WOOD_ASH.get())).save(output);
		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, EItems.TAGLOCK.get())
				.requires(Items.GLASS_BOTTLE).requires(EItems.BONE_NEEDLE.get())
				.unlockedBy(getHasName(EItems.BONE_NEEDLE.get()), has(EItems.BONE_NEEDLE.get())).save(output);
		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, EItems.WAYSTONE.get())
				.requires(Items.FLINT).requires(EItems.BONE_NEEDLE.get())
				.unlockedBy(getHasName(EItems.BONE_NEEDLE.get()), has(EItems.BONE_NEEDLE.get())).save(output);


		buttonBuilder(EItems.ALDER_BUTTON.get(), Ingredient.of(EItems.ALDER_PLANKS.get()))
				.unlockedBy(getHasName(EItems.ALDER_PLANKS.get()), has(EItems.ALDER_PLANKS.get())).save(output);
		buttonBuilder(EItems.HAWTHORN_BUTTON.get(), Ingredient.of(EItems.HAWTHORN_PLANKS.get()))
				.unlockedBy(getHasName(EItems.HAWTHORN_PLANKS.get()), has(EItems.HAWTHORN_PLANKS.get())).save(output);
		buttonBuilder(EItems.ROWAN_BUTTON.get(), Ingredient.of(EItems.ROWAN_PLANKS.get()))
				.unlockedBy(getHasName(EItems.ROWAN_PLANKS.get()), has(EItems.ROWAN_PLANKS.get())).save(output);

		planksFromLog(output, EItems.ALDER_PLANKS.get(), ETags.Items.ALDER_LOGS, 4);
		planksFromLog(output, EItems.HAWTHORN_PLANKS.get(), ETags.Items.HAWTHORN_LOGS, 4);
		planksFromLog(output, EItems.ROWAN_PLANKS.get(), ETags.Items.ROWAN_LOGS, 4);

		ItemStack book = new ItemStack(MItems.BOOK.get());
		book.set(MDataComponents.BOOK, Enchanted.id("art_of_witchcraft"));

		ShapelessRecipeBuilder.shapeless(RecipeCategory.TOOLS, book)
				.requires(Items.INK_SAC).requires(Items.BOOK).requires(Items.FEATHER)
				.requires(EItems.BELLADONNA_SEEDS.get())
				.unlockedBy(getHasName(EItems.BELLADONNA_SEEDS.get()), has(EItems.BELLADONNA_SEEDS.get())).save(output, Enchanted.id("art_of_witchcraft"));
	}

	protected void buildSmeltingRecipes(RecipeOutput output, Provider registries) {
		SimpleCookingRecipeBuilder.smelting(
						Ingredient.of(EItems.SOFT_CLAY_JAR.get()), RecipeCategory.MISC, EItems.CLAY_JAR.get(),
						0.1F, 200)
				.unlockedBy(getHasName(EItems.SOFT_CLAY_JAR.get()), has(EItems.SOFT_CLAY_JAR.get())).save(output);
		SimpleCookingRecipeBuilder.smelting(
						Ingredient.of(ItemTags.SAPLINGS), RecipeCategory.MISC, EItems.WOOD_ASH.get(),
						0.1F, 150)
				.unlockedBy("has_sapling", has(ItemTags.SAPLINGS)).save(output);
	}

	protected void buildByproductRecipes(RecipeOutput output, Provider registries) {
		byproduct(output, new ItemStack(EItems.BREATH_OF_THE_GODDESS.get()), Items.BIRCH_SAPLING);
		byproduct(output, new ItemStack(EItems.EXHALE_OF_THE_HORNED_ONE.get()), Items.OAK_SAPLING);
		byproduct(output, new ItemStack(EItems.FOUL_FUME.get()), Items.JUNGLE_SAPLING);
		byproduct(output, new ItemStack(EItems.FOUL_FUME.get()), ItemTags.LOGS_THAT_BURN);
		byproduct(output, new ItemStack(EItems.FOUL_FUME.get()), ETags.Items.RAW_FOODS);
		byproduct(output, new ItemStack(EItems.HINT_OF_REBIRTH.get()), Items.SPRUCE_SAPLING);
		byproduct(output, new ItemStack(EItems.ODOUR_OF_PURITY.get()), EItems.HAWTHORN_SAPLING.get());
		byproduct(output, new ItemStack(EItems.REEK_OF_MISFORTUNE.get()), EItems.ALDER_SAPLING.get());
		byproduct(output, new ItemStack(EItems.WHIFF_OF_MAGIC.get()), EItems.ROWAN_SAPLING.get());
	}

	protected void buildSpinningRecipes(RecipeOutput output, Provider registries) {
		// Basic poppets
		spinning(output, EItems.POPPET_INFUSED.get(),
				EItems.POPPET.get(), EItems.ATTUNED_STONE.get(), Items.ENDER_PEARL);
		spinning(output, EItems.POPPET_STURDY.get(),
				EItems.POPPET.get(), EItems.HINT_OF_REBIRTH.get(), Items.SHIELD);
		// Effect poppets
		spinningSet(output, EItems.ARMOUR_POPPET.get(), EItems.ARMOUR_POPPET_INFUSED.get(), EItems.ARMOUR_POPPET_STURDY.get(),
				EItems.HINT_OF_REBIRTH.get(), Items.DIAMOND_BOOTS);
		spinningSet(output, EItems.EARTH_POPPET.get(), EItems.EARTH_POPPET_INFUSED.get(), EItems.EARTH_POPPET_STURDY.get(),
				new ItemStack(EItems.MANDRAKE_ROOT.get()), PotionContents.createItemStack(Items.POTION, Potions.LONG_SLOW_FALLING));
		spinningSet(output, EItems.FIRE_POPPET.get(), EItems.FIRE_POPPET_INFUSED.get(), EItems.FIRE_POPPET_STURDY.get(),
				new ItemStack(EItems.EMBER_MOSS.get()), PotionContents.createItemStack(Items.POTION, Potions.LONG_FIRE_RESISTANCE));
		spinningSet(output, EItems.HUNGER_POPPET.get(), EItems.HUNGER_POPPET_INFUSED.get(), EItems.HUNGER_POPPET_STURDY.get(),
				EItems.ROWAN_BERRIES.get(), Items.ROTTEN_FLESH);
		spinningSet(output, EItems.TOOL_POPPET.get(), EItems.TOOL_POPPET_INFUSED.get(), EItems.TOOL_POPPET_STURDY.get(),
				EItems.HINT_OF_REBIRTH.get(), Items.DIAMOND_SWORD);
		spinningSet(output, EItems.VOID_POPPET.get(), EItems.VOID_POPPET_INFUSED.get(), EItems.VOID_POPPET_STURDY.get(),
				EItems.OTHERWHERE_CHALK.get(), EItems.ENDER_DEW.get());
		spinningSet(output, EItems.WATER_POPPET.get(), EItems.WATER_POPPET_INFUSED.get(), EItems.WATER_POPPET_STURDY.get(),
				new ItemStack(EItems.WATER_ARTICHOKE.get()), PotionContents.createItemStack(Items.POTION, Potions.LONG_WATER_BREATHING));
		spinningSet(output, EItems.MAGIC_POPPET.get(), EItems.MAGIC_POPPET_INFUSED.get(), EItems.MAGIC_POPPET_STURDY.get(),
				new ItemStack(EItems.ANOINTING_PASTE.get()), PotionContents.createItemStack(Items.POTION, Potions.STRONG_HARMING));
		spinningSet(output, EItems.VOODOO_PROTECTION_POPPET.get(), EItems.VOODOO_PROTECTION_POPPET_INFUSED.get(), EItems.VOODOO_PROTECTION_POPPET_STURDY.get(),
				new ItemStack(EItems.BONE_NEEDLE.get()), PotionContents.createItemStack(Items.POTION, Potions.STRONG_HEALING));

		// Special poppets
		spinning(output, EItems.VOODOO_POPPET.get(), EItems.POPPET_INFUSED.get(), EItems.BONE_NEEDLE.get(), EItems.BREW_OF_THE_GROTESQUE.get());
	}

	protected void buildDistillingRecipes(RecipeOutput output, Provider registries) {
		DistillingRecipeBuilder.create(new ItemStack(EItems.CLAY_JAR.get(), 3), new ItemStack(EItems.BREATH_OF_THE_GODDESS.get()), new ItemStack(Items.LAPIS_LAZULI))
				.results(EItems.TEAR_OF_THE_GODDESS.get(), EItems.WHIFF_OF_MAGIC.get(), Items.SLIME_BALL, EItems.FOUL_FUME.get()).save(output);
		DistillingRecipeBuilder.create(new ItemStack(EItems.CLAY_JAR.get(), 2), new ItemStack(EItems.DEMON_HEART.get()), new ItemStack(EItems.DIAMOND_VAPOUR.get()))
				.results(new ItemStack(EItems.DEMONIC_BLOOD.get(), 2))
				.results(EItems.REFINED_EVIL.get()).save(output);
		DistillingRecipeBuilder.create(new ItemStack(EItems.CLAY_JAR.get(), 3), new ItemStack(Items.DIAMOND), new ItemStack(EItems.OIL_OF_VITRIOL.get()))
				.results(new ItemStack(EItems.DIAMOND_VAPOUR.get(), 2))
				.results(EItems.ODOUR_OF_PURITY.get()).save(output);
		DistillingRecipeBuilder.create(EItems.CLAY_JAR.get(), EItems.DIAMOND_VAPOUR.get(), Items.BLAZE_ROD)
				.results(EItems.DEMONIC_BLOOD.get()).save(output);
		DistillingRecipeBuilder.create(new ItemStack(EItems.CLAY_JAR.get(), 3), new ItemStack(EItems.DIAMOND_VAPOUR.get()), new ItemStack(Items.GHAST_TEAR))
				.results(EItems.ODOUR_OF_PURITY.get(), EItems.REEK_OF_MISFORTUNE.get(), EItems.FOUL_FUME.get(), EItems.REFINED_EVIL.get()).save(output);
		DistillingRecipeBuilder.create(new ItemStack(EItems.CLAY_JAR.get(), 6), new ItemStack(Items.ENDER_PEARL))
				.results(new ItemStack(EItems.ENDER_DEW.get(), 5))
				.results(EItems.WHIFF_OF_MAGIC.get()).save(output);
		DistillingRecipeBuilder.create(EItems.CLAY_JAR.get(), EItems.FOUL_FUME.get(), EItems.QUICKLIME.get())
				.results(EItems.GYPSUM.get(), EItems.OIL_OF_VITRIOL.get(), Items.SLIME_BALL).save(output);
	}

	protected void buildCauldronRecipes(RecipeOutput output, Provider registries) {
		CauldronTypeRecipeBuilder.cauldron(new ItemStack(EItems.GOLDEN_CHALK.get()), 3000)
				.inputs(EItems.MANDRAKE_ROOT.get(), Items.GOLD_NUGGET, EItems.RITUAL_CHALK.get())
				.cookColor(0x594000)
				.finalColor(0xc29B00).save(output);
		CauldronTypeRecipeBuilder.cauldron(new ItemStack(EItems.OTHERWHERE_CHALK.get()), 2000)
				.inputs(Items.NETHER_WART, EItems.TEAR_OF_THE_GODDESS.get(), Items.ENDER_PEARL, EItems.RITUAL_CHALK.get())
				.cookColor(0x31154A)
				.finalColor(0x490D82).save(output);
		CauldronTypeRecipeBuilder.cauldron(new ItemStack(EItems.NETHER_CHALK.get()), 2000)
				.inputs(Items.NETHER_WART, Items.BLAZE_POWDER, EItems.RITUAL_CHALK.get())
				.cookColor(0x54011A)
				.finalColor(0x9C012F).save(output);
		CauldronTypeRecipeBuilder.cauldron(new ItemStack(EItems.DROP_OF_LUCK.get()), 7000)
				.inputs(EItems.MANDRAKE_ROOT.get(), Items.NETHER_WART, EItems.TEAR_OF_THE_GODDESS.get(),
						EItems.REFINED_EVIL.get(), EItems.MUTANDIS_EXTREMIS.get())
				.cookColor(0x004517)
				.finalColor(0x007527).save(output);
		CauldronTypeRecipeBuilder.cauldron(new ItemStack(EItems.MUTANDIS.get(), 6), 0)
				.inputs(EItems.MANDRAKE_ROOT.get(), EItems.EXHALE_OF_THE_HORNED_ONE.get(), Items.EGG)
				.cookColor(0x1A4723)
				.finalColor(0x3E804E).save(output);
		CauldronTypeRecipeBuilder.cauldron(new ItemStack(EItems.MUTANDIS_EXTREMIS.get()), 7000)
				.inputs(EItems.MUTANDIS.get(), Items.NETHER_WART)
				.cookColor(0x541818)
				.finalColor(0x801D1D).save(output);
	}

	protected void buildKettleRecipes(RecipeOutput output, Provider registries) {
		CauldronTypeRecipeBuilder.kettle(new ItemStack(EItems.BREW_OF_LOVE.get(), 3), 0)
				.inputs(Items.POPPY, Items.GOLDEN_CARROT, Items.LILY_PAD, Items.COCOA_BEANS, EItems.WHIFF_OF_MAGIC.get(),
						EItems.WATER_ARTICHOKE.get())
				.cookColor(0xB046A5)
				.finalColor(0xF78FEB).save(output);
		CauldronTypeRecipeBuilder.kettle(new ItemStack(EItems.BREW_OF_SPROUTING.get(), 3), 0)
				.inputs(EItems.ROWAN_SAPLING.get(), EItems.TONGUE_OF_DOG.get(), EItems.ALDER_SAPLING.get(),
						EItems.MANDRAKE_ROOT.get(), EItems.HAWTHORN_SAPLING.get(), Items.POPPY)
				.cookColor(0x5C3C16)
				.finalColor(0x805A2B).save(output);
		CauldronTypeRecipeBuilder.kettle(new ItemStack(EItems.BREW_OF_THE_DEPTHS.get(), 3), 0)
				.inputs(Items.LILY_PAD, Items.INK_SAC, EItems.MANDRAKE_ROOT.get(), EItems.TEAR_OF_THE_GODDESS.get(),
						EItems.WATER_ARTICHOKE.get(), EItems.ODOUR_OF_PURITY.get())
				.cookColor(0x186EA8)
				.finalColor(0x54BAD6).save(output);
		CauldronTypeRecipeBuilder.kettle(new ItemStack(EItems.BREW_OF_THE_GROTESQUE.get(), 3), 750)
				.inputs(EItems.MUTANDIS_EXTREMIS.get(), EItems.MANDRAKE_ROOT.get(), EItems.WATER_ARTICHOKE.get(),
						Items.GOLDEN_APPLE, EItems.TONGUE_OF_DOG.get(), Items.POISONOUS_POTATO)
				.cookColor(0x362A21)
				.finalColor(0x805F46).save(output);
		CauldronTypeRecipeBuilder.kettle(new ItemStack(EItems.FLYING_OINTMENT.get()), 3000)
				.inputs(EItems.REDSTONE_SOUP.get())
				.inputs(PotionContents.createItemStack(Items.POTION, Potions.LONG_SWIFTNESS))
				.inputs(Items.DIAMOND, Items.FEATHER, EItems.WOOL_OF_BAT.get(), EItems.BELLADONNA_FLOWER.get())
				.cookColor(0x706615)
				.finalColor(0xDBC72A).save(output);
		CauldronTypeRecipeBuilder.kettle(new ItemStack(EItems.HAPPENSTANCE_OIL.get()), 2000)
				.inputs(EItems.REDSTONE_SOUP.get())
				.inputs(PotionContents.createItemStack(Items.POTION, Potions.LONG_NIGHT_VISION))
				.inputs(Items.ENDER_EYE, Items.GOLDEN_CARROT, Items.SPIDER_EYE, EItems.MANDRAKE_ROOT.get())
				.cookColor(0x320F6E)
				.finalColor(0x541AB8).save(output);
		CauldronTypeRecipeBuilder.kettle(new ItemStack(EItems.MYSTIC_UNGUENT.get()), 3000)
				.inputs(EItems.REDSTONE_SOUP.get())
				.inputs(PotionContents.createItemStack(Items.POTION, Potions.STRONG_STRENGTH))
				.inputs(Items.DIAMOND, EItems.ROWAN_SAPLING.get(), EItems.CREEPER_HEART.get(), EItems.DEMONIC_BLOOD.get())
				.cookColor(0x183016)
				.finalColor(0x1C5E16).save(output);
		CauldronTypeRecipeBuilder.kettle(new ItemStack(EItems.REDSTONE_SOUP.get()), 1000)
				.inputs(Items.REDSTONE, EItems.DROP_OF_LUCK.get(), EItems.WOOL_OF_BAT.get(), EItems.TONGUE_OF_DOG.get(),
						EItems.BELLADONNA_FLOWER.get(), EItems.MANDRAKE_ROOT.get())
				.cookColor(0x801E17)
				.finalColor(0xDE311D).save(output);
		CauldronTypeRecipeBuilder.kettle(new ItemStack(EItems.SOUL_OF_THE_WORLD.get(), 2), 4000)
				.inputs(EItems.REDSTONE_SOUP.get())
				.inputs(PotionContents.createItemStack(Items.POTION, Potions.LONG_REGENERATION))
				.inputs(EItems.ROWAN_SAPLING.get(), EItems.ATTUNED_STONE.get(), EItems.MANDRAKE_ROOT.get(), Items.GOLDEN_APPLE)
				.cookColor(0x0D5C19)
				.finalColor(0x09991F).save(output);
		CauldronTypeRecipeBuilder.kettle(new ItemStack(EItems.SPIRIT_OF_OTHERWHERE.get(), 2), 4000)
				.inputs(EItems.REDSTONE_SOUP.get())
				.inputs(PotionContents.createItemStack(Items.POTION, Potions.LONG_SWIFTNESS))
				.inputs(EItems.WOOL_OF_BAT.get(), Items.ENDER_EYE, Items.ENDER_EYE, EItems.DROP_OF_LUCK.get())
				.cookColor(0x2F1645)
				.finalColor(0x4E1680).save(output);
	}

	protected static void spinning(RecipeOutput output, ItemLike result, ItemLike first, Item second, Item third) {
		SpinningRecipeBuilder.create(result, first, second, third).power(500).save(output);
	}

	protected static void spinningSet(RecipeOutput output, ItemLike normal, ItemLike infused, ItemLike sturdy, Item second, Item third) {
		SpinningRecipeBuilder.create(normal, EItems.POPPET.get(), second, third).power(500).save(output);
		SpinningRecipeBuilder.create(infused, EItems.POPPET_INFUSED.get(), second, third).power(1000).save(output);
		SpinningRecipeBuilder.create(sturdy, EItems.POPPET_STURDY.get(), second, third).power(1000).save(output);
	}

	protected static void spinningSet(RecipeOutput output, ItemLike normal, ItemLike infused, ItemLike sturdy, ItemStack second, ItemStack third) {
		SpinningRecipeBuilder.create(normal, EItems.POPPET.get().getDefaultInstance(), second, third).power(500).save(output);
		SpinningRecipeBuilder.create(infused, EItems.POPPET_INFUSED.get().getDefaultInstance(), second, third).power(1000).save(output);
		SpinningRecipeBuilder.create(sturdy, EItems.POPPET_STURDY.get().getDefaultInstance(), second, third).power(1000).save(output);
	}

	protected static void byproduct(RecipeOutput output, ItemStack result, ItemLike... items) {
		ByproductRecipeBuilder.create(result, items).save(output);
	}

	protected static void byproduct(RecipeOutput output, ItemStack result, ItemStack... items) {
		ByproductRecipeBuilder.create(result, items).save(output);
	}

	protected static void byproduct(RecipeOutput output, ItemStack result, TagKey<Item> items) {
		ByproductRecipeBuilder.create(result, items).save(output);
	}

	protected static void slab(RecipeOutput output, RecipeCategory category, ItemLike slab, ItemLike material) {
		EShapedRecipeBuilder.shaped(category, slab, 6)
				.define('#', material)
				.pattern("###")
				.save(output);
	}

	protected static void stairs(RecipeOutput output, ItemLike stairs, ItemLike material) {
		EShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, stairs, 4)
				.define('#', material)
				.pattern("#  ")
				.pattern("## ")
				.pattern("###")
				.save(output);
	}

	protected static void pressurePlate(RecipeOutput output, ItemLike pressurePlate, ItemLike material) {
		EShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, pressurePlate)
				.define('#', material)
				.pattern("##")
				.save(output);
	}

	protected static void fence(RecipeOutput output, ItemLike fence, Ingredient material) {
		EShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, fence, fence == Blocks.NETHER_BRICK_FENCE ? 6 : 3)
				.define('W', material)
				.define('#', fence == Blocks.NETHER_BRICK_FENCE ? Items.NETHER_BRICK : Items.STICK)
				.pattern("W#W")
				.pattern("W#W")
				.save(output);
	}

	protected static void fenceGate(RecipeOutput output, ItemLike fenceGate, Ingredient material) {
		EShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, fenceGate)
				.define('#', Items.STICK)
				.define('W', material)
				.pattern("#W#")
				.pattern("#W#")
				.save(output);
	}

}
