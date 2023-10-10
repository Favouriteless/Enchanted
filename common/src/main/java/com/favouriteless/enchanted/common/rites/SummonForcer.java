package com.favouriteless.enchanted.common.rites;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.portal.PortalInfo;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

/**
 * Implementation of {@link ITeleporter} which sets the target destination to an arbitrary position, for summoning rites.
 */
public class SummonForcer implements ITeleporter {

	private final Vec3 destination;

	public SummonForcer(Vec3 destination) {
		this.destination = destination;
	}

	@Nullable
	@Override
	public PortalInfo getPortalInfo(Entity entity, ServerLevel destWorld, Function<ServerLevel, PortalInfo> defaultPortalInfo) {
		return new PortalInfo(destination, Vec3.ZERO, 0.0F, 0.0F);
	}

	@Override
	public boolean playTeleportSound(ServerPlayer player, ServerLevel sourceWorld, ServerLevel destWorld) {
		return false;
	}
}
