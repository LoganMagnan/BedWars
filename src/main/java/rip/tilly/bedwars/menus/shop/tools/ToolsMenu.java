package rip.tilly.bedwars.menus.shop.tools;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import rip.tilly.bedwars.BedWars;
import rip.tilly.bedwars.menus.shop.ShopType;
import rip.tilly.bedwars.menus.shop.ShopTypeButton;
import rip.tilly.bedwars.playerdata.PlayerData;
import rip.tilly.bedwars.playerdata.currentgame.PlayerCurrentGameData;
import rip.tilly.bedwars.utils.CC;
import rip.tilly.bedwars.utils.ItemBuilder;
import rip.tilly.bedwars.utils.menu.Button;
import rip.tilly.bedwars.utils.menu.Menu;
import java.util.HashMap;
import java.util.Map;

public class ToolsMenu extends Menu {

    private BedWars main = BedWars.getInstance();

    @Override
    public boolean isUpdateAfterClick() {
        return true;
    }

    @Override
    public String getTitle(Player player) {
        return CC.translate("&eClick a tool to purchase...");
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        PlayerData playerData = this.main.getPlayerDataManager().getPlayerData(player.getUniqueId());

        PlayerCurrentGameData playerCurrentGameData = playerData.getCurrentGameData();

        for (ShopType types : ShopType.values()) {
            buttons.put(types.getSlot(), new ShopTypeButton(types));
        }

        buttons.put(ShopType.TOOLS.getSlot() + 9, Button.placeholder(Material.STAINED_GLASS_PANE, (byte) 5, ""));
        buttons.put(19, new ShearsButton("Shears", Material.SHEARS, Material.IRON_INGOT, "Iron", ChatColor.WHITE, 20, playerCurrentGameData));

        this.fillEmptySlots(buttons, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name("").build());

        return buttons;
    }

    @Override
    public int getSize() {
        return 54;
    }
}
