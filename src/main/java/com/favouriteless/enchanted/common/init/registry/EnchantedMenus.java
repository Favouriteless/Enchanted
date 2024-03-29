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
import com.favouriteless.enchanted.common.menus.*;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class EnchantedMenus {

    public static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(ForgeRegistries.CONTAINERS, Enchanted.MOD_ID);

    public static final RegistryObject<MenuType<WitchOvenMenu>> WITCH_OVEN = MENU_TYPES.register("witch_oven",
            () -> IForgeMenuType.create(WitchOvenMenu::new));
    public static final RegistryObject<MenuType<DistilleryMenu>> DISTILLERY = MENU_TYPES.register("distillery",
            () -> IForgeMenuType.create(DistilleryMenu::new));
    public static final RegistryObject<MenuType<AltarMenu>> ALTAR = MENU_TYPES.register("altar",
            () -> IForgeMenuType.create(AltarMenu::new));
    public static final RegistryObject<MenuType<SpinningWheelMenu>> SPINNING_WHEEL = MENU_TYPES.register("spinning_wheel",
            () -> IForgeMenuType.create(SpinningWheelMenu::new));
    public static final RegistryObject<MenuType<PoppetShelfMenu>> POPPET_SHELF = MENU_TYPES.register("poppet_shelf",
            () -> IForgeMenuType.create(PoppetShelfMenu::new));
}