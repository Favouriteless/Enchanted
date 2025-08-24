package net.favouriteless.enchanted.api.datagen.builders.modopedia.components;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Either;
import net.favouriteless.enchanted.integrations.modopedia.client.page_components.RitePageComponent;
import net.favouriteless.modopedia.api.datagen.builders.PageComponentBuilder;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceLocation;

public class RiteBuilder extends PageComponentBuilder {

    private final Either<ResourceLocation, String> rite;

    private RiteBuilder(ResourceLocation rite) {
        super(RitePageComponent.ID);
        this.rite = Either.left(rite);
    }

    private RiteBuilder(String rite) {
        super(RitePageComponent.ID);
        this.rite = Either.right(rite);
    }

    public static RiteBuilder of(ResourceLocation rite) {
        return new RiteBuilder(rite);
    }

    public static RiteBuilder of(String rite) {
        return new RiteBuilder(rite);
    }

    @Override
    protected void build(JsonObject json, RegistryOps<JsonElement> ops) {
        json.add("rite", resolve(rite, r -> ResourceLocation.CODEC.encodeStart(ops, r).getOrThrow()));
    }

}
