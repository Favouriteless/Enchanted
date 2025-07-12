package net.favouriteless.enchanted.client.screens.modopedia;

import net.favouriteless.modopedia.api.book.Book;
import net.favouriteless.modopedia.api.book.BookContent.LocalisedBookContent;
import net.favouriteless.modopedia.api.book.BookTexture.FixedRectangle;
import net.favouriteless.modopedia.api.book.BookType;
import net.favouriteless.modopedia.book.text.Justify;
import net.favouriteless.modopedia.book.text.TextChunk;
import net.favouriteless.modopedia.book.text.TextParser;
import net.favouriteless.modopedia.client.screens.books.BookScreen;
import net.favouriteless.modopedia.client.screens.books.book_screen_pages.LandingScreenPage;
import net.favouriteless.modopedia.client.screens.books.book_screen_pages.ScreenPage;
import net.favouriteless.modopedia.common.book_types.LockedViewProvider;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;

import java.util.List;

public class EClassicLandingScreen<T extends BookType & LockedViewProvider> extends net.favouriteless.modopedia.client.screens.books.ClassicLandingScreen<T> {

    public EClassicLandingScreen(Book book, T type, String language, LocalisedBookContent content, BookScreen<?> lastScreen) {
        super(book, type, language, content, lastScreen);
    }

    @Override
    protected ScreenPage initFirstPage() {
        String rawLandingText = book.getRawLandingText();
        if(rawLandingText != null)
            rawLandingText = Language.getInstance().getOrDefault(rawLandingText);

        int lineWidth = texture.pages().getFirst().width();
        int lineHeight = Minecraft.getInstance().font.lineHeight;
        List<TextChunk> landingText = TextParser.parse(rawLandingText, getStyle(), lineWidth, lineHeight, language, Justify.LEFT);

        Font font = Minecraft.getInstance().font;

        FixedRectangle r = texture.titleBacker();

        Component title = this.title.copy().withStyle(ChatFormatting.BLACK);
        int titleX = r.width() / 2 - font.width(title) / 2 ;
        return new LandingScreenPage(this, title, Component.empty(), titleX, r.height() / 2 - 4, 10, landingText, 0, 0);
    }

}

