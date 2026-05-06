package net.favouriteless.enchanted.client;

import net.favouriteless.enchanted.client.particles.types.TwoColourOptions;
import net.favouriteless.enchanted.client.render.poppet.PoppetAnimationManager;
import net.favouriteless.enchanted.client.sounds.BubblingSoundInstance;
import net.favouriteless.enchanted.client.sounds.MortarSoundInstance;
import net.favouriteless.enchanted.common.blocks.entity.KettleBlockEntity;
import net.favouriteless.enchanted.common.blocks.entity.MortarBlockEntity;
import net.favouriteless.enchanted.common.enchanted.poppet.PoppetColour;
import net.favouriteless.enchanted.common.entities.Broomstick;
import net.favouriteless.enchanted.common.init.EParticleTypes;
import net.favouriteless.enchanted.common.items.poppets.PoppetItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;

public class ClientProxy {

    public static void controlBroom(Broomstick broom) {
        Options options = Minecraft.getInstance().options;

        broom.setInputs(
                options.keyUp.isDown(), options.keyDown.isDown(),
                options.keyLeft.isDown(), options.keyRight.isDown(),
                options.keyJump.isDown(), options.keySprint.isDown()
        );
    }

    public static void startBubblingSound(KettleBlockEntity be) {
        Minecraft.getInstance().getSoundManager().play(new BubblingSoundInstance(be));
    }

    public static void startMortarSound(MortarBlockEntity be) {
        Minecraft.getInstance().getSoundManager().play(new MortarSoundInstance(be));
    }

    public static void playPoppetAnimation(int id, Item item) {
        Minecraft mc = Minecraft.getInstance();

        Entity entity = mc.level.getEntity(id);
        if(entity == null)
            return;

        PoppetColour colour = item instanceof PoppetItem poppet ? poppet.getColour() : null;
        if(colour != null)
            mc.particleEngine.createTrackingEmitter(entity, new TwoColourOptions(EParticleTypes.POPPET.get(), colour.primary(), colour.secondary()), 40);

        mc.level.playSound(mc.player, entity, SoundEvents.TOTEM_USE, SoundSource.PLAYERS, 0.5F, 1.0F);
        PoppetAnimationManager.startAnimation(item);
    }

}
