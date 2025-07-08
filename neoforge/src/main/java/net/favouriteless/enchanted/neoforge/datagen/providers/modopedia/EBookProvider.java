package net.favouriteless.enchanted.neoforge.datagen.providers.modopedia;

import net.favouriteless.enchanted.common.Enchanted;
import net.favouriteless.enchanted.integrations.modopedia.common.EClassicBookType;
import net.favouriteless.modopedia.api.book.Book;
import net.favouriteless.modopedia.api.datagen.builders.BookBuilder;
import net.favouriteless.modopedia.api.datagen.providers.BookProvider;
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
        BookBuilder.of("book.title.enchanted.art_of_witchcraft")
                .subtitle("book.subtitle.enchanted.art_of_witchcraft")
                .landingText(
                        """
                        $(b)Witchcraft$() is the art of bringing out and using the magical effects of seemingly mundane objects.
                        
                        This book aims to explain the various schools of witchcraft."""
                )
                .texture(Enchanted.id("art_of_witchcraft"))
                .tab(Enchanted.id("main"))
                .type(new EClassicBookType(LockedViewType.HIDDEN))
                .build("art_of_witchcraft", output);
    }

}
