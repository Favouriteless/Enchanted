package net.favouriteless.enchanted.common.init;

import net.favouriteless.enchanted.common.menus.*;
import net.favouriteless.enchanted.platform.EServices;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import org.apache.commons.lang3.function.TriFunction;

import java.util.function.BiFunction;
import java.util.function.Supplier;

/*
 * Make sure to register these in ClientRegistry too if they have a Screen.
 */
public class EMenuTypes {

    public static final Supplier<MenuType<AltarMenu>> ALTAR = register("altar", AltarMenu::new, BlockPos.STREAM_CODEC);
    public static final Supplier<MenuType<DistilleryMenu>> DISTILLERY = register("distillery", DistilleryMenu::new, BlockPos.STREAM_CODEC);
    public static final Supplier<MenuType<PoppetShelfMenu>> POPPET_SHELF = register("poppet_shelf", PoppetShelfMenu::new, BlockPos.STREAM_CODEC);
    public static final Supplier<MenuType<SpinningWheelMenu>> SPINNING_WHEEL = register("spinning_wheel", SpinningWheelMenu::new, BlockPos.STREAM_CODEC);
    public static final Supplier<MenuType<WitchOvenMenu>> WITCH_OVEN = register("witch_oven", WitchOvenMenu::new, BlockPos.STREAM_CODEC);


    private static <T extends AbstractContainerMenu, C> Supplier<MenuType<T>> register(String name,
                                                                                       TriFunction<Integer, Inventory, C, T> factory,
                                                                                       StreamCodec<? super RegistryFriendlyByteBuf, C> codec) {
        return EServices.REGISTRY.registerMenu(name, factory, codec);
    }

    private static <T extends AbstractContainerMenu> Supplier<MenuType<T>> register(String name,
                                                                                    BiFunction<Integer, Inventory, T> factory) {
        return EServices.REGISTRY.registerMenu(name, factory);
    }

    public static void load() {
    } // Method which exists purely to load the class.

}