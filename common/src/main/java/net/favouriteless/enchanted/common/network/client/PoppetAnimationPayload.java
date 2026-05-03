package net.favouriteless.enchanted.common.network.client;

import net.favouriteless.enchanted.client.ClientProxy;
import net.favouriteless.enchanted.common.Enchanted;
import net.favouriteless.enchanted.platform.PacketContext;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public record PoppetAnimationPayload(int entity, Item poppet) implements CustomPacketPayload {

	public static final Type<PoppetAnimationPayload> TYPE = new Type<>(Enchanted.id("poppet_animation"));

	public static final StreamCodec<RegistryFriendlyByteBuf, PoppetAnimationPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, p -> p.entity,
            ByteBufCodecs.registry(Registries.ITEM), p -> p.poppet,
			PoppetAnimationPayload::new
	);

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}

	public static void handle(PoppetAnimationPayload payload, PacketContext context) {
		context.enqueueWork(() -> ClientProxy.playPoppetAnimation(payload.entity, payload.poppet));
	}

}
