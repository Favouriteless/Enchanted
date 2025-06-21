package net.favouriteless.enchanted.common.curses;

import net.favouriteless.enchanted.common.Enchanted;
import net.favouriteless.enchanted.api.curses.Curse;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

// TODO: Dispatch codec this and add proper API
public class CurseTypes {

    private static final Map<ResourceLocation, CurseType<?>> CURSE_TYPES = new HashMap<>();

    public static final CurseType<CurseMisfortune> MISFORTUNE = register("misfortune", CurseMisfortune::new);
    public static final CurseType<CurseSinking> SINKING = register("sinking", CurseSinking::new);
    public static final CurseType<CurseOverheating> OVERHEATING = register("overheating", CurseOverheating::new);



    /**
     * Register a {@link CurseType} to be used by Enchanted.
     */
    public static <T extends Curse> CurseType<T> register(ResourceLocation id, Supplier<T> curseSupplier) {
        CurseType<T> riteType = new CurseType<>(id, curseSupplier);
        CURSE_TYPES.put(id, riteType);
        return riteType;
    }

    private static <T extends Curse> CurseType<T> register(String id, Supplier<T> curseSupplier) {
        return register(Enchanted.id(id), curseSupplier);
    }

    public static CurseType<?> get(ResourceLocation id) {
        return CURSE_TYPES.get(id);
    }

    public static Curse create(ResourceLocation id) {
        CurseType<?> type = get(id);
        return type != null ? type.create() : null;
    }

}
