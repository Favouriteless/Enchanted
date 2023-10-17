package com.favouriteless.enchanted.api;

import net.minecraft.nbt.Tag;

/**
 * Alternative to Forge's INBTSerializable
 */
public interface ISerializable<T extends Tag> {

	T serializeNBT();
	void deserializeNBT(T arg);

}
