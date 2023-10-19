package com.favouriteless.enchanted.platform;

import com.favouriteless.enchanted.platform.services.IClientRegistryHelper;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class ForgeClientRegistryHelper implements IClientRegistryHelper {

	@Override
	public <T extends Entity> void register(EntityType<T> type, EntityRendererProvider<T> constructor) {
		EntityRenderers.register(type, constructor);
	}

	@Override
	public <T extends BlockEntity> void register(BlockEntityType<T> type, BlockEntityRendererProvider<T> constructor) {
		BlockEntityRenderers.register(type, constructor);
	}

}
