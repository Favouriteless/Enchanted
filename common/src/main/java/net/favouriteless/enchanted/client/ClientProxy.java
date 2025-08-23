package net.favouriteless.enchanted.client;

import net.favouriteless.enchanted.common.blocks.entity.KettleBlockEntity;
import net.favouriteless.enchanted.common.entities.Broomstick;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.world.level.block.entity.BlockEntity;

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

}
