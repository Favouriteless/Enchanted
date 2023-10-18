package com.favouriteless.enchanted.common.rites.entity.protection;

import com.favouriteless.stateobserver.api.StateChangeSet.StateChange;
import com.favouriteless.stateobserver.api.StateObserver;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

public class RiteOfProtectionObserver extends StateObserver {

	public final Block block;

	public RiteOfProtectionObserver(Level level, BlockPos pos, int radiusX, int radiusY, int radiusZ, Block block) {
		super(level, pos, radiusX, radiusY, radiusZ);
		this.block = block;
	}

	@Override
	protected void handleChanges() {
		if(!getLevel().isClientSide) {
			for(StateChange change : getStateChangeSet().getChanges()) { // For all changes
				if(change.oldState().getBlock() == block) {
					if(change.newState().getBlock() != block) {
						getLevel().setBlockAndUpdate(change.pos(), change.oldState());
					}
				}
			}
		}
	}

	@Override
	public void onInit() {

	}

	@Override
	public void onRemove() {

	}

}
