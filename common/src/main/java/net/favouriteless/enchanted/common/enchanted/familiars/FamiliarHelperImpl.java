package net.favouriteless.enchanted.common.enchanted.familiars;

import net.favouriteless.enchanted.api.familiars.FamiliarEntry;
import net.favouriteless.enchanted.api.familiars.FamiliarHelper;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.TamableAnimal;

public class FamiliarHelperImpl implements FamiliarHelper {

    public static final FamiliarHelperImpl INSTANCE = new FamiliarHelperImpl();

    private FamiliarHelperImpl() {}

	@Override
    public void dismiss(TamableAnimal entity) {
		FamiliarSavedData data = FamiliarSavedData.get(entity.level());

		FamiliarEntry entry = data.getEntry(entity.getOwnerUUID());

		entry.setDismissed(true);
		entry.setNbt(entity.saveWithoutId(new CompoundTag()));
		data.setDirty();

		double width = entity.getBbWidth();
		double height = entity.getBbHeight();
		((ServerLevel)entity.level()).sendParticles(ParticleTypes.PORTAL, entity.getX(), entity.getY(), entity.getZ(), 30, width, height, width, 0.0D);

		entity.level().playSound(null, entity.blockPosition(), SoundEvents.ENDERMAN_TELEPORT, SoundSource.NEUTRAL, 1.0F, 1.0F);
		entity.discard();
	}

}
