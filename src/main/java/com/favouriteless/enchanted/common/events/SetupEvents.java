/*
 * Copyright (c) 2021. Favouriteless
 * Enchanted, a minecraft mod.
 * GNU GPLv3 License
 *
 *     This file is part of Enchanted.
 *
 *     Enchanted is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Enchanted is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Enchanted.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.favouriteless.enchanted.common.events;

import com.favouriteless.enchanted.Enchanted;
import com.favouriteless.enchanted.api.capabilities.bed.BedPlayerCapabilityManager;
import com.favouriteless.enchanted.client.render.tileentity.KettleRenderer;
import com.favouriteless.enchanted.client.render.tileentity.WitchCauldronRenderer;
import com.favouriteless.enchanted.client.screens.AltarScreen;
import com.favouriteless.enchanted.client.screens.DistilleryScreen;
import com.favouriteless.enchanted.client.screens.WitchOvenScreen;
import com.favouriteless.enchanted.common.entities.mandrake.MandrakeEntity;
import com.favouriteless.enchanted.common.init.*;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.loading.moddiscovery.ModInfo;
import net.minecraftforge.forgespi.language.IModInfo;
import vazkii.patchouli.api.PatchouliAPI;
import vazkii.patchouli.api.PatchouliAPI.IPatchouliAPI;
import vazkii.patchouli.common.book.BookRegistry;

import java.nio.file.Files;

@EventBusSubscriber(modid=Enchanted.MOD_ID, bus=Bus.MOD)
public class SetupEvents {

    public static final String TEMPLATES_LOCATION = "patchouli_books/global_templates";

    @SubscribeEvent
    public static void commonSetup(FMLCommonSetupEvent event) {
        EnchantedRecipeTypes.init();
        BedPlayerCapabilityManager.registerCapabilities();
    }

    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent event) {
        ScreenManager.register(EnchantedContainers.WITCH_OVEN.get(), WitchOvenScreen::new);
        ScreenManager.register(EnchantedContainers.DISTILLERY.get(), DistilleryScreen::new);
        ScreenManager.register(EnchantedContainers.ALTAR.get(), AltarScreen::new);
        
        EnchantedBlocks.initRender();
        EnchantedEntityTypes.registerEntityRenderers();

        ClientRegistry.bindTileEntityRenderer(EnchantedTileEntities.WITCH_CAULDRON.get(), WitchCauldronRenderer::new);
        ClientRegistry.bindTileEntityRenderer(EnchantedTileEntities.KETTLE.get(), KettleRenderer::new);

        registerPatchouliTemplates();
    }

    private static void registerPatchouliTemplates() {
        IPatchouliAPI api = PatchouliAPI.get();

        IModInfo info = ModLoadingContext.get().getActiveContainer().getModInfo();
        ModInfo mod;

        if(info instanceof ModInfo) {
            mod = (ModInfo)info;
        }
        else {
            return;
        }

        String id = mod.getModId();
        BookRegistry.findFiles(mod, String.format("data/%s/%s", id, TEMPLATES_LOCATION), (path) -> Files.exists(path),
                (path, file) -> {
                    if (Files.isRegularFile(file) && file.getFileName().toString().endsWith(".json")) {
                        String fileStr = file.toString().replaceAll("\\\\", "/");
                        String relPath = fileStr
                                .substring(fileStr.indexOf(TEMPLATES_LOCATION) + TEMPLATES_LOCATION.length() + 1);
                        String name = relPath.substring(0, relPath.indexOf(".json"));

                        api.registerTemplateAsBuiltin(new ResourceLocation(Enchanted.MOD_ID, name), () -> mod.getClass().getResourceAsStream(fileStr.substring(fileStr.indexOf("/data"))));

                    }
                    return true;
                }, true, 1);
    }

    @SubscribeEvent
    public static void registerBlockColors(ColorHandlerEvent.Block event) {
        event.getBlockColors().register((a, b, c, d) -> 0xF0F0F0, EnchantedBlocks.CHALK_WHITE.get());
        event.getBlockColors().register((a, b, c, d) -> 0x801818, EnchantedBlocks.CHALK_RED.get());
        event.getBlockColors().register((a, b, c, d) -> 0x4F2F78, EnchantedBlocks.CHALK_PURPLE.get());
    }

    @SubscribeEvent
    public static void registerEntityAttributes(EntityAttributeCreationEvent event) {
        event.put(EnchantedEntityTypes.MANDRAKE.get(), MandrakeEntity.createAttributes().build());
    }

}
