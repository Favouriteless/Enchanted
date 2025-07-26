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

public class RitePageBuilder extends TemplateComponentBuilder {

    public static final ResourceLocation ID = Enchanted.id("page/rite");

    private final Either<ResourceLocation, String> rite;

    private RitePageBuilder(ResourceLocation rite) {
        super(ID);
        this.rite = Either.left(rite);
    }

    private RitePageBuilder(String rite) {
        super(ID);
        this.rite = Either.right(rite);
    }

    public static RitePageBuilder of(ResourceLocation rite) {
        return new RitePageBuilder(rite);
    }

    public static RitePageBuilder of(String rite) {
        return new RitePageBuilder(rite);
    }

    @Override
    protected void build(JsonObject json, RegistryOps<JsonElement> ops) {
        json.add("rite", resolve(rite, r -> ResourceLocation.CODEC.encodeStart(ops, r).getOrThrow()));
    }

}