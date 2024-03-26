package com.favouriteless.enchanted.datagen;

import com.favouriteless.enchanted.Enchanted;
import com.favouriteless.enchanted.datagen.providers.BlockstateProvider;
import com.favouriteless.enchanted.datagen.providers.ItemModelProvider;
import com.favouriteless.enchanted.datagen.providers.RecipeProvider;
import com.favouriteless.enchanted.datagen.providers.tag.*;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(modid=Enchanted.MOD_ID, bus=Bus.MOD)
public class DataGenerators {


	@SubscribeEvent
	public static void gatherData(GatherDataEvent event) {
		DataGenerator gen = event.getGenerator();
		ExistingFileHelper fileHelper = event.getExistingFileHelper();

		BlockTagProvider blockTagProvider = new BlockTagProvider(gen, fileHelper);
		gen.addProvider(true, blockTagProvider);
		gen.addProvider(true, new ItemTagProvider(gen, blockTagProvider, fileHelper));
		gen.addProvider(true, new EntityTypeTagProvider(gen, fileHelper));
		gen.addProvider(true, new MobEffectTagProvider(gen, fileHelper));
		gen.addProvider(true, new BiomeTagProvider(gen, fileHelper));

		gen.addProvider(true, new BlockstateProvider(gen, fileHelper));
		gen.addProvider(true, new ItemModelProvider(gen, fileHelper));
		gen.addProvider(true, new RecipeProvider(gen));
	}

}
