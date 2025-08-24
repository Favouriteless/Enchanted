package net.favouriteless.enchanted.client;

import net.favouriteless.enchanted.common.blocks.entity.KettleBlockEntity;
import net.favouriteless.enchanted.common.init.ESoundEvents;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;

public class BubblingSoundInstance extends AbstractTickableSoundInstance {

	private final KettleBlockEntity be;

	public BubblingSoundInstance(KettleBlockEntity be) {
		super(ESoundEvents.CAULDRON_BUBBLING.value(), SoundSource.BLOCKS, SoundInstance.createUnseededRandom());
		BlockPos pos = be.getBlockPos();

		this.be = be;
		this.looping = true;
		this.delay = 0;
		this.x = pos.getX() + 0.5D;
		this.y = pos.getY() + 0.5D;
		this.z = pos.getZ() + 0.5D;
		this.volume = be.isHot() ? 1.0F : 0.0F;
	}

	public boolean canPlaySound() {
		return !be.isRemoved();
	}

	public boolean canStartSilent() {
		return true;
	}

	@Override
	public void tick() {
		if(be.isRemoved())
			stop();
		else {
			if(be.isHot()) {
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
