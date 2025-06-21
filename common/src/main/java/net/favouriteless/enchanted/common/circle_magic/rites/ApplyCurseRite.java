package net.favouriteless.enchanted.common.circle_magic.rites;

import net.favouriteless.enchanted.api.curses.CurseManager;
import net.favouriteless.enchanted.api.familiars.FamiliarSavedData;
import net.favouriteless.enchanted.api.familiars.IFamiliarEntry;
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
        if(params.target == null)
            return cancel();

        IFamiliarEntry familiar = FamiliarSavedData.get(level).getEntry(params.caster);

        int strength = familiar != null && familiar.getType() == FamiliarTypes.CAT ? 1 : 0;

        level.sendParticles(EParticleTypes.CURSE_SEED.get(), pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D, 1, 0, 0, 0, 0);
        level.playSound(null, pos, ESoundEvents.CURSE_CAST.value(), SoundSource.MASTER, 1.5F, 1.0F);

        CurseManager.get().createCurse(curse, level, params.target, strength);
        return false;
    }

}
