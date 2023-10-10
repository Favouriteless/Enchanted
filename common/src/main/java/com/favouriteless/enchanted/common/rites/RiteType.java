package com.favouriteless.enchanted.common.rites;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;

import java.util.UUID;

public class RiteType<T extends AbstractRite> extends ForgeRegistryEntry<RiteType<?>> {

	private final RiteFactory<T> factory;

	public RiteType(RiteFactory<T> factory) {
		this.factory = factory;
	}

	public T create(ServerLevel level, BlockPos pos, UUID caster) {
		return this.factory.create(this, level, pos, caster);
	}

	public T create() {
		return this.factory.create(this, null, null, null);
	}

	public interface RiteFactory<T extends AbstractRite> {
		T create(RiteType<T> type, ServerLevel level, BlockPos pos, UUID caster);
	}

}
