package com.favouriteless.magicraft.rituals;

import com.favouriteless.magicraft.init.MagicraftRituals;
import net.minecraft.block.BlockState;

import net.minecraft.client.renderer.texture.ITickable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import java.util.*;

public abstract class Ritual implements ITickable {

    private static final Set<String> chalkCharacters = new HashSet<String>(Arrays.asList("G","W","R","P"));
    public String[] GLYPHS;
    public List<EntityType<?>> ENTITIES = new ArrayList<EntityType<?>>();
    public HashMap<Item, Integer> ITEMS = new HashMap<Item, Integer>();
    public String name;

    public boolean isExecutingEffect;


    protected List<Entity> ENTITIES_TO_KILL;
    protected boolean activating = false; // True if currently killing entities
    protected int ticks = 0;
    public boolean inactive = false; // Removed from ACTIVE_RITUALS next tick if true

    public BlockState state; // Blockstate ritual started at
    public World world; // World ritual started in
    public BlockPos pos; // Position ritual started at
    protected UUID casterUUID; // Player who started ritual
    public UUID targetUUID; // Target of the ritual

    public Ritual() { this.name = "Ritual"; }

    public Ritual(double xPos, double yPos, double zPos, UUID caster, UUID target, World world) {
        this();
        this.pos = new BlockPos(xPos, yPos, zPos);
        this.world = world;
        this.state = this.world.getBlockState(this.pos);
        this.casterUUID = caster;
        this.targetUUID = target;
        this.isExecutingEffect = true;
    }

    public abstract void Execute(BlockState state, World world, BlockPos pos, UUID casterUUID);
    public abstract Ritual GetRitual(List<Entity> entitiesNeeded);
    public abstract Ritual GetRitualFromData(double xPos, double yPos, double zPos, UUID caster, UUID target, String dimensionKey, ServerWorld world);

    public CompoundNBT GetTag() {
        CompoundNBT tag = new CompoundNBT();

        tag.putString("name", this.name);

        tag.putString("dimensionKey", this.world.getDimensionKey().getRegistryName().toString());

        tag.putDouble("xPos", this.pos.getX());
        tag.putDouble("yPos", this.pos.getY());
        tag.putDouble("zPos", this.pos.getZ());

        tag.putUniqueId("casterUUID", this.casterUUID);

        if(this.targetUUID != null) {
            tag.putUniqueId("targetUUID", this.targetUUID);
        }

        return tag;
    }

    public void StartRitual(BlockState state, World world, BlockPos pos, PlayerEntity player) {
        MagicraftRituals.ACTIVE_RITUALS.add(this);
        this.activating = true;
        this.state = state;
        this.world = world;
        this.pos = pos;
        this.casterUUID = player.getUniqueID();
    }

    @Override
    public void tick() { // Tick based activation stuff, ie items disappearing, entities dying, particles etc
        if(this.activating) {
            this.ticks++;

            if(this.ticks == 20) {
                this.ticks = 0;

                if(!world.isRemote()) {
                    if (!this.ENTITIES_TO_KILL.isEmpty()) {
                        Entity entity = this.ENTITIES_TO_KILL.get(0);

                        if(entity.isAlive()) {

                            ((ServerWorld) this.world).spawnParticle(ParticleTypes.POOF, entity.getPosX(), entity.getPosY(), entity.getPosZ(), 10, 0.3, 0.3, 0.3, 0);

                            if (entity instanceof LivingEntity) {
                                entity.attackEntityFrom(DamageSource.MAGIC, 1000);
                            } else {
                                this.world.playSound(null, entity.getPosX(), entity.getPosY(), entity.getPosZ(), SoundEvents.ENTITY_CHICKEN_EGG, SoundCategory.MASTER, 1f, 1f);
                                entity.remove();
                            }

                            this.ENTITIES_TO_KILL.remove(entity);
                        }
                        else {
                            this.activating = false;
                        }
                    } else {
                        this.Execute(state, world, pos, casterUUID);
                        this.activating = false;
                    }
                }

            }

        }
        if(this.isExecutingEffect) {
            this.onTick();
        }
    }

    protected abstract void onTick();

    public boolean CheckGlyphs(String[] glyphsIn) { // Checks if the blocks in 15x15 area centered on gold chalk are correct for the ritual

        for (int i = 0; i < this.GLYPHS.length; i++) {
            if (!this.GLYPHS[i].equals("X")) {
                if (!this.GLYPHS[i].equals(glyphsIn[i])) {
                    return false;
                }
            }

        }
        return true;
    }

    public List<Entity> GetEntities(List<Entity> ritualEntitiesIn) { // Checks if entities within area are correct for ritual
        HashMap<Item, Integer> targetItems = new HashMap<>(this.ITEMS);

        List<Entity> ritualEntities = new ArrayList<>(ritualEntitiesIn);
        List<Entity> out = new ArrayList<>();

        boolean hasEntity = false;

        // ENTITIES
        for (EntityType<?> entityType : this.ENTITIES) {
            hasEntity = false;
            for (Entity entity : ritualEntities) {
                if (entity.getType() == entityType) {
                    hasEntity = true;
                    out.add(entity);
                    ritualEntities.remove(entity);
                    break;
                }
            }
            if (!hasEntity) {
                return null;
            }
        }

        // ITEMS
        for (Item item : targetItems.keySet()) {
            hasEntity = false;
            List<Entity> toRemove = new ArrayList<>();

            for (Entity entity : ritualEntities) {
                if (entity.getType() == EntityType.ITEM) {
                    ItemEntity itemEntity = (ItemEntity) entity;

                    if (itemEntity.getItem().getItem() == item) {
                        out.add(entity);

                        if (itemEntity.getItem().getCount() >= targetItems.get(item)) {
                            hasEntity = true;
                        }
                        else { targetItems.put(item, targetItems.get(item) - itemEntity.getItem().getCount()); }

                        toRemove.add(entity);
                        if (hasEntity) { break; }
                    }
                }
            }
            if (!hasEntity) { return null; }
            ritualEntities.removeAll(toRemove);

        }
        return out;
    }

}
