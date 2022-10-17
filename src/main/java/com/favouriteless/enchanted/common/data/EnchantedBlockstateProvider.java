/*
 *
 *   Copyright (c) 2022. Favouriteless
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

package com.favouriteless.enchanted.common.data;


import com.favouriteless.enchanted.Enchanted;
import com.favouriteless.enchanted.common.blocks.FumeFunnelBlock;
import com.favouriteless.enchanted.common.blocks.KettleBlock;
import com.favouriteless.enchanted.common.blocks.chalk.ChalkCircleBlock;
import com.favouriteless.enchanted.common.blocks.chalk.GoldChalkBlock;
import com.favouriteless.enchanted.common.blocks.crops.BloodPoppyBlock;
import com.favouriteless.enchanted.common.blocks.crops.CropsBlockAgeFive;
import com.favouriteless.enchanted.common.init.EnchantedBlocks;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Plane;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.generators.*;
import net.minecraftforge.common.data.ExistingFileHelper;

public class EnchantedBlockstateProvider extends BlockStateProvider {


	public EnchantedBlockstateProvider(DataGenerator gen, ExistingFileHelper exFileHelper) {
		super(gen, Enchanted.MOD_ID, exFileHelper);
	}

	@Override
	protected void registerStatesAndModels() {
		logBlockWithItem(EnchantedBlocks.ROWAN_LOG.get());
		logBlockWithItem(EnchantedBlocks.HAWTHORN_LOG.get());
		logBlockWithItem(EnchantedBlocks.ALDER_LOG.get());
		simpleBlockWithItem(EnchantedBlocks.ROWAN_PLANKS.get());
		simpleBlockWithItem(EnchantedBlocks.HAWTHORN_PLANKS.get());
		simpleBlockWithItem(EnchantedBlocks.ALDER_PLANKS.get());
		slabBlockWithItem(EnchantedBlocks.ROWAN_SLAB.get(), EnchantedBlocks.ROWAN_PLANKS.get());
		slabBlockWithItem(EnchantedBlocks.HAWTHORN_SLAB.get(), EnchantedBlocks.HAWTHORN_PLANKS.get());
		slabBlockWithItem(EnchantedBlocks.ALDER_SLAB.get(), EnchantedBlocks.ALDER_PLANKS.get());
		stairsBlockWithItem(EnchantedBlocks.ROWAN_STAIRS.get(), EnchantedBlocks.ROWAN_PLANKS.get());
		stairsBlockWithItem(EnchantedBlocks.HAWTHORN_STAIRS.get(), EnchantedBlocks.HAWTHORN_PLANKS.get());
		stairsBlockWithItem(EnchantedBlocks.ALDER_STAIRS.get(), EnchantedBlocks.ALDER_PLANKS.get());
		leafRandomBlockWithItem(EnchantedBlocks.ROWAN_LEAVES.get(), 4);
		leafRandomBlockWithItem(EnchantedBlocks.HAWTHORN_LEAVES.get(), 4);
		leafRandomBlockWithItem(EnchantedBlocks.ALDER_LEAVES.get(), 4);
		crossBlockWithItem(EnchantedBlocks.ROWAN_SAPLING.get());
		crossBlockWithItem(EnchantedBlocks.HAWTHORN_SAPLING.get());
		crossBlockWithItem(EnchantedBlocks.ALDER_SAPLING.get());

		chalkBlockWithItem(EnchantedBlocks.CHALK_WHITE.get());
		chalkBlockWithItem(EnchantedBlocks.CHALK_RED.get());
		chalkBlockWithItem(EnchantedBlocks.CHALK_PURPLE.get());
		goldChalkBlockWithItem(EnchantedBlocks.CHALK_GOLD.get());

		simpleBlockItem(EnchantedBlocks.ALTAR.get(), models().getExistingFile(modLoc("block/altar")));
		horizontalLitBlockWithItem(EnchantedBlocks.WITCH_OVEN.get(), "_on", 180);
		fumeFunnelWithItem(EnchantedBlocks.FUME_FUNNEL.get());
		fumeFunnelWithItem(EnchantedBlocks.FUME_FUNNEL_FILTERED.get());
		horizontalLitBlockWithItem(EnchantedBlocks.DISTILLERY.get(), "", 180);
		complexBlockWithItem(EnchantedBlocks.WITCH_CAULDRON.get());
		complexBlockWithItem(EnchantedBlocks.CHALICE.get());
		complexBlockWithItem(EnchantedBlocks.CHALICE_FILLED.get());
		complexBlockWithItem(EnchantedBlocks.CHALICE_FILLED_MILK.get());
		complexBlockWithItem(EnchantedBlocks.CANDELABRA.get());
		kettleBlockWithItem(EnchantedBlocks.KETTLE.get());
		complexBlockWithItem(EnchantedBlocks.POPPET_SHELF.get());

		cropsBlockWithItem(EnchantedBlocks.BELLADONNA.get(), "crop");
		cropsBlockWithItem(EnchantedBlocks.SNOWBELL.get(), "cross");
		cropsBlockWithItem(EnchantedBlocks.ARTICHOKE.get(), "crop");
		cropsBlockWithItem(EnchantedBlocks.MANDRAKE.get(), "crop");
		cropsBlockWithItem(EnchantedBlocks.GARLIC.get(), "crop");
		cropsBlockWithItem(EnchantedBlocks.WOLFSBANE.get(), "cross");
		crossBlockWithItem(EnchantedBlocks.GLINT_WEED.get());
		crossBlockWithItem(EnchantedBlocks.EMBER_MOSS.get());
		getVariantBuilder(EnchantedBlocks.BLOOD_POPPY.get()).forAllStates(state -> {
			String name = "block/" + EnchantedBlocks.BLOOD_POPPY.get().getRegistryName().getPath();
			if(state.getValue(BloodPoppyBlock.FILLED)) name = name + "_filled";
			return ConfiguredModel.builder()
					.modelFile(models().withExistingParent(name, mcLoc("block/cross")).texture("cross", name))
					.build();
		});
		simpleItem(EnchantedBlocks.BLOOD_POPPY.get());
		simpleItem(EnchantedBlocks.SPANISH_MOSS.get());

	}

	private void kettleBlockWithItem(Block block) {
		getVariantBuilder(block).forAllStates(state -> ConfiguredModel.builder()
				.modelFile(models().getExistingFile(state.getValue(KettleBlock.TYPE) == 0 ? modLoc("block/kettle") : state.getValue(KettleBlock.TYPE) == 1 ? modLoc("block/kettle_hanging") : modLoc("block/kettle_hanging_beam")))
				.rotationY((int)state.getValue(KettleBlock.FACING).toYRot() % 360)
				.build()
		);
		simpleBlockItem(block, models().getExistingFile(modLoc("block/kettle")));
	}

	private void simpleItem(Block block) {
		String name = "/" + block.getRegistryName().getPath();
		itemModels().withExistingParent(ModelProvider.ITEM_FOLDER + name, mcLoc(ModelProvider.ITEM_FOLDER + "/generated")).texture("layer0", ModelProvider.BLOCK_FOLDER + name);
	}

	private void chalkBlockWithItem(Block block) {
		getVariantBuilder(block).forAllStates(state -> ConfiguredModel.builder()
						.modelFile(models().withExistingParent("block/glyph_" + (state.getValue(ChalkCircleBlock.GLYPH)%12), modLoc("block/glyph")).texture("top", "block/glyph_"+(state.getValue(ChalkCircleBlock.GLYPH)%12)))
						.rotationY((state.getValue(ChalkCircleBlock.GLYPH) % 4 * 90))
						.build()
				);
		itemModels().withExistingParent(block.getRegistryName().toString(), mcLoc(ModelProvider.ITEM_FOLDER + "/generated")).texture("layer0", modLoc(ModelProvider.ITEM_FOLDER + "/" + block.getRegistryName().getPath()));
	}

	private void goldChalkBlockWithItem(Block block) {
		VariantBlockStateBuilder builder = getVariantBuilder(block);
		String name = block.getRegistryName().getPath();

		ModelFile model = models().withExistingParent(name, modLoc("block/glyph")).texture("top", modLoc("block/glyph_gold"));
		for(int j = 0; j < 4; j++) {
			builder.partialState().with(GoldChalkBlock.GLYPH, j).setModels(new ConfiguredModel(model, 0, j*90, false));
		}

		itemModels().withExistingParent(block.getRegistryName().toString(), mcLoc(ModelProvider.ITEM_FOLDER + "/generated")).texture("layer0", modLoc(ModelProvider.ITEM_FOLDER + "/" + block.getRegistryName().getPath()));
	}

	private void simpleBlockWithItem(Block block) {
		simpleBlock(block);
		simpleBlockItem(block, models().getExistingFile(modLoc("block/" + block.getRegistryName().getPath())));
	}

	private void complexBlockWithItem(Block block) {
		ModelFile model = models().getExistingFile(modLoc("block/" + block.getRegistryName().getPath()));
		simpleBlock(block, model);
		simpleBlockItem(block, model);
	}

	private void slabBlockWithItem(SlabBlock block, Block parent) {
		ResourceLocation path = blockTexture(parent);
		slabBlock(block, path, blockTexture(parent));
		simpleBlockItem(block, models().getExistingFile(blockTexture(block)));
	}

	private void stairsBlockWithItem(StairBlock block, Block parent) {
		stairsBlock(block, blockTexture(parent));
		simpleBlockItem(block, models().getExistingFile(blockTexture(block)));
	}

	private void horizontalLitBlockWithItem(Block block, String onSuffix, int angleOffset) {
		String name = block.getRegistryName().getPath();

		ModelFile modelUnlit = models().getExistingFile(modLoc("block/" + name));
		ModelFile modelLit = models().getExistingFile(modLoc("block/" + name + onSuffix));

		getVariantBuilder(block).forAllStates(state -> ConfiguredModel.builder()
				.modelFile(state.getValue(BlockStateProperties.LIT) ? modelLit : modelUnlit)
				.rotationY((int)(state.getValue(BlockStateProperties.HORIZONTAL_FACING).toYRot() + angleOffset) % 360)
				.build());
		itemModels().getBuilder(block.getRegistryName().getPath()).parent(modelUnlit);
	}

	private void fumeFunnelWithItem(Block block) {
		String name = block.getRegistryName().getPath();

		VariantBlockStateBuilder builder = getVariantBuilder(block);
		String[] types = { "_left", "_top", "_right", "" };

		for(int type = 0; type < types.length; type++) {
			ModelFile modelUnlit = models().getExistingFile(modLoc("block/" + name + types[type]));
			ModelFile modelLit = (type == 3) ? modelUnlit : models().getExistingFile(modLoc("block/" + name + types[type] + "_on"));
			for(Direction dir : Plane.HORIZONTAL) {
				builder.partialState().with(FumeFunnelBlock.TYPE, type).with(FumeFunnelBlock.FACING, dir).with(FumeFunnelBlock.LIT, false)
						.setModels(new ConfiguredModel(modelUnlit, 0, (int)(dir.toYRot() + 180), false));
				builder.partialState().with(FumeFunnelBlock.TYPE, type).with(FumeFunnelBlock.FACING, dir).with(FumeFunnelBlock.LIT, true)
							.setModels(new ConfiguredModel(modelLit, 0, (int)(dir.toYRot() + 180) % 360, false));
			}
		}

		itemModels().getBuilder(block.getRegistryName().getPath()).parent(models().getExistingFile(modLoc("block/" + name + types[0])));
	}

	private void leafRandomBlockWithItem(Block block, int numVariants) {
		String name = block.getRegistryName().getPath();
		ConfiguredModel[] models = new ConfiguredModel[numVariants];
		for(int i = 0; i < numVariants; i++) {
			models[i] = new ConfiguredModel(models().getExistingFile(modLoc("block/" + name + "_" + i)));
		}
		getVariantBuilder(block).partialState().setModels(models);
		itemModels().getBuilder(block.getRegistryName().getPath()).parent(models().getExistingFile(modLoc("block/" + name + "_0")));
	}

	private void logBlockWithItem(RotatedPillarBlock block) {
		logBlock(block);
		simpleBlockItem(block, models().getExistingFile(blockTexture(block)));
	}

	private void crossBlock(Block block) {
		ResourceLocation path = blockTexture(block);
		simpleBlock(block, models().cross(path.toString(), path));
	}

	private void crossBlockWithItem(Block block) {
		crossBlock(block);
		simpleItem(block);
	}

	private void cropsBlockWithItem(CropsBlockAgeFive block, String parent) {
		String path = BlockModelProvider.BLOCK_FOLDER + "/" + block.getRegistryName().getPath() + "_stage_";
		getVariantBuilder(block).forAllStates(state -> ConfiguredModel.builder()
						.modelFile(models().withExistingParent(path + state.getValue(CropsBlockAgeFive.AGE_FIVE), mcLoc("block/" + parent)).texture(parent, path + state.getValue(CropsBlockAgeFive.AGE_FIVE)))
						.build());
	}

}
