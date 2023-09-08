/*
 *
 *   Copyright (c) 2023. Favouriteless
 *   Enchanted, a minecraft mod.
 *   GNU GPLv3 License
 *
 *       This file is part of Enchanted.
 *
 *       Enchanted is free software: you can redistribute it and/or modify
 *       it under the terms of the GNU General Public License as published by
 *       the Free Software Foundation, either version 3 of the License, or
 *       (at your option) any later version.
 *
 *       Enchanted is distributed in the hope that it will be useful,
 *       but WITHOUT ANY WARRANTY; without even the implied warranty of
 *       MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *       GNU General Public License for more details.
 *
 *       You should have received a copy of the GNU General Public License
 *       along with Enchanted.  If not, see <https://www.gnu.org/licenses/>.
 *
 *
 */

package com.favouriteless.enchanted.api.familiars;

import com.favouriteless.enchanted.api.capabilities.EnchantedCapabilities;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.NonNullConsumer;

public class FamiliarHelper {

	/**
	 * Attempt to dismiss a familiar. Dismissed familiars will discard themselves and passive bonuses will stop working.
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

	public static void runIfCap(Level level, NonNullConsumer<IFamiliarCapability> consumer) {
		level.getServer().getLevel(Level.OVERWORLD).getCapability(EnchantedCapabilities.FAMILIAR).ifPresent(consumer);
	}

}
