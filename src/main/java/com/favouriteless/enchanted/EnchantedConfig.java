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

    static {
        BUILDER.push("Altar Options");
        ALTAR_RANGE = BUILDER.define("altar_range", 16);
        ALTAR_BASE_RECHARGE = BUILDER.define("altar_recharge_rate", 2.0D);
        BUILDER.pop();

        BUILDER.push("Cauldron Options");
        CAULDRON_ITEM_SPOIL = BUILDER.comment("Allow incorrect items to spoil brew").define("cauldron_item_spoil", true);
        BUILDER.pop();

        BUILDER.push("Kettle Options");
        KETTLE_ITEM_SPOIL = BUILDER.comment("Allow incorrect items to spoil brew").define("kettle_item_spoil", true);
        BUILDER.pop();

        BUILDER.push("Miscellaneous Options");
        DISABLE_TOTEMS = BUILDER.comment("Disable totems of undying (to make poppets more useful)").define("disable_totems", false);
        BUILDER.pop();

        SPEC = BUILDER.build();
    }

}
