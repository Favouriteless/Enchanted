package net.favouriteless.enchanted.common.circle_magic.rites;

import net.favouriteless.enchanted.api.familiars.FamiliarSavedData;
import net.favouriteless.enchanted.api.familiars.FamiliarType;
import net.favouriteless.enchanted.common.Enchanted;
import net.favouriteless.enchanted.common.familiars.FamiliarTypes;
import net.favouriteless.enchanted.common.init.EParticleTypes;
import net.favouriteless.enchanted.common.init.ESoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.UUID;

public class BindFamiliarRite extends Rite {

    public static final int BIND_TICKS = 300;
    public static final int START_SOUND = 175;
    public static final Vec3 OFFSET = new Vec3(0.5D, 2.5D, 0.5D);

    public BindFamiliarRite(BaseRiteParams baseParams, RiteParams params) {
        super(baseParams, params);
    }

    @Override
    protected boolean onStart(RiteParams params) {
        Entity target = findEntity(params.target);
        if(target == null)
            return cancel();

        Vec3 newPos = new Vec3(OFFSET.x + pos.getX(), OFFSET.y + pos.getY(), OFFSET.z + pos.getZ());

        target.setNoGravity(true);

        level.playSound(null, target.getX(), target.getY(),  target.getZ(), SoundEvents.ENDERMAN_TELEPORT, SoundSource.NEUTRAL, 1.0F, 1.0F);

        double offset = target.getBbWidth() / 1.5D;
        level.sendParticles(ParticleTypes.PORTAL, target.getX(), target.getY(),  target.getZ(), 20 + Enchanted.RANDOM.nextInt(10), offset, offset, offset, 0.0D);
        target.teleportTo(newPos.x, newPos.y, newPos.z);
        level.sendParticles(ParticleTypes.PORTAL, target.getX(), target.getY(),  target.getZ(), 20 + Enchanted.RANDOM.nextInt(10), offset, offset, offset, 0.0D);

        level.sendParticles(EParticleTypes.BIND_FAMILIAR_SEED.get(), newPos.x, newPos.y, newPos.z, 1, 0, 0, 0, 0);

        return true;
    }

    @Override
    protected boolean onTick(RiteParams params) {
        Entity target = findEntity(params.target);
        if(target == null)
            return false;

        if(params.ticks() == START_SOUND)
            level.playSound(null, target.getX(),  target.getY(),  target.getZ(), ESoundEvents.BIND_FAMILIAR.value(), SoundSource.MASTER, 1.5F, 1.0F);

        if(params.ticks() < BIND_TICKS) {
            double dx = (pos.getX() + OFFSET.x + Math.random() * 0.2D) - 0.1D;
            double dy = ((pos.getY() + OFFSET.y + Math.random() * 0.2D) - 0.1D) - target.getBbHeight() / 2.0D;
            double dz = (pos.getZ() + OFFSET.z + Math.random() * 0.2D) - 0.1D;
            target.teleportTo(dx, dy, dz);
        }
        else {
            FamiliarType<?, ?> type = FamiliarTypes.getByInput(target.getType());
            if(type == null)
                return false;

            TamableAnimal familiar = type.getFor(level);
            familiar.setPos(target.getX(), target.getY(), target.getZ());
            familiar.setOwnerUUID(params.caster);
            familiar.setTame(true, true);
            familiar.setCustomName(target.getCustomName());
            familiar.setPersistenceRequired();

            FamiliarSavedData data = FamiliarSavedData.get(level);
            data.setFamiliar(params.caster, type, familiar);

            Enchanted.LOG.info("Familiar of type {} bound to {}", type.getId().toString(), params.caster);
            level.addFreshEntity(familiar);
            target.discard();

            return false;
        }

        return true;
    }

    @Override
    protected UUID findTargetUUID(ServerLevel level, BlockPos pos, RiteParams params) {
        List<TamableAnimal> potentials = level.getEntitiesOfClass(TamableAnimal.class, type.getBounds(pos), e -> FamiliarTypes.getByInput(e.getType()) != null);
        for(TamableAnimal animal : potentials) {
            if(animal.getOwnerUUID() != null && animal.getOwnerUUID().equals(params.caster))
                return animal.getUUID();
        }
        return null;
    }

}
