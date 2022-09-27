package rip.tilly.bedwars.commands.party;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import rip.tilly.bedwars.BedWars;
import rip.tilly.bedwars.commands.BaseCommand;
import rip.tilly.bedwars.utils.CC;

import java.util.ArrayList;
import java.util.List;

public class InfoCommand extends BaseCommand {

    private BedWars main = BedWars.getInstance();

    @Override
    public void executeAs(CommandSender sender, Command cmd, String label, String[] args) {
        Player player = (Player) sender;
        
        if (this.main.getPartyManager().getParty(player.getUniqueId()) == null) {
            player.sendMessage(CC.translate("&cYou are not in a party!"));
            return;
        }

        player.sendMessage(CC.translate(CC.chatBar));
        player.sendMessage(CC.translate("&dParty Information"));
        player.sendMessage(CC.translate(CC.chatBar));
        player.sendMessage(CC.translate("&7⚫ &9Leader: &e" + Bukkit.getPlayer(this.main.getPartyManager().getParty(player.getUniqueId()).getLeader()).getName()));
        player.sendMessage(CC.translate("&7⚫ &9Party members: &e" + this.main.getPartyManager().getParty(player.getUniqueId()).getMembers().size() + "&7/&e" + this.main.getPartyManager().getParty(player.getUniqueId()).getLimit()));
        player.sendMessage(CC.translate(CC.chatBar));
    }

    @Override
    public List<String> getTabCompletions(CommandSender sender, Command cmd, String label, String[] args) {
        List<String> tabCompletions = new ArrayList<String>();

        return tabCompletions;
    }
}
