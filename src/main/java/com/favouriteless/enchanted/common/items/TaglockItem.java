/*
 * Copyright (c) 2022. Favouriteless
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

import com.favouriteless.enchanted.api.capabilities.bed.BedPlayerCapabilityManager;
import com.favouriteless.enchanted.api.capabilities.bed.IBedPlayerCapability;
import com.favouriteless.enchanted.common.blocks.crops.BloodPoppyBlock;
import com.favouriteless.enchanted.common.init.EnchantedBlocks;
import com.favouriteless.enchanted.common.init.EnchantedItems;
import com.favouriteless.enchanted.common.tileentity.BloodPoppyTileEntity;
import net.minecraft.block.BedBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.play.server.SPlaySoundEffectPacket;
import net.minecraft.state.properties.BedPart;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.vector.Vector2f;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

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
        fillTaglockEntity(player, stack, target);

        return ActionResultType.SUCCESS;
    }

    @Override
    public ActionResultType useOn(ItemUseContext context) {
        World world = context.getLevel();
        BlockState state = world.getBlockState(context.getClickedPos());
        if(state.getBlock() instanceof BedBlock) {
            if(!world.isClientSide) {
                TileEntity tileEntity;
                if (state.getValue(BedBlock.PART) == BedPart.HEAD) {
                    tileEntity = world.getBlockEntity(context.getClickedPos());
                } else {
                    tileEntity = world.getBlockEntity(context.getClickedPos().relative(BedBlock.getConnectedDirection(state)));
                }
                if (tileEntity == null) return ActionResultType.FAIL;
                IBedPlayerCapability playerCapability = tileEntity.getCapability(BedPlayerCapabilityManager.INSTANCE).orElse(null);

                if (playerCapability.getValue() != null) {
                    fillTaglockEntity(context.getPlayer(), context.getItemInHand(), world.getPlayerByUUID(playerCapability.getValue()));
                    context.getLevel().getPlayerByUUID(playerCapability.getValue());
                }
                return ActionResultType.CONSUME;
            }
            return ActionResultType.SUCCESS;
        }
        else if(state.getBlock().is(EnchantedBlocks.BLOOD_POPPY.get())) {
            if(!world.isClientSide) {
                if (state.getValue(BloodPoppyBlock.FILLED)) {
                    TileEntity tileEntity = world.getBlockEntity(context.getClickedPos());
                    if (tileEntity != null) {
                        BloodPoppyTileEntity poppyTileEntity = (BloodPoppyTileEntity)tileEntity;
                        fillTaglock(context.getPlayer(), context.getItemInHand(), poppyTileEntity.getUUID(), poppyTileEntity.getName());
                        BloodPoppyBlock.reset(world, context.getClickedPos());
                    }
                    else {
                        return ActionResultType.FAIL;
                    }
                }
                return ActionResultType.CONSUME;
            }
            return ActionResultType.SUCCESS;
        }
        return ActionResultType.FAIL;
    }

    public void fillTaglockEntity(PlayerEntity player, ItemStack stack, LivingEntity entity) {
        fillTaglock(player, stack, entity.getUUID(), entity.getDisplayName().getString());
    }

    public void fillTaglock(PlayerEntity pPlayer, ItemStack stack, UUID uuid, String name) {
        if(pPlayer instanceof ServerPlayerEntity) {
            ServerPlayerEntity player = (ServerPlayerEntity)pPlayer;
            ItemStack newStack = new ItemStack(EnchantedItems.TAGLOCK_FILLED.get(), 1);

            CompoundNBT nbt = new CompoundNBT();
            nbt.putUUID("entity", uuid);
            nbt.putString("entityName", name);
            newStack.setTag(nbt);

            if (!player.inventory.add(newStack)) {
                ItemEntity itemEntity = new ItemEntity(player.level, player.getX(), player.getY(0.5), player.getZ(), newStack);
                itemEntity.setNoPickUpDelay();
                itemEntity.setOwner(player.getUUID());
                player.level.addFreshEntity(itemEntity);
            }

            // Send sound packet to player
            player.connection.send(new SPlaySoundEffectPacket(SoundEvents.EXPERIENCE_ORB_PICKUP, SoundCategory.MASTER, player.getX(), player.getY(), player.getZ(), 1.0F, 1.0F));
            stack.shrink(1);
        }
    }

    private boolean facingAway(PlayerEntity source, PlayerEntity target){
        Vector3d vv1 = source.getLookAngle().normalize();
        Vector3d vv2 = target.getLookAngle().normalize();

        Vector2f v1 = new Vector2f((float)vv1.x, (float)vv1.z);
        Vector2f v2 = new Vector2f((float)vv2.x, (float)vv2.z);

        return !(Math.acos((v1.x * v2.x + v1.y *v2.y) / (Math.sqrt(v1.x * v1.x + v1.y * v1.y) * Math.sqrt(v2.x * v2.x + v2.y * v2.y))) > Math.PI/2);
    }


}
