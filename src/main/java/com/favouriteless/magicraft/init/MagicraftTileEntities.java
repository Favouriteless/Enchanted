package com.favouriteless.magicraft.init;

import com.favouriteless.magicraft.Magicraft;
import com.favouriteless.magicraft.tileentity.*;

import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class MagicraftTileEntities {

    public static final DeferredRegister<TileEntityType<?>> TILE_ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, Magicraft.MOD_ID);

    public static final RegistryObject<TileEntityType<ChalkGoldTileEntity>> CHALK_GOLD = TILE_ENTITY_TYPES.register("chalk_gold",
            () -> TileEntityType.Builder.create(ChalkGoldTileEntity::new, MagicraftBlocks.CHALK_GOLD.get()).build(null));

    public static final RegistryObject<TileEntityType<WitchOvenTileEntity>> WITCH_OVEN = TILE_ENTITY_TYPES.register("witch_oven",
            () -> TileEntityType.Builder.create(WitchOvenTileEntity::new, MagicraftBlocks.WITCH_OVEN.get()).build(null));

    public static final RegistryObject<TileEntityType<DistilleryTileEntity>> DISTILLERY = TILE_ENTITY_TYPES.register("distillery",
            () -> TileEntityType.Builder.create(DistilleryTileEntity::new, MagicraftBlocks.DISTILLERY.get()).build(null));
}
