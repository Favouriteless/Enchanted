package com.favouriteless.magicraft.rituals;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Items;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import java.util.List;
import java.util.UUID;

public class RitualTemplate extends Ritual {

    // This constructor is for loading entities. Do not use.
    public RitualTemplate(double xPos, double yPos, double zPos, UUID caster, UUID target, ServerWorld world) {
        super(xPos, yPos, zPos, caster, target, world);
    }

    public RitualTemplate(List<Entity> entitiesNeeded) {
        this.ENTITIES_TO_KILL = entitiesNeeded; // Entities needed to start ritual. Includes items. DO NOT TOUCH.

        this.name = "RitualTemplate"; // Change this to the name of the class

        this.GLYPHS = new String[] { // 2D representation of chalk circles. X = anything *except* chalk, A = air, W = white chalk, R = red chalk, P = purple chalk, G = gold chalk (must be in center)
                "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X",
                "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X",
                "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X",
                "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X",
                "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X",
                "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X",
                "X", "X", "X", "X", "X", "X", "X", "G", "X", "X", "X", "X", "X", "X", "X",
                "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X",
                "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X",
                "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X",
                "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X",
                "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X",
                "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X",
                "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X",
                "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X"
        };


        this.ENTITIES.add(EntityType.PIG); // Add entities needed for ritual

        this.ITEMS.put(Items.DIAMOND, 5); // Add items, count needed for ritual

        // NOTE: ANY NEW VARS WILL NOT BE SAVED UPON WORLD UNLOAD. ONLY CREATE TEMP VARS.

    }

    @Override
    public Ritual GetRitual(List<Entity> entitiesNeeded) {
        return new RitualTemplate(entitiesNeeded);
    } // Change RitualTemplate in this method to new class name

    @Override
    public Ritual GetRitualFromData(double xPos, double yPos, double zPos, UUID caster, UUID target, String dimensionString, ServerWorld serverWorld) {
        ResourceLocation dimensionKey = new ResourceLocation(dimensionString);
        RegistryKey<World> key = RegistryKey.getOrCreateKey(Registry.WORLD_KEY, dimensionKey);
        ServerWorld world = serverWorld.getServer().getWorld(key);

        return new RitualTemplate(xPos, yPos, zPos, caster, target, world);
    } // Change RitualTemplate in this method to new class name



    @Override
    public void Execute(BlockState state, World world, BlockPos pos, UUID casterUUID) {
        // Do ritual effects here
        if(!world.isRemote) {
            ((ServerWorld) this.world).spawnParticle(ParticleTypes.WITCH, pos.getX(), pos.getY() + 1, pos.getZ(), 200, 1, 1, 1, 0);
            this.world.playSound(null, pos.getX(), pos.getY() + 1, pos.getZ(), SoundEvents.ENTITY_ENDER_DRAGON_GROWL, SoundCategory.MASTER, 1f, 1f);
        }
        this.inactive = true;
        this.isExecutingEffect = false;
    }
    
    @Override
    protected void onTick() {
        // Do tick based ritual effects here
    }

}
