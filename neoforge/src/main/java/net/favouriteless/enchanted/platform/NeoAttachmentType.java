package net.favouriteless.enchanted.platform;

import com.mojang.serialization.Codec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.attachment.AttachmentType.Builder;

import java.util.function.Supplier;

public class NeoAttachmentType<T> implements EAttachmentType<T> {

    private final AttachmentType<T> attachment;
    private final Supplier<T> defaultSupplier;

    public NeoAttachmentType(Supplier<T> defaultSupplier, Codec<T> codec,
                             StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec, boolean copyOnDeath) {
        Builder<T> builder = AttachmentType.builder(defaultSupplier);
        if(codec != null) {
            builder.serialize(codec);
            if(copyOnDeath)
                builder.copyOnDeath();
        }
        if(streamCodec != null)
            builder.sync(streamCodec);

        this.attachment = builder.build();
        this.defaultSupplier = defaultSupplier;
    }

    @Override
    public AttachmentType<T> getAttachment() {
        return attachment;
    }

    @Override
    public T getDefault() {
        return defaultSupplier.get();
    }

}
