package rip.tilly.bedwars.listeners.game;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import rip.tilly.bedwars.BedWars;
import rip.tilly.bedwars.events.PlayerKillEvent;
import rip.tilly.bedwars.game.Game;
import rip.tilly.bedwars.game.GameState;
import rip.tilly.bedwars.game.GameTeam;
import rip.tilly.bedwars.playerdata.PlayerData;
import rip.tilly.bedwars.playerdata.PlayerState;
import rip.tilly.bedwars.upgrades.Upgrade;
import rip.tilly.bedwars.utils.CC;
import rip.tilly.bedwars.utils.LocationUtils;

public class MovementListener implements Listener {

    private final BedWars plugin = BedWars.getInstance();

    private boolean isInGame(PlayerData playerData) {
        return playerData.getPlayerState() == PlayerState.PLAYING;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        if (player.getGameMode() == GameMode.CREATIVE && player.isOp()) {
            return;
        }

        PlayerData playerData = this.plugin.getPlayerDataManager().getPlayerData(player.getUniqueId());

        if (playerData == null) {
            this.plugin.getLogger().warning(player.getName() + "'s player data is null");

            return;
        }

        if (playerData.getPlayerState() == PlayerState.RESPAWNING) {
            return;
        }

        if (!this.isInGame(playerData)) {
            return;
        }

        Game game = this.plugin.getGameManager().getGame(player.getUniqueId());

        if (game == null) {
            return;
        }

        Location to = event.getTo();
        Location from = event.getFrom();

        if (game.getGameState() == GameState.STARTING) {
            if (to.getX() != from.getX() || to.getZ() != from.getZ()) {
                player.teleport(from);

                return;
            }
        }

        GameTeam playerTeam = game.getTeams().get(playerData.getTeamId() == 0 ? 0 : 1);
        GameTeam opposingTeam = game.getTeams().get(playerData.getTeamId() == 0 ? 1 : 0);

        if (game.getGameState() != GameState.ENDING) {
            if (playerTeam.getPlayerTeam().getTeamUpgrades().getLevelForUpgrade(Upgrade.HEAL_POOL) != 0) {
                if (game.isInside(player.getLocation(), game)) {
                    if (!player.getActivePotionEffects().contains(PotionEffectType.REGENERATION)) {
                        player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 1000000, 0));
                    }
                } else {
                    if (player.getActivePotionEffects().contains(PotionEffectType.REGENERATION)) {
                        player.removePotionEffect(PotionEffectType.REGENERATION);
                    }
                }
            }

            if (opposingTeam.getPlayerTeam().getTeamUpgrades().getLevelForUpgrade(Upgrade.TRAP) != 0) {
                if (player.getLocation().distance(playerData.getTeamId() == 0 ? game.getCopiedArena().getA().toBukkitLocation() : game.getCopiedArena().getB().toBukkitLocation()) < 15) {
                    opposingTeam.getPlayerTeam().getTeamUpgrades().getUpgrades().remove(Upgrade.TRAP);

                    player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 100, 5));

                    opposingTeam.playingPlayers().forEach(enemyPlayer -> {
                        enemyPlayer.sendMessage(CC.translate("&cYour trap has been activated"));
                    });

                    game.broadcastTitleToOneTeam("&cTRAP ACTIVATED", "&eYour trap has been activated", opposingTeam);
                }
            }

            if (player.getLocation().getY() <= game.getArena().getDeadZone()) {
                Player killer = playerData.getLastDamager();
                if (killer != null) {
                    this.plugin.getServer().getPluginManager().callEvent(new PlayerKillEvent(player, killer));
                } else {
                    this.plugin.getServer().getPluginManager().callEvent(new PlayerKillEvent(player, null));
                }
            }
        }
    }
}
