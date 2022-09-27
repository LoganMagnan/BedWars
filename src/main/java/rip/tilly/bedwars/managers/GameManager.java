package rip.tilly.bedwars.managers;

import lombok.Getter;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import rip.tilly.bedwars.BedWars;
import rip.tilly.bedwars.events.GameEndEvent;
import rip.tilly.bedwars.events.GameStartEvent;
import rip.tilly.bedwars.game.*;
import rip.tilly.bedwars.game.arena.Arena;
import rip.tilly.bedwars.managers.hotbar.impl.HotbarItem;
import rip.tilly.bedwars.playerdata.PlayerData;
import rip.tilly.bedwars.playerdata.PlayerState;
import rip.tilly.bedwars.playerdata.currentgame.PlayerCurrentGameData;
import rip.tilly.bedwars.playerdata.currentgame.TeamUpgrades;
import rip.tilly.bedwars.upgrades.Upgrade;
import rip.tilly.bedwars.utils.ItemBuilder;
import rip.tilly.bedwars.utils.PlayerUtil;
import rip.tilly.bedwars.utils.TtlHashMap;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class GameManager {

    private final BedWars plugin = BedWars.getInstance();

    private final Map<UUID, Set<GameRequest>> gameRequests = new TtlHashMap<>(TimeUnit.SECONDS, 30);
    private final Map<UUID, UUID> spectators = new ConcurrentHashMap<>();
    @Getter private final Map<UUID, Game> games = new ConcurrentHashMap<>();

    public int getPlaying() {
        int i = 0;
        for (Game game : this.games.values()) {
            for (GameTeam team : game.getTeams()) {
                i += team.getPlayingPlayers().size();
            }
        }
        return i;
    }

    public int getPlayingByType(GameType gameType) {
        int i = 0;
        for (Game game : this.games.values()) {
            if (game.getGameType() == gameType) {
                for (GameTeam teams : game.getTeams()) {
                    i += teams.getPlayingPlayers().size();
                }
            }
        }
        return i;
    }

    public Game getGame(PlayerData playerData) {
        return this.games.get(playerData.getCurrentGameId());
    }

    public Game getGame(UUID uuid) {
        return this.getGame(this.plugin.getPlayerDataManager().getPlayerData(uuid));
    }

    public Game getGameFromUUID(UUID uuid) {
        return this.games.get(uuid);
    }

    public void createGameRequest(Player requester, Player requested, Arena arena, boolean party) {
        GameRequest request = new GameRequest(requester.getUniqueId(), requested.getUniqueId(), arena, party);
        this.gameRequests.computeIfAbsent(requested.getUniqueId(), k -> new HashSet<>()).add(request);
    }

    public GameRequest getGameRequest(UUID requester, UUID requested) {
        Set<GameRequest> requests = this.gameRequests.get(requested);
        if (requests == null) {
            return null;
        }

        return requests.stream().filter(req -> req.getRequester().equals(requester)).findAny().orElse(null);
    }

    public void removeGameRequests(UUID uuid) {
        this.gameRequests.remove(uuid);
    }

    public void createGame(Game game) {
        this.games.put(game.getGameId(), game);
        this.plugin.getServer().getPluginManager().callEvent(new GameStartEvent(game));
    }

    public void removeGame(Game game) {
        this.games.remove(game.getGameId());
    }

    public void addDroppedItem(Game game, Item item) {
        game.addEntityToRemove(item);
        game.addRunnable(this.plugin.getServer().getScheduler().runTaskLater(this.plugin, () -> {
            game.removeEntityToRemove(item);
            item.remove();
        }, 100L).getTaskId());
    }

    public void addDroppedItems(Game game, Set<Item> items) {
        for (Item item : items) {
            game.addEntityToRemove(item);
        }
        game.addRunnable(this.plugin.getServer().getScheduler().runTaskLater(this.plugin, () -> {
            for (Item item : items) {
                game.removeEntityToRemove(item);
                item.remove();
            }
        }, 100L).getTaskId());
    }

    public Game getSpectatingGame(UUID uuid) {
        return this.games.get(this.spectators.get(uuid));
    }

    public void removePlayerFromGame(Player player, PlayerData playerData, boolean spectatorDeath) {
        Game game = this.games.get(playerData.getCurrentGameId());
        Player killer = playerData.getLastDamager();

        if (player.isOnline() && killer != null) {
            killer.hidePlayer(player);
        }

        GameTeam losingTeam = game.getTeams().get(playerData.getTeamId());
        GameTeam winningTeam = game.getTeams().get(losingTeam.getId() == 0 ? 1 : 0);

        if (killer != null) {
            game.broadcast(losingTeam.getPlayerTeam().getChatColor() + player.getName() + " &ehas been killed by " + winningTeam.getPlayerTeam().getChatColor() + killer.getName() + "&e! &b&lFINAL KILL!");
            PlayerData killerData = this.plugin.getPlayerDataManager().getPlayerData(killer.getUniqueId());
            killerData.setKills(killerData.getKills() + 1);
        } else {
            game.broadcast(losingTeam.getPlayerTeam().getChatColor() + player.getName() + " &ehas died! &b&lFINAL KILL!");
        }

        playerData.setDeaths(playerData.getDeaths() + 1);

        losingTeam.killPlayer(player.getUniqueId());

        if (spectatorDeath) {
            this.addSpectatorDeath(player, playerData, game);
        }

        int remaining = losingTeam.getPlayingPlayers().size();
        if (remaining == 0) {
            this.plugin.getServer().getPluginManager().callEvent(new GameEndEvent(game, winningTeam, losingTeam));
        }
    }

    private void addSpectatorDeath(Player player, PlayerData playerData, Game game) {
        this.spectators.put(player.getUniqueId(), game.getGameId());
        playerData.setPlayerState(PlayerState.SPECTATING);
        PlayerUtil.clearPlayer(player);
        game.addSpectator(player.getUniqueId());
        game.addRunnable(this.plugin.getServer().getScheduler().scheduleSyncDelayedTask(this.plugin, () -> {
            game.getTeams().forEach(team -> team.playingPlayers().forEach(member -> member.hidePlayer(player)));
            game.spectatorPlayers().forEach(spectator -> spectator.hidePlayer(player));
            player.getActivePotionEffects().stream().map(PotionEffect::getType).forEach(player::removePotionEffect);
            player.setWalkSpeed(0.2F);
            player.setFlySpeed(0.4F);
            player.setAllowFlight(true);
        }, 20L));

        player.setWalkSpeed(0.0F);
        player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 10000, -5));

        this.plugin.getHotbarManager().getSpectatorItems().stream().filter(HotbarItem::isEnabled).forEach(item -> player.getInventory().setItem(item.getSlot(), item.getItemStack()));
        player.updateInventory();
    }

    public void addSpectator(Player player, PlayerData playerData, Player target, Game targetGame) {
        this.spectators.put(player.getUniqueId(), targetGame.getGameId());

        if (targetGame.getGameState() != GameState.ENDING) {
            targetGame.broadcast("&d" + player.getName() + " &eis now spectating!");
        }

        targetGame.addSpectator(player.getUniqueId());
        playerData.setPlayerState(PlayerState.SPECTATING);

        player.teleport(target);
        player.setAllowFlight(true);
        player.setFlying(true);

        player.getInventory().clear();
        this.plugin.getHotbarManager().getSpectatorItems().stream().filter(HotbarItem::isEnabled).forEach(item -> player.getInventory().setItem(item.getSlot(), item.getItemStack()));
        player.updateInventory();

        this.plugin.getServer().getOnlinePlayers().forEach(online -> {
            online.hidePlayer(player);
            player.hidePlayer(online);
        });

        targetGame.getTeams().forEach(team -> team.playingPlayers().forEach(player::showPlayer));
    }

    public void removeSpectator(Player player) {
        Game game = this.games.get(this.spectators.get(player.getUniqueId()));
        game.removeSpectator(player.getUniqueId());

        if (game.getGameState() != GameState.ENDING) {
            game.broadcast("&d" + player.getName() + " &eis no longer spectating!");
        }

        this.spectators.remove(player.getUniqueId());
        this.plugin.getPlayerDataManager().resetPlayer(player, true);
    }

    public List<ItemStack> getGameItems(PlayerCurrentGameData currentGameData, TeamUpgrades teamUpgrades) {
        List<ItemStack> allItems = new ArrayList<>();

        ItemBuilder sword = new ItemBuilder(Material.WOOD_SWORD).addUnbreakable();
        if (teamUpgrades.getLevelForUpgrade(Upgrade.SHARPENED_SWORDS) == 1) {
            sword.enchantment(Enchantment.DAMAGE_ALL, 1);
        }
        allItems.add(sword.build());

        if (currentGameData.isShears()) {
            ItemStack shears = new ItemBuilder(Material.SHEARS).addUnbreakable().build();
            allItems.add(shears);
        }

        ItemStack axe = null;
        if (currentGameData.getAxeLevel() > 0) {
            switch (currentGameData.getAxeLevel()) {
                case 1:
                    axe = new ItemBuilder(Material.WOOD_AXE).enchantment(Enchantment.DIG_SPEED, 1).addUnbreakable().build();
                    break;
                case 2:
                    axe = new ItemBuilder(Material.STONE_AXE).enchantment(Enchantment.DIG_SPEED, 1).addUnbreakable().build();
                    break;
                case 3:
                    axe = new ItemBuilder(Material.IRON_AXE).enchantment(Enchantment.DIG_SPEED, 2).addUnbreakable().build();
                    break;
                case 4:
                    axe = new ItemBuilder(Material.DIAMOND_AXE).enchantment(Enchantment.DIG_SPEED, 3).addUnbreakable().build();
                    break;
                default:
                    break;
            }
        }
        if (axe != null) {
            allItems.add(axe);
        }

        ItemStack pickaxe = null;
        if (currentGameData.getPickaxeLevel() > 0) {
            switch (currentGameData.getPickaxeLevel()) {
                case 1:
                    pickaxe = new ItemBuilder(Material.WOOD_PICKAXE).enchantment(Enchantment.DIG_SPEED, 1).addUnbreakable().build();
                    break;
                case 2:
                    pickaxe = new ItemBuilder(Material.IRON_PICKAXE).enchantment(Enchantment.DIG_SPEED, 2).addUnbreakable().build();
                    break;
                case 3:
                    pickaxe = new ItemBuilder(Material.GOLD_PICKAXE).enchantment(Enchantment.DIG_SPEED, 3).enchantment(Enchantment.DAMAGE_ALL, 2).addUnbreakable().build();
                    break;
                case 4:
                    pickaxe = new ItemBuilder(Material.DIAMOND_PICKAXE).enchantment(Enchantment.DIG_SPEED, 3).addUnbreakable().build();
                    break;
                default:
                    break;
            }
        }
        if (pickaxe != null) {
            allItems.add(pickaxe);
        }

        return allItems;
    }

    public ItemStack[] getGameStartArmor(PlayerData playerData) {
        Color color = playerData.getPlayerTeam().getColor();

        ItemStack leatherBoots = new ItemBuilder(Material.LEATHER_BOOTS).color(color).addUnbreakable().build();
        ItemStack leatherLeggings = new ItemBuilder(Material.LEATHER_LEGGINGS).color(color).addUnbreakable().build();
        ItemStack leatherChestplate = new ItemBuilder(Material.LEATHER_CHESTPLATE).color(color).addUnbreakable().build();
        ItemStack leatherHelmet = new ItemBuilder(Material.LEATHER_HELMET).color(color).addUnbreakable().build();

        return new ItemStack[] {
                leatherBoots,
                leatherLeggings,
                leatherChestplate,
                leatherHelmet
        };
    }

    public void clearBlocks(Game game) {
        game.getPlacedBlocksLocations().forEach(location -> {
            location.getBlock().setType(Material.AIR);
            game.removePlacedBlock(location.getBlock());
        });
    }
}
