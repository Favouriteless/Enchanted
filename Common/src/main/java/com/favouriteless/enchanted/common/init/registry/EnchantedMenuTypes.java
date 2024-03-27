package com.favouriteless.enchanted.common.init.registry;

import com.favouriteless.enchanted.common.menus.*;
import com.favouriteless.enchanted.platform.Services;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import org.apache.commons.lang3.function.TriFunction;

import java.util.function.Supplier;

/*
 * Make sure to register these in ClientRegistry too if they have a Screen.
 */
public class EnchantedMenuTypes {
    
    public static final Supplier<MenuType<WitchOvenMenu>> WITCH_OVEN = register("witch_oven", WitchOvenMenu::new);
    public static final Supplier<MenuType<DistilleryMenu>> DISTILLERY = register("distillery", DistilleryMenu::new);
    public static final Supplier<MenuType<AltarMenu>> ALTAR = register("altar", AltarMenu::new);
    public static final Supplier<MenuType<SpinningWheelMenu>> SPINNING_WHEEL = register("spinning_wheel", SpinningWheelMenu::new);
    public static final Supplier<MenuType<PoppetShelfMenu>> POPPET_SHELF = register("poppet_shelf", PoppetShelfMenu::new);



    private static <T extends AbstractContainerMenu> Supplier<MenuType<T>> register(String name, TriFunction<Integer, Inventory, FriendlyByteBuf, T> create) {
        return Services.COMMON_REGISTRY.registerMenu(name, create);
    }

    public static void load() {} // Method which exists purely to load the class.

}