package net.favouriteless.enchanted.common.enchanted.circle_magic.rites.factory;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.favouriteless.enchanted.api.circle_magic.RiteFactory;
import net.favouriteless.enchanted.common.Enchanted;
import net.favouriteless.enchanted.common.enchanted.circle_magic.RiteTargetingType;
import net.favouriteless.enchanted.common.enchanted.circle_magic.rites.Rite;
import net.favouriteless.enchanted.common.enchanted.circle_magic.rites.Rite.BaseRiteParams;
import net.favouriteless.enchanted.common.enchanted.circle_magic.rites.Rite.RiteParams;
import net.favouriteless.enchanted.common.enchanted.circle_magic.rites.TransposeCasterEntityRite;
import net.favouriteless.enchanted.common.enchanted.circle_magic.rites.TransposeCasterWaystoneRite;
import net.minecraft.resources.ResourceLocation;

public record TransposeCasterFactory(RiteTargetingType target) implements RiteFactory {

    public static final ResourceLocation ID = Enchanted.id("transpose_caster");

    public static final MapCodec<TransposeCasterFactory> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            RiteTargetingType.CODEC.optionalFieldOf("target", RiteTargetingType.DEFAULT).forGetter(f -> f.target)
    ).apply(instance, TransposeCasterFactory::new));

    @Override
    public Rite create(BaseRiteParams baseParams, RiteParams params) {
        return switch(target) {
            case DEFAULT, LOCATION -> new TransposeCasterWaystoneRite(baseParams, params);
            case ENTITY -> new TransposeCasterEntityRite(baseParams, params);
        };
    }

    @Override
    public ResourceLocation id() {
        return ID;
    }

}
