package com.favouriteless.enchanted;

import com.favouriteless.enchanted.platform.RegistryHandlerImpl;
import net.minecraftforge.fml.common.Mod;

@Mod(Enchanted.MOD_ID)
public class EnchantedForge {
    
    public EnchantedForge() {
        new RegistryHandlerImpl();

        Enchanted.init();
    }

}