package net.favouriteless.enchanted.common.enchanted.poppet;

public record PoppetColour(int primary, int secondary) {

    public static final PoppetColour EARTH = new PoppetColour(0xFF705036, 0xFF3B2B1E);
    public static final PoppetColour EQUIPMENT = new PoppetColour(0xFFE3E3E3, 0xFFA3A3A3);
    public static final PoppetColour FIRE = new PoppetColour(0xFFFF9D26, 0xFFDE4635);
    public static final PoppetColour HUNGER = new PoppetColour(0xFF71B83B, 0xFF4E8041);
    public static final PoppetColour MAGIC = new PoppetColour(0xFFAC4AED, 0xFFC891ED);
    public static final PoppetColour VOID = new PoppetColour(0xFF703F8F, 0xFF3A214A);
    public static final PoppetColour VOODOO_PROTECTION = new PoppetColour(0xFF292929, 0xFF525252);
    public static final PoppetColour WATER = new PoppetColour(0xFF49B4E6, 0xFF1834C4);

}
