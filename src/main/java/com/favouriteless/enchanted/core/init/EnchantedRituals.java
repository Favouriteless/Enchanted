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

package com.favouriteless.enchanted.core.init;

import com.favouriteless.enchanted.Enchanted;
import com.favouriteless.enchanted.common.rituals.*;
import com.favouriteless.enchanted.core.init.registries.RitualRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;

import java.util.*;

public class EnchantedRituals {

    public static final DeferredRegister<AbstractRitual> RITUAL_TYPES = DeferredRegister.create(AbstractRitual.class, Enchanted.MOD_ID);

    public static final RegistryObject<AbstractRitual> RITE_OF_CHARGING_STONE = RITUAL_TYPES.register("rite_of_charging_stone", RiteOfChargingStone::new);
    public static final RegistryObject<AbstractRitual> RITE_OF_TOTAL_ECLIPSE = RITUAL_TYPES.register("rite_of_total_eclipse", RiteOfTotalEclipse::new);

}
