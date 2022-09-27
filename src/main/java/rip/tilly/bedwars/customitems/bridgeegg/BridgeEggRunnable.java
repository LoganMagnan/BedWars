package rip.tilly.bedwars.customitems.bridgeegg;

import lombok.Getter;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Egg;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import rip.tilly.bedwars.BedWars;
import rip.tilly.bedwars.game.Game;
import rip.tilly.bedwars.playerdata.PlayerTeam;

@Getter
@SuppressWarnings("WeakerAccess")
public class BridgeEggRunnable implements Runnable {

    private final BedWars plugin;
    private final Egg egg;
    private final PlayerTeam playerTeam;
    private final Player player;
    private final Game game;
    private final BukkitTask bukkitTask;

    public BridgeEggRunnable(BedWars plugin, Egg egg, PlayerTeam playerTeam, Player player, Game game) {
        this.plugin = plugin;
        this.egg = egg;
        this.playerTeam = playerTeam;
        this.player = player;
        this.game = game;
        this.bukkitTask = this.plugin.getServer().getScheduler().runTaskTimer(this.plugin, this, 0, 1);
    }

    public void run() {
        Location location = this.egg.getLocation();
        if (this.egg.isDead() || this.player.getLocation().distance(location) > 27 || this.player.getLocation().getY() - location.getY() > 9) {
            BridgeEggListener.removeEgg(this.egg);
            return;
        }

        if (this.player.getLocation().distance(location) > 4) {

            Block block1 = location.clone().subtract(0, 2, 0).getBlock();
            if (!this.game.isInside(block1.getLocation(), this.game)) {
                if (block1.getType() == Material.AIR) {
                    block1.setType(Material.WOOL);
                    block1.setData((byte) this.playerTeam.getColorData());
                    this.game.addPlacedBlock(block1);
                    location.getWorld().playEffect(block1.getLocation(), Effect.MOBSPAWNER_FLAMES, 3);
                    this.player.playSound(location, Sound.CHICKEN_EGG_POP, 1, 1);
                }
            }

            Block block2 = location.clone().subtract(1, 2, 0).getBlock();
            if (!this.game.isInside(block2.getLocation(), this.game)) {
                if (block2.getType() == Material.AIR) {
                    block2.setType(Material.WOOL);
                    block2.setData((byte) this.playerTeam.getColorData());
                    this.game.addPlacedBlock(block2);
                    location.getWorld().playEffect(block2.getLocation(), Effect.MOBSPAWNER_FLAMES, 3);
                    this.player.playSound(location, Sound.CHICKEN_EGG_POP, 1, 1);
                }
            }

            Block block3 = location.clone().subtract(0, 2, 1).getBlock();
            if (!this.game.isInside(block3.getLocation(), this.game)) {
                if (block3.getType() == Material.AIR) {
                    block3.setType(Material.WOOL);
                    block3.setData((byte) this.playerTeam.getColorData());
                    this.game.addPlacedBlock(block3);
                    location.getWorld().playEffect(block3.getLocation(), Effect.MOBSPAWNER_FLAMES, 3);
                    this.player.playSound(location, Sound.CHICKEN_EGG_POP, 1, 1);
                }
            }
        }
    }

    public void cancel() {
        this.bukkitTask.cancel();
    }
}
