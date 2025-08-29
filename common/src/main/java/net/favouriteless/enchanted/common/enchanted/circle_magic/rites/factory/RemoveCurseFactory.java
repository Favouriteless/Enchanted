package net.favouriteless.enchanted.common.enchanted.circle_magic.rites.factory;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.favouriteless.enchanted.api.circle_magic.RiteFactory;
import net.favouriteless.enchanted.api.curses.Curse.Type;
import net.favouriteless.enchanted.api.curses.CurseManager;
import net.favouriteless.enchanted.common.Enchanted;
import net.favouriteless.enchanted.common.enchanted.circle_magic.rites.RemoveCurseRite;
import net.favouriteless.enchanted.common.enchanted.circle_magic.rites.Rite;
import net.favouriteless.enchanted.common.enchanted.circle_magic.rites.Rite.BaseRiteParams;
import net.favouriteless.enchanted.common.enchanted.circle_magic.rites.Rite.RiteParams;
import net.minecraft.resources.ResourceLocation;

public class RemoveCurseFactory implements RiteFactory {

    public static final ResourceLocation ID = Enchanted.id("remove_curse");

    public static final MapCodec<RemoveCurseFactory> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            CurseManager.get().typeCodec().fieldOf("curse").forGetter(f -> f.curse)
    ).apply(instance, RemoveCurseFactory::new));

    private final Type<?> curse;

    public RemoveCurseFactory(Type<?> curse) {
        this.curse = curse;
    }

    @Override
    public Rite create(BaseRiteParams baseParams, RiteParams params) {
        return new RemoveCurseRite(baseParams, params, curse);
    }

    @Override
    public ResourceLocation id() {
        return ID;
    }

}
