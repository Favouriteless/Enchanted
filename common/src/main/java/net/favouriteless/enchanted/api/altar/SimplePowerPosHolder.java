package net.favouriteless.enchanted.api.altar;

import net.favouriteless.enchanted.api.altar.PowerConsumer.PowerPosHolder;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;

import java.util.ArrayList;
import java.util.List;

/**
 * Simple {@link PowerPosHolder} implementation which stores the provided {@link BlockPos} by their distance from the
 * holder.
 */
public class SimplePowerPosHolder implements PowerPosHolder {

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
	public void remove(BlockPos pos) {
		altars.remove(pos);
	}

	@Override
	public void add(BlockPos pos) {
        if(altars.isEmpty()) {
            altars.add(pos);
            return;
        }
        if(altars.contains(pos))
            return;

        for(int i = 0; i < altars.size(); i++) { // Inserts new pos in sorted place.
            if(this.pos.distSqr(pos) < this.pos.distSqr(altars.get(i))) {
                altars.add(i, pos);
                return;
            }
            else if(i == altars.size() - 1) {
                altars.add(pos);
                return;
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
