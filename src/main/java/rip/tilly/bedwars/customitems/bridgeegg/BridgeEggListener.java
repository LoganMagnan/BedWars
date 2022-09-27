package rip.tilly.bedwars.customitems.bridgeegg;

import org.bukkit.entity.Egg;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import rip.tilly.bedwars.BedWars;
import rip.tilly.bedwars.game.Game;
import rip.tilly.bedwars.playerdata.PlayerTeam;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("WeakerAccess")
public class BridgeEggListener implements Listener {

    private final BedWars plugin = BedWars.getInstance();
    private static final HashMap<Egg, BridgeEggRunnable> currentTasks = new HashMap<>();

    @EventHandler
    public void onProjectileLaunch(ProjectileLaunchEvent event) {
        if (event.getEntity() instanceof Egg) {
            Egg egg = (Egg) event.getEntity();
            if (egg.getShooter() instanceof Player) {
                Player player = (Player) egg.getShooter();
                Game game = this.plugin.getGameManager().getGame(player.getUniqueId());
                if (game != null) {
                    PlayerTeam playerTeam = this.plugin.getPlayerDataManager().getPlayerData(player.getUniqueId()).getPlayerTeam();
                    currentTasks.put(egg, new BridgeEggRunnable(this.plugin, egg, playerTeam, player, game));
                }
            }
        }
    }

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event) {
        if (event.getEntity() instanceof Egg) {
            removeEgg((Egg) event.getEntity());
        }
    }

    public static void removeEgg(Egg egg) {
        if (currentTasks.containsKey(egg)) {
            if (currentTasks.get(egg) != null) {
                currentTasks.get(egg).cancel();
            }
            currentTasks.remove(egg);
        }
    }

    public static Map<Egg, BridgeEggRunnable> getEggTasks() {
        return Collections.unmodifiableMap(currentTasks);
    }
}
