package rip.tilly.bedwars.commands.party;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import rip.tilly.bedwars.BedWars;
import rip.tilly.bedwars.commands.BaseCommand;
import rip.tilly.bedwars.managers.party.Party;
import rip.tilly.bedwars.utils.CC;

import java.util.ArrayList;
import java.util.List;

public class ChatCommand extends BaseCommand {

    private final BedWars plugin = BedWars.getInstance();

    @Override
    public void executeAs(CommandSender sender, Command cmd, String label, String[] args) {
        Player player = (Player) sender;

        Party party = this.plugin.getPartyManager().getParty(player.getUniqueId());
        if (args.length == 1) {
            player.performCommand("toggle partychat");
            return;
        } else {
            if (party != null) {
                StringBuilder stringBuilder = new StringBuilder();

                for (int i = 1; i < args.length; i++){
                    stringBuilder.append(args[i]).append(" ");
                }

                String allArgs = stringBuilder.toString().trim();

                String message = "&9Party > &d" + player.getDisplayName() + "&f: " + allArgs;
                party.broadcast(message);
            } else {
                player.sendMessage(CC.translate("&cYou are not in a party!"));
            }
        }
    }

    @Override
    public List<String> getTabCompletions(CommandSender sender, Command cmd, String label, String[] args) {
        List<String> tabCompletions = new ArrayList<String>();

        return tabCompletions;
    }
}
