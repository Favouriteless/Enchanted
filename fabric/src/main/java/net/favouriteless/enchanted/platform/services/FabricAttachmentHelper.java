package net.favouriteless.enchanted.platform.services;

import com.mojang.serialization.Codec;
import net.fabricmc.fabric.api.attachment.v1.AttachmentTarget;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.favouriteless.enchanted.common.Enchanted;
import net.favouriteless.enchanted.platform.EAttachmentType;
import net.favouriteless.enchanted.platform.FabricAttachmentType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

import java.util.function.Supplier;

public class FabricAttachmentHelper implements AttachmentHelper {

    @Override
    public <T> EAttachmentType<T> register(String name, Supplier<T> defaultSupplier) {
        return register(name, defaultSupplier, null, null, false);
    }

    @Override
    public <T> EAttachmentType<T> register(String name, Supplier<T> defaultSupplier, Codec<T> codec, boolean copyOnDeath) {
        return register(name, defaultSupplier, codec, null, copyOnDeath);
    }

    @Override
    public <T> EAttachmentType<T> register(String name, Supplier<T> defaultSupplier, Codec<T> codec, StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec, boolean copyOnDeath) {
        return new FabricAttachmentType<>(Enchanted.id(name), defaultSupplier, codec, streamCodec, copyOnDeath);
    }

    @Override
    @SuppressWarnings({ "unchecked", "UnstableApiUsage" })
    public <T> T get(Object holder, EAttachmentType<T> attachment) {
        if (holder instanceof AttachmentTarget target) {
            return target.getAttachedOrCreate((AttachmentType<T>) attachment.getAttachment());
        }
        return attachment.getDefault();
    }

    @Override
    @SuppressWarnings({ "unchecked", "UnstableApiUsage" })
    public <T> void set(Object holder, EAttachmentType<T> attachment, T data) {
        if (holder instanceof AttachmentTarget target) {
            target.setAttached((AttachmentType<T>) attachment.getAttachment(), data);
        }
    }

}
