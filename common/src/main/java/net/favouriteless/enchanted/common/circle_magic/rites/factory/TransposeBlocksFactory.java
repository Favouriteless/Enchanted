package net.favouriteless.enchanted.common.circle_magic.rites.factory;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.favouriteless.enchanted.api.circle_magic.RiteFactory;
import net.favouriteless.enchanted.common.Enchanted;
import net.favouriteless.enchanted.common.circle_magic.rites.Rite;
import net.favouriteless.enchanted.common.circle_magic.rites.Rite.BaseRiteParams;
import net.favouriteless.enchanted.common.circle_magic.rites.Rite.RiteParams;
import net.favouriteless.enchanted.common.circle_magic.rites.TransposeBlocksRite;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

public record TransposeBlocksFactory(TagKey<Block> tag, ItemStack tool) implements RiteFactory {

    public static final ResourceLocation ID = Enchanted.id("transpose_blocks");

    public static final MapCodec<TransposeBlocksFactory> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            TagKey.codec(Registries.BLOCK).fieldOf("tag").forGetter(p -> p.tag),
            ItemStack.CODEC.optionalFieldOf("tool", ItemStack.EMPTY).forGetter(p -> p.tool)
    ).apply(instance, TransposeBlocksFactory::new));

    @Override
    public Rite create(BaseRiteParams baseParams, RiteParams params) {
        return new TransposeBlocksRite(baseParams, params, tag, tool);
    }

    @Override
    public ResourceLocation id() {
        return ID;
    }

}
