package me.notenoughskill.customenchants.utils;

public enum CustomEnchantment {
    RADIATE("basic", "Radiate", "Gives player night vision", 1, "Helmet"),
    FINAL_WISH("basic", "Final Wish", "Inflicts temporary weakness on whoever kills you", 1, "Helmet"),
    MEDICATE("basic", "Medicate", "Take less enviromental damage", 2, "Helmet"),
    EXTINGUISH("basic", "Extinguish", "Reduces lava and fire damage", 3, "Boots"),
    SATURATION("advanced", "Saturation", "Chance to restore hunger every few seconds", 6, "Helmet"),
    INFERNAL("advanced", "Infernal", "Chance to spawn 4 infernal minions to assist you fighting", 8, "Helmet"),
    ENRAGE("advanced", "Enrage", "Chance to gain strength 2 & speed 3 for a few seconds", 3, "Helmet"),
    DEFLECT("advanced", "Deflect", "Chance to remove a debuff and give it to your enemy when hit", 4, "Helmet"),
    VALIANT("extreme", "Valiant", "Take reduced damage from your enemies", 5, "Chestplate"),
    REPLICATE("extreme", "Replicate", "Chance for consumables to not be taken when consumed", 5, "Chestplate"),
    REFLECT("extreme", "Reflect", "Chance for you to reflect the enemies attack", 5, "Chestplate"),
    DISORDER("extreme", "Disorder", "Chance to shuffle the items in your opponents inventory", 5, "Chestplate");

    private final String description;
    private final int maxLevel;
    private final String applicableGear;
    private final String type;
    private final String displayName;

    CustomEnchantment(String type, String displayName, String description, int maxLevel, String applicableGear) {
        this.description = description;
        this.maxLevel = maxLevel;
        this.applicableGear = applicableGear;
        this.type = type;
        this.displayName = displayName;
    }

    public String getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    public int getMaxLevel() {
        return maxLevel;
    }

    public String getApplicableGear() {
        return applicableGear;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static CustomEnchantment fromName(String name) {
        for (CustomEnchantment enchantment : values()) {
            if (name.equalsIgnoreCase(enchantment.getDisplayName())) {
                return enchantment;
            }
        }
        return null;
    }
}
