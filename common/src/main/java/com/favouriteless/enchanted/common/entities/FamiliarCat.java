package com.favouriteless.enchanted.common.entities;

import com.favouriteless.enchanted.Enchanted;
import com.favouriteless.enchanted.api.familiars.FamiliarHelper;
import com.favouriteless.enchanted.api.familiars.IFamiliarCapability.IFamiliarEntry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class FamiliarCat extends Cat {

	public FamiliarCat(EntityType<? extends Cat> type, Level level) {
		super(type, level);
	}

	@Override
	public InteractionResult interact(Player player, InteractionHand hand) {
		if(player.isCrouching() && hand == InteractionHand.MAIN_HAND) {
			if(!level.isClientSide) {
				if(player.getUUID().equals(getOwnerUUID())) {
					FamiliarHelper.dismiss(this);
					return InteractionResult.sidedSuccess(level.isClientSide);
				}
			}
		}
		return super.interact(player, hand);
	}

	@Override
	public void tick() {
		super.tick();
		if(!level.isClientSide) {
			FamiliarHelper.runIfCap(level, cap -> {
				IFamiliarEntry entry = cap.getFamiliarFor(getOwnerUUID());
				if(entry == null || !getUUID().equals(entry.getUUID())) {
					discard();
					Enchanted.LOG.info(String.format("Found familiar with non-matching UUID for %s, discarding.", getOwnerUUID()));
				}
			});
		}
	}

	@Override
	public void addAdditionalSaveData(CompoundTag nbt) {
		super.addAdditionalSaveData(nbt);
		if(!level.isClientSide) {
			FamiliarHelper.runIfCap(level, cap -> {
				IFamiliarEntry entry = cap.getFamiliarFor(getOwnerUUID());
				if(entry != null)
					entry.setNbt(nbt);
			});
		}
	}

	@Override
	public void die(DamageSource cause) {
		super.die(cause);
		if(!level.isClientSide)
			FamiliarHelper.dismiss(this);
	}

	public static AttributeSupplier createCatAttributes() {
		return Monster.createMonsterAttributes()
				.add(Attributes.MAX_HEALTH, 50.0D)
				.add(Attributes.ARMOR, 5.0D)
				.add(Attributes.MOVEMENT_SPEED, 0.3F)
				.add(Attributes.ATTACK_DAMAGE, 3.0D)
				.build();
	}

	@Override
	public boolean isInvulnerableTo(DamageSource source) {
		return source == DamageSource.LAVA ||
				source == DamageSource.IN_FIRE ||
				source == DamageSource.ON_FIRE ||
				source == DamageSource.FALL ||
				source == DamageSource.DROWN;
	}

}

