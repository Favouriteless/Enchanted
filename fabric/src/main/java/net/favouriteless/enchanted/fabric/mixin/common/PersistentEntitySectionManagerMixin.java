package net.favouriteless.enchanted.fabric.mixin.common;

import net.favouriteless.enchanted.common.init.EItems;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.entity.EntityAccess;
import net.minecraft.world.level.entity.PersistentEntitySectionManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PersistentEntitySectionManager.class)
public class PersistentEntitySectionManagerMixin<T extends EntityAccess> {

    @Inject(method = "addEntity", at = @At("HEAD"), cancellable = true)
    private void addEntity(T e, boolean worldGenSpawned, CallbackInfoReturnable<Boolean> cir) {
        if (e instanceof Entity entity && entity.getClass().equals(ItemEntity.class)) { // Class compare because we do not want to trigger on subclasses
            if (((ItemEntity) entity).getItem().getItem() == EItems.VOODOO_POPPET.get()) {
                entity.level().addFreshEntity(EItems.VOODOO_POPPET.get().createEntity(entity.level(), entity, ((ItemEntity) entity).getItem()));
                cir.setReturnValue(false);
            }
        }
    }

}
