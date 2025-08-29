package net.favouriteless.enchanted.api.brewing;

import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public interface BrewEffectRegistry {

    static BrewEffectRegistry get() {
        return null;
    }

    void register(ResourceLocation id, BrewEffect effect);

    @Nullable BrewEffect get(ResourceLocation id);

}
