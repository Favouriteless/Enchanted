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

package com.favouriteless.enchanted.common.items;

import com.favouriteless.enchanted.Enchanted;
import com.favouriteless.enchanted.api.capabilities.EnchantedCapabilities;
import com.favouriteless.enchanted.api.capabilities.IBedCapability;
import com.favouriteless.enchanted.common.blockentities.BloodPoppyBlockEntity;
import com.favouriteless.enchanted.common.blocks.crops.BloodPoppyBlock;
import com.favouriteless.enchanted.common.init.EnchantedItems;
import com.favouriteless.enchanted.common.init.EnchantedTags.EntityTypes;
import com.favouriteless.enchanted.common.init.registry.EnchantedBlocks;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.protocol.game.ClientboundSoundPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BedPart;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.UUID;

public class TaglockItem extends Item {

    public TaglockItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity target, InteractionHand hand) {
        if (target instanceof Player) {
            int failChance = 2;
            if (!player.isCrouching()) {
                failChance += 2;
            }
            if(!facingAway(player, (Player) target)) {
                failChance += 4;
            }
            if (Enchanted.RANDOM.nextInt(10) >= failChance) {
                if(!player.level.isClientSide) {
                    player.displayClientMessage(new TextComponent("Taglock attempt failed").withStyle(ChatFormatting.RED), false);
                    ((ServerPlayer) target).displayClientMessage(new TextComponent(player.getDisplayName().getString() + " has tried to taglock you").withStyle(ChatFormatting.RED), false);
                }
                return InteractionResult.FAIL;
            }
        }
        if(!ForgeRegistries.ENTITIES.tags().getTag(EntityTypes.TAGLOCK_BLACKLIST).contains(target.getType()))
            fillTaglockEntity(player, stack, target);

        return InteractionResult.SUCCESS;
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level world = context.getLevel();
        BlockState state = world.getBlockState(context.getClickedPos());
        if(state.getBlock() instanceof BedBlock) {
            if(!world.isClientSide) {
                BlockEntity blockEntity;
                if (state.getValue(BedBlock.PART) == BedPart.HEAD) {
                    blockEntity = world.getBlockEntity(context.getClickedPos());
                } else {
                    blockEntity = world.getBlockEntity(context.getClickedPos().relative(BedBlock.getConnectedDirection(state)));
                }
                if (blockEntity == null) return InteractionResult.FAIL;
                IBedCapability playerCapability = blockEntity.getCapability(EnchantedCapabilities.BED).orElse(null);

                if (playerCapability.getUUID() != null && playerCapability.getName() != null) {
                    fillTaglock(context.getPlayer(), context.getItemInHand(), playerCapability.getUUID(), playerCapability.getName());
                    context.getLevel().getPlayerByUUID(playerCapability.getUUID());
                    playerCapability.setName(null);
                    playerCapability.setUUID(null);
                }
                return InteractionResult.CONSUME;
            }
            return InteractionResult.SUCCESS;
        }
        else if(state.getBlock() == EnchantedBlocks.BLOOD_POPPY.get()) {
            if(!world.isClientSide) {
                if (state.getValue(BloodPoppyBlock.FILLED)) {
                    BlockEntity blockEntity = world.getBlockEntity(context.getClickedPos());
                    if (blockEntity != null) {
                        BloodPoppyBlockEntity poppyBe = (BloodPoppyBlockEntity)blockEntity;
                        fillTaglock(context.getPlayer(), context.getItemInHand(), poppyBe.getUUID(), poppyBe.getName());
                        BloodPoppyBlock.reset(world, context.getClickedPos());
                    }
                    else {
                        return InteractionResult.FAIL;
                    }
                }
                return InteractionResult.CONSUME;
            }
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.FAIL;
    }

    public void fillTaglockEntity(Player player, ItemStack stack, LivingEntity entity) {
        fillTaglock(player, stack, entity.getUUID(), entity.getDisplayName().getString());
    }

    public void fillTaglock(Player pPlayer, ItemStack stack, UUID uuid, String name) {
        if(pPlayer instanceof ServerPlayer player) {
            ItemStack newStack = new ItemStack(EnchantedItems.TAGLOCK_FILLED.get(), 1);

            CompoundTag nbt = new CompoundTag();
            nbt.putUUID("entity", uuid);
            nbt.putString("entityName", name);
            newStack.setTag(nbt);

            if (!player.getInventory().add(newStack)) {
                ItemEntity itemEntity = new ItemEntity(player.level, player.getX(), player.getY(0.5), player.getZ(), newStack);
                itemEntity.setNoPickUpDelay();
                itemEntity.setOwner(player.getUUID());
                player.level.addFreshEntity(itemEntity);
            }

            // Send sound packet to player
            player.connection.send(new ClientboundSoundPacket(SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.MASTER, player.getX(), player.getY(), player.getZ(), 1.0F, 1.0F));
            stack.shrink(1);
        }
    }

    private boolean facingAway(Player source, Player target){
        Vec3 vv1 = source.getLookAngle().normalize();
        Vec3 vv2 = target.getLookAngle().normalize();

        Vec2 v1 = new Vec2((float)vv1.x, (float)vv1.z);
        Vec2 v2 = new Vec2((float)vv2.x, (float)vv2.z);

        return !(Math.acos((v1.x * v2.x + v1.y *v2.y) / (Math.sqrt(v1.x * v1.x + v1.y * v1.y) * Math.sqrt(v2.x * v2.x + v2.y * v2.y))) > Math.PI/2);
    }


}
