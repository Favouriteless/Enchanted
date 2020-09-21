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

    // Do not use these constructors
    public RitualTemplate(double xPos, double yPos, double zPos, UUID caster, UUID target, ServerWorld world) { super(xPos, yPos, zPos, caster, target, world); }
    public RitualTemplate(List<Entity> entitiesNeeded) { ENTITIES_TO_KILL = entitiesNeeded; }

    public RitualTemplate() {
        this.GLYPHS_REQUIRED = new String[] { // 2D representation of chalk circles. X = anything, A = air, W = white chalk, R = red chalk, P = purple chalk, G = gold chalk (must be in center)
                "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X",
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
                "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X"
        };

        ITEMS_REQUIRED.put(Items.DIAMOND, 5); // Add items, count required for ritual

        ENTITIES_REQUIRED = new EntityType<?>[] {  // Add entities required for ritual
                EntityType.PIG
        };
    }

    @Override
    public void Execute(BlockState state, World world, BlockPos pos, UUID casterUUID) {
        // Do ritual effects here
    }
    
    @Override
    protected void onTick() {
        // Do tick based ritual effects here
    }

}
