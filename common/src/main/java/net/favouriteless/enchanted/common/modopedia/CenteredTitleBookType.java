package net.favouriteless.enchanted.common.modopedia;

import net.favouriteless.enchanted.client.screens.modopedia.EnchantedClassicLandingScreen;
import net.favouriteless.modopedia.api.books.Book;
import net.favouriteless.modopedia.api.books.BookContent.LocalisedBookContent;
import net.favouriteless.modopedia.book.book_types.ClassicBookType;
import net.minecraft.client.gui.screens.Screen;
import org.jetbrains.annotations.Nullable;

public class CenteredTitleBookType extends ClassicBookType {

    @Override
    @Nullable
    public Screen openLandingScreen(Book book, String langCode, LocalisedBookContent content) {
        return new EnchantedClassicLandingScreen(book, langCode, content);
    }

}
