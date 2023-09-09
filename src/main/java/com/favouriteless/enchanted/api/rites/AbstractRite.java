/*
 *
 *   Copyright (c) 2023. Favouriteless
 *   Enchanted, a minecraft mod.
 *   GNU GPLv3 License
 *
 *       This file is part of Enchanted.
 *
 *       Enchanted is free software: you can redistribute it and/or modify
 *       it under the terms of the GNU General Public License as published by
 *       the Free Software Foundation, either version 3 of the License, or
 *       (at your option) any later version.
 *
 *       Enchanted is distributed in the hope that it will be useful,
 *       but WITHOUT ANY WARRANTY; without even the implied warranty of
 *       MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *       GNU General Public License for more details.
 *
 *       You should have received a copy of the GNU General Public License
 *       along with Enchanted.  If not, see <https://www.gnu.org/licenses/>.
 *
 *
 */

package com.favouriteless.enchanted.api.rites;

import com.favouriteless.enchanted.api.power.IPowerProvider;
import com.favouriteless.enchanted.api.power.PowerHelper;
import com.favouriteless.enchanted.common.blockentities.ChalkGoldBlockEntity;
import com.favouriteless.enchanted.common.init.EnchantedItems;
import com.favouriteless.enchanted.common.init.registry.RiteTypes;
import com.favouriteless.enchanted.common.items.TaglockFilledItem;
import com.favouriteless.enchanted.common.rites.CirclePart;
import com.favouriteless.enchanted.common.rites.RiteType;
import com.mojang.math.Vector3f;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.registries.DeferredRegister;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * Base class for every Rite (circle magic ritual), containing all of the logic required to detect/find the correct rite
 * as well as handling the creation/destruction of rites.
 *
 * <p>An {@link AbstractRite} needs to have a {@link RiteType}
 * registered for the circles to be able to find it. This registry is located in {@link RiteTypes} but mods trying to
 * implement this should create their own {@link DeferredRegister} for their Rites.</p>
 *
 * <p>Important methods for classes implementing {@link AbstractRite} have been moved towards the top of the class.</p>
 */
public abstract class AbstractRite {

    private final RiteType<?> riteType;

    public final HashMap<CirclePart, Block> CIRCLES_REQUIRED = new HashMap<>();
    public final HashMap<EntityType<?>, Integer> ENTITIES_REQUIRED = new HashMap<>();
    public final HashMap<Item, Integer> ITEMS_REQUIRED = new HashMap<>();
    public final int POWER;
    public final int POWER_TICK;

    protected final List<ItemStack> itemsConsumed = new ArrayList<>();

    private ServerLevel level; // The Level the rite started in/is saved in.
    private BlockPos pos; // The position the rite started at/is saved at.
    private UUID casterUUID; // UUID of the player who started the rite.
    private UUID targetUUID; // The UUID of the rite's target. Only set if the rite inputs contained a filled taglock.
    private Entity targetEntity; // The Entity the rite is targeting. Typically not set.

    private boolean isStarting = false;
    protected long ticks = 0;
    private boolean isAttached = true;

    private ChalkGoldBlockEntity chalk = null;

    public boolean isRemoved = false;

    public AbstractRite(RiteType<?> type, ServerLevel level, BlockPos pos, UUID casterUUID, int power, int powerTick) {
        this.level = level;
        this.pos = pos;
        this.riteType = type;
        this.casterUUID = casterUUID;
        this.POWER = power;
        this.POWER_TICK = powerTick;

        if(level != null && pos != null) {
            if(level.getBlockEntity(pos) instanceof ChalkGoldBlockEntity chalk)
                this.chalk = chalk;
        }
    }

    /**
     * Called after the rite has checked it's conditions in {@link AbstractRite#checkAdditional()} when the rite starts
     * to be executed.
     *
     * <p>Override this to add effects which need to happen instantly such as spawning an item, spawning particles or
     * setting up tick data.</p>
     */
    protected void execute() {}

    /**
     * Handles tick based effects of the Rite which are unrelated to startup or initialisation. Runs immediately after
     * {@link AbstractRite#execute()} and once every tick.
     *
     * <p>Override this to add tick based effects to your rite.</p>
     */
    protected void onTick() {}

    /**
     * Handles behaviour which needs to happen when the rite stops executing, for example removing barrier blocks
     * which were placed. This is the first method called when {@link AbstractRite#stopExecuting()} is called.
     *
     * <p>Override this for cleanup or anything else you need to do when the rite finishes.</p>
     */
    public void onStopExecuting() {}

    /**
     * Stops the rite from continuing to tick and detaches it from it's {@link ChalkGoldBlockEntity} so other rites can
     * be started.
     *
     * <p>IMPORTANT: This needs to be called regardless of if your rite needs to tick or not. Always call this method,
     * even if it's just at the end of {@link AbstractRite#execute()}</p>
     */
    public void stopExecuting() {
        onStopExecuting();
        detatchFromChalk();
        this.isStarting = false;
        this.isRemoved = true;
    }

    /**
     * Cancel this rite and return the items used to start it.
     *
     * <p>Call this if your rite needs to be cancelled and restarted for some reason, for example if the {@link Entity}
     * it was targetting is no longer valid.</p>
     */
    public void cancel() {
        detatchFromChalk();
        isStarting = false;
        this.isRemoved = true;

        while(!itemsConsumed.isEmpty()) {
            ItemStack stack = itemsConsumed.get(0);
            ItemEntity entity = new ItemEntity(level, pos.getX()+0.5D, pos.getY()+0.5D, pos.getZ()+0.5D, stack);
            level.addFreshEntity(entity);
            itemsConsumed.remove(stack);
        }

        level.playSound(null, pos, SoundEvents.NOTE_BLOCK_SNARE, SoundSource.MASTER, 1.0F, 1.0F);

        Player player = level.getServer().getPlayerList().getPlayer(casterUUID);
        if(player != null) player.displayClientMessage(new TextComponent("Rite failed.").withStyle(ChatFormatting.RED), false);

        for(int i = 0; i < 25; i++) {
            double dx = pos.getX() + Math.random();
            double dy = pos.getY() + Math.random();
            double dz = pos.getZ() + Math.random();
            level.sendParticles(new DustParticleOptions(new Vector3f(254 / 255F, 94 / 255F, 94 / 255F), 1.0F), dx, dy, dz, 1, 0.0F, 0.0F, 0.0F, 0.0F);
        }
    }

    /**
     * Override to add additional starting conditions to your rite such as checking if a target {@link Entity} is tamed.
     * If false is returned, the rite will call {@link AbstractRite#cancel()} on itself automatically.
     * @return True if the rite can continue execute, otherwise false.
     */
    protected boolean checkAdditional() {
        return true;
    }

    /**
     * Override this to save any additional info to the rite's nbt tag
     * @param nbt
     * @return Final nbt tag
     */
    protected void saveAdditional(CompoundTag nbt) {}

    /**
     * Override this to load any additional nbt info, return false if loading needs to fail for whatever reason.
     * @param nbt
     */
    protected boolean loadAdditional(CompoundTag nbt, Level level) { return true; }

    /**
     * Attempt to grab the {@link Entity} with a {@link UUID} matching the one specified by targetUUID, which should
     * automatically be set if the rite used a {@link TaglockFilledItem} in it's requirements.
     * @return The entity matching the target's {@link UUID}, or null if none were found.
     */
    protected Entity tryFindTargetEntity() {
        Entity target = level.getServer().getPlayerList().getPlayer(targetUUID);

        if(target != null)
            return target;
        else {
            for(ServerLevel serverWorld : level.getServer().getAllLevels()) {
                target = serverWorld.getEntity(targetUUID);
                if(target != null) {
                    targetEntity = target;
                    return target;
                }
            }
        }
        return null;
    }

    public ServerLevel getLevel() {
        return level;
    }

    public BlockPos getPos() {
        return pos;
    }

    public UUID getCasterUUID() {
        return casterUUID;
    }

    public UUID getTargetUUID() {
        return targetUUID;
    }

    public Entity getTargetEntity() {
        return targetEntity;
    }

    // -------------------------- METHODS BELOW THIS POINT ARE NOT NEEDED UNDER NORMAL CIRCUMSTANCES --------------------------

    public CompoundTag save() {
        CompoundTag nbt = new CompoundTag();
        nbt.putString("type", getType().getRegistryName().toString());
        nbt.putString("dimension", level.dimension().location().toString());
        nbt.putInt("x", pos.getX());
        nbt.putInt("y", pos.getY());
        nbt.putInt("z", pos.getZ());
        nbt.putUUID("caster", casterUUID);
        if(targetUUID != null) nbt.putUUID("target", targetUUID);
        nbt.putLong("ticks", ticks);
        nbt.putBoolean("isAttached", isAttached);

        saveAdditional(nbt);
        return nbt;
    }

    public boolean load(CompoundTag nbt, Level level) {
        this.level = level.getServer().getLevel(ResourceKey.create(Registry.DIMENSION_REGISTRY, new ResourceLocation(nbt.getString("dimension"))));
        this.pos = new BlockPos(nbt.getInt("x"), nbt.getInt("y"), nbt.getInt("z"));
        this.casterUUID = nbt.getUUID("caster");
        this.ticks = nbt.getLong("ticks");
        this.isAttached = nbt.getBoolean("isAttached");

        if(nbt.contains("target"))
            targetUUID = nbt.getUUID("target");

        return loadAdditional(nbt, level);
    }

    protected boolean tryConsumePower(int amount) {
        if(level != null) {
            if(amount > 0) {
                BlockEntity be = level.getBlockEntity(pos);
                if(be instanceof ChalkGoldBlockEntity) {
                    IPowerProvider provider = PowerHelper.tryGetPowerProvider(level, ((ChalkGoldBlockEntity)be).getPosHolder());

                    if(provider != null) {
                        return provider.tryConsumePower(amount);
                    }
                }
            }
            else {
                return true;
            }
        }
        return false;
    }

    public RiteType<?> getType() {
        return this.riteType;
    }

    public void tick() {
        if(level != null) {
            if(isAttached && chalk == null) {
                BlockEntity be = level.getBlockEntity(pos);
                if(be instanceof ChalkGoldBlockEntity chalk) {
                    this.chalk = chalk;
                    chalk.setRite(this);
                }
                else {
                    stopExecuting(); // Stop executing rite if the chalk isn't found.
                }
            }


            ticks++;
            if (isStarting && !isRemoved) {
                if(ticks % 20 == 0) {
                    List<Entity> allEntities = level.getEntities(null, new AABB(pos.offset(-7, 0, -7), pos.offset(7, 1, 7)));

                    boolean hasItem = false;
                    for(Entity entity : allEntities) {
                        if(entity instanceof ItemEntity itemEntity) {
                            if(ITEMS_REQUIRED.containsKey(itemEntity.getItem().getItem())) {
                                consumeItem(itemEntity);
                                hasItem = true;
                                break;
                            }
                        }
                    }

                    boolean hasEntity = false;
                    if(!hasItem) {
                        if(ITEMS_REQUIRED.isEmpty()) {
                            for(Entity entity : allEntities) {
                                if(ENTITIES_REQUIRED.containsKey(entity.getType())) {
                                    hasEntity = true;
                                    consumeEntity(entity);
                                    break;
                                }
                            }
                        }
                        else {
                            cancel();
                            return;
                        }

                        if(!hasEntity) {
                            if(ENTITIES_REQUIRED.isEmpty()) {
                                startExecuting();
                            }
                            else {
                                cancel();
                            }
                        }
                    }
                }
            }
            else if(!isRemoved) {
                if(tryConsumePower(POWER_TICK))
                    onTick();
                else
                    stopExecuting();
            }
        }
    }

    protected void startExecuting() {
        if(tryConsumePower(POWER) && checkAdditional()) {
            this.isStarting = false;
            this.ticks = 0; // Reset ticks to make it less confusing
            execute();
        }
        else {
            cancel();
        }
    }

    protected void detatchFromChalk() {
        BlockEntity be = level.getBlockEntity(pos);
        if(be instanceof ChalkGoldBlockEntity chalk) {
            if(chalk.getRite() == this)
                chalk.clearRite();
        }
        isAttached = false;
    }

    protected void consumeItem(ItemEntity entity) {
        entity.setNeverPickUp();
        ItemStack stack = entity.getItem();
        Item item = stack.getItem();
        int amountNeeded = ITEMS_REQUIRED.get(stack.getItem());

        if(amountNeeded >= stack.getCount()) { // Not enough/perfect
            ITEMS_REQUIRED.put(item, ITEMS_REQUIRED.get(item)-stack.getCount());
            if(ITEMS_REQUIRED.get(item) <= 0) ITEMS_REQUIRED.remove(item); // Remove if all consumed

            if(item != EnchantedItems.ATTUNED_STONE_CHARGED.get())
                itemsConsumed.add(stack);
            else {
                ItemEntity itemEntity = new ItemEntity(level, entity.position().x(), entity.position().y(), entity.position().z(), new ItemStack(EnchantedItems.ATTUNED_STONE.get(), stack.getCount()));
                level.addFreshEntity(itemEntity);
            }

            entity.discard();
        }
        else { // Too much
            ITEMS_REQUIRED.remove(item);
            itemsConsumed.add(new ItemStack(item, amountNeeded, stack.getTag()));
            stack.shrink(amountNeeded);
            entity.setItem(stack);
        }
        if(item == EnchantedItems.TAGLOCK_FILLED.get() && stack.hasTag()) {
            this.targetUUID = stack.getTag().getUUID("entity");
            targetEntity = tryFindTargetEntity();
        }

        playConsumeEffects(entity);
        entity.setDefaultPickUpDelay();
    }

    protected void consumeItemNoRequirement(ItemEntity entity) {
        entity.setNeverPickUp();
        playConsumeEffects(entity);
        itemsConsumed.add(entity.getItem());
        entity.discard();
    }

    protected void playConsumeEffects(ItemEntity entity) {
        level.playSound(null, entity.blockPosition(), SoundEvents.CHICKEN_EGG, SoundSource.MASTER, 1.0F, 1.0F);
        for(int i = 0; i < 5; i++) {
            double dx = entity.position().x - 0.15D + (Math.random() * 0.3D);
            double dy = entity.position().y + (Math.random() * 0.3D);
            double dz = entity.position().z - 0.15D + (Math.random() * 0.3D);
            level.sendParticles(ParticleTypes.SMOKE, dx, dy, dz, 5, 0.0D, 0.0D, 0.0D, 0.0D);
        }
    }

    protected void consumeEntity(Entity entity) {
        int newAmount = ENTITIES_REQUIRED.get(entity.getType())-1;
        if(newAmount > 0) {
            ENTITIES_REQUIRED.put(entity.getType(), newAmount);
        }
        else {
            ENTITIES_REQUIRED.remove(entity.getType());
        }
        entity.discard();

        level.playSound(null, entity.blockPosition(), SoundEvents.CHICKEN_EGG, SoundSource.MASTER, 1.0F, 1.0F);
        for(int i = 0; i < 10; i++) {
            double dx = entity.position().x - (entity.getBbWidth()/2) + (Math.random() * entity.getBbWidth());
            double dy = entity.position().y + (Math.random() * entity.getBbHeight());
            double dz = entity.position().z - (entity.getBbWidth()/2) + (Math.random() * entity.getBbWidth());
            level.sendParticles(ParticleTypes.SMOKE, dx, dy, dz, 1, 0.0D, 0.0D, 0.0D, 0.0D);
        }
    }

    public boolean is(HashMap<CirclePart, Block> circles, HashMap<EntityType<?>, Integer> entities, HashMap<Item, Integer> items) {
        return CIRCLES_REQUIRED.equals(circles) && ENTITIES_REQUIRED.equals(entities) && ITEMS_REQUIRED.equals(items);
    }

    /**
     * Gets the number of extra requirement entities over the rite requirements at a given position
     * @param world
     * @param pos
     * @return No. of extra requirement entities, -1 if not valid.
     */
    public int differenceAt(Level world, BlockPos pos) {
        for(CirclePart circlePart : CIRCLES_REQUIRED.keySet()) {
            if(!circlePart.match(world, pos, CIRCLES_REQUIRED.get(circlePart))) {
                return -1;
            }
        }
        List<Entity> allEntities = world.getEntities(null, new AABB(pos.offset(-7, 0, -7), pos.offset(7, 1, 7)));
        HashMap<Item, Integer> items = new HashMap<>();
        HashMap<EntityType<?>, Integer> entities = new HashMap<>();

        for(Entity entity : allEntities) { // Get items/entities in area
            if(entity instanceof ItemEntity itemEntity) {
                ItemStack itemStack = itemEntity.getItem();
                if(!items.containsKey(itemStack.getItem())) {
                    items.put(itemStack.getItem(), itemStack.getCount());
                }
                else {
                    items.put(itemStack.getItem(), items.get(itemStack.getItem())+itemStack.getCount());
                }
            }
            else {
                if(!entities.containsKey(entity.getType())) {
                    entities.put(entity.getType(), 1);
                }
                else {
                    entities.put(entity.getType(), entities.get(entity.getType()) + 1);
                }
            }
        }

        int diff = 0;
        if(!ITEMS_REQUIRED.isEmpty()) {
            for (Item item : ITEMS_REQUIRED.keySet()) { // Check if enough items
                if (!(items.containsKey(item) && items.get(item) >= ITEMS_REQUIRED.get(item))) return -1;
            }
            for (Item item : items.keySet()) {
                if (!ITEMS_REQUIRED.containsKey(item)) diff += items.get(item);
            }
        }
        if(!ENTITIES_REQUIRED.isEmpty()) {
            for (EntityType<?> type : ENTITIES_REQUIRED.keySet()) { // Check if enough entities
                if (!(entities.containsKey(type) && entities.get(type) >= ENTITIES_REQUIRED.get(type))) return -1;
            }

            for (EntityType<?> type : entities.keySet()) {
                if (!ENTITIES_REQUIRED.containsKey(type)) diff += entities.get(type);
            }
        }

        return diff;
    }

    public void start() {
        this.isStarting = true;
    }

    protected void spawnMagicParticles() {
        for(int i = 0; i < 25; i++) {
            double dx = pos.getX() - 1.0D + Math.random() * 3.0D;
            double dy = pos.getY() + Math.random() * 2.0D;
            double dz = pos.getZ() - 1.0D + Math.random() * 3.0D;
            level.sendParticles(ParticleTypes.WITCH, dx, dy, dz, 1, 0.0D, 0.0D, 0.0D, 0.0D);
        }
    }

    public boolean isStarting() {
        return isStarting;
    }

    public boolean hasCircle(CirclePart part, Block block) {
        return CIRCLES_REQUIRED.containsKey(part) && CIRCLES_REQUIRED.get(part) == block;
    }

    protected void replaceItem(ItemEntity entity, ItemStack... newItems) {
        if(!entity.level.isClientSide) {
            for(ItemStack stack : newItems) {
                ItemEntity newEntity = new ItemEntity(entity.level, entity.position().x(), entity.position().y(), entity.position().z(), stack);
                entity.level.addFreshEntity(newEntity);
            }
            entity.discard();
        }
    }

}
