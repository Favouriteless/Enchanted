package com.favouriteless.enchanted.common.sounds;

import com.favouriteless.enchanted.common.blocks.entity.CauldronBlockEntity;
import com.favouriteless.enchanted.common.init.registry.EnchantedSoundEvents;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundSource;

public class CauldronBubblingSoundInstance extends AbstractTickableSoundInstance {

	private final CauldronBlockEntity<?> blockEntity;

	public CauldronBubblingSoundInstance(CauldronBlockEntity<?> blockEntity) {
		super(EnchantedSoundEvents.CAULDRON_BUBBLING.get(), SoundSource.BLOCKS, SoundInstance.createUnseededRandom());
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
		if(blockEntity.isRemoved())
			stop();
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
