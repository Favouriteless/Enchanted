/*
 *
 *   Copyright (c) 2023. Favouriteless
 *   Enchanted, a minecraft mod.
 *   GNU GPLv3 License
 *
 *       This file is part of Enchanted.
 *
 *       Enchanted is free software: you can redistribute it and/or modify
 *       it under the terms of the GNU General Public License as published by
 *       the Free Software Foundation, either version 3 of the License, or
 *       (at your option) any later version.
 *
 *       Enchanted is distributed in the hope that it will be useful,
 *       but WITHOUT ANY WARRANTY; without even the implied warranty of
 *       MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *       GNU General Public License for more details.
 *
 *       You should have received a copy of the GNU General Public License
 *       along with Enchanted.  If not, see <https://www.gnu.org/licenses/>.
 *
 *
 */

package com.favouriteless.enchanted.common.rites.binding;

import com.favouriteless.enchanted.api.rites.AbstractRite;
import com.favouriteless.enchanted.common.init.registry.EnchantedBlocks;
import com.favouriteless.enchanted.common.init.EnchantedItems;
import com.favouriteless.enchanted.common.init.registry.EnchantedRiteTypes;
import com.favouriteless.enchanted.common.util.rite.CirclePart;
import com.favouriteless.enchanted.common.util.rite.RiteType;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;

public class RiteBindingTalisman extends AbstractRite {

    protected RiteBindingTalisman(RiteType<?> type, int power) {
        super(type, power, 0);
    }

    public RiteBindingTalisman() {
        this(EnchantedRiteTypes.BINDING_TALISMAN.get(), 1000); // Power, power per tick
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
            ItemStack talisman = new ItemStack(EnchantedItems.CIRCLE_TALISMAN.get(), 1, nbt);
            talisman.setTag(nbt);

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
                ServerPlayer player = level.getServer().getPlayerList().getPlayer(casterUUID);
                if(player != null)
                    player.displayClientMessage(new TextComponent("Talisman already contains a circle").withStyle(ChatFormatting.RED), false);
                return false;
            }
        }
        return true;
    }

    private byte testForCircle(CirclePart circle) {
        if(circle.match(level, pos, EnchantedBlocks.CHALK_WHITE.get())) return 1;
        if(circle.match(level, pos, EnchantedBlocks.CHALK_RED.get())) return 2;
        if(circle.match(level, pos, EnchantedBlocks.CHALK_PURPLE.get())) return 3;
        return 0;
    }

}
