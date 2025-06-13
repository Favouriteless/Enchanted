package net.favouriteless.enchanted.integrations.modopedia.common;

import com.mojang.serialization.MapCodec;
import net.favouriteless.enchanted.common.Enchanted;
import net.favouriteless.modopedia.api.book.BookType;

public class EClassicBookType implements BookType {

    public static final Type<EClassicBookType> TYPE = new Type<>(Enchanted.id("classic"), MapCodec.unit(new EClassicBookType()));

    @Override
    public Type<EClassicBookType> type() {
        return TYPE;
    }

}
