package net.favouriteless.enchanted.api;

public record Vec2i(int x, int y) {

    @Override
    public boolean equals(Object obj) {
        return this == obj || (obj instanceof Vec2i(int x1, int y1) && x1 == x && y1 == y);
    }

}
