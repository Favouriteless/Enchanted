package com.favouriteless.enchanted.common.menus.slots;

import net.minecraft.world.inventory.DataSlot;

public class SimpleDataSlot extends DataSlot {

    private int data;

    @Override
    public int get() {
        return data;
    }

    @Override
    public void set(int value) {
        data = value;
    }

}
