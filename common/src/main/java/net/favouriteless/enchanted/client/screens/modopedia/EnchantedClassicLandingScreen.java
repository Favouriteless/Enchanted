package net.favouriteless.enchanted.client.screens.modopedia;

import net.favouriteless.modopedia.api.books.Book;
import net.favouriteless.modopedia.api.books.BookContent.LocalisedBookContent;
import net.favouriteless.modopedia.api.books.BookTexture.FixedRectangle;
import net.favouriteless.modopedia.book.text.Justify;
import net.favouriteless.modopedia.book.text.TextChunk;
import net.favouriteless.modopedia.book.text.TextParser;
import net.favouriteless.modopedia.client.screens.books.BookScreen;
import net.favouriteless.modopedia.client.screens.books.ClassicLandingScreen;
import net.favouriteless.modopedia.client.screens.books.book_screen_pages.LandingScreenPage;
import net.favouriteless.modopedia.client.screens.books.book_screen_pages.ScreenPage;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;

import java.util.List;

public class EnchantedClassicLandingScreen extends ClassicLandingScreen {

    public EnchantedClassicLandingScreen(Book book, String langCode, LocalisedBookContent content, BookScreen lastScreen) {
        super(book, langCode, content, lastScreen);
    }

    public EnchantedClassicLandingScreen(Book book, String langCode, LocalisedBookContent content) {
        this(book, langCode, content, null);
    }

    @Override
    protected ScreenPage initFirstPage() {
        String rawLandingText = book.getRawLandingText();
        if(rawLandingText != null)
            rawLandingText = Language.getInstance().getOrDefault(rawLandingText);

        List<TextChunk> landingText = TextParser.parse(rawLandingText, texture.pages().getFirst().width(),
                Minecraft.getInstance().font.lineHeight, Justify.LEFT, getStyle());

        Font font = Minecraft.getInstance().font;

        FixedRectangle r = texture.titleBacker();

        Component title = this.title.copy().withStyle(ChatFormatting.BLACK);
        int titleX = r.width() / 2 - font.width(title) / 2 ;
        return new LandingScreenPage(this, title, Component.empty(), titleX, r.height() / 2 - 4, 10, landingText, 0, 0);
    }

}

