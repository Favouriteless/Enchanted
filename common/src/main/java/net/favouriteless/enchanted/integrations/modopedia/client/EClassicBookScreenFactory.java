package net.favouriteless.enchanted.integrations.modopedia.client;

import net.favouriteless.enchanted.client.screens.modopedia.EnchantedClassicLandingScreen;
import net.favouriteless.enchanted.integrations.modopedia.common.EClassicBookType;
import net.favouriteless.modopedia.api.book.Book;
import net.favouriteless.modopedia.api.book.BookContent.LocalisedBookContent;
import net.favouriteless.modopedia.api.book.BookScreenFactory;
import net.favouriteless.modopedia.api.book.Category;
import net.favouriteless.modopedia.api.book.Entry;
import net.favouriteless.modopedia.client.screens.books.BookScreen;
import net.favouriteless.modopedia.client.screens.books.CategoryScreen;
import net.favouriteless.modopedia.client.screens.books.EntryScreen;
import org.jetbrains.annotations.Nullable;

public class EClassicBookScreenFactory implements BookScreenFactory<EClassicBookType> {

    @Override
    public @Nullable BookScreen openLandingScreen(EClassicBookType type, Book book, String langCode, LocalisedBookContent content, BookScreen lastScreen) {
        return new EnchantedClassicLandingScreen(book, langCode, content);
    }

    @Override
    public @Nullable BookScreen openCategoryScreen(EClassicBookType type, Book book, String langCode, LocalisedBookContent content, String category, BookScreen lastScreen) {
        Category cat = content.getCategory(category);
        return cat != null ? new CategoryScreen(book, langCode, content, cat, lastScreen) : lastScreen;
    }

    @Override
    public @Nullable BookScreen openEntryScreen(EClassicBookType type, Book book, String langCode, LocalisedBookContent content, String entry, BookScreen lastScreen) {
        Entry ent = content.getEntry(entry);
        return ent != null ? new EntryScreen(book, langCode, content, ent, lastScreen) : null;
    }

}
