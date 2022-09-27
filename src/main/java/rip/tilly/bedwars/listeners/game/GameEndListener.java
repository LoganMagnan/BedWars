package rip.tilly.bedwars.listeners.game;

import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.github.paperspigot.Title;
import rip.tilly.bedwars.BedWars;
import rip.tilly.bedwars.events.GameEndEvent;
import rip.tilly.bedwars.game.Game;
import rip.tilly.bedwars.game.GameState;
import rip.tilly.bedwars.game.GameTeam;
import rip.tilly.bedwars.generators.Generator;
import rip.tilly.bedwars.playerdata.PlayerData;
import rip.tilly.bedwars.playerdata.currentgame.TeamUpgrades;
import rip.tilly.bedwars.utils.CC;
import rip.tilly.bedwars.utils.PlayerUtil;

public class GameEndListener implements Listener {

    private final BedWars plugin = BedWars.getInstance();

    @EventHandler
    public void onGameEnd(GameEndEvent event) {
        Game game = event.getGame();
        game.setGameState(GameState.ENDING);
        game.setCountdown(3);

        GameTeam winningTeam = event.getWinningTeam();
        GameTeam losingTeam = event.getLosingTeam();

        game.setWinningTeamId(winningTeam.getId());

        for (Generator generator : game.getActivatedGenerators()) {
            generator.setActivated(false);
        }

        game.getTeams().forEach(team -> team.allPlayers().forEach(player -> {
            PlayerUtil.clearPlayer(player);

            PlayerData playerData = this.plugin.getPlayerDataManager().getPlayerData(player.getUniqueId());

            TeamUpgrades teamUpgrades = playerData.getPlayerTeam().getTeamUpgrades();

            playerData.setLastDamager(null);
            playerData.getCurrentGameData().setGameKills(0);
            playerData.getCurrentGameData().setGameBedsDestroyed(0);

            teamUpgrades.getUpgrades().clear();

            String winnerTitle = CC.translate("&aVICTORY!");
            String losingTitle = CC.translate("&cDEFEAT!");
            String subTitle = CC.translate(winningTeam.getPlayerTeam().getChatColor() + winningTeam.getPlayerTeam().getName() + " &fhas won the game!");

            if (winningTeam.getAllPlayers().contains(player.getUniqueId())) {
                player.sendTitle(new Title(winnerTitle, subTitle, 5, 20, 5));
                playerData.setWins(playerData.getWins() + 1);
                playerData.addRandomXp(player);
            } else if (losingTeam.getAllPlayers().contains(player.getUniqueId())) {
                player.sendTitle(new Title(losingTitle, subTitle, 5, 20, 5));
                playerData.setLosses(playerData.getLosses() + 1);
            }

            playerData.setGamesPlayed(playerData.getGamesPlayed() + 1);
        }));

        for (Entity entity : game.getEntitiesToRemove()) {
            entity.remove();
        }
    }
}
