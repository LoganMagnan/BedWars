package rip.tilly.bedwars.game;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import rip.tilly.bedwars.BedWars;
import rip.tilly.bedwars.playerdata.PlayerTeam;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Stream;

@Getter
@Setter
public class GameTeam {

    protected final BedWars plugin = BedWars.getInstance();

    private final List<UUID> allPlayers;
    private final List<UUID> playingPlayers = new ArrayList<>();
    private UUID leader;

    private final int id;
    private final PlayerTeam playerTeam;

    private boolean hasBed = true;

    public GameTeam(UUID leader, List<UUID> allPlayers, int id, PlayerTeam playerTeam) {
        this.leader = leader;
        this.allPlayers = allPlayers;
        this.playingPlayers.addAll(allPlayers);

        this.id = id;
        this.playerTeam = playerTeam;
    }

    public void killPlayer(UUID uuid) {
        this.playingPlayers.remove(uuid);
    }

    public Stream<Player> allPlayers() {
        return this.allPlayers.stream().map(this.plugin.getServer()::getPlayer).filter(Objects::nonNull);
    }

    public Stream<Player> playingPlayers() {
        return this.playingPlayers.stream().map(this.plugin.getServer()::getPlayer).filter(Objects::nonNull);
    }

    public void destroyBed() {
        this.hasBed = false;
    }
}
