package net.favouriteless.enchanted.integrations.modopedia.common;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.favouriteless.enchanted.common.Enchanted;
import net.favouriteless.modopedia.api.book.BookType;
import net.favouriteless.modopedia.common.book_types.LockedViewProvider;
import net.favouriteless.modopedia.common.book_types.LockedViewType;

public record EClassicBookType(LockedViewType lockedViewType) implements BookType, LockedViewProvider {

    public static final MapCodec<EClassicBookType> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            LockedViewType.CODEC.optionalFieldOf("lock_view_type", LockedViewType.HIDDEN).forGetter(EClassicBookType::lockedViewType)
    ).apply(instance, EClassicBookType::new));

    public static final Type<EClassicBookType> TYPE = new Type<>(Enchanted.id("classic"), CODEC);

    @Override
    public Type<EClassicBookType> type() {
        return TYPE;
    }

}
