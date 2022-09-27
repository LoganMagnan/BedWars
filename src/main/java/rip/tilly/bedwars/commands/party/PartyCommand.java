package rip.tilly.bedwars.commands.party;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import rip.tilly.bedwars.utils.CC;

public class PartyCommand implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player player = (Player) sender;

        if (args.length == 0) {
            player.sendMessage(CC.translate(CC.chatBar));
            player.sendMessage(CC.translate("&dParty Commands"));
            player.sendMessage(CC.translate(CC.chatBar));
            player.sendMessage(CC.translate("&7⚫ &9/party &7- &eParty help information"));
            player.sendMessage(CC.translate("&7⚫ &9/party create &7- &eCreate a party"));
            player.sendMessage(CC.translate("&7⚫ &9/party leave &7- &eLeave a party"));
            player.sendMessage(CC.translate("&7⚫ &9/party join <player> &7- &eJoin a party"));
            player.sendMessage(CC.translate("&7⚫ &9/party accept <player> &7- &eAccept a party invitation"));
            player.sendMessage(CC.translate("&7⚫ &9/party invite <player> &7- &eInvite a player to a party"));
            player.sendMessage(CC.translate("&7⚫ &9/party remove <player> &7- &eRemove a player from a party"));
            player.sendMessage(CC.translate("&7⚫ &9/party info &7- &eCheck a party's information"));
            player.sendMessage(CC.translate(CC.chatBar));
        } else {
            switch (args[0].toLowerCase()) {
                case "create":
                    new CreateCommand().executeAs(sender, cmd, label, args);

                    break;
                case "leave":
                    new LeaveCommand().executeAs(sender, cmd, label, args);

                    break;
                case "join":
                    new JoinCommand().executeAs(sender, cmd, label, args);

                    break;
                case "accept":
                    new AcceptCommand().executeAs(sender, cmd, label, args);

                    break;
                case "inv":
                case "invite":
                    new InviteCommand().executeAs(sender, cmd, label, args);

                    break;
                case "remove":
                    new RemoveCommand().executeAs(sender, cmd, label, args);

                    break;
                case "info":
                    new InfoCommand().executeAs(sender, cmd, label, args);

                    break;
                case "c":
                case "chat":
                    new ChatCommand().executeAs(sender, cmd, label, args);

                    break;
            }
        }

        return true;
    }
}
