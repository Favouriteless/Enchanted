package net.favouriteless.enchanted.api.circle_magic;

import net.favouriteless.enchanted.common.circle_magic.rites.Rite;
import net.favouriteless.enchanted.common.circle_magic.rites.Rite.BaseRiteParams;
import net.favouriteless.enchanted.common.circle_magic.rites.Rite.RiteParams;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface RiteFactory {

    Rite create(BaseRiteParams baseParams, RiteParams params);

    ResourceLocation id();

    /**
     * Item outputs for JEI. If this list is non-null, JEI will automatically support RiteTypes using the factory.
     */
    default @Nullable List<ItemStack> getOutputs() {
        return null;
    }

}
