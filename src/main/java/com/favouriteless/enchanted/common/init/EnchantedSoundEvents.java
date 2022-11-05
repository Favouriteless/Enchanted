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
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class EnchantedSoundEvents {

	public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, Enchanted.MOD_ID);

	public static RegistryObject<SoundEvent> CURSE_WHISPER = SOUND_EVENTS.register("curse_whisper", () -> new SoundEvent(Enchanted.location("curse_whisper")));
	public static RegistryObject<SoundEvent> CAULDRON_BUBBLING = SOUND_EVENTS.register("cauldron_bubbling", () -> new SoundEvent(Enchanted.location("cauldron_bubbling")));
	public static RegistryObject<SoundEvent> CURSE_CAST = SOUND_EVENTS.register("curse_cast", () -> new SoundEvent(Enchanted.location("curse_cast")));


}
