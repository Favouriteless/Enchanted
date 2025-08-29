package net.favouriteless.enchanted.common.enchanted.circle_magic.rites;

import net.favouriteless.enchanted.common.enchanted.familiars.FamiliarSavedData;
import net.favouriteless.enchanted.api.familiars.FamiliarEntry;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.level.portal.DimensionTransition;
import net.minecraft.world.phys.Vec3;

public class SummonFamiliarRite extends Rite {

    public SummonFamiliarRite(BaseRiteParams baseParams, RiteParams params) {
        super(baseParams, params);
    }

    @Override
    protected boolean onStart(RiteParams params) {
        FamiliarEntry entry = FamiliarSavedData.get(getLevel()).getEntry(params.caster);
        if(entry == null)
            return cancel();

        Vec3 center = pos.getBottomCenter().add(0, 0.01D, 0);

        Entity target = findEntity(entry.getUUID());

        if(target == null) { // If the target isn't loaded, create a new one. Old one should discard itself on load.
            target = entry.getType().getTypeOut().create(level);
            target.load(entry.getNbt());
            target.setPos(center);

            level.addFreshEntity(target);
            ((TamableAnimal)target).setPersistenceRequired();
            entry.setUUID(target.getUUID());
        }

        if(target.level() != level)
            target.changeDimension(new DimensionTransition(level, center, Vec3.ZERO, 0, 0, DimensionTransition.DO_NOTHING));
        else
            target.teleportTo(center.x, center.y, center.z);

        entry.setDismissed(false);
        level.playSound(null, center.x, center.y, center.z, SoundEvents.ENDERMAN_TELEPORT, SoundSource.NEUTRAL, 1.0F, 1.0F);
        randomParticles(ParticleTypes.PORTAL);

        return false;
    }

}
