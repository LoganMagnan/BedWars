package rip.tilly.bedwars.commands.toggle;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import rip.tilly.bedwars.BedWars;
import rip.tilly.bedwars.commands.BaseCommand;
import rip.tilly.bedwars.playerdata.PlayerSettings;
import rip.tilly.bedwars.utils.CC;

import java.util.ArrayList;
import java.util.List;

public class ScoreboardCommand extends BaseCommand {

    @Override
    public void executeAs(CommandSender sender, Command cmd, String label, String[] args) {
        Player player = (Player) sender;

        PlayerSettings settings = BedWars.getInstance().getPlayerDataManager().getPlayerData(player.getUniqueId()).getPlayerSettings();
        settings.setScoreboardEnabled(!settings.isScoreboardEnabled());

        player.sendMessage(CC.translate(settings.isScoreboardEnabled() ? "&aScoreboard toggled to true!" : "&cScoreboard toggled to false!"));
    }

    @Override
    public List<String> getTabCompletions(CommandSender sender, Command cmd, String label, String[] args) {
        List<String> tabCompletions = new ArrayList<String>();

        return tabCompletions;
    }
}
