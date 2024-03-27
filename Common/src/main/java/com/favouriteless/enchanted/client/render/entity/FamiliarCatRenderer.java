package com.favouriteless.enchanted.client.render.entity;

import com.favouriteless.enchanted.client.ClientConfig;
import com.favouriteless.enchanted.common.entities.FamiliarCat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.minecraft.client.model.CatModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;

public class FamiliarCatRenderer extends MobRenderer<FamiliarCat, CatModel<FamiliarCat>> {

	public static final ResourceLocation TEXTURE_LOCATION = new ResourceLocation("textures/entity/cat/all_black.png");

	public FamiliarCatRenderer(EntityRendererProvider.Context context) {
		super(context, new CatModel<>(context.bakeLayer(ModelLayers.CAT)), 0.4F);
	}

	@Override
	public ResourceLocation getTextureLocation(FamiliarCat cat) {
		return ClientConfig.USE_ORIGINAL_CAT_TYPE.get() ? cat.getResourceLocation() : TEXTURE_LOCATION;
	}

	@Override
	protected void scale(FamiliarCat cat, PoseStack poseStack, float partialTicks) {
		super.scale(cat, poseStack, partialTicks);
		poseStack.scale(0.8F, 0.8F, 0.8F);
	}

	protected void setupRotations(FamiliarCat cat, PoseStack poseStack, float age, float yaw, float partialTicks) {
		super.setupRotations(cat, poseStack, age, yaw, partialTicks);
		float f = cat.getLieDownAmount(partialTicks);
		if (f > 0.0F) {
			poseStack.translate(0.4F * f, 0.15F * f, 0.1F * f);
			poseStack.mulPose(Vector3f.ZP.rotationDegrees(Mth.rotLerp(f, 0.0F, 90.0F)));
			BlockPos blockpos = cat.blockPosition();

			for(Player player : cat.level.getEntitiesOfClass(Player.class, (new AABB(blockpos)).inflate(2.0D, 2.0D, 2.0D))) {
				if (player.isSleeping()) {
					poseStack.translate(0.15F * f, 0.0D, 0.0D);
					break;
				}
			}
		}
	}

}
