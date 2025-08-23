package net.favouriteless.enchanted.common.util;

import net.favouriteless.enchanted.common.Enchanted;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public class LangUtils {

    public static MutableComponent translatable(String prefix, String suffix) {
        return Component.translatable(key(prefix, suffix));
    }

    public static MutableComponent translatable(String prefix, String suffix, Object... args) {
        return Component.translatable(key(prefix, suffix), args);
    }

    public static String key(String prefix, String suffix) {
        return key(Enchanted.MOD_ID, prefix, suffix);
    }

    public static String key(String modId, String prefix, String suffix) {
        return String.format("%s.%s.%s", prefix, modId, suffix);
    }

    public static String tooltip(String suffix) {
        return key("tooltip", suffix);
    }

    public static String jei(String suffix) {
        return key("jei", suffix);
    }

    public static String jeiCategory(String suffix) {
        return key("jei.category", suffix);
    }

    public static String rite(String suffix) {
        return key("rite", suffix);
    }

    public static String circleShape(String suffix) {
        return key("circle_magic.shape", suffix);
    }

    public static String book(String suffix) {
        return key("book", suffix);
    }

    public static String bookTitle(String suffix) {
        return key("book.title", suffix);
    }

    public static String bookSubtitle(String suffix) {
        return key("book.subtitle", suffix);
    }

    public static String bookHeader(String suffix) {
        return key("book.header", suffix);
    }

    public static String bookLandingText(String suffix) {
        return key("book.landing_text", suffix);
    }

    public static String item(String suffix) {
        return key("item", suffix);
    }

    public static String tab(String suffix) {
        return key("itemGroup", suffix);
    }

}
