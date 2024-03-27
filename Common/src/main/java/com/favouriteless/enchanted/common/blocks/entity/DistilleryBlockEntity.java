package com.favouriteless.enchanted.common.blocks.entity;

import com.favouriteless.enchanted.api.power.IPowerConsumer;
import com.favouriteless.enchanted.api.power.IPowerProvider;
import com.favouriteless.enchanted.api.power.PowerHelper;
import com.favouriteless.enchanted.common.altar.SimplePowerPosHolder;
import com.favouriteless.enchanted.common.init.registry.EnchantedBlockEntityTypes;
import com.favouriteless.enchanted.common.init.registry.EnchantedItems;
import com.favouriteless.enchanted.common.init.registry.EnchantedRecipeTypes;
import com.favouriteless.enchanted.common.menus.DistilleryMenu;
import com.favouriteless.enchanted.common.recipes.DistilleryRecipe;
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
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractFurnaceBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DistilleryBlockEntity extends ContainerBlockEntityBase implements IPowerConsumer, MenuProvider, WorldlyContainer {

    private static final int[] TOP_SLOTS = new int[] { 1, 2 };
    private static final int[] SIDE_SLOTS = new int[] { 0 };
    private static final int[] BOTTOM_SLOTS = new int[] { 3, 4, 5, 6 };

    private final RecipeManager.CachedCheck<Container, DistilleryRecipe> recipeCheck;
    private final SimplePowerPosHolder posHolder;

    private boolean isBurning = false;
    private int cookProgress = 0;
    private int cookDuration = 200;

    private final ContainerData data = new ContainerData() {
        @Override
        public int get(int index) {
            return switch(index) {
                case 0 -> cookProgress;
                case 1 -> cookDuration;
                default -> 0;
            };
        }

        @Override
        public void set(int index, int value) {
            switch(index) {
                case 0:
                    cookProgress = value;
                case 1:
                    cookDuration = value;
            }
        }

        @Override
        public int getCount() {
            return 2;
        }
    };

    public DistilleryBlockEntity(BlockPos pos, BlockState state) {
        super(EnchantedBlockEntityTypes.DISTILLERY.get(), pos, state, NonNullList.withSize(7, ItemStack.EMPTY));
        this.posHolder = new SimplePowerPosHolder(pos);
        this.recipeCheck = RecipeManager.createCheck(EnchantedRecipeTypes.DISTILLERY.get());
    }

    public static <T extends BlockEntity> void serverTick(Level level, BlockPos blockPos, BlockState blockState, T t) {
        if(t instanceof DistilleryBlockEntity be) {
            IPowerProvider powerProvider = PowerHelper.tryGetPowerProvider(level, be.posHolder);

            boolean isCooking = false; // Use this flag so cookProgress can be slowly lowered, couldn't use if/else.
            boolean isBurning = be.isBurning;
            boolean hasInput = !be.inventory.get(1).isEmpty() ||!be.inventory.get(2).isEmpty();

            if(hasInput && powerProvider != null) {
                DistilleryRecipe recipe = be.recipeCheck.getRecipeFor(be, level).orElse(null);

                if(be.canDistill(recipe) && powerProvider.tryConsumePower(10.0D)) {
                    isCooking = true;
                    be.isBurning = true;

                    if(++be.cookProgress == be.cookDuration) {
                        be.cookProgress = 0;
                        be.distill(recipe);
                    }
                }
            }

            if(!isCooking) {
                be.isBurning = false;
                if(be.cookProgress > 0)
                    be.cookProgress = Mth.clamp(be.cookProgress - 2, 0, be.cookDuration);
            }


            if(isBurning != be.isBurning)
                level.setBlock(be.worldPosition, level.getBlockState(be.worldPosition).setValue(AbstractFurnaceBlock.LIT, be.isBurning), 3);

            be.setChanged();
        }
    }

    /**
     * Attempt to distill the items in this {@link DistilleryBlockEntity}'s input slots.
     * @param recipe The {@link DistilleryRecipe} to use.
     */
    protected void distill(DistilleryRecipe recipe) {
        if(recipe != null) {
            for(ItemStack recipeItem : recipe.getItemsIn()) { // First, attempt to remove the input items.
                for(int i = 0; i < 3; i++) {
                    ItemStack inputItem = inventory.get(i);
                    if(ItemStack.isSameItemSameTags(recipeItem, inputItem)) {
                        if(inputItem.getCount() >= recipeItem.getCount()) {
                            inputItem.shrink(recipeItem.getCount());
                            break;
                        }
                    }
                }
            }

            List<ItemStack> itemsOut = recipe.getItemsOut();

            for(ItemStack recipeItem : itemsOut) {
                for(int i = 3; i < inventory.size(); i++) { // First, stack the result into existing slots.
                    ItemStack outputItem = inventory.get(i);

                    if(ItemStack.isSameItemSameTags(recipeItem, outputItem)) {
                        int amount = Math.min(outputItem.getMaxStackSize() - outputItem.getCount(), recipeItem.getCount());
                        outputItem.grow(amount);
                        recipeItem.shrink(amount);

                        if(recipeItem.isEmpty())
                            break;
                    }
                }
                if(!recipeItem.isEmpty()) {
                    for(int i = 3; i < inventory.size(); i++) { // After, stack the result into the first empty slot
                        if(inventory.get(i).isEmpty()) {
                            inventory.set(i, recipeItem);
                            break;
                        }
                    }
                }
            }
        }
    }

    /**
     * Check if this {@link DistilleryBlockEntity} has enough space for the output of a given recipe.
     * @param recipe The recipe to check for.
     * @return True if space is found, otherwise false.
     */
    private boolean canDistill(DistilleryRecipe recipe) {
        if(recipe != null) {
            List<ItemStack> itemsOut = new ArrayList<>(recipe.getItemsOut());

            Iterator<ItemStack> iterator = itemsOut.iterator();
            while(iterator.hasNext()) { // First, check if the output for the recipe can fit by stacking with items.
                ItemStack recipeStack = iterator.next();

                for(int i = 3; i < inventory.size(); i++) {
                    ItemStack outStack = inventory.get(i);

                    if(ItemStack.isSameItemSameTags(recipeStack, outStack)) {
                        if(recipeStack.getCount() + outStack.getCount() <= outStack.getMaxStackSize()) {
                            iterator.remove();
                            break;
                        }
                        recipeStack.shrink(outStack.getMaxStackSize() - outStack.getCount());
                    }
                }
            }

            int emptySpaces = 0;
            for(int i = 3; i < inventory.size(); i++) {
                if(inventory.get(i).isEmpty())
                    emptySpaces++;
            }

            return emptySpaces >= itemsOut.size(); // If there are same/more empty, we know the output should fit.
        }
        return false;
    }

    private static int getTotalCookTime(Level level, DistilleryBlockEntity be) {
        return be.recipeCheck.getRecipeFor(be, level).map(DistilleryRecipe::getCookTime).orElse(200);
    }

    @Override
    public Component getDefaultName() {
        return Component.translatable("container.enchanted.distillery");
    }

    @Override
    public AbstractContainerMenu createMenu(int id, @NotNull Inventory playerInventory, @NotNull Player player) {
        return new DistilleryMenu(id, playerInventory, this, data);
    }

    @Override
    public void saveAdditional(@NotNull CompoundTag nbt) {
        super.saveAdditional(nbt);
        nbt.put("posHolder", posHolder.serialize());
        nbt.putBoolean("isBurning", isBurning);
        nbt.putInt("cookTime", cookProgress);
        nbt.putInt("cookTimeTotal", cookDuration);
    }

    @Override
    public void load(@NotNull CompoundTag nbt) {
        super.load(nbt);
        posHolder.deserialize(nbt.getList("posHolder", 10));
        isBurning = nbt.getBoolean("isBurning");
        cookProgress = nbt.getInt("cookTime");
        cookDuration = nbt.getInt("cookTimeTotal");
    }

    @Override
    @NotNull
    public IPowerConsumer.IPowerPosHolder getPosHolder() {
        return posHolder;
    }

    @Override
    public int [] getSlotsForFace(@NotNull Direction face) {
        if(face == Direction.UP)
            return TOP_SLOTS;
        else if(face == Direction.DOWN)
            return BOTTOM_SLOTS;
        else
            return SIDE_SLOTS;
    }

    @Override
    public boolean canPlaceItemThroughFace(int index, @NotNull ItemStack stack, @Nullable Direction face) {
        if(index < 3)
            return index != 0 || stack.is(EnchantedItems.CLAY_JAR.get()); // Only let jars into slot 0
        else
            return false;
    }

    @Override
    public boolean canTakeItemThroughFace(int index, @NotNull ItemStack stack, @Nullable Direction face) {
        return index > 2;
    }

    @Override
    public void setItem(int index, @NotNull ItemStack stack) {
        boolean matching = !stack.isEmpty() && ItemStack.isSameItemSameTags(stack, inventory.get(index));
        inventory.set(index, stack);

        if(index < 3 && !matching) { // If the item changed was one of the inputs, recalculate.
            cookDuration = getTotalCookTime(level, this);
            cookProgress = 0;
            setChanged();
        }
    }

}
