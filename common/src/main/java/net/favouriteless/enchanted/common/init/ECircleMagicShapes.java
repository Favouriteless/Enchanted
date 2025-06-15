package net.favouriteless.enchanted.common.init;

import net.favouriteless.enchanted.common.Enchanted;
import net.favouriteless.enchanted.common.circle_magic.CircleMagicShape;
import net.minecraft.resources.ResourceKey;

public class ECircleMagicShapes {

    public static final ResourceKey<CircleMagicShape> SMALL_CIRCLE = key("small_circle");
    public static final ResourceKey<CircleMagicShape> MEDIUM_CIRCLE = key("medium_circle");
    public static final ResourceKey<CircleMagicShape> LARGE_CIRCLE = key("large_circle");

    private static ResourceKey<CircleMagicShape> key(String path) {
        return ResourceKey.create(EData.CIRCLE_SHAPE_REGISTRY, Enchanted.id(path));
    }
}
