package rip.tilly.bedwars.commands.party;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import rip.tilly.bedwars.BedWars;
import rip.tilly.bedwars.commands.BaseCommand;
import rip.tilly.bedwars.playerdata.PlayerState;
import rip.tilly.bedwars.utils.CC;

import java.util.ArrayList;
import java.util.List;

public class LeaveCommand extends BaseCommand {

    private BedWars main = BedWars.getInstance();

    @Override
    public void executeAs(CommandSender sender, Command cmd, String label, String[] args) {
        Player player = (Player) sender;

        if (this.main.getPlayerDataManager().getPlayerData(player.getUniqueId()).getPlayerState() != PlayerState.SPAWN) {
            player.sendMessage(CC.translate("&cYou can't use this while not in the spawn!"));
            return;
        }

        this.main.getPartyManager().leaveParty(player);
    }

    @Override
    public List<String> getTabCompletions(CommandSender sender, Command cmd, String label, String[] args) {
        List<String> tabCompletions = new ArrayList<String>();

        return tabCompletions;
    }
}
