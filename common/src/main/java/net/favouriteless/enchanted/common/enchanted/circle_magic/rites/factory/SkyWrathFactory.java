package net.favouriteless.enchanted.common.enchanted.circle_magic.rites.factory;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.favouriteless.enchanted.api.circle_magic.RiteFactory;
import net.favouriteless.enchanted.common.Enchanted;
import net.favouriteless.enchanted.common.enchanted.circle_magic.RiteTargetingType;
import net.favouriteless.enchanted.common.enchanted.circle_magic.rites.Rite;
import net.favouriteless.enchanted.common.enchanted.circle_magic.rites.Rite.BaseRiteParams;
import net.favouriteless.enchanted.common.enchanted.circle_magic.rites.Rite.RiteParams;
import net.favouriteless.enchanted.common.enchanted.circle_magic.rites.SkyWrathEntityRite;
import net.favouriteless.enchanted.common.enchanted.circle_magic.rites.SkyWrathRite;
import net.favouriteless.enchanted.common.enchanted.circle_magic.rites.SkyWrathWaystoneRite;
import net.minecraft.resources.ResourceLocation;

public record SkyWrathFactory(RiteTargetingType target) implements RiteFactory {

    public static final ResourceLocation ID = Enchanted.id("sky_wrath");

    public static final MapCodec<SkyWrathFactory> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            RiteTargetingType.CODEC.optionalFieldOf("target", RiteTargetingType.DEFAULT).forGetter(f -> f.target)
    ).apply(instance, SkyWrathFactory::new));

    @Override
    public Rite create(BaseRiteParams baseParams, RiteParams params) {
        return switch(target) {
            case DEFAULT -> new SkyWrathRite(baseParams, params);
            case LOCATION -> new SkyWrathWaystoneRite(baseParams, params);
            case ENTITY -> new SkyWrathEntityRite(baseParams, params);
        };
    }

    @Override
    public ResourceLocation id() {
        return ID;
    }

}
