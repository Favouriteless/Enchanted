package net.favouriteless.enchanted.api.familiars;

import net.favouriteless.enchanted.common.entities.FamiliarCat;
import net.favouriteless.enchanted.common.enchanted.familiars.CatFamiliarType;
import net.favouriteless.enchanted.common.enchanted.familiars.EFamiliarTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.animal.Cat;

import java.util.function.Supplier;

/**
 * A {@link FamiliarType} defines which {@link EntityType} creates which type of familiar, as well as defining logic for
 * creating a new familiar {@link Entity} given an input of an {@link Entity} of the specified type.
 * {@link FamiliarType}s should be registered using {@link EFamiliarTypes#register(ResourceLocation, FamiliarType)}.
 *
 * <p>See {@link CatFamiliarType} for an example implementation of a {@link FamiliarType}</p>
 *
 * @param <T> The {@link Entity} class of the input entity (e.g. {@link Cat}).
 * @param <C> The {@link Entity} class of the output entity (e.g. {@link FamiliarCat}).
 */
public abstract class FamiliarType<T extends TamableAnimal, C extends TamableAnimal> {

	private final ResourceLocation id;
	private final Supplier<EntityType<T>> typeIn;
	private final Supplier<EntityType<C>> typeOut;

	public FamiliarType(ResourceLocation id, Supplier<EntityType<T>> typeIn, Supplier<EntityType<C>> typeOut) {
		this.id = id;
		this.typeIn = typeIn;
		this.typeOut = typeOut;
	}

	/**
	 * @return The {@link ResourceLocation} ID this {@link FamiliarType} is registered under.
	 */
	public ResourceLocation getId() {
		return id;
	}

	/**
	 * @return The {@link EntityType} required for input entities of this {@link FamiliarType}.
	 */
	public EntityType<T> getTypeIn() {
		return typeIn.get();
	}

	/**
	 * @return The {@link EntityType} produced by this {@link FamiliarType}.
	 */
	public EntityType<C> getTypeOut() {
		return typeOut.get();
	}

	/**
	 * Creates a familiar {@link TamableAnimal} from the supplied {@link TamableAnimal}. Implementations do not need to
	 * change the owner, tamed state or name of the returned {@link TamableAnimal} as that is already handled by
	 * RiteBindingFamiliar
	 */
	public abstract C create(T from);

	public C getFor(ServerLevel level) {
		return create(getTypeIn().create(level));
	}

}
