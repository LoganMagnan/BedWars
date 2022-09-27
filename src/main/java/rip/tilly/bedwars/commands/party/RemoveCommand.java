package rip.tilly.bedwars.commands.party;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import rip.tilly.bedwars.BedWars;
import rip.tilly.bedwars.commands.BaseCommand;
import rip.tilly.bedwars.managers.party.Party;
import rip.tilly.bedwars.utils.CC;

import java.util.ArrayList;
import java.util.List;

public class RemoveCommand extends BaseCommand {

    private BedWars main = BedWars.getInstance();

    @Override
    public void executeAs(CommandSender sender, Command cmd, String label, String[] args) {
        Player player = (Player) sender;
        Player target = Bukkit.getPlayer(args[1]);

        Party party = this.main.getPartyManager().getParty(player.getUniqueId());
        if (party == null) {
            player.sendMessage(CC.translate("&cYou are not in a party!"));
            return;
        }

        if (party.getLeader() != player.getUniqueId()) {
            player.performCommand("party info");
            return;
        }

        if (target == null) {
            player.sendMessage(CC.translate("&cPlayer not found!"));
            return;
        }

        Party targetParty = this.main.getPartyManager().getParty(target.getUniqueId());
        if (targetParty == null || targetParty.getLeader() != party.getLeader()) {
            player.sendMessage(CC.translate("&cThat player is not in your party!"));
            return;
        }

        this.main.getPartyManager().leaveParty(target);
    }

    @Override
    public List<String> getTabCompletions(CommandSender sender, Command cmd, String label, String[] args) {
        List<String> tabCompletions = new ArrayList<String>();

        return tabCompletions;
    }
}
