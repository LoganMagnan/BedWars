package rip.tilly.bedwars;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import rip.tilly.bedwars.customitems.bridgeegg.BridgeEggListener;
import rip.tilly.bedwars.game.Game;
import rip.tilly.bedwars.listeners.*;
import rip.tilly.bedwars.listeners.game.*;
import rip.tilly.bedwars.managers.CommandManager;
import rip.tilly.bedwars.managers.GameManager;
import rip.tilly.bedwars.managers.PlayerDataManager;
import rip.tilly.bedwars.managers.SpawnManager;
import rip.tilly.bedwars.managers.arena.ArenaManager;
import rip.tilly.bedwars.managers.arena.chunk.ChunkClearingManager;
import rip.tilly.bedwars.managers.arena.chunk.ChunkManager;
import rip.tilly.bedwars.managers.hotbar.HotbarManager;
import rip.tilly.bedwars.managers.mongo.MongoManager;
import rip.tilly.bedwars.managers.party.PartyManager;
import rip.tilly.bedwars.managers.queue.QueueManager;
import rip.tilly.bedwars.providers.placeholderapi.PlaceholderAPIProvider;
import rip.tilly.bedwars.providers.scoreboard.ScoreboardProvider;
import rip.tilly.bedwars.utils.CC;
import rip.tilly.bedwars.utils.aether.Aether;
import rip.tilly.bedwars.utils.config.file.Config;
import rip.tilly.bedwars.utils.menusystem.PlayerMenuUtil;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Lucanius & Trixkz
 * Project: BedWars
 */
@Getter
public final class BedWars extends JavaPlugin {

    @Getter private static BedWars instance;

    private Config mainConfig, arenasConfig;

    private MongoManager mongoManager;
    private ChunkClearingManager chunkClearingManager;
    private PlayerDataManager playerDataManager;
    private ArenaManager arenaManager;
    private GameManager gameManager;
    private SpawnManager spawnManager;
    private ChunkManager chunkManager;
    private HotbarManager hotbarManager;
    private CommandManager commandManager;
    private PartyManager partyManager;
    private QueueManager queueManager;

    private final HashMap<Player, PlayerMenuUtil> playerMenuUtilMap = new HashMap<>();

    @Override
    public void onEnable() {
        instance = this;

        this.saveDefaultConfig();

        this.mainConfig = new Config("config", this);
        this.arenasConfig = new Config("arenas", this);

        Bukkit.getConsoleSender().sendMessage("------------------------------------------------");
        Bukkit.getConsoleSender().sendMessage(CC.translate("&dzBedWars &8- &av" + this.getDescription().getVersion()));
        Bukkit.getConsoleSender().sendMessage(CC.translate("&7Made by &eTrixkz"));
        Bukkit.getConsoleSender().sendMessage("------------------------------------------------");

        this.loadManagers();
        this.loadListeners();
        this.loadRunnables();

        if (this.getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new PlaceholderAPIProvider(this).register();
            Bukkit.getConsoleSender().sendMessage(CC.translate("&aPlaceholderAPI successfully registered!"));
        }

        for (World world : Bukkit.getWorlds()) {
            for (Entity entity : world.getEntities()) {
                if (entity.getType() != EntityType.PLAYER && entity.getType() != EntityType.ITEM_FRAME) {
                    entity.remove();
                }
            }

            world.setGameRuleValue("doDaylightCycle", "false");
            world.setTime(0L);
            world.setStorm(false);
        }
    }

    @Override
    public void onDisable() {
        instance = null;

        for (Map.Entry<UUID, Game> entry : this.gameManager.getGames().entrySet()) {
            Game game = entry.getValue();
            this.chunkClearingManager.resetArena(game.getCopiedArena());
        }

        for (World world : Bukkit.getWorlds()) {
            for (Entity entity : world.getEntities()) {
                if (entity.getType() == EntityType.DROPPED_ITEM) {
                    entity.remove();
                }
            }
            for (Chunk chunk : world.getLoadedChunks()) {
                chunk.unload(true);
            }
        }

        this.mongoManager.disconnect();
    }

    private void loadManagers() {
        this.mongoManager = new MongoManager();
        this.chunkClearingManager = new ChunkClearingManager();
        this.playerDataManager = new PlayerDataManager();
        this.arenaManager = new ArenaManager();
        this.gameManager = new GameManager();
        this.spawnManager = new SpawnManager();
        this.chunkManager = new ChunkManager();
        this.hotbarManager = new HotbarManager();
        this.commandManager = new CommandManager();
        this.partyManager = new PartyManager();
        this.queueManager = new QueueManager();
    }

    private void loadListeners() {
        Arrays.asList(
                new PlayerDataListener(), new RandomListeners(), new InteractListener(), new ButtonListener(),
                new MenuListener(), new GameStartListener(), new GameEndListener(), new WorldListener(),
                new MovementListener(), new PlayerKillListener(), new DamageListener(), new PartyChatListener(),
                new BridgeEggListener()
        ).forEach(listener -> this.getServer().getPluginManager().registerEvents(listener, this));
    }

    private void loadRunnables() {
        new Aether(this, new ScoreboardProvider());
    }

    public PlayerMenuUtil getPlayerMenuUtil(Player player) {
        PlayerMenuUtil playerMenuUtil;

        if (playerMenuUtilMap.containsKey(player)) {
            return playerMenuUtilMap.get(player);
        } else {
            playerMenuUtil = new PlayerMenuUtil(player);

            playerMenuUtilMap.put(player, playerMenuUtil);

            return playerMenuUtil;
        }
    }
}
