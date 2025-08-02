package net.favouriteless.enchanted.common.circle_magic.rites;

import net.favouriteless.enchanted.api.curses.Curse.Type;
import net.favouriteless.enchanted.api.curses.CurseInstance;
import net.favouriteless.enchanted.api.curses.CurseManager;
import net.favouriteless.enchanted.api.familiars.FamiliarSavedData;
import net.favouriteless.enchanted.api.familiars.IFamiliarEntry;
import net.favouriteless.enchanted.common.familiars.FamiliarTypes;
import net.favouriteless.enchanted.common.init.EParticleTypes;
import net.favouriteless.enchanted.common.init.ESoundEvents;
import net.minecraft.sounds.SoundSource;

public class RemoveCurseRite extends Rite {

    public static final int RAISE = 300;
    public static final int START_SOUND = 190;

    private final Type<?> curse;

    public RemoveCurseRite(BaseRiteParams baseParams, RiteParams params, Type<?> curse) {
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

        if(params.ticks() < RAISE)
            return true;

        IFamiliarEntry familiar = FamiliarSavedData.get(level).getEntry(params.caster);

        int removeLevel = 0;
        if(familiar != null && familiar.getType() == FamiliarTypes.CAT)
            removeLevel++;

        CurseManager manager = CurseManager.get();

        for(CurseInstance instance : manager.getCurses(params.target, level)) {
            if(!instance.getCurse().type().equals(curse))
                continue;

            double chance = (instance.getStrength() - removeLevel) * 0.2D; // 20% chance to fail per level difference.

            if(Math.random() > chance)
                manager.removeCurse(curse, params.target, level);
            else
                manager.applyCurse(curse, params.target, instance.getStrength() + 1, level);
        }
        return false;
    }

}
