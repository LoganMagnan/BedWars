package rip.tilly.bedwars.menus.shop.blocks;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import rip.tilly.bedwars.menus.shop.ShopButton;
import rip.tilly.bedwars.menus.shop.ShopType;
import rip.tilly.bedwars.menus.shop.ShopTypeButton;
import rip.tilly.bedwars.utils.CC;
import rip.tilly.bedwars.utils.ItemBuilder;
import rip.tilly.bedwars.utils.menu.Button;
import rip.tilly.bedwars.utils.menu.Menu;

import java.util.HashMap;
import java.util.Map;

public class BlocksMenu extends Menu {

    @Override
    public boolean isUpdateAfterClick() {
        return true;
    }

    @Override
    public String getTitle(Player player) {
        return CC.translate("&eClick a block to purchase...");
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        for (ShopType types : ShopType.values()) {
            buttons.put(types.getSlot(), new ShopTypeButton(types));
        }

        buttons.put(ShopType.BLOCKS.getSlot() + 9, Button.placeholder(Material.STAINED_GLASS_PANE, (byte) 5, " "));

        buttons.put(19, new ShopButton("Wool", Material.WOOL, 0, 16, Material.IRON_INGOT, "Iron", ChatColor.WHITE, 4, true));
        buttons.put(20, new ShopButton("Stained Clay", Material.STAINED_CLAY, 0, 16, Material.IRON_INGOT, "Iron", ChatColor.WHITE,12, true));
        buttons.put(21, new ShopButton("Blast-Proof Glass", Material.GLASS, 0, 4, Material.IRON_INGOT, "Iron", ChatColor.WHITE,12, true));
        buttons.put(22, new ShopButton("End Stone", Material.ENDER_STONE, 0, 12, Material.IRON_INGOT, "Iron", ChatColor.WHITE,24, false));
        buttons.put(23, new ShopButton("Ladder", Material.LADDER, 0, 8, Material.IRON_INGOT, "Iron", ChatColor.WHITE,4, false));
        buttons.put(24, new ShopButton("Wood", Material.WOOD, 0, 12, Material.GOLD_INGOT, "Gold", ChatColor.GOLD,4, false));
        buttons.put(25, new ShopButton("Obsidian", Material.OBSIDIAN, 0, 4, Material.EMERALD, "Emeralds", ChatColor.DARK_GREEN,4, false));

        fillEmptySlots(buttons, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());

        return buttons;
    }

    @Override
    public int getSize() {
        return 6 * 9;
    }
}
