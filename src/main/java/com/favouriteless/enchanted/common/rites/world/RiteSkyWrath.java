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

package com.favouriteless.enchanted.common.rites.world;

import com.favouriteless.enchanted.EnchantedConfig;
import com.favouriteless.enchanted.api.rites.AbstractRite;
import com.favouriteless.enchanted.common.init.EnchantedItems;
import com.favouriteless.enchanted.common.init.registry.EnchantedBlocks;
import com.favouriteless.enchanted.common.init.registry.EnchantedParticles;
import com.favouriteless.enchanted.common.rites.CirclePart;
import com.favouriteless.enchanted.common.rites.RiteType;
import com.favouriteless.enchanted.common.util.WaystoneHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.UUID;

public class RiteSkyWrath extends AbstractRite {

    private static long LAST_USE_TIME = System.currentTimeMillis();
    public static final int START_RAINING = 120;
    public static final int EXPLODE = 180;
    public static final double LIGHTNING_RADIUS = 5;
    private BlockPos targetPos = null;
    private Level targetLevel = null;

    public RiteSkyWrath(RiteType<?> type, ServerLevel level, BlockPos pos, UUID caster, int power, int powerTick) {
        super(type, level, pos, caster, power, powerTick);
    }

    public RiteSkyWrath(RiteType<?> type, ServerLevel level, BlockPos pos, UUID caster) {
        this(type, level, pos, caster, 1000, 0); // Power, power per tick
        CIRCLES_REQUIRED.put(CirclePart.SMALL, EnchantedBlocks.CHALK_WHITE.get());
        ITEMS_REQUIRED.put(Items.IRON_SWORD, 1);
        ITEMS_REQUIRED.put(EnchantedItems.QUICKLIME.get(), 1);
    }

    @Override
    public void execute() {
        ServerLevel level = getLevel();
        BlockPos pos = getPos();
        detatchFromChalk();
        LAST_USE_TIME = System.currentTimeMillis();
        List<Entity> entities = CirclePart.SMALL.getEntitiesInside(level, pos, entity -> entity instanceof ItemEntity);

        for(Entity entity : entities) {
            if(entity instanceof ItemEntity itemEntity) {
                if(itemEntity.getItem().getItem() == EnchantedItems.BOUND_WAYSTONE.get()) {
                    if(itemEntity.getItem().hasTag()) {
                        targetPos = WaystoneHelper.getPos(itemEntity.getItem());
                        targetLevel = WaystoneHelper.getLevel(level, itemEntity.getItem());
                        if(targetPos != null) {
                            itemEntity.setNeverPickUp();
                            itemEntity.discard();
                            playConsumeEffects(itemEntity);
                            break;
                        }
                    }
                }
                else if(itemEntity.getItem().getItem() == EnchantedItems.BLOODED_WAYSTONE.get()) {
                    if(itemEntity.getItem().hasTag()) {
                        setTargetEntity(WaystoneHelper.getEntity(level, itemEntity.getItem()));
                        Entity targetEntity = getTargetEntity();
                        if(targetEntity != null) {
                            targetPos = targetEntity.blockPosition();
                            targetLevel = targetEntity.level;
                            itemEntity.setNeverPickUp();
                            itemEntity.discard();
                            playConsumeEffects(itemEntity);
                            break;
                        }
                    }
                }
            }
        }

        if(targetPos != null && targetLevel != null && targetLevel instanceof ServerLevel serverLevel)
            serverLevel.sendParticles(EnchantedParticles.SKY_WRATH_SEED.get(), targetPos.getX()+0.5D, targetPos.getY()+2.0D, targetPos.getZ()+0.5D, 1, 0.0D, 0.0D, 0.0D, 0.0D);
        else
            level.sendParticles(EnchantedParticles.SKY_WRATH_SEED.get(), pos.getX()+0.5D, pos.getY()+2.0D, pos.getZ()+0.5D, 1, 0.0D, 0.0D, 0.0D, 0.0D);
    }

    @Override
    public void onTick() {
        ServerLevel level = getLevel();
        BlockPos pos = getPos();
        if(ticks == START_RAINING) {
            level.setWeatherParameters(0, 6000, true, true);
        }
        else if(ticks > EXPLODE) {
            if(targetPos != null)
                spawnLightning(targetLevel, targetPos.getX() + 0.5D, targetPos.getY(), targetPos.getZ() + 0.5D);
            else
                spawnLightning(level, pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D);
            stopExecuting();
        }
    }

    @Override
    protected boolean checkAdditional() {
        if(System.currentTimeMillis() - LAST_USE_TIME < EnchantedConfig.SKY_WRATH_COOLDOWN.get() * 1000) {
            Player caster = getLevel().getServer().getPlayerList().getPlayer(getCasterUUID());
            caster.displayClientMessage(new TextComponent("The sky is not ready to release lightning.").withStyle(ChatFormatting.RED), false);
            return false;
        }
        return true;
    }

    protected void spawnLightning(Level pLevel, double x, double y, double z) {
        for(int a = 0; a < 360; a+=60) {
            double angle = Math.toRadians(a);
            double cx = x + Math.sin(angle) * LIGHTNING_RADIUS;
            double cz = z + Math.cos(angle) * LIGHTNING_RADIUS;

            LightningBolt lightningBolt = EntityType.LIGHTNING_BOLT.create(pLevel);
            lightningBolt.moveTo(cx, y, cz);
            pLevel.addFreshEntity(lightningBolt);
        }
    }

}
