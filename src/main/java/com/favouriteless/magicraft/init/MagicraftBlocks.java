package com.favouriteless.magicraft.init;

import com.favouriteless.magicraft.Magicraft;
import com.favouriteless.magicraft.blocks.*;
import com.favouriteless.magicraft.world.feature.*;

import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.Items;
import net.minecraft.particles.ParticleTypes;
import net.minecraftforge.common.Tags;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class MagicraftBlocks {

    public static final DeferredRegister<Block> BLOCKS = new DeferredRegister<>(ForgeRegistries.BLOCKS, Magicraft.MOD_ID);
    public static BlockColors BLOCK_COLORS;

    public static void initRender(FMLClientSetupEvent event) {
        RenderTypeLookup.setRenderLayer(MagicraftBlocks.CHALK_GOLD.get(), RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(MagicraftBlocks.CHALK_WHITE.get(), RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(MagicraftBlocks.CHALK_RED.get(), RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(MagicraftBlocks.CHALK_PURPLE.get(), RenderType.getCutout());

        RenderTypeLookup.setRenderLayer(MagicraftBlocks.ROWAN_SAPLING.get(), RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(MagicraftBlocks.HAWTHORN_SAPLING.get(), RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(MagicraftBlocks.ALDER_SAPLING.get(), RenderType.getCutout());

        BLOCK_COLORS = event.getMinecraftSupplier().get().getBlockColors();

        BLOCK_COLORS.register((a, b, c, d) -> -16777216 | 240 << 16 | 240 << 8 | 240, CHALK_WHITE.get());
        BLOCK_COLORS.register((a, b, c, d) -> -16777216 | 128 << 16 | 24 << 8 | 24, CHALK_RED.get());
        BLOCK_COLORS.register((a, b, c, d) -> -16777216 | 96 << 16 | 52 << 8 | 153, CHALK_PURPLE.get());

    }

    public static final RegistryObject<Block> LEATHER_BLOCK = BLOCKS.register("leather_block", LeatherBlock::new);

    public static final RegistryObject<Block> WITCH_OVEN = BLOCKS.register("witch_oven", WitchOven::new);
    public static final RegistryObject<Block> FUME_FUNNEL = BLOCKS.register("fume_funnel", FumeFunnel::new);
    public static final RegistryObject<Block> FUME_FUNNEL_FILTERED = BLOCKS.register("fume_funnel_filtered", FumeFunnelFiltered::new);
    public static final RegistryObject<Block> DISTILLERY = BLOCKS.register("distillery", Distillery::new);

    public static final RegistryObject<Block> ROWAN_PLANKS = BLOCKS.register("rowan_planks", () -> new Block(Block.Properties.from(Blocks.OAK_PLANKS)));
    public static final RegistryObject<Block> ROWAN_LOG = BLOCKS.register("rowan_log", () -> new LogBlock(MaterialColor.WOOD, Block.Properties.from(Blocks.OAK_LOG)));
    public static final RegistryObject<Block> ROWAN_LEAVES = BLOCKS.register("rowan_leaves", () -> new LeavesBlock(Block.Properties.create(Material.LEAVES).hardnessAndResistance(0.2F).tickRandomly().sound(SoundType.PLANT).notSolid()));
    public static final RegistryObject<Block> ROWAN_SAPLING = BLOCKS.register("rowan_sapling", () -> new SaplingBase(RowanTree::new, Block.Properties.from(Blocks.OAK_SAPLING)));

    public static final RegistryObject<Block> HAWTHORN_PLANKS = BLOCKS.register("hawthorn_planks", () -> new Block(Block.Properties.from(Blocks.OAK_PLANKS)));
    public static final RegistryObject<Block> HAWTHORN_LOG = BLOCKS.register("hawthorn_log", () -> new LogBlock(MaterialColor.WOOD, Block.Properties.from(Blocks.OAK_LOG)));
    public static final RegistryObject<Block> HAWTHORN_LEAVES = BLOCKS.register("hawthorn_leaves", () -> new LeavesBlock(Block.Properties.create(Material.LEAVES).hardnessAndResistance(0.2F).tickRandomly().sound(SoundType.PLANT).notSolid()));
    public static final RegistryObject<Block> HAWTHORN_SAPLING = BLOCKS.register("hawthorn_sapling", () -> new SaplingBase(HawthornTree::new, Block.Properties.from(Blocks.OAK_SAPLING)));

    public static final RegistryObject<Block> ALDER_PLANKS = BLOCKS.register("alder_planks", () -> new Block(Block.Properties.from(Blocks.OAK_PLANKS)));
    public static final RegistryObject<Block> ALDER_LOG = BLOCKS.register("alder_log", () -> new LogBlock(MaterialColor.WOOD, Block.Properties.from(Blocks.OAK_LOG)));
    public static final RegistryObject<Block> ALDER_LEAVES = BLOCKS.register("alder_leaves", () -> new LeavesBlock(Block.Properties.create(Material.LEAVES).hardnessAndResistance(0.2F).tickRandomly().sound(SoundType.PLANT).notSolid()));
    public static final RegistryObject<Block> ALDER_SAPLING = BLOCKS.register("alder_sapling", () -> new SaplingBase(AlderTree::new, Block.Properties.from(Blocks.OAK_SAPLING)));

    public static final RegistryObject<Block> CHALK_GOLD = BLOCKS.register("chalk_gold", ChalkGold::new);
    public static final RegistryObject<Block> CHALK_WHITE = BLOCKS.register("chalk_white", () -> new ChalkCircle(MagicraftMaterials.WHITECHALK, null));
    public static final RegistryObject<Block> CHALK_RED = BLOCKS.register("chalk_red", () -> new ChalkCircle(MagicraftMaterials.REDCHALK, ParticleTypes.FLAME));
    public static final RegistryObject<Block> CHALK_PURPLE = BLOCKS.register("chalk_purple", () -> new ChalkCircle(MagicraftMaterials.PURPLECHALK, ParticleTypes.DRAGON_BREATH));

    public static final RegistryObject<Block> DEMON_HEART = BLOCKS.register("demon_heart", DemonHeart::new);

}
