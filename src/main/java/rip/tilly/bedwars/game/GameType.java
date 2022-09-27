package rip.tilly.bedwars.game;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;

import java.util.Arrays;
import java.util.List;

@Getter
@RequiredArgsConstructor
public enum GameType {

    V1(10, "BedWars 1v1", Arrays.asList(" ", "&9You have to start somewhere,", "&9so why not start here?"),
            Material.WOOD_SWORD,
            0, 2),
    V2(12, "BedWars 2v2", Arrays.asList(" ", "&9Adapt, overcome and conquer,", "&9you are advancing rapidly"),
            Material.STONE_SWORD,
            0, 4),
    V3(14, "BedWars 3v3", Arrays.asList(" ", "&9How crazy is this,", "&9it seems like it was just yesterday", "&9since you started"),
            Material.IRON_SWORD,
            0, 6),
    V4(16, "BedWars 4v4", Arrays.asList(" ", "&9You are now a master,", "&9you can go up against", "&9the undefeated PvP bot &7(Coming soon)"),
            Material.DIAMOND_SWORD,
            0, 8);

    private final int slot;
    private final String name;
    private final List<String> lore;
    private final Material material;
    private final int data;
    private final int queueAmount;
}
