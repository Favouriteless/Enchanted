/*
 * Copyright (c) 2022. Favouriteless
 * Enchanted, a minecraft mod.
 * GNU GPLv3 License
 *
 *     This file is part of Enchanted.
 *
 *     Enchanted is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Enchanted is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Enchanted.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.favouriteless.enchanted.common.items;

import com.favouriteless.enchanted.Enchanted;
import com.favouriteless.enchanted.client.render.entity.armor.EarmuffsModel;
import com.favouriteless.enchanted.common.init.EnchantedItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.AbstractSoundInstance;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.sound.PlaySoundEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

import javax.annotation.Nullable;

import net.minecraft.world.item.Item.Properties;

@EventBusSubscriber(modid=Enchanted.MOD_ID, bus=Bus.FORGE, value=Dist.CLIENT)
public class EarmuffsItem extends ArmorItem {

	public EarmuffsItem(Properties pProperties) {
		super(ArmorMaterials.LEATHER, EquipmentSlot.HEAD, pProperties);
	}

	@SubscribeEvent
	@OnlyIn(Dist.CLIENT)
	public static void playSound(PlaySoundEvent event) {
		Minecraft mc = Minecraft.getInstance();
		Player player = mc.player;

		if(player != null) {
			if(player.getItemBySlot(EquipmentSlot.HEAD).getItem() == EnchantedItems.EARMUFFS.get()) {
				AbstractSoundInstance sound = (AbstractSoundInstance)event.getResultSound();
				sound.volume *= 0.03F;
				event.setResultSound(sound);
			}
		}
	}

	@Nullable
	@Override
	@OnlyIn(Dist.CLIENT)
	public <A extends HumanoidModel<?>> A getArmorModel(LivingEntity entityLiving, ItemStack itemStack, EquipmentSlot armorSlot, A _default) {
		return (A)new EarmuffsModel();
	}

	@Nullable
	@Override
	@OnlyIn(Dist.CLIENT)
	public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
		return EarmuffsModel.TEXTURE.toString();
	}
}
