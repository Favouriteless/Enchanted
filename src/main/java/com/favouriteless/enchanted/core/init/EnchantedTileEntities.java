package com.favouriteless.enchanted.core.init;

import com.favouriteless.enchanted.Enchanted;
import com.favouriteless.enchanted.common.tileentity.*;

import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class EnchantedTileEntities {

    public static final DeferredRegister<TileEntityType<?>> TILE_ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, Enchanted.MOD_ID);

    public static final RegistryObject<TileEntityType<ChalkGoldTileEntity>> CHALK_GOLD = TILE_ENTITY_TYPES.register("chalk_gold",
            () -> TileEntityType.Builder.of(ChalkGoldTileEntity::new, EnchantedBlocks.CHALK_GOLD.get()).build(null));

    public static final RegistryObject<TileEntityType<WitchOvenTileEntity>> WITCH_OVEN = TILE_ENTITY_TYPES.register("witch_oven",
            () -> TileEntityType.Builder.of(WitchOvenTileEntity::new, EnchantedBlocks.WITCH_OVEN.get()).build(null));

    public static final RegistryObject<TileEntityType<DistilleryTileEntity>> DISTILLERY = TILE_ENTITY_TYPES.register("distillery",
            () -> TileEntityType.Builder.of(DistilleryTileEntity::new, EnchantedBlocks.DISTILLERY.get()).build(null));
}
