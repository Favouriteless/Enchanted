package net.favouriteless.enchanted.common.circle_magic.rites;

import net.favouriteless.enchanted.api.familiars.FamiliarSavedData;
import net.favouriteless.enchanted.api.familiars.IFamiliarEntry;
import net.favouriteless.enchanted.common.curses.CurseManager;
import net.favouriteless.enchanted.common.curses.CurseType;
import net.favouriteless.enchanted.common.familiars.FamiliarTypes;
import net.favouriteless.enchanted.common.init.EParticleTypes;
import net.favouriteless.enchanted.common.init.ESoundEvents;
import net.minecraft.sounds.SoundSource;

public class ApplyCurseRite extends Rite {

    private final CurseType<?> curse;

    public ApplyCurseRite(BaseRiteParams baseParams, RiteParams params, CurseType<?> curse) {
        super(baseParams, params);
        this.curse = curse;
    }

    @Override
    protected boolean onStart(RiteParams params) {
        if(params.target == null || params.caster == null)
            return cancel();

        int casterLevel = 0;

        IFamiliarEntry familiarEntry = FamiliarSavedData.get(level).getEntry(params.caster);

        if(familiarEntry != null) {
            if(!familiarEntry.isDismissed() && familiarEntry.getType() == FamiliarTypes.CAT)
                casterLevel++;
        }

        level.sendParticles(EParticleTypes.CURSE_SEED.get(), pos.getX()+0.5D, pos.getY(), pos.getZ()+0.5D, 1, 0.0D, 0.0D, 0.0D, 0.0D);
        level.playSound(null, pos, ESoundEvents.CURSE_CAST.get(), SoundSource.MASTER, 1.5F, 1.0F);
        CurseManager.createCurse(level, curse, params.target, params.caster, casterLevel);
        return false;
    }

}
