package net.favouriteless.enchanted.platform;

import net.favouriteless.enchanted.platform.services.AttachmentHelper;

/**
 * Wrapper for NeoForge and Fabric AttachmentTypes.
 *
 * @param <T> Used in the {@link AttachmentHelper} methods for type safety.
 */
public interface EAttachmentType<T> {

    Object getAttachment();

    T getDefault();

}
