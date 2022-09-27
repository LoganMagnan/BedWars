package rip.tilly.bedwars.playerdata;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import rip.tilly.bedwars.BedWars;
import rip.tilly.bedwars.managers.PlayerDataManager;
import rip.tilly.bedwars.playerdata.currentgame.PlayerCurrentGameData;
import rip.tilly.bedwars.utils.CC;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Getter
@Setter
public class PlayerData {

    private final PlayerDataManager playerDataManager = BedWars.getInstance().getPlayerDataManager();
    private PlayerState playerState = PlayerState.SPAWN;

    private PlayerSettings playerSettings = new PlayerSettings();
    private PlayerCurrentGameData currentGameData = new PlayerCurrentGameData();

    private PlayerTeam playerTeam  = PlayerTeam.PINK;

    private final UUID uniqueId;
    private boolean loaded;

    private UUID currentGameId;
    private int teamId;

    private int level;
    private double xp;
    private int wins;
    private int losses;
    private int kills;
    private int deaths;
    private int gamesPlayed;
    private int bedsDestroyed;

    private Player lastDamager;

    public PlayerData(UUID uniqueId) {
        this.uniqueId = uniqueId;
        this.loaded = false;

        this.playerDataManager.loadPlayerData(this);
    }

    public void addRandomXp(Player player) {
        double xp = ThreadLocalRandom.current().nextDouble(0.01, 0.05);

        this.xp += xp;

        player.sendMessage(CC.translate("&b&l+" + ((int) (xp * 100)) + "&b&l% XP"));

        if (this.xp >= 1) {
            this.level += 1;
            this.xp = this.xp - (long) this.xp;
        }
    }
}
