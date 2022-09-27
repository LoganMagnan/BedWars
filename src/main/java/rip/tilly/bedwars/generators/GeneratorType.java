package rip.tilly.bedwars.generators;

public enum GeneratorType {

    IRON,
    GOLD,
    DIAMOND,
    EMERALD;

    public String getColorCode() {
        switch (this) {
            case IRON:
                return "&f";
            case GOLD:
                return "&6";
            case DIAMOND:
                return "&b";
            case EMERALD:
                return "&a";
        }

        return "&f";
    }
}
