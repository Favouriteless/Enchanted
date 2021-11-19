/*
 * Copyright (c) 2021. Favouriteless
 * Enchanted, a minecraft mod.
 * GNU GPLv3 License
 *
 *     This file is part of Enchanted.
 *
 *     Enchanted is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Enchanted is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Enchanted.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.favouriteless.enchanted.common.items;

import com.favouriteless.enchanted.common.init.EnchantedItems;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.*;
import net.minecraft.util.math.vector.Vector2f;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

public class TaglockItem extends Item {

    public TaglockItem(Properties properties) {
        super(properties);
    }

    @Override
    public ActionResultType interactLivingEntity(ItemStack stack, PlayerEntity player, LivingEntity target, Hand hand) {
        if (target instanceof PlayerEntity) {
            int failChance = 2;
            if (!player.isCrouching()) {
                failChance += 2;
            }
            if(!facingAway(player, (PlayerEntity) target)) {
                failChance += 4;
            }
            if (random.nextInt(10) >= failChance) {
                if(!player.level.isClientSide) {
                    player.displayClientMessage(new StringTextComponent("Taglock attempt failed").withStyle(TextFormatting.RED), false);
                    ((ServerPlayerEntity) target).displayClientMessage(new StringTextComponent(player.getDisplayName() + " has tried to taglock you").withStyle(TextFormatting.RED), false);
                }
                return ActionResultType.FAIL;
            }
        }
        fillTaglock(player, stack, target);
        if(player.level.isClientSide) player.level.playSound(player, player, SoundEvents.EXPERIENCE_ORB_PICKUP, SoundCategory.MASTER, 1.0F, 1.0F);

        return ActionResultType.SUCCESS;
    }

    public void fillTaglock(PlayerEntity player, ItemStack stack, LivingEntity entity) {
        ItemStack newStack = new ItemStack(EnchantedItems.TAGLOCK_FILLED.get(), 1);
        CompoundNBT nbt = new CompoundNBT();
        nbt.putUUID("entity", entity.getUUID());
        nbt.putString("entityName", entity.getDisplayName().getString());
        newStack.setTag(nbt);

        if(!player.inventory.add(newStack)) {
            ItemEntity itemEntity = new ItemEntity(player.level, player.getX(), player.getY(0.5), player.getZ(), newStack);
            itemEntity.setNoPickUpDelay();
            itemEntity.setOwner(player.getUUID());
            player.level.addFreshEntity(itemEntity);
        }

        stack.shrink(1);
    }

    private boolean facingAway(PlayerEntity source, PlayerEntity target){
        Vector3d vv1 = source.getLookAngle().normalize();
        Vector3d vv2 = target.getLookAngle().normalize();

        Vector2f v1 = new Vector2f((float)vv1.x, (float)vv1.z);
        Vector2f v2 = new Vector2f((float)vv2.x, (float)vv2.z);

        return !(Math.acos((v1.x * v2.x + v1.y *v2.y) / (Math.sqrt(v1.x * v1.x + v1.y * v1.y) * Math.sqrt(v2.x * v2.x + v2.y * v2.y))) > Math.PI/2);
    }
}
