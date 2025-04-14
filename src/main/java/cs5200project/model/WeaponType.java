package cs5200project.model;

public enum WeaponType {
    SWORD("Sword"),
    STAFF("Staff"),
    BOW("Bow"),
    KNIFE("Knife"),
    AXE("Axe"),
    HAMMER("Hammer"),
    SPEAR("Spear"),
    SHIELD("Shield");

    private final String name;

    WeaponType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
} 