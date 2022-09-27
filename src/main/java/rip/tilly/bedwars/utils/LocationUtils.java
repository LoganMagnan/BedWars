package rip.tilly.bedwars.utils;

import org.bukkit.Location;
import org.bukkit.block.Block;

import java.util.ArrayList;
import java.util.List;

public class LocationUtils {

    public static List<Block> getBlocks(Location center, int radius) {
        return getBlocks(center, radius, radius);
    }

    public static List<Block> getBlocks(Location center, int radius, int yRadius) {
        if (radius < 0) {
            return new ArrayList<>();
        }

        int iterations = radius * 2 + 1;

        List<Block> blocks = new ArrayList<>(iterations * iterations * iterations);

        for (int x = -radius; x <= radius; x++) {
            for (int y = -yRadius; y <= yRadius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    blocks.add(center.getBlock().getRelative(x, y, z));
                }
            }
        }

        return blocks;
    }
}
