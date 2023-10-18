package com.favouriteless.enchanted.common.init.registry;

import com.favouriteless.enchanted.Enchanted;
import com.favouriteless.enchanted.api.curses.AbstractCurse;
import com.favouriteless.enchanted.common.curses.CurseMisfortune;
import com.favouriteless.enchanted.common.curses.CurseOverheating;
import com.favouriteless.enchanted.common.curses.CurseSinking;
import com.favouriteless.enchanted.common.curses.CurseType;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;


public class CurseTypes {

    private static final Map<ResourceLocation, CurseType<?>> CURSE_TYPES = new HashMap<>();

    public static final CurseType<CurseMisfortune> MISFORTUNE = register("misfortune", CurseMisfortune::new);
    public static final CurseType<CurseSinking> SINKING = register("sinking", CurseSinking::new);
    public static final CurseType<CurseOverheating> OVERHEATING = register("overheating", CurseOverheating::new);



    /**
     * Register a {@link CurseType} to be used by Enchanted.
     */
    public static <T extends AbstractCurse> CurseType<T> register(ResourceLocation id, Supplier<T> curseSupplier) {
        CurseType<T> riteType = new CurseType<>(id, curseSupplier);
        CURSE_TYPES.put(id, riteType);
        return riteType;
    }

    private static <T extends AbstractCurse> CurseType<T> register(String id, Supplier<T> curseSupplier) {
        return register(Enchanted.location(id), curseSupplier);
    }

    public static AbstractCurse getInstance(ResourceLocation id) {
        CurseType<?> type = CURSE_TYPES.get(id);
        return type != null ? type.create() : null;
    }

}
