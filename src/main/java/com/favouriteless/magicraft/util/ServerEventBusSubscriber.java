package com.favouriteless.magicraft.util;

import com.favouriteless.magicraft.init.MagicraftRituals;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.server.FMLServerStoppingEvent;

import java.util.ArrayList;

@Mod.EventBusSubscriber
public class ServerEventBusSubscriber {

    @SubscribeEvent
    public static void OnServerTick(TickEvent.ServerTickEvent event) {

    }



    @SubscribeEvent
    public static void OnWorldLoaded(WorldEvent.Load event) {

        if (!event.getWorld().isRemote() && event.getWorld() instanceof ServerWorld)
        {
            MagicraftWorldSavedData saver = MagicraftWorldSavedData.forWorld((ServerWorld) event.getWorld());

            if(saver.data.contains("MagicraftActiveRituals"))
            {
                MagicraftRituals.loadRitualsTag( (CompoundNBT)saver.data.get("MagicraftActiveRituals"), (ServerWorld)event.getWorld());
            }
        }

    }



    @SubscribeEvent
    public static void OnWorldSave(WorldEvent.Save event) {

        if (!event.getWorld().isRemote() && event.getWorld() instanceof ServerWorld)
        {
            MagicraftWorldSavedData saver = MagicraftWorldSavedData.forWorld((ServerWorld) event.getWorld());

            CompoundNBT nbt = new CompoundNBT();

            nbt.put("MagicraftActiveRituals", MagicraftRituals.getRitualsTag());

            saver.data = nbt;
            saver.markDirty();
        }
    }

    @SubscribeEvent
    public static void OnServerStop(FMLServerStoppingEvent event) {
        MagicraftRituals.ACTIVE_RITUALS = new ArrayList<>();
    }

}
