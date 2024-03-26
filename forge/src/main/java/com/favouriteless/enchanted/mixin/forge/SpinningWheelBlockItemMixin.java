package com.favouriteless.enchanted.mixin.forge;

import com.favouriteless.enchanted.client.render.blockentity.item.SpinningWheelItemRenderer;
import com.favouriteless.enchanted.common.items.SpinningWheelBlockItem;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.util.function.Consumer;

@Mixin(SpinningWheelBlockItem.class)
public abstract class SpinningWheelBlockItemMixin {

    /**
     * @author Favouriteless
     * @reason This couldn't be overridden in the item class because it's a forge coremod.
     */
    @Overwrite
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            private SpinningWheelItemRenderer renderer;

            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                if(renderer == null)
                    renderer = new SpinningWheelItemRenderer();

                return renderer;
            }
        });
    }

}
