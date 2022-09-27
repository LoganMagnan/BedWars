package rip.tilly.bedwars.menus.shop;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;

import java.util.Arrays;
import java.util.List;

@Getter
@RequiredArgsConstructor
public enum ShopType {

    QUICK("Quick Buy", Arrays.asList(
            " ",
            "&9Handy to buy things quickly!",
            " "
    ), Material.NETHER_STAR, 0, 0),

    BLOCKS("Blocks", Arrays.asList(
            " ",
            "&9Buy yourself some blocks to",
            "&9defend your bed and get to",
            "&9your enemies!",
            " "
    ), Material.STAINED_CLAY, 0, 2),

    ARMOR("Armor", Arrays.asList(
            " ",
            "&9Protect yourself with armor",
            "&9that stays after death!",
            " "
    ), Material.LEATHER_CHESTPLATE, 0, 3),

    TOOLS("Tools", Arrays.asList(
            " ",
            "&9Break the blocks of your enemies",
            "&9quicker and faster with tools!",
            " "
    ), Material.STONE_PICKAXE, 0, 4),

    WEAPONS("Weapons", Arrays.asList(
            " ",
            "&9Make killing enemies easier",
            "&9and quicker with stronger",
            "&9weapons!",
            " "
    ), Material.GOLD_SWORD, 0, 5),

    RANGED("Ranged", Arrays.asList(
            " ",
            "&9Don't like close quarter",
            "&9combat? Then the ranged",
            "&9weapons are for you!",
            " "
    ), Material.BOW, 0, 6),

    POTIONS("Potions", Arrays.asList(
            " ",
            "&9Sneak up on your enemies",
            "&9with special brewed potions!",
            " "
    ), Material.POTION, 0, 7),

    UTILITY("Utility", Arrays.asList(
            " ",
            "&9Need extra utilities to destroy",
            "&9your enemies? Then the utilities",
            "&9are for you!",
            " "
    ), Material.EGG, 0, 8);

    private final String name;
    private final List<String> lore;
    private final Material material;
    private final int data;
    private final int slot;

    public static ShopType getByName(String name) {
        return Arrays.stream(values()).filter(type -> type.name.equalsIgnoreCase(name)).findFirst().orElse(null);
    }
}
