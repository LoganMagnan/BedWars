package rip.tilly.bedwars.commands.arena;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import rip.tilly.bedwars.BedWars;
import rip.tilly.bedwars.commands.BaseCommand;
import rip.tilly.bedwars.game.arena.Arena;
import rip.tilly.bedwars.utils.CC;
import rip.tilly.bedwars.utils.CustomLocation;

import java.util.ArrayList;
import java.util.List;

public class SetMinCommand extends BaseCommand {

    private BedWars main = BedWars.getInstance();

    @Override
    public void executeAs(CommandSender sender, Command cmd, String label, String[] args) {
        Player player = (Player) sender;

        if (args.length == 0) {
            player.sendMessage(CC.translate("&cError: no arena found! /arena <args> <arena>"));
            return;
        }

        Arena arena = this.main.getArenaManager().getArena(args[1]);

        if (arena != null) {
            Location location = player.getLocation();

            arena.setMin(CustomLocation.fromBukkitLocation(location));

            player.sendMessage(CC.translate("&aSuccessfully set the minimum position for the arena called &a&l" + args[1]));
        } else {
            player.sendMessage(CC.translate("&cThis arena does not exist"));
        }
    }

    @Override
    public List<String> getTabCompletions(CommandSender sender, Command cmd, String label, String[] args) {
        List<String> tabCompletions = new ArrayList<String>();

        return tabCompletions;
    }
}
