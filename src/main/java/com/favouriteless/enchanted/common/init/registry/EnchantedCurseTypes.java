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
import com.favouriteless.enchanted.api.curses.AbstractCurse;
import com.favouriteless.enchanted.common.curses.CurseOfMisfortune;
import com.favouriteless.enchanted.common.curses.CurseOfOverheating;
import com.favouriteless.enchanted.common.curses.CurseOfSinking;
import com.favouriteless.enchanted.common.util.curse.CurseType;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.*;

import java.util.function.Supplier;

public class EnchantedCurseTypes {

    public static final DeferredRegister<CurseType<?>> CURSE_TYPES = DeferredRegister.create(Enchanted.location("curses"), Enchanted.MOD_ID);
    public static final Supplier<IForgeRegistry<CurseType<?>>> REGISTRY = CURSE_TYPES.makeRegistry(Generify.<CurseType<?>>from(CurseType.class), RegistryBuilder::new);

    public static final RegistryObject<CurseType<?>> MISFORTUNE = CURSE_TYPES.register("misfortune", () -> new CurseType<>(CurseOfMisfortune::new));
    public static final RegistryObject<CurseType<?>> SINKING = CURSE_TYPES.register("sinking", () -> new CurseType<>(CurseOfSinking::new));
    public static final RegistryObject<CurseType<?>> OVERHEATING = CURSE_TYPES.register("overheating", () -> new CurseType<>(CurseOfOverheating::new));


    public static AbstractCurse getByName(ResourceLocation loc) {
        for(RegistryObject<CurseType<?>> registryObject : CURSE_TYPES.getEntries()) {
            if(registryObject.getId().equals(loc))
                return registryObject.get().create();
        }
        return null;
    }

    private static class Generify {

        @SuppressWarnings("unchecked")
        public static <T extends IForgeRegistryEntry<T>> Class<T> from(Class<? super T> cls)
        {
            return (Class<T>) cls;
        }

    }

}
