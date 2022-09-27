package rip.tilly.bedwars.listeners.game;

import org.bukkit.Location;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import rip.tilly.bedwars.BedWars;
import rip.tilly.bedwars.events.PlayerKillEvent;
import rip.tilly.bedwars.game.Game;
import rip.tilly.bedwars.game.GameState;
import rip.tilly.bedwars.playerdata.PlayerData;
import rip.tilly.bedwars.playerdata.PlayerState;
import rip.tilly.bedwars.utils.CC;

public class DamageListener implements Listener {

    private final BedWars plugin = BedWars.getInstance();

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            return;
        }

        Player player = (Player) event.getEntity();
        PlayerData playerData = this.plugin.getPlayerDataManager().getPlayerData(player.getUniqueId());
        EntityDamageEvent.DamageCause cause = event.getCause();
        switch (playerData.getPlayerState()) {
            case PLAYING:
                Game game = this.plugin.getGameManager().getGame(player.getUniqueId());
                if (game.getGameState() != GameState.FIGHTING) {
                    event.setCancelled(true);
                }
                break;
            case RESPAWNING:
                Game respawnGame = this.plugin.getGameManager().getGame(player.getUniqueId());
                if (cause == EntityDamageEvent.DamageCause.VOID) {
                    Location gameLocation = playerData.getTeamId() == 1 ? respawnGame.getCopiedArena().getA().toBukkitLocation() : respawnGame.getCopiedArena().getB().toBukkitLocation();
                    player.teleport(gameLocation);
                }
                event.setCancelled(true);
                break;
            case SPECTATING:
                if (cause == EntityDamageEvent.DamageCause.VOID) {
                    Game spectatingGame = this.plugin.getGameManager().getSpectatingGame(player.getUniqueId());
                    Location location = spectatingGame.getCopiedArena().getA().toBukkitLocation();
                    player.teleport(location);
                }
                event.setCancelled(true);
                break;
            default:
                if (cause == EntityDamageEvent.DamageCause.VOID) {
                    Location spawnLocation = this.plugin.getSpawnManager().getSpawnLocation().toBukkitLocation();
                    player.teleport(spawnLocation);
                }
                event.setCancelled(true);
                break;
        }
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            return;
        }

        Player player = (Player) event.getEntity();
        Player damager;

        if (event.getDamager() instanceof Player) {
            damager = (Player) event.getDamager();
        } else if (event.getDamager() instanceof Arrow && ((Projectile) event.getDamager()).getShooter() != ((Player) event.getEntity()).getPlayer()) {
            damager = (Player) ((Projectile) event.getDamager()).getShooter();
        } else {
            return;
        }

        if (!player.canSee(damager) && damager.canSee(player)) {
            event.setCancelled(true);
            return;
        }

        PlayerData playerData = this.plugin.getPlayerDataManager().getPlayerData(player.getUniqueId());
        PlayerData damagerData = this.plugin.getPlayerDataManager().getPlayerData(damager.getUniqueId());

        if (playerData.getPlayerState() != PlayerState.PLAYING) {
            event.setCancelled(true);
            return;
        }
        if (damagerData.getPlayerState() != PlayerState.PLAYING) {
            event.setCancelled(true);
            return;
        }

        Game game = this.plugin.getGameManager().getGame(player.getUniqueId());
        if (game == null) {
            event.setDamage(0);
            return;
        }

        if (playerData.getPlayerTeam() == damagerData.getPlayerTeam()) {
            event.setCancelled(true);
            return;
        }

        playerData.setLastDamager(damager);
        double health = player.getHealth() - event.getFinalDamage();
        if (health < 0) {
            event.setCancelled(true);
            this.plugin.getServer().getPluginManager().callEvent(new PlayerKillEvent(player, damager));
        }

        if (!(event.getDamager() instanceof Arrow)) {
            return;
        }

        Arrow arrow = (Arrow) event.getDamager();
        if (!(arrow.getShooter() instanceof Player)) {
            return;
        }

        Player shooter = (Player) arrow.getShooter();
        if (player.getName().equals(shooter.getName())) {
            return;
        }

        if (health < 0) {
            event.setCancelled(true);
            this.plugin.getServer().getPluginManager().callEvent(new PlayerKillEvent(player, shooter));
        } else {
            shooter.sendMessage(CC.translate("&d" + player.getName() + " &eis now at &c" + health + "❤&e."));
        }
    }
}
