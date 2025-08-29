package net.favouriteless.enchanted.common.items.poppets;

import net.favouriteless.enchanted.common.items.TaglockFilledItem;
import net.favouriteless.enchanted.common.items.component.EDataComponents;
import net.favouriteless.enchanted.common.enchanted.poppet.PoppetColour;
import net.favouriteless.enchanted.common.enchanted.poppet.PoppetUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.UUID;

public class PoppetItem extends Item {

    public final PoppetColour colour;

	public PoppetItem(PoppetColour colour, Properties properties) {
		super(properties);
        this.colour = colour;
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> toolTip, TooltipFlag flag) {
		if(PoppetUtils.isBound(stack))
			toolTip.add(Component.literal(PoppetUtils.getBoundName(stack)).withStyle(ChatFormatting.GRAY));
	}

	@Override
	public ItemStack finishUsingItem(ItemStack itemStack, Level level, LivingEntity entity) {
		if(entity instanceof Player player) {
			ItemStack taglock = player.getOffhandItem();

			if(taglock.getItem() instanceof TaglockFilledItem) {
				if(taglock.has(EDataComponents.ENTITY_REF.get())) {
					UUID uuid = taglock.get(EDataComponents.ENTITY_REF.get()).uuid();
					Player target = level.getPlayerByUUID(uuid);

					if(target != null) {
						PoppetUtils.bind(itemStack, target);
						if(!player.isCreative())
							taglock.shrink(1);
					}
				}
			}
		}
		return itemStack;
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		if(hand == InteractionHand.MAIN_HAND) {
			if(!PoppetUtils.isBound(player.getMainHandItem())) {
				ItemStack taglock = player.getOffhandItem();
				if(taglock.getItem() instanceof TaglockFilledItem) {
					if(taglock.has(EDataComponents.ENTITY_REF.get())) {
						Player target = level.getPlayerByUUID(taglock.get(EDataComponents.ENTITY_REF.get()).uuid());

						if(target != null) {
							player.startUsingItem(hand);
							return InteractionResultHolder.consume(player.getMainHandItem());
						}
					}
				}
			}
		}
		return super.use(level, player, hand);
	}

	@Override
	public boolean isEnchantable(ItemStack stack) {
		return false;
	}

	@Override
	public UseAnim getUseAnimation(ItemStack pStack) {
		return UseAnim.BOW;
	}

	@Override
	public int getUseDuration(ItemStack stack, LivingEntity entity) {
		return 32;
	}

}
