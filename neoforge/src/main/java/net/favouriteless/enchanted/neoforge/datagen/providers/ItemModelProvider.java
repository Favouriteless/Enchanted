package net.favouriteless.enchanted.neoforge.datagen.providers;

import net.favouriteless.enchanted.common.Enchanted;
import net.favouriteless.enchanted.common.init.EItems;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class ItemModelProvider extends net.neoforged.neoforge.client.model.generators.ItemModelProvider {

	private static ModelFile ITEM_GENERATED;

	public ItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
		super(output, Enchanted.MOD_ID, existingFileHelper);
	}


	@Override
	protected void registerModels() {
		ITEM_GENERATED = getExistingFile(mcLoc("item/generated"));

		simpleItem(EItems.WATER_ARTICHOKE_SEEDS.get());
		simpleItem(EItems.BELLADONNA_SEEDS.get());
		simpleItem(EItems.MANDRAKE_SEEDS.get());
		simpleItem(EItems.SNOWBELL_SEEDS.get());
		simpleItem(EItems.WOLFSBANE_SEEDS.get());
		simpleItem(EItems.WATER_ARTICHOKE.get());
		simpleItem(EItems.BELLADONNA_FLOWER.get());
		simpleItem(EItems.MANDRAKE_ROOT.get());
		simpleItem(EItems.WOLFSBANE_FLOWER.get());
		simpleItem(EItems.ICY_NEEDLE.get());
		simpleItem(EItems.GARLIC.get());
		simpleItem(EItems.ANOINTING_PASTE.get());
		simpleItem(EItems.MUTANDIS.get());
		simpleItem(EItems.MUTANDIS_EXTREMIS.get());
		simpleItem(EItems.TAGLOCK.get());
		simpleItem(EItems.TAGLOCK_FILLED.get());
		simpleItem(EItems.SOFT_CLAY_JAR.get());
		simpleItem(EItems.CLAY_JAR.get());
		simpleItem(EItems.BREATH_OF_THE_GODDESS.get());
		simpleItem(EItems.EXHALE_OF_THE_HORNED_ONE.get());
		simpleItem(EItems.FOUL_FUME.get());
		simpleItem(EItems.HINT_OF_REBIRTH.get());
		simpleItem(EItems.TEAR_OF_THE_GODDESS.get());
		simpleItem(EItems.WHIFF_OF_MAGIC.get());
		simpleItem(EItems.CONDENSED_FEAR.get());
		simpleItem(EItems.DIAMOND_VAPOUR.get());
		simpleItem(EItems.DROP_OF_LUCK.get());
		simpleItem(EItems.ENDER_DEW.get());
		simpleItem(EItems.FOCUSED_WILL.get());
		simpleItem(EItems.DEMONIC_BLOOD.get());
		simpleItem(EItems.MELLIFLUOUS_HUNGER.get());
		simpleItem(EItems.OIL_OF_VITRIOL.get());
		simpleItem(EItems.PURIFIED_MILK.get());
		simpleItem(EItems.ODOUR_OF_PURITY.get());
		simpleItem(EItems.REEK_OF_MISFORTUNE.get());
		simpleItem(EItems.ATTUNED_STONE.get());
		simpleItem(EItems.ATTUNED_STONE_CHARGED.get());
		simpleItem(EItems.GYPSUM.get());
		simpleItem(EItems.QUICKLIME.get());
		simpleItem(EItems.REFINED_EVIL.get());
		simpleItem(EItems.ROWAN_BERRIES.get());
		simpleItem(EItems.WOOD_ASH.get());
		simpleItem(EItems.FUME_FILTER.get());
		simpleItem(EItems.WOOL_OF_BAT.get());
		simpleItem(EItems.TONGUE_OF_DOG.get());
		simpleItem(EItems.CREEPER_HEART.get());
		simpleItem(EItems.ENT_TWIG.get());
		simpleToolItem(EItems.ARTHANA.get());
		simpleItem(EItems.BONE_NEEDLE.get());
		simpleItem(EItems.REDSTONE_SOUP.get());
		simpleItem(EItems.FLYING_OINTMENT.get());
		simpleItem(EItems.MYSTIC_UNGUENT.get());
		simpleItem(EItems.HAPPENSTANCE_OIL.get());
		simpleItem(EItems.GHOST_OF_THE_LIGHT.get());
		simpleItem(EItems.SOUL_OF_THE_WORLD.get());
		simpleItem(EItems.SPIRIT_OF_OTHERWHERE.get());
		simpleItem(EItems.INFERNAL_ANIMUS.get());
		simpleItem(EItems.BREW_OF_THE_GROTESQUE.get());
		simpleItem(EItems.BREW_OF_LOVE.get());
		simpleItem(EItems.BREW_OF_SPROUTING.get());
		simpleItem(EItems.BREW_OF_THE_DEPTHS.get());

		talismanItem(EItems.CIRCLE_TALISMAN.get());
		simpleItem(EItems.WAYSTONE.get());
		simpleItem(EItems.BLOODED_WAYSTONE.get());
		simpleItem(EItems.BOUND_WAYSTONE.get());

		simpleItem(EItems.EARMUFFS.get());

		simpleItem(EItems.BROOM.get());
		simpleItem(EItems.ENCHANTED_BROOMSTICK.get());

		simpleItem(EItems.POPPET.get());
		simpleItem(EItems.POPPET_INFUSED.get());
		simpleItem(EItems.POPPET_STURDY.get());
		simpleItem(EItems.EARTH_POPPET.get());
		simpleItem(EItems.EARTH_POPPET_INFUSED.get());
		simpleItem(EItems.EARTH_POPPET_STURDY.get());
		simpleItem(EItems.FIRE_POPPET.get());
		simpleItem(EItems.FIRE_POPPET_INFUSED.get());
		simpleItem(EItems.FIRE_POPPET_STURDY.get());
		simpleItem(EItems.WATER_POPPET.get());
		simpleItem(EItems.WATER_POPPET_INFUSED.get());
		simpleItem(EItems.WATER_POPPET_STURDY.get());
		simpleItem(EItems.HUNGER_POPPET.get());
		simpleItem(EItems.HUNGER_POPPET_INFUSED.get());
		simpleItem(EItems.HUNGER_POPPET_STURDY.get());
		simpleItem(EItems.VOID_POPPET.get());
		simpleItem(EItems.VOID_POPPET_INFUSED.get());
		simpleItem(EItems.VOID_POPPET_STURDY.get());
		simpleItem(EItems.TOOL_POPPET.get());
		simpleItem(EItems.TOOL_POPPET_INFUSED.get());
		simpleItem(EItems.TOOL_POPPET_STURDY.get());
		simpleItem(EItems.ARMOUR_POPPET.get());
		simpleItem(EItems.ARMOUR_POPPET_INFUSED.get());
		simpleItem(EItems.ARMOUR_POPPET_STURDY.get());
		simpleItem(EItems.MAGIC_POPPET.get());
		simpleItem(EItems.MAGIC_POPPET_INFUSED.get());
		simpleItem(EItems.MAGIC_POPPET_STURDY.get());
		simpleItem(EItems.VOODOO_POPPET.get());
		simpleItem(EItems.VOODOO_PROTECTION_POPPET.get());
		simpleItem(EItems.VOODOO_PROTECTION_POPPET_INFUSED.get());
		simpleItem(EItems.VOODOO_PROTECTION_POPPET_STURDY.get());
	}

	private void simpleItem(Item item) {
		String name = BuiltInRegistries.ITEM.getKey(item).getPath();
		withExistingParent(ITEM_FOLDER + "/" + name, mcLoc(ITEM_FOLDER + "/generated")).texture("layer0", ITEM_FOLDER + "/" + name);
	}

	private void simpleToolItem(Item item) {
		String name = BuiltInRegistries.ITEM.getKey(item).getPath();
		withExistingParent(ITEM_FOLDER + "/" + name, mcLoc(ITEM_FOLDER + "/handheld")).texture("layer0", ITEM_FOLDER + "/" + name);
	}

	private void talismanItem(Item item) {
		String name = ITEM_FOLDER + "/" + BuiltInRegistries.ITEM.getKey(item).getPath();

		ItemModelBuilder builder = getBuilder(name);
		builder.parent(ITEM_GENERATED).texture("layer0", name);

		for(int x = 1; x < 4; x++) {
			builder.override().predicate(modLoc("large"), x * 0.299F).model(
					withExistingParent(name + "_" + "0" + "_" + "0" + "_" + x, mcLoc(ITEM_FOLDER + "/generated"))
							.texture("layer0", name)
							.texture("layer1", name + "_large_" + x)).end();
			builder.override().predicate(modLoc("medium"), x * 0.299F).model(
					withExistingParent(name + "_" + "0" + "_" + x + "_" + "0", mcLoc(ITEM_FOLDER + "/generated"))
							.texture("layer0", name)
							.texture("layer1", name + "_medium_" + x)).end();
			builder.override().predicate(modLoc("small"), x * 0.299F).model(
					withExistingParent(name + "_" + x + "_" + "0" + "_" + "0", mcLoc(ITEM_FOLDER + "/generated"))
							.texture("layer0", name)
							.texture("layer1", name + "_small_" + x)).end();
		}


		for(int x = 1; x < 4; x++) {
			for(int y = 1; y < 4; y++) {
				builder.override().predicate(modLoc("medium"), y*0.299F).predicate(modLoc("large"), x*0.299F).model(
						withExistingParent(name + "_" + "0" + "_" + y + "_" + x, mcLoc(ITEM_FOLDER + "/generated"))
								.texture("layer0", name)
								.texture("layer1", name + "_medium_" + y)
								.texture("layer2", name + "_large_" + x)).end();
				builder.override().predicate(modLoc("small"), y*0.299F).predicate(modLoc("large"), x*0.299F).model(
						withExistingParent(name + "_" + y + "_" + "0" + "_" + x, mcLoc(ITEM_FOLDER + "/generated"))
								.texture("layer0", name)
								.texture("layer1", name + "_small_" + y)
								.texture("layer2", name + "_large_" + x)).end();
				builder.override().predicate(modLoc("small"), y*0.299F).predicate(modLoc("medium"), x*0.299F).model(
						withExistingParent(name + "_" + y + "_" + x + "_" + "0", mcLoc(ITEM_FOLDER + "/generated"))
								.texture("layer0", name)
								.texture("layer1", name + "_small_" + y)
								.texture("layer2", name + "_medium_" + x)).end();
			}
		}

		for(int l = 1; l < 4; l++) {
			for(int m = 1; m < 4; m++) {
				for(int s = 1; s < 4; s++) {
					builder.override().predicate(modLoc("small"), s*0.299F).predicate(modLoc("medium"), m*0.299F).predicate(modLoc("large"), l*0.299F).model(
							withExistingParent(name + "_" + s + "_" + m + "_" + l, mcLoc(ITEM_FOLDER + "/generated"))
									.texture("layer0", name)
									.texture("layer1", name + "_small_" + s)
									.texture("layer2", name + "_medium_" + m)
									.texture("layer3", name + "_large_" + l)).end();
				}
			}
		}
	}

}



