package net.favouriteless.enchanted.common.circle_magic.rites;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.favouriteless.enchanted.common.Enchanted;
import net.favouriteless.enchanted.common.blocks.entity.GoldChalkBlockEntity;
import net.favouriteless.enchanted.common.circle_magic.RiteManager;
import net.favouriteless.enchanted.common.circle_magic.RiteType;
import net.favouriteless.enchanted.common.init.registry.EItems;
import net.favouriteless.enchanted.common.items.TaglockFilledItem;
import net.minecraft.core.BlockPos;
import net.minecraft.core.UUIDUtil;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.resources.RegistryOps;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.lang.ref.WeakReference;
import java.util.*;

public abstract class Rite {

    protected final RiteType type;
    protected final ServerLevel level;
    protected final BlockPos pos;
    protected final int tickPower;
    private RiteParams params;

    private final Map<UUID, WeakReference<Entity>> entityCache = new HashMap<>();

    protected Rite(BaseRiteParams baseParams, RiteParams params) {
        this.type = baseParams.type;
        this.level = baseParams.level;
        this.pos = baseParams.pos;
        this.tickPower = baseParams.tickPower;
        this.params = params;
    }

    /**
     * Called when a rite using this behaviour first starts executing. Rite will stop if this returns false.
     *
     * @param params The params of this rite (caster, target, items etc.)
     */
    protected boolean onStart(RiteParams params) {
        return true;
    }

    /**
     * Called when a rite using this behaviour first starts executing. Rite will stop if this returns false.
     *
     * @param params The params of this rite (caster, target, items etc.)
     */
    protected boolean onTick(RiteParams params) {
        return false;
    }

    /**
     * Called when a rite using this behaviour first starts executing. Rite will stop if this returns false.
     *
     * @param params The params of this rite (caster, target, items etc.)
     */
    protected void onStop(RiteParams params) {
    }

    protected void saveAdditional(CompoundTag tag, ServerLevel level) {
    }

    protected void loadAdditional(CompoundTag tag, ServerLevel level) {
    }

    /**
     * Refund the items used to start this rite and detatch from chalk.
     */
    protected boolean cancel() {
        level.playSound(null, pos, SoundEvents.NOTE_BLOCK_SNARE.value(), SoundSource.MASTER, 1.0F, 1.0F);
        for (ItemStack stack : params.consumedItems) {
            ItemEntity entity = new ItemEntity(level, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, stack);
            level.addFreshEntity(entity);
        }
        if (level.getBlockEntity(pos) instanceof GoldChalkBlockEntity chalk)
            chalk.detatch();
        return false;
    }

    /**
     * Consume an ItemEntity and add it's ItemStack to this rite's consumed items. Will create particles and sound.
     * Charged attuned stones will just have their charge consumed.
     */
    protected void consumeItem(ItemEntity entity) {
        if(!entity.getItem().is(EItems.ATTUNED_STONE_CHARGED.get())) {
            params.consumedItems.add(entity.getItem());
            entity.discard();
        } else {
            entity.setItem(new ItemStack(EItems.ATTUNED_STONE.get(), entity.getItem().getCount()));
        }

        entity.level().playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.CHICKEN_EGG,
                SoundSource.MASTER, 1.0f, 1.0f);
        ((ServerLevel) entity.level()).sendParticles(ParticleTypes.CLOUD, entity.getX(), entity.getY(), entity.getZ(),
                1, 0, 0, 0, 0);
    }

    /**
     * Perform a check on all players and all levels to find an Entity. The result of this check will be cached.
     */
    protected @Nullable Entity findEntity(UUID uuid) {
        Entity out;

        if(entityCache.containsKey(uuid)) { // Cache our entities first since we're usually trying to grab the same one anyway
            out = entityCache.get(uuid).get();
            if(out != null)
                return out;
            entityCache.remove(uuid);
        }

        out = level.getServer().getPlayerList().getPlayer(uuid);
        if(out != null)
            return cacheAndReturn(uuid, out);

        for (ServerLevel dim : level.getServer().getAllLevels()) {
            out = dim.getEntity(uuid);
            if(out != null)
                return cacheAndReturn(uuid, out);
        }
        return null;
    }

    /**
     * Used to grab the target of this rite's UUID, called directly before
     * {@link Rite#onStart(RiteParams)}. Override this if you want to change it for some reason.
     */
    protected UUID findTargetUUID(ServerLevel level, BlockPos pos, RiteParams params) {
        for(ItemStack stack : params.consumedItems) {
            if(!stack.hasTag())
                continue;

            CompoundTag nbt = stack.getTag();

            if(!nbt.contains(TaglockFilledItem.TARGET_TAG))
                continue;

            return nbt.getUUID(TaglockFilledItem.TARGET_TAG);
        }
        return null;
    }

    /**
     * Spawn random particles around the center of this rite.
     */
    protected void randomParticles(ParticleOptions options) {
        level.sendParticles(options, pos.getX(), pos.getY(), pos.getZ(),
                25, 1.5D, 1.5D, 1.5D, 0.0D);
    }

    // ----------------------------------- NON-API IMPLEMENTATIONS BELOW THIS POINT -----------------------------------

    public boolean tick() {
        if (level.isLoaded(pos)) {
            if (tickPower > 0) {
                if (!(level.getBlockEntity(pos) instanceof GoldChalkBlockEntity chalk) || !chalk.tryConsumePower(tickPower))
                    return stop();
            }

            if (!onTick(params))
                return stop();
        }
        params.ticks++;
        return true;
    }

    public void start() {
        params.target = findTargetUUID(level, pos, params);
        if (!onStart(params)) {
            stop();
            RiteManager.removeRite(level, this);
        }
    }

    public boolean stop() {
        onStop(params);

        if (level.getBlockEntity(pos) instanceof GoldChalkBlockEntity chalk)
            chalk.detatch();
        return false;
    }

    public CompoundTag save() {
        CompoundTag tag = new CompoundTag();
        tag.put("Params", RiteParams.CODEC.encodeStart(RegistryOps.create(NbtOps.INSTANCE, level.registryAccess()), params).getOrThrow(false, Enchanted.LOG::error));
        saveAdditional(tag, level);
        return tag;
    }

    public void load(CompoundTag tag) {
        params = RiteParams.CODEC.parse(RegistryOps.create(NbtOps.INSTANCE, level.registryAccess()), tag.get("Params")).getOrThrow(false, Enchanted.LOG::error);
        loadAdditional(tag, level);
    }

    public RiteType getType() {
        return type;
    }

    public ServerLevel getLevel() {
        return level;
    }

    public BlockPos getPos() {
        return pos;
    }

    private Entity cacheAndReturn(UUID uuid, Entity entity) {
        entityCache.put(uuid, new WeakReference<>(entity));
        return entity;
    }


    public record BaseRiteParams(RiteType type, ServerLevel level, BlockPos pos, int tickPower) {
    }

    public static class RiteParams {

        public static final Codec<RiteParams> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                        UUIDUtil.CODEC.fieldOf("caster").forGetter(p -> p.caster),
                        UUIDUtil.CODEC.optionalFieldOf("target").forGetter(p -> Optional.ofNullable(p.target)),
                        ItemStack.CODEC.listOf().fieldOf("consumed_items").forGetter(p -> p.consumedItems),
                        Codec.INT.fieldOf("ticks").forGetter(p -> p.ticks)
                ).apply(instance, (caster, target, consumedItems, ticks) ->
                        new RiteParams(caster, target.orElse(null), consumedItems, ticks))
        );

        public final List<ItemStack> consumedItems; // Mutable
        public final UUID caster;
        public @Nullable UUID target;

        private int ticks;

        private RiteParams(UUID caster, UUID target, List<ItemStack> consumedItems, int ticks) {
            this.caster = caster;
            this.target = target;
            this.consumedItems = new ArrayList<>(consumedItems); // We make sure consumedItems is mutable.
            this.ticks = ticks;
        }

        public static RiteParams of(UUID caster, List<ItemStack> consumedItems) {
            return new RiteParams(caster, null, consumedItems, 0);
        }

        public static RiteParams empty() {
            return new RiteParams(null, null, List.of(), 0);
        }

        public int ticks() {
            return ticks;
        }

    }

}