package com.favouriteless.enchanted.common.rites;

import com.favouriteless.enchanted.api.rites.AbstractRite;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;

import java.util.UUID;

public class RiteType<T extends AbstractRite> {

	private final ResourceLocation id;
	private final RiteFactory<T> factory;

	public RiteType(ResourceLocation id, RiteFactory<T> factory) {
		this.id = id;
		this.factory = factory;
	}

	public ResourceLocation getId() {
		return id;
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
