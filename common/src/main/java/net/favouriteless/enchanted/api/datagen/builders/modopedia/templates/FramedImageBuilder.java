package net.favouriteless.enchanted.api.datagen.builders.modopedia.templates;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Either;
import net.favouriteless.enchanted.common.Enchanted;
import net.favouriteless.modopedia.api.datagen.builders.page_components.TemplateComponentBuilder;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public class FramedImageBuilder extends TemplateComponentBuilder {

    public static final ResourceLocation ID = Enchanted.id("framed_image");

    private final Either<List<ResourceLocation>, String> images;

    protected FramedImageBuilder(ResourceLocation... images) {
        super(ID);
        this.images = Either.left(List.of(images));
    }

    protected FramedImageBuilder(String images) {
        super(ID);
        this.images = Either.right(images);
    }

    public static FramedImageBuilder of(ResourceLocation... images) {
        return new FramedImageBuilder(images);
    }

    public static FramedImageBuilder of(String images) {
        return new FramedImageBuilder(images);
    }

    @Override
    public FramedImageBuilder x(int x) {
        return (FramedImageBuilder) super.x(x);
    }

    @Override
    public FramedImageBuilder x(String x) {
        return (FramedImageBuilder) super.x(x);
    }

    @Override
    public FramedImageBuilder y(int y) {
        return (FramedImageBuilder) super.y(y);
    }

    @Override
    public FramedImageBuilder y(String y) {
        return (FramedImageBuilder) super.y(y);
    }

    @Override
    protected void build(JsonObject json, RegistryOps<JsonElement> ops) {
        json.add("images", resolve(images, l -> ResourceLocation.CODEC.listOf().encodeStart(ops, l).getOrThrow()));
    }

}