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

package com.favouriteless.enchanted.common.data;

import com.favouriteless.enchanted.Enchanted;
import com.favouriteless.enchanted.common.init.EnchantedItems;
import net.minecraft.data.DataGenerator;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.client.model.generators.ModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class EnchantedItemModelProvider extends ItemModelProvider {

	private static ModelFile ITEM_GENERATED;

	public EnchantedItemModelProvider(DataGenerator generator, ExistingFileHelper existingFileHelper) {
		super(generator, Enchanted.MOD_ID, existingFileHelper);
	}

	@Override
	protected void registerModels() {
		ITEM_GENERATED = getExistingFile(mcLoc("item/generated"));

		simpleItem(EnchantedItems.ARTICHOKE_SEEDS.get());
		simpleItem(EnchantedItems.BELLADONNA_SEEDS.get());
		simpleItem(EnchantedItems.MANDRAKE_SEEDS.get());
		simpleItem(EnchantedItems.SNOWBELL_SEEDS.get());
		simpleItem(EnchantedItems.WOLFSBANE_SEEDS.get());
		simpleItem(EnchantedItems.ARTICHOKE.get());
		simpleItem(EnchantedItems.BELLADONNA_FLOWER.get());
		simpleItem(EnchantedItems.MANDRAKE_ROOT.get());
		simpleItem(EnchantedItems.WOLFSBANE_FLOWER.get());
		simpleItem(EnchantedItems.ICY_NEEDLE.get());
		simpleItem(EnchantedItems.ANOINTING_PASTE.get());
		simpleItem(EnchantedItems.MUTANDIS.get());
		simpleItem(EnchantedItems.MUTANDIS_EXTREMIS.get());
		simpleItem(EnchantedItems.TAGLOCK.get());
		simpleItem(EnchantedItems.TAGLOCK_FILLED.get());
		simpleItem(EnchantedItems.CLAY_JAR_SOFT.get());
		simpleItem(EnchantedItems.CLAY_JAR.get());
		simpleItem(EnchantedItems.BREATH_OF_THE_GODDESS.get());
		simpleItem(EnchantedItems.EXHALE_OF_THE_HORNED_ONE.get());
		simpleItem(EnchantedItems.FOUL_FUME.get());
		simpleItem(EnchantedItems.HINT_OF_REBIRTH.get());
		simpleItem(EnchantedItems.TEAR_OF_THE_GODDESS.get());
		simpleItem(EnchantedItems.WHIFF_OF_MAGIC.get());
		simpleItem(EnchantedItems.CONDENSED_FEAR.get());
		simpleItem(EnchantedItems.DIAMOND_VAPOUR.get());
		simpleItem(EnchantedItems.DROP_OF_LUCK.get());
		simpleItem(EnchantedItems.ENDER_DEW.get());
		simpleItem(EnchantedItems.FOCUSED_WILL.get());
		simpleItem(EnchantedItems.DEMONIC_BLOOD.get());
		simpleItem(EnchantedItems.MELLIFLUOUS_HUNGER.get());
		simpleItem(EnchantedItems.OIL_OF_VITRIOL.get());
		simpleItem(EnchantedItems.PURIFIED_MILK.get());
		simpleItem(EnchantedItems.ODOUR_OF_PURITY.get());
		simpleItem(EnchantedItems.REEK_OF_MISFORTUNE.get());
		simpleItem(EnchantedItems.ATTUNED_STONE.get());
		simpleItem(EnchantedItems.ATTUNED_STONE_CHARGED.get());
		simpleItem(EnchantedItems.GYPSUM.get());
		simpleItem(EnchantedItems.QUICKLIME.get());
		simpleItem(EnchantedItems.REFINED_EVIL.get());
		simpleItem(EnchantedItems.ROWAN_BERRIES.get());
		simpleItem(EnchantedItems.WOOD_ASH.get());
		simpleItem(EnchantedItems.FUME_FILTER.get());
		simpleItem(EnchantedItems.WOOL_OF_BAT.get());
		simpleItem(EnchantedItems.TONGUE_OF_DOG.get());
		simpleItem(EnchantedItems.CREEPER_HEART.get());
		simpleItem(EnchantedItems.BONE_NEEDLE.get());
		simpleItem(EnchantedItems.REDSTONE_SOUP.get());
		simpleItem(EnchantedItems.FLYING_OINTMENT.get());
		simpleItem(EnchantedItems.MYSTIC_UNGUENT.get());
		simpleItem(EnchantedItems.HAPPENSTANCE_OIL.get());
		simpleItem(EnchantedItems.GHOST_OF_THE_LIGHT.get());
		simpleItem(EnchantedItems.SOUL_OF_THE_WORLD.get());
		simpleItem(EnchantedItems.SPIRIT_OF_OTHERWHERE.get());
		simpleItem(EnchantedItems.INFERNAL_ANIMUS.get());
		simpleItem(EnchantedItems.GARLIC.get());
		simpleToolItem(EnchantedItems.ARTHANA.get());

		talismanItem(EnchantedItems.CIRCLE_TALISMAN.get());
		simpleItem(EnchantedItems.WAYSTONE.get());
		simpleItem(EnchantedItems.BOUND_WAYSTONE.get());

		simpleItem(EnchantedItems.EARMUFFS.get());

		simpleItem(EnchantedItems.BROOM.get());
		simpleItem(EnchantedItems.ENCHANTED_BROOMSTICK.get());
	}

	private void simpleItem(Item item) {
		String name = item.getRegistryName().getPath();
		withExistingParent(ModelProvider.ITEM_FOLDER + "/" + name, mcLoc(ModelProvider.ITEM_FOLDER + "/generated")).texture("layer0", ModelProvider.ITEM_FOLDER + "/" + name);
	}

	private void simpleToolItem(Item item) {
		String name = item.getRegistryName().getPath();
		withExistingParent(ModelProvider.ITEM_FOLDER + "/" + name, mcLoc(ModelProvider.ITEM_FOLDER + "/handheld")).texture("layer0", ModelProvider.ITEM_FOLDER + "/" + name);
	}

	private void talismanItem(Item item) {
		String name = ModelProvider.ITEM_FOLDER + "/" + item.getRegistryName().getPath();

		ItemModelBuilder builder = getBuilder(name);
		builder.parent(ITEM_GENERATED).texture("layer0", name);

		for(int x = 1; x < 4; x++) {
			builder.override().predicate(modLoc("large"), x * 0.299F).model(
					withExistingParent(name + "_" + "0" + "_" + "0" + "_" + x, mcLoc(ModelProvider.ITEM_FOLDER + "/generated"))
							.texture("layer0", name)
							.texture("layer1", name + "_large_" + x)).end();
			builder.override().predicate(modLoc("medium"), x * 0.299F).model(
					withExistingParent(name + "_" + "0" + "_" + x + "_" + "0", mcLoc(ModelProvider.ITEM_FOLDER + "/generated"))
							.texture("layer0", name)
							.texture("layer1", name + "_medium_" + x)).end();
			builder.override().predicate(modLoc("small"), x * 0.299F).model(
					withExistingParent(name + "_" + x + "_" + "0" + "_" + "0", mcLoc(ModelProvider.ITEM_FOLDER + "/generated"))
							.texture("layer0", name)
							.texture("layer1", name + "_small_" + x)).end();
		}


		for(int x = 1; x < 4; x++) {
			for(int y = 1; y < 4; y++) {
				builder.override().predicate(modLoc("medium"), y*0.299F).predicate(modLoc("large"), x*0.299F).model(
						withExistingParent(name + "_" + "0" + "_" + y + "_" + x, mcLoc(ModelProvider.ITEM_FOLDER + "/generated"))
								.texture("layer0", name)
								.texture("layer1", name + "_medium_" + y)
								.texture("layer2", name + "_large_" + x)).end();
				builder.override().predicate(modLoc("small"), y*0.299F).predicate(modLoc("large"), x*0.299F).model(
						withExistingParent(name + "_" + y + "_" + "0" + "_" + x, mcLoc(ModelProvider.ITEM_FOLDER + "/generated"))
								.texture("layer0", name)
								.texture("layer1", name + "_small_" + y)
								.texture("layer2", name + "_large_" + x)).end();
				builder.override().predicate(modLoc("small"), y*0.299F).predicate(modLoc("medium"), x*0.299F).model(
						withExistingParent(name + "_" + y + "_" + x + "_" + "0", mcLoc(ModelProvider.ITEM_FOLDER + "/generated"))
								.texture("layer0", name)
								.texture("layer1", name + "_small_" + y)
								.texture("layer2", name + "_medium_" + x)).end();
			}
		}

		for(int l = 1; l < 4; l++) {
			for(int m = 1; m < 4; m++) {
				for(int s = 1; s < 4; s++) {
					builder.override().predicate(modLoc("small"), s*0.299F).predicate(modLoc("medium"), m*0.299F).predicate(modLoc("large"), l*0.299F).model(
							withExistingParent(name + "_" + s + "_" + m + "_" + l, mcLoc(ModelProvider.ITEM_FOLDER + "/generated"))
									.texture("layer0", name)
									.texture("layer1", name + "_small_" + s)
									.texture("layer2", name + "_medium_" + m)
									.texture("layer3", name + "_large_" + l)).end();
				}
			}
		}
	}

}



