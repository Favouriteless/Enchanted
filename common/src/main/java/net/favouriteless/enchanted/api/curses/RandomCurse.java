package net.favouriteless.enchanted.api.curses;

import net.favouriteless.enchanted.common.curses.CurseType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;

import java.util.Random;

/**
 * A {@link Curse} which triggers randomly within a specified range of time.
 */
public abstract class RandomCurse extends Curse {

	public static final Random RANDOM = new Random();

	private final int min;
	private final int max;

	private long nextUseTick = 0;

	public RandomCurse(CurseType<?> type, int min, int max) {
		super(type);
		this.min = min;
		this.max = max;
	}

	@Override
	protected void onTick(ServerPlayer target, long ticks) {
		if(nextUseTick <= ticks) {
			execute(target);
			nextUseTick = ticks + RANDOM.nextLong(min * 20L, max * 20L);
		}
	}

	protected abstract void execute(ServerPlayer target);

	@Override
	protected void saveAdditional(CompoundTag nbt) {
		nbt.putLong("nextUse", nextUseTick);
	}

	@Override
	protected void loadAdditional(CompoundTag nbt) {
		nextUseTick = nbt.getLong("nextUse");
	}

}
