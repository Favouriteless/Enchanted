package com.favouriteless.enchanted.common.blocks.entity;

import com.favouriteless.enchanted.Enchanted;
import com.favouriteless.enchanted.common.blocks.FumeFunnelBlock;
import com.favouriteless.enchanted.common.blocks.WitchOvenBlock;
import com.favouriteless.enchanted.common.init.EnchantedTags;
import com.favouriteless.enchanted.common.init.registry.EnchantedBlockEntityTypes;
import com.favouriteless.enchanted.common.init.registry.EnchantedBlocks;
import com.favouriteless.enchanted.common.init.registry.EnchantedRecipeTypes;
import com.favouriteless.enchanted.common.menus.WitchOvenMenu;
import com.favouriteless.enchanted.common.recipes.WitchOvenRecipe;
import com.favouriteless.enchanted.platform.Services;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.Container;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractFurnaceBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class WitchOvenBlockEntity extends ContainerBlockEntityBase implements MenuProvider, WorldlyContainer {

    private static final int[] TOP_SLOTS = new int[] { 0 };
    private static final int[] SIDE_SLOTS = new int[] { 1 };
    private static final int[] FACE_SLOTS = new int[] { 2 };
    private static final int[] BOTTOM_SLOTS = new int[] { 3, 2, 4 };

    private final RecipeManager.CachedCheck<Container, SmeltingRecipe> smeltCheck;
    private final RecipeManager.CachedCheck<Container, WitchOvenRecipe> ovenCheck;

    private int burnProgress = 0;
    private int burnDuration = 0;
    private int cookProgress = 0;
    private int cookDuration = 200;

    private final ContainerData data = new ContainerData() {
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

    public WitchOvenBlockEntity(BlockPos pos, BlockState state) {
        super(EnchantedBlockEntityTypes.WITCH_OVEN.get(), pos, state, NonNullList.withSize(4, ItemStack.EMPTY));
        this.smeltCheck = RecipeManager.createCheck(RecipeType.SMELTING);
        this.ovenCheck = RecipeManager.createCheck(EnchantedRecipeTypes.WITCH_OVEN);
    }

    public static <T extends BlockEntity> void serverTick(Level level, BlockPos blockPos, BlockState blockState, T t) {
        if(t instanceof WitchOvenBlockEntity be) {
            boolean startedBurning = be.isLit();
            boolean isChanged = false;

            if(be.isLit())
                be.burnProgress--;

            ItemStack fuelStack = be.getFuel();

            boolean hasFuel = !fuelStack.isEmpty();
            boolean hasInput = !be.getInput().isEmpty();

            if(be.isLit() || hasInput && hasFuel) {
                Recipe<?> recipe = hasInput ? be.smeltCheck.getRecipeFor(be, level).orElse(null) : null;

                if(!be.isLit() && be.canBurn(recipe)) { // Handle starting off a stack of fuel or refreshing burn timer.
                    be.burnProgress = Services.PLATFORM.getBurnTime(fuelStack, RecipeType.SMELTING);
                    be.burnDuration = be.burnProgress;
                    if (be.isLit()) {
                        isChanged = true;
                        if(Services.PLATFORM.hasCraftingRemainingItem(fuelStack))
                            be.inventory.set(1, Services.PLATFORM.getCraftingRemainingItem(fuelStack));
                        else if(hasFuel) {
                            fuelStack.shrink(1);
                            if(fuelStack.isEmpty())
                                be.inventory.set(1, Services.PLATFORM.getCraftingRemainingItem(fuelStack));
                        }
                    }
                }

                if(be.isLit() && be.canBurn(recipe)) {
                    if(++be.cookProgress == be.cookDuration) {
                        be.cookDuration = 0;
                        be.cookDuration = getTotalCookTime(level, be);
                        be.burn(recipe);
                        isChanged = true;
                    }
                }
                else
                    be.cookProgress = 0;
            }
            else if(!be.isLit() && be.cookProgress > 0)
                be.cookProgress = Mth.clamp(be.cookProgress - 2, 0, be.cookDuration); // Tick the progress down if fuel ran out.

            if (startedBurning != be.isLit()) { // Update fume funnels and self if the burn state changed
                isChanged = true;
                level.setBlock(be.worldPosition, level.getBlockState(be.worldPosition).setValue(WitchOvenBlock.LIT, be.isLit()), 3);
                be.updateFumeFunnels();
            }


            if(isChanged)
                be.setChanged();
        }
    }

    /**
     * Attempt to smelt the item in this {@link WitchOvenBlockEntity}'s input slot and roll for a byproduct.
     * @param recipe The smelting recipe to use.
     */
    private void burn(@Nullable Recipe<?> recipe) {
        if(canBurn(recipe)) {
            ItemStack input = getInput();
            ItemStack fuel = getFuel();
            ItemStack result = recipe.getResultItem();
            ItemStack output = getOutput();

            if(Enchanted.RANDOM.nextDouble() <= getByproductChance())
                tryCreateByproduct(ovenCheck.getRecipeFor(this, level).orElse(null));

            if(output.isEmpty()) // Output the smelting item
                inventory.set(3, result.copy());
            else
                output.grow(1);

            if (input.is(Blocks.WET_SPONGE.asItem()) && !fuel.isEmpty() && fuel.is(Items.BUCKET))
                inventory.set(2, new ItemStack(Items.WATER_BUCKET));

            input.shrink(1);
        }
    }

    /**
     * Check if this Witch Oven is able to burn the item in it's input slot, and has enough space for the output.
     * @param recipe The smelting recipe to check for.
     * @return True if burnable, otherwise false.
     */
    private boolean canBurn(@Nullable Recipe<?> recipe) {
        ItemStack input = getInput();
        if(recipe != null && !input.isEmpty() && !input.is(EnchantedTags.Items.WITCH_OVEN_BLACKLIST)) {
            ItemStack result = ((Recipe<Container>)recipe).assemble(this);
            if(result.isEmpty())
                return false; // Don't bother checking recipes with no output.
            else {
                ItemStack output = getOutput();
                if(output.isEmpty())
                    return true; // Check the output is valid to place the result into.
                else {
                    if(!output.sameItem(result))
                        return false;
                    else
                        return output.getCount() + result.getCount() <= output.getMaxStackSize();
                }
            }
        }
        return false;
    }

    /**
     * Attempt to create a byproduct from the item currently in the input slot, assuming enough jars are present.
     * @param recipe The {@link Recipe} to create a result from.
     */
    private void tryCreateByproduct(@Nullable WitchOvenRecipe recipe) {
        if(recipe != null && !getInput().isEmpty()) {
            ItemStack result = recipe.assemble(this);
            if(!result.isEmpty()) {
                ItemStack jars = getJarInput();

                if(jars.getCount() >= result.getCount()) {
                    ItemStack output = getJarOutput();
                    if(output.isEmpty())
                        inventory.set(4, result);
                    else if(output.sameItem(result) && output.getCount() + result.getCount() <= output.getMaxStackSize())
                        output.grow(result.getCount());

                    jars.shrink(result.getCount());
                }
            }
        }
    }
    
    /**
     * Updates lit property of fume funnels attached to this {@link WitchOvenBlockEntity}.
     */
    private void updateFumeFunnels() {
        if(!level.isClientSide) {
            BlockState state = level.getBlockState(worldPosition);
            Direction facing = state.getValue(WitchOvenBlock.FACING);

            BlockPos[] potentialFilters = new BlockPos[]{
                    new BlockPos(worldPosition.offset(facing.getClockWise().getNormal())),
                    new BlockPos(worldPosition.offset(facing.getCounterClockWise().getNormal())),
                    new BlockPos(worldPosition.offset(Direction.UP.getNormal()))
            };

            for (BlockPos _pos : potentialFilters) {
                if (level.getBlockState(_pos).getBlock() instanceof FumeFunnelBlock)
                    level.setBlock(_pos, level.getBlockState(_pos).setValue(AbstractFurnaceBlock.LIT, isLit()), 3);
            }
        }
    }

    /**
     * @return The chance for a byproduct to be produced by this Witch Oven, accounting fume funnels.
     */
    private double getByproductChance() {
        double byproductChance = 0.3D;

        Direction facing = level.getBlockState(worldPosition).getValue(WitchOvenBlock.FACING);
        Block[] potentialFilters = new Block[] { // Top filter is just decoration
                level.getBlockState(worldPosition.offset(facing.getClockWise().getNormal())).getBlock(),
                level.getBlockState(worldPosition.offset(facing.getCounterClockWise().getNormal())).getBlock()
        };

        for(Block block : potentialFilters) {
            if(block == EnchantedBlocks.FUME_FUNNEL.get())
                byproductChance += 0.25D;
            else if(block == EnchantedBlocks.FUME_FUNNEL_FILTERED.get())
                byproductChance += 0.3D;
        }
        return byproductChance;
    }

    private static int getTotalCookTime(Level level, WitchOvenBlockEntity be) {
        return be.smeltCheck.getRecipeFor(be, level).map(recipe -> Math.round(recipe.getCookingTime() * 0.8F)).orElse(200);
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

    public int getBurnProgress() {
        return burnProgress;
    }

    public int getBurnDuration() {
        return burnDuration;
    }

    public int getCookProgress() {
        return cookProgress;
    }

    public int getCookDuration() {
        return cookDuration;
    }

    @Override
    public @NotNull Component getDefaultName() {
        return Component.translatable("container.enchanted.witch_oven");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory playerInventory, Player player) {
        return new WitchOvenMenu(id, playerInventory, this, data);
    }

    @Override
    public void saveAdditional(@NotNull CompoundTag nbt) {
        super.saveAdditional(nbt);
        nbt.putInt("burnTime", burnProgress);
        nbt.putInt("burnTimeTotal", burnDuration);
        nbt.putInt("cookTime", cookProgress);
        nbt.putInt("cookTimeTotal", cookDuration);
    }

    @Override
    public void load(@NotNull CompoundTag nbt) {
        super.load(nbt);
        burnProgress = nbt.getInt("burnTime");
        burnDuration = nbt.getInt("burnTimeTotal");
        cookProgress = nbt.getInt("cookTime");
        cookDuration = nbt.getInt("cookTimeTotal");
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
        return index != 3 && index != 4;
    }

    @Override
    public boolean canTakeItemThroughFace(int index, @NotNull ItemStack stack, @Nullable Direction face) {
        if(face == Direction.DOWN && index == 1)
            return stack.is(Items.WATER_BUCKET) || stack.is(Items.BUCKET);
        else
            return true;
    }

    @Override
    public void setItem(int index, @NotNull ItemStack stack) {
        boolean matching = !stack.isEmpty() && ItemStack.isSameItemSameTags(stack, getInput());
        inventory.set(index, stack);

        if(index == 0 && !matching) {
            cookDuration = getTotalCookTime(level, this);
            cookProgress = 0;
            setChanged();
        }
    }

}