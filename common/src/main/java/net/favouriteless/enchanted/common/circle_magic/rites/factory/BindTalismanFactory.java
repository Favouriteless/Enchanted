package net.favouriteless.enchanted.common.circle_magic.rites.factory;

import com.mojang.serialization.MapCodec;
import net.favouriteless.enchanted.api.circle_magic.RiteFactory;
import net.favouriteless.enchanted.common.Enchanted;
import net.favouriteless.enchanted.common.circle_magic.rites.BindTalismanRite;
import net.favouriteless.enchanted.common.circle_magic.rites.Rite;
import net.favouriteless.enchanted.common.circle_magic.rites.Rite.BaseRiteParams;
import net.favouriteless.enchanted.common.circle_magic.rites.Rite.RiteParams;
import net.minecraft.resources.ResourceLocation;

public class BindTalismanFactory implements RiteFactory {

    public static final ResourceLocation ID = Enchanted.id("bind_talisman");

    public static final MapCodec<BindTalismanFactory> CODEC = MapCodec.unit(BindTalismanFactory::new);

    @Override
    public Rite create(BaseRiteParams baseParams, RiteParams params) {
        return new BindTalismanRite(baseParams, params);
    }

    @Override
    public ResourceLocation id() {
        return ID;
    }

}
