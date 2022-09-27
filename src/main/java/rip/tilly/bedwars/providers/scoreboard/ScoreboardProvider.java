package rip.tilly.bedwars.providers.scoreboard;

import me.clip.placeholderapi.PlaceholderAPI;
import org.apache.commons.lang3.StringEscapeUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import rip.tilly.bedwars.BedWars;
import rip.tilly.bedwars.game.Game;
import rip.tilly.bedwars.game.GameTeam;
import rip.tilly.bedwars.managers.party.Party;
import rip.tilly.bedwars.managers.queue.QueueEntry;
import rip.tilly.bedwars.playerdata.PlayerData;
import rip.tilly.bedwars.playerdata.PlayerState;
import rip.tilly.bedwars.utils.CC;
import rip.tilly.bedwars.utils.TimeUtils;
import rip.tilly.bedwars.utils.aether.scoreboard.Board;
import rip.tilly.bedwars.utils.aether.scoreboard.BoardAdapter;
import rip.tilly.bedwars.utils.aether.scoreboard.cooldown.BoardCooldown;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class ScoreboardProvider implements BoardAdapter {

    private final BedWars plugin = BedWars.getInstance();

    @Override
    public String getTitle(Player player) {
        return CC.translate("&d&lBedWars");
    }

    @Override
    public List<String> getScoreboard(Player player, Board board, Set<BoardCooldown> cooldowns) {
        PlayerData playerData = this.plugin.getPlayerDataManager().getPlayerData(player.getUniqueId());

        if (playerData == null) {
            return null;
        }

        if (!playerData.getPlayerSettings().isScoreboardEnabled()) {
            return null;
        }

        switch (playerData.getPlayerState()) {
            case SPAWN:
            case QUEUE:
                return this.spawnScoreboard(playerData);
            case RESPAWNING:
            case PLAYING:
                return this.playingScoreboard(playerData);
            case SPECTATING:
                return this.spectatingScoreboard(playerData);
        }

        return null;
    }

    @Override
    public void onScoreboardCreate(Player player, Scoreboard scoreboard) {
        if (scoreboard != null) {
            Team red = scoreboard.getTeam("red");
            if (red == null) {
                red = scoreboard.registerNewTeam("red");
            }

            Team green = scoreboard.getTeam("green");
            if (green == null) {
                green = scoreboard.registerNewTeam("green");
            }

            red.setPrefix(String.valueOf(ChatColor.RED));
            green.setPrefix(String.valueOf(ChatColor.GREEN));

            PlayerData playerData = this.plugin.getPlayerDataManager().getPlayerData(player.getUniqueId());
            if (playerData.getPlayerState() != PlayerState.PLAYING && playerData.getPlayerState() != PlayerState.RESPAWNING) {
                Objective objective = player.getScoreboard().getObjective(DisplaySlot.BELOW_NAME);
                if (objective != null) {
                    objective.unregister();
                }

                for (String entry : red.getEntries()) {
                    red.removeEntry(entry);
                }

                for (String entry : green.getEntries()) {
                    green.removeEntry(entry);
                }

                for (Player online : Bukkit.getOnlinePlayers()) {
                    if (online == null) return;

                    Team spawn = scoreboard.getTeam(online.getName());
                    if (spawn == null) {
                        spawn = scoreboard.registerNewTeam(online.getName());
                    }

                    if (online == player) {
                        spawn.setPrefix(CC.translate(PlaceholderAPI.setPlaceholders(player, "%aqua_player_color%")));
                    } else {
                        spawn.setPrefix(CC.translate(PlaceholderAPI.setPlaceholders(online, "%aqua_player_color%")));
                    }

                    String onlinePlayer = online.getName();
                    if (spawn.hasEntry(onlinePlayer)) {
                        continue;
                    }
                    spawn.addEntry(onlinePlayer);

                    return;
                }

                return;
            }

            Game game = this.plugin.getGameManager().getGame(player.getUniqueId());
            Objective objective = player.getScoreboard().getObjective(DisplaySlot.BELOW_NAME);
            if (objective == null) {
                objective = player.getScoreboard().registerNewObjective("showhealth", "health");
            }

            objective.setDisplaySlot(DisplaySlot.BELOW_NAME);
            objective.setDisplayName(ChatColor.RED + StringEscapeUtils.unescapeJava("\u2764"));
            objective.getScore(player.getName()).setScore((int) Math.floor(player.getHealth()));

            for (GameTeam team : game.getTeams()) {
                for (UUID teamUUID : team.getPlayingPlayers()) {
                    Player teamPlayer = this.plugin.getServer().getPlayer(teamUUID);
                    if (teamPlayer != null) {

                        String teamPlayerName = teamPlayer.getName();
                        if (team.getId() == 1) {
                            if (green.hasEntry(teamPlayerName)) {
                                continue;
                            }
                            green.addEntry(teamPlayerName);
                        } else {
                            if (red.hasEntry(teamPlayerName)) {
                                continue;
                            }
                            red.addEntry(teamPlayerName);
                        }
                    }
                }
            }
        }
    }

    private List<String> spawnScoreboard(PlayerData playerData) {
        List<String> lines = new ArrayList<>();

        lines.add(CC.scoreboardBar);

        Party party = this.plugin.getPartyManager().getParty(playerData.getUniqueId());
        if (party != null) {
            lines.add("&9Party Leader: &d" + Bukkit.getPlayer(party.getLeader()).getName());
            lines.add("&9Party Members: &d" + party.getMembers().size() + "&7/&d" + party.getLimit());
            lines.add(CC.scoreboardBar);
        }

        if (playerData.getPlayerState() == PlayerState.QUEUE) {
            QueueEntry queueEntry = this.plugin.getQueueManager().getQueueEntry(playerData.getUniqueId());

            if (queueEntry != null) {
                long queueTime = System.currentTimeMillis() - (this.plugin.getQueueManager().getPlayerQueueTime(playerData.getUniqueId()));

                String formattedQueueTime = TimeUtils.formatIntoMMSS(Math.round(queueTime / 1000L));

                lines.add("&e" + queueEntry.getGameType().getName() + " Queue");
                lines.add("&fTime: &d" + formattedQueueTime);
                lines.add(CC.scoreboardBar);
            }
        }

        lines.add("&fOnline: &d" + this.plugin.getServer().getOnlinePlayers().size());
        lines.add("&fQueueing: &d" + this.plugin.getQueueManager().getAllQueueSize());
        lines.add("&fPlaying: &d" + this.plugin.getGameManager().getPlaying());

        lines.add(" ");

        lines.add("&fLevel: &d" + playerData.getLevel());
        String finishedProgress = "";
        int notFinishedProgress = 10;
        for (int i = 0; i < playerData.getXp() * 100; i++) {
            if (i % 10 == 0) {
                finishedProgress += "⬛";

                notFinishedProgress--;
            }
        }

        String leftOverProgress = "";
        for (int i = 1; i <= notFinishedProgress; i++) {
            leftOverProgress += "⬛";
        }

        lines.add("&8" + finishedProgress + "&7" + leftOverProgress + " &7(" + ((int) (playerData.getXp() * 100)) + "%&7)");

        lines.add(" ");
        lines.add("&dtilly.rip");
        lines.add(CC.scoreboardBar);

        return CC.translate(lines);
    }

    private List<String> playingScoreboard(PlayerData playerData) {
        List<String> lines = new ArrayList<>();
        Game game = this.plugin.getGameManager().getGame(playerData);
        GameTeam yourTeam = game.getTeamByName(playerData.getPlayerTeam().getName());
        GameTeam opposingTeam = game.getTeams().get(playerData.getTeamId() == 1 ? 0 : 1);

        lines.add(CC.scoreboardBar);
        lines.add("&fDuration: &d" + game.getDuration());
        lines.add("");
        lines.add(game.getNextGeneratorTierString(game.getDurationTimer()));
        lines.add("");
        if (yourTeam.isHasBed()) {
            lines.add("&7[" + yourTeam.getPlayerTeam().getChatColor() + yourTeam.getPlayerTeam().getSmallName() + "&7] &a&l✓ &7(You)");
        } else if (yourTeam.getPlayingPlayers().size() > 0) {
            lines.add("&7[" + yourTeam.getPlayerTeam().getChatColor() + yourTeam.getPlayerTeam().getSmallName() + "&7] &f" + yourTeam.getPlayingPlayers().size() + " &7(You)");
        } else {
            lines.add("&7[" + yourTeam.getPlayerTeam().getChatColor() + yourTeam.getPlayerTeam().getSmallName() + "&7] &c&l✗ &7(You)");
        }
        if (opposingTeam.isHasBed()) {
            lines.add("&7[" + opposingTeam.getPlayerTeam().getChatColor() + opposingTeam.getPlayerTeam().getSmallName() + "&7] &a&l✓");
        } else if (opposingTeam.getPlayingPlayers().size() > 0) {
            lines.add("&7[" + opposingTeam.getPlayerTeam().getChatColor() + opposingTeam.getPlayerTeam().getSmallName() + "&7] &f" + yourTeam.getPlayingPlayers().size());
        } else {
            lines.add("&7[" + opposingTeam.getPlayerTeam().getChatColor() + opposingTeam.getPlayerTeam().getSmallName() + "&7] &c&l✗");
        }
        lines.add(" ");
        lines.add("&fKills: &d" + playerData.getCurrentGameData().getGameKills());
        lines.add("&fBeds Destroyed: &d" + playerData.getCurrentGameData().getGameBedsDestroyed());
        lines.add(" ");
        lines.add("&dtilly.rip");
        lines.add(CC.scoreboardBar);

        return CC.translate(lines);
    }

    private List<String> spectatingScoreboard(PlayerData playerData) {
        List<String> lines = new ArrayList<>();
        Game game = this.plugin.getGameManager().getSpectatingGame(playerData.getUniqueId());
        GameTeam yourTeam = game.getTeams().get(0);
        GameTeam opposingTeam = game.getTeams().get(1);

        lines.add(CC.scoreboardBar);
        lines.add("&fDuration: &d" + game.getDuration());
        lines.add(" ");
        if (yourTeam.isHasBed()) {
            lines.add("&7[" + yourTeam.getPlayerTeam().getChatColor() + yourTeam.getPlayerTeam().getSmallName() + "&7] &a&l✓");
        } else if (yourTeam.getPlayingPlayers().size() > 0) {
            lines.add("&7[" + yourTeam.getPlayerTeam().getChatColor() + yourTeam.getPlayerTeam().getSmallName() + "&7] &f" + yourTeam.getPlayingPlayers().size());
        } else {
            lines.add("&7[" + yourTeam.getPlayerTeam().getChatColor() + yourTeam.getPlayerTeam().getSmallName() + "&7] &c&l✗");
        }
        if (opposingTeam.isHasBed()) {
            lines.add("&7[" + opposingTeam.getPlayerTeam().getChatColor() + opposingTeam.getPlayerTeam().getSmallName() + "&7] &a&l✓");
        } else if (opposingTeam.getPlayingPlayers().size() > 0) {
            lines.add("&7[" + opposingTeam.getPlayerTeam().getChatColor() + opposingTeam.getPlayerTeam().getSmallName() + "&7] &f" + yourTeam.getPlayingPlayers().size());
        } else {
            lines.add("&7[" + opposingTeam.getPlayerTeam().getChatColor() + opposingTeam.getPlayerTeam().getSmallName() + "&7] &c&l✗");
        }
        lines.add(" ");

        List<Player> teamAplayers = yourTeam.playingPlayers().collect(Collectors.toList());
        List<Player> teamBplayers = opposingTeam.playingPlayers().collect(Collectors.toList());

        lines.add(yourTeam.getPlayerTeam().getChatColor() + (teamAplayers.size() > 0 ? teamAplayers.get(0).getName() : "None"));
        lines.add("&7VS");

        lines.add(opposingTeam.getPlayerTeam().getChatColor() + (teamBplayers.size() > 0 ? teamBplayers.get(0).getName() : "None"));
        lines.add(" ");
        lines.add("&dtilly.rip");
        lines.add(CC.scoreboardBar);

        return CC.translate(lines);
    }
}
