/*
 * Copyright (c) 2022. Favouriteless
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

package com.favouriteless.enchanted.common.init;

import com.favouriteless.enchanted.Enchanted;
import com.favouriteless.enchanted.common.blocks.*;
import com.favouriteless.enchanted.common.blocks.altar.AltarBlock;
import com.favouriteless.enchanted.common.blocks.altar.CandelabraBlock;
import com.favouriteless.enchanted.common.blocks.altar.ChaliceBlock;
import com.favouriteless.enchanted.common.blocks.altar.ChaliceBlockMilk;
import com.favouriteless.enchanted.common.blocks.chalk.ChalkCircleBlock;
import com.favouriteless.enchanted.common.blocks.chalk.GoldChalkBlock;
import com.favouriteless.enchanted.common.blocks.crops.*;
import com.favouriteless.enchanted.common.world.features.AlderTree;
import com.favouriteless.enchanted.common.world.features.HawthornTree;
import com.favouriteless.enchanted.common.world.features.RowanTree;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.core.Direction;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.ToIntFunction;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.RegistryObject;

public class EnchantedBlocks {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Enchanted.MOD_ID);

    public static void initRender() {
        ItemBlockRenderTypes.setRenderLayer(EnchantedBlocks.CHALK_GOLD.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(EnchantedBlocks.CHALK_WHITE.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(EnchantedBlocks.CHALK_RED.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(EnchantedBlocks.CHALK_PURPLE.get(), RenderType.cutout());

        ItemBlockRenderTypes.setRenderLayer(EnchantedBlocks.ROWAN_SAPLING.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(EnchantedBlocks.HAWTHORN_SAPLING.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(EnchantedBlocks.ALDER_SAPLING.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(EnchantedBlocks.BELLADONNA.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(EnchantedBlocks.ARTICHOKE.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(EnchantedBlocks.MANDRAKE.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(EnchantedBlocks.GLINT_WEED.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(EnchantedBlocks.EMBER_MOSS.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(EnchantedBlocks.SPANISH_MOSS.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(EnchantedBlocks.WOLFSBANE.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(EnchantedBlocks.GARLIC.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(EnchantedBlocks.SNOWBELL.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(EnchantedBlocks.BLOOD_POPPY.get(), RenderType.cutout());
    }

    public static final RegistryObject<Block> ALTAR = BLOCKS.register("altar", () -> new AltarBlock(Properties.of(EnchantedMaterials.ALTAR).requiresCorrectToolForDrops().strength(1.5F, 6.0F)));
    public static final RegistryObject<Block> WITCH_OVEN = BLOCKS.register("witch_oven", () -> new WitchOvenBlock(Properties.copy(Blocks.ANVIL).strength(5.0F, 1200.0F).lightLevel(getLightValueLit(13)).noOcclusion()));
    public static final RegistryObject<Block> FUME_FUNNEL = BLOCKS.register("fume_funnel", () -> new FumeFunnelBlock(Properties.copy(WITCH_OVEN.get())));
    public static final RegistryObject<Block> FUME_FUNNEL_FILTERED = BLOCKS.register("fume_funnel_filtered", () -> new FumeFunnelBlock(Properties.copy(FUME_FUNNEL.get())));
    public static final RegistryObject<Block> DISTILLERY = BLOCKS.register("distillery", () -> new DistilleryBlock(Properties.copy(WITCH_OVEN.get())));
    public static final RegistryObject<Block> WITCH_CAULDRON = BLOCKS.register("witch_cauldron", () -> new WitchCauldronBlock(Properties.copy(Blocks.CAULDRON).noOcclusion()));
    public static final RegistryObject<Block> KETTLE = BLOCKS.register("kettle", () -> new KettleBlock(Properties.copy(Blocks.CAULDRON).noOcclusion()));
    public static final RegistryObject<Block> SPINNING_WHEEL = BLOCKS.register("spinning_wheel", () -> new SpinningWheelBlock(Properties.copy(Blocks.OAK_PLANKS).noOcclusion()));
    public static final RegistryObject<Block> POPPET_SHELF = BLOCKS.register("poppet_shelf", () -> new PoppetShelfBlock(Properties.copy(Blocks.ENCHANTING_TABLE).noOcclusion()));

    public static final RegistryObject<Block> CHALICE = BLOCKS.register("chalice", () -> new ChaliceBlock(Properties.of(Material.STONE).strength(1.0F, 6.0F).noOcclusion(), false));
    public static final RegistryObject<Block> CHALICE_FILLED = BLOCKS.register("chalice_filled", () -> new ChaliceBlock(Properties.copy(CHALICE.get()), true));
    public static final RegistryObject<Block> CHALICE_FILLED_MILK = BLOCKS.register("chalice_filled_milk", () -> new ChaliceBlockMilk(Properties.copy(CHALICE.get())));
    public static final RegistryObject<Block> CANDELABRA = BLOCKS.register("candelabra", () -> new CandelabraBlock(Properties.copy(CHALICE.get()).lightLevel((state) -> 14)));

    public static final RegistryObject<Block> ROWAN_PLANKS = BLOCKS.register("rowan_planks", () -> new Block(Properties.copy(Blocks.OAK_PLANKS)));
    public static final RegistryObject<StairBlock> ROWAN_STAIRS = BLOCKS.register("rowan_stairs", () -> new StairBlock(ROWAN_PLANKS.get().defaultBlockState(), Properties.copy(ROWAN_PLANKS.get())));
    public static final RegistryObject<SlabBlock> ROWAN_SLAB = BLOCKS.register("rowan_slab", () -> new SlabBlock(Properties.copy(Blocks.OAK_SLAB)));
    public static final RegistryObject<RotatedPillarBlock> ROWAN_LOG = BLOCKS.register("rowan_log", () -> log(MaterialColor.WOOD, MaterialColor.PODZOL));
    public static final RegistryObject<Block> ROWAN_LEAVES = BLOCKS.register("rowan_leaves", () -> new LeavesBlock(Properties.copy(Blocks.OAK_LEAVES)));
    public static final RegistryObject<SaplingBlock> ROWAN_SAPLING = BLOCKS.register("rowan_sapling", () -> new SaplingBlock(new RowanTree(), Block.Properties.copy(Blocks.OAK_SAPLING)));

    public static final RegistryObject<Block> HAWTHORN_PLANKS = BLOCKS.register("hawthorn_planks", () -> new Block(Properties.copy(Blocks.OAK_PLANKS)));
    public static final RegistryObject<StairBlock> HAWTHORN_STAIRS = BLOCKS.register("hawthorn_stairs", () -> new StairBlock(HAWTHORN_PLANKS.get().defaultBlockState(), Properties.copy(HAWTHORN_PLANKS.get())));
    public static final RegistryObject<SlabBlock> HAWTHORN_SLAB = BLOCKS.register("hawthorn_slab", () -> new SlabBlock(Properties.copy(Blocks.OAK_SLAB)));
    public static final RegistryObject<RotatedPillarBlock> HAWTHORN_LOG = BLOCKS.register("hawthorn_log", () -> log(MaterialColor.CLAY, MaterialColor.CLAY));
    public static final RegistryObject<Block> HAWTHORN_LEAVES = BLOCKS.register("hawthorn_leaves", () -> new LeavesBlock(Properties.copy(Blocks.OAK_LEAVES)));
    public static final RegistryObject<SaplingBlock> HAWTHORN_SAPLING = BLOCKS.register("hawthorn_sapling", () -> new SaplingBlock(new HawthornTree(), Block.Properties.copy(Blocks.OAK_SAPLING)));

    public static final RegistryObject<Block> ALDER_PLANKS = BLOCKS.register("alder_planks", () -> new Block(Properties.copy(Blocks.OAK_PLANKS)));
    public static final RegistryObject<StairBlock> ALDER_STAIRS = BLOCKS.register("alder_stairs", () -> new StairBlock(ALDER_PLANKS.get().defaultBlockState(), Properties.copy(ALDER_PLANKS.get())));
    public static final RegistryObject<SlabBlock> ALDER_SLAB = BLOCKS.register("alder_slab", () -> new SlabBlock(Properties.copy(Blocks.OAK_SLAB)));
    public static final RegistryObject<RotatedPillarBlock> ALDER_LOG = BLOCKS.register("alder_log", () -> log(MaterialColor.WOOD, MaterialColor.CLAY));
    public static final RegistryObject<Block> ALDER_LEAVES = BLOCKS.register("alder_leaves", () -> new LeavesBlock(Properties.copy(Blocks.OAK_LEAVES)));
    public static final RegistryObject<SaplingBlock> ALDER_SAPLING = BLOCKS.register("alder_sapling", () -> new SaplingBlock(new AlderTree(), Block.Properties.copy(Blocks.OAK_SAPLING)));

    public static final RegistryObject<CropsBlockAgeFive> BELLADONNA = BLOCKS.register("belladonna", () -> new BelladonnaBlock(Properties.copy(Blocks.WHEAT)));
    public static final RegistryObject<CropsBlockAgeFive> SNOWBELL = BLOCKS.register("snowbell", () -> new SnowbellBlock(Properties.copy(Blocks.WHEAT)));
    public static final RegistryObject<CropsBlockAgeFive> ARTICHOKE = BLOCKS.register("artichoke", () -> new ArtichokeBlock(Properties.copy(Blocks.WHEAT).sound(SoundType.LILY_PAD)));
    public static final RegistryObject<CropsBlockAgeFive> MANDRAKE = BLOCKS.register("mandrake", () -> new MandrakeBlock(Properties.copy(Blocks.WHEAT)));
    public static final RegistryObject<CropsBlockAgeFive> GARLIC = BLOCKS.register("garlic", () -> new GarlicBlock(Properties.copy(Blocks.WHEAT)));
    public static final RegistryObject<CropsBlockAgeFive> WOLFSBANE = BLOCKS.register("wolfsbane", () -> new WolfsbaneBlock(Properties.copy(Blocks.WHEAT)));
    public static final RegistryObject<Block> GLINT_WEED = BLOCKS.register("glint_weed", () -> new GlintWeedBlock(Properties.copy(Blocks.POPPY).lightLevel((a) -> 14).randomTicks()));
    public static final RegistryObject<Block> EMBER_MOSS = BLOCKS.register("ember_moss", () -> new EmberMossBlock(Properties.copy(Blocks.POPPY).lightLevel((a) -> 6).randomTicks()));
    public static final RegistryObject<Block> SPANISH_MOSS = BLOCKS.register("spanish_moss", () -> new SpanishMossBlock(Properties.copy(Blocks.VINE)));
    public static final RegistryObject<Block> BLOOD_POPPY = BLOCKS.register("blood_poppy", () -> new BloodPoppyBlock(Properties.copy(Blocks.POPPY)));

    public static final RegistryObject<Block> CHALK_GOLD = BLOCKS.register("chalk_gold", GoldChalkBlock::new);
    public static final RegistryObject<Block> CHALK_WHITE = BLOCKS.register("chalk_white", () -> new ChalkCircleBlock(null));
    public static final RegistryObject<Block> CHALK_RED = BLOCKS.register("chalk_red", () -> new ChalkCircleBlock(ParticleTypes.FLAME));
    public static final RegistryObject<Block> CHALK_PURPLE = BLOCKS.register("chalk_purple", () -> new ChalkCircleBlock(ParticleTypes.DRAGON_BREATH));

    //public static final RegistryObject<Block> DEMON_HEART = BLOCKS.register("demon_heart", DemonHeart::new);

    //-------------------------------------------------------- UTILITY FUNCTIONS FOR CREATING BLOCKS --------------------------------------------------------

    private static ToIntFunction<BlockState> getLightValueLit(int lightValue) {
        return (state) -> state.getValue(BlockStateProperties.LIT) ? lightValue : 0;
    }

    private static RotatedPillarBlock log(MaterialColor topColor, MaterialColor barkColor) {
        return new RotatedPillarBlock(Properties.of(Material.WOOD, (sideColor) -> sideColor.getValue(RotatedPillarBlock.AXIS) == Direction.Axis.Y ? topColor : barkColor).strength(2.0F).sound(SoundType.WOOD));
    }

}
