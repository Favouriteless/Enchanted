/*
 *
 *   Copyright (c) 2022. Favouriteless
 *   Enchanted, a minecraft mod.
 *   GNU GPLv3 License
 *
 *       This file is part of Enchanted.
 *
 *       Enchanted is free software: you can redistribute it and/or modify
 *       it under the terms of the GNU General Public License as published by
 *       the Free Software Foundation, either version 3 of the License, or
 *       (at your option) any later version.
 *
 *       Enchanted is distributed in the hope that it will be useful,
 *       but WITHOUT ANY WARRANTY; without even the implied warranty of
 *       MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *       GNU General Public License for more details.
 *
 *       You should have received a copy of the GNU General Public License
 *       along with Enchanted.  If not, see <https://www.gnu.org/licenses/>.
 *
 *
 */

package com.favouriteless.enchanted.common.init;

import com.favouriteless.enchanted.Enchanted;
import com.favouriteless.enchanted.client.particles.*;
import com.favouriteless.enchanted.client.particles.CircleMagicParticleType.CircleMagicData;
import com.favouriteless.enchanted.client.particles.SimpleColouredParticleType.SimpleColouredData;
import com.favouriteless.enchanted.client.particles.TwoToneColouredParticleType.TwoToneColouredData;
import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.ParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(modid=Enchanted.MOD_ID, bus=Mod.EventBusSubscriber.Bus.MOD, value=Dist.CLIENT)
public class EnchantedParticles {

    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, Enchanted.MOD_ID);

    public static final RegistryObject<ParticleType<SimpleColouredData>> BOILING = PARTICLE_TYPES.register("boiling", () -> new SimpleColouredParticleType(false));
    public static final RegistryObject<ParticleType<SimpleColouredData>> CAULDRON_BREW = PARTICLE_TYPES.register("cauldron_brew", () -> new SimpleColouredParticleType(false));
    public static final RegistryObject<ParticleType<SimpleColouredData>> CAULDRON_COOK = PARTICLE_TYPES.register("cauldron_cook", () -> new SimpleColouredParticleType(false));
    public static final RegistryObject<ParticleType<SimpleColouredData>> KETTLE_COOK = PARTICLE_TYPES.register("kettle_cook", () -> new SimpleColouredParticleType(false));
    public static final RegistryObject<ParticleType<CircleMagicData>> CIRCLE_MAGIC = PARTICLE_TYPES.register("circle_magic", () -> new CircleMagicParticleType(false));
    public static final RegistryObject<ParticleType<TwoToneColouredData>> POPPET = PARTICLE_TYPES.register("poppet", () -> new TwoToneColouredParticleType(false));

    @SubscribeEvent
    public static void registerParticleFactory(ParticleFactoryRegisterEvent event) {
        Minecraft.getInstance().particleEngine.register(BOILING.get(), BoilingParticle.Factory::new);
        Minecraft.getInstance().particleEngine.register(CAULDRON_BREW.get(), CauldronBrewParticle.Factory::new);
        Minecraft.getInstance().particleEngine.register(CAULDRON_COOK.get(), CauldronCookParticle.Factory::new);
        Minecraft.getInstance().particleEngine.register(KETTLE_COOK.get(), KettleCookParticle.Factory::new);
        Minecraft.getInstance().particleEngine.register(CIRCLE_MAGIC.get(), CircleMagicParticle.Factory::new);
        Minecraft.getInstance().particleEngine.register(POPPET.get(), PoppetParticle.Factory::new);
    }

}
