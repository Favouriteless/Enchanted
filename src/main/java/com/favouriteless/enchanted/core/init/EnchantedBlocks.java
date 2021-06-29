package com.favouriteless.enchanted.core.init;

import com.favouriteless.enchanted.Enchanted;

import com.favouriteless.enchanted.common.blocks.*;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.entity.EntityType;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.ToIntFunction;

@Mod.EventBusSubscriber(modid = Enchanted.MOD_ID)
public class EnchantedBlocks {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Enchanted.MOD_ID);

    public static void initRender() {
        RenderTypeLookup.setRenderLayer(EnchantedBlocks.CHALK_GOLD.get(), RenderType.cutout());
        RenderTypeLookup.setRenderLayer(EnchantedBlocks.CHALK_WHITE.get(), RenderType.cutout());
        RenderTypeLookup.setRenderLayer(EnchantedBlocks.CHALK_RED.get(), RenderType.cutout());
        RenderTypeLookup.setRenderLayer(EnchantedBlocks.CHALK_PURPLE.get(), RenderType.cutout());

        //RenderTypeLookup.setRenderLayer(EnchantedBlocks.ROWAN_SAPLING.get(), RenderType.cutout());
        //RenderTypeLookup.setRenderLayer(EnchantedBlocks.HAWTHORN_SAPLING.get(), RenderType.cutout());
        //RenderTypeLookup.setRenderLayer(EnchantedBlocks.ALDER_SAPLING.get(), RenderType.cutout());
    }

    public static final RegistryObject<Block> LEATHER_BLOCK = BLOCKS.register("leather_block", () -> new Block(AbstractBlock.Properties.of(Material.WOOL, MaterialColor.COLOR_BROWN).strength(0.8F).sound(SoundType.WOOL)));

//    public static final RegistryObject<Block> ALTAR = BLOCKS.register("altar", Altar::new);
    public static final RegistryObject<Block> WITCH_OVEN = BLOCKS.register("witch_oven", () -> new WitchOvenBlock(AbstractBlock.Properties.copy(Blocks.ANVIL).strength(5.0F, 1200.0F).lightLevel(getLightValueLit(13))));
    public static final RegistryObject<Block> FUME_FUNNEL = BLOCKS.register("fume_funnel", () -> new FumeFunnel(AbstractBlock.Properties.copy(WITCH_OVEN.get())));
    public static final RegistryObject<Block> FUME_FUNNEL_FILTERED = BLOCKS.register("fume_funnel_filtered", () -> new FumeFunnel(AbstractBlock.Properties.copy(FUME_FUNNEL.get())));

    public static final RegistryObject<Block> DISTILLERY = BLOCKS.register("distillery", () -> new DistilleryBlock(AbstractBlock.Properties.copy(WITCH_OVEN.get())));

    public static final RegistryObject<Block> ROWAN_PLANKS = BLOCKS.register("rowan_planks", () -> new Block(AbstractBlock.Properties.copy(Blocks.OAK_PLANKS)));
    public static final RegistryObject<Block> ROWAN_LOG = BLOCKS.register("rowan_log", () -> log(MaterialColor.WOOD, MaterialColor.PODZOL));
    public static final RegistryObject<Block> ROWAN_LEAVES = BLOCKS.register("rowan_leaves", EnchantedBlocks::leaves);
    //public static final RegistryObject<Block> ROWAN_SAPLING = BLOCKS.register("rowan_sapling", () -> new SaplingBlock(new RowanTree(), AbstractBlock.Properties.copy(Blocks.OAK_SAPLING)));

    public static final RegistryObject<Block> HAWTHORN_PLANKS = BLOCKS.register("hawthorn_planks", () -> new Block(AbstractBlock.Properties.copy(Blocks.OAK_PLANKS)));
    public static final RegistryObject<Block> HAWTHORN_LOG = BLOCKS.register("hawthorn_log", () -> log(MaterialColor.CLAY, MaterialColor.CLAY));
    public static final RegistryObject<Block> HAWTHORN_LEAVES = BLOCKS.register("hawthorn_leaves", EnchantedBlocks::leaves);
    //public static final RegistryObject<Block> HAWTHORN_SAPLING = BLOCKS.register("hawthorn_sapling", () -> new SaplingBlock(new HawthornTree(), AbstractBlock.Properties.copy(Blocks.OAK_SAPLING)));

    public static final RegistryObject<Block> ALDER_PLANKS = BLOCKS.register("alder_planks", () -> new Block(AbstractBlock.Properties.copy(Blocks.OAK_PLANKS)));
    public static final RegistryObject<Block> ALDER_LOG = BLOCKS.register("alder_log", () -> log(MaterialColor.WOOD, MaterialColor.CLAY));
    public static final RegistryObject<Block> ALDER_LEAVES = BLOCKS.register("alder_leaves", EnchantedBlocks::leaves);
    //public static final RegistryObject<Block> ALDER_SAPLING = BLOCKS.register("alder_sapling", () -> new SaplingBlock(new AlderTree(), AbstractBlock.Properties.copy(Blocks.OAK_SAPLING)));

    public static final RegistryObject<Block> CHALK_GOLD = BLOCKS.register("chalk_gold", GoldChalkBlock::new);
    public static final RegistryObject<Block> CHALK_WHITE = BLOCKS.register("chalk_white", () -> new ChalkCircleBlock(null));
    public static final RegistryObject<Block> CHALK_RED = BLOCKS.register("chalk_red", () -> new ChalkCircleBlock(ParticleTypes.FLAME));
    public static final RegistryObject<Block> CHALK_PURPLE = BLOCKS.register("chalk_purple", () -> new ChalkCircleBlock(ParticleTypes.DRAGON_BREATH));

    //public static final RegistryObject<Block> DEMON_HEART = BLOCKS.register("demon_heart", DemonHeart::new);

    //-------------------------------------------------------- UTILITY FUNCTIONS FOR CREATING BLOCKS --------------------------------------------------------

    private static Boolean ocelotOrParrot(BlockState state, IBlockReader reader, BlockPos pos, EntityType<?> entity) {
        return entity == EntityType.OCELOT || entity == EntityType.PARROT;
    }
    private static boolean never(BlockState state, IBlockReader reader, BlockPos pos) {
        return false;
    }

    private static ToIntFunction<BlockState> getLightValueLit(int lightValue) {
        return (state) -> state.getValue(BlockStateProperties.LIT) ? lightValue : 0;
    }

    private static RotatedPillarBlock log(MaterialColor topColor, MaterialColor barkColor) {
        return new RotatedPillarBlock(AbstractBlock.Properties.of(Material.WOOD, (sideColor) -> sideColor.getValue(RotatedPillarBlock.AXIS) == Direction.Axis.Y ? topColor : barkColor).strength(2.0F).sound(SoundType.WOOD));
    }
    private static LeavesBlock leaves() {
        return new LeavesBlock(AbstractBlock.Properties.of(Material.LEAVES)
                .strength(0.2F)
                .randomTicks().sound(SoundType.GRASS)
                .noOcclusion()
                .isValidSpawn(EnchantedBlocks::ocelotOrParrot)
                .isSuffocating(EnchantedBlocks::never)
                .isViewBlocking(EnchantedBlocks::never));
    }

}
