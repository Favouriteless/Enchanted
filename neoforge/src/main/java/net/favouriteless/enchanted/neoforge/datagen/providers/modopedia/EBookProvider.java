package net.favouriteless.enchanted.neoforge.datagen.providers.modopedia;

import net.favouriteless.enchanted.common.Enchanted;
import net.favouriteless.enchanted.integrations.modopedia.common.EClassicBookType;
import net.favouriteless.modopedia.api.book.Book;
import net.favouriteless.modopedia.api.datagen.builders.BookBuilder;
import net.favouriteless.modopedia.api.datagen.providers.BookProvider;
import net.favouriteless.modopedia.api.text.FormattedStringBuilder;
import net.favouriteless.modopedia.common.book_types.LockedViewType;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.PackOutput;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

public class EBookProvider extends BookProvider {

    public EBookProvider(CompletableFuture<Provider> registries, PackOutput output) {
        super(Enchanted.MOD_ID, registries, output);
    }

    @Override
    protected void build(Provider provider, BiConsumer<String, Book> output) {
        BookBuilder.of(Enchanted.translationKey("book.title", "art_of_witchcraft"))
                .subtitle(Enchanted.translationKey("book.subtitle", "art_of_witchcraft"))
                .landingText(Enchanted.translationKey("book.landing_text", "art_of_witchcraft"))
                .texture(Enchanted.id("art_of_witchcraft"))
                .tab(Enchanted.id("main"))
                .type(new EClassicBookType(LockedViewType.HIDDEN))
                .build("art_of_witchcraft", output);
    }

}
