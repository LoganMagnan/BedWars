package rip.tilly.bedwars.managers.arena.chunk;

import net.minecraft.server.v1_8_R3.Chunk;
import net.minecraft.server.v1_8_R3.ChunkSection;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.CraftChunk;
import rip.tilly.bedwars.game.arena.CopiedArena;
import rip.tilly.bedwars.managers.arena.chunk.data.BedWarsChunk;
import rip.tilly.bedwars.managers.arena.chunk.data.BedWarsChunkData;
import rip.tilly.bedwars.managers.arena.chunk.data.BedWarsNMSUtil;
import rip.tilly.bedwars.utils.CC;
import rip.tilly.bedwars.utils.cuboid.Cuboid;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This code has been created by
 * gatogamer#6666 A.K.A. gatogamer.
 * If you want to use my code, please
 * don't remove this messages and
 * give me the credits. Arigato! n.n
 */
public class ChunkClearingManager {

    public Map<CopiedArena, BedWarsChunkData> chunks = new ConcurrentHashMap<>();

    public void copyArena(CopiedArena copiedArena) {
        long time = System.currentTimeMillis();

        Cuboid cuboid = new Cuboid(copiedArena.getMin().toBukkitLocation(), copiedArena.getMax().toBukkitLocation());
        BedWarsChunkData data = new BedWarsChunkData();

        cuboid.getChunks().forEach(chunk -> {
            chunk.load();
            Chunk nmsChunk = ((CraftChunk) chunk).getHandle();
            ChunkSection[] nmsSection = BedWarsNMSUtil.cloneSections(nmsChunk.getSections());
            data.chunks.put(new BedWarsChunk(chunk.getX(), chunk.getZ()), BedWarsNMSUtil.cloneSections(nmsSection));
        });

        chunks.put(copiedArena, data);

        Bukkit.getConsoleSender().sendMessage(CC.translate("&aArena copied! &a&l(" + (System.currentTimeMillis() - time) + "ms)"));
    }

    public void resetArena(CopiedArena copiedArena) {
        long time = System.currentTimeMillis();

        Cuboid cuboid = new Cuboid(copiedArena.getMin().toBukkitLocation(), copiedArena.getMax().toBukkitLocation());
        cuboid.getChunks().forEach(chunk -> {
            try {
                chunk.load();
                BedWarsNMSUtil.setSections(((CraftChunk) chunk).getHandle(), BedWarsNMSUtil.cloneSections(chunks.get(copiedArena).getBedWarsChunk(chunk.getX(), chunk.getZ())));
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });

        Bukkit.getConsoleSender().sendMessage(CC.translate("&aArena reset! &a&l(" + (System.currentTimeMillis() - time) + "ms)"));
    }
}
