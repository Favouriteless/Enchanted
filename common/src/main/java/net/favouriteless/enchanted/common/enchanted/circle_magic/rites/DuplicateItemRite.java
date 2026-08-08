package net.favouriteless.enchanted.common.enchanted.circle_magic.rites;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class DuplicateItemRite extends Rite {

    private final Item targetItem;
    private final int count;

    public DuplicateItemRite(BaseRiteParams baseParams, RiteParams params, Item targetItem, int count) {
        super(baseParams, params);
        this.targetItem = targetItem;
        this.count = count;
    }

    @Override
    protected boolean onStart(RiteParams params) {
        for (ItemStack stack : params.consumedItems) {
            if (stack.getItem() == targetItem) {
                ItemStack copy = stack.copy();
                copy.setCount(count);
                ItemEntity itemEntity = new ItemEntity(level, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, copy);
                level.addFreshEntity(itemEntity);
                break;
            }
        }
        level.playSound(null, pos, SoundEvents.ZOMBIE_VILLAGER_CURE, SoundSource.MASTER, 0.5F, 1.0F);
        randomParticles(ParticleTypes.WITCH);
        return false;
    }

}
