package rip.tilly.bedwars.managers.arena.chunk;

import org.bukkit.Chunk;
import org.bukkit.scheduler.BukkitRunnable;
import rip.tilly.bedwars.BedWars;
import rip.tilly.bedwars.game.arena.Arena;
import rip.tilly.bedwars.game.arena.CopiedArena;
import rip.tilly.bedwars.utils.CustomLocation;

public class ChunkManager {

    private final BedWars plugin = BedWars.getInstance();

    public ChunkManager() {
        new BukkitRunnable() {
            @Override
            public void run() {
                loadChunks();
            }
        }.runTaskLater(this.plugin, 1);
    }

    private void loadChunks() {
        CustomLocation spawnMin = this.plugin.getSpawnManager().getSpawnMin();
        CustomLocation spawnMax = this.plugin.getSpawnManager().getSpawnMax();
        if (spawnMin != null && spawnMax != null) {
            int spawnMinX = spawnMin.toBukkitLocation().getBlockX() >> 4;
            int spawnMinZ = spawnMin.toBukkitLocation().getBlockZ() >> 4;
            int spawnMaxX = spawnMax.toBukkitLocation().getBlockX() >> 4;
            int spawnMaxZ = spawnMax.toBukkitLocation().getBlockZ() >> 4;

            if (spawnMinX > spawnMaxX) {
                int lastSpawnMinX = spawnMinX;
                spawnMinX = spawnMaxX;
                spawnMaxX = lastSpawnMinX;
            }

            if (spawnMinZ > spawnMaxZ) {
                int lastSpawnMinZ = spawnMinZ;
                spawnMinZ = spawnMaxZ;
                spawnMaxZ = lastSpawnMinZ;
            }

            for (int x = spawnMinX; x <= spawnMaxX; x++) {
                for (int z = spawnMinZ; z <= spawnMaxZ; z++) {
                    Chunk chunk = spawnMin.toBukkitWorld().getChunkAt(x, z);
                    if (!chunk.isLoaded()) {
                        chunk.load();
                    }
                }
            }
        }

        for (Arena arena : this.plugin.getArenaManager().getArenas().values()) {
            if (!arena.isEnabled()) {
                continue;
            }

            int arenaMinX = arena.getMin().toBukkitLocation().getBlockX() >> 4;
            int arenaMinZ = arena.getMin().toBukkitLocation().getBlockZ() >> 4;
            int arenaMaxX = arena.getMax().toBukkitLocation().getBlockX() >> 4;
            int arenaMaxZ = arena.getMax().toBukkitLocation().getBlockZ() >> 4;

            if (arenaMinX > arenaMaxX) {
                int lastArenaMinX = arenaMinX;
                arenaMinX = arenaMaxX;
                arenaMaxX = lastArenaMinX;
            }

            if (arenaMinZ > arenaMaxZ) {
                int lastArenaMinZ = arenaMinZ;
                arenaMinZ = arenaMaxZ;
                arenaMaxZ = lastArenaMinZ;
            }

            for (int x = arenaMinX; x <= arenaMaxX; x++) {
                for (int z = arenaMinZ; z <= arenaMaxZ; z++) {
                    Chunk chunk = arena.getMin().toBukkitWorld().getChunkAt(x, z);
                    if (!chunk.isLoaded()) {
                        chunk.load();
                    }
                }
            }

            for (CopiedArena copiedArena : arena.getCopiedArenas()) {
                arenaMinX = copiedArena.getMin().toBukkitLocation().getBlockX() >> 4;
                arenaMinZ = copiedArena.getMin().toBukkitLocation().getBlockZ() >> 4;
                arenaMaxX = copiedArena.getMax().toBukkitLocation().getBlockX() >> 4;
                arenaMaxZ = copiedArena.getMax().toBukkitLocation().getBlockZ() >> 4;

                if (arenaMinX > arenaMaxX) {
                    int lastArenaMinX = arenaMinX;
                    arenaMinX = arenaMaxX;
                    arenaMaxX = lastArenaMinX;
                }

                if (arenaMinZ > arenaMaxZ) {
                    int lastArenaMinZ = arenaMinZ;
                    arenaMinZ = arenaMaxZ;
                    arenaMaxZ = lastArenaMinZ;
                }

                for (int x = arenaMinX; x <= arenaMaxX; x++) {
                    for (int z = arenaMinZ; z <= arenaMaxZ; z++) {
                        Chunk chunk = copiedArena.getMin().toBukkitWorld().getChunkAt(x, z);
                        if (!chunk.isLoaded()) {
                            chunk.load();
                        }
                    }
                }
            }
        }
    }
}
