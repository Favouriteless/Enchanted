package net.favouriteless.enchanted.common.blocks.entity;

import net.favouriteless.enchanted.api.altar.PowerConsumer;
import net.favouriteless.enchanted.api.altar.PowerHelper;
import net.favouriteless.enchanted.api.altar.PowerProvider;
import net.favouriteless.enchanted.api.altar.SimplePowerPosHolder;
import net.favouriteless.enchanted.common.ServerConfig;
import net.favouriteless.enchanted.common.enchanted.circle_magic.RiteManager;
import net.favouriteless.enchanted.common.enchanted.circle_magic.RiteType;
import net.favouriteless.enchanted.common.enchanted.circle_magic.rites.Rite;
import net.favouriteless.enchanted.common.init.EData;
import net.favouriteless.enchanted.common.init.EItems;
import net.favouriteless.enchanted.common.util.ItemUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class GoldChalkBlockEntity extends BlockEntity implements PowerConsumer {

    private final SimplePowerPosHolder posHolder;

    private boolean isExecuting;

    private boolean isInitialising = false;

    private RiteType type;
    private List<ItemStack> itemsToConsume;
    private List<EntityType<?>> entitiesToConsume;
    private UUID caster;
    private List<ItemStack> itemsConsumed = new ArrayList<>();

    public GoldChalkBlockEntity(BlockPos pos, BlockState state) {
        super(EBlockEntityTypes.GOLD_CHALK.get(), pos, state);
        this.posHolder = new SimplePowerPosHolder(pos);
    }

    public void use(Level level, BlockPos pos, Player player) {
        if (!level.isClientSide) {
            if (isExecuting) {
                Rite rite = RiteManager.getRiteAt((ServerLevel) level, worldPosition);
                if (rite != null) {
                    rite.stop();
                    RiteManager.removeRite((ServerLevel) level, rite);
                } else {
                    detatch();
                }
            } else if (!isInitialising) {
                type = RiteType.getFirstMatching(level, pos);
                if (type != null) {

                    ResourceLocation key = level.registryAccess().registryOrThrow(EData.RITE_TYPES_REGISTRY).getKey(type);
                    if (key == null || ServerConfig.INSTANCE.disabledRites.get().contains(key.toString())) {
                        player.displayClientMessage(Component.literal("This rite has been disabled in the config.").withStyle(ChatFormatting.RED), false);
                        level.playSound(null, worldPosition, SoundEvents.NOTE_BLOCK_SNARE.value(), SoundSource.MASTER, 1.0f, 1.0f);
                        type = null;
                        return;
                    }

                    itemsToConsume = type.getItems();
                    entitiesToConsume = type.getEntities();
                    caster = player.getUUID();
                    isInitialising = true;
                } else {
                    level.playSound(null, worldPosition, SoundEvents.NOTE_BLOCK_SNARE.value(), SoundSource.MASTER, 1.0f, 1.0f);
                }
            }
        }
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, GoldChalkBlockEntity be) {
        if (!be.isInitialising) {
            if (be.isExecuting) {
                be.createExecutingParticles();
            }
        } else {
            be.createExecutingParticles();
            if (level.getGameTime() % 20 != 0) {
                return;
            }

            be.itemsToConsume.removeIf(ItemStack::isEmpty);
            if (!be.isDoneConsuming()) {
                if (!be.tryConsumeNext() && !be.isDoneConsuming()) {
                    be.cancel();
                }
            } else {
                PowerProvider provider = PowerHelper.tryGetProvider(level, be.posHolder);
                int power = be.type.getPower();

                if (power == 0 || (provider != null && provider.tryConsume(power))) {
                    be.isExecuting = true;
                    be.isInitialising = false;
                    Rite rite = be.type.create((ServerLevel) level, be.worldPosition, be.caster, be.itemsConsumed);
                    be.itemsConsumed = new ArrayList<>();
                    RiteManager.addRite((ServerLevel) level, rite);
                    rite.start();
                } else {
                    be.cancel();
                }
            }
        }
        setChanged(level, pos, state);
    }

    protected void createExecutingParticles() {
        double dx = worldPosition.getX() + Math.random();
        double dy = worldPosition.getY() + Math.random() * 0.3D;
        double dz = worldPosition.getZ() + Math.random();
        ((ServerLevel) level).sendParticles(
                new DustParticleOptions(
                        new Vector3f(254 / 255f, 94 / 255f, 94 / 255f), 1.0F),
                dx, dy, dz, 1,
                0.0D, 0.0D, 0.0D, 0.0D
        );
    }

    protected boolean isDoneConsuming() {
        return itemsToConsume.isEmpty() && entitiesToConsume.isEmpty();
    }

    public void detatch() {
        isExecuting = false;
        type = null;
        isInitialising = false;
    }

    protected void cancel() {
        isExecuting = false;
        isInitialising = false;
        type = null;
        itemsToConsume = null;
        entitiesToConsume = null;

        if (!level.isClientSide) {
            level.playSound(null, worldPosition, SoundEvents.NOTE_BLOCK_SNARE.value(), SoundSource.MASTER, 1.0f, 1.0f);
            for (ItemStack stack : itemsConsumed) {
                ItemEntity entity = new ItemEntity(level, worldPosition.getX() + 0.5d, worldPosition.getY() + 0.5d, worldPosition.getZ() + 0.5d, stack);
                level.addFreshEntity(entity);
            }
        }
        itemsConsumed.clear();
    }

    protected boolean tryConsumeNext() {
        if (tryConsumeItem()) {
            return true;
        }
        return tryConsumeEntity();
    }

    protected boolean tryConsumeItem() {
        List<ItemEntity> entities = level.getEntitiesOfClass(
                ItemEntity.class, new AABB(
                        worldPosition.getX() - 3, worldPosition.getY(), worldPosition.getZ() - 3,
                        worldPosition.getX() + 4, worldPosition.getY() + 1, worldPosition.getZ() + 4
                )
        );

        for (ItemEntity entity : entities) {
            ItemStack item = entity.getItem();

            for (ItemStack required : itemsToConsume) {
                if (!ItemUtils.isSameItemPartial(item, required)) {
                    continue;
                }

                createConsumeEffect(entity);
                int toConsume = Math.min(required.getCount(), item.getCount());

                if (!entity.getItem().is(EItems.ATTUNED_STONE_CHARGED.get())) {

                    ItemStack copy = item.copy();
                    copy.setCount(toConsume);
                    itemsConsumed.add(copy);

                    item.shrink(toConsume);
                    if (item.isEmpty()) {
                        entity.discard();
                    }
                } else {
                    entity.setItem(new ItemStack(EItems.ATTUNED_STONE.get(), toConsume));
                }
                required.shrink(toConsume);
                return true;
            }
        }

        return false;
    }

    protected boolean tryConsumeEntity() {
        List<Entity> entities = level.getEntities(
                null, new AABB(
                        worldPosition.getX() - 3, worldPosition.getY(), worldPosition.getZ() - 3,
                        worldPosition.getX() + 4, worldPosition.getY() + 1, worldPosition.getZ() + 4
                )
        );

        for (Entity entity : entities) {
            if (entitiesToConsume.remove(entity.getType())) {
                entity.kill();
                return true;
            }
        }

        return false;
    }

    protected void createConsumeEffect(Entity entity) {
        if (!level.isClientSide) {
            level.playSound(
                    null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.CHICKEN_EGG,
                    SoundSource.MASTER, 1.0f, 1.0f
            );

            ((ServerLevel) level).sendParticles(
                    ParticleTypes.CLOUD, entity.getX(), entity.getY(), entity.getZ(), 1,
                    0, 0, 0, 0
            );
        }
    }

    @Override
    public void saveAdditional(CompoundTag tag, Provider registries) {
        tag.put("posHolder", posHolder.serialize());
        tag.putBoolean("isExecuting", isExecuting);
    }

    @Override
    public void loadAdditional(CompoundTag tag, Provider registries) {
        posHolder.deserialize(tag.getCompound("posHolder"));
        isExecuting = tag.getBoolean("isExecuting");
    }

    public boolean tryConsumePower(int amount) {
        PowerProvider provider = PowerHelper.tryGetProvider(level, posHolder);
        return amount == 0 || provider != null && provider.tryConsume(amount);
    }

    @Override
    public @NotNull PowerConsumer.PowerPosHolder getPosHolder() {
        return posHolder;
    }
}
