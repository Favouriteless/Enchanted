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

package com.favouriteless.enchanted;

import net.minecraftforge.common.ForgeConfigSpec;

public class EnchantedConfig {

    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    // Altar
    public static final ForgeConfigSpec.ConfigValue<Integer> ALTAR_RANGE;
    public static final ForgeConfigSpec.ConfigValue<Double> ALTAR_BASE_RECHARGE;

    // Cauldron
    public static final ForgeConfigSpec.ConfigValue<Boolean> CAULDRON_ITEM_SPOIL;

    // Kettle
    public static final ForgeConfigSpec.ConfigValue<Boolean> KETTLE_ITEM_SPOIL;

    public static final ForgeConfigSpec.ConfigValue<Boolean> DISABLE_TOTEMS;
    public static final ForgeConfigSpec.ConfigValue<Boolean> WHITELIST_TOOL_POPPET;
    public static final ForgeConfigSpec.ConfigValue<Boolean> WHITELIST_ARMOUR_POPPET;

    // Rites
    public static final ForgeConfigSpec.ConfigValue<Integer> TOTAL_ECLIPSE_COOLDOWN;
    public static final ForgeConfigSpec.ConfigValue<Integer> SKY_WRATH_COOLDOWN;
    public static final ForgeConfigSpec.ConfigValue<Double> BROILING_BURN_CHANCE;
    public static final ForgeConfigSpec.ConfigValue<Boolean> DISABLE_MISFORTUNE;
    public static final ForgeConfigSpec.ConfigValue<Boolean> DISABLE_OVERHEATING;
    public static final ForgeConfigSpec.ConfigValue<Boolean> DISABLE_SINKING;
    public static final ForgeConfigSpec.ConfigValue<Boolean> DISABLE_BLIGHT;
    public static final ForgeConfigSpec.ConfigValue<Double> BLIGHT_DECAY_CHANCE;
    public static final ForgeConfigSpec.ConfigValue<Double> BLIGHT_ZOMBIE_CHANCE;
    public static final ForgeConfigSpec.ConfigValue<Double> FERTILITY_BONE_MEAL_CHANCE;

    static {
        BUILDER.push("Altar Options");
        ALTAR_RANGE = BUILDER.comment("#default 16").define("altar_range", 16);
        ALTAR_BASE_RECHARGE = BUILDER.comment("#default 2.0").define("altar_recharge_rate", 2.0D);
        BUILDER.pop();

        BUILDER.push("Cauldron Options");
        CAULDRON_ITEM_SPOIL = BUILDER.comment("Allow incorrect items to spoil brew #default true").define("cauldron_item_spoil", true);
        BUILDER.pop();

        BUILDER.push("Kettle Options");
        KETTLE_ITEM_SPOIL = BUILDER.comment("Allow incorrect items to spoil brew #default true").define("kettle_item_spoil", true);
        BUILDER.pop();

        BUILDER.push("Poppet Options");
        WHITELIST_TOOL_POPPET = BUILDER.comment("Enable the #enchanted:tool_poppet_whitelist tag #default false").define("tool_poppet_whitelist", false);
        WHITELIST_ARMOUR_POPPET = BUILDER.comment("Enable the #enchanted:armour_poppet_whitelist tag #default false").define("armour_poppet_whitelist", false);
        BUILDER.pop();

        BUILDER.push("Rite Options");
        TOTAL_ECLIPSE_COOLDOWN = BUILDER.comment("Global cooldown for the rite of eclipse in seconds").define("total_eclipse_cooldown", 0);
        SKY_WRATH_COOLDOWN = BUILDER.comment("Global cooldown for the rite of sky's wrath in seconds").define("sky_wrath_cooldown", 0);
        BROILING_BURN_CHANCE = BUILDER.comment("Chance for food to be burned by the rite of broiling #default 0.3").define("broiling_burn_chance", 0.3D);
        DISABLE_MISFORTUNE = BUILDER.comment("Disable the curse of misfortune #default false").define("disable_misfortune", false);
        DISABLE_OVERHEATING = BUILDER.comment("Disable the curse of overheating #default false").define("disable_overheating", false);
        DISABLE_SINKING = BUILDER.comment("Disable the curse of sinking #default false").define("disable_sinking", false);
        DISABLE_BLIGHT = BUILDER.comment("Disable the curse of blight #default false").define("disable_blight", false);
        BLIGHT_DECAY_CHANCE = BUILDER.comment("Chance for blocks to be decayed by the curse of blight #default 0.3").define("blight_decay_chance", 0.3D);
        BLIGHT_ZOMBIE_CHANCE = BUILDER.comment("Chance for villagers to be zombified by the curse of blight #default 0.3").define("blight_zombie_chance", 0.3D);
        FERTILITY_BONE_MEAL_CHANCE = BUILDER.comment("Chance for blocks to be bone mealed by the rite of fertility #default 0.8").define("fertility_bone_meal_chance", 0.8D);
        BUILDER.pop();

        BUILDER.push("Miscellaneous Options");
        DISABLE_TOTEMS = BUILDER.comment("Disable totems of undying (to make poppets more useful) #default false").define("disable_totems", false);
        BUILDER.pop();

        SPEC = BUILDER.build();
    }

}
