package com.favouriteless.magicraft.rituals;

import com.favouriteless.magicraft.init.MagicraftItems;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;

import java.util.List;
import java.util.UUID;

public class RiteOfChargingStone extends Ritual {

    // Do not use these constructors
    public RiteOfChargingStone(double xPos, double yPos, double zPos, UUID caster, UUID target, ServerWorld world) { super(xPos, yPos, zPos, caster, target, world); }
    public RiteOfChargingStone(List<Entity> entitiesNeeded) { ENTITIES_TO_KILL = entitiesNeeded; }

    public RiteOfChargingStone() {
        GLYPHS_REQUIRED = new String[] { // 2D representation of chalk circles. X = anything, A = air, W = white chalk, R = red chalk, P = purple chalk, G = gold chalk (must be in center)
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

        ITEMS_REQUIRED.put(MagicraftItems.ATTUNED_STONE.get(), 1);
        ITEMS_REQUIRED.put(Items.GLOWSTONE_DUST, 1);
        ITEMS_REQUIRED.put(Items.REDSTONE, 1);
        ITEMS_REQUIRED.put(MagicraftItems.WOOD_ASH.get(), 1);
        ITEMS_REQUIRED.put(MagicraftItems.QUICKLIME.get(), 1);
    }

    @Override
    public void Execute(BlockState state, BlockPos pos, UUID casterUUID) {
        world.addEntity(new ItemEntity(world, pos.getX(), pos.getY() + 1.1, pos.getZ(), new ItemStack(MagicraftItems.ATTUNED_STONE_CHARGED.get(), 1)));
        world.spawnParticle(ParticleTypes.WITCH, pos.getX(), pos.getY() + 1, pos.getZ(), 200, 1, 1, 1, 0);
        world.playSound(null, pos.getX(), pos.getY() + 1, pos.getZ(), SoundEvents.ENTITY_ENDER_DRAGON_GROWL, SoundCategory.MASTER, 1f, 1f);

        inactive = true;
    }

    @Override
    protected void onTick() {
        // Do tick based ritual effects here
    }

}