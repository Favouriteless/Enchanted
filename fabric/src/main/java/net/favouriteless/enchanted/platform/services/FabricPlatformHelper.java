package net.favouriteless.enchanted.platform.services;

import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import org.jetbrains.annotations.Nullable;

public class FabricPlatformHelper implements PlatformHelper {

    @Override
    public boolean isModLoaded(String modId) {
        return FabricLoader.getInstance().isModLoaded(modId);
    }

    @Override
    public boolean isDevelopmentEnvironment() {
        return FabricLoader.getInstance().isDevelopmentEnvironment();
    }

    @Override
    public <T> void openMenu(ServerPlayer player, MenuProvider provider, T data, StreamCodec<? super RegistryFriendlyByteBuf, T> codec) {
        player.openMenu(new ExtendedScreenHandlerFactory<T>() {

            @Override
            public T getScreenOpeningData(ServerPlayer player) {
                return data;
            }

            @Override
            public Component getDisplayName() {
                return provider.getDisplayName();
            }

            @Override
            public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
                return provider.createMenu(id, inventory, player);
            }
        });
    }

    @Override
    public void openMenu(ServerPlayer player, MenuProvider provider) {
        player.openMenu(provider);
    }

    @Override
    public int getBurnTime(ItemStack stack, @Nullable RecipeType<?> recipeType) {
        Integer time = FuelRegistry.INSTANCE.get(stack.getItem());
        return time != null ? time : 0;
    }

    @Override
    public boolean hasCraftingRemainingItem(ItemStack item) {
        return item.getItem().hasCraftingRemainingItem();
    }

    @Override
    public ItemStack getCraftingRemainingItem(ItemStack item) {
        Item remaining = item.getItem().getCraftingRemainingItem();
        return remaining != null ? new ItemStack(remaining) : ItemStack.EMPTY;
    }

}