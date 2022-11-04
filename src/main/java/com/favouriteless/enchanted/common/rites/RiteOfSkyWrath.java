/*
 *
 *   Copyright (c) 2022. Favouriteless
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

package com.favouriteless.enchanted.common.rites;

import com.favouriteless.enchanted.EnchantedConfig;
import com.favouriteless.enchanted.api.rites.AbstractRite;
import com.favouriteless.enchanted.common.init.EnchantedBlocks;
import com.favouriteless.enchanted.common.init.EnchantedItems;
import com.favouriteless.enchanted.common.init.EnchantedParticles;
import com.favouriteless.enchanted.common.init.EnchantedRiteTypes;
import com.favouriteless.enchanted.common.util.WaystoneHelper;
import com.favouriteless.enchanted.common.util.rite.CirclePart;
import com.favouriteless.enchanted.common.util.rite.RiteType;
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

public class RiteOfSkyWrath extends AbstractRite {

    private static long LAST_USE_TIME = System.currentTimeMillis();
    public static final int START_RAINING = 120;
    public static final int EXPLODE = 180;
    public static final double LIGHTNING_RADIUS = 5;
    private BlockPos targetPos = null;
    private Level targetLevel = null;

    public RiteOfSkyWrath(RiteType<?> type, int power, int powerTick) {
        super(type, power, powerTick);
    }

    public RiteOfSkyWrath() {
        this(EnchantedRiteTypes.SKY_WRATH.get(), 1000, 0); // Power, power per tick
        CIRCLES_REQUIRED.put(CirclePart.SMALL, EnchantedBlocks.CHALK_WHITE.get());
        ITEMS_REQUIRED.put(Items.IRON_SWORD, 1);
        ITEMS_REQUIRED.put(EnchantedItems.QUICKLIME.get(), 1);
    }

    @Override
    public void execute() {
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
                        targetEntity = WaystoneHelper.getPlayer(level, itemEntity.getItem());
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
        if(System.currentTimeMillis() > LAST_USE_TIME + EnchantedConfig.SKY_WRATH_COOLDOWN.get() * 1000) {
            Player caster = level.getServer().getPlayerList().getPlayer(casterUUID);
            caster.displayClientMessage(new TextComponent("The sky is not ready to release lightning.").withStyle(ChatFormatting.RED), false);
            return true;
        }
        return false;
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
