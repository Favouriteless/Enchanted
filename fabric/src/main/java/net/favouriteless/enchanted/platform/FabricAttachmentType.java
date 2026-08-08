package net.favouriteless.enchanted.platform;

import com.mojang.serialization.Codec;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Supplier;

@SuppressWarnings("UnstableApiUsage")
public class FabricAttachmentType<T> implements EAttachmentType<T> {

    private final AttachmentType<T> attachment;
    private final Supplier<T> defaultSupplier;

    public FabricAttachmentType(ResourceLocation name, Supplier<T> defaultSupplier, Codec<T> codec,
                                StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec, boolean copyOnDeath) {
        this.attachment = AttachmentRegistry.create(
                name, builder -> {
                    builder.initializer(defaultSupplier);
                    if (codec != null) {
                        builder.persistent(codec);
                        if (copyOnDeath) {
                            builder.copyOnDeath();
                        }
                    }
                    if (streamCodec != null) {
                        builder.syncWith(streamCodec, (target, player) -> true);
                    }
                }
        );
        this.defaultSupplier = defaultSupplier;
    }

    @Override
    public Object getAttachment() {
        return attachment;
    }

    @Override
    public T getDefault() {
        return defaultSupplier.get();
    }

}
