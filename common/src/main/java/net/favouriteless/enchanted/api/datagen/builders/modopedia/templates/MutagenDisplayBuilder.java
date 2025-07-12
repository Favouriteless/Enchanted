package net.favouriteless.enchanted.api.datagen.builders.modopedia.templates;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Either;
import net.favouriteless.enchanted.common.Enchanted;
import net.favouriteless.modopedia.api.datagen.builders.page_components.TemplateComponentBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

public class MutagenDisplayBuilder extends TemplateComponentBuilder {

    public static final ResourceLocation ID = Enchanted.id("mutagen_display");

    private final Either<Block, String> block;

    private MutagenDisplayBuilder(Block block) {
        super(ID);
        this.block = Either.left(block);
    }

    private MutagenDisplayBuilder(String block) {
        super(ID);
        this.block = Either.right(block);
    }

    public static MutagenDisplayBuilder of(Block block) {
        return new MutagenDisplayBuilder(block);
    }

    public static MutagenDisplayBuilder of(String block) {
        return new MutagenDisplayBuilder(block);
    }

    @Override
    protected void build(JsonObject json, RegistryOps<JsonElement> ops) {
        json.add("block", resolve(block, b -> ResourceLocation.CODEC.encodeStart(ops, BuiltInRegistries.BLOCK.getKey(b)).getOrThrow()));
    }

}
