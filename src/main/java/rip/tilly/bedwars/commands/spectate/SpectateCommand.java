package rip.tilly.bedwars.commands.spectate;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import rip.tilly.bedwars.BedWars;
import rip.tilly.bedwars.game.Game;
import rip.tilly.bedwars.playerdata.PlayerData;
import rip.tilly.bedwars.playerdata.PlayerState;
import rip.tilly.bedwars.utils.CC;

public class SpectateCommand implements CommandExecutor {

    private final BedWars plugin = BedWars.getInstance();

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player player = (Player) sender;
        PlayerData playerData = this.plugin.getPlayerDataManager().getPlayerData(player.getUniqueId());

        if (args.length == 0) {
            player.sendMessage(CC.translate("&cUsage: /spectate <player>"));
        } else {
            if (playerData.getPlayerState() != PlayerState.SPAWN) {
                player.sendMessage(CC.translate("&cYou need to be in the spawn to be able use this!"));
                return true;
            }

            Player target = this.plugin.getServer().getPlayer(args[0]);
            if (target == null) {
                player.sendMessage(CC.translate("&cError: player not found!"));
                return true;
            }

            PlayerData targetData = this.plugin.getPlayerDataManager().getPlayerData(target.getUniqueId());
            if (targetData.getPlayerState() != PlayerState.PLAYING) {
                player.sendMessage(CC.translate("&cError: that player is not in a game!"));
                return true;
            }

            Game game = this.plugin.getGameManager().getGame(targetData);
            this.plugin.getGameManager().addSpectator(player, playerData, target, game);
        }

        return true;
    }
}
