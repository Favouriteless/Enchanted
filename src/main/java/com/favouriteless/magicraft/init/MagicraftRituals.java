package com.favouriteless.magicraft.init;

import com.favouriteless.magicraft.Magicraft;
import com.favouriteless.magicraft.rituals.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;

import java.util.*;

public class MagicraftRituals {

    public static final DeferredRegister<Ritual> RITUAL_TYPES = DeferredRegister.create(Ritual.class, Magicraft.MOD_ID);

    public static final RegistryObject<Ritual> RITE_OF_CHARGING_STONE = RITUAL_TYPES.register("rite_of_charging_stone", RiteOfChargingStone::new);
    public static final RegistryObject<Ritual> RITE_OF_TOTAL_ECLIPSE = RITUAL_TYPES.register("rite_of_total_eclipse", RiteOfTotalEclipse::new);

    public static List<Ritual> ACTIVE_RITUALS = new ArrayList<>();
    public static HashMap<Material, String> CHARACTER_MAP = new HashMap<>();

    public static void init() { // Adds ritual types to map
        CHARACTER_MAP.put(MagicraftMaterials.GOLDCHALK, "G");
        CHARACTER_MAP.put(MagicraftMaterials.WHITECHALK, "W");
        CHARACTER_MAP.put(MagicraftMaterials.REDCHALK, "R");
        CHARACTER_MAP.put(MagicraftMaterials.PURPLECHALK, "P");
        CHARACTER_MAP.put(Material.AIR, "A");
    }



    public static CompoundNBT GetRitualsTag() {
        CompoundNBT tag = new CompoundNBT();
        int counter = 0;

        for(Ritual ritual : ACTIVE_RITUALS) {
            if(ritual.isExecutingEffect) {
                tag.put(Integer.toString(counter), ritual.GetTag());
                counter++;
            }
        }
        return tag;
    }



    public static void LoadRitualsTag(CompoundNBT tag, ServerWorld world) {
        for(String key : tag.keySet()) {
            CompoundNBT ritualTag = tag.getCompound(key);

            ACTIVE_RITUALS.add(RITUALS.get(ritualTag.getString("name")).GetRitualFromData(
                    ritualTag.getDouble("xPos"),
                    ritualTag.getDouble("yPos"),
                    ritualTag.getDouble("zPos"),
                    ritualTag.getUniqueId("casterUUID"),
                    ritualTag.getUniqueId("targetUUID"),
                    ritualTag.getString("dimensionKey"),
                    world)
            );

        }

    }


    /*
    public RitualTemplate(double xPos, double yPos, double zPos, UUID caster, UUID target, String dimensionString, ServerWorld serverWorld) {
        ResourceLocation dimensionKey = new ResourceLocation(dimensionString);
        RegistryKey<World> key = RegistryKey.getOrCreateKey(Registry.WORLD_KEY, dimensionKey);
        ServerWorld world = serverWorld.getServer().getWorld(key);

        this(xPos, yPos, zPos, caster, target, world);
    }
    */

}
