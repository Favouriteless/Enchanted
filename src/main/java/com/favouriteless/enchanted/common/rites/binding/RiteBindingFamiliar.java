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

import com.favouriteless.enchanted.api.capabilities.EnchantedCapabilities;
import com.favouriteless.enchanted.api.rites.AbstractRite;
import com.favouriteless.enchanted.common.familiars.FamiliarType;
import com.favouriteless.enchanted.common.init.EnchantedItems;
import com.favouriteless.enchanted.common.init.registry.EnchantedBlocks;
import com.favouriteless.enchanted.common.init.registry.FamiliarTypes;
import com.favouriteless.enchanted.common.init.registry.RiteTypes;
import com.favouriteless.enchanted.common.rites.CirclePart;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;

import java.util.List;
import java.util.UUID;

public class RiteBindingFamiliar extends AbstractRite {

	public RiteBindingFamiliar() {
		super(RiteTypes.BINDING_FAMILIAR.get(), 8000, 0);
		CIRCLES_REQUIRED.put(CirclePart.SMALL, EnchantedBlocks.CHALK_WHITE.get());
		ITEMS_REQUIRED.put(EnchantedItems.TEAR_OF_THE_GODDESS.get(), 1);
		ITEMS_REQUIRED.put(EnchantedItems.ODOUR_OF_PURITY.get(), 1);
		ITEMS_REQUIRED.put(EnchantedItems.WHIFF_OF_MAGIC.get(), 1);
		ITEMS_REQUIRED.put(Items.DIAMOND, 1);
		ITEMS_REQUIRED.put(EnchantedItems.DEMONIC_BLOOD.get(), 1);
	}

	@Override
	protected void execute() {
		if(targetEntity != null) {
			Player caster = level.getPlayerByUUID(casterUUID);

			if(caster != null) {
				FamiliarType familiarType = FamiliarTypes.getByProducer(targetEntity.getType());

				if(familiarType != null) {
					Entity newEntity = familiarType.getTypeOut().create(level);
					newEntity.setPos(targetEntity.position());

					if(newEntity instanceof TamableAnimal tamable) {
						tamable.setOwnerUUID(casterUUID);
						tamable.setTame(true);
					}

					targetEntity.discard();
					level.addFreshEntity(newEntity);

					caster.getCapability(EnchantedCapabilities.FAMILIAR).ifPresent(cap -> {
						if(cap.getFamiliarType() != null)
							cap.setFamiliarUUID(null);


						cap.setFamiliarType(targetEntity.getType());
						cap.setFamiliarUUID(newEntity.getUUID());
					});
				}
			}
		}
		level.playSound(null, pos, SoundEvents.ELDER_GUARDIAN_CURSE, SoundSource.PLAYERS, 1.0F, 1.0F);
		stopExecuting();
	}

	@Override
	protected boolean checkAdditional() {
		List<Entity> entities = CirclePart.SMALL.getEntitiesInside(level, pos, entity -> FamiliarTypes.isFamiliarProducer(entity.getType()));

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
