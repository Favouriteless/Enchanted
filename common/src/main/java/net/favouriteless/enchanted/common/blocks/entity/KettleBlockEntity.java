package net.favouriteless.enchanted.common.blocks.entity;

import net.favouriteless.enchanted.api.power.IPowerConsumer;
import net.favouriteless.enchanted.api.power.IPowerProvider;
import net.favouriteless.enchanted.api.power.PowerHelper;
import net.favouriteless.enchanted.client.ClientProxy;
import net.favouriteless.enchanted.client.EnchantedClient;
import net.favouriteless.enchanted.client.particles.types.ColourOptions;
import net.favouriteless.enchanted.common.ServerConfig;
import net.favouriteless.enchanted.common.altar.SimplePowerPosHolder;
import net.favouriteless.enchanted.common.init.EParticleTypes;
import net.favouriteless.enchanted.common.init.ERecipeTypes;
import net.favouriteless.enchanted.common.init.ETags.Blocks;
import net.favouriteless.enchanted.common.recipes.KettleRecipe;
import net.favouriteless.enchanted.common.recipes.recipe_inputs.ListInput;
import net.favouriteless.enchanted.common.util.ColourUtils;
import net.favouriteless.enchanted.common.util.ColourUtils.AHSV;
import net.favouriteless.enchanted.common.util.ColourUtils.ARGB;
import net.favouriteless.enchanted.common.util.ContainerUtils;
import net.favouriteless.enchanted.common.util.LangUtils;
import net.favouriteless.enchanted.common.util.RandomUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class KettleBlockEntity extends ContainerBlockEntityBase implements IPowerConsumer {

    private static final int COOK_DURATION = 160;
    private static final int WATER_CAPACITY = 1000;
    private static final byte MAX_HEAT = 80;
    private static final float BLEND_TIME = 10;

    private static final int WATER_COLOUR = 0x3F76E2;
    private static final int FAIL_COLOUR = 0x96642F;

    private final List<RecipeHolder<KettleRecipe>> recipes = new ArrayList<>();
    private final SimplePowerPosHolder powerHolder;

    private ItemStack result = null;

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
        super(EBlockEntityTypes.KETTLE.get(), pos, state, NonNullList.create());
        this.powerHolder = new SimplePowerPosHolder(pos);
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, KettleBlockEntity be) {
        if(be.firstTick)
            be.firstTick();
        if(be.isFailed || be.isComplete)
            return;

        if(!providesHeat(level.getBlockState(pos.below())) || be.water != WATER_CAPACITY) {
            boolean update = be.progress > 0 || be.isHot();
            be.progress = 0;
            be.heat = 0;

            if(!be.inventory.isEmpty()) {
                update = true;
                be.setFailed();
            }

            if(update) // Only send update if we failed or need to stop boiling.
                be.updateBlock();

            be.setChanged();
            return;
        }

        if(!be.isHot()) {
            be.heat++;
            if(be.isHot())
                be.updateBlock();
            be.setChanged();
            return;
        }

        if(be.recipes.size() != 1 || !be.recipes.getFirst().value().fullMatch(ListInput.of(be.inventory)))
            return;

        if(be.progress < COOK_DURATION) {
            if(be.progress++ == 0 || be.progress == COOK_DURATION)
                be.updateBlock(); // Only send update if we just started or finished cooking.
            be.setChanged();
            return;
        }

        KettleRecipe recipe = be.recipes.getFirst().value();
        IPowerProvider provider = PowerHelper.tryGetProvider(level, be.powerHolder);

        if(recipe.getPower() == 0 || (provider != null && provider.tryConsume(recipe.getPower()))) {
            be.result = recipe.assemble(ListInput.of(be.inventory), level.registryAccess());
            be.colour = recipe.getFinalColour();
            be.isComplete = true;
        }
        else {
            be.isFailed = true;
        }
        be.setChanged();
        be.updateBlock();
    }

    public static void clientTick(Level level, BlockPos pos, BlockState state, KettleBlockEntity be) {
        if(be.firstTick)
            be.firstTick();

        double waterY = Mth.lerp((float)be.water / WATER_CAPACITY, 0.0625D, 0.3125D);

        if(be.isHot() && RandomUtils.nextFloat() < 0.8F) {
            double dx = pos.getX() + 0.5D + (Math.random() - 0.5D) * 0.375D; // 0.375 is width of water
            double dy = pos.getY() + waterY + 0.025D;
            double dz = pos.getZ() + 0.5D + (Math.random() - 0.5D) * 0.375D;
            level.addParticle(new ColourOptions(EParticleTypes.BOILING.get(), be.colour), dx, dy, dz, 0, 0, 0);
        }

        if(be.isFailed)
            return;

        if(!be.isComplete && be.progress > 0) {
            double dx = pos.getX() + Math.random();
            double dy = pos.getY() + Math.random();
            double dz = pos.getZ() + Math.random();
            level.addParticle(new ColourOptions(EParticleTypes.KETTLE_COOK.get(), be.colour), dx, dy, dz, 0D, 0D, 0D);
        }
        else if(be.isHot() && be.hasItems && RandomUtils.nextInt(10) > 6) {
            double xo = 0.5D + (Math.random() - 0.5D) * 0.375D; // 0.375 is width of water
            double zo = 0.5D + (Math.random() - 0.5D) * 0.375D;
            double dx = pos.getX() + xo;
            double dy = pos.getY() + waterY;
            double dz = pos.getZ() + zo;
            Vec3 velocity = new Vec3(xo, 0, zo).subtract(0.5D, 0.0D, 0.5D).normalize().scale((1D + Math.random()) * 0.06D);
            level.addParticle(new ColourOptions(EParticleTypes.CAULDRON_BREW.get(), be.colour), dx, dy, dz, velocity.x, (1.0D + Math.random()) * 0.06D, velocity.z);
        }
    }

    public boolean addItem(ItemStack stack) {
        if(isComplete || isFailed || !isWaterFull() || !isHot())
            return false;

        inventory.add(stack);
        updateRecipes();

        if(recipes.isEmpty()) {
            if(ServerConfig.INSTANCE.kettleItemSpoil.get()) {
                setFailed();
            }
            else {
                inventory.remove(stack);
                updateRecipes();
            }
        }
        else {
            colour = recipes.getFirst().value().getCookColour();
        }

        setChanged();
        updateBlock();
        return true;
    }

    private void updateRecipes() {
        if(level == null)
            return;

        if(recipes.isEmpty())
            setRecipes(level.getRecipeManager().getRecipesFor(ERecipeTypes.KETTLE.get(), ListInput.of(inventory), level));
        else
            recipes.removeIf(h -> !h.value().matches(ListInput.of(inventory), level));
    }

    private void firstTick() {
        firstTick = false;
        if(!level.isClientSide) {
            updateRecipes();
            updateBlock();
        }
        else {
            ClientProxy.startBubblingSound(this);
        }
    }

    private void setFailed() {
        isFailed = true;
        isComplete = false;
        colour = FAIL_COLOUR;
        result = null;
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag, @NotNull HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        saveSynced(tag, registries);

        if(result != null)
            tag.put("result", ItemStack.CODEC.encodeStart(NbtOps.INSTANCE, result).getOrThrow());
        tag.put("powerHolder", powerHolder.serialize());
    }

    private void saveSynced(CompoundTag tag, Provider registries) {
        tag.putInt("water", water);
        tag.putByte("heat", heat);
        tag.putBoolean("isFailed", isFailed);
        tag.putBoolean("isComplete", isComplete);
        tag.putInt("colour", colour);
        tag.putInt("progress", progress);
        tag.putBoolean("hasItems", !inventory.isEmpty());
    }

    @Override
    public void loadAdditional(@NotNull CompoundTag tag, @NotNull HolderLookup.Provider registries) {
        loadSynced(tag, registries);
        ContainerUtils.loadAllItems(tag, inventory, registries); // We have to do this instead of super because it's a dynamically sized inventory.

        if(tag.contains("result"))
            result = ItemStack.CODEC.parse(NbtOps.INSTANCE, tag.get("result")).getOrThrow();
        if(tag.contains("posHolder"))
            powerHolder.deserialize(tag.getCompound("powerHolder"));

        if(tag.contains("CustomName", 8))
            setCustomName(Component.Serializer.fromJson(tag.getString("CustomName"), registries));
    }

    private void loadSynced(CompoundTag tag, Provider registries) {
        water = tag.getInt("water");
        heat = tag.getByte("heat");
        isFailed = tag.getBoolean("isFailed");
        isComplete = tag.getBoolean("isComplete");
        progress = tag.getInt("progress");
        int c = tag.getInt("colour");

        if(level != null && level.isClientSide && oldColour != c) {
            oldColour = colour;
            blendStart = EnchantedClient.getGameTime();
            hasItems = tag.getBoolean("hasItems");
        }

        colour = c;
    }

    private void setRecipes(Collection<RecipeHolder<KettleRecipe>> recipes) {
        this.recipes.clear();
        this.recipes.addAll(recipes);
    }

    public int addWater(int amount) {
        int added = Math.min(amount, WATER_CAPACITY - water);
        water += added;
        if(added != 0) {
            setChanged();
            updateBlock();
        }
        return added;
    }

    public int getWaterCapacity() {
        return WATER_CAPACITY;
    }

    public int getWater() {
        return water;
    }

    public ARGB getColour(double currentTime) {
        float f = (float)Math.min((currentTime - blendStart) / BLEND_TIME, 1);
        ARGB s = ColourUtils.intToARGB(oldColour);
        ARGB e = ColourUtils.intToARGB(colour);
        return new ARGB(
                160,
                (int)(Mth.lerp(f, s.r(), e.r()) + 0.5F),
                (int)(Mth.lerp(f, s.g(), e.g()) + 0.5F),
                (int)(Mth.lerp(f, s.b(), e.b()) + 0.5F)
        );
    }

    public boolean isWaterFull() {
        return water == WATER_CAPACITY;
    }

    public boolean isHot() {
        return heat >= MAX_HEAT;
    }

    public static boolean providesHeat(BlockState state) {
        return state.is(Blocks.HEAT_SOURCES) && (!state.getValues().containsKey(BlockStateProperties.LIT) || state.getValue(BlockStateProperties.LIT));
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag(Provider registries) {
        CompoundTag nbt = new CompoundTag();
        saveSynced(nbt, registries);
        return nbt;
    }

    @Override
    protected Component getDefaultName() {
        return LangUtils.translatable("container", "kettle");
    }

    @Override
    public NonNullList<ItemStack> getDroppableInventory() {
        return NonNullList.create();
    }

    @Override
    public @NotNull IPowerPosHolder getPosHolder() {
        return powerHolder;
    }

}
