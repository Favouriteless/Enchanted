package net.favouriteless.enchanted.common.enchanted.circle_magic.rites.factory;

import com.mojang.serialization.MapCodec;
import net.favouriteless.enchanted.api.circle_magic.RiteFactory;
import net.favouriteless.enchanted.common.Enchanted;
import net.favouriteless.enchanted.common.enchanted.circle_magic.rites.Rite;
import net.favouriteless.enchanted.common.enchanted.circle_magic.rites.Rite.BaseRiteParams;
import net.favouriteless.enchanted.common.enchanted.circle_magic.rites.Rite.RiteParams;
import net.favouriteless.enchanted.common.enchanted.circle_magic.rites.TotalEclipseRite;
import net.minecraft.resources.ResourceLocation;

public class TotalEclipseFactory implements RiteFactory {

    public static final ResourceLocation ID = Enchanted.id("total_eclipse");

    public static final MapCodec<TotalEclipseFactory> CODEC = MapCodec.unit(TotalEclipseFactory::new);

    @Override
    public Rite create(BaseRiteParams baseParams, RiteParams params) {
        return new TotalEclipseRite(baseParams, params);
    }

    @Override
    public ResourceLocation id() {
        return ID;
    }

}
