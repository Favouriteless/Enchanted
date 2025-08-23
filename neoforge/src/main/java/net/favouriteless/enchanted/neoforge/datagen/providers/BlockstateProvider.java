package net.favouriteless.enchanted.neoforge.datagen.providers;

import net.favouriteless.enchanted.common.Enchanted;
import net.favouriteless.enchanted.common.blocks.FumeFunnelBlock;
import net.favouriteless.enchanted.common.blocks.KettleBlock;
import net.favouriteless.enchanted.common.blocks.chalk.ChalkCircleBlock;
import net.favouriteless.enchanted.common.blocks.chalk.GoldChalkBlock;
import net.favouriteless.enchanted.common.blocks.crops.BloodPoppyBlock;
import net.favouriteless.enchanted.common.blocks.crops.CropBlockAgeFive;
import net.favouriteless.enchanted.common.init.EBlocks;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Plane;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.neoforge.client.model.generators.*;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class BlockstateProvider extends BlockStateProvider {

	public BlockstateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
		super(output, Enchanted.MOD_ID, exFileHelper);
	}

	@Override
	protected void registerStatesAndModels() {
		logWithItem(EBlocks.ROWAN_LOG.get());
		logWithItem(EBlocks.HAWTHORN_LOG.get());
		logWithItem(EBlocks.ALDER_LOG.get());
		logWithItem(EBlocks.WICKER_BUNDLE.get());
		logWithItem(EBlocks.STRIPPED_ALDER_LOG.get());
		logWithItem(EBlocks.STRIPPED_ROWAN_LOG.get());
		logWithItem(EBlocks.STRIPPED_HAWTHORN_LOG.get());
		fenceWithItem(EBlocks.ALDER_FENCE.get(), blockTexture(EBlocks.ALDER_PLANKS.get()));
		fenceWithItem(EBlocks.ROWAN_FENCE.get(), blockTexture(EBlocks.ROWAN_PLANKS.get()));
		fenceWithItem(EBlocks.HAWTHORN_FENCE.get(), blockTexture(EBlocks.HAWTHORN_PLANKS.get()));
		fenceGateWithItem(EBlocks.ALDER_FENCE_GATE.get(), blockTexture(EBlocks.ALDER_PLANKS.get()));
		fenceGateWithItem(EBlocks.ROWAN_FENCE_GATE.get(), blockTexture(EBlocks.ROWAN_PLANKS.get()));
		fenceGateWithItem(EBlocks.HAWTHORN_FENCE_GATE.get(), blockTexture(EBlocks.HAWTHORN_PLANKS.get()));
		buttonWithItem(EBlocks.ALDER_BUTTON.get(), blockTexture(EBlocks.ALDER_PLANKS.get()));
		buttonWithItem(EBlocks.HAWTHORN_BUTTON.get(), blockTexture(EBlocks.HAWTHORN_PLANKS.get()));
		buttonWithItem(EBlocks.ROWAN_BUTTON.get(), blockTexture(EBlocks.ROWAN_PLANKS.get()));
		pressurePlateWithItem(EBlocks.ROWAN_PRESSURE_PLATE.get(), blockTexture(EBlocks.ROWAN_PLANKS.get()));
		pressurePlateWithItem(EBlocks.ALDER_PRESSURE_PLATE.get(), blockTexture(EBlocks.ALDER_PLANKS.get()));
		pressurePlateWithItem(EBlocks.HAWTHORN_PRESSURE_PLATE.get(), blockTexture(EBlocks.HAWTHORN_PLANKS.get()));
		simpleWithItem(EBlocks.ROWAN_PLANKS.get());
		simpleWithItem(EBlocks.HAWTHORN_PLANKS.get());
		simpleWithItem(EBlocks.ALDER_PLANKS.get());
		slabWithItem(EBlocks.ROWAN_SLAB.get(), EBlocks.ROWAN_PLANKS.get());
		slabWithItem(EBlocks.HAWTHORN_SLAB.get(), EBlocks.HAWTHORN_PLANKS.get());
		slabWithItem(EBlocks.ALDER_SLAB.get(), EBlocks.ALDER_PLANKS.get());
		stairsWithItem(EBlocks.ROWAN_STAIRS.get(), EBlocks.ROWAN_PLANKS.get());
		stairsWithItem(EBlocks.HAWTHORN_STAIRS.get(), EBlocks.HAWTHORN_PLANKS.get());
		stairsWithItem(EBlocks.ALDER_STAIRS.get(), EBlocks.ALDER_PLANKS.get());
		leafRandomWithItem(EBlocks.ROWAN_LEAVES.get(), 4);
		leafRandomWithItem(EBlocks.HAWTHORN_LEAVES.get(), 4);
		leafRandomWithItem(EBlocks.ALDER_LEAVES.get(), 4);
		crossWithItem(EBlocks.ROWAN_SAPLING.get());
		crossWithItem(EBlocks.HAWTHORN_SAPLING.get());
		crossWithItem(EBlocks.ALDER_SAPLING.get());

		chalkWithItem(EBlocks.RITUAL_CHALK.get());
		chalkWithItem(EBlocks.NETHER_CHALK.get());
		chalkWithItem(EBlocks.OTHERWHERE_CHALK.get());
		goldChalkWithItem(EBlocks.GOLDEN_CHALK.get());

		simpleBlockItem(EBlocks.ALTAR.get(), models().getExistingFile(modLoc("block/altar")));
		horizontalLitWithItem(EBlocks.WITCH_OVEN.get(), "_on", 180);
		fumeFunnelWithItem(EBlocks.FUME_FUNNEL.get());
		fumeFunnelWithItem(EBlocks.FUME_FUNNEL_FILTERED.get());
		horizontalLitWithItem(EBlocks.DISTILLERY.get(), "", 180);
		complexWithItem(EBlocks.WITCH_CAULDRON.get());
		complexWithItem(EBlocks.CHALICE.get());
		complexWithItem(EBlocks.CHALICE_FILLED.get());
		complexWithItem(EBlocks.INFINITY_EGG.get(), "block/dragon_egg");
		complexWithItem(EBlocks.CANDELABRA.get());
		kettleWithItem(EBlocks.KETTLE.get());
		complexWithItem(EBlocks.POPPET_SHELF.get());

		cropsWithItem(EBlocks.BELLADONNA.get(), "crop");
		cropsWithItem(EBlocks.SNOWBELL.get(), "cross");
		cropsWithItem(EBlocks.WATER_ARTICHOKE.get(), "crop");
		cropsWithItem(EBlocks.MANDRAKE.get(), "crop");
		cropsWithItem(EBlocks.GARLIC.get(), "crop");
		cropsWithItem(EBlocks.WOLFSBANE.get(), "cross");
		crossWithItem(EBlocks.GLINT_WEED.get());
		crossWithItem(EBlocks.EMBER_MOSS.get());
		getVariantBuilder(EBlocks.BLOOD_POPPY.get()).forAllStates(state -> {
			String name = "block/" + BuiltInRegistries.BLOCK.getKey(EBlocks.BLOOD_POPPY.get()).getPath();
			if(state.getValue(BloodPoppyBlock.FILLED)) name = name + "_filled";
			return ConfiguredModel.builder()
					.modelFile(models().withExistingParent(name, mcLoc("block/cross"))
							.texture("cross", name)
							.renderType("minecraft:cutout"))
					.build();
		});
		simpleItem(EBlocks.BLOOD_POPPY.get());
		simpleItem(EBlocks.SPANISH_MOSS.get());

	}

	private void kettleWithItem(Block block) {
		getVariantBuilder(block).forAllStates(state -> ConfiguredModel.builder()
				.modelFile(models().getExistingFile(
                        state.getValue(KettleBlock.TYPE) == KettleBlock.Type.GROUND ? modLoc("block/kettle") :
                        state.getValue(KettleBlock.TYPE) == KettleBlock.Type.HANGING ? modLoc("block/kettle_hanging") : modLoc("block/kettle_hanging_beam"))
                )
				.rotationY((int)state.getValue(KettleBlock.FACING).toYRot() % 360)
				.build()
		);
		simpleBlockItem(block, models().getExistingFile(modLoc("block/kettle")));
	}

	private void simpleItem(Block block) {
		String name = BuiltInRegistries.BLOCK.getKey(block).getPath();
		itemModels()
				.withExistingParent(ModelProvider.ITEM_FOLDER + "/" + name, mcLoc(ModelProvider.ITEM_FOLDER + "/generated"))
				.texture("layer0", ModelProvider.BLOCK_FOLDER + "/" + name);
	}

	private void chalkWithItem(Block block) {
		getVariantBuilder(block).forAllStates(state -> ConfiguredModel.builder()
						.modelFile(models().withExistingParent("block/glyph_" + (state.getValue(ChalkCircleBlock.GLYPH)%12), modLoc("block/glyph")).texture("top", "block/glyph_"+(state.getValue(ChalkCircleBlock.GLYPH)%12)))
						.rotationY((state.getValue(ChalkCircleBlock.GLYPH) % 4 * 90))
						.build()
				);
		itemModels()
				.withExistingParent(BuiltInRegistries.BLOCK.getKey(block).toString(), mcLoc(ModelProvider.ITEM_FOLDER + "/generated"))
				.texture("layer0", modLoc(ModelProvider.ITEM_FOLDER + "/" + BuiltInRegistries.BLOCK.getKey(block).getPath()));
	}

	private void goldChalkWithItem(Block block) {
		VariantBlockStateBuilder builder = getVariantBuilder(block);
		String name = BuiltInRegistries.BLOCK.getKey(block).getPath();

		ModelFile model = models().withExistingParent(name, modLoc("block/glyph")).texture("top", modLoc("block/glyph_gold"));
		for(int j = 0; j < 4; j++) {
			builder.partialState().with(GoldChalkBlock.GLYPH, j).setModels(new ConfiguredModel(model, 0, j*90, false));
		}

		itemModels()
				.withExistingParent(BuiltInRegistries.BLOCK.getKey(block).toString(), mcLoc(ModelProvider.ITEM_FOLDER + "/generated"))
				.texture("layer0", modLoc(ModelProvider.ITEM_FOLDER + "/" + name));
	}

	private void simpleWithItem(Block block) {
		simpleBlock(block);
		simpleBlockItem(block, models().getExistingFile(modLoc("block/" + BuiltInRegistries.BLOCK.getKey(block).getPath())));
	}

	private void complexWithItem(Block block) {
		ModelFile model = models().getExistingFile(modLoc("block/" + BuiltInRegistries.BLOCK.getKey(block).getPath()));
		simpleBlock(block, model);
		simpleBlockItem(block, model);
	}

	private void complexWithItem(Block block, String location) {
		ModelFile model = models().getExistingFile(mcLoc(location));
		simpleBlock(block, model);
		simpleBlockItem(block, model);
	}

	private void slabWithItem(SlabBlock block, Block parent) {
		ResourceLocation path = blockTexture(parent);
		slabBlock(block, path, blockTexture(parent));
		simpleBlockItem(block, models().getExistingFile(blockTexture(block)));
	}

	private void stairsWithItem(StairBlock block, Block parent) {
		stairsBlock(block, blockTexture(parent));
		simpleBlockItem(block, models().getExistingFile(blockTexture(block)));
	}

	private void horizontalLitWithItem(Block block, String onSuffix, int angleOffset) {
		String name = BuiltInRegistries.BLOCK.getKey(block).getPath();

		ModelFile modelUnlit = models().getExistingFile(modLoc("block/" + name));
		ModelFile modelLit = models().getExistingFile(modLoc("block/" + name + onSuffix));

		getVariantBuilder(block).forAllStates(state -> ConfiguredModel.builder()
				.modelFile(state.getValue(BlockStateProperties.LIT) ? modelLit : modelUnlit)
				.rotationY((int)(state.getValue(BlockStateProperties.HORIZONTAL_FACING).toYRot() + angleOffset) % 360)
				.build());
		itemModels().getBuilder(name)
				.parent(modelUnlit);
	}

	private void fumeFunnelWithItem(Block block) {
		String name = BuiltInRegistries.BLOCK.getKey(block).getPath();

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

		itemModels().getBuilder(name)
				.parent(models().getExistingFile(modLoc("block/" + name + types[0])));
	}

	private void leafRandomWithItem(Block block, int numVariants) {
		String name = BuiltInRegistries.BLOCK.getKey(block).getPath();
		ConfiguredModel[] models = new ConfiguredModel[numVariants];
		for(int i = 0; i < numVariants; i++) {
			models[i] = new ConfiguredModel(models().getExistingFile(modLoc("block/" + name + "_" + i)));
		}
		getVariantBuilder(block).partialState().setModels(models);
		itemModels().getBuilder(name)
				.parent(models().getExistingFile(modLoc("block/" + name + "_0")));
	}

	private void logWithItem(RotatedPillarBlock block) {
		logBlock(block);
		simpleBlockItem(block, models().getExistingFile(blockTexture(block)));
	}

	private void fenceWithItem(FenceBlock block, ResourceLocation texture) {
		fenceBlock(block, texture);
		ResourceLocation key = BuiltInRegistries.BLOCK.getKey(block);
		itemModels().getBuilder(key.getPath())
				.parent(models().getExistingFile(mcLoc("block/fence_inventory")))
				.texture("texture", texture);
	}

	private void fenceGateWithItem(FenceGateBlock block, ResourceLocation texture) {
		fenceGateBlock(block, texture);
		ResourceLocation key = BuiltInRegistries.BLOCK.getKey(block);
		itemModels().getBuilder(key.getPath()).parent(models().getExistingFile(modLoc("block/" + key.getPath())));
	}

	private void buttonWithItem(ButtonBlock block, ResourceLocation texture) {
		buttonBlock(block, texture);
		ResourceLocation key = BuiltInRegistries.BLOCK.getKey(block);
		itemModels().getBuilder(key.getPath())
				.parent(models().getExistingFile(mcLoc("block/button_inventory")))
				.texture("texture", texture);;
	}

	private void pressurePlateWithItem(PressurePlateBlock block, ResourceLocation texture) {
		pressurePlateBlock(block, texture);
		ResourceLocation key = BuiltInRegistries.BLOCK.getKey(block);
		itemModels().getBuilder(key.getPath()).parent(models().getExistingFile(modLoc("block/" + key.getPath())));
	}

	private void cross(Block block) {
		ResourceLocation path = blockTexture(block);
		simpleBlock(block, models().cross(path.toString(), path).renderType(ResourceLocation.withDefaultNamespace("cutout")));
	}

	private void crossWithItem(Block block) {
		cross(block);
		simpleItem(block);
	}

	private void cropsWithItem(CropBlockAgeFive block, String parent) {
		String path = BlockModelProvider.BLOCK_FOLDER + "/" + BuiltInRegistries.BLOCK.getKey(block).getPath() + "_stage_";
		getVariantBuilder(block).forAllStates(state -> ConfiguredModel.builder()
						.modelFile(models()
								.withExistingParent(path + state.getValue(CropBlockAgeFive.AGE_FIVE), mcLoc("block/" + parent))
								.texture(parent, path + state.getValue(CropBlockAgeFive.AGE_FIVE))
								.renderType(ResourceLocation.withDefaultNamespace("cutout")))
						.build());
	}

}
