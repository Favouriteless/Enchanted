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

package com.favouriteless.enchanted.common.network.packets;

import com.favouriteless.enchanted.client.particles.TwoToneColouredParticleType.TwoToneColouredData;
import com.favouriteless.enchanted.client.render.poppet.PoppetAnimationManager;
import com.favouriteless.enchanted.common.init.EnchantedParticles;
import com.favouriteless.enchanted.common.init.PoppetColour;
import com.favouriteless.enchanted.common.items.poppets.AbstractPoppetItem;
import com.favouriteless.enchanted.common.items.poppets.PoppetHelper.PoppetResult;
import com.favouriteless.enchanted.common.network.EnchantedPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent.Context;

import java.util.function.Supplier;

public class EnchantedPoppetAnimationPacket implements EnchantedPacket {

	private final PoppetResult result;
	private final ItemStack item;
	private final int entityId;

	public EnchantedPoppetAnimationPacket(PoppetResult result, ItemStack itemStack, int entityId) {
		this.result = result;
		this.item = itemStack;
		this.entityId = entityId;
	}

	@Override
	public void encode(PacketBuffer buffer) {
		buffer.writeEnum(result);
		buffer.writeItem(item);
		buffer.writeInt(entityId);
	}

	public static EnchantedPoppetAnimationPacket decode(PacketBuffer buffer) {
		return new EnchantedPoppetAnimationPacket(buffer.readEnum(PoppetResult.class), buffer.readItem(), buffer.readInt());
	}

	@Override
	public void handle(Supplier<Context> context) {
		Minecraft mc = Minecraft.getInstance();

		Entity entity = mc.level.getEntity(entityId);
		if(mc.level.isClientSide) {
			if(item.getItem() instanceof AbstractPoppetItem) {
				PoppetColour poppetColour = ((AbstractPoppetItem)item.getItem()).colour;
				mc.particleEngine.createTrackingEmitter(entity, new TwoToneColouredData(EnchantedParticles.POPPET.get(),
						poppetColour.rPrimary, poppetColour.gPrimary, poppetColour.gSecondary,
						poppetColour.rSecondary, poppetColour.gSecondary, poppetColour.bSecondary), 40);

				if(entity == mc.player) {
					PoppetAnimationManager.startAnimation(result, item);
				}
			}
		}
		context.get().setPacketHandled(true);
	}

}
