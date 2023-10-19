package com.favouriteless.enchanted.common.init.registry;

import com.favouriteless.enchanted.common.menus.*;
import com.favouriteless.enchanted.platform.Services;
import net.minecraft.core.Registry;
import net.minecraft.world.inventory.MenuType;

import java.util.function.Supplier;

public class EnchantedMenus {
    
    public static final Supplier<MenuType<WitchOvenMenu>> WITCH_OVEN = register("witch_oven", () -> IForgeMenuType.create(WitchOvenMenu::new));
    public static final Supplier<MenuType<DistilleryMenu>> DISTILLERY = register("distillery", () -> IForgeMenuType.create(DistilleryMenu::new));
    public static final Supplier<MenuType<AltarMenu>> ALTAR = register("altar", () -> IForgeMenuType.create(AltarMenu::new));
    public static final Supplier<MenuType<SpinningWheelMenu>> SPINNING_WHEEL = register("spinning_wheel", () -> IForgeMenuType.create(SpinningWheelMenu::new));
    public static final Supplier<MenuType<PoppetShelfMenu>> POPPET_SHELF = register("poppet_shelf", () -> IForgeMenuType.create(PoppetShelfMenu::new));



    private static <T extends MenuType<?>> Supplier<T> register(String name, Supplier<T> menuTypeSupplier) {
        return Services.COMMON_REGISTRY.register(Registry.MENU, name, menuTypeSupplier);
    }

    public static void load() {} // Method which exists purely to load the class.

}