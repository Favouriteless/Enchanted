package net.favouriteless.enchanted.platform.services;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.fml.ModList;
import net.neoforged.fml.loading.FMLLoader;
import org.jetbrains.annotations.Nullable;

public class NeoPlatformHelper implements IPlatformHelper {

    @Override
    public String getPlatformName() {
        return "NeoForge";
    }

    @Override
    public boolean isModLoaded(String modId) {
        return ModList.get().isLoaded(modId);
    }

    @Override
    public boolean isDevelopmentEnvironment() {
        return !FMLLoader.isProduction();
    }

    @Override
    public <T> void openMenu(ServerPlayer player, MenuProvider provider, T data, StreamCodec<? super RegistryFriendlyByteBuf, T> codec) {
        player.openMenu(provider, buf -> codec.encode(buf, data));
    }

    @Override
    public void openMenu(ServerPlayer player, MenuProvider provider) {
        player.openMenu(provider);
    }

    @Override
    public int getBurnTime(ItemStack stack, @Nullable RecipeType<?> recipeType) {
        return stack.getBurnTime(recipeType);
    }

    @Override
    public boolean hasCraftingRemainingItem(ItemStack item) {
        return item.hasCraftingRemainingItem();
    }

    @Override
    public ItemStack getCraftingRemainingItem(ItemStack item) {
        return item.getCraftingRemainingItem();
    }

}