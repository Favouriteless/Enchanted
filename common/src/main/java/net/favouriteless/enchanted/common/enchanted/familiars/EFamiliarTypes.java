package net.favouriteless.enchanted.common.enchanted.familiars;

import net.favouriteless.enchanted.api.familiars.FamiliarType;
import net.favouriteless.enchanted.common.Enchanted;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.TamableAnimal;

import java.util.HashMap;
import java.util.Map;

public class EFamiliarTypes {

    private static final Map<ResourceLocation, FamiliarType<?, ?>> FAMILIAR_TYPES = new HashMap<>();

    public static final FamiliarType<?, ?> CAT = register("cat", new CatFamiliarType(Enchanted.id("cat")));


    public static <T extends TamableAnimal, C extends TamableAnimal> FamiliarType<T, C> register(ResourceLocation id, FamiliarType<T, C> type) {
        FAMILIAR_TYPES.put(id, type);
        return type;
    }

    public static <T extends TamableAnimal, C extends TamableAnimal> FamiliarType<T, C> register(String id, FamiliarType<T, C> type) {
        return register(Enchanted.id(id), type);
    }

    public static FamiliarType<?, ?> get(ResourceLocation id) {
        return FAMILIAR_TYPES.get(id);
    }

    public static FamiliarType<?, ?> getByInput(EntityType<?> typeIn) {
        for (FamiliarType<?, ?> type : FAMILIAR_TYPES.values()) {
            if (type.getTypeIn() == typeIn) {
                return type;
            }
        }
        return null;
    }

}
