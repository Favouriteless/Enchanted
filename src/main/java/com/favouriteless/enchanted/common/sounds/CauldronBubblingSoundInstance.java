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

package com.favouriteless.enchanted.common.sounds;

import com.favouriteless.enchanted.common.blockentities.CauldronBlockEntity;
import com.favouriteless.enchanted.common.init.registry.EnchantedSoundEvents;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.sounds.SoundSource;

public class CauldronBubblingSoundInstance extends AbstractTickableSoundInstance {

	private final CauldronBlockEntity<?> blockEntity;

	public CauldronBubblingSoundInstance(CauldronBlockEntity<?> blockEntity) {
		super(EnchantedSoundEvents.CAULDRON_BUBBLING.get(), SoundSource.BLOCKS);
		this.blockEntity = blockEntity;
		this.looping = true;
		this.delay = 0;
		this.x = blockEntity.getBlockPos().getX() + 0.5D;
		this.y = blockEntity.getBlockPos().getY() + 0.5D;
		this.z = blockEntity.getBlockPos().getZ() + 0.5D;
		this.volume = blockEntity.isHot() ? 1.0F : 0.0F;
	}

	public boolean canPlaySound() {
		return !blockEntity.isRemoved();
	}

	public boolean canStartSilent() {
		return true;
	}

	@Override
	public void tick() {
		if(blockEntity.isRemoved()) {
			stop();
		}
		else {
			if(blockEntity.isHot()) {
				if(volume < 1.0F)
					volume += 0.05F;
				else
					volume = 1.0F;
			}
			else {
				if(volume < 0.0F)
					volume -= 0.05F;
				else
					volume = 0.0F;
			}
		}
	}

}
