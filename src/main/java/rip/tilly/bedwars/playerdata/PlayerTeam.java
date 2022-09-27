package rip.tilly.bedwars.playerdata;

import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import rip.tilly.bedwars.playerdata.currentgame.TeamUpgrades;

import java.util.Arrays;

@Getter
public enum PlayerTeam {

    WHITE("White", Color.WHITE, ChatColor.WHITE, 0, "W"),
    SILVER("Silver", Color.SILVER, ChatColor.GRAY, 8, "S"),
    GRAY("Gray", Color.GRAY, ChatColor.DARK_GRAY, 7, "G"),
    BLACK("Black", Color.BLACK, ChatColor.BLACK, 15, "B"),
    BLUE("Blue", Color.BLUE, ChatColor.BLUE, 11, "B"),
    CYAN("Cyan", Color.TEAL, ChatColor.DARK_AQUA, 9, "C"),
    AQUA("Aqua", Color.AQUA, ChatColor.AQUA, 3, "A"),
    LIME("Lime", Color.LIME, ChatColor.GREEN, 5, "L"),
    GREEN("Green", Color.GREEN, ChatColor.DARK_GREEN, 13, "G"),
    YELLOW("Yellow", Color.YELLOW, ChatColor.YELLOW, 4, "Y"),
    ORANGE("Orange", Color.ORANGE, ChatColor.GOLD, 1, "O"),
    RED("Red", Color.RED, ChatColor.RED, 14, "R"),
    PURPLE("Purple", Color.PURPLE, ChatColor.DARK_PURPLE, 10, "P"),
    PINK("Pink", Color.FUCHSIA, ChatColor.LIGHT_PURPLE, 6, "P");

    private final String name;
    private final Color color;
    private final ChatColor chatColor;
    private final int colorData;
    private final String smallName;
    private final TeamUpgrades teamUpgrades;

    PlayerTeam(String name, Color color, ChatColor chatColor, int colorData, String smallName) {
        this.name = name;
        this.color = color;
        this.chatColor = chatColor;
        this.colorData = colorData;
        this.smallName = smallName;
        this.teamUpgrades = new TeamUpgrades();
    }

    public static PlayerTeam getFromName(String name) {
        return Arrays.stream(values()).filter((team) -> team.name.equalsIgnoreCase(name) || team.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }
}
