package com.favouriteless.magicraft.init;

import com.favouriteless.magicraft.rituals.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.Item;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.server.ServerWorld;

import java.util.*;

public class MagicraftRituals {

    private static final Set<String> chalkCharacters = new HashSet<String>(Arrays.asList("G","W","R","P"));
    public static HashMap<String, Ritual> RITUALS = new HashMap<>();
    public static List<Ritual> ACTIVE_RITUALS = new ArrayList<>();



    public static void init() { // Adds ritual types to map
        RITUALS.put("RiteOfChargingStone", new RiteOfChargingStone(new ArrayList<Entity>()));
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



    public static Ritual GetRitual(String[] ritualGlyphs, List<Entity> ritualEntities) {

        for(Ritual ritual : RITUALS.values()) {
            if(ritual.CheckGlyphs(ritualGlyphs)) {

                List<Entity> entitiesNeeded = ritual.GetEntities(ritualEntities);

                if(entitiesNeeded != null) { return ritual.GetRitual(entitiesNeeded); }
            }
        }
        return null;
    }

}
