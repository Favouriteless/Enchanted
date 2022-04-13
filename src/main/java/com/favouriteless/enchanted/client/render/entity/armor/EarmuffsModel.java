package com.favouriteless.enchanted.client.render.entity.armor;

import com.favouriteless.enchanted.Enchanted;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class EarmuffsModel extends BipedModel<LivingEntity> {

	public static final ResourceLocation TEXTURE = new ResourceLocation(Enchanted.MOD_ID, "textures/models/armor/custom/earmuffs.png");

	public EarmuffsModel() {
		super(0.5f);
		texWidth = 32;
		texHeight = 16;

		head = new ModelRenderer(this);
		head.setPos(0.0F, 24.0F, 0.0F);
		head.texOffs(18, 2).addBox(4.0F, -6.0F, -2.0F, 1.0F, 4.0F, 4.0F, 0.0F, false);
		head.texOffs(0, 2).addBox(4.5F, -5.5F, -1.5F, 1.0F, 3.0F, 3.0F, 0.0F, false);
		head.texOffs(18, 2).addBox(-5.0F, -6.0F, -2.0F, 1.0F, 4.0F, 4.0F, 0.0F, false);
		head.texOffs(0, 2).addBox(-5.5F, -5.5F, -1.5F, 1.0F, 3.0F, 3.0F, 0.0F, false);
		head.texOffs(0, 13).addBox(-4.5F, -8.5F, -1.0F, 9.0F, 1.0F, 2.0F, 0.0F, false);
		head.texOffs(4, 9).addBox(3.5F, -7.5F, -1.0F, 1.0F, 2.0F, 2.0F, 0.0F, false);
		head.texOffs(10, 9).addBox(-4.5F, -7.5F, -1.0F, 1.0F, 2.0F, 2.0F, 0.0F, false);
	}



	@Override
	public void renderToBuffer(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		head.render(matrixStack, buffer, packedLight, packedOverlay);
	}

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.xRot = x;
		modelRenderer.yRot = y;
		modelRenderer.zRot = z;
	}
}