package rip.tilly.bedwars.generators;

import java.util.Arrays;

public enum GeneratorTier {

    ONE,
    TWO,
    THREE,
    FOUR,
    FIVE;

    public String getFormattedName() {
        switch (this) {
            case ONE:
                return "I";
            case TWO:
                return "II";
            case THREE:
                return "III";
            case FOUR:
                return "IV";
            case FIVE:
                return "V";
        }

        return "I";
    }

    public static GeneratorTier getByName(String name) {
        return Arrays.stream(values()).filter(type -> type.name().equalsIgnoreCase(name)).findFirst().orElse(null);
    }
}
