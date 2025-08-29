package net.favouriteless.enchanted.api.circle_magic;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.favouriteless.enchanted.common.enchanted.circle_magic.RiteFactoryRegistryImpl;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

/**
 * Maps {@link RiteFactory} IDs to their codecs
 */
public interface RiteFactoryRegistry {

    static RiteFactoryRegistry get() {
        return RiteFactoryRegistryImpl.INSTANCE;
    }

    void register(ResourceLocation id, MapCodec<? extends RiteFactory> codec);

    /**
     * @return RiteFactory registered under the given ID, or null if none were found.
     */
    @Nullable MapCodec<? extends RiteFactory> getCodec(ResourceLocation id);

    /**
     * @return The main dispatch codec for RiteFactories
     */
    Codec<RiteFactory> codec();

}
