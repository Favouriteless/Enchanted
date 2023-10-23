package com.favouriteless.enchanted.mixin;

import net.minecraft.world.entity.monster.ZombieVillager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import javax.annotation.Nullable;
import java.util.UUID;

@Mixin(ZombieVillager.class)
public interface ZombieVillagerAccessor {

    @Invoker("startConverting")
    void startConverting(@Nullable UUID uuid, int conversionTime);

}
