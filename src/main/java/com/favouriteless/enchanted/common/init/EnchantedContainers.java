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
import com.favouriteless.enchanted.common.containers.*;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class EnchantedContainers {

    public static final DeferredRegister<ContainerType<?>> CONTAINER_TYPES = DeferredRegister.create(ForgeRegistries.CONTAINERS, Enchanted.MOD_ID);

    public static final RegistryObject<ContainerType<WitchOvenContainer>> WITCH_OVEN = CONTAINER_TYPES.register("witch_oven",
            () -> IForgeContainerType.create(WitchOvenContainer::new));
    public static final RegistryObject<ContainerType<DistilleryContainer>> DISTILLERY = CONTAINER_TYPES.register("distillery",
            () -> IForgeContainerType.create(DistilleryContainer::new));
    public static final RegistryObject<ContainerType<AltarContainer>> ALTAR = CONTAINER_TYPES.register("altar",
            () -> IForgeContainerType.create(AltarContainer::new));
    public static final RegistryObject<ContainerType<SpinningWheelContainer>> SPINNING_WHEEL = CONTAINER_TYPES.register("spinning_wheel",
            () -> IForgeContainerType.create(SpinningWheelContainer::new));
    public static final RegistryObject<ContainerType<PoppetShelfContainer>> POPPET_SHELF = CONTAINER_TYPES.register("poppet_shelf",
            () -> IForgeContainerType.create(PoppetShelfContainer::new));
}