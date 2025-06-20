package net.favouriteless.enchanted.mixin.common;

import net.favouriteless.enchanted.common.LootExtensions;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;

@Mixin(LootTable.class)
public class LootTableMixin {

    @Inject(method = "getRandomItemsRaw(Lnet/minecraft/world/level/storage/loot/LootContext;Ljava/util/function/Consumer;)V", at = @At("TAIL"))
    private void getRandomItemsRaw(LootContext context, Consumer<ItemStack> output, CallbackInfo ci) {
        LootExtensions.tryRoll((LootTable)(Object)this, context, output);
    }

}
