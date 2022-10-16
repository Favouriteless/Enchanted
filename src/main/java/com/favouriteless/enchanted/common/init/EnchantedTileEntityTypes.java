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
import com.favouriteless.enchanted.common.tileentity.*;

import com.favouriteless.enchanted.common.tileentity.AltarTileEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class EnchantedTileEntityTypes {

    public static final DeferredRegister<BlockEntityType<?>> TILE_ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, Enchanted.MOD_ID);

    public static final RegistryObject<BlockEntityType<ChalkGoldTileEntity>> CHALK_GOLD = TILE_ENTITY_TYPES.register("chalk_gold",
            () -> BlockEntityType.Builder.of(ChalkGoldTileEntity::new, EnchantedBlocks.CHALK_GOLD.get()).build(null));

    public static final RegistryObject<BlockEntityType<WitchOvenTileEntity>> WITCH_OVEN = TILE_ENTITY_TYPES.register("witch_oven",
            () -> BlockEntityType.Builder.of(WitchOvenTileEntity::new, EnchantedBlocks.WITCH_OVEN.get()).build(null));

    public static final RegistryObject<BlockEntityType<DistilleryTileEntity>> DISTILLERY = TILE_ENTITY_TYPES.register("distillery",
            () -> BlockEntityType.Builder.of(DistilleryTileEntity::new, EnchantedBlocks.DISTILLERY.get()).build(null));

    public static final RegistryObject<BlockEntityType<AltarTileEntity>> ALTAR = TILE_ENTITY_TYPES.register("altar",
            () -> BlockEntityType.Builder.of(AltarTileEntity::new, EnchantedBlocks.ALTAR.get()).build(null));

    public static final RegistryObject<BlockEntityType<BloodPoppyTileEntity>> BLOOD_POPPY = TILE_ENTITY_TYPES.register("blood_poppy",
            () -> BlockEntityType.Builder.of(BloodPoppyTileEntity::new, EnchantedBlocks.BLOOD_POPPY.get()).build(null));

    public static final RegistryObject<BlockEntityType<WitchCauldronTileEntity>> WITCH_CAULDRON = TILE_ENTITY_TYPES.register("witch_cauldron",
            () -> BlockEntityType.Builder.of(WitchCauldronTileEntity::new, EnchantedBlocks.WITCH_CAULDRON.get()).build(null));

    public static final RegistryObject<BlockEntityType<KettleTileEntity>> KETTLE = TILE_ENTITY_TYPES.register("kettle",
            () -> BlockEntityType.Builder.of(KettleTileEntity::new, EnchantedBlocks.KETTLE.get()).build(null));

    public static final RegistryObject<BlockEntityType<SpinningWheelTileEntity>> SPINNING_WHEEL = TILE_ENTITY_TYPES.register("spinning_wheel",
            () -> BlockEntityType.Builder.of(SpinningWheelTileEntity::new, EnchantedBlocks.SPINNING_WHEEL.get()).build(null));

    public static final RegistryObject<BlockEntityType<PoppetShelfTileEntity>> POPPET_SHELF = TILE_ENTITY_TYPES.register("poppet_shelf",
            () -> BlockEntityType.Builder.of(PoppetShelfTileEntity::new, EnchantedBlocks.POPPET_SHELF.get()).build(null));
}
