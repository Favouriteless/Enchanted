package net.favouriteless.enchanted.common.blocks.entity;

import net.favouriteless.enchanted.api.EFluidContainer;
import net.favouriteless.enchanted.api.altar.PowerConsumer;
import net.favouriteless.enchanted.api.altar.PowerHelper;
import net.favouriteless.enchanted.api.altar.PowerProvider;
import net.favouriteless.enchanted.api.altar.SimplePowerPosHolder;
import net.favouriteless.enchanted.client.ClientProxy;
import net.favouriteless.enchanted.client.EnchantedClient;
import net.favouriteless.enchanted.client.particles.types.ColourOptions;
import net.favouriteless.enchanted.common.ServerConfig;
import net.favouriteless.enchanted.common.init.EParticleTypes;
import net.favouriteless.enchanted.common.init.ERecipeTypes;
import net.favouriteless.enchanted.common.init.ETags.Blocks;
import net.favouriteless.enchanted.common.recipes.KettleRecipe;
import net.favouriteless.enchanted.common.recipes.recipe_inputs.ListInput;
import net.favouriteless.enchanted.common.util.ColourUtils;
import net.favouriteless.enchanted.common.util.ColourUtils.ARGB;
import net.favouriteless.enchanted.common.util.ContainerUtils;
import net.favouriteless.enchanted.common.util.RandomUtils;
import net.favouriteless.enchanted.platform.EServices;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class KettleBlockEntity extends EBlockEntity implements EFluidContainer, PowerConsumer {

    private static final int WATER_CAPACITY = EServices.FLUID.getBucketCapacity();
    private static final int COOK_DURATION = 160;
    private static final byte MAX_HEAT = 80;
    private static final float BLEND_TIME = 10;

    private static final int WATER_COLOUR = 0x3F76E2;
    private static final int FAIL_COLOUR = 0x96642F;

    private final SimplePowerPosHolder powerHolder;

    // Result is exposed by Capabilities (NeoForge) and Transfer API (Fabric).
    // Ingredients are not exposed by any API.
    @NotNull
    private ItemStack result = ItemStack.EMPTY;
    private final NonNullList<ItemStack> ingredients = NonNullList.create();
    private List<RecipeHolder<KettleRecipe>> recipes = new ArrayList<>();

    private int water = 0;
    private byte heat = 0;
    private int progress;
    private int colour = WATER_COLOUR;

    private boolean isFailed;
    private boolean isComplete;

    private boolean firstTick = true;

    private int oldColour = WATER_COLOUR; // Client only
    private double blendStart = 0; // Client only
    private boolean hasItems = false; // Client only

    public KettleBlockEntity(BlockPos pos, BlockState state) {
        super(EBlockEntityTypes.KETTLE.get(), pos, state);
        this.powerHolder = new SimplePowerPosHolder(pos);
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, KettleBlockEntity be) {
        if (be.firstTick) {
            be.firstTick();
        }
        if (be.isFailed || be.isComplete) {
            return;
        }

        if (!be.providesHeat(level.getBlockState(pos.below())) || be.water != WATER_CAPACITY) {
            boolean update = be.progress > 0 || be.isHot();
            be.progress = 0;
            be.heat = 0;

            if (!be.ingredients.isEmpty()) {
                update = true;
                be.fail();
            }

            if (update) // Only send update if we failed or need to stop boiling.
            {
                be.updateBlock();
            }

            be.setChanged();
            return;
        }

        if (!be.isHot()) {
            be.heat++;
            if (be.isHot()) {
                be.updateBlock();
            }
            be.setChanged();
            return;
        }

        if (be.recipes.size() != 1 || !be.recipes.getFirst().value().fullMatch(ListInput.of(be.ingredients))) {
            return;
        }

        if (be.progress < COOK_DURATION) {
            if (be.progress++ == 0 || be.progress == COOK_DURATION) {
                be.updateBlock(); // Only send update if we just started or finished cooking.
            }
            be.setChanged();
            return;
        }

        KettleRecipe recipe = be.recipes.getFirst().value();
        PowerProvider provider = PowerHelper.tryGetProvider(level, be.powerHolder);

        if (recipe.getPower() == 0 || (provider != null && provider.tryConsume(recipe.getPower()))) {
            be.result = recipe.assemble(ListInput.of(be.ingredients), level.registryAccess());
            be.colour = recipe.getFinalColour();
            be.isComplete = true;
            level.playSound(null, pos, SoundEvents.AMETHYST_BLOCK_CHIME, SoundSource.BLOCKS, 2, 1);
        } else {
            be.isFailed = true;
        }
        be.setChanged();
        be.updateBlock();
    }

    public static void clientTick(Level level, BlockPos pos, BlockState state, KettleBlockEntity be) {
        if (be.firstTick) {
            be.firstTick();
        }

        double waterY = Mth.lerp((float) be.water / WATER_CAPACITY, 0.0625D, 0.3125D);

        if (be.isHot() && RandomUtils.nextFloat() < 0.8F) {
            double dx = pos.getX() + 0.5D + (Math.random() - 0.5D) * 0.375D; // 0.375 is width of water
            double dy = pos.getY() + waterY + 0.025D;
            double dz = pos.getZ() + 0.5D + (Math.random() - 0.5D) * 0.375D;
            level.addParticle(new ColourOptions(EParticleTypes.BOILING.get(), be.colour), dx, dy, dz, 0, 0, 0);
        }

        if (be.isFailed) {
            return;
        }

        if (!be.isComplete && be.progress > 0) {
            double dx = pos.getX() + Math.random();
            double dy = pos.getY() + Math.random();
            double dz = pos.getZ() + Math.random();
            level.addParticle(new ColourOptions(EParticleTypes.KETTLE_COOK.get(), be.colour), dx, dy, dz, 0D, 0D, 0D);
        } else if (be.isHot() && be.hasItems && RandomUtils.nextInt(10) > 6) {
            double xo = 0.5D + (Math.random() - 0.5D) * 0.375D; // 0.375 is width of water
            double zo = 0.5D + (Math.random() - 0.5D) * 0.375D;
            double dx = pos.getX() + xo;
            double dy = pos.getY() + waterY;
            double dz = pos.getZ() + zo;
            Vec3 velocity = new Vec3(xo, 0, zo).subtract(0.5D, 0.0D, 0.5D).normalize().scale((1D + Math.random()) * 0.06D);
            level.addParticle(new ColourOptions(EParticleTypes.CAULDRON_BREW.get(), be.colour), dx, dy, dz, velocity.x, (1.0D + Math.random()) * 0.06D, velocity.z);
        }
    }

    /**
     * Add an ingredient to the kettle and refresh recipes. This is not exposed via Capabilities or the Transfer API.
     * Ingredients are considered voided once added, but stored for recipe matching
     *
     * @return true if the ItemStack was added, otherwise false.
     */
    public boolean addItem(ItemStack stack) {
        if (isComplete || isFailed || !isWaterFull() || !isHot()) {
            return false;
        }

        ingredients.add(stack);
        updateRecipes();

        if (recipes.isEmpty()) {
            if (ServerConfig.INSTANCE.kettleItemSpoil.get()) {
                fail();
            } else {
                ingredients.remove(stack);
                updateRecipes();
            }
        } else {
            colour = recipes.getFirst().value().getCookColour();
        }

        setChanged();
        updateBlock();
        return true;
    }

    /**
     * Remove an stack from the result stack and check if the kettle should be reset or updated.
     *
     * @param simulate If true, an output will be simulated without any changes.
     *
     * @return The {@link ItemStack} removed from the kettle.
     */
    public ItemStack takeItem(boolean simulate) {
        if (result.isEmpty()) {
            return ItemStack.EMPTY;
        }

        ItemStack out = takeItemNoUpdate(simulate);
        if (!simulate) {
            checkResultEmpty();
        }
        return out;
    }

    /**
     * Remove an stack from the result stack WITHOUT checking if the kettle needs to be reset or updated. Used by the
     * storage wrapper on Fabric.
     *
     * @param simulate If true, an output will be simulated without any changes.
     *
     * @return The {@link ItemStack} removed from the kettle.
     */
    public ItemStack takeItemNoUpdate(boolean simulate) {
        if (isFailed || !isComplete || result.isEmpty()) {
            return ItemStack.EMPTY;
        }

        if (simulate) {
            return result.copyWithCount(1);
        }

        water -= water / result.getCount() + 1;
        if (water < 0) {
            water = 0;
        }

        return result.split(1);
    }

    public void checkResultEmpty() {
        if (result.isEmpty()) {
            resetValues();
        }
        setChanged();
        updateBlock();
    }

    @Override
    public int drain(int amount, boolean simulate) {
        int drained = Math.min(water, amount);
        if (simulate) {
            return drained;
        }

        water -= drained;

        if (drained == 0) {
            return drained;
        }

        boolean update = false;
        if (water == 0) {
            resetValues();
            update = true;
        } else if (isFailed || isComplete || progress > 0 || !ingredients.isEmpty()) {
            fail();
            update = true;
        }

        if (update) {
            updateBlock();
        }
        return drained;
    }

    @Override
    public int fill(int amount, boolean simulate) {
        if (isComplete || isFailed) {
            return 0;
        }

        int added = Math.min(amount, WATER_CAPACITY - water);
        if (simulate) {
            return added;
        }
        water += added;
        if (added != 0) {
            setChanged();
            updateBlock();
        }
        return added;
    }

    private void firstTick() {
        firstTick = false;
        if (!level.isClientSide) {
            updateRecipes();
            updateBlock();
        } else {
            ClientProxy.startBubblingSound(this);
        }
    }

    private void updateRecipes() {
        if (level == null) {
            return;
        }

        if (recipes.isEmpty()) {
            recipes = level.getRecipeManager().getRecipesFor(ERecipeTypes.KETTLE.get(), ListInput.of(ingredients), level);
        } else {
            recipes.removeIf(h -> !h.value().matches(ListInput.of(ingredients), level));
        }
    }

    private void fail() {
        isFailed = true;
        isComplete = false;
        colour = FAIL_COLOUR;
        result = ItemStack.EMPTY;
        ingredients.clear();

        if (level != null && !level.isClientSide) {
            level.playSound(null, worldPosition, SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.BLOCKS, 1.0F, 1.0F);
        }
    }

    /**
     * Reset this kettle's state to as if it were new.
     */
    private void resetValues() {
        isComplete = false;
        isFailed = false;
        progress = 0;
        water = 0;
        heat = 0;
        colour = WATER_COLOUR;
        result = ItemStack.EMPTY;
        ingredients.clear();
        recipes.clear();
        setChanged();
    }

    public boolean isWaterFull() {
        return water == WATER_CAPACITY;
    }

    public boolean isHot() {
        return heat >= MAX_HEAT;
    }

    public boolean providesHeat(BlockState state) {
        return state.is(Blocks.HEAT_SOURCES) && (!state.getValues().containsKey(BlockStateProperties.LIT) || state.getValue(BlockStateProperties.LIT));
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag, @NotNull Provider registries) {
        super.saveAdditional(tag, registries);
        ContainerUtils.saveAllItems(tag, ingredients, registries);
        if (!result.isEmpty()) {
            tag.put("result", result.save(registries));
        }
        tag.put("powerHolder", powerHolder.serialize());
    }

    @Override
    protected void saveSynced(CompoundTag tag, Provider registries) {
        tag.putInt("water", water);
        tag.putByte("heat", heat);
        tag.putBoolean("isFailed", isFailed);
        tag.putBoolean("isComplete", isComplete);
        tag.putInt("colour", colour);
        tag.putInt("progress", progress);
        tag.putBoolean("hasItems", !ingredients.isEmpty());
    }

    @Override
    public void loadAdditional(@NotNull CompoundTag tag, @NotNull Provider registries) {
        super.loadAdditional(tag, registries);
        ContainerUtils.loadAllItems(tag, ingredients, registries); // We have to use this version because it's a dynamically sized inventory.
        result = ItemStack.parseOptional(registries, tag.getCompound("result"));

        if (tag.contains("powerHolder")) {
            powerHolder.deserialize(tag.getCompound("powerHolder"));
        }
    }

    @Override
    protected void loadSynced(CompoundTag tag, Provider registries) {
        water = tag.getInt("water");
        heat = tag.getByte("heat");
        isFailed = tag.getBoolean("isFailed");
        isComplete = tag.getBoolean("isComplete");
        progress = tag.getInt("progress");
        int c = tag.getInt("colour");

        if (level != null && level.isClientSide && oldColour != c) {
            oldColour = colour;
            blendStart = EnchantedClient.getGameTime();
            hasItems = tag.getBoolean("hasItems");
        }

        colour = c;
    }

    public ItemStack getResult() {
        return result;
    }

    public void setResult(ItemStack result) {
        this.result = result;
    }

    public boolean isComplete() {
        return isComplete;
    }

    public boolean isFailed() {
        return isFailed;
    }

    public ARGB getColour(double currentTime) {
        float f = (float) Math.min((currentTime - blendStart) / BLEND_TIME, 1);
        ARGB s = ColourUtils.intToARGB(oldColour);
        ARGB e = ColourUtils.intToARGB(colour);
        return new ARGB(
                160,
                (int) Mth.lerp(f, s.r(), e.r()),
                (int) Mth.lerp(f, s.g(), e.g()),
                (int) Mth.lerp(f, s.b(), e.b())
        );
    }

    @Override
    public int getFluidCapacity() {
        return WATER_CAPACITY;
    }

    @Override
    public int getFluidAmount() {
        return water;
    }

    @Override
    public void setFluidAmount(int amount) {
        this.water = amount;
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public PowerPosHolder getPosHolder() {
        return powerHolder;
    }

}
