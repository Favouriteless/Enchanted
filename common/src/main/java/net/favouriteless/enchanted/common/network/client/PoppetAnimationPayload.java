package net.favouriteless.enchanted.common.network.client;

import net.favouriteless.enchanted.client.particles.types.TwoColourOptions;
import net.favouriteless.enchanted.client.render.poppet.PoppetAnimationManager;
import net.favouriteless.enchanted.common.Enchanted;
import net.favouriteless.enchanted.common.init.EParticleTypes;
import net.favouriteless.enchanted.common.items.poppets.PoppetItem;
import net.favouriteless.enchanted.common.poppet.PoppetColour;
import net.favouriteless.enchanted.common.poppet.PoppetUseResult.ResultType;
import net.favouriteless.enchanted.platform.PacketContext;
import net.minecraft.client.Minecraft;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.util.FastColor;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;

public class PoppetAnimationPayload implements CustomPacketPayload {

	public static final Type<PoppetAnimationPayload> TYPE = new Type<>(Enchanted.id("poppet_animation"));

	public static final StreamCodec<RegistryFriendlyByteBuf, PoppetAnimationPayload> STREAM_CODEC = StreamCodec.composite(
			ResultType.STREAM_CODEC, p -> p.result,
			ItemStack.STREAM_CODEC, p -> p.item,
			ByteBufCodecs.INT, p -> p.entityId,
			PoppetAnimationPayload::new
	);

	private final ResultType result;
	private final ItemStack item;
	private final int entityId;

	public PoppetAnimationPayload(ResultType result, ItemStack itemStack, int entityId) {
		this.result = result;
		this.item = itemStack;
		this.entityId = entityId;
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}

	public static void handle(PoppetAnimationPayload payload, PacketContext context) {
		context.enqueueWork(() -> {
			Minecraft mc = Minecraft.getInstance();
			Entity entity = mc.level.getEntity(payload.entityId);
			if(entity != null) {
				if(payload.item.getItem() instanceof PoppetItem) {
					PoppetColour colour = ((PoppetItem)payload.item.getItem()).colour;
					mc.particleEngine.createTrackingEmitter(entity, new TwoColourOptions(EParticleTypes.POPPET.get(),
							FastColor.ARGB32.color(colour.rPrimary, colour.gPrimary, colour.gSecondary),
							FastColor.ARGB32.color(colour.rSecondary, colour.gSecondary, colour.bSecondary)), 40);

					if(entity == mc.player)
						PoppetAnimationManager.startAnimation(payload.result, payload.item);
				}
			}
		});
	}

}
