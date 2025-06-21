package net.favouriteless.enchanted.common.curses;

import net.favouriteless.enchanted.api.curses.Curse;
import net.minecraft.resources.ResourceLocation;

import java.util.UUID;
import java.util.function.Supplier;

public class CurseType<T extends Curse> {

	private final ResourceLocation id;
	private final Supplier<T> supplier;

	public CurseType(ResourceLocation id, Supplier<T> supplier) {
		this.id = id;
		this.supplier = supplier;
	}

	public T create() {
		return this.supplier.get();
	}

	public T create(UUID target, int strength) {
		T curse = create();
		curse.setTargetUUID(target);
		curse.strength = strength;
		return curse;
	}

	public ResourceLocation getId() {
		return id;
	}

}
