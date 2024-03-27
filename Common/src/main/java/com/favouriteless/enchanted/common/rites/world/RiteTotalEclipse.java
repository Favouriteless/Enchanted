package com.favouriteless.enchanted.common.rites.world;

import com.favouriteless.enchanted.api.rites.AbstractRite;
import com.favouriteless.enchanted.common.CommonConfig;
import com.favouriteless.enchanted.common.init.registry.EnchantedBlocks;
import com.favouriteless.enchanted.common.init.registry.EnchantedItems;
import com.favouriteless.enchanted.common.rites.CirclePart;
import com.favouriteless.enchanted.common.rites.RiteType;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;

import java.util.UUID;

public class RiteTotalEclipse extends AbstractRite {

    private static long LAST_USE_TIME = System.currentTimeMillis();

    protected RiteTotalEclipse(RiteType<?> type, ServerLevel level, BlockPos pos, UUID caster, int power, int powerTick) {
        super(type, level, pos, caster, power, powerTick);
    }

    public RiteTotalEclipse(RiteType<?> type, ServerLevel level, BlockPos pos, UUID caster) {
        this(type, level, pos, caster, 3000, 0); // Power, power per tick
        CIRCLES_REQUIRED.put(CirclePart.SMALL, EnchantedBlocks.CHALK_WHITE.get());
        ITEMS_REQUIRED.put(Items.STONE_AXE, 1);
        ITEMS_REQUIRED.put(EnchantedItems.QUICKLIME.get(), 1);
    }

    @Override
    public void execute() {
        ServerLevel level = getLevel();
        level.setDayTime(18000);
        level.playSound(null, getPos(), SoundEvents.ZOMBIE_VILLAGER_CURE, SoundSource.MASTER, 0.5F, 1.0F);
        LAST_USE_TIME = System.currentTimeMillis();
        stopExecuting();
    }

    @Override
    protected boolean checkAdditional() {
        if(System.currentTimeMillis() > LAST_USE_TIME + CommonConfig.TOTAL_ECLIPSE_COOLDOWN.get() * 1000L)
            return true;

        Player caster = getLevel().getServer().getPlayerList().getPlayer(getCasterUUID());
        caster.displayClientMessage(Component.literal("The moon is not ready to be called forth.").withStyle(ChatFormatting.RED), false);
        return false;
    }

}
