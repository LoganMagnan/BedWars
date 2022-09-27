package rip.tilly.bedwars.menus.settings;

import lombok.AllArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import rip.tilly.bedwars.BedWars;
import rip.tilly.bedwars.playerdata.PlayerSettings;
import rip.tilly.bedwars.utils.CC;
import rip.tilly.bedwars.utils.ItemBuilder;
import rip.tilly.bedwars.utils.menu.Button;
import rip.tilly.bedwars.utils.menu.Menu;

import java.util.*;

public class SettingsMenu extends Menu {

    @Override
    public String getTitle(Player player) {
        return CC.translate("&eSelect a setting to toggle...");
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        PlayerSettings settings = BedWars.getInstance().getPlayerDataManager().getPlayerData(player.getUniqueId()).getPlayerSettings();

        buttons.put(11, new SettingsButton(Material.PAINTING, 0,
                "&d&lToggle Scoreboard",
                Arrays.asList(
                        " ",
                        "&9If enabled, you will be",
                        "&9able to see the scoreboard",
                        " ",
                        (settings.isScoreboardEnabled() ? " &a► " : " &7► ") + "Scoreboard enabled.",
                        (!settings.isScoreboardEnabled() ? " &c► " : " &7► ") + "Scoreboard disabled."),
                "toggle scoreboard"));

        buttons.put(15, new SettingsButton(Material.BOOK_AND_QUILL, 0,
                "&d&lToggle Party Chat",
                Arrays.asList(
                        " ",
                        "&9If enabled, you will talk",
                        "&9in party chat",
                        " ",
                        (settings.isPartyChatEnabled() ? " &a► " : " &7► ") + "Party Chat enabled.",
                        (!settings.isPartyChatEnabled() ? " &c► " : " &7► ") + "Party Chat disabled."),
                "toggle partychat"));

        fillEmptySlots(buttons, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name(" ").build());

        return buttons;
    }

    @Override
    public int getSize() {
        return 3 * 9;
    }

    @AllArgsConstructor
    private class SettingsButton extends Button {

        private final Material material;
        private final int data;
        private final String name;
        private final List<String> lore;
        private final String command;

        @Override
        public ItemStack getButtonItem(Player player) {
            List<String> loreList = new ArrayList<>();

            loreList.addAll(lore);

            return new ItemBuilder(material)
                    .durability(data)
                    .name(name)
                    .lore(loreList)
                    .hideFlags()
                    .build();
        }

        @Override
        public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
            playNeutral(player);
            player.performCommand(command);
        }
    }
}
