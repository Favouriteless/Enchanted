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

package com.favouriteless.enchanted.common.init.registry;

import com.favouriteless.enchanted.Enchanted;
import com.favouriteless.enchanted.common.familiars.FamiliarType;
import com.favouriteless.enchanted.common.familiars.types.CatFamiliarType;
import com.favouriteless.enchanted.core.util.RegistryHelper.Generify;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class FamiliarTypes {

	public static final DeferredRegister<FamiliarType<?, ?>> FAMILIAR_TYPES = DeferredRegister.create(Enchanted.location("familiars"), Enchanted.MOD_ID);
	public static final Supplier<IForgeRegistry<FamiliarType<?, ?>>> REGISTRY = FAMILIAR_TYPES.makeRegistry(Generify.<FamiliarType<?, ?>>from(FamiliarType.class), RegistryBuilder::new);

	public static final RegistryObject<FamiliarType<?, ?>> CAT = FAMILIAR_TYPES.register("cat", CatFamiliarType::new);



	public static FamiliarType<?, ?> getByInput(EntityType<?> type) {
		for(RegistryObject<FamiliarType<?, ?>> registryObject : FAMILIAR_TYPES.getEntries()) {
			if(registryObject.get().getTypeIn() == type)
				return registryObject.get();
		}
		return null;
	}

}
