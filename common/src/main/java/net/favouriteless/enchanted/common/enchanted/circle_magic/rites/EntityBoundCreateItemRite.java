package net.favouriteless.enchanted.common.enchanted.circle_magic.rites;

import net.favouriteless.enchanted.common.items.component.EDataComponents;
import net.favouriteless.enchanted.common.items.component.EntityRefData;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class EntityBoundCreateItemRite extends Rite {

    private final List<ItemStack> items;

    public EntityBoundCreateItemRite(BaseRiteParams baseParams, RiteParams params, List<ItemStack> items) {
        super(baseParams, params);
        this.items = items;
    }

    @Override
    protected boolean onStart(RiteParams params) {
        EntityRefData ref = null;

        for(ItemStack stack : params.consumedItems) {
            if(stack.has(EDataComponents.ENTITY_REF.get())) {
                ref = stack.get(EDataComponents.ENTITY_REF.get());

                Entity entity = findEntity(ref.uuid()); // Try to re-grab name if possible.
                if(entity != null)
                    ref = EntityRefData.of(ref.uuid(), entity.getDisplayName().getString());

                break;
            }
        }

        for(ItemStack stack : items) {
            stack.set(EDataComponents.ENTITY_REF.get(), ref);
            ItemEntity itemEntity = new ItemEntity(level, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, stack.copy());
            level.addFreshEntity(itemEntity);
        }
        level.playSound(null, pos, SoundEvents.ZOMBIE_VILLAGER_CURE, SoundSource.MASTER, 0.5F, 1.0F);
        randomParticles(ParticleTypes.WITCH);
        return false;
    }

}
