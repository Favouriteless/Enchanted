package com.favouriteless.enchanted.common.curses;

import com.favouriteless.enchanted.api.curses.AbstractCurse;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Supplier;

public class CurseType<T extends AbstractCurse> {

	private final ResourceLocation id;
	private final Supplier<T> supplier;

	public CurseType(ResourceLocation id, Supplier<T> supplier) {
		this.id = id;
		this.supplier = supplier;
	}

	public T create() {
		return this.supplier.get();
	}

}
