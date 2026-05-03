package net.favouriteless.enchanted.common.entities;

import net.favouriteless.enchanted.common.enchanted.poppet.PoppetHelper;
import net.favouriteless.enchanted.common.util.EntityUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;

public class VoodooItemEntity extends ItemEntity {

    private int underWaterTicks;
    private int health;

    public VoodooItemEntity(EntityType<? extends ItemEntity> type, Level level) {
        super(type, level);
    }

    @Override
    public boolean fireImmune() {
        return true;
    }

    @Override
    public void setItem(ItemStack stack) {
        if(stack.isDamageableItem())
            this.health = stack.getMaxDamage() - stack.getDamageValue();
        super.setItem(stack);
    }

    @Override
    public void tick() {
        super.tick();
        if(level() instanceof ServerLevel level && PoppetHelper.isBound(getItem())) {
            ItemStack item = getItem();
            ServerPlayer owner = ((ServerPlayer)getOwner());
            ServerPlayer target = EntityUtils.tryFindPlayer(level, PoppetHelper.getData(item).uuid()); // Not NPE, we already checked

            if(target == null)
                return;

            if(isInWaterOrBubble())
                ++underWaterTicks;
            else
                underWaterTicks = 0;

            if(underWaterTicks > 20 && tryHurt(owner, target, level.damageSources().drown(), 1)) {
            }
            else if(isInLava() && tryHurt(owner, target, level.damageSources().lava(), 4)) {
                target.igniteForSeconds(15);
            }
            else if(level.getBlockState(blockPosition()).is(Blocks.FIRE) && tryHurt(owner, target, level.damageSources().inFire(), 1) ||
                    level.getBlockState(blockPosition()).is(Blocks.SOUL_FIRE) && tryHurt(owner, target, level.damageSources().inFire(), 2)
            ) {
                if(target.getRemainingFireTicks() < 0)
                    target.igniteForSeconds(8.0F);
                target.setRemainingFireTicks(target.getRemainingFireTicks() + 1);
            }
        }
    }

    public boolean tryHurt(ServerPlayer owner, ServerPlayer target, DamageSource source, int amount) {
        if(PoppetHelper.tryUseVoodoo(owner, target, getItem()) && target.hurt(source, amount)) {
            hurt(amount);
            return true;
        }
        return false;
    }

    public void hurt(int amount) {
        ItemStack item = getItem();

        if(level().isClientSide)
            return;

        health = health - amount;
        item.setDamageValue(item.getMaxDamage() - health);
        if (health <= 0) {
            getItem().onDestroyed(this);
            discard();
        }
    }

}
