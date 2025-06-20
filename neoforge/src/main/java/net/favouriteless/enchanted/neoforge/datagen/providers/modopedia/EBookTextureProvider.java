package net.favouriteless.enchanted.neoforge.datagen.providers.modopedia;

import net.favouriteless.enchanted.common.Enchanted;
import net.favouriteless.modopedia.api.book.BookTexture;
import net.favouriteless.modopedia.api.datagen.builders.BookTextureBuilder;
import net.favouriteless.modopedia.api.datagen.providers.BookTextureProvider;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.PackOutput;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

public class EBookTextureProvider extends BookTextureProvider {

    public EBookTextureProvider(CompletableFuture<Provider> registries, PackOutput output) {
        super(Enchanted.MOD_ID, registries, output);
    }

    @Override
    protected void build(BiConsumer<String, BookTexture> output) {
        BookTextureBuilder.of("art_of_witchcraft")
                .texture(Enchanted.id("textures/gui/art_of_witchcraft.png"), 390, 400)
                .sized(386, 211)
                .titleBacker(83, 40, 0, 239, 96, 28)
                .leftButton(46, 206, 0, 211, 18, 10)
                .rightButton(322, 206, 19, 211, 18, 10)
                .backButton(184, 206, 54, 211, 18, 11)
                .refreshButton(27, 128, 38, 211, 15, 14)
                .page(84, 50, 100, 135)
                .page(202, 50, 100, 135)
                .widget("separator", 0, 267, 100, 6)
                .widget("small_frame", 136, 211, 20, 20)
                .widget("medium_frame", 210, 211, 54, 54)
                .widget("large_frame", 264, 211, 104, 104)
                .widget("crafting_grid", 156, 211, 54, 54)
                .widget("crafting_flame", 122, 211, 14, 14)
                .widget("crafting_arrow", 106, 211, 16, 13)
                .widget("down_arrow", 106, 224, 16, 13)
                .widget("cauldron", 0, 273, 90, 80)
                .widget("distillery", 100, 265, 78, 113)
                .build(output);
    }

}
