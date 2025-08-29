package net.favouriteless.enchanted.common.enchanted.altar;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.favouriteless.enchanted.common.init.EData;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

import java.util.Optional;

public record AltarUpgrade(ResourceLocation type, Block block, double recharge, double power, int priority) {

    public static final Codec<AltarUpgrade> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.fieldOf("type").forGetter(data -> data.type.toString()),
            Codec.STRING.fieldOf("block").forGetter(data -> BuiltInRegistries.BLOCK.getKey(data.block).toString()),
            Codec.DOUBLE.fieldOf("recharge").forGetter(data -> data.recharge),
            Codec.DOUBLE.fieldOf("power").forGetter(data -> data.power),
            Codec.INT.fieldOf("priority").forGetter(data -> data.priority)
    ).apply(instance, (type, block, recharge, power, priority) -> new AltarUpgrade(ResourceLocation.parse(type), BuiltInRegistries.BLOCK.get(ResourceLocation.parse(block)), recharge, power, priority)));

    public static AltarUpgrade get(Level level, Block block) {
        Optional<Registry<AltarUpgrade>> optional = level.registryAccess().registry(EData.ALTAR_UPGRADE_REGISTRY);
        if(optional.isPresent()) {
            for(AltarUpgrade upgrade : optional.get()) {
                if(upgrade.block() == block)
                    return upgrade;
            }
        }
        return null;
    }

    public static AltarUpgrade get(Level level, ResourceLocation location) {
        return level.registryAccess().registry(EData.ALTAR_UPGRADE_REGISTRY)
                .map(altarUpgrades -> altarUpgrades.get(location))
                .orElse(null);
    }

}
