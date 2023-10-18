package com.favouriteless.enchanted.common.init.registry;

import com.favouriteless.enchanted.common.menus.*;
import net.minecraft.world.inventory.MenuType;

import java.util.function.Supplier;

public class EnchantedMenus {
    
    public static final Supplier<MenuType<WitchOvenMenu>> WITCH_OVEN = register("witch_oven", () -> IForgeMenuType.create(WitchOvenMenu::new));
    public static final Supplier<MenuType<DistilleryMenu>> DISTILLERY = register("distillery", () -> IForgeMenuType.create(DistilleryMenu::new));
    public static final Supplier<MenuType<AltarMenu>> ALTAR = register("altar", () -> IForgeMenuType.create(AltarMenu::new));
    public static final Supplier<MenuType<SpinningWheelMenu>> SPINNING_WHEEL = register("spinning_wheel", () -> IForgeMenuType.create(SpinningWheelMenu::new));
    public static final Supplier<MenuType<PoppetShelfMenu>> POPPET_SHELF = register("poppet_shelf", () -> IForgeMenuType.create(PoppetShelfMenu::new));
}