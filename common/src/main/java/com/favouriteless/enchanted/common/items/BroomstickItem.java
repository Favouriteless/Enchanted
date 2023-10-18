package com.favouriteless.enchanted.common.items;

import com.favouriteless.enchanted.common.entities.Broomstick;
import com.favouriteless.enchanted.common.init.registry.EnchantedEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.phys.Vec3;

public class BroomstickItem extends Item {

	public BroomstickItem(Properties pProperties) {
		super(pProperties);
	}

	@Override
	public boolean isFoil(ItemStack pStack) {
		return true;
	}

	@Override
	public InteractionResult useOn(UseOnContext context) {
		if(context.getLevel().isClientSide) {
			return InteractionResult.SUCCESS;
		}
		else {
			BlockPos pos = context.getClickedPos().relative(context.getClickedFace());

			Broomstick broom = EnchantedEntityTypes.BROOMSTICK.get().create(context.getLevel());
			broom.setPos(pos.getX()+0.5D, pos.getY(), pos.getZ()+0.5D);
			broom.setDeltaMovement(Vec3.ZERO);
			broom.xo = pos.getX()+0.5D;
			broom.yo = pos.getY();
			broom.zo = pos.getZ()+0.5D;
			broom.setYRot(context.getPlayer().getYRot());;
			context.getLevel().addFreshEntity(broom);

			if(!context.getPlayer().getAbilities().instabuild) // Player not in creative
				context.getItemInHand().shrink(1);
		}
		return InteractionResult.PASS;
	}
}
