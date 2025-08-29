package net.favouriteless.enchanted.common.enchanted.altar;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.favouriteless.enchanted.common.init.EData;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

public record PowerProvider(int power, int limit) {

    public static final Codec<PowerProvider> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.fieldOf("power").forGetter(data -> data.power),
            Codec.INT.fieldOf("limit").forGetter(data -> data.limit)
    ).apply(instance, PowerProvider::new));

    @SuppressWarnings("deprecation")
    public static PowerProvider get(Level level, Block block) {
        return get(block.builtInRegistryHolder().key().location(), level.registryAccess().registryOrThrow(EData.ALTAR_BLOCK_REGISTRY));
    }

    public static PowerProvider get(Level level, TagKey<Block> tag) {
        return get(tag.location(), level.registryAccess().registryOrThrow(EData.ALTAR_TAG_REGISTRY));
    }

    public static PowerProvider get(ResourceLocation id, Registry<PowerProvider> registry) {
        return registry.get(id);
    }

}