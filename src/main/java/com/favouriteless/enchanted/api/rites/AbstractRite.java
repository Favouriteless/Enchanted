/*
 *
 *   Copyright (c) 2022. Favouriteless
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

import com.favouriteless.enchanted.api.altar.AltarPowerHelper;
import com.favouriteless.enchanted.common.init.EnchantedItems;
import com.favouriteless.enchanted.common.blockentities.AltarBlockEntity;
import com.favouriteless.enchanted.common.util.rite.CirclePart;
import com.favouriteless.enchanted.common.util.rite.RiteManager;
import com.favouriteless.enchanted.common.util.rite.RiteType;
import com.favouriteless.enchanted.common.blockentities.ChalkGoldBlockEntity;
import com.mojang.math.Vector3f;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.phys.AABB;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.ChatFormatting;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public abstract class AbstractRite {

    public final HashMap<CirclePart, Block> CIRCLES_REQUIRED = new HashMap<>();
    public final HashMap<EntityType<?>, Integer> ENTITIES_REQUIRED = new HashMap<>();
    public final HashMap<Item, Integer> ITEMS_REQUIRED = new HashMap<>();
    public final int POWER;
    public final int POWER_TICK;

    protected final List<ItemStack> itemsConsumed = new ArrayList<>();

    public ServerLevel level; // World ritual started in
    public BlockPos pos; // Position ritual started at
    public UUID casterUUID; // Player who started ritual
    public UUID targetUUID; // Target of the ritual
    public Entity targetEntity;

    private boolean isStarting = false;
    protected int ticks = 0;
    private boolean isAttached = true;

    private ChalkGoldBlockEntity chalk = null;

    public boolean isRemoved = false;

    public AbstractRite(int power, int powerTick) {
        this.POWER = power;
        this.POWER_TICK = powerTick;
    }

    public CompoundTag save() {
        CompoundTag nbt = new CompoundTag();
        nbt.putString("type", getType().getRegistryName().toString());
        nbt.putString("dimension", level.dimension().location().toString());
        nbt.putInt("x", pos.getX());
        nbt.putInt("y", pos.getY());
        nbt.putInt("z", pos.getZ());
        nbt.putUUID("caster", casterUUID);
        if(targetUUID != null) nbt.putUUID("target", targetUUID);
        nbt.putInt("ticks", ticks);
        nbt.putBoolean("isAttached", isAttached);

        return saveAdditional(nbt);
    }

    public void load(CompoundTag nbt, Level world) {
        setLevel(world.getServer().getLevel(ResourceKey.create(Registry.DIMENSION_REGISTRY, new ResourceLocation(nbt.getString("dimension")))));
        setPos(new BlockPos(nbt.getInt("x"), nbt.getInt("y"), nbt.getInt("z")));
        casterUUID = nbt.getUUID("caster");
        if(nbt.contains("target")) targetUUID = nbt.getUUID("target");
        ticks = nbt.getInt("ticks");
        isAttached = nbt.getBoolean("isAttached");

        loadAdditional(nbt);
    }

    protected boolean tryConsumePower(int amount) {
        if(level != null) {
            if(amount > 0) {
                BlockEntity te = level.getBlockEntity(pos);
                if(te instanceof ChalkGoldBlockEntity) {
                    List<BlockPos> potentialAltars = ((ChalkGoldBlockEntity) te).getAltarPositions();
                    AltarBlockEntity altar = AltarPowerHelper.tryGetAltar(level, potentialAltars);

                    if(altar != null) {
                        if(altar.currentPower >= amount) {
                            altar.currentPower -= amount;
                            return true;
                        }
                    }
                }
            }
            else {
                return true;
            }
        }
        return false;
    }

    /**
     * Override this to save any additional info to the rite's nbt tag
     * @param nbt
     * @return Final nbt tag
     */
    protected CompoundTag saveAdditional(CompoundTag nbt) {
        return nbt;
    }

    /**
     * Override this to load any additional nbt info
     * @param nbt
     */
    protected void loadAdditional(CompoundTag nbt) {

    }

    /**
     * Initial effects of the rite, for example spawning an item or playing a sound
     */
    protected abstract void execute();

    protected boolean checkAdditional() {
        return true;
    }

    /**
     * Run tick based rite effects unrelated to starting up
     */
    protected abstract void onTick();

    public abstract RiteType<?> getType();

    public void tick() {
        if(level != null && !level.isClientSide ) {
            if(isAttached && chalk == null) {
                BlockEntity te = level.getBlockEntity(pos);
                if(te instanceof ChalkGoldBlockEntity) {
                    setChalk((ChalkGoldBlockEntity)te);
                    chalk.setRite(this);
                }
            }


            ticks++;
            if (isStarting) {
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
//                if(tryConsumePower(POWER_TICK))
//                    onTick();
//                else
//                    stopExecuting();
                onTick();
            }
        }
    }

    protected void startExecuting() {
//        if(tryConsumePower(POWER) && checkAdditional()) {
        if(checkAdditional()) {
            this.isStarting = false;
            execute();
        }
        else {
            cancel();
        }
    }

    protected void detatchFromChalk() {
        BlockEntity te = level.getBlockEntity(pos);
        if(te instanceof ChalkGoldBlockEntity chalk) {
            if(chalk.getRite() == this)
                chalk.clearRite();
        }
        isAttached = false;
    }

    /**
     * Call this when the rite is finished
     */
    public void stopExecuting() {
        detatchFromChalk();
        this.isStarting = false;
        BlockEntity te = level.getBlockEntity(pos);
        if(te instanceof ChalkGoldBlockEntity) {
            ((ChalkGoldBlockEntity)te).clearRite();
        }
        RiteManager.removeRite(this);
    }

    public void cancel() {
        isStarting = false;
        RiteManager.removeRite(this);

        while(!itemsConsumed.isEmpty()) {
            ItemStack stack = itemsConsumed.get(0);
            ItemEntity entity = new ItemEntity(level, pos.getX()+0.5D, pos.getY()+0.5D, pos.getZ()+0.5D, stack);
            level.addFreshEntity(entity);
            itemsConsumed.remove(stack);
        }

        level.playSound(null, pos, SoundEvents.NOTE_BLOCK_SNARE, SoundSource.MASTER, 1.0F, 1.0F);

        Player player = level.getPlayerByUUID(casterUUID);
        if(player != null) player.displayClientMessage(new TextComponent("Rite failed.").withStyle(ChatFormatting.RED), false);

        for(int i = 0; i < 25; i++) {
            double dx = pos.getX() + Math.random();
            double dy = pos.getY() + Math.random();
            double dz = pos.getZ() + Math.random();
            level.sendParticles(new DustParticleOptions(new Vector3f(254 / 255F, 94 / 255F, 94 / 255F), 1.0F), dx, dy, dz, 1, 0.0F, 0.0F, 0.0F, 0.0F);
        }

        BlockEntity be = level.getBlockEntity(pos);
        if(be instanceof ChalkGoldBlockEntity chalk) {
            chalk.clearRite();
        }
    }

    private void consumeItem(ItemEntity entity) {
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

            entity.setNeverPickUp();
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
            targetEntity = getTargetEntity();
        }

        level.playSound(null, entity.blockPosition(), SoundEvents.CHICKEN_EGG, SoundSource.MASTER, 1.0F, 1.0F);
        for(int i = 0; i < 5; i++) {
            double dx = entity.position().x - 0.15D + (Math.random() * 0.3D);
            double dy = entity.position().y + (Math.random() * 0.3D);
            double dz = entity.position().z - 0.15D + (Math.random() * 0.3D);
            level.sendParticles(ParticleTypes.SMOKE, dx, dy, dz, 1, 0.0D, 0.0D, 0.0D, 0.0D);
        }
    }

    protected Entity getTargetEntity() {
        Entity target = level.getServer().getPlayerList().getPlayer(targetUUID);

        if(target != null)
            return target;
        else {
            for(ServerLevel serverWorld : level.getServer().getAllLevels()) {
                target = serverWorld.getEntity(targetUUID);
                if(target != null) return target;
            }
        }
        return null;
    }

    private void consumeEntity(Entity entity) {
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

    public void setLevel(ServerLevel level) {
        this.level = level;
    }

    public void setPos(BlockPos pos) {
        this.pos = pos;
    }

    public void setCaster(Player player) {
        this.casterUUID = player.getUUID();
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

    public boolean getStarting() {
        return isStarting;
    }

    public void setChalk(ChalkGoldBlockEntity chalk) {
        this.chalk = chalk;
    }

    public boolean hasCircle(CirclePart part, Block block) {
        return CIRCLES_REQUIRED.containsKey(part) && CIRCLES_REQUIRED.get(part) == block;
    }

}
