package rip.tilly.bedwars.managers.queue;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import rip.tilly.bedwars.BedWars;
import rip.tilly.bedwars.game.Game;
import rip.tilly.bedwars.game.GameTeam;
import rip.tilly.bedwars.game.GameType;
import rip.tilly.bedwars.game.arena.Arena;
import rip.tilly.bedwars.managers.hotbar.impl.HotbarItem;
import rip.tilly.bedwars.playerdata.PlayerData;
import rip.tilly.bedwars.playerdata.PlayerState;
import rip.tilly.bedwars.playerdata.PlayerTeam;
import rip.tilly.bedwars.utils.CC;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class QueueManager {

    private final BedWars plugin = BedWars.getInstance();
    private final Map<UUID, QueueEntry> queued = new ConcurrentHashMap<>();
    private final Map<UUID, Long> queueTime = new HashMap<>();

    public QueueManager() {
        this.plugin.getServer().getScheduler().runTaskTimer(this.plugin, () -> this.queued.forEach((key, value) -> {
            this.findMatch(this.plugin.getServer().getPlayer(key), value.getGameType());
        }), 20, 20);
    }

    public void addPlayerToQueue(Player player, PlayerData playerData, GameType gameType) {
        playerData.setPlayerState(PlayerState.QUEUE);

        QueueEntry queueEntry = new QueueEntry(gameType);
        this.queued.put(playerData.getUniqueId(), queueEntry);
        this.giveQueueItems(player);

        player.sendMessage(" ");
        player.sendMessage(CC.translate("&d&l" + gameType.getName() + " Queue"));
        player.sendMessage(CC.translate("&7&oSearching for a game..."));
        player.sendMessage(" ");

        this.queueTime.put(player.getUniqueId(), System.currentTimeMillis());
    }

    public void removePlayerFromQueue(Player player) {
        if (player == null) {
            return;
        }

        QueueEntry entry = this.queued.get(player.getUniqueId());
        if (entry == null) {
            return;
        }

        this.plugin.getPlayerDataManager().resetPlayer(player, false);
        this.queued.remove(player.getUniqueId());

        player.sendMessage(CC.translate("&cYou have left the queue!"));
    }

    private void findMatch(Player player, GameType gameType) {
        if (player == null) {
            return;
        }

        PlayerData playerData = this.plugin.getPlayerDataManager().getPlayerData(player.getUniqueId());
        if (playerData == null) {
            this.plugin.getLogger().warning(player.getName() + "'s player data is null");
            return;
        }

        for (UUID opponent : this.queued.keySet()) {
            if (opponent == player.getUniqueId()) {
                continue;
            }

            QueueEntry queueEntry = this.queued.get(opponent);
            if (queueEntry.getGameType() != gameType) {
                continue;
            }

            Player opponentPlayer = Bukkit.getPlayer(opponent);
            Arena arena = this.plugin.getArenaManager().getRandomArena();

            player.sendMessage(" ");
            player.sendMessage(CC.translate("&aGame found!"));
            player.sendMessage(CC.translate("&7⚫ &fOpponent: &d" + opponentPlayer.getName()));
            player.sendMessage(CC.translate("&7⚫ &fArena: &d" + arena.getName()));
            player.sendMessage(CC.translate("&7⚫ &fGame: &d" + gameType.getName()));
            player.sendMessage(" ");

            opponentPlayer.sendMessage(" ");
            opponentPlayer.sendMessage(CC.translate("&aGame found!"));
            opponentPlayer.sendMessage(CC.translate("&7⚫ &fOpponent: &d" + player.getName()));
            opponentPlayer.sendMessage(CC.translate("&7⚫ &fArena: &d" + arena.getName()));
            opponentPlayer.sendMessage(CC.translate("&7⚫ &fGame: &d" + gameType.getName()));
            opponentPlayer.sendMessage(" ");

            GameTeam teamA = new GameTeam(player.getUniqueId(), Collections.singletonList(player.getUniqueId()), 0, PlayerTeam.RED);
            GameTeam teamB = new GameTeam(player.getUniqueId(), Collections.singletonList(opponentPlayer.getUniqueId()), 1, PlayerTeam.LIME);

            Game game = new Game(arena, gameType, teamA, teamB);
            this.plugin.getGameManager().createGame(game);

            this.queued.remove(player.getUniqueId());
            this.queued.remove(opponentPlayer.getUniqueId());

            this.queueTime.remove(player.getUniqueId());

//            if (this.getQueueSizeByType(gameType) >= gameType.getQueueAmount()) {
//
//            }
        }
    }

    private void giveQueueItems(Player player) {
        player.closeInventory();
        player.getInventory().clear();

        this.plugin.getHotbarManager().getQueueItems().stream().filter(HotbarItem::isEnabled).forEach(item -> player.getInventory().setItem(item.getSlot(), item.getItemStack()));

        player.updateInventory();
    }

    public QueueEntry getQueueEntry(UUID uuid) {
        return this.queued.get(uuid);
    }

    public long getPlayerQueueTime(UUID uuid) {
        return this.queueTime.get(uuid);
    }

    public int getQueueSizeByType(GameType type) {
        return (int) this.queued.entrySet().stream().filter(entry -> entry.getValue().getGameType() == type).count();
    }

    public int getAllQueueSize() {
        return this.queued.entrySet().size();
    }
}
