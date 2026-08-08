package net.favouriteless.enchanted.common.items.component;

import net.favouriteless.enchanted.platform.EServices;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

import java.util.Map;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public class EDataComponents {

    public static final Supplier<DataComponentType<EntityRefData>> ENTITY_REF = register("entity_ref", b -> b.persistent(EntityRefData.CODEC).networkSynchronized(EntityRefData.STREAM_CODEC).cacheEncoding());
    public static final Supplier<DataComponentType<BlockPos>> BLOCK_POS = register("block_pos", b -> b.persistent(BlockPos.CODEC).networkSynchronized(BlockPos.STREAM_CODEC).cacheEncoding());
    public static final Supplier<DataComponentType<ResourceKey<Level>>> LEVEL_KEY = register("level_key", b -> b.persistent(ResourceKey.codec(Registries.DIMENSION)).networkSynchronized(ResourceKey.streamCodec(Registries.DIMENSION)).cacheEncoding());
    public static final Supplier<DataComponentType<Map<ResourceLocation, Block>>> CIRCLE_MAGIC_SHAPE_MAP = register("circle_magic_shape_map", b -> b.persistent(CircleMagicShapeMap.CODEC).networkSynchronized(CircleMagicShapeMap.STREAM_CODEC).cacheEncoding());

    private static <T> Supplier<DataComponentType<T>> register(String name, UnaryOperator<DataComponentType.Builder<T>> builder) {
        return EServices.REGISTRY.register(BuiltInRegistries.DATA_COMPONENT_TYPE, name, () -> builder.apply(DataComponentType.builder()).build());
    }

    public static void load() {
    } // Method which exists purely to load the class.

}
