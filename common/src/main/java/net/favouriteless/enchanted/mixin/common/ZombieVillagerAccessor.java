package net.favouriteless.enchanted.mixin.common;

import net.minecraft.world.entity.monster.ZombieVillager;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.UUID;

@Mixin(ZombieVillager.class)
public interface ZombieVillagerAccessor {

    @Invoker("startConverting")
    void startConversion(@Nullable UUID uuid, int conversionTime);

}
