package rip.tilly.bedwars.menus.shop.weapons;

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

public class WeaponsMenu extends Menu {

    private BedWars main = BedWars.getInstance();

    @Override
    public boolean isUpdateAfterClick() {
        return true;
    }

    @Override
    public String getTitle(Player player) {
        return CC.translate("&eClick a weapon to purchase...");
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        for (ShopType types : ShopType.values()) {
            buttons.put(types.getSlot(), new ShopTypeButton(types));
        }

        buttons.put(ShopType.WEAPONS.getSlot() + 9, Button.placeholder(Material.STAINED_GLASS_PANE, (byte) 5, ""));
        buttons.put(19, new WeaponsButton("Stone Sword", Material.STONE_SWORD, 0, 1, Material.IRON_INGOT, "Iron", ChatColor.WHITE, 10, true));
        buttons.put(20, new WeaponsButton("Iron Sword", Material.IRON_SWORD, 0, 1, Material.GOLD_INGOT, "Gold", ChatColor.GOLD, 7, true));
        buttons.put(21, new WeaponsButton("Diamond Sword", Material.DIAMOND_SWORD, 0, 1, Material.EMERALD, "Emerald", ChatColor.DARK_GREEN, 3, true));
        buttons.put(22, new WeaponsButton("Knockback Stick", Material.STICK, 0, 1, Material.GOLD_INGOT, "Gold", ChatColor.GOLD, 5, true));

        this.fillEmptySlots(buttons, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name("").build());

        return buttons;
    }

    @Override
    public int getSize() {
        return 54;
    }
}
