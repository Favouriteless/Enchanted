package net.favouriteless.enchanted.common.init;

import com.mojang.serialization.Codec;
import net.favouriteless.enchanted.common.Enchanted;
import net.favouriteless.enchanted.common.altar.AltarUpgrade;
import net.favouriteless.enchanted.common.altar.PowerProvider;
import net.favouriteless.enchanted.common.circle_magic.CircleMagicShape;
import net.favouriteless.enchanted.common.circle_magic.RiteType;
import net.favouriteless.enchanted.common.mutandis.MutagenInfo;
import net.favouriteless.enchanted.platform.EServices;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

public class EData {

    public static final ResourceKey<Registry<AltarUpgrade>> ALTAR_UPGRADE_REGISTRY = register(ResourceKey.createRegistryKey(Enchanted.id("altar/upgrade")), AltarUpgrade.CODEC);
    public static final ResourceKey<Registry<PowerProvider>> ALTAR_BLOCK_REGISTRY = register(ResourceKey.createRegistryKey(Enchanted.id("altar/block")), PowerProvider.CODEC);
    public static final ResourceKey<Registry<PowerProvider>> ALTAR_TAG_REGISTRY = register(ResourceKey.createRegistryKey(Enchanted.id("altar/tag")), PowerProvider.CODEC);

    public static final ResourceKey<Registry<MutagenInfo>> MUTAGEN_REGISTRY = registerSynced(ResourceKey.createRegistryKey(Enchanted.id("mutagens")), MutagenInfo.CODEC, MutagenInfo.CODEC);
    public static final ResourceKey<Registry<CircleMagicShape>> CIRCLE_SHAPE_REGISTRY = registerSynced(ResourceKey.createRegistryKey(Enchanted.id("circle_magic/shape")), CircleMagicShape.CODEC, CircleMagicShape.CODEC);
    public static final ResourceKey<Registry<RiteType>> RITE_TYPES_REGISTRY = registerSynced(ResourceKey.createRegistryKey(Enchanted.id("circle_magic/rite")), RiteType.CODEC, RiteType.CODEC);


    private static <T> ResourceKey<Registry<T>> register(ResourceKey<Registry<T>> key, Codec<T> codec) {
        return EServices.REGISTRY.registerDataRegistry(key, codec);
    }

    private static <T> ResourceKey<Registry<T>> registerSynced(ResourceKey<Registry<T>> key, Codec<T> codec, Codec<T> networkCodec) {
        return EServices.REGISTRY.registerSyncedDataRegistry(key, codec, networkCodec);
    }

    public static void load() {}

}
