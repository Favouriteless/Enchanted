package com.favouriteless.magicraft.init;

import com.favouriteless.magicraft.rituals.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.server.ServerWorld;

import java.util.*;

public class MagicraftRituals {

    private static final Set<String> chalkCharacters = new HashSet<String>(Arrays.asList("G","W","R","P"));
    public static HashMap<String, Ritual> RITUALS = new HashMap<>();
    public static List<Ritual> ACTIVE_RITUALS = new ArrayList<>();
    public static HashMap<Material, String> CHARACTER_MAP = new HashMap<>();



    public static void init() { // Adds ritual types to map

        CHARACTER_MAP.put(MagicraftMaterials.GOLDCHALK, "G");
        CHARACTER_MAP.put(MagicraftMaterials.WHITECHALK, "W");
        CHARACTER_MAP.put(MagicraftMaterials.REDCHALK, "R");
        CHARACTER_MAP.put(MagicraftMaterials.PURPLECHALK, "P");
        CHARACTER_MAP.put(Material.AIR, "A");

        // THE ORDER IN WHICH THESE ARE PUT IS THE PRIORITY IN WHICH THEY ARE USED. CARE FOR OVERLAPPING REQUIREMENTS IN RITUALS, THE ONE PUT FIRST WILL BE EXECUTED FIRST AND THE SECOND WILL BE UNUSABLE
        RITUALS.put("RiteOfChargingStone", new RiteOfChargingStone(new ArrayList<Entity>()));
        RITUALS.put("RiteOfTotalEclipse", new RiteOfTotalEclipse(new ArrayList<Entity>()));
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



    public static List<Ritual> GetRituals(String[] ritualGlyphs, List<Entity> ritualEntities) {
        List<Ritual> out = new ArrayList<Ritual>();
        for(Ritual ritual : RITUALS.values()) {
            if(ritual.CheckGlyphs(ritualGlyphs)) {

                List<Entity> entitiesNeeded = ritual.GetEntities(ritualEntities);
                ritualEntities.removeAll(entitiesNeeded);

                if(entitiesNeeded != null) {
                    out.add(ritual.GetRitual(entitiesNeeded));
                }
            }
        }

        return out;
    }

}
