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

package com.favouriteless.enchanted.common.rites.binding;

import com.favouriteless.enchanted.Enchanted;
import com.favouriteless.enchanted.api.familiars.FamiliarHelper;
import com.favouriteless.enchanted.api.rites.AbstractRite;
import com.favouriteless.enchanted.common.familiars.FamiliarType;
import com.favouriteless.enchanted.common.init.EnchantedItems;
import com.favouriteless.enchanted.common.init.registry.EnchantedBlocks;
import com.favouriteless.enchanted.common.init.registry.EnchantedParticles;
import com.favouriteless.enchanted.common.init.registry.FamiliarTypes;
import com.favouriteless.enchanted.common.init.registry.RiteTypes;
import com.favouriteless.enchanted.common.rites.CirclePart;
import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.UUID;

public class RiteBindingFamiliar extends AbstractRite {

	public static final int BIND_TICKS = 300;
	public static final int START_SOUND = 190;
	public static final Vec3 OFFSET = new Vec3(0.5D, 2.5D, 0.5D);

	public RiteBindingFamiliar() {
		super(RiteTypes.BINDING_FAMILIAR.get(), 8000, 0);
		CIRCLES_REQUIRED.put(CirclePart.MEDIUM, EnchantedBlocks.CHALK_WHITE.get());
		ITEMS_REQUIRED.put(EnchantedItems.TEAR_OF_THE_GODDESS.get(), 1);
		ITEMS_REQUIRED.put(EnchantedItems.ODOUR_OF_PURITY.get(), 1);
		ITEMS_REQUIRED.put(EnchantedItems.WHIFF_OF_MAGIC.get(), 1);
		ITEMS_REQUIRED.put(Items.DIAMOND, 1);
		ITEMS_REQUIRED.put(EnchantedItems.DEMONIC_BLOOD.get(), 1);
	}

	@Override
	protected void execute() {
		if(targetEntity != null) {
			Vec3 newPos = new Vec3(OFFSET.x + pos.getX(), OFFSET.y + pos.getY(), OFFSET.z + pos.getZ());
			targetEntity.setNoGravity(true);

			level.playSound(null, targetEntity.blockPosition(), SoundEvents.ENDERMAN_TELEPORT, SoundSource.NEUTRAL, 1.0F, 1.0F);
			double offset = targetEntity.getBbWidth() / 1.5D;

			level.sendParticles(ParticleTypes.PORTAL, targetEntity.getX(), targetEntity.getY(),  targetEntity.getZ(), 20 + Enchanted.RANDOM.nextInt(10), offset, offset, offset, 0.0D);
			targetEntity.teleportTo(newPos.x, newPos.y, newPos.z);
			level.sendParticles(ParticleTypes.PORTAL, targetEntity.getX(), targetEntity.getY(),  targetEntity.getZ(), 20 + Enchanted.RANDOM.nextInt(10), offset, offset, offset, 0.0D);

			level.sendParticles(EnchantedParticles.BIND_FAMILIAR_SEED.get(), newPos.x, newPos.y, newPos.z, 1, 0.0D, 0.0D, 0.0D, 0.0D);
		}
	}

	@Override
	protected void onTick() {
		if(!stillValid()) {
			stopExecuting();
			return;
		}

		if(ticks < BIND_TICKS) {
			double dx = (pos.getX() + OFFSET.x + Enchanted.RANDOM.nextDouble() * 0.2D) - 0.1D;
			double dy = ((pos.getY() + OFFSET.y + Enchanted.RANDOM.nextDouble() * 0.2D) - 0.1D) - targetEntity.getBbHeight() / 2.0D;
			double dz = (pos.getZ() + OFFSET.z + Enchanted.RANDOM.nextDouble() * 0.2D) - 0.1D;
			targetEntity.teleportTo(dx, dy, dz);
		}
		else {
			FamiliarType<?, ?> type = FamiliarTypes.getByInput(targetEntity.getType());

			if(type != null) {
				TamableAnimal familiar = type.getFor(level);
				familiar.setPos(targetEntity.getX(), targetEntity.getY(), targetEntity.getZ());
				familiar.setOwnerUUID(casterUUID);
				familiar.setTame(true);
				targetEntity.discard();
				level.addFreshEntity(familiar);

				FamiliarHelper.runIfCap(level, cap -> cap.setFamiliar(casterUUID, type, familiar));
			}
			level.playSound(null, pos, SoundEvents.ELDER_GUARDIAN_CURSE, SoundSource.PLAYERS, 1.0F, 1.0F);
			stopExecuting();
		}
	}

	private boolean stillValid() {
		return targetEntity != null && targetEntity.isAlive();
	}

	@Override
	protected boolean checkAdditional() {
		List<Entity> entities = CirclePart.MEDIUM.getEntitiesInside(level, pos, entity -> FamiliarTypes.getByInput(entity.getType()) != null);

		if(entities.size() > 0) {
			if(entities.get(0) instanceof TamableAnimal animal) {
				UUID ownerUUID = animal.getOwnerUUID();

				if(!ownerUUID.equals(casterUUID)) {
					Player caster = level.getPlayerByUUID(casterUUID);
					caster.displayClientMessage(new TextComponent("This creature does not trust you.").withStyle(ChatFormatting.RED), false);
					return false;
				}
			}
			targetEntity = entities.get(0);
			return true;
		}
		return false;
	}

}
