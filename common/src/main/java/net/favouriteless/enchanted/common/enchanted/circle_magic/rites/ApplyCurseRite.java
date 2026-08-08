package net.favouriteless.enchanted.common.enchanted.circle_magic.rites;

import net.favouriteless.enchanted.api.curses.Curse.Type;
import net.favouriteless.enchanted.api.curses.CurseManager;
import net.favouriteless.enchanted.api.familiars.FamiliarEntry;
import net.favouriteless.enchanted.common.enchanted.familiars.EFamiliarTypes;
import net.favouriteless.enchanted.common.enchanted.familiars.FamiliarSavedData;
import net.favouriteless.enchanted.common.init.EParticleTypes;
import net.favouriteless.enchanted.common.init.ESoundEvents;
import net.minecraft.sounds.SoundSource;

public class ApplyCurseRite extends Rite {

    private final Type<?> curse;

    public ApplyCurseRite(BaseRiteParams baseParams, RiteParams params, Type<?> curse) {
        super(baseParams, params);
        this.curse = curse;
    }

    @Override
    protected boolean onStart(RiteParams params) {
        if (params.target == null) {
            return cancel();
        }

        FamiliarEntry familiar = FamiliarSavedData.get(level).getEntry(params.caster);

        int curseLevel = familiar != null && familiar.getType() == EFamiliarTypes.CAT ? 1 : 0;

        level.sendParticles(EParticleTypes.CURSE_SEED.get(), pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D, 1, 0, 0, 0, 0);
        level.playSound(null, pos, ESoundEvents.CURSE_CAST.value(), SoundSource.MASTER, 1.5F, 1.0F);

        CurseManager.get().applyCurse(curse, params.target, curseLevel, level);
        return false;
    }

}
