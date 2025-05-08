package net.favouriteless.enchanted.api.power;

import net.favouriteless.enchanted.api.power.IPowerConsumer.IPowerPosHolder;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.List;

public class PowerHelper {

	/**
	 * Attempt to grab a {@link IPowerProvider} from an {@link IPowerPosHolder}.
	 * If one of the positions is for some reason invalid, the position will be removed.
	 *
	 * @param level The {@link Level} to look for altars in.
	 * @param holder The {@link IPowerPosHolder} containing the positions which need checking.
	 *
	 * @return The first valid {@link IPowerProvider} found in level in the
	 * positions provided by holder.
	 */
	public static IPowerProvider tryGetPowerProvider(Level level, IPowerPosHolder holder) {
		List<BlockPos> providers = holder.getPositions();
		while(!providers.isEmpty()) {
			if(level != null) {
				BlockPos pos = providers.getFirst();
				if(level.getBlockEntity(pos) instanceof IPowerProvider provider)
					return provider;
				else
					providers.remove(pos);
			}
		}
		return null;
	}

}
