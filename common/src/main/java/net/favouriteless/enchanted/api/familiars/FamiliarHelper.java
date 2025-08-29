package net.favouriteless.enchanted.api.familiars;

import net.favouriteless.enchanted.common.enchanted.familiars.FamiliarHelperImpl;
import net.minecraft.world.entity.TamableAnimal;

public interface FamiliarHelper {

    static FamiliarHelper get() {
        return FamiliarHelperImpl.INSTANCE;
    }

    void dismiss(TamableAnimal familiar);

}
