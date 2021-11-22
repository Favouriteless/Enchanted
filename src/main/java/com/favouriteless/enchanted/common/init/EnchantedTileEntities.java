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

package com.favouriteless.enchanted.common.init;

import com.favouriteless.enchanted.Enchanted;
import com.favouriteless.enchanted.common.tileentity.*;

import com.favouriteless.enchanted.common.tileentity.AltarTileEntity;
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

    public static final RegistryObject<TileEntityType<AltarTileEntity>> ALTAR = TILE_ENTITY_TYPES.register("altar",
            () -> TileEntityType.Builder.of(AltarTileEntity::new, EnchantedBlocks.ALTAR.get()).build(null));

    public static final RegistryObject<TileEntityType<BloodPoppyTileEntity>> BLOOD_POPPY = TILE_ENTITY_TYPES.register("blood_poppy",
            () -> TileEntityType.Builder.of(BloodPoppyTileEntity::new, EnchantedBlocks.BLOOD_POPPY.get()).build(null));
}
