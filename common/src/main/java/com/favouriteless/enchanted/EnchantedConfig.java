package com.favouriteless.enchanted;

import com.favouriteless.enchanted.client.ClientConfig;
import com.favouriteless.enchanted.common.CommonConfig;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry.Category;
import me.shedaniel.autoconfig.annotation.ConfigEntry.Gui.TransitiveObject;
import me.shedaniel.autoconfig.serializer.PartitioningSerializer;

@Config(name = Enchanted.MOD_ID)
public class EnchantedConfig extends PartitioningSerializer.GlobalData {

    @Category("common")
    @TransitiveObject
    public CommonConfig common = new CommonConfig();

    @Category("client")
    @TransitiveObject
    public ClientConfig client = new ClientConfig();

}