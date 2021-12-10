/*
 * Copyright (c) 2021. Favouriteless
 * Enchanted, a minecraft mod.
 * GNU GPLv3 License
 *
 *     This file is part of Enchanted.
 *
 *     Enchanted is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Enchanted is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Enchanted.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.favouriteless.enchanted.api.rites;

import com.favouriteless.enchanted.api.altar.AltarPowerHelper;
import com.favouriteless.enchanted.common.init.EnchantedItems;
import com.favouriteless.enchanted.common.rites.util.CirclePart;
import com.favouriteless.enchanted.common.rites.util.RiteManager;
import com.favouriteless.enchanted.common.rites.util.RiteType;
import com.favouriteless.enchanted.common.tileentity.AltarTileEntity;
import com.favouriteless.enchanted.common.tileentity.ChalkGoldTileEntity;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.particles.RedstoneParticleData;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import org.apache.logging.log4j.core.jmx.Server;

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

    public ServerWorld world; // World ritual started in
    public BlockPos pos; // Position ritual started at
    public UUID casterUUID; // Player who started ritual
    public UUID targetUUID; // Target of the ritual
    public Entity targetEntity;

    private boolean isStarting = false;
    protected int ticks = 0;
    private boolean isAttached = true;

    private ChalkGoldTileEntity chalk = null;

    public boolean isRemoved = false;

    public AbstractRite(int power, int powerTick) {
        this.POWER = power;
        this.POWER_TICK = powerTick;
    }

    public CompoundNBT save() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putString("type", getType().getRegistryName().toString());
        nbt.putString("dimension", world.dimension().location().toString());
        nbt.putInt("x", pos.getX());
        nbt.putInt("y", pos.getY());
        nbt.putInt("z", pos.getZ());
        nbt.putUUID("caster", casterUUID);
        if(targetUUID != null) nbt.putUUID("target", targetUUID);
        nbt.putInt("ticks", ticks);
        nbt.putBoolean("isAttached", isAttached);

        return saveAdditional(nbt);
    }

    public void load(CompoundNBT nbt, World world) {
        setWorld(world.getServer().getLevel(RegistryKey.create(Registry.DIMENSION_REGISTRY, new ResourceLocation(nbt.getString("dimension")))));
        setPos(new BlockPos(nbt.getInt("x"), nbt.getInt("y"), nbt.getInt("z")));
        casterUUID = nbt.getUUID("caster");
        if(nbt.contains("target")) targetUUID = nbt.getUUID("target");
        ticks = nbt.getInt("ticks");
        isAttached = nbt.getBoolean("isAttached");

        loadAdditional(nbt);
    }

    protected boolean tryConsumePower(int amount) {
        if(world != null) {
            if(amount > 0) {
                TileEntity te = world.getBlockEntity(pos);
                if(te instanceof ChalkGoldTileEntity) {
                    List<BlockPos> potentialAltars = ((ChalkGoldTileEntity) te).getAltarPositions();
                    AltarTileEntity altar = AltarPowerHelper.tryGetAltar(world, potentialAltars);

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
    protected CompoundNBT saveAdditional(CompoundNBT nbt) {
        return nbt;
    }

    /**
     * Override this to load any additional nbt info
     * @param nbt
     */
    protected void loadAdditional(CompoundNBT nbt) {

    }

    /**
     * Initial effects of the rite, for example spawning an item or playing a sound
     */
    protected abstract void execute();

    /**
     * Run tick based rite effects unrelated to starting up
     */
    protected abstract void onTick();

    public abstract RiteType<?> getType();

    public void tick() {
        if(world != null && !world.isClientSide ) {
            if(isAttached && chalk == null) {
                TileEntity te = world.getBlockEntity(pos);
                if(te instanceof ChalkGoldTileEntity) {
                    setChalk((ChalkGoldTileEntity)te);
                    chalk.setRite(this);
                }
            }


            ticks++;
            if (isStarting) {
                if(ticks % 20 == 0) {
                    List<Entity> allEntities = world.getEntities(null, new AxisAlignedBB(pos.offset(-7, 0, -7), pos.offset(7, 1, 7)));

                    boolean hasItem = false;
                    for(Entity entity : allEntities) {
                        if(entity instanceof ItemEntity) {
                            ItemEntity itemEntity = (ItemEntity) entity;
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
        if(tryConsumePower(POWER)) {
            this.isStarting = false;
            execute();
        }
        else {
            cancel();
        }
    }

    protected void detatchFromChalk() {
        TileEntity te = world.getBlockEntity(pos);
        if(te instanceof ChalkGoldTileEntity) {
            ChalkGoldTileEntity chalk = (ChalkGoldTileEntity)te;
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
        TileEntity te = world.getBlockEntity(pos);
        if(te instanceof ChalkGoldTileEntity) {
            ((ChalkGoldTileEntity)te).clearRite();
        }
        RiteManager.removeRite(this);
    }

    public void cancel() {
        isStarting = false;
        RiteManager.removeRite(this);

        while(!itemsConsumed.isEmpty()) {
            ItemStack stack = itemsConsumed.get(0);
            ItemEntity entity = new ItemEntity(world, pos.getX()+0.5D, pos.getY()+0.5D, pos.getZ()+0.5D, stack);
            world.addFreshEntity(entity);
            itemsConsumed.remove(stack);
        }

        world.playSound(null, pos, SoundEvents.NOTE_BLOCK_SNARE, SoundCategory.MASTER, 1.0F, 1.0F);

        PlayerEntity player = world.getPlayerByUUID(casterUUID);
        if(player != null) player.displayClientMessage(new StringTextComponent("Rite failed.").withStyle(TextFormatting.RED), false);

        for(int i = 0; i < 25; i++) {
            double dx = pos.getX() + Math.random();
            double dy = pos.getY() + Math.random();
            double dz = pos.getZ() + Math.random();
            world.sendParticles(new RedstoneParticleData(254/255F,94/255F,94/255F, 1.0F), dx, dy, dz, 1, 0.0F, 0.0F, 0.0F, 0.0F);
        }

        TileEntity te = world.getBlockEntity(pos);
        if(te instanceof ChalkGoldTileEntity) {
            ((ChalkGoldTileEntity)te).clearRite();
        }
    }

    private void consumeItem(ItemEntity entity) {
        ItemStack stack = entity.getItem();
        Item item = stack.getItem();
        int amountNeeded = ITEMS_REQUIRED.get(stack.getItem());

        if(amountNeeded >= stack.getCount()) { // Not enough/perfect
            ITEMS_REQUIRED.put(item, ITEMS_REQUIRED.get(item)-stack.getCount());
            if(ITEMS_REQUIRED.get(item) <= 0) ITEMS_REQUIRED.remove(item); // Remove if all consumed
            itemsConsumed.add(stack);
            entity.setNeverPickUp();
            entity.remove();
        }
        else { // Too much
            ITEMS_REQUIRED.remove(item);
            itemsConsumed.add(new ItemStack(item, amountNeeded, stack.getTag()));
            stack.shrink(amountNeeded);
            entity.setItem(stack);
        }
        if(item == EnchantedItems.TAGLOCK_FILLED.get() && stack.hasTag()) {
            UUID entityUuid = stack.getTag().getUUID("entity");
            this.targetUUID = entityUuid;
            targetEntity = getTargetEntity();
        }

        world.playSound(null, entity.blockPosition(), SoundEvents.CHICKEN_EGG, SoundCategory.MASTER, 1.0F, 1.0F);
        for(int i = 0; i < 5; i++) {
            double dx = entity.position().x - 0.15D + (Math.random() * 0.3D);
            double dy = entity.position().y + (Math.random() * 0.3D);
            double dz = entity.position().z - 0.15D + (Math.random() * 0.3D);
            world.sendParticles(ParticleTypes.SMOKE, dx, dy, dz, 1, 0.0D, 0.0D, 0.0D, 0.0D);
        }
    }

    protected Entity getTargetEntity() {
        Entity target = world.getServer().getPlayerList().getPlayer(targetUUID);

        if(target != null)
            return target;
        else {
            for(ServerWorld serverWorld : world.getServer().getAllLevels()) {
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
        entity.remove();

        world.playSound(null, entity.blockPosition(), SoundEvents.CHICKEN_EGG, SoundCategory.MASTER, 1.0F, 1.0F);
        for(int i = 0; i < 10; i++) {
            double dx = entity.position().x - (entity.getBbWidth()/2) + (Math.random() * entity.getBbWidth());
            double dy = entity.position().y + (Math.random() * entity.getBbHeight());
            double dz = entity.position().z - (entity.getBbWidth()/2) + (Math.random() * entity.getBbWidth());
            ((ServerWorld)world).sendParticles(ParticleTypes.SMOKE, dx, dy, dz, 1, 0.0D, 0.0D, 0.0D, 0.0D);
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
    public int differenceAt(World world, BlockPos pos) {
        for(CirclePart circlePart : CIRCLES_REQUIRED.keySet()) {
            if(!circlePart.match(world, pos, CIRCLES_REQUIRED.get(circlePart))) {
                return -1;
            }
        }
        List<Entity> allEntities = world.getEntities(null, new AxisAlignedBB(pos.offset(-7, 0, -7), pos.offset(7, 1, 7)));
        HashMap<Item, Integer> items = new HashMap<>();
        HashMap<EntityType<?>, Integer> entities = new HashMap<>();

        for(Entity entity : allEntities) { // Get items/entities in area
            if(entity instanceof ItemEntity) {
                ItemEntity itemEntity = (ItemEntity)entity;
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

    public void setWorld(ServerWorld world) {
        this.world = world;
    }

    public void setPos(BlockPos pos) {
        this.pos = pos;
    }

    public void setCaster(PlayerEntity player) {
        this.casterUUID = player.getUUID();
    }

    public void start() {
        this.isStarting = true;
    }

    protected void spawnParticles() {
        for(int i = 0; i < 25; i++) {
            double dx = pos.getX() - 1.0D + Math.random() * 3.0D;
            double dy = pos.getY() + Math.random() * 2.0D;
            double dz = pos.getZ() - 1.0D + Math.random() * 3.0D;
            ((ServerWorld)world).sendParticles(ParticleTypes.WITCH, dx, dy, dz, 1, 0.0D, 0.0D, 0.0D, 0.0D);
        }
    }

    public boolean getStarting() {
        return isStarting;
    }

    public void setChalk(ChalkGoldTileEntity chalk) {
        this.chalk = chalk;
    }

}
