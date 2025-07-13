package net.favouriteless.enchanted.api.datagen.builders.modopedia.components;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Either;
import net.favouriteless.enchanted.integrations.modopedia.client.page_components.MutagenDisplayPageComponent;
import net.favouriteless.modopedia.api.datagen.builders.PageComponentBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.RegistryOps;
import net.minecraft.world.level.block.Block;

public class MutagenDisplayBuilder extends PageComponentBuilder {

    private final Either<Block, String> result;

    private MutagenDisplayBuilder(Block result) {
        super(MutagenDisplayPageComponent.ID);
        this.result = Either.left(result);
    }

    private MutagenDisplayBuilder(String result) {
        super(MutagenDisplayPageComponent.ID);
        this.result = Either.right(result);
    }

    public static MutagenDisplayBuilder of(Block result) {
        return new MutagenDisplayBuilder(result);
    }

    public static MutagenDisplayBuilder of(String result) {
        return new MutagenDisplayBuilder(result);
    }

    @Override
    protected void build(JsonObject json, RegistryOps<JsonElement> ops) {
        json.add("result", resolve(result, b -> BuiltInRegistries.BLOCK.byNameCodec().encodeStart(ops, b).getOrThrow()));
    }

}
