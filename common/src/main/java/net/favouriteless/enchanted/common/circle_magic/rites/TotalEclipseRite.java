package net.favouriteless.enchanted.common.circle_magic.rites;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;

public class TotalEclipseRite extends Rite {

    public TotalEclipseRite(BaseRiteParams baseParams, RiteParams params) {
        super(baseParams, params);
    }

    @Override
    protected boolean onStart(RiteParams params) {
        long t = level.getDayTime();
        level.setDayTime((t - t % Level.TICKS_PER_DAY) + 18000);
        level.playSound(null, pos, SoundEvents.ZOMBIE_VILLAGER_CURE, SoundSource.MASTER, 0.5F, 1.0F);
        return true;
    }

}
