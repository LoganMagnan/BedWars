package rip.tilly.bedwars.managers.arena;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import rip.tilly.bedwars.BedWars;
import rip.tilly.bedwars.game.arena.Arena;
import rip.tilly.bedwars.game.arena.CopiedArena;
import rip.tilly.bedwars.utils.CustomLocation;
import rip.tilly.bedwars.utils.config.file.Config;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class ArenaManager {

    private final BedWars plugin = BedWars.getInstance();
    private final Config config = this.plugin.getArenasConfig();

    @Getter private final Map<String, Arena> arenas = new HashMap<>();
    @Getter private final Map<CopiedArena, UUID> arenaGameUUIDs = new HashMap<>();

    @Getter @Setter private int generatingArenaRunnable;

    public ArenaManager() {
        this.loadArenas();
    }

    private void loadArenas() {
        FileConfiguration fileConfig = this.config.getConfig();
        ConfigurationSection section = fileConfig.getConfigurationSection("arenas");
        if (section == null) {
            return;
        }

        section.getKeys(false).forEach(name -> {
            String icon = section.getString(name + ".icon") == null ? Material.PAPER.name() : section.getString(name + ".icon");
            int iconData = section.getInt(name + ".icon-data");

            String a = section.getString(name + ".a");
            String b = section.getString(name + ".b");
            String min = section.getString(name + ".min");
            String max = section.getString(name + ".max");
            String teamAmin = section.getString(name + ".teamAmin");
            String teamAmax = section.getString(name + ".teamAmax");
            String teamBmin = section.getString(name + ".teamBmin");
            String teamBmax = section.getString(name + ".teamBmax");

            int deadZone = section.getInt(name + ".deadZone");
            int buildMax = section.getInt(name + ".buildMax");

            String teamAshop = section.getString(name + ".teamAshop");
            String teamBshop = section.getString(name + ".teamBshop");
            String teamAupgrades = section.getString(name + ".teamAupgrades");
            String teamBupgrades = section.getString(name + ".teamBupgrades");

            List<String> teamGenerators = section.getStringList(name + ".team-generators");
            List<String> diamondGenerators = section.getStringList(name + ".diamond-generators");
            List<String> emeraldGenerators = section.getStringList(name + ".emerald-generators");

            CustomLocation spawnA = CustomLocation.stringToLocation(a);
            CustomLocation spawnB = CustomLocation.stringToLocation(b);
            CustomLocation locMin = CustomLocation.stringToLocation(min);
            CustomLocation locMax = CustomLocation.stringToLocation(max);
            CustomLocation locTeamAmin = CustomLocation.stringToLocation(teamAmin);
            CustomLocation locTeamAmax = CustomLocation.stringToLocation(teamAmax);
            CustomLocation locTeamBmin = CustomLocation.stringToLocation(teamBmin);
            CustomLocation locTeamBmax = CustomLocation.stringToLocation(teamBmax);

            CustomLocation locTeamAshop = CustomLocation.stringToLocation(teamAshop);
            CustomLocation locTeamBshop = CustomLocation.stringToLocation(teamBshop);
            CustomLocation locTeamAupgrades = CustomLocation.stringToLocation(teamAupgrades);
            CustomLocation locTeamBupgrades = CustomLocation.stringToLocation(teamBupgrades);

            List<CustomLocation> teamGeneratorLocations = new ArrayList<>();

            for (String location : teamGenerators) {
                teamGeneratorLocations.add(CustomLocation.stringToLocation(location));
            }

            List<CustomLocation> diamondGeneratorLocations = new ArrayList<>();

            for (String location : diamondGenerators) {
                diamondGeneratorLocations.add(CustomLocation.stringToLocation(location));
            }

            List<CustomLocation> emeraldGeneratorLocations = new ArrayList<>();

            for (String location : emeraldGenerators) {
                emeraldGeneratorLocations.add(CustomLocation.stringToLocation(location));
            }

            List<CopiedArena> copiedArenas = new ArrayList<>();
            ConfigurationSection copiedSection = section.getConfigurationSection(name + ".copiedArenas");
            if (copiedSection != null) {
                copiedSection.getKeys(false).forEach(copy -> {
                    String copyA = copiedSection.getString(copy + ".a");
                    String copyB = copiedSection.getString(copy + ".b");
                    String copyMin = copiedSection.getString(copy + ".min");
                    String copyMax = copiedSection.getString(copy + ".max");
                    String copyTeamAmin = copiedSection.getString(copy + ".teamAmin");
                    String copyTeamAmax = copiedSection.getString(copy + ".teamAmax");
                    String copyTeamBmin = copiedSection.getString(copy + ".teamBmin");
                    String copyTeamBmax = copiedSection.getString(copy + ".teamBmax");

                    String copyTeamAshop = copiedSection.getString(copy + ".teamAshop");
                    String copyTeamBshop = copiedSection.getString(copy + ".teamBshop");
                    String copyTeamAupgrades = copiedSection.getString(copy + ".teamAupgrades");
                    String copyTeamBupgrades = copiedSection.getString(copy + ".teamBupgrades");

                    List<String> copyTeamGenerators = copiedSection.getStringList(copy + ".team-generators");
                    List<String> copyDiamondGenerators = copiedSection.getStringList(copy + ".diamond-generators");
                    List<String> copyEmeraldGenerators = copiedSection.getStringList(copy + ".emerald-generators");

                    CustomLocation copySpawnA = CustomLocation.stringToLocation(copyA);
                    CustomLocation copySpawnB = CustomLocation.stringToLocation(copyB);
                    CustomLocation copyLocMin = CustomLocation.stringToLocation(copyMin);
                    CustomLocation copyLocMax = CustomLocation.stringToLocation(copyMax);
                    CustomLocation copyLocTeamAmin = CustomLocation.stringToLocation(copyTeamAmin);
                    CustomLocation copyLocTeamAmax = CustomLocation.stringToLocation(copyTeamAmax);
                    CustomLocation copyLocTeamBmin = CustomLocation.stringToLocation(copyTeamBmin);
                    CustomLocation copyLocTeamBmax = CustomLocation.stringToLocation(copyTeamBmax);

                    CustomLocation copyLocTeamAshop = CustomLocation.stringToLocation(copyTeamAshop);
                    CustomLocation copyLocTeamBshop = CustomLocation.stringToLocation(copyTeamBshop);
                    CustomLocation copyLocTeamAupgrades = CustomLocation.stringToLocation(copyTeamAupgrades);
                    CustomLocation copyLocTeamBupgrades = CustomLocation.stringToLocation(copyTeamBupgrades);

                    List<CustomLocation> copyTeamGeneratorLocations = new ArrayList<>();

                    for (String location : copyTeamGenerators) {
                        copyTeamGeneratorLocations.add(CustomLocation.stringToLocation(location));
                    }

                    List<CustomLocation> copyDiamondGeneratorLocations = new ArrayList<>();

                    for (String location : copyDiamondGenerators) {
                        copyDiamondGeneratorLocations.add(CustomLocation.stringToLocation(location));
                    }

                    List<CustomLocation> copyEmeraldGeneratorLocations = new ArrayList<>();

                    for (String location : copyEmeraldGenerators) {
                        copyEmeraldGeneratorLocations.add(CustomLocation.stringToLocation(location));
                    }

                    CopiedArena copiedArena = new CopiedArena(
                            copySpawnA,
                            copySpawnB,
                            copyLocMin,
                            copyLocMax,
                            copyLocTeamAmin,
                            copyLocTeamAmax,
                            copyLocTeamBmin,
                            copyLocTeamBmax,

                            copyLocTeamAshop,
                            copyLocTeamBshop,
                            copyLocTeamAupgrades,
                            copyLocTeamBupgrades,

                            copyTeamGeneratorLocations,
                            copyDiamondGeneratorLocations,
                            copyEmeraldGeneratorLocations
                    );

                    this.plugin.getChunkClearingManager().copyArena(copiedArena);

                    copiedArenas.add(copiedArena);
                });
            }

            boolean enabled = section.getBoolean(name + ".enabled", false);

            Arena arena = new Arena(
                    name,
                    copiedArenas,
                    new ArrayList<>(copiedArenas),
                    icon,
                    iconData,
                    spawnA,
                    spawnB,
                    locMin,
                    locMax,
                    locTeamAmin,
                    locTeamAmax,
                    locTeamBmin,
                    locTeamBmax,
                    deadZone,
                    buildMax,

                    locTeamAshop,
                    locTeamBshop,
                    locTeamAupgrades,
                    locTeamBupgrades,

                    teamGeneratorLocations,
                    diamondGeneratorLocations,
                    emeraldGeneratorLocations,

                    enabled
            );

            this.arenas.put(name, arena);
        });
    }

    public void saveArenas() {
        FileConfiguration fileConfig = this.config.getConfig();
        fileConfig.set("arenas", null);
        arenas.forEach((name, arena) -> {
            String icon = arena.getIcon();
            int iconData = arena.getIconData();

            String a = CustomLocation.locationToString(arena.getA());
            String b = CustomLocation.locationToString(arena.getB());
            String min = CustomLocation.locationToString(arena.getMin());
            String max = CustomLocation.locationToString(arena.getMax());
            String teamAmin = CustomLocation.locationToString(arena.getTeamAmin());
            String teamAmax = CustomLocation.locationToString(arena.getTeamAmax());
            String teamBmin = CustomLocation.locationToString(arena.getTeamBmin());
            String teamBmax = CustomLocation.locationToString(arena.getTeamBmax());

            int deadZone = arena.getDeadZone();
            int buildMax = arena.getBuildMax();

            String teamAshop = CustomLocation.locationToString(arena.getTeamAshop());
            String teamBshop = CustomLocation.locationToString(arena.getTeamBshop());
            String teamAupgrades = CustomLocation.locationToString(arena.getTeamAupgrades());
            String teamBupgrades = CustomLocation.locationToString(arena.getTeamBupgrades());

            String root = "arenas." + name;

            fileConfig.set(root + ".icon", icon);
            fileConfig.set(root + ".icon-data", iconData);

            fileConfig.set(root + ".a", a);
            fileConfig.set(root + ".b", b);
            fileConfig.set(root + ".min", min);
            fileConfig.set(root + ".max", max);
            fileConfig.set(root + ".teamAmin", teamAmin);
            fileConfig.set(root + ".teamAmax", teamAmax);
            fileConfig.set(root + ".teamBmin", teamBmin);
            fileConfig.set(root + ".teamBmax", teamBmax);

            fileConfig.set(root + ".deadZone", deadZone);
            fileConfig.set(root + ".buildMax", buildMax);

            fileConfig.set(root + ".teamAshop", teamAshop);
            fileConfig.set(root + ".teamBshop", teamBshop);
            fileConfig.set(root + ".teamAupgrades", teamAupgrades);
            fileConfig.set(root + ".teamBupgrades", teamBupgrades);

            fileConfig.set(root + ".team-generators", this.fromLocations(arena.getTeamGenerators()));
            fileConfig.set(root + ".diamond-generators", this.fromLocations(arena.getDiamondGenerators()));
            fileConfig.set(root + ".emerald-generators", this.fromLocations(arena.getEmeraldGenerators()));

            fileConfig.set(root + ".enabled", arena.isEnabled());
            fileConfig.set(root + ".copiedArenas", null);

            int i = 0;
            if (arena.getCopiedArenas() != null) {
                for (CopiedArena copiedArena : arena.getCopiedArenas()) {
                    String copyA = CustomLocation.locationToString(copiedArena.getA());
                    String copyB = CustomLocation.locationToString(copiedArena.getB());
                    String copyMin = CustomLocation.locationToString(copiedArena.getMin());
                    String copyMax = CustomLocation.locationToString(copiedArena.getMax());
                    String copyTeamAmin = CustomLocation.locationToString(copiedArena.getTeamAmin());
                    String copyTeamAmax = CustomLocation.locationToString(copiedArena.getTeamAmax());
                    String copyTeamBmin = CustomLocation.locationToString(copiedArena.getTeamBmin());
                    String copyTeamBmax = CustomLocation.locationToString(copiedArena.getTeamBmax());

                    String copyTeamAshop = CustomLocation.locationToString(copiedArena.getTeamAshop());
                    String copyTeamBshop = CustomLocation.locationToString(copiedArena.getTeamBshop());
                    String copyTeamAupgrades = CustomLocation.locationToString(copiedArena.getTeamAupgrades());
                    String copyTeamBupgrades = CustomLocation.locationToString(copiedArena.getTeamBupgrades());

                    String copyRoot = root + ".copiedArenas." + i;

                    fileConfig.set(copyRoot + ".a", copyA);
                    fileConfig.set(copyRoot + ".b", copyB);
                    fileConfig.set(copyRoot + ".min", copyMin);
                    fileConfig.set(copyRoot + ".max", copyMax);
                    fileConfig.set(copyRoot + ".teamAmin", copyTeamAmin);
                    fileConfig.set(copyRoot + ".teamAmax", copyTeamAmax);
                    fileConfig.set(copyRoot + ".teamBmin", copyTeamBmin);
                    fileConfig.set(copyRoot + ".teamBmax", copyTeamBmax);

                    fileConfig.set(copyRoot + ".teamAshop", copyTeamAshop);
                    fileConfig.set(copyRoot + ".teamBshop", copyTeamBshop);
                    fileConfig.set(copyRoot + ".teamAupgrades", copyTeamAupgrades);
                    fileConfig.set(copyRoot + ".teamBupgrades", copyTeamBupgrades);

                    fileConfig.set(copyRoot + ".team-generators", this.fromLocations(copiedArena.getTeamGenerators()));
                    fileConfig.set(copyRoot + ".diamond-generators", this.fromLocations(copiedArena.getDiamondGenerators()));
                    fileConfig.set(copyRoot + ".emerald-generators", this.fromLocations(copiedArena.getEmeraldGenerators()));

                    i++;
                }
            }
        });

        this.config.save();
    }

    public void reloadArenas() {
        this.saveArenas();
        this.arenas.clear();
        this.loadArenas();
    }

    public void createArena(String name) {
        this.arenas.put(name, new Arena(name));
    }

    public void deleteArena(String name) {
        this.arenas.remove(name);
    }

    public Arena getArena(String name) {
        return this.arenas.get(name);
    }

    public Arena getRandomArena() {
        List<Arena> enabledArenas = new ArrayList<>();

        for (Arena arena : this.arenas.values()) {
            if (!arena.isEnabled()) {
                continue;
            }
            enabledArenas.add(arena);
        }

        if (enabledArenas.size() == 0) {
            return null;
        }

        return enabledArenas.get(ThreadLocalRandom.current().nextInt(enabledArenas.size()));
    }

    public void removeArenaGameUUID(CopiedArena copiedArena) {
        this.arenaGameUUIDs.remove(copiedArena);
    }

    public UUID getArenaGameUUID(CopiedArena copiedArena) {
        return this.arenaGameUUIDs.get(copiedArena);
    }

    public void setArenaGameUUIDs(CopiedArena copiedArena, UUID uuid) {
        this.arenaGameUUIDs.put(copiedArena, uuid);
    }

    public List<String> fromLocations(List<CustomLocation> locations) {
        List<String> toReturn = new ArrayList<>();
        for (CustomLocation location : locations) {
            toReturn.add(CustomLocation.locationToString(location));
        }

        return toReturn;
    }
}
