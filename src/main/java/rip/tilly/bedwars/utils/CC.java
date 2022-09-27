package rip.tilly.bedwars.utils;

import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;

public class CC {

    public static final String scoreboardBar = ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "----------------------";
    public static final String chatBar = ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "--------------------------------------------";

    public static String translate(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static List<String> translate(List<String> lines) {
        List<String> strings = new ArrayList<>();
        for (String line : lines) {
            strings.add(ChatColor.translateAlternateColorCodes('&', line));
        }
        return strings;
    }

    public static List<String> translate(String[] lines) {
        List<String> strings = new ArrayList<>();
        for (String line : lines) {
            if (line != null) {
                strings.add(ChatColor.translateAlternateColorCodes('&', line));
            }
        }
        return strings;
    }

    public static boolean isNumeric(String string) {
        return regexNumeric(string).length() == 0;
    }

    public static String regexNumeric(String string) {
        return string.replaceAll("[0-9]", "").replaceFirst("\\.", "");
    }
}
