package net.favouriteless.enchanted.common.items;

import net.favouriteless.enchanted.api.MutagenManager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

public class MutandisItem extends Item {

    private final boolean isExtremis;

    public MutandisItem(boolean isExtremis) {
        super(new Properties());
        this.isExtremis = isExtremis;
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();

        if (level instanceof ServerLevel serverLevel) {
            if (MutagenManager.get().tryStartMutating(serverLevel, pos, isExtremis)) {
                if (!context.getPlayer().isCreative()) {
                    context.getItemInHand().shrink(1);
                }
                serverLevel.playSound(null, pos, SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.MASTER);
                serverLevel.sendParticles(ParticleTypes.WITCH, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, 25, 0.5D, 0.5D, 0.5D, 0.0D);
            }
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

}
