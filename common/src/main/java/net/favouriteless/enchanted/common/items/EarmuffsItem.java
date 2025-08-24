package net.favouriteless.enchanted.common.items;

import net.favouriteless.enchanted.client.render.armor.DefaultedGeoArmorRenderer;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.client.GeoRenderProvider;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager.ControllerRegistrar;
import software.bernie.geckolib.renderer.GeoArmorRenderer;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.function.Consumer;

public class EarmuffsItem extends ArmorItem implements GeoItem {

	private final AnimatableInstanceCache animationCache = GeckoLibUtil.createInstanceCache(this);

	public EarmuffsItem(Holder<ArmorMaterial> material, ArmorItem.Type type, Properties properties) {
		super(material, type, properties);
	}

	@Override
	public AnimatableInstanceCache getAnimatableInstanceCache() {
		return animationCache;
	}

	@Override
	public void registerControllers(ControllerRegistrar controllers) {}

	@Override
	public void createGeoRenderer(Consumer<GeoRenderProvider> consumer) {
		consumer.accept(new GeoRenderProvider() {
			private GeoArmorRenderer<?> renderer;

			@Override
			public <T extends LivingEntity> HumanoidModel<?> getGeoArmorRenderer(T livingEntity, ItemStack itemStack,
																				 EquipmentSlot equipmentSlot,
																				 HumanoidModel<T> original) {
				if(renderer == null)
					renderer = new DefaultedGeoArmorRenderer("earmuffs");

				return renderer;
			}
		});	}
}
