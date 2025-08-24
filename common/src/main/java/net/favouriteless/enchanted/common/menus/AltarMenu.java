package net.favouriteless.enchanted.common.menus;

import net.favouriteless.enchanted.common.blocks.entity.AltarBlockEntity;
import net.favouriteless.enchanted.common.init.EBlocks;
import net.favouriteless.enchanted.common.init.EMenuTypes;
import net.favouriteless.enchanted.common.util.MenuUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.item.ItemStack;

public class AltarMenu extends AbstractContainerMenu {

    public final AltarBlockEntity blockEntity;
    private final ContainerLevelAccess canInteractWithCallable;
    private final ContainerData data;

    public AltarMenu(final int id, final AltarBlockEntity be, ContainerData data) {
        super(EMenuTypes.ALTAR.get(), id);
        this.blockEntity = be;
        this.canInteractWithCallable = ContainerLevelAccess.create(be.getLevel(), be.getBlockPos());
        this.data = data;
        addDataSlots(this.data);
    }

    public AltarMenu(int id, Inventory playerInventory, BlockPos pos) {
        this(id, MenuUtils.getBlockEntity(playerInventory, pos, AltarBlockEntity.class), new SimpleContainerData(3));
    }

    public int getCurrentPower() {
        return data.get(0);
    }

    public int getMaxPower() {
        return data.get(1);
    }

    public int getRechargeMultiplier() {
        return data.get(2);
    }

    @Override
    public ItemStack quickMoveStack(Player var1, int var2) {
        return null;
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(canInteractWithCallable, player, EBlocks.ALTAR.get());
    }

}
