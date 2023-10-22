package com.favouriteless.enchanted.common.familiars;

import com.favouriteless.enchanted.common.entities.FamiliarCat;
import com.favouriteless.enchanted.api.familiars.FamiliarType;
import com.favouriteless.enchanted.common.init.registry.EnchantedEntityTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Cat;

public class CatFamiliarType extends FamiliarType<Cat, FamiliarCat> {

	public CatFamiliarType(ResourceLocation id) {
		super(id, () -> EntityType.CAT, EnchantedEntityTypes.FAMILIAR_CAT);
	}

	@Override
	public FamiliarCat create(Cat from) {
		FamiliarCat familiar = EnchantedEntityTypes.FAMILIAR_CAT.get().create(from.getLevel());
		familiar.setCatVariant(from.getCatVariant());
		familiar.setCollarColor(from.getCollarColor());
		return familiar;
	}

}
