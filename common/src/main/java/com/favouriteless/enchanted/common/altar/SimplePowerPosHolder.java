package com.favouriteless.enchanted.common.altar;

import com.favouriteless.enchanted.api.power.IPowerConsumer.IPowerPosHolder;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;

import java.util.ArrayList;
import java.util.List;

/**
 * Simple {@link IPowerPosHolder} implementation which stores
 * the provided {@link BlockPos} by their distance from the holder.
 */
public class SimplePowerPosHolder implements IPowerPosHolder {

	private final List<BlockPos> altars = new ArrayList<>();
	private final BlockPos pos;

	public SimplePowerPosHolder(BlockPos pos) {
		this.pos = pos;
	}

	@Override
	public List<BlockPos> getPositions() {
		return altars;
	}

	@Override
	public void remove(BlockPos altarPos) {
		altars.remove(altarPos);
	}

	@Override
	public void add(BlockPos altarPos) {
		if(altars.isEmpty()) {
			altars.add(altarPos);
		}
		else {
			if(!altars.contains(pos)) {
				for(int i = 0; i < altars.size(); i++) {
					if(pos.distSqr(altarPos) < pos.distSqr(altars.get(i))) {
						altars.add(i, altarPos);
						return;
					}
					else if(i == altars.size() - 1) {
						altars.add(altarPos);
						return;
					}
				}
			}
		}
	}

	@Override
	public ListTag serializeNBT() {
		ListTag nbt = new ListTag();
		for(BlockPos pos : altars) {
			CompoundTag posTag = new CompoundTag();
			posTag.putInt("x", pos.getX());
			posTag.putInt("y", pos.getY());
			posTag.putInt("z", pos.getZ());
			nbt.add(posTag);
		}
		return nbt;
	}

	@Override
	public void deserializeNBT(ListTag nbt) {
		for(Tag tag : nbt) {
			CompoundTag posTag = (CompoundTag)tag;
			altars.add(new BlockPos(posTag.getInt("x"), posTag.getInt("y"), posTag.getInt("z")));
		}
	}

}
