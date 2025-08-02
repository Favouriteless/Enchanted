package net.favouriteless.enchanted.common.blocks.entity;

import net.favouriteless.enchanted.common.blocks.FumeFunnelBlock;
import net.favouriteless.enchanted.common.blocks.WitchOvenBlock;
import net.favouriteless.enchanted.common.init.ETags;
import net.favouriteless.enchanted.common.init.EItems;
import net.favouriteless.enchanted.common.menus.WitchOvenMenu;
import net.favouriteless.enchanted.common.recipes.ByproductRecipe;
import net.favouriteless.enchanted.common.init.ERecipeTypes;
import net.favouriteless.enchanted.platform.EServices;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.item.crafting.RecipeManager.CachedCheck;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class WitchOvenBlockEntity extends ContainerBlockEntityBase implements MenuProvider, WorldlyContainer {

    private static final int[] TOP_SLOTS = new int[] { 0 };
    private static final int[] SIDE_SLOTS = new int[] { 1 };
    private static final int[] FACE_SLOTS = new int[] { 2 };
    private static final int[] BOTTOM_SLOTS = new int[] { 3, 4 };

    private final CachedCheck<SingleRecipeInput, SmeltingRecipe> smeltCheck;
    private final CachedCheck<SingleRecipeInput, ByproductRecipe> byproductCheck;

    private int burnProgress = 0;
    private int burnDuration = 0;
    private int cookProgress = 0;
    private int cookDuration = 200;


    public WitchOvenBlockEntity(BlockPos pos, BlockState state) {
        super(EBlockEntityTypes.WITCH_OVEN.get(), pos, state, NonNullList.withSize(5, ItemStack.EMPTY));
        this.smeltCheck = RecipeManager.createCheck(RecipeType.SMELTING);
        this.byproductCheck = RecipeManager.createCheck(ERecipeTypes.BYPRODUCT.get());
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, WitchOvenBlockEntity be) {
        boolean wasLit = be.isLit();
        boolean isChanged = false;

        if(be.isLit())
            be.burnProgress--;

        ItemStack fuel = be.getFuel();
        boolean hasInput = !be.getInput().isEmpty();

        if(be.isLit() || hasInput && !fuel.isEmpty()) {
            RecipeHolder<SmeltingRecipe> holder = hasInput ? be.smeltCheck.getRecipeFor(new SingleRecipeInput(be.inventory.get(0)), level).orElse(null) : null;
            if(holder != null) {
                boolean canBurn = be.canBurn(holder);

                if(!be.isLit() && canBurn) {
                    be.burnProgress = EServices.PLATFORM.getBurnTime(fuel, RecipeType.SMELTING);
                    be.burnDuration = be.burnProgress;

                    if(be.isLit()) {
                        isChanged = true;

                        ItemStack remainder = EServices.PLATFORM.getCraftingRemainingItem(fuel);
                        fuel.shrink(1);
                        if(fuel.isEmpty())
                            be.inventory.set(2, remainder == null ? ItemStack.EMPTY : remainder);
                    }
                }

                if(be.isLit() && canBurn) {
                    if(++be.cookProgress == be.cookDuration) {
                        be.cookProgress = 0;
                        be.cookDuration = be.getTotalCookTime();
                        be.burn(holder);
                        isChanged = true;
                    }
                }
                else {
                    be.cookProgress = 0;
                }
            }
        }
        else if(!be.isLit() && be.cookProgress > 0) {
            be.cookProgress = Mth.clamp(be.cookProgress - 2, 0, be.cookDuration); // Tick the progress down if fuel ran out.
        }

        if(wasLit != be.isLit()) { // Update fume funnels and self if the burn state changed
            level.setBlockAndUpdate(be.worldPosition, level.getBlockState(be.worldPosition).setValue(WitchOvenBlock.LIT, be.isLit()));
            be.updateFumeFunnels();
            isChanged = true;
        }

        if(isChanged)
            be.setChanged();
    }

    private boolean canBurn(@NotNull RecipeHolder<SmeltingRecipe> recipe) {
        ItemStack input = getInput();

        if(input.is(ETags.Items.WITCH_OVEN_BLACKLIST))
            return false;

        ItemStack result = recipe.value().assemble(new SingleRecipeInput(inventory.get(0)), level.registryAccess());
        if(result.isEmpty())
            return false; // Don't bother checking recipes with no output.

        ItemStack output = getOutput();

        if(output.isEmpty()) // Check the output is valid to place the result into.
            return true;
        if(!ItemStack.isSameItemSameComponents(output, result))
            return false;

        return output.getCount() + result.getCount() <= output.getMaxStackSize();
    }

    /**
     * Create the result for a smelting recipe. Does not check if item can fit.
     */
    private void burn(@NotNull RecipeHolder<SmeltingRecipe> recipe) {
        ItemStack input = getInput();
        ItemStack output = getOutput();
        ItemStack fuel = getFuel();

        if(Math.random() <= getByproductChance())
            createByproduct(byproductCheck.getRecipeFor(new SingleRecipeInput(input), level).orElse(null));

        ItemStack result = recipe.value().assemble(new SingleRecipeInput(inventory.get(0)), level.registryAccess());

        if(output.isEmpty())
            inventory.set(3, result);
        else
            output.grow(result.getCount());

        if(input.is(Items.WET_SPONGE) && !fuel.isEmpty() && fuel.is(Items.BUCKET))
            inventory.set(2, new ItemStack(Items.WATER_BUCKET));

        input.shrink(1);
    }

    /**
     * Create the result for a byproduct recipe if it can fit and has enough jars.
     */
    private void createByproduct(@Nullable RecipeHolder<ByproductRecipe> recipe) {
        if(recipe == null)
            return;

        ItemStack result = recipe.value().assemble(null, null); // ByproductRecipe doesn't care about these, but they're SUPPOSED to be NotNull.
        ItemStack input = getJarInput();

        if(input.getCount() < result.getCount()) // Assume not enough jars.
            return;

        ItemStack output = getJarOutput();

        if(!output.isEmpty()) {
            if(!ItemStack.isSameItemSameComponents(result, output) || result.getCount() + output.getCount() > output.getMaxStackSize())
                return;

            output.grow(result.getCount());
        }
        else {
            inventory.set(4, result);
        }
        input.shrink(result.getCount());
    }

    /**
     * @return The weight for a byproduct to be produced by this Witch Oven, accounting fume funnels.
     */
    private double getByproductChance() {
        double byproductChance = 0.3D;

        Direction facing = level.getBlockState(worldPosition).getValue(WitchOvenBlock.FACING);

        BlockState left = level.getBlockState(worldPosition.offset(facing.getCounterClockWise().getNormal()));
        BlockState right = level.getBlockState(worldPosition.offset(facing.getClockWise().getNormal()));

        if(left.getBlock() instanceof FumeFunnelBlock funnel)
            byproductChance += funnel.getByproductChance();
        if(right.getBlock() instanceof FumeFunnelBlock funnel)
            byproductChance += funnel.getByproductChance();

        return byproductChance;
    }

    /**
     * Updates lit property of fume funnels attached to this {@link WitchOvenBlockEntity}.
     */
    private void updateFumeFunnels() {
        Direction facing = level.getBlockState(worldPosition).getValue(WitchOvenBlock.FACING);

        BlockPos left = worldPosition.offset(facing.getCounterClockWise().getNormal());
        BlockPos right = worldPosition.offset(facing.getClockWise().getNormal());
        BlockPos top = worldPosition.above();

        if(level.getBlockState(left).getBlock() instanceof FumeFunnelBlock)
            level.setBlockAndUpdate(left, level.getBlockState(left).setValue(WitchOvenBlock.LIT, isLit()));
        if(level.getBlockState(right).getBlock() instanceof FumeFunnelBlock)
            level.setBlockAndUpdate(right, level.getBlockState(right).setValue(WitchOvenBlock.LIT, isLit()));
        if(level.getBlockState(top).getBlock() instanceof FumeFunnelBlock)
            level.setBlockAndUpdate(top, level.getBlockState(top).setValue(WitchOvenBlock.LIT, isLit()));
    }

    public ItemStack getInput() {
        return inventory.get(0);
    }

    public ItemStack getJarInput() {
        return inventory.get(1);
    }

    public ItemStack getFuel() {
        return inventory.get(2);
    }

    public ItemStack getOutput() {
        return inventory.get(3);
    }

    public ItemStack getJarOutput() {
        return inventory.get(4);
    }

    @Override
    public @NotNull Component getDefaultName() {
        return Component.translatable("container.enchanted.witch_oven");
    }

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory playerInventory, Player player) {
        return new WitchOvenMenu(id, playerInventory, this, access);
    }

    @Override
    public void saveAdditional(@NotNull CompoundTag tag, @NotNull Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putInt("burnTime", burnProgress);
        tag.putInt("burnTimeTotal", burnDuration);
        tag.putInt("cookTime", cookProgress);
        tag.putInt("cookTimeTotal", cookDuration);
    }

    @Override
    public void loadAdditional(@NotNull CompoundTag tag, @NotNull Provider registries) {
        super.loadAdditional(tag, registries);
        burnProgress = tag.getInt("burnTime");
        burnDuration = tag.getInt("burnTimeTotal");
        cookProgress = tag.getInt("cookTime");
        cookDuration = tag.getInt("cookTimeTotal");
    }

    private boolean isLit() {
        return burnProgress > 0;
    }

    @Override
    public int[] getSlotsForFace(@NotNull Direction face) {
        if(face == Direction.UP)
            return TOP_SLOTS;
        else if(face == Direction.DOWN)
            return BOTTOM_SLOTS;
        else if(face.getAxis() == level.getBlockState(worldPosition).getValue(WitchOvenBlock.FACING).getAxis())
            return FACE_SLOTS;
        else
            return SIDE_SLOTS;
    }

    @Override
    public boolean canPlaceItemThroughFace(int index, @NotNull ItemStack stack, @Nullable Direction face) {
        if(index == 1)
            return stack.getItem() == EItems.CLAY_JAR.get();
        return index != 3 && index != 4;
    }

    @Override
    public boolean canTakeItemThroughFace(int index, @NotNull ItemStack stack, @Nullable Direction face) {
        if(face == Direction.DOWN)
            return index == 1 ? stack.is(Items.WATER_BUCKET) || stack.is(Items.BUCKET) : index == 3 || index == 4;
        return true;
    }

    @Override
    public void setItem(int index, @NotNull ItemStack stack) {
        boolean matching = !stack.isEmpty() && ItemStack.isSameItemSameComponents(stack, getInput());
        inventory.set(index, stack);

        if(index == 0 && !matching) {
            cookDuration = getTotalCookTime();
            cookProgress = 0;
            setChanged();
        }
    }

    private int getTotalCookTime() {
        return smeltCheck.getRecipeFor(new SingleRecipeInput(getInput()), level)
                .map(holder -> holder.value().getCookingTime()).orElse(200);
    }

    private final ContainerData access = new ContainerData() {
        @Override
        public int get(int index) {
            return switch(index) {
                case 0 -> burnProgress;
                case 1 -> burnDuration;
                case 2 -> cookProgress;
                case 3 -> cookDuration;
                default -> 0;
            };
        }

        @Override
        public void set(int index, int value) {
            switch(index) {
                case 0:
                    burnProgress = value;
                case 1:
                    burnDuration = value;
                case 2:
                    cookProgress = value;
                case 3:
                    cookDuration = value;
            }
        }

        @Override
        public int getCount() {
            return 4;
        }
    };

}