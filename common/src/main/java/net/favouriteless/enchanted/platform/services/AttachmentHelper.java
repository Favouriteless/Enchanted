package net.favouriteless.enchanted.platform.services;

import com.mojang.serialization.Codec;
import net.favouriteless.enchanted.platform.EAttachmentType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

import java.util.function.Supplier;

public interface AttachmentHelper {

    <T> EAttachmentType<T> register(String name, Supplier<T> defaultSupplier);

    <T> EAttachmentType<T> register(String name, Supplier<T> defaultSupplier, Codec<T> codec, boolean copyOnDeath);

    <T> EAttachmentType<T> register(String name, Supplier<T> defaultSupplier, Codec<T> codec, StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec, boolean copyOnDeath);

    <T> T get(Object holder, EAttachmentType<T> attachment);

    <T> void set(Object holder, EAttachmentType<T> attachment, T data);

}
