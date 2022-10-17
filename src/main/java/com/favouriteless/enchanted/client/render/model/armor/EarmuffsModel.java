/*
 *
 *   Copyright (c) 2022. Favouriteless
 *   Enchanted, a minecraft mod.
 *   GNU GPLv3 License
 *
 *       This file is part of Enchanted.
 *
 *       Enchanted is free software: you can redistribute it and/or modify
 *       it under the terms of the GNU General Public License as published by
 *       the Free Software Foundation, either version 3 of the License, or
 *       (at your option) any later version.
 *
 *       Enchanted is distributed in the hope that it will be useful,
 *       but WITHOUT ANY WARRANTY; without even the implied warranty of
 *       MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *       GNU General Public License for more details.
 *
 *       You should have received a copy of the GNU General Public License
 *       along with Enchanted.  If not, see <https://www.gnu.org/licenses/>.
 *
 *
 */

package com.favouriteless.enchanted.client.render.model.armor;

import com.favouriteless.enchanted.Enchanted;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.LayerDefinitions;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class EarmuffsModel extends HumanoidModel<LivingEntity> {

	public static final ResourceLocation TEXTURE = new ResourceLocation(Enchanted.MOD_ID, "textures/models/armor/custom/earmuffs.png");

	public EarmuffsModel(ModelPart root) {
		super(root);
	}

	public static LayerDefinition createLayerDefinition() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		partdefinition.addOrReplaceChild("head",
				CubeListBuilder.create()
						.texOffs(18, 2).addBox(4.0F, -6.0F, -2.0F, 1.0F, 4.0F, 4.0F)
						.texOffs(0, 2).addBox(4.5F, -5.5F, -1.5F, 1.0F, 3.0F, 3.0F)
						.texOffs(18, 2).addBox(-5.0F, -6.0F, -2.0F, 1.0F, 4.0F, 4.0F)
						.texOffs(0, 2).addBox(-5.5F, -5.5F, -1.5F, 1.0F, 3.0F, 3.0F)
						.texOffs(0, 13).addBox(-4.5F, -8.5F, -1.0F, 9.0F, 1.0F, 2.0F)
						.texOffs(4, 9).addBox(3.5F, -7.5F, -1.0F, 1.0F, 2.0F, 2.0F)
						.texOffs(10, 9).addBox(-4.5F, -7.5F, -1.0F, 1.0F, 2.0F, 2.0F),
				PartPose.offset(0.0F, 24.0F, 0.0F));


		return LayerDefinition.create(meshdefinition, 32, 16);
	}



	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		head.render(poseStack, buffer, packedLight, packedOverlay);
	}

}