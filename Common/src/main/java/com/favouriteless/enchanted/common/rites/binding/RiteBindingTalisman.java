package com.favouriteless.enchanted.common.rites.binding;

import com.favouriteless.enchanted.api.rites.AbstractRite;
import com.favouriteless.enchanted.common.init.registry.EnchantedBlocks;
import com.favouriteless.enchanted.common.init.registry.EnchantedItems;
import com.favouriteless.enchanted.common.rites.CirclePart;
import com.favouriteless.enchanted.common.rites.RiteType;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;

import java.util.UUID;

public class RiteBindingTalisman extends AbstractRite {

    protected RiteBindingTalisman(RiteType<?> type, ServerLevel level, BlockPos pos, UUID caster, int power) {
        super(type, level, pos, caster, power, 0);
    }

    public RiteBindingTalisman(RiteType<?> type, ServerLevel level, BlockPos pos, UUID caster) {
        this(type, level, pos, caster, 1000); // Power, power per tick
        ITEMS_REQUIRED.put(EnchantedItems.CIRCLE_TALISMAN.get(), 1);
        ITEMS_REQUIRED.put(Items.REDSTONE, 1);
    }

    @Override
    public void execute() {
        byte small = testForCircle(CirclePart.SMALL);
        byte medium = testForCircle(CirclePart.MEDIUM);
        byte large = testForCircle(CirclePart.LARGE);

        if(small == 0 && medium == 0 && large == 0) {
            cancel();
            stopExecuting();
            return;
        }
        else {
            CompoundTag nbt = new CompoundTag();
            nbt.putByte("small", small);
            nbt.putByte("medium", medium);
            nbt.putByte("large", large);
            ItemStack talisman = new ItemStack(EnchantedItems.CIRCLE_TALISMAN.get(), 1);
            talisman.setTag(nbt);

            ServerLevel level = getLevel();
            BlockPos pos = getPos();
            level.addFreshEntity(new ItemEntity(level, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, talisman));
            level.playSound(null, pos, SoundEvents.ZOMBIE_VILLAGER_CURE, SoundSource.MASTER, 1.0F, 1.0F);
            spawnMagicParticles();

            if(small != 0) CirclePart.SMALL.destroy(level, pos);
            if(medium != 0) CirclePart.MEDIUM.destroy(level, pos);
            if(large != 0) CirclePart.LARGE.destroy(level, pos);
            level.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
        }

        stopExecuting();
    }

    @Override
    protected boolean checkAdditional() {
        for(ItemStack item : itemsConsumed) {
            if(item.getItem() == EnchantedItems.CIRCLE_TALISMAN.get() && item.hasTag()) { // If input talisman is already bound
                cancel();
                ServerPlayer player = tryFindCaster();
                if(player != null)
                    player.displayClientMessage(Component.literal("Talisman already contains a circle").withStyle(ChatFormatting.RED), false);
                return false;
            }
        }
        return true;
    }

    private byte testForCircle(CirclePart circle) {
        ServerLevel level = getLevel();
        BlockPos pos = getPos();
        if(circle.match(level, pos, EnchantedBlocks.CHALK_WHITE.get())) return 1;
        if(circle.match(level, pos, EnchantedBlocks.CHALK_RED.get())) return 2;
        if(circle.match(level, pos, EnchantedBlocks.CHALK_PURPLE.get())) return 3;
        return 0;
    }

}
