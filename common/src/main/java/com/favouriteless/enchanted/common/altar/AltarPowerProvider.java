package com.favouriteless.enchanted.common.altar;

public class AltarPowerProvider<T> {

    private final T key;
    private final int power;
    private final int limit;

    public AltarPowerProvider(T key, int power, int limit) {
        this.key = key;
        this.power = power;
        this.limit = limit;
    }

    public boolean is(T key) {
        return this.key == key;
    }

    public T getKey() {
        return this.key;
    }

    public int getPower() {
        return this.power;
    }

    public int getLimit() {
        return this.limit;
    }
}