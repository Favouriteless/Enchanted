package net.favouriteless.enchanted.common.circle_magic.rites;

import net.favouriteless.enchanted.common.items.TaglockFilledItem;
import net.favouriteless.enchanted.common.util.WaystoneHelper;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.UUID;

public class EntityBoundCreateItemRite extends Rite {

    private final List<ItemStack> items;

    public EntityBoundCreateItemRite(BaseRiteParams baseParams, RiteParams params, List<ItemStack> items) {
        super(baseParams, params);
        this.items = items;
    }

    @Override
    protected boolean onStart(RiteParams params) {
        UUID uuid = null;
        String name = null;

        for(ItemStack stack : params.consumedItems) {
            if(!stack.hasTag() || !stack.getTag().contains(TaglockFilledItem.TARGET_TAG))
                continue;

            CompoundTag nbt = stack.getTag();
            uuid = nbt.getUUID(TaglockFilledItem.TARGET_TAG);

            Entity entity = findEntity(uuid);
            if(entity != null)
                name = entity.getDisplayName().getString();
            else
                name = nbt.getString(TaglockFilledItem.NAME_TAG);

            break;
        }

        for(ItemStack stack : items) {
            WaystoneHelper.bind(stack, uuid, name);
            ItemEntity entity = new ItemEntity(level, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, stack.copy());
            level.addFreshEntity(entity);
        }
        level.playSound(null, pos, SoundEvents.ZOMBIE_VILLAGER_CURE, SoundSource.MASTER, 0.5F, 1.0F);
        randomParticles(ParticleTypes.WITCH);
        return false;
    }

}
