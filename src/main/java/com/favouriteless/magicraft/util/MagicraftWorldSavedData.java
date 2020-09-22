package com.favouriteless.magicraft.util;

import com.favouriteless.magicraft.Magicraft;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.server.ServerWorld;;
import net.minecraft.world.storage.DimensionSavedDataManager;
import net.minecraft.world.storage.WorldSavedData;

import java.util.function.Supplier;

public class MagicraftWorldSavedData extends WorldSavedData implements Supplier {

    public CompoundNBT data = new CompoundNBT();

    public MagicraftWorldSavedData() { super(Magicraft.MOD_ID); }
    public MagicraftWorldSavedData(String name) { super(name); }

    @Override
    public void read(CompoundNBT nbt) {
        data = nbt.getCompound("magicraft_data");
    }

    @Override
    public CompoundNBT write(CompoundNBT nbt) {
        nbt.put("magicraft_data", data);
        return nbt;
    }

    public static MagicraftWorldSavedData forWorld(ServerWorld world) {

        DimensionSavedDataManager storage = world.getSavedData();
        MagicraftWorldSavedData saveData = storage.getOrCreate(MagicraftWorldSavedData::new, Magicraft.MOD_ID);

        return saveData;
    }

    @Override
    public Object get()
    {
        return this;
    }

}
