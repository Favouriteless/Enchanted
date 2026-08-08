package net.favouriteless.enchanted.api;

/**
 * Alternative to Forge's INBTSerializable
 */
public interface ISerializable<T> {

    T serialize();

    void deserialize(T data);

}
