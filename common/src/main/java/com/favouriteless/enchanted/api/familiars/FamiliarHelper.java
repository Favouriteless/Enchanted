package com.favouriteless.enchanted.api.familiars;

import com.favouriteless.enchanted.api.capabilities.EnchantedCapabilities;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.TamableAnimal;

public class FamiliarHelper {

	/**
	 * Attempt to dismiss a familiar. Dismissed familiars be discarded and passive bonuses will stop working.
	 */
	public static void dismiss(TamableAnimal entity) {
		runIfCap(entity.getLevel(), cap -> {
			cap.getFamiliarFor(entity.getOwnerUUID()).setDismissed(true);

			double width = entity.getBbWidth();
			double height = entity.getBbHeight();
			((ServerLevel)entity.getLevel()).sendParticles(ParticleTypes.PORTAL, entity.getX(), entity.getY(), entity.getZ(), 30, width, height, width, 0.0D);

			entity.getLevel().playSound(null, entity.blockPosition(), SoundEvents.ENDERMAN_TELEPORT, SoundSource.NEUTRAL, 1.0F, 1.0F);
			entity.discard();
		});
	}

	/**
	 * Helper method to run a consumer on {@link IFamiliarCapability} regardless of which level is specified. This will
	 * only be present on the server and should always be present there.
	 * @param level Any level, just need level access of some kind.
	 * @param consumer Consumer to be run on the {@link LazyOptional#ifPresent(NonNullConsumer)} call.
	 */
	public static void runIfCap(Level level, NonNullConsumer<IFamiliarCapability> consumer) {
		level.getServer().getLevel(Level.OVERWORLD).getCapability(EnchantedCapabilities.FAMILIAR).ifPresent(consumer);
	}

}
