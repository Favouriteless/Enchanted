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

package com.favouriteless.enchanted.client.render.tileentity;

import com.favouriteless.enchanted.common.blocks.SpinningWheelBlock;
import com.favouriteless.enchanted.common.tileentity.SpinningWheelTileEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.resources.ResourceLocation;
import com.mojang.math.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class SpinningWheelRenderer extends BlockEntityRenderer<SpinningWheelTileEntity> {

    public static final ResourceLocation TEXTURE = new ResourceLocation("enchanted:textures/block/spinning_wheel.png");

    private final ModelPart wheel;
    private final ModelPart body;
    private final ModelPart arm;

    public SpinningWheelRenderer(BlockEntityRenderDispatcher dispatcher) {
        super(dispatcher);

        wheel = new ModelPart(32, 32, 0, 0);
        wheel.setPos(-5.1F, 9.8F, 0.0F);
        wheel.texOffs(0, 14).addBox(-2.0F, 2.0F, -0.5F, 5.0F, 1.0F, 1.0F, 0.0F, false);
        wheel.texOffs(0, 6).addBox(-3.0F, -2.0F, -0.5F, 1.0F, 5.0F, 1.0F, 0.0F, false);
        wheel.texOffs(0, 12).addBox(-3.0F, -3.0F, -0.5F, 5.0F, 1.0F, 1.0F, 0.0F, false);
        wheel.texOffs(4, 6).addBox(2.0F, -3.0F, -0.5F, 1.0F, 5.0F, 1.0F, 0.0F, false);
        wheel.texOffs(16, 28).addBox(0.0F, -2.0F, 0.0F, 1.0F, 2.0F, 0.0F, 0.0F, false);
        wheel.texOffs(18, 28).addBox(-1.0F, 0.0F, 0.0F, 1.0F, 2.0F, 0.0F, 0.0F, false);
        wheel.texOffs(16, 31).addBox(-2.0F, -1.0F, 0.0F, 2.0F, 1.0F, 0.0F, 0.0F, false);
        wheel.texOffs(16, 30).addBox(0.0F, 0.0F, 0.0F, 2.0F, 1.0F, 0.0F, 0.0F, false);

        body = new ModelPart(32, 32, 0, 0);
        body.setPos(-0.396F, 5.2851F, 0.0F);
        body.zRot = (float)Math.toRadians(180);

        ModelPart base2 = new ModelPart(32, 32, 0, 0);
        base2.setPos(1.4579F, -0.0037F, 0.0F);
        body.addChild(base2);
        base2.zRot = 0.2618F;
        base2.texOffs(0, 0).addBox(-1.5F, -0.5F, -2.5F, 4.0F, 1.0F, 5.0F, 0.0F, false);

        ModelPart base1 = new ModelPart(32, 32, 0, 0);
        base1.setPos(-3.3718F, -1.2978F, 0.0F);
        body.addChild(base1);
        base1.zRot = 0.2618F;
        base1.texOffs(0, 6).addBox(-2.5F, -0.5F, -1.5F, 6.0F, 1.0F, 3.0F, 0.0F, false);

        ModelPart legS = new ModelPart(32, 32, 0, 0);
        legS.setPos(2.9606F, 2.9883F, 1.5F);
        body.addChild(legS);
        legS.zRot = -0.1745F;
        legS.texOffs(28, 26).addBox(-0.5F, -2.5F, -0.5F, 1.0F, 5.0F, 1.0F, 0.0F, false);
        legS.texOffs(24, 26).addBox(-0.5F, -2.5F, -3.5F, 1.0F, 5.0F, 1.0F, 0.0F, false);

        ModelPart legE = new ModelPart(32, 32, 0, 0);
        legE.setPos(-4.9391F, 1.8151F, 0.0F);
        body.addChild(legE);
        legE.zRot = 0.1309F;
        legE.texOffs(20, 16).addBox(-0.5F, -3.5F, -0.5F, 1.0F, 7.0F, 1.0F, 0.0F, false);

        ModelPart armS = new ModelPart(32, 32, 0, 0);
        armS.setPos(2.354F, -0.7149F, -1.0F);
        body.addChild(armS);
        armS.zRot = 0.5672F;
        armS.texOffs(24, 16).addBox(-0.5F, -4.8F, -0.5F, 1.0F, 6.0F, 1.0F, 0.0F, false);
        armS.texOffs(28, 16).addBox(-0.5F, -4.8F, 1.5F, 1.0F, 6.0F, 1.0F, 0.0F, false);
        armS.texOffs(0, 0).addBox(-0.5F, -4.8F, 0.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);

        arm = new ModelPart(32, 32, 0, 0);
        arm.setPos(3.8123F, 7.3247F, 0.0F);
        arm.zRot = (float)Math.toRadians(180);
        arm.texOffs(20, 28).addBox(-0.5F, -2.75F, -0.5F, 1.0F, 3.0F, 1.0F, 0.0F, false);
        arm.texOffs(0, 27).addBox(-1.0F, -4.5F, -1.0F, 2.0F, 3.0F, 2.0F, 0.0F, false);
    }

    @Override
    public void render(SpinningWheelTileEntity te, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
        matrixStack.pushPose();
        float rotationYDegrees = (te.getLevel() != null ? te.getBlockState().getValue(SpinningWheelBlock.FACING).getOpposite().toYRot() : 0);

        matrixStack.translate(0.5F, 0.0F, 0.5F);
        matrixStack.mulPose(Vector3f.YP.rotationDegrees(rotationYDegrees));

        ContainerData furnaceData = te.getData();
        float cookTime = furnaceData.get(2) >= 1 ? furnaceData.get(2) + partialTicks - 1 : 0;
        float turnFactor = 25;
        float rotationDegreesWheel = cookTime % turnFactor * 360 / turnFactor;
        float rotationDegreesArm = rotationDegreesWheel * 2;

        VertexConsumer vertexBuilder = buffer.getBuffer((RenderType.entityTranslucentCull(TEXTURE)));
        body.render(matrixStack, vertexBuilder, combinedLight, combinedOverlay);

        matrixStack.pushPose();
        wheel.zRot = (float)(Math.PI + Math.toRadians(rotationDegreesWheel));
        wheel.render(matrixStack, vertexBuilder, combinedLight, combinedOverlay);
        matrixStack.popPose();

        matrixStack.pushPose();
        arm.yRot = (float)(Math.PI + Math.toRadians(rotationDegreesArm));
        arm.render(matrixStack, vertexBuilder, combinedLight, combinedOverlay);
        matrixStack.popPose();

        matrixStack.popPose();
    }

}
