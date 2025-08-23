package net.favouriteless.enchanted.common.util;

import net.minecraft.util.Mth;

public class ColourUtils {

    public static int argbToInt(ARGB argb) {
        return argbToInt(argb.a, argb.r, argb.g, argb.b);
    }

    public static int argbToInt(int a, int r, int g, int b) {
        return a << 24 | r << 16 | g << 8 | b;
    }

    public static ARGB intToARGB(int colour) {
        return new ARGB(
                (colour >> 24) & 0xFF,
                (colour >> 16) & 0xFF,
                (colour >> 8) & 0xFF,
                colour & 0xFF
        );
    }

    public static AHSV argbToHSV(ARGB argb) {
        float a = argb.a() / 255.0F;
        float r = argb.r() / 255.0F;
        float g = argb.g() / 255.0F;
        float b = argb.b() / 255.0F;

        float min = Math.min(argb.r(), Math.min(argb.g(), argb.b()));
        float max = Math.max(argb.r(), Math.max(argb.g(), argb.b()));
        float d = max - min;

        float s = max != 0 ? d / max : 0;
        float h = 0;

        if(max == r)
            h = (60 * (g - b) / d);
        else if(max == g)
            h = (60 * (b - r) / d) + 120;
        else if(max == b)
            h = (60 * (r - g) / d) + 240;

        return new AHSV(a, h, s, max);
    }

    public static ARGB hsvToARGB(AHSV ahsv) {
        float h = ahsv.h() * 6;
        float c = ahsv.v() * ahsv.s();
        float x = c * (1 - Mth.abs(h % 2 - 1));
        float m = ahsv.v() - c;

        float a = ahsv.a();
        float r, g, b;
        r = g = b = m;

        switch((int)Math.floor(h)) {
            case 0:
                r += c;
                g += x;
            case 1:
                r += x;
                g += c;
            case 2:
                g += c;
                b += x;
            case 3:
                g += x;
                b += c;
            case 4:
                r += x;
                b += c;
            case 5:
                r += c;
                b += x;
        }

        return new ARGB((int)(a * 255.0F + 0.5F), (int)(r * 255 + 0.5F), (int)(g * 255 + 0.5F), (int)(b * 255 + 0.5F));
    }



    public record ARGB(int a, int r, int g, int b) {}

    public record AHSV(float a, float h, float s, float v) {}

}
