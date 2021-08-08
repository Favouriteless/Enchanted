package com.favouriteless.enchanted.core.init;

import com.favouriteless.enchanted.Enchanted;
import com.favouriteless.enchanted.common.containers.*;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class EnchantedContainers {

    public static final DeferredRegister<ContainerType<?>> CONTAINER_TYPES = DeferredRegister.create(ForgeRegistries.CONTAINERS, Enchanted.MOD_ID);

    public static final RegistryObject<ContainerType<WitchOvenContainer>> WITCH_OVEN = CONTAINER_TYPES.register("witch_oven",
            () -> IForgeContainerType.create(WitchOvenContainer::new));
    public static final RegistryObject<ContainerType<DistilleryContainer>> DISTILLERY = CONTAINER_TYPES.register("distillery",
            () -> IForgeContainerType.create(DistilleryContainer::new));
}