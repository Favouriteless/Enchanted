package com.favouriteless.magicraft.init;

import com.favouriteless.magicraft.Magicraft;
import com.favouriteless.magicraft.init.registries.RitualRegistry;
import com.favouriteless.magicraft.rituals.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.*;

public class MagicraftRituals {

    public static final DeferredRegister<AbstractRitual> RITUAL_TYPES = DeferredRegister.create(AbstractRitual.class, Magicraft.MOD_ID);

    public static final RegistryObject<AbstractRitual> RITE_OF_CHARGING_STONE = RITUAL_TYPES.register("rite_of_charging_stone", RiteOfChargingStone::new);
    public static final RegistryObject<AbstractRitual> RITE_OF_TOTAL_ECLIPSE = RITUAL_TYPES.register("rite_of_total_eclipse", RiteOfTotalEclipse::new);

    public static List<AbstractRitual> ACTIVE_RITUALS = new ArrayList<>();
    public static HashMap<Material, String> CHARACTER_MAP = new HashMap<>();

    public static void init() { // Adds ritual types to map
        CHARACTER_MAP.put(MagicraftMaterials.GOLDCHALK, "G");
        CHARACTER_MAP.put(MagicraftMaterials.WHITECHALK, "W");
        CHARACTER_MAP.put(MagicraftMaterials.REDCHALK, "R");
        CHARACTER_MAP.put(MagicraftMaterials.PURPLECHALK, "P");
        CHARACTER_MAP.put(Material.AIR, "A");
    }

    public static CompoundNBT getRitualsTag() {
        CompoundNBT tag = new CompoundNBT();
        int counter = 0;

        for(AbstractRitual ritual : ACTIVE_RITUALS) {
            if(ritual.isExecutingEffect) {
                tag.put(Integer.toString(counter), ritual.getTag());
                counter++;
            }
        }
        return tag;
    }

    public static List<AbstractRitual> forData(String[] glyphs, List<Entity> entities) {
        


        return null;
    }

    public static void loadRitualsTag(CompoundNBT tag, ServerWorld world) {
        for(String key : tag.keySet()) {
            CompoundNBT ritualTag = tag.getCompound(key);

            AbstractRitual ritual = RitualRegistry.INSTANCE.getValue(new ResourceLocation("magicraft", tag.getString("name")));
            ritual.setData(
                    new BlockPos(tag.getDouble("xPos"), tag.getDouble("yPos"), tag.getDouble("zPos")),
                    tag.getUniqueId("casterUUID"),
                    tag.getUniqueId("targetUUID"),
                    world.getServer().getWorld(RegistryKey.getOrCreateKey(Registry.WORLD_KEY, new ResourceLocation(tag.getString("dimensionKey"))))
                    );

            ACTIVE_RITUALS.add(ritual);
        }
    }

}
