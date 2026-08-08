package net.favouriteless.enchanted.common.items;

import net.favouriteless.enchanted.common.entities.Broomstick;
import net.favouriteless.enchanted.common.init.EEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

public class BroomstickItem extends Item {

    public BroomstickItem(Properties properties) {
        super(properties);
    }

    @Override
    public boolean isFoil(ItemStack pStack) {
        return true;
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        Player player = context.getPlayer();

        if (!level.isClientSide) {
            BlockPos pos = context.getClickedPos().relative(context.getClickedFace());

            Broomstick broom = EEntityTypes.BROOMSTICK.get().create(level);
            broom.setPos(pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D);
            broom.setYRot(player.getYRot());
            ;
            level.addFreshEntity(broom);

            if (!player.getAbilities().instabuild) // Player not in creative
            {
                context.getItemInHand().shrink(1);
            }
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

}
