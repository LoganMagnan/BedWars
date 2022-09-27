package rip.tilly.bedwars.commands.level;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import rip.tilly.bedwars.BedWars;
import rip.tilly.bedwars.utils.CC;

public class LevelCommand implements CommandExecutor {

    private BedWars main = BedWars.getInstance();

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player player = (Player) sender;

        if (!player.hasPermission("bedwars.admin")) {
            player.sendMessage(CC.translate("&cNo permission"));

            return true;
        }

        if (args.length == 0) {
            player.sendMessage(CC.translate("&cUsage:"));
            player.sendMessage(CC.translate("  &c/level"));
            player.sendMessage(CC.translate("  &c/level set <player> <amount>"));
            player.sendMessage(CC.translate("  &c/level add <player> <amount>"));
            player.sendMessage(CC.translate("  &c/level remove <player> <amount>"));
        } else {
            switch (args[0].toLowerCase()) {
                case "set":
                    new SetCommand().executeAs(sender, cmd, label, args);

                    break;
                case "add":
                    new AddCommand().executeAs(sender, cmd, label, args);

                    break;
                case "remove":
                    new RemoveCommand().executeAs(sender, cmd, label, args);

                    break;
            }
        }

        return true;
    }
}
