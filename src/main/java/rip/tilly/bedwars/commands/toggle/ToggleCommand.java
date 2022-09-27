package rip.tilly.bedwars.commands.toggle;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import rip.tilly.bedwars.utils.CC;

public class ToggleCommand implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player player = (Player) sender;

        if (args.length == 0) {
            player.sendMessage(CC.translate("&cUsage:"));
            player.sendMessage(CC.translate("  &c/toggle"));
            player.sendMessage(CC.translate("  &c/toggle scoreboard"));
            player.sendMessage(CC.translate("  &c/toggle partychat"));
        } else {
            switch (args[0].toLowerCase()) {
                case "scoreboard":
                    new ScoreboardCommand().executeAs(sender, cmd, label, args);

                    break;
                case "pc":
                case "partychat":
                    new PartyChatCommand().executeAs(sender, cmd, label, args);

                    break;
            }
        }

        return true;
    }
}
