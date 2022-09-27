package rip.tilly.bedwars.menus.shop.utilities;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import rip.tilly.bedwars.BedWars;
import rip.tilly.bedwars.menus.shop.ShopType;
import rip.tilly.bedwars.menus.shop.ShopTypeButton;
import rip.tilly.bedwars.utils.CC;
import rip.tilly.bedwars.utils.ItemBuilder;
import rip.tilly.bedwars.utils.menu.Button;
import rip.tilly.bedwars.utils.menu.Menu;
import java.util.HashMap;
import java.util.Map;

public class UtilityMenu extends Menu {

    private BedWars main = BedWars.getInstance();

    @Override
    public boolean isUpdateAfterClick() {
        return true;
    }

    @Override
    public String getTitle(Player player) {
        return CC.translate("&eClick a utility to purchase...");
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        for (ShopType types : ShopType.values()) {
            buttons.put(types.getSlot(), new ShopTypeButton(types));
        }

        buttons.put(ShopType.UTILITY.getSlot() + 9, Button.placeholder(Material.STAINED_GLASS_PANE, (byte) 5, ""));
        buttons.put(19, new UtilityButton("Golden Apple", new String[]{"Well rounded healing"}, Material.GOLDEN_APPLE, 0, 1, Material.GOLD_INGOT, "Gold", ChatColor.GOLD, 3, true));
        buttons.put(20, new UtilityButton("Bed Bug", new String[]{"Spawns a silverfish where the", "snowball lands to distract your", "enemies"}, Material.SNOW_BALL, 0, 1, Material.IRON_INGOT, "Iron", ChatColor.WHITE, 30, true));
        buttons.put(21, new UtilityButton("Dream Defender", new String[]{"Spawns an iron golem to help", "defend your base"}, Material.SNOW_BALL, 0, 1, Material.IRON_INGOT, "Iron", ChatColor.WHITE, 120, true));
        buttons.put(22, new UtilityButton("Fireball", new String[]{"Right click to throw a fireball", "at your enemies when they are currently", "walking on a bridge"}, Material.FIREBALL, 0, 1, Material.IRON_INGOT, "Iron", ChatColor.WHITE, 40, true));
        buttons.put(23, new UtilityButton("TNT", new String[]{"Instantly ignites and is ready", "to explode things"}, Material.TNT, 0, 1, Material.GOLD_INGOT, "Gold", ChatColor.GOLD, 4, true));
        buttons.put(24, new UtilityButton("Ender Pearl", new String[]{"The fastest way to get", "to an enemy's base"}, Material.ENDER_PEARL, 0, 1, Material.EMERALD, "Emerald", ChatColor.DARK_GREEN, 4, true));
        buttons.put(25, new UtilityButton("Water Bucket", new String[]{"Awesome for clutching", "and much more"}, Material.WATER_BUCKET, 0, 1, Material.GOLD_INGOT, "Gold", ChatColor.GOLD, 6, true));
        buttons.put(28, new UtilityButton("Bridge Egg", new String[]{"Right click to throw an egg", "to then make a bridge in its", "path"}, Material.EGG, 0, 1, Material.EMERALD, "Emerald", ChatColor.DARK_GREEN, 1, true));
        buttons.put(29, new UtilityButton("Magic Milk", new String[]{"Avoid setting off a trap", "for thirty seconds"}, Material.MILK_BUCKET, 0, 1, Material.GOLD_INGOT, "Gold", ChatColor.GOLD, 4, true));
        buttons.put(30, new UtilityButton("Sponge", new String[]{"Awesome for distracting", "your enemies"}, Material.SPONGE, 0, 4, Material.GOLD_INGOT, "Gold", ChatColor.GOLD, 6, true));
        buttons.put(31, new UtilityButton("Popup Tower", new String[]{"Right click to make a", "popup tower"}, Material.CHEST, 0, 1, Material.IRON_INGOT, "Iron", ChatColor.WHITE, 24, true));

        this.fillEmptySlots(buttons, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name("").build());

        return buttons;
    }

    @Override
    public int getSize() {
        return 54;
    }
}
