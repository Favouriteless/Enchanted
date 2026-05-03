package net.favouriteless.enchanted.client.render.poppet;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.Util;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.Item;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class PoppetAnimationManager {

	private static final List<PoppetAnimation> ACTIVE_ANIMATIONS = new ArrayList<>();

	private static final Vector3f DIFFUSE_LIGHT_0 = Util.make(new Vector3f(0.2F, 0.24F, -0.7F), Vector3f::normalize);
	private static final Vector3f DIFFUSE_LIGHT_1 = Util.make(new Vector3f(-0.2F, 0.24F, 0.7F), Vector3f::normalize);

	public static void startAnimation(Item item) {
        startAnimation(new PoppetAnimation(item));
	}

	public static void startAnimation(PoppetAnimation animation) {
		ACTIVE_ANIMATIONS.add(animation);
	}

	public static void tick() {
		for(PoppetAnimation animation : ACTIVE_ANIMATIONS) {
			animation.tick();
		}
		ACTIVE_ANIMATIONS.removeIf((anim) -> anim.ticks <= 0);
	}

	public static void render(GuiGraphics graphics, float partialTicks, int widthScaled, int heightScaled) {
		RenderSystem.enableDepthTest();
		RenderSystem.disableCull();
		RenderSystem.setupGui3DDiffuseLighting(DIFFUSE_LIGHT_0, DIFFUSE_LIGHT_1);
		for(PoppetAnimation animation : ACTIVE_ANIMATIONS) {
			animation.render(graphics.pose(), partialTicks, widthScaled, heightScaled);
		}
		RenderSystem.enableCull();
		RenderSystem.disableDepthTest();
	}

}
