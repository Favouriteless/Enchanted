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

package com.favouriteless.enchanted.client.particles;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.particle.SpriteTexturedParticle;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.*;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public abstract class ScreenSpaceTexturedParticle extends SpriteTexturedParticle {

	protected float quadSize = 0.1F * (this.random.nextFloat() * 0.5F + 0.5F) * 2.0F;

	protected ScreenSpaceTexturedParticle(ClientWorld world, double x, double y, double z) {
		super(world, x, y, z);
	}

	protected ScreenSpaceTexturedParticle(ClientWorld world, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
		super(world, x, y, z, xSpeed, ySpeed, zSpeed);
	}

	@Override
	public void render(IVertexBuilder buffer, ActiveRenderInfo renderInfo, float partialTicks) {
		Vector3d cameraPos = renderInfo.getPosition();
		float xPos = (float)(MathHelper.lerp(partialTicks, this.xo, this.x) - cameraPos.x());
		float yPos = (float)(MathHelper.lerp(partialTicks, this.yo, this.y) - cameraPos.y());
		float scale = this.getQuadSize(partialTicks);

		MatrixStack matrixStack = new MatrixStack();
		Quaternion rotation = new Quaternion(renderInfo.rotation());
		float lerpedRoll = MathHelper.lerp(partialTicks, this.oRoll, this.roll);
		rotation.mul(Vector3f.ZP.rotation(lerpedRoll));
		matrixStack.mulPose(rotation);
		matrixStack.scale(scale, scale, scale);
		matrixStack.translate(0.0D, 0.0D, 2.0D);

		MatrixStack.Entry entry = matrixStack.last();
		Matrix4f pose = entry.pose();
		Matrix3f entryNormal = entry.normal();

		Vector3f normal = new Vector3f(0f, 0f, -1f);
		normal.transform(entryNormal);

		Vector3f[] vertexPositions = new Vector3f[] { new Vector3f(-1.0F, -1.0F, 0.0F), new Vector3f(-1.0F, 1.0F, 0.0F), new Vector3f(1.0F, 1.0F, 0.0F), new Vector3f(1.0F, -1.0F, 0.0F) };
		Vector2f[] vertexUVs = new Vector2f[] { new Vector2f(getU1(), getV1()), new Vector2f(getU1(), getV0()), new Vector2f(getU0(), getV0()), new Vector2f(getU0(), getV1()) };

		for(int i = 0; i < 4; ++i) {
			Vector3f pos = vertexPositions[i];
			Vector2f uvs = vertexUVs[i];
			Vector4f vertexPos = new Vector4f(pos.x(), pos.y(), pos.z(), 1.0F);
			vertexPos.transform(pose);
			buffer.vertex(vertexPos.x(), vertexPos.y(), vertexPos.z()).uv(uvs.x, uvs.y).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(15728880).endVertex();
		}
	}

	@Override
	public boolean shouldCull() {
		return false;
	}
}
