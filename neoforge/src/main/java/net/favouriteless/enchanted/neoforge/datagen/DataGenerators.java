package net.favouriteless.enchanted.neoforge.datagen;

import net.favouriteless.enchanted.common.Enchanted;
import net.favouriteless.enchanted.neoforge.datagen.providers.*;
import net.favouriteless.enchanted.neoforge.datagen.providers.enchanted.EMutagenInfoProvider;
import net.favouriteless.enchanted.neoforge.datagen.providers.modopedia.EBookProvider;
import net.favouriteless.enchanted.neoforge.datagen.providers.modopedia.EBookTextureProvider;
import net.favouriteless.enchanted.neoforge.datagen.providers.modopedia.EContentSetProvider;
import net.favouriteless.enchanted.neoforge.datagen.providers.modopedia.ETemplateProvider;
import net.favouriteless.enchanted.neoforge.datagen.providers.tag.*;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(modid = Enchanted.MOD_ID)
public class DataGenerators {

	@SubscribeEvent
	public static void gatherData(GatherDataEvent event) {
		ExistingFileHelper fileHelper = event.getExistingFileHelper();
		DataGenerator gen = event.getGenerator();
		PackOutput output = gen.getPackOutput();
		CompletableFuture<HolderLookup.Provider> provider = event.getLookupProvider();

		EBlockTagProvider blockTagProvider = gen.addProvider(true, new EBlockTagProvider(output, provider, fileHelper));
		gen.addProvider(true, new EItemTagProvider(output, provider, fileHelper, blockTagProvider.contentsGetter()));
		gen.addProvider(true, new EEntityTypeTagProvider(output, provider, fileHelper));
		gen.addProvider(true, new EMobEffectTagProvider(output, provider, fileHelper));
		gen.addProvider(true, new EBiomeTagProvider(output, provider, fileHelper));
		gen.addProvider(true, new EDamageTypeTagProvider(output, provider, fileHelper));
		gen.addProvider(true, new ERecipeProvider(output, provider));

		gen.addProvider(true, new BlockstateProvider(output, fileHelper));
		gen.addProvider(true, new ItemModelProvider(output, fileHelper));
		gen.addProvider(true, new ELanguageProvider(output));
		gen.addProvider(true, ELootTableProvider.create(output, provider));

		gen.addProvider(true, new EBookTextureProvider(provider, output));
		gen.addProvider(true, new ETemplateProvider(provider, output));
		gen.addProvider(true, new EBookProvider(provider, output));
		gen.addProvider(true, new EContentSetProvider(provider, output));

		gen.addProvider(true, new EMutagenInfoProvider(output, provider));
	}

}
