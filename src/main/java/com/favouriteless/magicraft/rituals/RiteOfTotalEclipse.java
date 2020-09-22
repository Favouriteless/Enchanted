package com.favouriteless.magicraft.rituals;

import com.favouriteless.magicraft.init.MagicraftItems;
import net.minecraft.entity.Entity;
import net.minecraft.item.Items;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;

import java.util.List;
import java.util.UUID;

public class RiteOfTotalEclipse extends AbstractRitual {

    // Do not use this constructor
    public RiteOfTotalEclipse(List<Entity> entitiesNeeded) { ENTITIES_TO_KILL = entitiesNeeded; }

    public RiteOfTotalEclipse() {
        GLYPHS_REQUIRED = new String[] { // 2D representation of chalk circles. X = anything, A = air, W = white chalk, R = red chalk, P = purple chalk, G = gold chalk (must be in center)
                "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X",
                "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X",
                "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X",
                "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X",
                "X", "X", "X", "X", "X", "X", "W", "W", "W", "X", "X", "X", "X", "X", "X",
                "X", "X", "X", "X", "X", "W", "X", "X", "X", "W", "X", "X", "X", "X", "X",
                "X", "X", "X", "X", "W", "X", "X", "X", "X", "X", "W", "X", "X", "X", "X",
                "X", "X", "X", "X", "W", "X", "X", "G", "X", "X", "W", "X", "X", "X", "X",
                "X", "X", "X", "X", "W", "X", "X", "X", "X", "X", "W", "X", "X", "X", "X",
                "X", "X", "X", "X", "X", "W", "X", "X", "X", "W", "X", "X", "X", "X", "X",
                "X", "X", "X", "X", "X", "X", "W", "W", "W", "X", "X", "X", "X", "X", "X",
                "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X",
                "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X",
                "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X",
                "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X"
        };

        ITEMS_REQUIRED.put(MagicraftItems.QUICKLIME.get(), 1);
        ITEMS_REQUIRED.put(Items.STONE_AXE, 1);
    }

    @Override
    public void execute() {
        world.spawnParticle(ParticleTypes.WITCH, pos.getX(), pos.getY() + 1, pos.getZ(), 200, 1, 1, 1, 0);
        world.playSound(null, pos.getX(), pos.getY() + 1, pos.getZ(), SoundEvents.ENTITY_ENDER_DRAGON_GROWL, SoundCategory.MASTER, 1f, 1f);
        world.func_241114_a_((long)14000);

        inactive = true;
        isExecutingEffect = false;
    }

    @Override
    protected void onTick() {
        // Do tick based ritual effects here
    }

}
