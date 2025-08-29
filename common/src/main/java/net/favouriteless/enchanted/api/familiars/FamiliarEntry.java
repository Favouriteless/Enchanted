package net.favouriteless.enchanted.api.familiars;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.TamableAnimal;

import java.util.UUID;

public interface FamiliarEntry {

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