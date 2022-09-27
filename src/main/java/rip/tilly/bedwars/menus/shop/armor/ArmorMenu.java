package rip.tilly.bedwars.menus.shop.armor;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import rip.tilly.bedwars.menus.shop.ShopType;
import rip.tilly.bedwars.menus.shop.ShopTypeButton;
import rip.tilly.bedwars.playerdata.currentgame.ArmorType;
import rip.tilly.bedwars.utils.CC;
import rip.tilly.bedwars.utils.ItemBuilder;
import rip.tilly.bedwars.utils.menu.Button;
import rip.tilly.bedwars.utils.menu.Menu;

import java.util.HashMap;
import java.util.Map;

public class ArmorMenu extends Menu {

    @Override
    public boolean isUpdateAfterClick() {
        return true;
    }

    @Override
    public String getTitle(Player player) {
        return CC.translate("&eClick armor to purchase...");
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        for (ShopType types : ShopType.values()) {
            buttons.put(types.getSlot(), new ShopTypeButton(types));
        }

        buttons.put(ShopType.ARMOR.getSlot() + 9, Button.placeholder(Material.STAINED_GLASS_PANE, (byte) 5, " "));

        buttons.put(19, new ArmorButton("Permanent Chain Armor", Material.CHAINMAIL_BOOTS, Material.IRON_INGOT, "Iron", ChatColor.WHITE, 40, ArmorType.CHAIN));
        buttons.put(20, new ArmorButton("Permanent Iron Armor", Material.IRON_BOOTS, Material.GOLD_INGOT, "Gold", ChatColor.GOLD, 12, ArmorType.IRON));
        buttons.put(21, new ArmorButton("Permanent Diamond Armor", Material.DIAMOND_BOOTS, Material.EMERALD, "Emeralds", ChatColor.DARK_GREEN, 6, ArmorType.DIAMOND));

        fillEmptySlots(buttons, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());

        return buttons;
    }

    @Override
    public int getSize() {
        return 6 * 9;
    }
}
