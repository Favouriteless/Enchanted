package com.favouriteless.magicraft.rituals;

import com.favouriteless.magicraft.init.MagicraftItems;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
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

public class RiteOfChargingStone extends Ritual {

    // This constructor is for loading entities. Do not use.
    public RiteOfChargingStone(double xPos, double yPos, double zPos, UUID caster, UUID target, ServerWorld world) {
        super(xPos, yPos, zPos, caster, target, world);
    }

    public RiteOfChargingStone(List<Entity> entitiesNeeded) {
        this.ENTITIES_TO_KILL = entitiesNeeded; // Entities needed to start ritual. Includes items. DO NOT TOUCH.

        this.name = "RiteOfChargingStone"; // Change this to the name of the class

        this.GLYPHS = new String[] { // 2D representation of chalk circles. X = anything *except* chalk, A = air, W = white chalk, R = red chalk, P = purple chalk, G = gold chalk (must be in center)
                "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X",
                "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X",
                "X", "X", "X", "X", "X", "W", "W", "W", "W", "W", "X", "X", "X", "X", "X",
                "X", "X", "X", "X", "W", "A", "A", "A", "A", "A", "W", "X", "X", "X", "X",
                "X", "X", "X", "W", "A", "A", "W", "W", "W", "A", "A", "W", "X", "X", "X",
                "X", "X", "W", "A", "A", "W", "A", "A", "A", "W", "A", "A", "W", "X", "X",
                "X", "X", "W", "A", "W", "A", "A", "A", "A", "A", "W", "A", "W", "X", "X",
                "X", "X", "W", "A", "W", "A", "A", "G", "A", "A", "W", "A", "W", "X", "X",
                "X", "X", "W", "A", "W", "A", "A", "A", "A", "A", "W", "A", "W", "X", "X",
                "X", "X", "W", "A", "A", "W", "A", "A", "A", "W", "A", "A", "W", "X", "X",
                "X", "X", "X", "W", "A", "A", "W", "W", "W", "A", "A", "W", "X", "X", "X",
                "X", "X", "X", "X", "W", "A", "A", "A", "A", "A", "W", "X", "X", "X", "X",
                "X", "X", "X", "X", "X", "W", "W", "W", "W", "W", "X", "X", "X", "X", "X",
                "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X",
                "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X"
        };

        this.ITEMS.put(MagicraftItems.ATTUNED_STONE.get(), 1);
        this.ITEMS.put(Items.GLOWSTONE_DUST, 1);
        this.ITEMS.put(Items.REDSTONE, 1);
        this.ITEMS.put(MagicraftItems.WOOD_ASH.get(), 1);
        this.ITEMS.put(MagicraftItems.QUICKLIME.get(), 1);

        // NOTE: ANY NEW VARS WILL NOT BE SAVED UPON WORLD UNLOAD. ONLY CREATE TEMP VARS.

    }

    @Override
    public Ritual GetRitual(List<Entity> entitiesNeeded) {
        return new RiteOfChargingStone(entitiesNeeded);
    } // Change RitualTemplate in this method to new class name

    @Override
    public Ritual GetRitualFromData(double xPos, double yPos, double zPos, UUID caster, UUID target, String dimensionString, ServerWorld serverWorld) {
        ResourceLocation dimensionKey = new ResourceLocation(dimensionString);
        RegistryKey<World> key = RegistryKey.getOrCreateKey(Registry.WORLD_KEY, dimensionKey);
        ServerWorld world = serverWorld.getServer().getWorld(key);

        return new RiteOfChargingStone(xPos, yPos, zPos, caster, target, world);
    } // Change RitualTemplate in this method to new class name



    @Override
    public void Execute(BlockState state, World world, BlockPos pos, UUID casterUUID) {
        if(!world.isRemote) {
            world.addEntity(new ItemEntity(world, pos.getX(), pos.getY() + 1.1, pos.getZ(), new ItemStack(MagicraftItems.ATTUNED_STONE_CHARGED.get(), 1)));
            ((ServerWorld) this.world).spawnParticle(ParticleTypes.WITCH, pos.getX(), pos.getY() + 1, pos.getZ(), 200, 1, 1, 1, 0);
            this.world.playSound(null, pos.getX(), pos.getY() + 1, pos.getZ(), SoundEvents.ENTITY_ENDER_DRAGON_GROWL, SoundCategory.MASTER, 1f, 1f);
        }
        this.inactive = true;
    }
    
    @Override
    protected void onTick() {
        // Do tick based ritual effects here
    }

}
