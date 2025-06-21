package net.favouriteless.enchanted.common.circle_magic.rites.factory;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.favouriteless.enchanted.api.circle_magic.RiteFactory;
import net.favouriteless.enchanted.common.Enchanted;
import net.favouriteless.enchanted.common.circle_magic.rites.FertilityRite;
import net.favouriteless.enchanted.common.circle_magic.rites.Rite;
import net.favouriteless.enchanted.common.circle_magic.rites.Rite.BaseRiteParams;
import net.favouriteless.enchanted.common.circle_magic.rites.Rite.RiteParams;
import net.minecraft.resources.ResourceLocation;

public record FertilityFactory(int radius, double bonemealChance) implements RiteFactory {

    public static final ResourceLocation ID = Enchanted.id("fertility");

    public static final MapCodec<FertilityFactory> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.INT.fieldOf("radius").forGetter(f -> f.radius),
            Codec.DOUBLE.optionalFieldOf("bonemeal_chance", 0.04D).forGetter(f -> f.bonemealChance)
    ).apply(instance, FertilityFactory::new));

    @Override
    public Rite create(BaseRiteParams baseParams, RiteParams params) {
        return new FertilityRite(baseParams, params, radius, bonemealChance);
    }

    @Override
    public ResourceLocation id() {
        return ID;
    }

}
