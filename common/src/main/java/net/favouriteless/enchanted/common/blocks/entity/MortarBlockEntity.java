package net.favouriteless.enchanted.common.blocks.entity;

import net.favouriteless.enchanted.client.ClientProxy;
import net.favouriteless.enchanted.common.init.ERecipeTypes;
import net.favouriteless.enchanted.common.recipes.MortarRecipe;
import net.favouriteless.enchanted.common.util.RandomUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeManager.CachedCheck;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class MortarBlockEntity extends EBlockEntity {

    private static final int MAX_ITEMS = 6;
    private static final int PROGRESS_PER_ITEM = 30;
    private static final int MAX_GRIND_TICKS = 5;

    private final CachedCheck<SingleRecipeInput, MortarRecipe> recipeCheck;

    @NotNull private ItemStack input = ItemStack.EMPTY;
    @NotNull private ItemStack result = ItemStack.EMPTY;
    private int progress = 0;
    private int colour = 0;
    private int grindTicks = 0;

    private boolean firstTick = true;

    // Client only fields related to rendering/sounds
    private int oPestleTicks = 0;
    private int pestleTicks = 0;


    public MortarBlockEntity(BlockPos pos, BlockState state) {
        super(EBlockEntityTypes.MORTAR.get(), pos, state);
        recipeCheck = RecipeManager.createCheck(ERecipeTypes.MORTAR.get());
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, MortarBlockEntity be) {
        if(be.firstTick)
            be.firstTick();
        if(be.grindTicks <= 0)
            return;

        SingleRecipeInput recipeInput = new SingleRecipeInput(be.input);
        RecipeHolder<MortarRecipe> recipe = be.recipeCheck.getRecipeFor(recipeInput, level).orElse(null);
        if(recipe == null)
            return;

        be.grindTicks--;

        int maxProgress = be.getMaxProgress();

        if(be.progress >= maxProgress)
            return;

        if(++be.progress == maxProgress) {
            be.result = recipe.value().assemble(recipeInput, level.registryAccess());
            be.result.setCount(be.input.getCount());
            be.input = ItemStack.EMPTY;
            be.grindTicks = 0;
        }

        be.updateBlock();
    }

    public static void clientTick(Level level, BlockPos pos, BlockState state, MortarBlockEntity be) {
        if(be.firstTick)
            be.firstTick();
        if(be.grindTicks == 0)
            return;

        // Pestle animation uses different var to avoid the "slingshot" effect when client ticks occur while the result
        // packet is still in transit
        be.oPestleTicks = be.pestleTicks++;

        if(be.input.isEmpty())
            return;

        if(RandomUtils.nextFloat() > 0.5F)
            return;

        double width = 0.375D;
        double hWidth = width/2;

        double x = pos.getX() + 0.5D + (RandomUtils.nextDouble(width) - hWidth);
        double y = pos.getY() + 0.3D + (RandomUtils.nextDouble(width) - hWidth);
        double z = pos.getZ() + 0.5D + (RandomUtils.nextDouble(width) - hWidth);
        double xs = RandomUtils.nextDouble(0.05D) - 0.025D;
        double ys = RandomUtils.nextDouble(0.1D) - 0.05D;
        double zs = RandomUtils.nextDouble(0.05D) - 0.025D;

        level.addParticle(new ItemParticleOption(ParticleTypes.ITEM, be.input), x, y, z, xs, ys, zs);
    }

    public void grind() {
        if(!input.isEmpty())
            grindTicks = MAX_GRIND_TICKS;
    }

    public boolean addIngredient(ItemStack stack) {
        if(stack.isEmpty() || !result.isEmpty())
            return false;

        RecipeHolder<MortarRecipe> recipe = recipeCheck.getRecipeFor(new SingleRecipeInput(stack), level).orElse(null);
        if(recipe == null)
            return false;

        int count = input.getCount();

        if(input.isEmpty())
            input = stack.copyWithCount(1);
        else if(ItemStack.isSameItemSameComponents(input, stack) && count < input.getMaxStackSize() && count < MAX_ITEMS)
            input.grow(1);
        else
            return false;

        stack.shrink(1);
        colour = recipe.value().getColour();
        updateBlock();
        return true;
    }

    private void firstTick() {
        firstTick = false;
        if(level.isClientSide)
            ClientProxy.startMortarSound(this);
    }

    public ItemStack takeResult() {
        ItemStack out = result;
        if(!out.isEmpty()) {
            grindTicks = 0;
            progress = 0;
            result = ItemStack.EMPTY;
            updateBlock();
        }
        return out;
    }

    public ItemStack takeIngredient() {
        ItemStack out = input.split(1);
        if(!out.isEmpty()) {
            grindTicks = 0;
            progress = 0;
            updateBlock();
        }
        return out;
    }

    public ItemStack getInput() {
        return input.copy();
    }

    public int getGrindTicks() {
        return grindTicks;
    }

    public float getPestleTicks(float partialTick) {
        return Mth.lerp(partialTick, oPestleTicks, pestleTicks);
    }

    public int getMaxProgress() {
        return PROGRESS_PER_ITEM * (result.isEmpty() ? input.getCount() : result.getCount());
    }

    public int getProgress() {
        return progress;
    }

    public int getColour() {
        return colour;
    }

    @Override
    protected void saveAdditional(CompoundTag tag, Provider registries) {
        super.saveAdditional(tag, registries);
    }

    @Override
    protected void saveSynced(CompoundTag tag, Provider registries) {
        if(!input.isEmpty())
            tag.put("input", input.save(registries));
        if(!result.isEmpty()) // Result is sent so MortarBlock knows how to handle use interactions
            tag.put("result", result.save(registries));

        tag.putInt("progress", progress);
        tag.putInt("colour", colour);
        tag.putInt("grind_ticks", grindTicks);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, Provider registries) {
        super.loadAdditional(tag, registries);
    }

    @Override
    protected void loadSynced(CompoundTag tag, Provider registries) {
        input = ItemStack.parseOptional(registries, tag.getCompound("input"));
        result = ItemStack.parseOptional(registries, tag.getCompound("result"));
        progress = tag.getInt("progress");
        colour = tag.getInt("colour");
        grindTicks = tag.getInt("grind_ticks");
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

}
