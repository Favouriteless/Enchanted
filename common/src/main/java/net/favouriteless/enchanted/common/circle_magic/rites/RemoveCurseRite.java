package net.favouriteless.enchanted.common.circle_magic.rites;

import net.favouriteless.enchanted.api.curses.Curse;
import net.favouriteless.enchanted.api.curses.CurseSavedData;
import net.favouriteless.enchanted.api.familiars.FamiliarSavedData;
import net.favouriteless.enchanted.api.familiars.IFamiliarEntry;
import net.favouriteless.enchanted.common.curses.CurseManager;
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
            level.playSound(null, pos, ESoundEvents.REMOVE_CURSE.get(), SoundSource.MASTER, 1.0F, 1.0F);
            return true;
        }

        if(params.ticks() == RAISE) {
            List<Curse> curses = CurseSavedData.get(level).entries.get(params.target);

            if(curses == null)
                return false;

            int casterLevel = 0;

            FamiliarSavedData data = FamiliarSavedData.get(level);
            IFamiliarEntry familiarEntry = data.getEntry(params.caster);
            if(familiarEntry != null && !familiarEntry.isDismissed() && familiarEntry.getType() == FamiliarTypes.CAT)
                casterLevel++;

            for(Curse curse : curses) {
                if(curse.getType() == this.curse) {
                    int diff = casterLevel - curse.getLevel();

                    double cureChance = 1.0D + (diff * 0.2D); // If the caster is equal level, there is an 100% chance the curse will be cured.

                    if(Math.random() < cureChance)
                        CurseManager.removeCurse(level, curse);
                    else if(curse.getLevel() < CurseManager.MAX_LEVEL)
                        curse.setLevel(curse.getLevel() + 1);
                    break;
                }
            }
            return false;
        }
        return true;
    }

}
