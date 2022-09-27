package rip.tilly.bedwars.listeners.game;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import rip.tilly.bedwars.BedWars;
import rip.tilly.bedwars.events.GameStartEvent;
import rip.tilly.bedwars.game.Game;
import rip.tilly.bedwars.game.arena.CopiedArena;
import rip.tilly.bedwars.generators.Generator;
import rip.tilly.bedwars.generators.GeneratorType;
import rip.tilly.bedwars.playerdata.PlayerData;
import rip.tilly.bedwars.playerdata.PlayerState;
import rip.tilly.bedwars.runnables.GameRunnable;
import rip.tilly.bedwars.utils.CC;
import rip.tilly.bedwars.utils.CustomLocation;
import rip.tilly.bedwars.utils.PlayerUtil;

import java.util.HashSet;
import java.util.Set;

public class GameStartListener implements Listener {

    private final BedWars plugin = BedWars.getInstance();

    @EventHandler
    public void onGameStart(GameStartEvent event) {
        Game game = event.getGame();

        if (game.getArena().getAvailableArenas().size() > 0) {
            game.setCopiedArena(game.getArena().getAvailableArena());
            this.plugin.getArenaManager().setArenaGameUUIDs(game.getCopiedArena(), game.getGameId());
        } else {
            game.broadcast(CC.translate("&cThere are no available arenas at this moment!"));
            this.plugin.getGameManager().removeGame(game);
            return;
        }

        CopiedArena currentArena = game.getCopiedArena();

        for (CustomLocation location : currentArena.getTeamGenerators()) {
            Generator teamGen1 = new Generator(location.toBukkitLocation(), GeneratorType.IRON, true, game);
            teamGen1.setActivated(true);

            game.getActivatedGenerators().add(teamGen1);

            Generator teamGen2 = new Generator(location.toBukkitLocation(), GeneratorType.GOLD, true, game);
            teamGen2.setActivated(true);

            game.getActivatedGenerators().add(teamGen2);
        }

        for (CustomLocation location : currentArena.getDiamondGenerators()) {
            Generator diaGen = new Generator(location.toBukkitLocation(), GeneratorType.DIAMOND, false, game);
            diaGen.setActivated(true);

            game.getActivatedGenerators().add(diaGen);
        }

        for (CustomLocation location : currentArena.getEmeraldGenerators()) {
            Generator emeGen = new Generator(location.toBukkitLocation(), GeneratorType.EMERALD, false, game);
            emeGen.setActivated(true);

            game.getActivatedGenerators().add(emeGen);
        }

        game.spawnVillagers();

        Set<Player> gamePlayers = new HashSet<>();
        game.getTeams().forEach(team -> team.playingPlayers().forEach(player -> {
            gamePlayers.add(player);
            this.plugin.getGameManager().removeGameRequests(player.getUniqueId());

            PlayerUtil.clearPlayer(player);

            PlayerData playerData = this.plugin.getPlayerDataManager().getPlayerData(player.getUniqueId());
            playerData.setCurrentGameId(game.getGameId());
            playerData.setTeamId(team.getId());
            playerData.setPlayerTeam(team.getPlayerTeam());

            playerData.setPlayerState(PlayerState.PLAYING);

            CustomLocation locationA = game.getCopiedArena().getA();
            CustomLocation locationB = game.getCopiedArena().getB();

            player.teleport(team.getId() == 1 ? locationA.toBukkitLocation() : locationB.toBukkitLocation());

            player.getInventory().setArmorContents(this.plugin.getGameManager().getGameStartArmor(playerData));
            for (ItemStack stack : this.plugin.getGameManager().getGameItems(playerData.getCurrentGameData(), playerData.getPlayerTeam().getTeamUpgrades())) {
                player.getInventory().addItem(stack);
            }
        }));

        for (Player player : gamePlayers) {
            for (Player online : this.plugin.getServer().getOnlinePlayers()) {
                online.hidePlayer(player);
                player.hidePlayer(online);
            }
        }

        for (Player player : gamePlayers) {
            for (Player other : gamePlayers) {
                player.showPlayer(other);
            }
        }

        new GameRunnable(game).runTaskTimer(this.plugin, 20L, 20L);
    }
}
