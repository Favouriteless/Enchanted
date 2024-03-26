package com.favouriteless.enchanted.common.items;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorMaterials;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.item.GeoArmorItem;
import software.bernie.geckolib3.util.GeckoLibUtil;

public class EarmuffsItem extends GeoArmorItem implements IAnimatable {

	private final AnimationFactory factory = GeckoLibUtil.createFactory(this);

	public EarmuffsItem(Properties properties) {
		super(ArmorMaterials.LEATHER, EquipmentSlot.HEAD, properties);
	}


	@Override
	public void registerControllers(AnimationData data) {

	}

	@Override
	public AnimationFactory getFactory() {
		return factory;
	}

}
