package rip.tilly.bedwars.managers.party;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import rip.tilly.bedwars.BedWars;
import rip.tilly.bedwars.game.GameTeam;
import rip.tilly.bedwars.playerdata.PlayerTeam;
import rip.tilly.bedwars.utils.CC;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
@Setter
public class Party {

    private final BedWars plugin = BedWars.getInstance();

    private UUID leader;
    private Set<UUID> members = new HashSet<>();
    private int limit = 8;
    private boolean open;

    private List<GameTeam> teams = new ArrayList<>();

    public Party(UUID leader) {
        this.leader = leader;
        this.members.add(leader);
    }

    public void addMember(UUID uuid) {
        this.members.add(uuid);
    }

    public void removeMember(UUID uuid) {
        this.members.remove(uuid);
    }

    public void broadcast(String message) {
        this.members().forEach(member -> member.sendMessage(CC.translate(message)));
    }

    private GameTeam findTeam(UUID uuid) {
        GameTeam team = null;
        for (GameTeam gameTeams : teams) {
            if (gameTeams.getAllPlayers().contains(uuid)) {
                team = gameTeams;
            }
        }

        return team;
    }

    public GameTeam findOpponent(UUID uuid) {
        GameTeam team = null;
        for (GameTeam gameTeams : teams) {
            if (!gameTeams.getAllPlayers().contains(uuid)) {
                team = gameTeams;
            }
        }

        return team;
    }

    public List<UUID> getPartySplitTeam(UUID uuid) {
        List<UUID> uuids = new ArrayList<>();
        for (UUID pUuid : this.findTeam(uuid).getPlayingPlayers()) {
            if (pUuid != uuid) {
                uuids.add(pUuid);
            }
        }

        return uuids;
    }

    public GameTeam[] split() {
        teams.clear();

        List<UUID> teamA = new ArrayList<>();
        List<UUID> teamB = new ArrayList<>();

        ThreadLocalRandom random = ThreadLocalRandom.current();

        for (UUID member : this.members) {
            if (teamA.size() == teamB.size()) {
                if (random.nextBoolean()) {
                    teamA.add(member);
                } else {
                    teamB.add(member);
                }
            } else {
                if (teamA.size() < teamB.size()) {
                    teamA.add(member);
                } else {
                    teamB.add(member);
                }
            }
        }

        GameTeam team1 = new GameTeam(teamA.get(0), teamA, 0, PlayerTeam.RED);
        GameTeam team2 = new GameTeam(teamB.get(0), teamB, 1, PlayerTeam.LIME);

        teams.add(team1);
        teams.add(team2);

        return new GameTeam[]{team1, team2};
    }

    public List<UUID> getPartyMembersExcludeMember(UUID uuid) {
        return members.stream().filter(m -> m != uuid).collect(Collectors.toList());
    }

    public List<UUID> getPartyMembersExcludeLeader() {
        return members.stream().filter(m -> !this.leader.equals(m)).collect(Collectors.toList());
    }

    public Stream<Player> members() {
        return this.members.stream().map(this.plugin.getServer()::getPlayer).filter(Objects::nonNull);
    }
}
