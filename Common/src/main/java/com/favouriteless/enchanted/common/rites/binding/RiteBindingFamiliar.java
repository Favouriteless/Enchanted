package com.favouriteless.enchanted.common.rites.binding;

import com.favouriteless.enchanted.Enchanted;
import com.favouriteless.enchanted.api.familiars.FamiliarSavedData;
import com.favouriteless.enchanted.api.familiars.FamiliarType;
import com.favouriteless.enchanted.api.rites.AbstractRite;
import com.favouriteless.enchanted.common.init.registry.*;
import com.favouriteless.enchanted.common.rites.CirclePart;
import com.favouriteless.enchanted.common.rites.RiteType;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.UUID;

public class RiteBindingFamiliar extends AbstractRite {

	public static final int BIND_TICKS = 300;
	public static final int START_SOUND = 175;
	public static final Vec3 OFFSET = new Vec3(0.5D, 2.5D, 0.5D);

	public RiteBindingFamiliar(RiteType<?> type, ServerLevel level, BlockPos pos, UUID caster) {
		super(type, level, pos, caster, 8000, 0);
		CIRCLES_REQUIRED.put(CirclePart.MEDIUM, EnchantedBlocks.CHALK_WHITE.get());
		ITEMS_REQUIRED.put(EnchantedItems.TEAR_OF_THE_GODDESS.get(), 1);
		ITEMS_REQUIRED.put(EnchantedItems.ODOUR_OF_PURITY.get(), 1);
		ITEMS_REQUIRED.put(EnchantedItems.WHIFF_OF_MAGIC.get(), 1);
		ITEMS_REQUIRED.put(Items.DIAMOND, 1);
		ITEMS_REQUIRED.put(EnchantedItems.DEMONIC_BLOOD.get(), 1);
	}

	@Override
	protected void execute() {
		ServerLevel level = getLevel();
		BlockPos pos = getPos();
		Entity targetEntity = getTargetEntity();
		if(targetEntity != null) {
			Vec3 newPos = new Vec3(OFFSET.x + pos.getX(), OFFSET.y + pos.getY(), OFFSET.z + pos.getZ());
			targetEntity.setNoGravity(true);

			level.playSound(null, targetEntity.blockPosition(), SoundEvents.ENDERMAN_TELEPORT, SoundSource.NEUTRAL, 1.0F, 1.0F);
			double offset = targetEntity.getBbWidth() / 1.5D;

			level.sendParticles(ParticleTypes.PORTAL, targetEntity.getX(), targetEntity.getY(),  targetEntity.getZ(), 20 + Enchanted.RANDOM.nextInt(10), offset, offset, offset, 0.0D);
			targetEntity.teleportTo(newPos.x, newPos.y, newPos.z);
			level.sendParticles(ParticleTypes.PORTAL, targetEntity.getX(), targetEntity.getY(),  targetEntity.getZ(), 20 + Enchanted.RANDOM.nextInt(10), offset, offset, offset, 0.0D);

			level.sendParticles(EnchantedParticles.BIND_FAMILIAR_SEED.get(), newPos.x, newPos.y, newPos.z, 1, 0.0D, 0.0D, 0.0D, 0.0D);
		}
	}

	@Override
	protected void onTick() {
		if(!stillValid()) {
			stopExecuting();
			return;
		}

		ServerLevel level = getLevel();
		BlockPos pos = getPos();
		Entity targetEntity = getTargetEntity();
		UUID casterUUID = getCasterUUID();
		if(ticks == START_SOUND)
			level.playSound(null, pos.getX() + OFFSET.x,  pos.getY() + OFFSET.y,  pos.getZ() + OFFSET.z, EnchantedSoundEvents.BIND_FAMILIAR.get(), SoundSource.MASTER, 1.5F, 1.0F);

		if(ticks < BIND_TICKS) {
			double dx = (pos.getX() + OFFSET.x + Enchanted.RANDOM.nextDouble() * 0.2D) - 0.1D;
			double dy = ((pos.getY() + OFFSET.y + Enchanted.RANDOM.nextDouble() * 0.2D) - 0.1D) - targetEntity.getBbHeight() / 2.0D;
			double dz = (pos.getZ() + OFFSET.z + Enchanted.RANDOM.nextDouble() * 0.2D) - 0.1D;
			targetEntity.teleportTo(dx, dy, dz);
		}
		else {
			FamiliarType<?, ?> type = FamiliarTypes.getByInput(targetEntity.getType());

			if(type != null) {
				TamableAnimal familiar = type.getFor(level);
				familiar.setPos(targetEntity.getX(), targetEntity.getY(), targetEntity.getZ());
				familiar.setOwnerUUID(casterUUID);
				familiar.setTame(true);
				familiar.setCustomName(targetEntity.getCustomName());
				familiar.setPersistenceRequired();

				FamiliarSavedData data = FamiliarSavedData.get(level);
				data.setFamiliar(casterUUID, type, familiar);
				data.setDirty();

				Enchanted.LOG.info(String.format("Familiar of type %s bound to %s", type.getId().toString(), casterUUID));
				level.addFreshEntity(familiar);
				targetEntity.discard();
			}
			stopExecuting();
		}
	}

	private boolean stillValid() {
		return getTargetEntity() != null && getTargetEntity().isAlive();
	}

	@Override
	protected boolean checkAdditional() {
		ServerLevel level = getLevel();
		BlockPos pos = getPos();
		UUID casterUUID = getCasterUUID();

		List<Entity> entities = CirclePart.MEDIUM.getEntitiesInside(level, pos, entity -> FamiliarTypes.getByInput(entity.getType()) != null);

		if(!entities.isEmpty()) {
			if(entities.get(0) instanceof TamableAnimal animal) {
				UUID ownerUUID = animal.getOwnerUUID();

				if(ownerUUID != null && !ownerUUID.equals(getCasterUUID())) {
					Player caster = level.getPlayerByUUID(casterUUID);
					if(caster != null)
						caster.displayClientMessage(Component.literal("This creature does not trust you.").withStyle(ChatFormatting.RED), false);
					return false;
				}
			}
			setTargetEntity(entities.get(0));
			return true;
		}
		return false;
	}

}
