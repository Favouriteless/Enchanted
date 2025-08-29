package net.favouriteless.enchanted.common.enchanted.circle_magic;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.favouriteless.enchanted.api.circle_magic.RiteFactory;
import net.favouriteless.enchanted.api.circle_magic.RiteFactoryRegistry;
import net.minecraft.resources.ResourceLocation;

public class RiteFactoryRegistryImpl implements RiteFactoryRegistry {

    public static final RiteFactoryRegistryImpl INSTANCE = new RiteFactoryRegistryImpl();

    private final BiMap<ResourceLocation, MapCodec<? extends RiteFactory>> typeCodecs = HashBiMap.create();
    private final Codec<RiteFactory> CODEC = ResourceLocation.CODEC.dispatch(RiteFactory::id, typeCodecs::get);

    private RiteFactoryRegistryImpl() {}

    @Override
    public void register(ResourceLocation id, MapCodec<? extends RiteFactory> codec) {
        if(typeCodecs.containsKey(id))
            throw new IllegalArgumentException("Attempted to register a duplicate RiteFactory: " + id.toString());
        typeCodecs.put(id, codec);
    }

    @Override
    public MapCodec<? extends RiteFactory> getCodec(ResourceLocation id) {
        return typeCodecs.get(id);
    }

    @Override
    public Codec<RiteFactory> codec() {
        return CODEC;
    }

}
