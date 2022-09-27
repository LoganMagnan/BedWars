package rip.tilly.bedwars.providers.placeholderapi;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import rip.tilly.bedwars.BedWars;
import rip.tilly.bedwars.playerdata.PlayerData;

public class PlaceholderAPIProvider extends PlaceholderExpansion {

    final private BedWars plugin;

    public PlaceholderAPIProvider(BedWars plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getIdentifier() {
        return this.plugin.getDescription().getName().toLowerCase();
    }

    @Override
    public String getAuthor() {
        return this.plugin.getDescription().getAuthors().get(0);
    }

    @Override
    public String getVersion() {
        return this.plugin.getDescription().getVersion();
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public String onPlaceholderRequest(Player player, String identifier) {
        if (player == null) {
            return ChatColor.RED + "No data saved!";
        }

        PlayerData playerData = this.plugin.getPlayerDataManager().getPlayerData(player.getUniqueId());
        if (playerData == null) {
            return ChatColor.RED + "No data saved!";
        }

        // %bedwars_kills% would show the players kills.
        switch (identifier.toLowerCase()) {
            case "kills":
                return String.valueOf(playerData.getKills());
            case "deaths":
                return String.valueOf(playerData.getDeaths());
            case "xp":
                return String.valueOf(playerData.getXp());
            case "level":
                return String.valueOf(playerData.getLevel());
            case "wins":
                return String.valueOf(playerData.getWins());
            case "losses":
                return String.valueOf(playerData.getLosses());
            case "gamesplayed":
                return String.valueOf(playerData.getGamesPlayed());
            case "bedsdestroyed":
                return String.valueOf(playerData.getBedsDestroyed());
        }

        return ChatColor.RED + "No data saved!";
    }
}
