package rip.tilly.bedwars.commands.level;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import rip.tilly.bedwars.BedWars;
import rip.tilly.bedwars.commands.BaseCommand;
import rip.tilly.bedwars.playerdata.PlayerData;
import rip.tilly.bedwars.utils.CC;

import java.util.ArrayList;
import java.util.List;

public class AddCommand extends BaseCommand {

    private BedWars main = BedWars.getInstance();

    @Override
    public void executeAs(CommandSender sender, Command cmd, String label, String[] args) {
        Player player = (Player) sender;

        Player target = this.main.getServer().getPlayer(args[1]);

        PlayerData playerData = this.main.getPlayerDataManager().getPlayerData(player.getUniqueId());

        if (!target.isOnline()) {
            player.sendMessage(CC.translate("&cError: Player is not currently online"));

            return;
        }

        if (!CC.isNumeric(args[2])) {
            player.sendMessage(CC.translate("&cError: Please send a valid number"));

            return;
        }

        Integer amount = Integer.parseInt(args[2]);

        playerData.setLevel(playerData.getLevel() + amount);
    }

    @Override
    public List<String> getTabCompletions(CommandSender sender, Command cmd, String label, String[] args) {
        List<String> tabCompletions = new ArrayList<String>();

        return tabCompletions;
    }
}
