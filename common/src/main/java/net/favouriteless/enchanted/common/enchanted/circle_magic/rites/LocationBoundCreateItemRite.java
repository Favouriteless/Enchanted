package net.favouriteless.enchanted.common.enchanted.circle_magic.rites;

import net.favouriteless.enchanted.common.items.component.EDataComponents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class LocationBoundCreateItemRite extends Rite {

    private final List<ItemStack> items;

    public LocationBoundCreateItemRite(BaseRiteParams baseParams, RiteParams params, List<ItemStack> items) {
        super(baseParams, params);
        this.items = items;
    }

    @Override
    protected boolean onStart(RiteParams params) {
        for (ItemStack stack : items) {
            stack.set(EDataComponents.BLOCK_POS.get(), pos);
            stack.set(EDataComponents.LEVEL_KEY.get(), level.dimension());
            ItemEntity itemEntity = new ItemEntity(level, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, stack.copy());
            level.addFreshEntity(itemEntity);
        }
        level.playSound(null, pos, SoundEvents.ZOMBIE_VILLAGER_CURE, SoundSource.MASTER, 0.5F, 1.0F);
        randomParticles(ParticleTypes.WITCH);
        return false;
    }

}
