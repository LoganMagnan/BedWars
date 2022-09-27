package rip.tilly.bedwars.listeners.game;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import rip.tilly.bedwars.BedWars;
import rip.tilly.bedwars.events.PlayerKillEvent;
import rip.tilly.bedwars.game.Game;
import rip.tilly.bedwars.game.GameTeam;
import rip.tilly.bedwars.playerdata.PlayerData;
import rip.tilly.bedwars.playerdata.PlayerState;
import rip.tilly.bedwars.playerdata.currentgame.PlayerCurrentGameData;
import rip.tilly.bedwars.runnables.RespawnRunnable;

public class PlayerKillListener implements Listener {

    private final BedWars plugin = BedWars.getInstance();

    @EventHandler
    public void onPlayerKill(PlayerKillEvent event) {
        Player player = event.getPlayer();
        Player killer = event.getKiller();

        PlayerData playerData = this.plugin.getPlayerDataManager().getPlayerData(player.getUniqueId());
        if (playerData == null) {
            this.plugin.getLogger().warning(player.getName() + "'s player data is null");
            return;
        }

        Game game = this.plugin.getGameManager().getGame(player.getUniqueId());
        if (game == null) {
            return;
        }

        GameTeam playerTeam = game.getTeams().get(playerData.getTeamId());
        if (playerTeam.isHasBed()) {
            playerData.setPlayerState(PlayerState.RESPAWNING);
            if (killer != null) {
                PlayerData killerData = this.plugin.getPlayerDataManager().getPlayerData(killer.getUniqueId());
                killerData.addRandomXp(killer);

                PlayerCurrentGameData currentGameData = playerData.getCurrentGameData();
                if (currentGameData.getAxeLevel() > 0) {
                    currentGameData.setAxeLevel(currentGameData.getAxeLevel() - 1);
                }
                if (currentGameData.getPickaxeLevel() > 0) {
                    currentGameData.setPickaxeLevel(currentGameData.getPickaxeLevel() - 1);
                }

                game.broadcast(playerData.getPlayerTeam().getChatColor() + player.getName() + " &ewas killed by " + killerData.getPlayerTeam().getChatColor() + killer.getName() + "&e!");
            } else {
                game.broadcast(playerData.getPlayerTeam().getChatColor() + player.getName() + " &efell into the void!");
            }

            new RespawnRunnable(this.plugin, player, playerData, game, playerTeam, 6, 6).runTaskTimer(this.plugin, 0, 20);
        } else {
            this.plugin.getGameManager().removePlayerFromGame(player, playerData, true);
        }
    }
}
