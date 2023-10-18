package com.favouriteless.enchanted.common;

import com.favouriteless.enchanted.Enchanted;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;

@Config(name= Enchanted.MOD_ID)
public class CommonConfig implements ConfigData {
    
    public int altarRange = 16;
    public double altarBaseRecharge = 2.0D;

    public boolean cauldronItemSpoil = true;

    public boolean kettleItemSpoil = true;

    public boolean whitelistToolPoppet = false;
    public boolean whitelistArmourPoppet = false;

    public int totalEclipseCooldown = 0;
    public int skyWrathCooldown = 0;
    public double broilingBurnChance = 0.3D;
    public boolean disableMisfortune = false;
    public boolean disableOverheating = false;
    public boolean disableSinking = false;
    public boolean disableBlight = false;
    public double blightDecayChance = 0.3D;
    public double blightZombieChance = 0.3D;
    public double fertilityBonemealChance = 0.8D;
    public int forestTreeCount = 25;
    public int forestRadius = 25;

    public boolean hoeOnlySeeds = false;
    public boolean disableTotems = false;
    public double entAxeMultiplier = 3.0D;

}
