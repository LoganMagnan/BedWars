package rip.tilly.bedwars.runnables;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import rip.tilly.bedwars.BedWars;
import rip.tilly.bedwars.game.arena.Arena;
import rip.tilly.bedwars.game.arena.CopiedArena;
import rip.tilly.bedwars.utils.CustomLocation;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
public class ArenaCommandRunnable implements Runnable {

    private final BedWars plugin;
    private final Arena copiedArena;

    private int times;

    @Override
    public void run() {
        this.duplicateArena(this.copiedArena, 10000, 10000);
    }

    private void duplicateArena(Arena arena, int offsetX, int offsetZ) {
        new DuplicateArenaRunnable(this.plugin, arena, offsetX, offsetZ, 500, 500) {
            @Override
            public void onComplete() {
                double minX = arena.getMin().getX() + this.getOffsetX();
                double minZ = arena.getMin().getZ() + this.getOffsetZ();
                double maxX = arena.getMax().getX() + this.getOffsetX();
                double maxZ = arena.getMax().getZ() + this.getOffsetZ();

                double aX = arena.getA().getX() + this.getOffsetX();
                double aZ = arena.getA().getZ() + this.getOffsetZ();
                double bX = arena.getB().getX() + this.getOffsetX();
                double bZ = arena.getB().getZ() + this.getOffsetZ();

                CustomLocation min = new CustomLocation(minX, arena.getMin().getY(), minZ, arena.getMin().getYaw(), arena.getMin().getPitch());
                CustomLocation max = new CustomLocation(maxX, arena.getMax().getY(), maxZ, arena.getMax().getYaw(), arena.getMax().getPitch());
                CustomLocation a = new CustomLocation(aX, arena.getA().getY(), aZ, arena.getA().getYaw(), arena.getA().getPitch());
                CustomLocation b = new CustomLocation(bX, arena.getB().getY(), bZ, arena.getB().getYaw(), arena.getB().getPitch());

                double aMinX = arena.getTeamAmin().getX() + this.getOffsetX();
                double aMinZ = arena.getTeamAmin().getZ() + this.getOffsetZ();
                double aMaxX = arena.getTeamAmax().getX() + this.getOffsetX();
                double aMaxZ = arena.getTeamAmax().getZ() + this.getOffsetZ();

                double bMinX = arena.getTeamBmin().getX() + this.getOffsetX();
                double bMinZ = arena.getTeamBmin().getZ() + this.getOffsetZ();
                double bMaxX = arena.getTeamBmax().getX() + this.getOffsetX();
                double bMaxZ = arena.getTeamBmax().getZ() + this.getOffsetZ();

                CustomLocation teamAmin = new CustomLocation(aMinX, arena.getTeamAmin().getY(), aMinZ, arena.getTeamAmin().getYaw(), arena.getTeamAmin().getPitch());
                CustomLocation teamAmax = new CustomLocation(aMaxX, arena.getTeamAmax().getY(), aMaxZ, arena.getTeamAmax().getYaw(), arena.getTeamAmax().getPitch());
                CustomLocation teamBmin = new CustomLocation(bMinX, arena.getTeamBmin().getY(), bMinZ, arena.getTeamBmin().getYaw(), arena.getTeamBmin().getPitch());
                CustomLocation teamBmax = new CustomLocation(bMaxX, arena.getTeamBmax().getY(), bMaxZ, arena.getTeamBmax().getYaw(), arena.getTeamBmax().getPitch());

                List<CustomLocation> teamGenerators = new ArrayList<>();
                for (CustomLocation location : arena.getTeamGenerators()) {
                    CustomLocation newTeamGenerator = new CustomLocation((location.getX() + this.getOffsetX()), location.getY(), (location.getZ() + this.getOffsetZ()), location.getYaw(), location.getPitch());
                    teamGenerators.add(newTeamGenerator);
                }

                List<CustomLocation> diamondGenerators = new ArrayList<>();
                for (CustomLocation location : arena.getDiamondGenerators()) {
                    CustomLocation newDiamondGenerator = new CustomLocation((location.getX() + this.getOffsetX()), location.getY(), (location.getZ() + this.getOffsetZ()), location.getYaw(), location.getPitch());
                    diamondGenerators.add(newDiamondGenerator);
                }

                List<CustomLocation> emeraldGenerators = new ArrayList<>();
                for (CustomLocation location : arena.getEmeraldGenerators()) {
                    CustomLocation newEmeraldGenerator = new CustomLocation((location.getX() + this.getOffsetX()), location.getY(), (location.getZ() + this.getOffsetZ()), location.getYaw(), location.getPitch());
                    emeraldGenerators.add(newEmeraldGenerator);
                }

                double teamAshopX = arena.getTeamAshop().getX() + this.getOffsetX();
                double teamAshopZ = arena.getTeamAshop().getZ() + this.getOffsetZ();
                double teamBshopX = arena.getTeamBshop().getX() + this.getOffsetX();
                double teamBshopZ = arena.getTeamBshop().getZ() + this.getOffsetZ();

                double teamAupgradesX = arena.getTeamAupgrades().getX() + this.getOffsetX();
                double teamAupgradesZ = arena.getTeamAupgrades().getZ() + this.getOffsetZ();
                double teamBupgradesX = arena.getTeamBupgrades().getX() + this.getOffsetX();
                double teamBupgradesZ = arena.getTeamBupgrades().getZ() + this.getOffsetZ();

                CustomLocation teamAshop = new CustomLocation(teamAshopX, arena.getTeamAshop().getY(), teamAshopZ, arena.getTeamAshop().getYaw(), arena.getTeamAshop().getPitch());
                CustomLocation teamBshop = new CustomLocation(teamBshopX, arena.getTeamBshop().getY(), teamBshopZ, arena.getTeamBshop().getYaw(), arena.getTeamBshop().getPitch());
                CustomLocation teamAupgrades = new CustomLocation(teamAupgradesX, arena.getTeamAupgrades().getY(), teamAupgradesZ, arena.getTeamAupgrades().getYaw(), arena.getTeamAupgrades().getPitch());
                CustomLocation teamBupgrades = new CustomLocation(teamBupgradesX, arena.getTeamBupgrades().getY(), teamBupgradesZ, arena.getTeamBupgrades().getYaw(), arena.getTeamBupgrades().getPitch());

                CopiedArena copiedArena = new CopiedArena(
                        a,
                        b,
                        min,
                        max,
                        teamAmin,
                        teamAmax,
                        teamBmin,
                        teamBmax,
                        teamAshop,
                        teamBshop,
                        teamAupgrades,
                        teamBupgrades,
                        teamGenerators,
                        diamondGenerators,
                        emeraldGenerators
                );

                arena.addCopiedArena(copiedArena);
                arena.addAvailableArena(copiedArena);

                String arenaPasteMessage = "[Copied Arena] - " + arena.getName() + " placed at " + (int) minX + ", " + (int) minZ + ". " + ArenaCommandRunnable.this.times + " copies remaining.";

                if (--ArenaCommandRunnable.this.times > 0) {
                    ArenaCommandRunnable.this.plugin.getServer().getLogger().info(arenaPasteMessage);
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        if (player.isOp()) {
                            player.sendMessage(ChatColor.GREEN + arenaPasteMessage);
                        }
                    }
                    ArenaCommandRunnable.this.duplicateArena(arena, (int) Math.round(maxX), (int) Math.round(maxZ));
                } else {
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        if (player.isOp()) {
                            player.sendMessage(ChatColor.GREEN + "All the copies for " + ArenaCommandRunnable.this.copiedArena.getName() + " have been pasted successfully!");
                        }
                    }
                    ArenaCommandRunnable.this.plugin.getServer().getLogger().info("All the copies for " + ArenaCommandRunnable.this.copiedArena.getName() + " have been pasted successfully!");
                    ArenaCommandRunnable.this.plugin.getArenaManager().setGeneratingArenaRunnable(ArenaCommandRunnable.this.plugin.getArenaManager().getGeneratingArenaRunnable() - 1);
                    this.getPlugin().getArenaManager().reloadArenas();
                }
            }
        }.run();
    }
}