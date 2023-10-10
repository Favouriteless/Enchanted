package com.favouriteless.enchanted.common.items;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;


public class EarmuffsItem extends ArmorItem {

	public EarmuffsItem(Properties properties) {
		super(ArmorMaterials.LEATHER, EquipmentSlot.HEAD, properties);
	}

	@Override
	public void initializeClient(Consumer<IItemRenderProperties> consumer) {
		consumer.accept(new IItemRenderProperties() {
			@Nullable
			@Override
			public HumanoidModel<?> getArmorModel(LivingEntity entityLiving, ItemStack itemStack, EquipmentSlot armorSlot, HumanoidModel<?> _default) {
				return new EarmuffsModel(Minecraft.getInstance().getEntityModels().bakeLayer(ModelLayerLocations.EARMUFFS));
			}
		});
	}

	@Nullable
	@Override
	public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
		return EarmuffsModel.TEXTURE.toString();
	}

}
