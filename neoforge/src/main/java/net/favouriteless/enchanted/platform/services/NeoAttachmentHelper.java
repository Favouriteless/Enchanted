package net.favouriteless.enchanted.platform.services;

import com.mojang.serialization.Codec;
import net.favouriteless.enchanted.platform.EAttachmentType;
import net.favouriteless.enchanted.platform.EServices;
import net.favouriteless.enchanted.platform.NeoAttachmentType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public class NeoAttachmentHelper implements AttachmentHelper {

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
        NeoAttachmentType<T> attach = new NeoAttachmentType<>(defaultSupplier, codec, streamCodec, copyOnDeath);
        EServices.REGISTRY.register(NeoForgeRegistries.ATTACHMENT_TYPES, name, attach::getAttachment);
        return attach;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T get(Object holder, EAttachmentType<T> attachment) {
        if (holder instanceof IAttachmentHolder attachHolder) {
            return attachHolder.getData((AttachmentType<T>) attachment.getAttachment());
        }
        return attachment.getDefault();
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> void set(Object holder, EAttachmentType<T> attachment, T data) {
        if (holder instanceof IAttachmentHolder attachHolder) {
            attachHolder.setData((AttachmentType<T>) attachment.getAttachment(), data);
        }
    }

}