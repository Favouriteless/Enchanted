package net.favouriteless.enchanted.common.circle_magic.rites;

import net.favouriteless.enchanted.api.curses.Curse;
import net.favouriteless.enchanted.api.curses.CurseManager;
import net.favouriteless.enchanted.common.curses.CurseSavedData;
import net.favouriteless.enchanted.api.familiars.FamiliarSavedData;
import net.favouriteless.enchanted.api.familiars.IFamiliarEntry;
import net.favouriteless.enchanted.common.curses.CurseManagerImpl;
import net.favouriteless.enchanted.common.curses.CurseType;
import net.favouriteless.enchanted.common.familiars.FamiliarTypes;
import net.favouriteless.enchanted.common.init.EParticleTypes;
import net.favouriteless.enchanted.common.init.ESoundEvents;
import net.minecraft.sounds.SoundSource;

import java.util.List;

public class RemoveCurseRite extends Rite {

    public static final int RAISE = 300;
    public static final int START_SOUND = 190;

    private final CurseType<?> curse;

    public RemoveCurseRite(BaseRiteParams baseParams, RiteParams params, CurseType<?> curse) {
        super(baseParams, params);
        this.curse = curse;
    }

    @Override
    protected boolean onStart(RiteParams params) {
        if(params.target == null)
            return cancel();

        level.sendParticles(EParticleTypes.REMOVE_CURSE_SEED.get(), pos.getX() + 0.5d, pos.getY() + 2.5d, pos.getZ() + 0.5d, 1, 0, 0, 0, 0);
        return true;
    }

    @Override
    protected boolean onTick(RiteParams params) {
        if(params.ticks() == START_SOUND) {
            level.playSound(null, pos, ESoundEvents.REMOVE_CURSE.value(), SoundSource.MASTER, 1.0F, 1.0F);
            return true;
        }

        if(params.ticks() == RAISE) {
            IFamiliarEntry familiar = FamiliarSavedData.get(level).getEntry(params.caster);

            int strength = 0;
            if(familiar != null && familiar.getType() == FamiliarTypes.CAT)
                strength++;

            CurseSavedData data = CurseSavedData.get(level);
            List<Curse> curses = data.get(params.target);

            for(Curse curse : curses) {
                if(curse.type != this.curse)
                    continue;

                double chance = (strength - curse.strength) * 0.2D; // 20% chance to fail per level difference.

                if(Math.random() > chance) {
                    CurseManager.get().removeCurse(level, curse);
                }
                else if(curse.strength < CurseManagerImpl.MAX_STRENGTH) {
                    curse.strength += 1;
                    data.setDirty();
                }
                break;
            }
            return false;
        }
        return true;
    }

}
