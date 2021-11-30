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

package com.favouriteless.enchanted.common.init;

import com.favouriteless.enchanted.Enchanted;
import com.favouriteless.enchanted.client.particles.*;
import com.mojang.serialization.Codec;
import net.minecraft.client.Minecraft;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(modid=Enchanted.MOD_ID, bus=Mod.EventBusSubscriber.Bus.MOD, value=Dist.CLIENT)
public class EnchantedParticles {

    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, Enchanted.MOD_ID);

    public static final RegistryObject<ParticleType<BoilingParticleData>> BOILING = PARTICLE_TYPES.register("boiling", () -> new ParticleType<BoilingParticleData>(false, BoilingParticleData.DESERIALIZER) {
        @Override
        public Codec<BoilingParticleData> codec() {
            return BoilingParticleData.CODEC;
        }
    });

    public static final RegistryObject<ParticleType<CauldronBrewParticleData>> CAULDRON_BREW = PARTICLE_TYPES.register("cauldron_brew", () -> new ParticleType<CauldronBrewParticleData>(false, CauldronBrewParticleData.DESERIALIZER) {
        @Override
        public Codec<CauldronBrewParticleData> codec() {
            return CauldronBrewParticleData.CODEC;
        }
    });

    @SubscribeEvent
    public static void registerParticleFactory(ParticleFactoryRegisterEvent event) {
        Minecraft.getInstance().particleEngine.register(BOILING.get(), BoilingParticle.Factory::new);
        Minecraft.getInstance().particleEngine.register(CAULDRON_BREW.get(), CauldronBrewParticle.Factory::new);
    }

}
