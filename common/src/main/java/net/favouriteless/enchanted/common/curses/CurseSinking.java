package net.favouriteless.enchanted.common.curses;

import net.favouriteless.enchanted.api.curses.Curse;
import net.favouriteless.enchanted.common.network.client.SinkingCursePayload;
import net.favouriteless.enchanted.platform.CommonServices;
import net.minecraft.server.level.ServerPlayer;

public class CurseSinking extends Curse {

	public boolean wasSwimming = false;
	public boolean wasFlying = false;

	public CurseSinking() {
		super(CurseTypes.SINKING);
	}

	@Override
	protected void onTick(final ServerPlayer target, long ticks) {
		boolean isSwimming = target.isInWater();
		boolean isFlying = target.isFallFlying();

		if(isSwimming == wasSwimming && isFlying == wasFlying)
			return;

        if(isSwimming)
            CommonServices.NETWORK.sendToPlayer(new SinkingCursePayload(-0.025F * (strength + 1)), target);
        else if(isFlying)
            CommonServices.NETWORK.sendToPlayer(new SinkingCursePayload(-0.05F * (strength + 1)), target);
        else
            CommonServices.NETWORK.sendToPlayer(new SinkingCursePayload(0.0F), target);

        wasSwimming = isSwimming;
        wasFlying = isFlying;
    }

	@Override
	public void onRemove(ServerPlayer target) {
		CommonServices.NETWORK.sendToPlayer(new SinkingCursePayload(0), target);
	}

}
