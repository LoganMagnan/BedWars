package rip.tilly.bedwars.runnables;

import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.scheduler.BukkitRunnable;
import rip.tilly.bedwars.BedWars;
import rip.tilly.bedwars.game.Game;
import rip.tilly.bedwars.game.GameState;

public class GameRunnable extends BukkitRunnable {

    private BedWars plugin = BedWars.getInstance();

    private Game game;

    public GameRunnable(Game game) {
        this.game = game;
    }

    @Override
    public void run() {
        switch (this.game.getGameState()) {
            case STARTING:
                if (this.game.decrementCountdown() == 0) {
                    this.game.setGameState(GameState.FIGHTING);
                    this.game.broadcastWithSound("&aThe match has started, good luck!", Sound.FIREWORK_BLAST);
                } else {
                    this.game.broadcastWithSound("&eStarting match in &d" + this.game.getCountdown() + " &eseconds...", Sound.NOTE_PLING);
                    this.game.broadcastTitle("&d&lStarting Match In...", "&e" + this.game.getCountdown());
                }

                break;
            case FIGHTING:
                this.game.incrementDuration();
                this.game.tick(this.game.getDurationTimer(), this.game);

                break;
            case ENDING:
                if (this.game.decrementCountdown() == 0) {
                    this.game.getEntitiesToRemove().forEach(Entity::remove);
                    this.game.getRunnables().forEach(runnable -> this.plugin.getServer().getScheduler().cancelTask(runnable));
                    this.game.getTeams().forEach(team -> team.playingPlayers().forEach(player -> this.plugin.getPlayerDataManager().resetPlayer(player, true)));
                    this.game.spectatorPlayers().forEach(this.plugin.getGameManager()::removeSpectator);

                    this.game.getDroppedItems().clear();

                    this.plugin.getChunkClearingManager().resetArena(this.game.getCopiedArena());
                    this.game.getArena().addAvailableArena(this.game.getCopiedArena());
                    this.plugin.getArenaManager().removeArenaGameUUID(this.game.getCopiedArena());

                    this.plugin.getGameManager().removeGame(this.game);
                    this.cancel();
                }

                break;
        }
    }
}
