package rip.tilly.bedwars.commands.setspawn;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import rip.tilly.bedwars.utils.CC;

public class SetSpawnCommand implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player player = (Player) sender;

        if (!player.hasPermission("bedwars.admin")) {
            player.sendMessage(CC.translate("&cNo permission"));
            return true;
        }

        if (args.length == 0) {
            player.sendMessage(CC.translate("&cUsage:"));
            player.sendMessage(CC.translate("  &c/setspawn spawn"));
            player.sendMessage(CC.translate("  &c/setspawn min"));
            player.sendMessage(CC.translate("  &c/setspawn max"));
            player.sendMessage(CC.translate("  &c/setspawn holograms"));
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "spawn":
                new SpawnCommand().executeAs(sender, cmd, label, args);
                break;
            case "min":
                new MinCommand().executeAs(sender, cmd, label, args);
                break;
            case "max":
                new MaxCommand().executeAs(sender, cmd, label, args);
                break;
            case "holograms":
                break;
        }

        return true;
    }
}
