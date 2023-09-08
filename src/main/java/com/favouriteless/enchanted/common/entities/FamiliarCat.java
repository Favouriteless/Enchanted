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

package com.favouriteless.enchanted.common.entities;

import com.favouriteless.enchanted.Enchanted;
import com.favouriteless.enchanted.api.familiars.FamiliarHelper;
import com.favouriteless.enchanted.api.familiars.IFamiliarCapability.FamiliarEntry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class FamiliarCat extends Cat {

	public FamiliarCat(EntityType<? extends Cat> type, Level level) {
		super(type, level);
	}

	@Override
	public InteractionResult interact(Player player, InteractionHand hand) {
		if(player.isCrouching() && hand == InteractionHand.MAIN_HAND) {
			if(!level.isClientSide) {
				if(player.getUUID().equals(getOwnerUUID())) {
					FamiliarHelper.dismiss(this);
					return InteractionResult.sidedSuccess(level.isClientSide);
				}
			}
		}
		return super.interact(player, hand);
	}

	@Override
	public void tick() {
		super.tick();
		if(!level.isClientSide) {
			FamiliarHelper.runIfCap(level, cap -> {
				FamiliarEntry entry = cap.getFamiliarFor(getOwnerUUID());
				if(entry == null || !getUUID().equals(entry.getUUID())) {
					discard();
					Enchanted.LOGGER.info(String.format("Found familiar with non-matching UUID for %s, discarding.", getOwnerUUID()));
				}
			});
		}
	}

	@Override
	public void addAdditionalSaveData(CompoundTag nbt) {
		super.addAdditionalSaveData(nbt);
		if(!level.isClientSide) {
			FamiliarHelper.runIfCap(level, cap -> {
				FamiliarEntry entry = cap.getFamiliarFor(getOwnerUUID());
				if(entry != null)
					entry.setNbt(nbt);
			});
		}
	}

	@Override
	public void die(DamageSource cause) {
		super.die(cause);
		if(!level.isClientSide)
			FamiliarHelper.dismiss(this);
	}

	public static AttributeSupplier.Builder createAttributes() {
		return Monster.createMonsterAttributes()
				.add(Attributes.MAX_HEALTH, 50.0D)
				.add(Attributes.ARMOR, 5.0D)
				.add(Attributes.MOVEMENT_SPEED, 0.3F)
				.add(Attributes.ATTACK_DAMAGE, 3.0D);
	}

	@Override
	public boolean isInvulnerableTo(DamageSource source) {
		return source == DamageSource.LAVA ||
				source == DamageSource.IN_FIRE ||
				source == DamageSource.ON_FIRE ||
				source == DamageSource.FALL ||
				source == DamageSource.DROWN;
	}

}

