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
import com.favouriteless.enchanted.client.particles.DelayedActionParticleType.DelayedActionData;
import com.favouriteless.enchanted.client.particles.TwoToneColouredParticleType.TwoToneColouredData;
import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(modid=Enchanted.MOD_ID, bus=Bus.MOD, value=Dist.CLIENT)
public class EnchantedParticles {

    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, Enchanted.MOD_ID);

    public static final RegistryObject<ParticleType<SimpleColouredData>> BOILING = PARTICLE_TYPES.register("boiling", () -> new SimpleColouredParticleType(false));
    public static final RegistryObject<ParticleType<SimpleColouredData>> CAULDRON_BREW = PARTICLE_TYPES.register("cauldron_brew", () -> new SimpleColouredParticleType(false));
    public static final RegistryObject<ParticleType<SimpleColouredData>> CAULDRON_COOK = PARTICLE_TYPES.register("cauldron_cook", () -> new SimpleColouredParticleType(false));
    public static final RegistryObject<ParticleType<SimpleColouredData>> KETTLE_COOK = PARTICLE_TYPES.register("kettle_cook", () -> new SimpleColouredParticleType(false));
    public static final RegistryObject<ParticleType<CircleMagicData>> CIRCLE_MAGIC = PARTICLE_TYPES.register("circle_magic", () -> new CircleMagicParticleType(false));
    public static final RegistryObject<ParticleType<TwoToneColouredData>> POPPET = PARTICLE_TYPES.register("poppet", () -> new TwoToneColouredParticleType(false));
    public static final RegistryObject<SimpleParticleType> IMPRISONMENT_CAGE = PARTICLE_TYPES.register("imprisonment_cage", () -> new SimpleParticleType(false));
    public static final RegistryObject<ParticleType<DelayedActionData>> SKY_WRATH = PARTICLE_TYPES.register("sky_wrath", () -> new DelayedActionParticleType(false));
    public static final RegistryObject<ParticleType<DelayedActionData>> REMOVE_CURSE = PARTICLE_TYPES.register("remove_curse", () -> new DelayedActionParticleType(false));
    public static final RegistryObject<ParticleType<CircleMagicData>> CURSE_BLIGHT = PARTICLE_TYPES.register("curse_blight", () -> new CircleMagicParticleType(false));

    public static final RegistryObject<SimpleParticleType> IMPRISONMENT_CAGE_SEED = PARTICLE_TYPES.register("imprisonment_cage_seed", () -> new SimpleParticleType(false));
    public static final RegistryObject<SimpleParticleType> TRANSPOSITION_IRON_SEED = PARTICLE_TYPES.register("transposition_iron_seed", () -> new SimpleParticleType(false));
    public static final RegistryObject<SimpleParticleType> BROILING_SEED = PARTICLE_TYPES.register("broiling_seed", () -> new SimpleParticleType(false));
    public static final RegistryObject<SimpleParticleType> SKY_WRATH_SEED = PARTICLE_TYPES.register("sky_wrath_seed", () -> new SimpleParticleType(false));
    public static final RegistryObject<SimpleParticleType> CURSE_SEED = PARTICLE_TYPES.register("curse_seed", () -> new SimpleParticleType(false));
    public static final RegistryObject<SimpleParticleType> CURSE_BLIGHT_SEED = PARTICLE_TYPES.register("curse_blight_seed", () -> new SimpleParticleType(false));
    public static final RegistryObject<SimpleParticleType> REMOVE_CURSE_SEED = PARTICLE_TYPES.register("remove_curse_seed", () -> new SimpleParticleType(false));

    @SubscribeEvent
    public static void registerParticleFactory(ParticleFactoryRegisterEvent event) {
        Minecraft.getInstance().particleEngine.register(BOILING.get(), BoilingParticle.Factory::new);
        Minecraft.getInstance().particleEngine.register(CAULDRON_BREW.get(), CauldronBrewParticle.Factory::new);
        Minecraft.getInstance().particleEngine.register(CAULDRON_COOK.get(), CauldronCookParticle.Factory::new);
        Minecraft.getInstance().particleEngine.register(KETTLE_COOK.get(), KettleCookParticle.Factory::new);
        Minecraft.getInstance().particleEngine.register(CIRCLE_MAGIC.get(), CircleMagicParticle.Factory::new);
        Minecraft.getInstance().particleEngine.register(POPPET.get(), PoppetParticle.Factory::new);
        Minecraft.getInstance().particleEngine.register(IMPRISONMENT_CAGE.get(), ImprisonmentCageParticle.Factory::new);
        Minecraft.getInstance().particleEngine.register(IMPRISONMENT_CAGE_SEED.get(), ImprisonmentCageSeedParticle.Factory::new);
        Minecraft.getInstance().particleEngine.register(TRANSPOSITION_IRON_SEED.get(), TranspositionIronSeedParticle.Factory::new);
        Minecraft.getInstance().particleEngine.register(BROILING_SEED.get(), BroilingSeedParticle.Factory::new);
        Minecraft.getInstance().particleEngine.register(SKY_WRATH_SEED.get(), SkyWrathSeedParticle.Factory::new);
        Minecraft.getInstance().particleEngine.register(SKY_WRATH.get(), SkyWrathParticle.Factory::new);
        Minecraft.getInstance().particleEngine.register(CURSE_SEED.get(), CurseSeedParticle.Factory::new);
        Minecraft.getInstance().particleEngine.register(CURSE_BLIGHT_SEED.get(), CurseBlightSeedParticle.Factory::new);
        Minecraft.getInstance().particleEngine.register(CURSE_BLIGHT.get(), CurseBlightParticle.Factory::new);
        Minecraft.getInstance().particleEngine.register(REMOVE_CURSE_SEED.get(), RemoveCurseSeedParticle.Factory::new);
        Minecraft.getInstance().particleEngine.register(REMOVE_CURSE.get(), RemoveCurseParticle.Factory::new);
    }

}
