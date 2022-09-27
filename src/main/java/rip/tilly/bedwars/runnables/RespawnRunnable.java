package rip.tilly.bedwars.runnables;

import lombok.AllArgsConstructor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.github.paperspigot.Title;
import rip.tilly.bedwars.BedWars;
import rip.tilly.bedwars.game.Game;
import rip.tilly.bedwars.game.GameTeam;
import rip.tilly.bedwars.playerdata.PlayerData;
import rip.tilly.bedwars.playerdata.PlayerState;
import rip.tilly.bedwars.playerdata.currentgame.TeamUpgrades;
import rip.tilly.bedwars.upgrades.Upgrade;
import rip.tilly.bedwars.utils.CC;

@AllArgsConstructor
public class RespawnRunnable extends BukkitRunnable {

    private final BedWars plugin;
    private final Player player;
    private final PlayerData playerData;
    private final Game game;
    private final GameTeam gameTeam;
    private final int startingTime;
    private int respawnTime;

    @Override
    public void run() {
        if (!this.player.isOnline()) {
            cancel();
            return;
        }
        if (this.playerData.getPlayerState() == PlayerState.SPAWN) {
            cancel();
            return;
        }

        if (this.respawnTime <= 1) {
            this.plugin.getServer().getScheduler().runTaskLater(this.plugin, () -> this.playerData.setPlayerState(PlayerState.PLAYING), 20L);

            this.game.getTeams().forEach(team -> team.playingPlayers().forEach(gamePlayer -> gamePlayer.showPlayer(this.player)));
            this.player.teleport(this.gameTeam.getId() == 1 ? this.game.getCopiedArena().getA().toBukkitLocation() : this.game.getCopiedArena().getB().toBukkitLocation());

            this.player.sendTitle(new Title(CC.translate("&aRespawning..."), "", 1, 20, 0));
            this.player.sendMessage(CC.translate("&aYou have respawned!"));
            this.player.playSound(this.player.getLocation(), Sound.ORB_PICKUP, 10F, 1F);

            PlayerData playerData = this.plugin.getPlayerDataManager().getPlayerData(this.player.getUniqueId());

            TeamUpgrades teamUpgrades = playerData.getPlayerTeam().getTeamUpgrades();
            teamUpgrades.giveTeamArmor(this.player);

            for (ItemStack stack : this.plugin.getGameManager().getGameItems(this.playerData.getCurrentGameData(), teamUpgrades)) {
                this.player.getInventory().addItem(stack);
            }

            if (teamUpgrades.getLevelForUpgrade(Upgrade.MANIAC_MINER) != 0) {
                this.player.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 1000000, teamUpgrades.getLevelForUpgrade(Upgrade.MANIAC_MINER)));
            }

            this.game.getTeams().forEach(team -> team.playingPlayers().filter(player1 -> !this.player.equals(player1))
                    .forEach(matchplayer -> matchplayer.sendMessage(CC.translate(this.gameTeam.getPlayerTeam().getChatColor() + this.player.getName() + " &ehas respawned!"))));

            this.player.setHealth(this.player.getMaxHealth());
            this.player.setFoodLevel(20);
            this.playerData.setLastDamager(null);

            this.player.setAllowFlight(false);
            this.player.setFlying(false);

            cancel();
            return;
        }

        if (this.respawnTime == this.startingTime) {
            this.playerData.setPlayerState(PlayerState.RESPAWNING);
            this.game.getTeams().forEach(team -> team.playingPlayers().forEach(gamePlayer -> gamePlayer.hidePlayer(this.player)));

            this.player.getInventory().clear();
            this.player.getInventory().setArmorContents(null);
            this. player.updateInventory();

            this.player.setHealth(this.player.getMaxHealth());
            this.player.setFoodLevel(20);

            this. player.setVelocity(this.player.getVelocity().add(new Vector(0, 0.25, 0)));
            this.player.setAllowFlight(true);
            this.player.setFlying(true);
            this.player.setVelocity(this.player.getVelocity().add(new Vector(0, 0.15, 0)));
            this. player.setAllowFlight(true);
            this. player.setFlying(true);
        }

        this.respawnTime--;
        this.player.sendTitle(new Title(CC.translate("&e" + this.respawnTime), " ", 5, 20, 5));
        this.player.playSound(this.player.getLocation(), Sound.NOTE_PLING, 0.7F, 1.0F);
    }
}
