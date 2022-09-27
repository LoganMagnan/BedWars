package rip.tilly.bedwars.game;

import io.netty.util.internal.ConcurrentSet;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.ItemStack;
import org.github.paperspigot.Title;
import rip.tilly.bedwars.BedWars;
import rip.tilly.bedwars.game.arena.Arena;
import rip.tilly.bedwars.game.arena.CopiedArena;
import rip.tilly.bedwars.generators.Generator;
import rip.tilly.bedwars.generators.GeneratorTier;
import rip.tilly.bedwars.generators.GeneratorType;
import rip.tilly.bedwars.utils.CC;
import rip.tilly.bedwars.utils.CustomLocation;
import rip.tilly.bedwars.utils.TimeUtils;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
@Setter
public class Game {

    private final BedWars plugin = BedWars.getInstance();

    private final Set<Entity> entitiesToRemove = new ConcurrentSet<>();
    private final Set<Location> placedBlocksLocations = new ConcurrentSet<>();
    private final Set<UUID> spectators = new ConcurrentSet<>();
    private final Set<Integer> runnables = new HashSet<>();
    private final Set<ItemStack> droppedItems = new ConcurrentSet<>();

    private final Arena arena;
    private final GameType gameType;
    private final List<GameTeam> teams;

    private final UUID gameId = UUID.randomUUID();

    private CopiedArena copiedArena;
    private GameState gameState = GameState.STARTING;
    private int countdown = 6;
    private int durationTimer;
    private int winningTeamId;

    private List<Generator> activatedGenerators = new ArrayList<>();

    private GeneratorTier diamondGeneratorTier = GeneratorTier.ONE;
    private GeneratorTier emeraldGeneratorTier = GeneratorTier.ONE;

    public Game(Arena arena, GameType gameType, GameTeam... teams) {
        this.arena = arena;
        this.gameType = gameType;
        this.teams = Arrays.asList(teams);
    }

    public GameTeam getTeamByName(String name) {
        return this.teams.stream().filter(team -> team.getPlayerTeam().getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    public String getDuration() {
        return TimeUtils.formatIntoMMSS(durationTimer);
    }

    public void addEntityToRemove(Entity entity) {
        this.entitiesToRemove.add(entity);
    }

    public void removeEntityToRemove(Entity entity) {
        this.entitiesToRemove.remove(entity);
    }

    public void clearEntitiesToRemove() {
        this.entitiesToRemove.clear();
    }

    public void broadcastTitle(String message, String subMessage) {
        Title title = new Title(CC.translate(message), CC.translate(subMessage), 5, 20, 5);
        this.teams.forEach(team -> team.playingPlayers().forEach(player -> player.sendTitle(title)));
    }

    public void broadcastTitleToOneTeam(String message, String subMessage, GameTeam gameTeam) {
        Title title = new Title(CC.translate(message), CC.translate(subMessage), 5, 20, 5);

        this.teams.stream().filter(team -> team == gameTeam).forEach(team -> team.playingPlayers().forEach(player -> player.sendTitle(title)));
    }

    public void broadcast(String message) {
        this.teams.forEach(team -> team.playingPlayers().forEach(player -> player.sendMessage(CC.translate(message))));
    }

    public void broadcastSound(Sound sound) {
        this.teams.forEach(team -> team.playingPlayers().forEach(player -> player.playSound(player.getLocation(), sound, 10, 1)));
    }

    public void broadcastWithSound(String message, Sound sound) {
        this.teams.forEach(team -> team.playingPlayers().forEach(player -> {
            player.sendMessage(CC.translate(message));
            player.playSound(player.getLocation(), sound, 10, 1);
        }));
    }

    public void broadcastToOneTeam(String message, GameTeam gameTeam) {
        this.teams.stream().filter(team -> team == gameTeam).forEach(team -> team.playingPlayers().forEach(player -> player.sendMessage(CC.translate(message))));
    }

    public int decrementCountdown() {
        return --this.countdown;
    }

    public void incrementDuration() {
        ++this.durationTimer;
    }

    public void addSpectator(UUID uuid) {
        this.spectators.add(uuid);
    }

    public void removeSpectator(UUID uuid) {
        this.spectators.remove(uuid);
    }

    public Stream<Player> spectatorPlayers() {
        return this.spectators.stream().map(this.plugin.getServer()::getPlayer).filter(Objects::nonNull);
    }

    public void addRunnable(int id) {
        this.runnables.add(id);
    }

    public void addPlacedBlock(Block block) {
        this.placedBlocksLocations.add(block.getLocation());
    }

    public void removePlacedBlock(Block block) {
        this.placedBlocksLocations.remove(block.getLocation());
    }

    public boolean isPlaceable(Location location, Game game) {
        double minX = game.getCopiedArena().getMin().getX();
        double minZ = game.getCopiedArena().getMin().getZ();
        double maxX = game.getCopiedArena().getMax().getX();
        double maxZ = game.getCopiedArena().getMax().getZ();

        if (minX > maxX) {
            double lastMinX = minX;
            minX = maxX;
            maxX = lastMinX;
        }

        if (minZ > maxZ) {
            double lastMinZ = minZ;
            minZ = maxZ;
            maxZ = lastMinZ;
        }

        if (location.getX() >= minX && location.getX() <= maxX && location.getZ() >= minZ && location.getZ() <= maxZ) {
            return true;
        }

        return false;
    }

    public boolean isInside(Location location, Game game) {
        double aMinX = game.getCopiedArena().getTeamAmin().getX();
        double aMinZ = game.getCopiedArena().getTeamAmin().getZ();
        double aMaxX = game.getCopiedArena().getTeamAmax().getX();
        double aMaxZ = game.getCopiedArena().getTeamAmax().getZ();

        if (aMinX > aMaxX) {
            double lastMinX = aMinX;
            aMinX = aMaxX;
            aMaxX = lastMinX;
        }

        if (aMinZ > aMaxZ) {
            double lastMinZ = aMinZ;
            aMinZ = aMaxZ;
            aMaxZ = lastMinZ;
        }

        if (location.getX() >= aMinX && location.getX() <= aMaxX && location.getZ() >= aMinZ && location.getZ() <= aMaxZ) {
            return true;
        }

        double bMinX = game.getCopiedArena().getTeamBmin().getX();
        double bMinZ = game.getCopiedArena().getTeamBmin().getZ();
        double bMaxX = game.getCopiedArena().getTeamBmax().getX();
        double bMaxZ = game.getCopiedArena().getTeamBmax().getZ();

        if (bMinX > bMaxX) {
            double lastMinX = bMinX;
            bMinX = bMaxX;
            bMaxX = lastMinX;
        }

        if (bMinZ > bMaxZ) {
            double lastMinZ = bMinZ;
            bMinZ = bMaxZ;
            bMaxZ = lastMinZ;
        }

        if (location.getX() >= bMinX && location.getX() <= bMaxX && location.getZ() >= bMinZ && location.getZ() <= bMaxZ) {
            return true;
        }

        return false;
    }

    public boolean isBreakable(Block block) {
        if (placedBlocksLocations.contains(block.getLocation())) {
            return true;
        }

        Material material = block.getType();
        switch (material) {
            case BED:
            case BED_BLOCK:
                return true;
        }

        return false;
    }

    public void tick(int amount, Game game) {
        if (this.secondsToMinutes(amount) == 5D) {
            game.broadcast("&bDiamond &egenerators have been upgraded to &cTier II");

            this.diamondGeneratorTier = GeneratorTier.TWO;
        }

        if (this.secondsToMinutes(amount) == 8D) {
            game.broadcast("&aEmerald &egenerators have been upgraded to &cTier II");

            this.emeraldGeneratorTier = GeneratorTier.TWO;
        }

        if (this.secondsToMinutes(amount) == 10D) {
            game.broadcast("&bDiamond &egenerators have been upgraded to &cTier III");

            this.diamondGeneratorTier = GeneratorTier.THREE;
        }

        if (this.secondsToMinutes(amount) == 12D) {
            game.broadcast("&aEmerald &egenerators have been upgraded to &cTier III");

            this.emeraldGeneratorTier = GeneratorTier.THREE;
        }

        if (this.secondsToMinutes(amount) == 15D) {
            game.broadcast("&bDiamond &egenerators have been upgraded to &cTier IV");

            this.diamondGeneratorTier = GeneratorTier.FOUR;
        }

        for (CustomLocation customLocation : this.arena.getTeamGenerators()) {
            Generator ironGenerator = new Generator(customLocation.toBukkitLocation(), GeneratorType.IRON, true, this);
            ironGenerator.spawn();

            Generator goldGenerator = new Generator(customLocation.toBukkitLocation(), GeneratorType.GOLD, true, this);
            goldGenerator.spawn();

            Generator emeraldGenerator = new Generator(customLocation.toBukkitLocation(), GeneratorType.EMERALD, true, this);
            emeraldGenerator.spawn();
        }

        for (Generator generator : this.getActivatedGenerators()) {
            generator.setActivated(true);

            if (generator.getGeneratorType() == GeneratorType.DIAMOND) {
                generator.setGeneratorTier(this.diamondGeneratorTier);
            }

            if (generator.getGeneratorType() == GeneratorType.EMERALD) {
                generator.setGeneratorTier(this.emeraldGeneratorTier);
            }

            generator.spawn();
        }
    }

    public String getNextGeneratorTierString(int amount) {
        Map<Double, String> toCalculate = new HashMap<>();
        toCalculate.put(5D, "&bDiamond &cII");
        toCalculate.put(8D, "&aEmerald &cII");
        toCalculate.put(10D, "&bDiamond &cIII");
        toCalculate.put(12D, "&aEmerald &cIII");
        toCalculate.put(15D, "&bDiamond &cIV");

        for (Map.Entry<Double, String> entry : toCalculate.entrySet().stream().sorted(Comparator.comparingInt(key -> (key.getKey()).intValue())).collect(Collectors.toList())) {
            if (secondsToMinutes(amount) < entry.getKey()) {
                int difference = (entry.getKey()).intValue() * 60 - amount;
                int minutesLeft = difference % 3600 / 60;
                int secondsLeft = difference % 60;

                return entry.getValue() + ": &d" + String.format("%02d:%02d", minutesLeft, secondsLeft);
            }
        }

        return "&cNone";
    }

    public double secondsToMinutes(int seconds) {
        return seconds / 60D;
    }

    public void spawnVillagers() {
//        CustomVillager villager = new CustomVillager(this.copiedArena.getA().toBukkitWorld());
//        villager.register();
//        villager.spawn(this.copiedArena.getTeamAshop().toBukkitLocation(), "&aItem Shop");
//        this.addEntityToRemove(villager.getBukkitEntity());

        Villager teamAShopVillager = this.copiedArena.getTeamAshop().toBukkitLocation().getWorld().spawn(this.copiedArena.getTeamAshop().toBukkitLocation(), Villager.class);
        teamAShopVillager.setCustomName(CC.translate("&aItem Shop"));
        teamAShopVillager.setCustomNameVisible(true);

        Villager teamBShopVillager = this.copiedArena.getTeamBshop().toBukkitLocation().getWorld().spawn(this.copiedArena.getTeamBshop().toBukkitLocation(), Villager.class);
        teamBShopVillager.setCustomName(CC.translate("&aItem Shop"));
        teamBShopVillager.setCustomNameVisible(true);

        Villager teamAUpgradesVillager = this.copiedArena.getTeamAupgrades().toBukkitLocation().getWorld().spawn(this.copiedArena.getTeamAupgrades().toBukkitLocation(), Villager.class);
        teamAUpgradesVillager.setCustomName(CC.translate("&aUpgrades Shop"));
        teamAUpgradesVillager.setCustomNameVisible(true);

        Villager teamBUpgradesVillager = this.copiedArena.getTeamBupgrades().toBukkitLocation().getWorld().spawn(this.copiedArena.getTeamBupgrades().toBukkitLocation(), Villager.class);
        teamBUpgradesVillager.setCustomName(CC.translate("&aUpgrades Shop"));
        teamBUpgradesVillager.setCustomNameVisible(true);

        this.addEntityToRemove(teamAShopVillager);
        this.addEntityToRemove(teamBShopVillager);
        this.addEntityToRemove(teamAUpgradesVillager);
        this.addEntityToRemove(teamBUpgradesVillager);
    }
}
