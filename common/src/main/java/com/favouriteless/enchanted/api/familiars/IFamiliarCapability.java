package com.favouriteless.enchanted.api.familiars;

import com.favouriteless.enchanted.Enchanted;
import com.favouriteless.enchanted.api.ISerializable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.level.Level;

import java.util.UUID;

/**
 * {@link IFamiliarCapability} is a {@link Level} capability used to store the familiar data for every player on a
 * server.
 *
 * <p>{@link IFamiliarCapability} is only ever present on the OVERWORLD {@link ServerLevel}, see
 * {@link FamiliarHelper#runIfCap(Level, NonNullConsumer)} for an easy method to grab the cap and run a consumer on it
 * via specifying any {@link Level}.</p>
 */
public interface IFamiliarCapability extends ISerializable<CompoundTag> {

	ResourceLocation LOCATION = Enchanted.location("familiars");

	/**
	 * Grab familiar entry owned by the player with the specified UUID.
	 * @param owner The {@link UUID} of the player who owns the familiar.
	 * @return The {@link FamiliarEntry} under the UUID of owner, or null if there is not one present.
	 */
	IFamiliarEntry getFamiliarFor(UUID owner);

	/**
	 * Creates a {@link FamiliarEntry} from the supplied information and stores it under the {@link UUID} of owner.
	 * @param owner The {@link UUID} of the owner of the familiar.
	 * @param type The {@link FamiliarType} of the familiar.
	 * @param familiar The familiar {@link Entity}.
	 */
	void setFamiliar(UUID owner, FamiliarType<?, ?> type, TamableAnimal familiar);



	/**
	 * Represents the most recent familiar created for a player, "old" entities should discard themselves on load if they
	 * do not match this description.
	 */
	interface IFamiliarEntry {

		/**
		 * @return The {@link FamiliarType} of the familiar stored in this entry.
		 */
		FamiliarType<?, ?> getType();

		/**
		 * @return The {@link UUID} belonging to the familiar {@link TamableAnimal} this entry was created from. Familiars
		 * not matching this should discard themselves when they load in.
		 */
		UUID getUUID();

		/**
		 * Sets {@link UUID} stored in this entry.
		 *
		 * @param uuid The {@link UUID} of the most recently bound or summoned familiar for this entry.
		 */
		void setUUID(UUID uuid);

		/**
		 * @return The {@link CompoundTag} containing the saved familiar's data, {@link UUID} will have been stripped
		 * from the returned tag.
		 */
		CompoundTag getNbt();

		/**
		 * Sets the {@link CompoundTag} this entry is storing for the familiar's data.
		 *
		 * <p>Implementations of this should strip {@link UUID} from the provided {@link CompoundTag}.</p>
		 */
		void setNbt(CompoundTag nbt);

		/**
		 * Dismissed familiars should not provide their passive bonuses, when a familiar is manually dismissed or dies
		 * this should be true.
		 */
		boolean isDismissed();

		/**
		 * Set the dismissed state of a familiar.
		 * @param value If the familiar should be dismissed or not.
		 */
		void setDismissed(boolean value);

	}

}
