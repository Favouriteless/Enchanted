package net.favouriteless.enchanted.api.altar;

import net.favouriteless.enchanted.api.altar.PowerConsumer.PowerPosHolder;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

import java.util.List;

public class PowerHelper {

    /**
     * Attempt to grab a {@link PowerProvider} from an {@link PowerPosHolder}.
     * If one of the positions is for some reason invalid, the position will be removed.
     *
     * @param level  The {@link Level} to look for altars in.
     * @param holder The {@link PowerPosHolder} containing the positions which need checking.
     *
     * @return The first valid {@link PowerProvider} found in level in the
     * positions provided by holder.
     */
    public static PowerProvider tryGetProvider(Level level, PowerPosHolder holder) {
        if (level == null) {
            return null;
        }

        List<BlockPos> providers = holder.getPositions();
        while (!providers.isEmpty()) {
            BlockPos pos = providers.getFirst();
            if (level.getBlockEntity(pos) instanceof PowerProvider provider) {
                return provider;
            }
            providers.remove(pos); // Remove "fake" entries
        }
        return null;
    }

}
