package com.favouriteless.magicraft.rituals;

import com.favouriteless.magicraft.init.MagicraftItems;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import java.util.List;
import java.util.UUID;

public class MeguminRitual extends Ritual {

    public MeguminRitual(double xPos, double yPos, double zPos, UUID caster, UUID target, int dimensionId, ServerWorld world) {
        super(xPos, yPos, zPos, caster, target, dimensionId, world);
    }

    public MeguminRitual(List<Entity> entitiesNeeded) {
        this.ENTITIES_TO_KILL = entitiesNeeded;
        this.name = "MeguminRitual";

        this.GLYPHS = new String[] {
                "X", "X", "X", "X", "P", "P", "P", "P", "P", "P", "P", "X", "X", "X", "X",
                "X", "X", "X", "P", "A", "A", "A", "A", "A", "A", "A", "P", "X", "X", "X",
                "X", "X", "P", "A", "A", "R", "R", "R", "R", "R", "A", "A", "P", "X", "X",
                "X", "P", "A", "A", "R", "A", "A", "A", "A", "A", "R", "A", "A", "P", "X",
                "P", "A", "A", "R", "A", "A", "W", "W", "W", "A", "A", "R", "A", "A", "P",
                "P", "A", "R", "A", "A", "W", "A", "A", "A", "W", "A", "A", "R", "A", "P",
                "P", "A", "R", "A", "W", "A", "A", "A", "A", "A", "W", "A", "R", "A", "P",
                "P", "A", "R", "A", "W", "A", "A", "G", "A", "A", "W", "A", "R", "A", "P",
                "P", "A", "R", "A", "W", "A", "A", "A", "A", "A", "W", "A", "R", "A", "P",
                "P", "A", "R", "A", "A", "W", "A", "A", "A", "W", "A", "A", "R", "A", "P",
                "P", "A", "A", "R", "A", "A", "W", "W", "W", "A", "A", "R", "A", "A", "P",
                "X", "P", "A", "A", "R", "A", "A", "A", "A", "A", "R", "A", "A", "P", "X",
                "X", "X", "P", "A", "A", "R", "R", "R", "R", "R", "A", "A", "P", "X", "X",
                "X", "X", "X", "P", "A", "A", "A", "A", "A", "A", "A", "P", "X", "X", "X",
                "X", "X", "X", "X", "P", "P", "P", "P", "P", "P", "P", "X", "X", "X", "X"
        };

        this.ENTITIES.add(EntityType.PIG);

        this.ITEMS.put(Items.DIAMOND, 5);

    }

    @Override
    public Ritual GetRitual(List<Entity> entitiesNeeded) {
        return new MeguminRitual(entitiesNeeded);
    }

    @Override
    public Ritual GetRitualFromData(double xPos, double yPos, double zPos, UUID caster, UUID target, int dimensionId, ServerWorld world) {
        return new MeguminRitual(xPos, yPos, zPos, caster, target, dimensionId, world);
    }

    @Override
    public void Execute(BlockState state, World world, BlockPos pos, UUID casterUUID)
    {
        if(!world.isRemote) {
            world.addEntity(new ItemEntity(world, pos.getX(), pos.getY() + 1.1, pos.getZ(), new ItemStack(MagicraftItems.MEGUMIN.get(), 1)));
            ((ServerWorld) this.world).spawnParticle(ParticleTypes.WITCH, pos.getX(), pos.getY() + 1, pos.getZ(), 200, 1, 1, 1, 0);
            this.world.playSound(null, pos.getX(), pos.getY() + 1, pos.getZ(), SoundEvents.ENTITY_ENDER_DRAGON_GROWL, SoundCategory.MASTER, 1f, 1f);
        }
        this.inactive = true;
    }


    @Override
    protected void onTick() { }

}
