package rip.tilly.bedwars.managers.arena.chunk.data;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.server.v1_8_R3.ChunkSection;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This code has been created by
 * gatogamer#6666 A.K.A. gatogamer.
 * If you want to use my code, please
 * don't remove this messages and
 * give me the credits. Arigato! n.n
 */
@Getter
@Setter
public class BedWarsChunkData {

    public Map<BedWarsChunk, ChunkSection[]> chunks = new ConcurrentHashMap<>();

    public ChunkSection[] getBedWarsChunk(int x, int z) {
        for (Map.Entry<BedWarsChunk, ChunkSection[]> chunksEntry : chunks.entrySet()) {
            if (chunksEntry.getKey().getX() == x && chunksEntry.getKey().getZ() == z) {
                return chunksEntry.getValue();
            }
        }
        return null;
    }
}
