package rip.tilly.bedwars.upgrades;

public enum Upgrade {

    SHARPENED_SWORDS,
    PROTECTION,
    MANIAC_MINER,
    FASTER_FORGE,
    HEAL_POOL,
    DRAGON_BUFF,
    TRAP;

    public int getHighestLevel() {
        switch (this) {
            case SHARPENED_SWORDS:
            case HEAL_POOL:
            case DRAGON_BUFF:
            case TRAP:
                return 1;
            case PROTECTION:
            case FASTER_FORGE:
                return 4;
            case MANIAC_MINER:
                return 2;
        }

        return -1;
    }

    public int getCostForLevel(int level) {
        if (this.getHighestLevel() < level) {
            return -1;
        }

        switch (this) {
            case SHARPENED_SWORDS:
                return 4;
            case PROTECTION:
                if (level == 1) {
                    return 2;
                }

                if (level == 2) {
                    return 4;
                }

                if (level == 3) {
                    return 8;
                }

                return 16;
            case MANIAC_MINER:
                if (level == 1) {
                    return 2;
                }

                return 4;
            case FASTER_FORGE:
                if (level == 1) {
                    return 2;
                }

                if (level == 2) {
                    return 4;
                }

                if (level == 3) {
                    return 6;
                }

                return 8;
            case HEAL_POOL:
            case DRAGON_BUFF:
            case TRAP:
                return 1;
        }

        return -1;
    }

    public String getPerkForLevel(int level) {
        switch (this) {
            case SHARPENED_SWORDS:
            case DRAGON_BUFF:
            case TRAP:
                return null;
            case PROTECTION:
                return "Protection " + this.getNumberToRomanNumeral(level);
            case MANIAC_MINER:
                return "Haste " + this.getNumberToRomanNumeral(level);
            case FASTER_FORGE:
                if (level == 1) {
                    return "+50% Resources";
                }

                if (level == 2) {
                    return "+100% Resources";
                }

                if (level == 3) {
                    return "Spawn Emeralds";
                }

                return "+200% Resources";
            case HEAL_POOL:
                return "Regeneration " + this.getNumberToRomanNumeral(level);
        }

        return null;
    }

    public String getNumberToRomanNumeral(int number) {
        StringBuilder stringBuilder = new StringBuilder();

        int times = 0;

        String[] romans = {"I", "IV", "V", "IX", "X", "XL", "L", "XC", "C", "CD", "D", "CM", "M"};

        int[] ints = {1, 4, 5, 9, 10, 40, 50, 90, 100, 400, 500, 900, 1000};

        for (int i = ints.length - 1; i >= 0; i--) {
            times = number / ints[i];

            number %= ints[i];

            while (times > 0) {
                stringBuilder.append(romans[i]);

                times--;
            }
        }

        return stringBuilder.toString();
    }

    public String getFormattedName() {
        switch (this) {
            case SHARPENED_SWORDS:
                return "Sharpened Swords";
            case PROTECTION:
                return "Protection";
            case MANIAC_MINER:
                return "Maniac Miner";
            case FASTER_FORGE:
                return "Faster Forge";
            case HEAL_POOL:
                return "Heal Pool";
            case DRAGON_BUFF:
                return "Dragon Buff";
            case TRAP:
                return "Trap";
        }

        return "";
    }
}
