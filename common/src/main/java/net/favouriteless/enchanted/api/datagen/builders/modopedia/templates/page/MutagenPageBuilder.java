package net.favouriteless.enchanted.api.datagen.builders.modopedia.templates.page;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Either;
import net.favouriteless.enchanted.common.Enchanted;
import net.favouriteless.modopedia.api.datagen.builders.page_components.TemplateComponentBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

public class MutagenPageBuilder extends TemplateComponentBuilder {

    public static final ResourceLocation ID = Enchanted.id("page/mutagen");

    private final Either<Block, String> result;

    private MutagenPageBuilder(Block result) {
        super(ID);
        this.result = Either.left(result);
    }

    private MutagenPageBuilder(String result) {
        super(ID);
        this.result = Either.right(result);
    }

    public static MutagenPageBuilder of(Block result) {
        return new MutagenPageBuilder(result);
    }

    public static MutagenPageBuilder of(String result) {
        return new MutagenPageBuilder(result);
    }

    @Override
    protected void build(JsonObject json, RegistryOps<JsonElement> ops) {
        json.add("result", resolve(result, b -> BuiltInRegistries.BLOCK.byNameCodec().encodeStart(ops, b).getOrThrow()));
    }

}