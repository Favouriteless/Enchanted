package net.favouriteless.enchanted.common.entities;

import net.favouriteless.enchanted.api.familiars.FamiliarEntry;
import net.favouriteless.enchanted.api.familiars.FamiliarHelper;
import net.favouriteless.enchanted.common.enchanted.familiars.FamiliarSavedData;
import net.favouriteless.enchanted.common.Enchanted;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.tags.DamageTypeTags;
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
	public InteractionResult mobInteract(Player player, InteractionHand hand) {
		if(player.isCrouching() && hand == InteractionHand.MAIN_HAND && player.getUUID().equals(getOwnerUUID())) {
            if(!level().isClientSide)
                FamiliarHelper.get().dismiss(this);

            return InteractionResult.sidedSuccess(level().isClientSide);
		}
		return super.mobInteract(player, hand);
	}

	@Override
	public void tick() {
		super.tick();
        if(level().isClientSide)
            return;

        FamiliarEntry entry = FamiliarSavedData.get(level()).getEntry(getOwnerUUID());
        if(entry == null || !getUUID().equals(entry.getUUID())) {
            discard();
            Enchanted.LOG.info(String.format("Found familiar with non-matching UUID for %s, discarding.", getOwnerUUID()));
        }
	}

	@Override
	public void addAdditionalSaveData(CompoundTag nbt) {
		super.addAdditionalSaveData(nbt);
		if(!level().isClientSide) {
			FamiliarSavedData data = FamiliarSavedData.get(level());
			FamiliarEntry entry = data.getEntry(getOwnerUUID());
			if(entry != null) {
				entry.setNbt(nbt);
				data.setDirty();
			}
		}
	}

	@Override
	public void die(DamageSource cause) {
		super.die(cause);
		if(!level().isClientSide)
			FamiliarHelper.get().dismiss(this);
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
		return source.is(DamageTypeTags.IS_FIRE) ||
				source.is(DamageTypeTags.IS_FALL) ||
				source.is(DamageTypeTags.IS_DROWNING) ||
				super.isInvulnerableTo(source);
	}

}

