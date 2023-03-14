/*
 *
 *   Copyright (c) 2023. Favouriteless
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

package com.favouriteless.enchanted.common.init;

import com.favouriteless.enchanted.Enchanted;
import com.favouriteless.enchanted.common.blockentities.*;

import com.favouriteless.enchanted.common.blockentities.AltarBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class EnchantedBlockEntityTypes {

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, Enchanted.MOD_ID);

    public static final RegistryObject<BlockEntityType<ChalkGoldBlockEntity>> CHALK_GOLD = BLOCK_ENTITY_TYPES.register("chalk_gold",
            () -> BlockEntityType.Builder.of(ChalkGoldBlockEntity::new, EnchantedBlocks.CHALK_GOLD.get()).build(null));

    public static final RegistryObject<BlockEntityType<WitchOvenBlockEntity>> WITCH_OVEN = BLOCK_ENTITY_TYPES.register("witch_oven",
            () -> BlockEntityType.Builder.of(WitchOvenBlockEntity::new, EnchantedBlocks.WITCH_OVEN.get()).build(null));

    public static final RegistryObject<BlockEntityType<DistilleryBlockEntity>> DISTILLERY = BLOCK_ENTITY_TYPES.register("distillery",
            () -> BlockEntityType.Builder.of(DistilleryBlockEntity::new, EnchantedBlocks.DISTILLERY.get()).build(null));

    public static final RegistryObject<BlockEntityType<AltarBlockEntity>> ALTAR = BLOCK_ENTITY_TYPES.register("altar",
            () -> BlockEntityType.Builder.of(AltarBlockEntity::new, EnchantedBlocks.ALTAR.get()).build(null));

    public static final RegistryObject<BlockEntityType<BloodPoppyBlockEntity>> BLOOD_POPPY = BLOCK_ENTITY_TYPES.register("blood_poppy",
            () -> BlockEntityType.Builder.of(BloodPoppyBlockEntity::new, EnchantedBlocks.BLOOD_POPPY.get()).build(null));

    public static final RegistryObject<BlockEntityType<WitchCauldronBlockEntity>> WITCH_CAULDRON = BLOCK_ENTITY_TYPES.register("witch_cauldron",
            () -> BlockEntityType.Builder.of(WitchCauldronBlockEntity::new, EnchantedBlocks.WITCH_CAULDRON.get()).build(null));

    public static final RegistryObject<BlockEntityType<KettleBlockEntity>> KETTLE = BLOCK_ENTITY_TYPES.register("kettle",
            () -> BlockEntityType.Builder.of(KettleBlockEntity::new, EnchantedBlocks.KETTLE.get()).build(null));

    public static final RegistryObject<BlockEntityType<SpinningWheelBlockEntity>> SPINNING_WHEEL = BLOCK_ENTITY_TYPES.register("spinning_wheel",
            () -> BlockEntityType.Builder.of(SpinningWheelBlockEntity::new, EnchantedBlocks.SPINNING_WHEEL.get()).build(null));

    public static final RegistryObject<BlockEntityType<PoppetShelfBlockEntity>> POPPET_SHELF = BLOCK_ENTITY_TYPES.register("poppet_shelf",
            () -> BlockEntityType.Builder.of(PoppetShelfBlockEntity::new, EnchantedBlocks.POPPET_SHELF.get()).build(null));
}
