package rip.tilly.bedwars.commands.setspawn;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import rip.tilly.bedwars.BedWars;
import rip.tilly.bedwars.commands.BaseCommand;
import rip.tilly.bedwars.utils.CustomLocation;

import java.util.ArrayList;
import java.util.List;

public class SpawnCommand extends BaseCommand {

    private BedWars main = BedWars.getInstance();

    @Override
    public void executeAs(CommandSender sender, Command cmd, String label, String[] args) {
        Player player = (Player) sender;
        this.main.getSpawnManager().setSpawnLocation(CustomLocation.fromBukkitLocation(player.getLocation()));
        player.sendMessage(ChatColor.GREEN + "Successfully set the spawn location.");
        saveLocation(player, "SPAWN.LOCATION");
    }

    @Override
    public List<String> getTabCompletions(CommandSender sender, Command cmd, String label, String[] args) {
        List<String> tabCompletions = new ArrayList<>();

        return tabCompletions;
    }

    private void saveLocation(Player player, String location) {
        FileConfiguration config = this.main.getMainConfig().getConfig();
        config.set(location, CustomLocation.locationToString(CustomLocation.fromBukkitLocation(player.getLocation())));
        this.main.getMainConfig().save();
    }
}
