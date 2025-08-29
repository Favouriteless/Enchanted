package net.favouriteless.enchanted.common.enchanted.altar;

import net.favouriteless.enchanted.api.altar.PowerConsumer.PowerPosHolder;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Simple {@link PowerPosHolder} implementation which stores
 * the provided {@link BlockPos} by their distance from the holder.
 */
public class SimplePowerPosHolder implements PowerPosHolder {

	private final List<BlockPos> altars = new ArrayList<>();
	private final BlockPos pos;

	public SimplePowerPosHolder(BlockPos pos) {
		this.pos = pos;
	}

	@Override
	@NotNull
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
			if(altars.contains(altarPos))
				return;

			for(int i = 0; i < altars.size(); i++) { // Crude sorting algorithm. Inserts new pos in correct place.
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

	@Override
	public CompoundTag serialize() {
		CompoundTag tag = new CompoundTag();
		tag.put("altars", BlockPos.CODEC.listOf().encodeStart(NbtOps.INSTANCE, altars).getOrThrow());
		return tag;
	}

	@Override
	public void deserialize(CompoundTag tag) {
		altars.clear();
		altars.addAll(BlockPos.CODEC.listOf().parse(NbtOps.INSTANCE, tag.get("altars")).getOrThrow());
	}

}
